package net.vc9ufi.cvitok.views.dialogs.colordialog;

import net.vc9ufi.cvitok.control.LookAt;
import net.vc9ufi.cvitok.data.Light;
import net.vc9ufi.cvitok.views.render.ImplRenderer;

import javax.microedition.khronos.opengles.GL10;

public class ColorDialogRenderer extends ImplRenderer {
    private ColorMatrix sphere;
    final static float[] BACKGROUND = {0.9f, 1.0f, 0.9f, 0.5f};
    private float[] background = BACKGROUND;


    public ColorDialogRenderer(LookAt camera) {
        super(camera);
        sphere = new ColorMatrix(1.5f, 4);
    }

    public float[] getColor() {
        return background;
    }

    @Override
    public void paint(GL10 gl) {
        sphere.paint(gl);

        background = sphere.getColor(gl, super.getWidth() / 2, super.getHeight() / 2);
        super.setupOrtho(gl);
        sphere.paintHUD(gl, ColorMatrix.invertColor(background), super.getWidth() / 20);
    }

    @Override
    public float[] background() {
        return background;
    }

    @Override
    public Light light() {
        return null;
    }
}
