package net.vc9ufi.cvitok.render;

import net.vc9ufi.geometry.Painter;

import javax.microedition.khronos.opengles.GL10;
import java.nio.FloatBuffer;

public class Pointers {

    private FloatBuffer mVertex;
    private FloatBuffer mColor;
    private FloatBuffer mNormal;

    private Painter mPainter;

    private int mSize = 0;

    public Pointers(FloatBuffer color, FloatBuffer normal, FloatBuffer vertex, int size, Painter painter) {
        this.mColor = color;
        this.mNormal = normal;
        this.mVertex = vertex;
        this.mSize = size;
        mPainter = painter;
    }

    public FloatBuffer getColor() {
        return mColor;
    }

    public FloatBuffer getNormal() {
        return mNormal;
    }

    public FloatBuffer getVertex() {
        return mVertex;
    }

    public int getSize() {
        return mSize;
    }

    public void paint(GL10 gl) {
        if (mPainter != null)
            mPainter.paint(gl, mVertex, mColor, mNormal, mSize);
    }
}
