package net.vc9ufi.cvitok.control;

import net.vc9ufi.geometry.Quaternion;
import net.vc9ufi.geometry.Vector3D;

public abstract class RotatingCamera extends Motion implements LookAt, Runnable{

    private float[] camera = new float[]{0, 5, 5};
    private float[] target = new float[]{0, 0, 0};
    private float[] up = new float[]{0, 0, 1};

    private static final float STEP_ANGLE = 0.003f;
    private static final float STEP_RADIUS = 0.01f;

    private float camera_fb;
    private float camera_lr;
    private float camera_r;

    boolean terminate = false;

    public static final int DELAY = 20;

    public RotatingCamera() {
        new Thread(this).start();
    }

    public synchronized void setAngle(float x, float y) {
        camera_fb -= y;
        camera_lr -= x;
    }


    @Override
    public void singleMove(float dx, float dy) {
        setAngle(dx, dy);
    }

    @Override
    public void multiMove(float dr, float dx, float dy) {
        camera_r -= dr;
    }

    @Override
    public void run() {
        Quaternion q;
        float[] orto;
        boolean work = false;
        while (!terminate) {
            float camera_fb_ = 0;
            float camera_lr_ = 0;
            float camera_r_ = 0;
            if (camera_fb != 0 || camera_lr != 0 || camera_r != 0) {
                work = true;
                camera_fb_ = camera_fb;
                camera_lr_ = camera_lr;
                camera_r_ = camera_r;
                camera_fb = 0;
                camera_lr = 0;
                camera_r = 0;
            }
            if (work) {
                camera_fb_ *= STEP_ANGLE;
                camera_lr_ *= STEP_ANGLE;
                camera_r_ *= STEP_RADIUS;

                orto = Vector3D.normalizeF(Vector3D.cross(up, camera));
                q = Quaternion.Product(
                        Quaternion.FromAxisAndAngle(up, camera_lr_),
                        Quaternion.FromAxisAndAngle(orto, camera_fb_));

                camera = Quaternion.Rotate(camera, q);
                up = Quaternion.Rotate(up, q);

                Vector3D.dLength(camera, camera_r_);
            }

            result(camera, target, up);
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

