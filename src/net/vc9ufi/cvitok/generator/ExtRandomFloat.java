package net.vc9ufi.cvitok.generator;

import java.util.Random;

public class ExtRandomFloat extends Random {
    float min = 0;
    float max = 0;

    public ExtRandomFloat() {
    }

    public ExtRandomFloat(float min, float max) {
        this.max = max;
        this.min = min;
    }

    public float getFloat() {
        return min + (max - min) * nextFloat();
    }

    public float getFloat(float min, float max) {
        return min + (max - min) * nextFloat();
    }
}
