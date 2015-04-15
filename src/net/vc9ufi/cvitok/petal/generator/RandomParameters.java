package net.vc9ufi.cvitok.petal.generator;

import net.vc9ufi.cvitok.data.Parameters;

import java.security.InvalidParameterException;
import java.util.Random;

public class RandomParameters extends Parameters {

    private int minQuantityOfPetals = 3;
    private int maxQuantityOfPetals = 1;

    private int minRadiusOfPetal = 30;
    private int maxRadiusOfPetal = 100;

    private float minConvex = -0.5f;
    private float maxConvex = 1f;

    private int minRed = 0;
    private int maxRed = 100;
    private int minGreen = 0;
    private int maxGreen = 100;
    private int minBlue = 0;
    private int maxBlue = 100;
    private int minAlpha = 60;
    private int maxAlpha = 100;

    private double[] theta;


    public RandomParameters(String name, double[] theta) {
        super(name);
        this.theta = theta;
    }

    public void setRandomConvex(int min, int max) {
        this.minConvex = min * 0.05f;
        this.maxConvex = max * 0.05f;
    }

    public void setRandomQuantity(int min, int max) throws InvalidParameterException {
        if (min < 1) throw new InvalidParameterException("min Quantity of Petals < 1");
        if (max < 1) throw new InvalidParameterException("max Quantity of Petals < 1");

        this.minQuantityOfPetals = min;
        this.maxQuantityOfPetals = max;
    }

    public void setRandomSize(int min, int max) throws InvalidParameterException {
        if (min < 0) throw new InvalidParameterException("min Radius Of Petal < 0");
        if (max < 0) throw new InvalidParameterException("max Radius Of Petal < 0");

        this.minRadiusOfPetal = min;
        this.maxRadiusOfPetal = max;
    }

    public void setRandomColor(int min, int max) throws InvalidParameterException {
        if (min < 0) throw new InvalidParameterException("min Color < 0");
        if (max < 0) throw new InvalidParameterException("max Color < 0");
        if (min > 100) throw new InvalidParameterException("min Color > 100");
        if (max > 100) throw new InvalidParameterException("max Color > 100");

        this.minRed = min;
        this.maxRed = max;

        this.minGreen = min;
        this.maxGreen = max;

        this.minBlue = min;
        this.maxBlue = max;

        this.minAlpha = min;
        this.maxAlpha = max;
    }

    public void setRandomRed(int min, int max) throws InvalidParameterException {
        if (min < 0) throw new InvalidParameterException("min Color < 0");
        if (max < 0) throw new InvalidParameterException("max Color < 0");
        if (min > 100) throw new InvalidParameterException("min Color > 100");
        if (max > 100) throw new InvalidParameterException("max Color > 100");

        this.minRed = min;
        this.maxRed = max;
    }

    public void setRandomGreen(int min, int max) throws InvalidParameterException {
        if (min < 0) throw new InvalidParameterException("min Color < 0");
        if (max < 0) throw new InvalidParameterException("max Color < 0");
        if (min > 100) throw new InvalidParameterException("min Color > 100");
        if (max > 100) throw new InvalidParameterException("max Color > 100");

        this.minGreen = min;
        this.maxGreen = max;
    }

    public void setRandomBlue(int min, int max) throws InvalidParameterException {
        if (min < 0) throw new InvalidParameterException("min Color < 0");
        if (max < 0) throw new InvalidParameterException("max Color < 0");
        if (min > 100) throw new InvalidParameterException("min Color > 100");
        if (max > 100) throw new InvalidParameterException("max Color > 100");

        this.minBlue = min;
        this.maxBlue = max;
    }

    public void setRandomAlpha(int min, int max) throws InvalidParameterException {
        if (min < 0) throw new InvalidParameterException("min Color < 0");
        if (max < 0) throw new InvalidParameterException("max Color < 0");
        if (min > 100) throw new InvalidParameterException("min Color > 100");
        if (max > 100) throw new InvalidParameterException("max Color > 100");

        this.minAlpha = min;
        this.maxAlpha = max;
    }


    public void row() {
        rowQuantity();
        rowGeometry();
        rowConvex();
        rowColors();

    }


    private void rowQuantity() {
        quantity = minQuantityOfPetals;
        int delta = maxQuantityOfPetals - minQuantityOfPetals;
        if (delta > 0) {
            Random random = new Random();
            quantity += random.nextInt(delta);
        } else if (delta < 0) {
            Random random = new Random();
            quantity -= random.nextInt(-delta);
        }
        angle = (float) (2 * Math.PI / quantity);
    }

    private void rowGeometry() {
        ExtRandomFloat random = new ExtRandomFloat(minRadiusOfPetal, maxRadiusOfPetal);

        float radius;
        double phi;

        radius = random.getFloat() * 0.03f;
        phi = angle * random.nextDouble() / 2;
        left.coord.p1.p = getPoint(radius, theta[0], phi);

        radius = random.getFloat() * 0.03f;
        phi = angle * random.nextDouble() / 2;
        left.coord.p2.p = getPoint(radius, theta[1], phi);

        radius = random.getFloat() * 0.03f;
        phi = angle * random.nextDouble();
        left.coord.finish.p = getPoint(radius, theta[2], phi);


        radius = random.getFloat() * 0.03f;
        phi = angle / 2 + angle * random.nextDouble() / 2;
        right.coord.p1.p = getPoint(radius, theta[3], phi);

        radius = random.getFloat() * 0.03f;
        phi = angle + angle * random.nextDouble() / 2;
        right.coord.p2.p = getPoint(radius, theta[4], phi);

        right.coord.finish = left.coord.finish;
    }

    private void rowConvex() {
        convex = minConvex + (maxConvex - minConvex) * new Random().nextFloat();
    }

    private void rowColors() {
        left.colors.start = getColor();
        left.colors.p1 = getColor();
        left.colors.p1 = getColor();
        left.colors.finish = getColor();

        right.colors.start = getColor();
        right.colors.p1 = getColor();
        right.colors.p1 = getColor();
        right.colors.finish = left.colors.finish;
    }


    private float[] getColor() {
        float[] color = new float[4];
        ExtRandomFloat random = new ExtRandomFloat();

        color[0] = random.getFloat(minRed, maxRed) / 100;
        color[1] = random.getFloat(minGreen, maxGreen) / 100;
        color[2] = random.getFloat(minBlue, maxBlue) / 100;
        color[3] = random.getFloat(minAlpha, maxAlpha) / 100;

        return color;
    }

    private float[] getPoint(float radius, double theta, double phi) {
        float[] p = new float[3];

        p[0] = radius * (float) (Math.sin(theta) * Math.cos(phi));
        p[1] = radius * (float) (Math.sin(theta) * Math.sin(phi));
        p[2] = radius * (float) Math.cos(theta);

        return p;
    }
}
