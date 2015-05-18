package net.vc9ufi.geometry.temp;

import net.vc9ufi.geometry.Vector3f;

public abstract class GlObject {
    public final int TAG;

    public GlObject(int TAG) {
        this.TAG = TAG;
    }

    public float calcRange(Vector3f point) {
        throw new AbstractMethodError();
    }
}
