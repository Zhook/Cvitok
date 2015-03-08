package net.vc9ufi.geometry;

import android.view.MotionEvent;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LookAt extends Thread {

    //
    private float[] camera = new float[]{0, 5, 5};
    private float[] up = new float[]{0, 0, 1};
    private float[] camera_ = new float[]{0, 5, 5};
    private float[] up_ = new float[]{0, 0, 1};
    //
    private static final float step_angle = 0.003f;
    private static final float step_radius = 0.01f;
    //
    private float camera_fb;
    private float camera_lr;
    private float camera_r;

    private float x = 0;
    private float y = 0;
    private float r = 0;

    boolean terminate = false;

    public LookAt() {
        start();
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

    public synchronized float[] getUp() {
        return up;
    }

    public synchronized void setAngle(float x, float y) {
        camera_fb += y;
        camera_lr += x;
    }

    public boolean motionEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                x = event.getX();
                y = event.getY();
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                float dx = event.getX(0) - event.getX(1);
                float dy = event.getY(0) - event.getY(1);
                r = (float) Math.sqrt(dx * dx + dy * dy);
                break;
            case MotionEvent.ACTION_MOVE:
                if (event.getPointerCount() > 1) {
                    dx = event.getX(0) - event.getX(1);
                    dy = event.getY(0) - event.getY(1);
                    float _r = (float) Math.sqrt(dx * dx + dy * dy);
                    float dr = r - _r;
                    synchronized (this) {
                        camera_r += dr;
                    }
                    r = _r;
                } else {
                    dx = event.getX() - x;
                    dy = event.getY() - y;
                    x = event.getX();
                    y = event.getY();
                    setAngle(dx, dy);
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                if (event.getPointerId(event.getActionIndex()) == 0) {
                    x = event.getX(1);
                    y = event.getY(1);
                } else {
                    x = event.getX(0);
                    y = event.getY(0);
                }
                break;
        }
        return true;
    }

    @Override
    public void run() {
        Quaternion q;
        float[] orto;
        while (!terminate) {
            float camera_fb_;
            float camera_lr_;
            float camera_r_;
            synchronized (this) {
                camera_fb_ = camera_fb;
                camera_lr_ = camera_lr;
                camera_r_ = camera_r;
                camera_fb = 0;
                camera_lr = 0;
                camera_r = 0;
            }
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

            synchronized (this) {
                camera = camera_;
                up = up_;
                try {
                    this.wait(20);
                } catch (InterruptedException ex) {
                    Logger.getLogger(this.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
