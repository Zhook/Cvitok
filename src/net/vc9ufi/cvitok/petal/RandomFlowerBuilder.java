package net.vc9ufi.cvitok.petal;

import net.vc9ufi.cvitok.data.FlowerFile;
import net.vc9ufi.cvitok.data.Light;
import net.vc9ufi.cvitok.data.Parameters;
import net.vc9ufi.geometry.Vertex;

import java.util.ArrayList;
import java.util.Random;

public class RandomFlowerBuilder {

    FlowerFile flower;

    private Random random;

    public RandomFlowerBuilder(String name) {
        flower = new FlowerFile();

        flower.name = name;
        flower.petals = new ArrayList<>();
        flower.background = new float[]{1, 1, 1, 1};
        flower.light = new Light();

        random = new Random();
    }


    public RandomFlowerBuilder setBackground() {
        return setBackground(START_BACKGROUND, SIZE_BACKGROUND);
    }

    public RandomFlowerBuilder setBackground(int start, int size) {
        if ((start > 255) || (start < 0)) return this;
        if ((size < 0) || (size + start > 256)) return this;

        flower.background = getRandomColor(start, size);
        return this;
    }

    public static final int START_BACKGROUND = 200;
    public static final int SIZE_BACKGROUND = 56;

    public RandomFlowerBuilder addPetals() {
        int l = 1 + random.nextInt(4);
        for (int i = 0; i < l; i++) {
            addPetal();
        }
        return this;
    }

    public RandomFlowerBuilder addPetal() {
        Parameters p = new Parameters(PETALNAME + String.valueOf(flower.petals.size()));
        p.changeQuantity(random.nextInt(SIZE_QUANTITY) / 0.01f);
        p.setConvex(random.nextFloat() - 0.5f);

        Vertex start = new Vertex(0, 0, 0);
        Vertex finish = getRandomVertex(1);
        p.left = getRandomLine(start, finish);
        p.right = getRandomLine(start, finish);

        flower.petals.add(p);

        return this;
    }

    public static final String PETALNAME = "Petal";

    public static final int SIZE_QUANTITY = 5;


    public float[] getRandomColor(int start, int size) {
        if ((start > 255) || (start < 0)) return new float[]{1, 1, 1, 1};
        if ((size < 0) || (size + start > 256)) return new float[]{1, 1, 1, 1};

        float[] color = new float[4];
        for (int i = 0; i < 4; i++)
            color[i] = (start + random.nextInt(size)) / 255f;
        color[3] += 0.5f;
        return color;
    }

    public Vertex getRandomVertex(float size) {
        return new Vertex(
                2 * size * random.nextFloat() - size,
                2 * size * random.nextFloat() - size,
                2 * size * random.nextFloat() - size);
    }

    public Parameters.Line getRandomLine(Vertex start, Vertex finish) {
        Parameters.Line line;
        Parameters.Colors colors;
        Parameters.Coordinates coordinates;

        colors = new Parameters.Colors(
                getRandomColor(0, 255),
                getRandomColor(0, 255),
                getRandomColor(0, 255),
                getRandomColor(0, 255));

        coordinates = new Parameters.Coordinates(
                start,
                getRandomVertex(0.5f),
                getRandomVertex(0.5f),
                finish);


        line = new Parameters.Line(colors, coordinates);
        return line;
    }

    public FlowerFile build() {
        return flower;
    }
}
