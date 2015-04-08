package net.vc9ufi.cvitok.control;

import net.vc9ufi.geometry.Quaternion;
import net.vc9ufi.geometry.Vector3D;

public class LookAt extends Motion {

    //
    private float[] camera = new float[]{0, 5, 5};
    private float[] target = new float[]{0, 0, 0};
    private float[] up = new float[]{0, 0, 1};
    private float[] camera_ = new float[]{0, 5, 5};
    private float[] up_ = new float[]{0, 0, 1};
    //
    private static final float step_angle = 0.003f;
    private static final float step_radius = 0.01f;
    //
    private volatile float camera_fb;
    private volatile float camera_lr;
    private volatile float camera_r;

    private float x = 0;
    private float y = 0;
    private float r = 0;

    boolean terminate = false;


    public LookAt() {
        initThread();
    }

    private void initThread() {
        new Thread(new Runnable() {
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
                        camera_fb_ *= step_angle;
                        camera_lr_ *= step_angle;
                        camera_r_ *= step_radius;

                        orto = Vector3D.normalizeF(Vector3D.cross(up_, camera_));
                        q = Quaternion.Product(
                                Quaternion.FromAxisAndAngle(up_, camera_lr_),
                                Quaternion.FromAxisAndAngle(orto, camera_fb_));

                        camera_ = Quaternion.Rotate(camera_, q);
                        up_ = Quaternion.Rotate(up_, q);

                        Vector3D.dLength(camera_, camera_r_);
                    }
                    synchronized (this) {
                        camera = camera_;
                        up = up_;
                        try {
                            this.wait(20);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }

    public synchronized float[] getCameraTargetUp() {
        return new float[]{
                camera[0], camera[1], camera[2],
                0, 0, 0,
                up[0], up[1], up[2]};
    }

    public synchronized float[] getCamera() {
        return camera;
    }

    public synchronized float[] getTarget() {
        return target;
    }

    public synchronized float[] getUp() {
        return up;
    }

    public void setAngle(float x, float y) {
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
}

