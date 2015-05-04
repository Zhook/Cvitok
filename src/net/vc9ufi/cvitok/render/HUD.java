package net.vc9ufi.cvitok.render;


import javax.microedition.khronos.opengles.GL10;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Arrays;

public class HUD {


    private float[] hudcolor;
    private FloatBuffer hudcolorbuff;
    private FloatBuffer hudvertex;
    private int hudsize;

    private static FloatBuffer floatToBuffer(float[] array) {
        ByteBuffer bb = ByteBuffer.allocateDirect(array.length * 4);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer result = bb.asFloatBuffer();
        result.put(array);
        result.position(0);
        return result;
    }

    public void paintHUD(GL10 gl, float[] color, int size) {
        calkHUD(color, size);

        gl.glVertexPointer(2, GL10.GL_FLOAT, 0, hudvertex);
        gl.glColorPointer(4, GL10.GL_FLOAT, 0, hudcolorbuff);
        gl.glDrawArrays(GL10.GL_LINE_LOOP, 0, 4);
    }

    public static float[] invertColor(float[] color) {
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
