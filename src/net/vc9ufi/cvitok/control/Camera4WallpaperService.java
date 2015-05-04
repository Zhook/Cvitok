package net.vc9ufi.cvitok.control;


import net.vc9ufi.geometry.Quaternion;
import net.vc9ufi.geometry.Vector3f;

import java.security.InvalidParameterException;

public abstract class Camera4WallpaperService extends Motion implements LookAt, Runnable {
    private final float[] START_POINT;
    private final float[] TARGET;
    private static final Vector3f DEF_UP2 = new Vector3f(0, 0, 1);

    private Vector3f mCamera = new Vector3f(0, 5, 5);


    private static final float STEP_ANGLE = 0.001f;
    private static final float DELTA_PHI = (float) (Math.PI * 60 / 180);
    private static final float DELTA_THETA = (float) (Math.PI * 20 / 180);

    volatile private float mCameraForwardBack;
    volatile private float mCameraLeftRight;

    private boolean terminate = false;

    private boolean mWork = true;

    private static final int DELAY = 20;

    public Camera4WallpaperService(float[] camera, float[] target) {
        if (camera == null || camera.length != 3) throw new InvalidParameterException("mCamera.length!=3");
        START_POINT = camera;
        TARGET = target;
        new Thread(this).start();
    }

    @Override
    public void singleMove(float dx, float dy) {
        setDy(dy);
    }

    public synchronized void setXOffset(float xOffset) {
        mCameraLeftRight = -DELTA_PHI * xOffset;
    }

    private synchronized void setDy(float dy) {
        mCameraForwardBack -= dy * STEP_ANGLE;
        if (mCameraForwardBack > DELTA_THETA) mCameraForwardBack = DELTA_THETA;
        if (mCameraForwardBack < -DELTA_THETA) mCameraForwardBack = -DELTA_THETA;
    }

    @Override
    public void run() {
        Quaternion q;

        Vector3f orto;

        float camera_lr_ = 0;
        float camera_fb_ = 0;
        while (!terminate) {
            if (camera_fb_ != mCameraForwardBack || camera_lr_ != mCameraLeftRight) {
                camera_fb_ = mCameraForwardBack;
                camera_lr_ = mCameraLeftRight;
                mWork = true;
            }

            if (mWork) {
                orto = DEF_UP2.crossProduct(mCamera).normalize();
                q = Quaternion.Product(
                        Quaternion.FromAxisAndAngle(DEF_UP2.p, camera_lr_),
                        Quaternion.FromAxisAndAngle(orto.p, camera_fb_));

                mCamera.p = Quaternion.Rotate(START_POINT, q);

                result(mCamera.p, TARGET, DEF_UP2.p);
                mWork = false;
            }
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
