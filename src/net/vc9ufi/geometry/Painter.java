package net.vc9ufi.geometry;

import javax.microedition.khronos.opengles.GL10;
import java.nio.FloatBuffer;

public interface Painter {

    void paint(GL10 gl, FloatBuffer vertex, FloatBuffer color, FloatBuffer normal, int size);
}
