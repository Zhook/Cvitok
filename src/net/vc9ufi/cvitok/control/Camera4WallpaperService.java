package net.vc9ufi.cvitok.control;


import net.vc9ufi.geometry.Quaternion;
import net.vc9ufi.geometry.Vector3D;

import java.security.InvalidParameterException;

public abstract class Camera4WallpaperService extends Motion implements LookAt, Runnable {
    private final float[] START_POINT;
    private final float[] TARGET;
    private final float[] UP;
    private static final float[] DEF_UP = new float[]{0, 0, 1};


    private float[] camera = new float[]{0, 5, 5};


    private static final float STEP_ANGLE = 0.001f;
    private static final float DELTA_PHI = (float) (Math.PI * 60 / 180);
    private static final float DELTA_THETA = (float) (Math.PI * 20 / 180);

    volatile private float camera_fb;
    volatile private float camera_lr;

    boolean terminate = false;

    public static final int DELAY = 20;

    public Camera4WallpaperService(float[] camera, float[] target, float[] up) {
        if (camera == null || camera.length != 3) throw new InvalidParameterException("camera.length!=3");
        START_POINT = camera;
        TARGET = target;
        UP = up;
        new Thread(this).start();
    }

    @Override
    public void singleMove(float dx, float dy) {
        setDy(dy);
    }

    public synchronized void setXOffset(float xOffset) {
        camera_lr = -DELTA_PHI * xOffset;
    }

    public synchronized void setDy(float dy) {
        camera_fb -= dy * STEP_ANGLE;
        if (camera_fb > DELTA_THETA) camera_fb = DELTA_THETA;
        if (camera_fb < -DELTA_THETA) camera_fb = -DELTA_THETA;
    }

    @Override
    public void run() {
        Quaternion q;
        float[] orto;

        float camera_lr_;
        float camera_fb_;
        while (!terminate) {
            camera_fb_ = camera_fb;
            camera_lr_ = camera_lr;

            orto = Vector3D.normalizeF(Vector3D.cross(DEF_UP, camera));
            q = Quaternion.Product(
                    Quaternion.FromAxisAndAngle(DEF_UP, camera_lr_),
                    Quaternion.FromAxisAndAngle(orto, camera_fb_));

            camera = Quaternion.Rotate(START_POINT, q);

            result(camera, TARGET, DEF_UP);
            wait(DELAY);
        }
    }

    private void wait(int t) {
        synchronized (Thread.currentThread()) {
            try {
                Thread.currentThread().wait(t);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
