package net.vc9ufi.cvitok.petal;

import javax.microedition.khronos.opengles.GL10;
import java.nio.FloatBuffer;

public class Pointers {
    public Pointers(FloatBuffer color, FloatBuffer normal, FloatBuffer vertex, int size) {
        this.color = color;
        this.normal = normal;
        this.vertex = vertex;
        this.size = size;
    }

    FloatBuffer vertex;
    FloatBuffer color;
    FloatBuffer normal;

    int size;

    public void paint(GL10 gl) {
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertex);
        gl.glColorPointer(4, GL10.GL_FLOAT, 0, color);
        gl.glNormalPointer(GL10.GL_FLOAT, 0, normal);
        gl.glDrawArrays(GL10.GL_TRIANGLES, 0, size);
    }
}
