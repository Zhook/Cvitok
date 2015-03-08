package net.vc9ufi.cvitok.data;

import com.google.gson.annotations.SerializedName;

import javax.microedition.khronos.opengles.GL10;

public class Light {
    @SerializedName("Position")
    public float[] position;
    @SerializedName("Direction")
    public float[] direction;
    @SerializedName("SpotCutoff")
    public float spotCutoff;
    @SerializedName("Ambient")
    public float[] ambient;
    @SerializedName("Diffuse")
    public float[] diffuse;
    @SerializedName("Specular")
    public float[] specular;

    public Light() {
        position = new float[]{3, 7, 10, 1};
        direction = new float[]{0, 0, -1};
        spotCutoff = 90f;
        ambient = new float[]{0.1f, 0.1f, 0.1f, 1};
        diffuse = new float[]{0.7f, 0.7f, 0.7f, 1};
        specular = new float[]{0.1f, 0.1f, 0.1f, 1};
    }


    public void lightOn(GL10 gl) {
        gl.glEnable(GL10.GL_LIGHT0);
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
