package net.vc9ufi.cvitok.dialogs.colordialog;

import android.opengl.GLES20;
import net.vc9ufi.geometry.Icosahedron;

import javax.microedition.khronos.opengles.GL10;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Arrays;

public class ColorMatrix {

    int quantity = 0;                   // = 20*(4^QUALITY)
    float[] vertex;                      //{x0,y0,z0,x1,y1,z1...}
    public FloatBuffer buffervertex;
    float[] color;                       //{r0,g0,b0,r1,g1,b1...}
    public FloatBuffer buffercolor;
    float[] normal;                      //{x0,y0,z0,x1,y1,z1...}
    public FloatBuffer buffernormal;
    boolean init = true;
    float[] hudcolor;
    FloatBuffer hudcolorbuff;
    FloatBuffer hudvertex;
    int hudsize;


    ArrayList<Icosahedron.Trian> trians = new ArrayList<>();


    public ColorMatrix(float radius, int quality) {
        quantity = Icosahedron.CalcIcosahedron(trians, radius, quality);

        vertex = new float[9 * quantity];
        color = new float[12 * quantity];
        normal = new float[9 * quantity];

        int i = 0;
        for (Icosahedron.Trian t : trians) {
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++) {
                    vertex[9 * i + 3 * j + k] = t.v[j][k];
                    normal[9 * i + 3 * j + k] = t.n[j][k];
                    color[12 * i + 4 * j + k] = 0.5f + t.n[j][k];
                }
                color[12 * i + 4 * j + 3] = 1.0f;
            }
            i++;
        }

        trians = null;

        buffervertex = floatToBuffer(vertex);
        buffercolor = floatToBuffer(color);
        buffernormal = floatToBuffer(normal);

        init = false;
    }

    public void paint(GL10 gl) {
        if (!init) {
            gl.glMaterialfv(GL10.GL_FRONT, GL10.GL_AMBIENT, new float[]{0.8f, 0.8f, 0.8f, 1}, 0);
            gl.glMaterialfv(GL10.GL_FRONT, GL10.GL_DIFFUSE, new float[]{0.8f, 0.8f, 0.8f, 1}, 0);
            gl.glMaterialfv(GL10.GL_FRONT, GL10.GL_SPECULAR, new float[]{0.2f, 0.2f, 0.2f, 1}, 0);
            gl.glVertexPointer(3, GL10.GL_FLOAT, 0, buffervertex);
            gl.glColorPointer(4, GL10.GL_FLOAT, 0, buffercolor);
            gl.glNormalPointer(GL10.GL_FLOAT, 0, buffernormal);
            gl.glDrawArrays(GL10.GL_TRIANGLES, 0, 3 * quantity);
        }
    }

    public void paintHUD(GL10 gl, float[] color, int size) {
        calkHUD(color, size);

        gl.glVertexPointer(2, GL10.GL_FLOAT, 0, hudvertex);
        gl.glColorPointer(4, GL10.GL_FLOAT, 0, hudcolorbuff);
        gl.glDrawArrays(GL10.GL_LINE_LOOP, 0, 4);
    }

    public static FloatBuffer floatToBuffer(float[] array) {
        ByteBuffer bb = ByteBuffer.allocateDirect(array.length * 4);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer result = bb.asFloatBuffer();
        result.put(array);
        result.position(0);
        return result;
    }

    public float[] getColor(GL10 gl, int x, int y) {
        ByteBuffer pixel = ByteBuffer.allocate(4);
        pixel.order(ByteOrder.nativeOrder());
        pixel.position(0);
        gl.glReadPixels(x, y, 1, 1, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, pixel);

        float[] c = new float[4];
        for (int i = 0; i < 4; i++) {
            c[i] = (pixel.get() & 0xFF) / 255.0f;
        }
        return c;
    }

    public static float[] invertColor(float[] color){
        float[] result = new float[4];
        result[0] = 1.0f - color[0];
        result[1] = 1.0f - color[1];
        result[2] = 1.0f - color[2];
        result[3] = color[3];

        return result;
    }

    private void calkHUD(float[] color, int size) {
        //color = new float[]{0, 0, 0, 1};
        if (!Arrays.equals(color, hudcolor)) {
            hudcolor = color;

            float[] c = new float[16];
            for (int i = 0; i < 4; i++) {
                System.arraycopy(color, 0, c, 4 * i, 4);
            }
            hudcolorbuff = floatToBuffer(c);
        }
        if (hudsize != size) {
            hudsize = size;
            hudvertex = floatToBuffer(new float[]{
                    -hudsize, 0,
                    0, hudsize,
                    hudsize, 0,
                    0, -hudsize});
        }

    }
}
