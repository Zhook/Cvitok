package net.vc9ufi.cvitok.generator;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import net.vc9ufi.cvitok.R;
import net.vc9ufi.cvitok.data.FlowerFile;
import net.vc9ufi.cvitok.data.Light;
import net.vc9ufi.cvitok.data.Parameters;
import net.vc9ufi.cvitok.views.settings.dialogs.PreferenceDialogRangeSeekBar;
import net.vc9ufi.cvitok.views.settings.dialogs.PreferenceRangeColorPicker;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class FlowerGenerator {

    private Context context;

    public FlowerGenerator(Context context) {
        this.context = context;
    }

    public FlowerFile generate() {
        FlowerFile flower = new FlowerFile();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        flower.background = rowBackground(sharedPreferences);
        flower.light = new Light();
        flower.petals = row(sharedPreferences);

        return flower;
    }


    private static double[][] rowVerticalSegments(int min, int max, int quantity) {
        double minTheta = min * Math.PI / 180;
        double maxTheta = max * Math.PI / 180;
        double[] theta = new double[quantity * 5];
        Random random = new Random();

        for (int i = 0; i < theta.length; i++)
            theta[i] = minTheta + (maxTheta - minTheta) * random.nextDouble();

        Arrays.sort(theta);

        double[][] result = new double[quantity][5];
        for (int i = 0; i < quantity; i++) {
            result[i][0] = theta[i * 5];
            result[i][1] = theta[i * 5 + 1];
            result[i][2] = theta[i * 5 + 2];
            result[i][3] = theta[i * 5 + 3];
            result[i][4] = theta[i * 5 + 4];
            shuffleArray(result[i]);
        }

        return result;
    }

    public float[] rowBackground(SharedPreferences sharedPreferences) {
        int[] minColor = new int[]{90, 90, 90};
        minColor = PreferenceRangeColorPicker.getMinColor(context, sharedPreferences, R.string.prefkey_background_color, minColor);
        int[] maxColor = new int[]{99, 99, 99};
        maxColor = PreferenceRangeColorPicker.getMaxColor(context, sharedPreferences, R.string.prefkey_background_color, maxColor);
        return getRandomColor(minColor, maxColor);
    }

    public int rowCirclesQuantity(SharedPreferences sharedPreferences) {
        int min = PreferenceDialogRangeSeekBar.getFirstValue(context, sharedPreferences, R.string.prefkey_generator_circles, 4);
        int max = PreferenceDialogRangeSeekBar.getSecondValue(context, sharedPreferences, R.string.prefkey_generator_circles, 7);
        return getRandomQuantity(min, max);
    }

    private ArrayList<Parameters> row(SharedPreferences sharedPreferences) {
        int circles = rowCirclesQuantity(sharedPreferences);
        int minTheta = PreferenceDialogRangeSeekBar.getFirstValue(context, sharedPreferences, R.string.prefkey_generator_theta, 10);
        int maxTheta = PreferenceDialogRangeSeekBar.getSecondValue(context, sharedPreferences, R.string.prefkey_generator_theta, 120);
        int minRadius = PreferenceDialogRangeSeekBar.getFirstValue(context, sharedPreferences, R.string.prefkey_generator_radius_of_petals, 20);
        int maxRadius = PreferenceDialogRangeSeekBar.getSecondValue(context, sharedPreferences, R.string.prefkey_generator_radius_of_petals, 60);
        int minQuantity = PreferenceDialogRangeSeekBar.getFirstValue(context, sharedPreferences, R.string.prefkey_generator_petals_in_circles, 6);
        int maxQuantity = PreferenceDialogRangeSeekBar.getSecondValue(context, sharedPreferences, R.string.prefkey_generator_petals_in_circles, 11);
        int minConvex = PreferenceDialogRangeSeekBar.getFirstValue(context, sharedPreferences, R.string.prefkey_generator_petals_convex, -30);
        int maxConvex = PreferenceDialogRangeSeekBar.getSecondValue(context, sharedPreferences, R.string.prefkey_generator_petals_convex, 30);


        int[] minColor = new int[]{80, 50, 10};
        minColor = PreferenceRangeColorPicker.getMinColor(context, sharedPreferences, R.string.prefkey_generator_petals_color, minColor);
        int[] maxColor = new int[]{100, 70, 30};
        maxColor = PreferenceRangeColorPicker.getMaxColor(context, sharedPreferences, R.string.prefkey_generator_petals_color, maxColor);

        int minTransparency = PreferenceDialogRangeSeekBar.getFirstValue(context, sharedPreferences, R.string.prefkey_generator_petals_transparency, 0);
        int maxTransparency = PreferenceDialogRangeSeekBar.getSecondValue(context, sharedPreferences, R.string.prefkey_generator_petals_transparency, 50);
        int minPortionTransparency = PreferenceDialogRangeSeekBar.getFirstValue(context, sharedPreferences, R.string.prefkey_generator_portion_transparency_petal, 0);
        int maxPortionTransparency = PreferenceDialogRangeSeekBar.getSecondValue(context, sharedPreferences, R.string.prefkey_generator_portion_transparency_petal, 50);
        int minQuantityTransparency = PreferenceDialogRangeSeekBar.getFirstValue(context, sharedPreferences, R.string.prefkey_generator_quantity_transparency_petals, 0);
        int maxQuantityTransparency = PreferenceDialogRangeSeekBar.getSecondValue(context, sharedPreferences, R.string.prefkey_generator_quantity_transparency_petals, 50);
        double[][] packAngles = rowVerticalSegments(minTheta, maxTheta, circles);

        ArrayList<Parameters> parameters = new ArrayList<>();
        ExtRandomFloat random = new ExtRandomFloat(minQuantityTransparency, maxQuantityTransparency);
        float QuantityTransparency = random.getFloat() / 100;
        RandomParameters p;
        for (int i = 0; i < circles; i++) {
            p = new RandomParameters(packAngles[i]);
            p.setRandomSize(minRadius, maxRadius);
            p.setRandomConvex(minConvex, maxConvex);
            p.setRandomQuantity(minQuantity, maxQuantity);
            p.setRandomRed(minColor[0], maxColor[0]);
            p.setRandomGreen(minColor[1], maxColor[1]);
            p.setRandomBlue(minColor[2], maxColor[2]);
            if (random.nextFloat() < QuantityTransparency) {
                p.setRandomAlpha(100 - minTransparency, 100 - maxTransparency);
                p.setPortionTransparency(minPortionTransparency, maxPortionTransparency);
            }
            p.row();
            parameters.add(p);
        }

        return parameters;
    }


    public static int getRandomQuantity(int min, int max) throws InvalidParameterException {
        if (min < 1) throw new InvalidParameterException("min Quantity of Petals < 1");
        if (max < 1) throw new InvalidParameterException("max Quantity of Petals < 1");

        int quantity = min;
        int delta = max - min;
        if (delta > 0) {
            Random random = new Random();
            quantity += random.nextInt(delta);
        } else if (delta < 0) {
            Random random = new Random();
            quantity -= random.nextInt(-delta);
        }

        return quantity;
    }

    private static float[] getRandomColor(int[] minColor, int[] maxColor) {
        ExtRandomFloat random = new ExtRandomFloat();
        float[] color = new float[4];

        color[0] = random.getFloat(minColor[0], maxColor[0]) / 100;
        color[1] = random.getFloat(minColor[1], maxColor[1]) / 100;
        color[2] = random.getFloat(minColor[2], maxColor[2]) / 100;
        color[3] = 0;

        return color;
    }

    public static void shuffleArray(double[] array) {
        Random random = new Random();
        double temp;
        int i = array.length - 1;
        while (i > 0) {
            int index = random.nextInt(i + 1);
            temp = array[index];
            array[index] = array[i];
            array[i] = temp;
            i--;
        }
    }
}
