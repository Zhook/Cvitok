package net.vc9ufi.cvitok.petal.generator;


import net.vc9ufi.cvitok.data.FlowerFile;
import net.vc9ufi.cvitok.data.Light;
import net.vc9ufi.cvitok.data.Parameters;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class RandomBuilder2 {
    private String name;
    private int ringsQuantity = 1;
    private int startPetalsQuantity = 0;
    private int sizePetalQuantity = 1;
    private float startPetalsRadious = 0;
    private float sizePetalsRadius = 1.5f;
    private int startAngleVertical = 0;
    private int sizeAngleVertical = 120;
    private float[] background = new float[]{1, 1, 1, 1};
    private Light light = new Light();



    public RandomBuilder2(String name) {
        this.name = name;
    }

    public RandomBuilder2 rowBackground(int start, int size) throws InvalidParameterException {
        background = getRandomColor(start, size);
        return this;
    }

    public RandomBuilder2 rowRingsQuantity(int start, int size) throws InvalidParameterException {
        ringsQuantity = getRandomQuantity(start, size);
        return this;
    }

    public RandomBuilder2 rowlight() {
        return this;
    }

    public RandomBuilder2 setPetalsQuantity(int start, int size) throws InvalidParameterException {
        if (start < 0) throw new InvalidParameterException("start must be >= 0");
        if (size < 1) throw new InvalidParameterException("size must be > 0");

        startPetalsQuantity = start;
        sizePetalQuantity = size;
        return this;
    }

    public RandomBuilder2 setRadius(float start, float size) throws InvalidParameterException {
        if (start < 0) throw new InvalidParameterException("start must be >= 0");
        if (size < 0) throw new InvalidParameterException("size must be >= 0");

        startPetalsRadious = start;
        sizePetalsRadius = size;
        return this;
    }

    public RandomBuilder2 setAngle(int start, int size) {
        startAngleVertical = start;
        sizeAngleVertical = size;
        return this;
    }


    public FlowerFile build() {
        FlowerFile flower = new FlowerFile();
        flower.background = background;
        flower.light = light;
        flower.name = name;
        flower.petals = row(
                startAngleVertical, sizeAngleVertical,
                ringsQuantity,
                startPetalsQuantity, sizePetalQuantity,
                startPetalsRadious, sizePetalsRadius);
        return flower;
    }


    private static double[][] rowVerticalSegments(int st, int sz, int quantity) {
        double start = st * Math.PI / 180;
        double size = sz * Math.PI / 180;
        double[] theta = new double[quantity * 5];
        Random random = new Random();

        for (int i = 0; i < theta.length; i++)
            theta[i] = start + size * random.nextDouble();

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


    private static ArrayList<Parameters> row(
            int startAngleVertical, int sizeAngleVertical,
            int ringsQuantity,
            int startPetalsInRing, int sizePetalsInRing,
            float startradius, float sizeRadius) {
        double[][] packAngles = rowVerticalSegments(startAngleVertical, sizeAngleVertical, ringsQuantity);

        ArrayList<Parameters> parameters = new ArrayList<>();

        RandomParameters p;
        for (int i = 0; i < ringsQuantity; i++) {
            p = new RandomParameters("Petal" + String.valueOf(i), packAngles[i]);
            p.setSizeOfPetal(startradius, sizeRadius);
            p.row();
            parameters.add(p);
        }


        return parameters;
    }

    public static int getRandomQuantity(int start, int size) throws InvalidParameterException {
        if (start < 0) throw new InvalidParameterException("start < 0");
        if (size < 0) throw new InvalidParameterException("size < 0");
        if (start + size < 1) throw new InvalidParameterException("start + size < 1");

        Random random = new Random();
        return start + random.nextInt(size);
    }

    public static float[] getRandomColor(int start, int size) throws InvalidParameterException {
        if (start < 0) throw new InvalidParameterException("start < 0");
        if (start > 255) throw new InvalidParameterException("start > 255");

        if (size < 1) throw new InvalidParameterException("size < 1");
        if (size + start > 256) throw new InvalidParameterException("size + start > 256");

        Random random = new Random();
        float[] color = new float[4];
        for (int i = 0; i < 4; i++)
            color[i] = (start + random.nextInt(size)) / 255f;

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
