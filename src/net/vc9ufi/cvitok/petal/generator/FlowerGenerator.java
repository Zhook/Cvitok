package net.vc9ufi.cvitok.petal.generator;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import net.vc9ufi.cvitok.R;
import net.vc9ufi.cvitok.data.FlowerFile;
import net.vc9ufi.cvitok.data.Light;
import net.vc9ufi.cvitok.data.Parameters;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class FlowerGenerator {

    Context context;

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
        int min = sharedPreferences.getInt(context.getString(R.string.generator_key_min_background_brightness), 60);
        int max = sharedPreferences.getInt(context.getString(R.string.generator_key_max_background_brightness), 100);
        return getRandomColor(min, max);
    }

    public int rowCirclesQuantity(SharedPreferences sharedPreferences) {
        int min = sharedPreferences.getInt(context.getString(R.string.generator_key_min_circles), 3);
        int max = sharedPreferences.getInt(context.getString(R.string.generator_key_max_circles), 6);
        return getRandomQuantity(min, max);
    }

    private ArrayList<Parameters> row(SharedPreferences sharedPreferences) {
        int circles = rowCirclesQuantity(sharedPreferences);
        int minTheta = sharedPreferences.getInt(context.getString(R.string.generator_key_min_theta), 0);
        int maxTheta = sharedPreferences.getInt(context.getString(R.string.generator_key_max_theta), 120);
        int minRadius = sharedPreferences.getInt(context.getString(R.string.generator_key_min_radius_of_petals), 30);
        int maxRadius = sharedPreferences.getInt(context.getString(R.string.generator_key_max_radius_of_petals), 100);
        int minQuantity = sharedPreferences.getInt(context.getString(R.string.generator_key_min_petals_in_circles), 3);
        int maxQuantity = sharedPreferences.getInt(context.getString(R.string.generator_key_max_petals_in_circles), 12);
        int minConvex = sharedPreferences.getInt(context.getString(R.string.generator_key_min_petals_convex), 3);
        int maxConvex = sharedPreferences.getInt(context.getString(R.string.generator_key_max_petals_convex), 12);
        double[][] packAngles = rowVerticalSegments(minTheta, maxTheta, circles);

        ArrayList<Parameters> parameters = new ArrayList<>();

        RandomParameters p;
        for (int i = 0; i < circles; i++) {
            p = new RandomParameters(packAngles[i]);
            p.setRandomSize(minRadius, maxRadius);
            p.setRandomConvex(minConvex, maxConvex);
            p.setRandomQuantity(minQuantity, maxQuantity);
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

    public static float[] getRandomColor(int min, int max) throws InvalidParameterException {
        if (min < 0) throw new InvalidParameterException("min Color < 0");
        if (max < 0) throw new InvalidParameterException("max Color < 0");
        if (min > 100) throw new InvalidParameterException("min Color > 100");
        if (max > 100) throw new InvalidParameterException("max Color > 100");

        ExtRandomFloat random = new ExtRandomFloat();
        float[] color = new float[4];
        for (int i = 0; i < 4; i++)
            color[i] = random.getFloat(min, max) / 100;

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
