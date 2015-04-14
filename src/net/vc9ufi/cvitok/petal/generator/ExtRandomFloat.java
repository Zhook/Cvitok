package net.vc9ufi.cvitok.petal.generator;

import java.util.Random;

public class ExtRandomFloat extends Random {
    private final float START;
    private final float DELTA;

    public ExtRandomFloat(float start, float delta) {
        this.DELTA = delta;
        this.START = start;
    }

    public float getFloat() {
        return START + DELTA * nextFloat();
    }
}
