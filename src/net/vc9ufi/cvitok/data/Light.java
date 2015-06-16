package net.vc9ufi.cvitok.data;

import android.view.View;
import com.google.gson.annotations.SerializedName;
import net.vc9ufi.cvitok.control.Motion;

import javax.microedition.khronos.opengles.GL10;

public class Light {
    @SerializedName("Position")
    public volatile float[] position;
    @SerializedName("Direction")
    public volatile float[] direction;
    @SerializedName("SpotCutoff")
    public volatile float spotCutoff;
    @SerializedName("Ambient")
    public volatile float[] ambient;
    @SerializedName("Diffuse")
    public volatile float[] diffuse;
    @SerializedName("Specular")
    public volatile float[] specular;

    private View.OnTouchListener mOnTouchListener;

    public Light() {
        position = new float[]{0, 6, 10, 1};
        direction = new float[]{0, 0, -1};
        spotCutoff = 180f;
        ambient = new float[]{0.3f, 0.3f, 0.3f, 1};
        diffuse = new float[]{0.7f, 0.7f, 0.7f, 1};
        specular = new float[]{0.1f, 0.1f, 0.1f, 1};

        mOnTouchListener = new Motion() {

            @Override
            public void singleMove(float dx, float dy) {
                single(dx, dy);
            }

            @Override
            public void multiMove(float dr, float dx, float dy) {
                multi(dr);
            }
        };
    }

    public View.OnTouchListener getOnTouchListener() {
        return mOnTouchListener;
    }

    public void lightOn(GL10 gl) {
        gl.glEnable(GL10.GL_LIGHT0);
        //gl.glLightModelf(GL10.GL_LIGHT_MODEL_TWO_SIDE, 1f);
        gl.glLightModelf(GL10.GL_LIGHT_MODEL_TWO_SIDE, GL10.GL_TRUE);
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, position, 0);
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_SPOT_DIRECTION, direction, 0);
        gl.glLightf(GL10.GL_LIGHT0, GL10.GL_SPOT_CUTOFF, spotCutoff);

        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, ambient, 0);
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, diffuse, 0);
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_SPECULAR, specular, 0);
    }

    public void single(float dx, float dy) {
        position[1] += dx;
        position[2] += dy;
    }

    public void multi(float dr) {
        spotCutoff += dr;
    }
}
