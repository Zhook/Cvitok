package net.vc9ufi.geometry.temp;


import net.vc9ufi.geometry.Vector3f;

import javax.microedition.khronos.opengles.GL10;

public abstract class GlObject {

    void draw(GL10 gl){

    }

    public final int TAG;

    public GlObject(int TAG) {
        this.TAG = TAG;
    }

    public float calcRange(Vector3f point) {
        throw new AbstractMethodError();
    }
}
