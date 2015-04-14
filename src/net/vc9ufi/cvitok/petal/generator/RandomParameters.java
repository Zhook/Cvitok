package net.vc9ufi.cvitok.petal.generator;

import net.vc9ufi.cvitok.data.Parameters;

import java.security.InvalidParameterException;
import java.util.Random;

public class RandomParameters extends Parameters {

    private int startQuantityOfPetals = 3;
    private int deltaQuantityOfPetals = 1;

    private float startRadiusOfPetal = 0;
    private float deltaRadiusOfPetal = 1;

    private float startConvex = -0.5f;
    private float deltaConvex = 1f;

    private int startRed = 0;
    private int deltaRed = 255;
    private int startGreen = 0;
    private int deltaGreen = 255;
    private int startBlue = 0;
    private int deltaBlue = 255;
    private int startAlpha = 150;
    private int deltaAlpha = 255;

    private double[] theta;


    public RandomParameters(String name, double[] theta) {
        super(name);
        this.theta = theta;
    }

    public void setSizeOfConvex(float startConvex, float deltaConvex) {
        this.startConvex = startConvex;
        this.deltaConvex = deltaConvex;
    }

    public void setQuantityOfPetal(int startQuantityOfPetals, int deltaQuantityOfPetals) throws InvalidParameterException {
        if (startQuantityOfPetals < 1) throw new InvalidParameterException("start Quantity of Petals < 1");
        if (deltaQuantityOfPetals < 0) throw new InvalidParameterException("delta Quantity of Petals < 0");

        this.startQuantityOfPetals = startQuantityOfPetals;
        this.deltaQuantityOfPetals = deltaQuantityOfPetals;
    }

    public void setSizeOfPetal(float startRadiusOfPetal, float deltaRadiusOfPetal) throws InvalidParameterException {
        if (startRadiusOfPetal < 0) throw new InvalidParameterException("start Radius Of Petal < 0");
        if (deltaRadiusOfPetal < 0) throw new InvalidParameterException("delta Radius Of Petal < 0");

        this.startRadiusOfPetal = startRadiusOfPetal;
        this.deltaRadiusOfPetal = deltaRadiusOfPetal;
    }

    public void setColor(int start, int delta) throws InvalidParameterException {
        if (start < 0) throw new InvalidParameterException("start Color < 0");
        if (start > 255) throw new InvalidParameterException("start Color > 255");

        if (delta < 1) throw new InvalidParameterException("delta Color < 1");
        if (delta + start > 255) throw new InvalidParameterException("start + delta Color > 255");

        this.startRed = start;
        this.deltaRed = delta;

        this.startGreen = start;
        this.deltaGreen = delta;

        this.startBlue = start;
        this.deltaBlue = delta;

        this.startAlpha = start;
        this.deltaAlpha = delta;
    }

    public void setRed(int start, int delta) throws InvalidParameterException {
        if (start < 0) throw new InvalidParameterException("start Red < 0");
        if (start > 255) throw new InvalidParameterException("start Red > 255");

        if (delta < 1) throw new InvalidParameterException("delta Red < 1");
        if (delta + start > 255) throw new InvalidParameterException("start + delta Red > 255");

        this.startRed = start;
        this.deltaRed = delta;
    }

    public void setGreen(int start, int delta) throws InvalidParameterException {
        if (start < 0) throw new InvalidParameterException("start Green < 0");
        if (start > 255) throw new InvalidParameterException("start Green > 255");

        if (delta < 1) throw new InvalidParameterException("delta Green < 1");
        if (delta + start > 255) throw new InvalidParameterException("start + delta Green > 255");

        this.startGreen = start;
        this.deltaGreen = delta;
    }

    public void setBlue(int start, int delta) throws InvalidParameterException {
        if (start < 0) throw new InvalidParameterException("start Blue < 0");
        if (start > 255) throw new InvalidParameterException("start Blue > 255");

        if (delta < 1) throw new InvalidParameterException("delta Blue < 1");
        if (delta + start > 255) throw new InvalidParameterException("start + delta Blue > 255");

        this.startBlue = start;
        this.deltaBlue = delta;
    }

    public void setAlpha(int start, int delta) throws InvalidParameterException {
        if (start < 0) throw new InvalidParameterException("start Alpha < 0");
        if (start > 255) throw new InvalidParameterException("start Alpha > 255");

        if (delta < 1) throw new InvalidParameterException("delta Alpha < 1");
        if (delta + start > 255) throw new InvalidParameterException("start + delta Alpha > 255");

        this.startAlpha = start;
        this.deltaAlpha = delta;
    }


    public void row() {
        rowQuantity();
        rowGeometry();
        rowConvex();
        rowColors();

    }


    private void rowQuantity() {
        Random random = new Random();
        super.quantity = startQuantityOfPetals + random.nextInt(deltaQuantityOfPetals + 1);
        super.angle = (float) (2 * Math.PI / quantity);
    }

    private void rowGeometry() {
        ExtRandomFloat random = new ExtRandomFloat(startRadiusOfPetal, deltaRadiusOfPetal);
        double maxAngle = Math.PI / quantity;

        float radius;
        double phi;

        radius = random.getFloat();
        phi = maxAngle * random.nextDouble();
        left.coord.p1.p = getPoint(radius, theta[0], phi);

        radius = random.getFloat();
        phi = maxAngle * random.nextDouble();
        left.coord.p2.p = getPoint(radius, theta[1], phi);

        radius = random.getFloat();
        phi = 2 * maxAngle * random.nextDouble();
        left.coord.finish.p = getPoint(radius, theta[2], phi);


        radius = random.getFloat();
        phi = maxAngle + maxAngle * random.nextDouble();
        right.coord.p1.p = getPoint(radius, theta[3], phi);

        radius = random.getFloat();
        phi = maxAngle + maxAngle * random.nextDouble();
        right.coord.p2.p = getPoint(radius, theta[4], phi);

        right.coord.finish = left.coord.finish;
    }

    private void rowConvex() {
        setConvex(startConvex + deltaConvex * new Random().nextFloat());
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
        Random random = new Random();

        color[0] = (startRed + random.nextInt(deltaRed + 1)) / 255f;
        color[1] = (startGreen + random.nextInt(deltaGreen + 1)) / 255f;
        color[2] = (startBlue + random.nextInt(deltaBlue + 1)) / 255f;
        color[3] = (startAlpha + random.nextInt(deltaAlpha + 1)) / 255f;

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
