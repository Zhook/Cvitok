package net.vc9ufi.cvitok.control;

import net.vc9ufi.geometry.Quaternion;
import net.vc9ufi.geometry.Vector3f;

public abstract class RotatingCamera extends Motion implements LookAt, Runnable {

    private Vector3f mCamera = new Vector3f(0, 5, 5);
    private Vector3f mTarget = new Vector3f(0, 0, 0);
    private Vector3f mUp = new Vector3f(0, 0, 1);

    private static final float STEP_ANGLE = 0.003f;
    private static final float STEP_RADIUS = 0.01f;

    private float mCameraForwardBack;
    private float mCameraLeftRight;
    private float mCameraRadius;

    boolean terminate = false;

    public static final int DELAY = 20;

    public RotatingCamera() {
        new Thread(this).start();
    }

    public synchronized void setAngle(float x, float y) {
        mCameraForwardBack -= y;
        mCameraLeftRight -= x;
    }


    @Override
    public void singleMove(float dx, float dy) {
        setAngle(dx, dy);
    }

    @Override
    public void multiMove(float dr, float dx, float dy) {
        mCameraRadius -= dr;
    }

    @Override
    public void run() {
        Quaternion q;
        Vector3f orto;

        boolean work = true;
        while (!terminate) {
            float cameraForwardBack_ = 0;
            float cameraLeftRight_ = 0;
            float cameraRadius_ = 0;
            if (mCameraForwardBack != 0 || mCameraLeftRight != 0 || mCameraRadius != 0) {
                work = true;
                cameraForwardBack_ = mCameraForwardBack;
                cameraLeftRight_ = mCameraLeftRight;
                cameraRadius_ = mCameraRadius;
                mCameraForwardBack = 0;
                mCameraLeftRight = 0;
                mCameraRadius = 0;
            }
            if (work) {
                cameraForwardBack_ *= STEP_ANGLE;
                cameraLeftRight_ *= STEP_ANGLE;
                cameraRadius_ *= STEP_RADIUS;

                orto = mUp.crossProduct(mCamera).normalize();

                q = Quaternion.Product(
                        Quaternion.FromAxisAndAngle(mUp.p, cameraLeftRight_),
                        Quaternion.FromAxisAndAngle(orto.p, cameraForwardBack_));

                mCamera.p = Quaternion.Rotate(mCamera.p, q);
                mUp.p = Quaternion.Rotate(mUp.p, q);

                mCamera.divLength(cameraRadius_);
                work = false;
                result(mCamera.p, mTarget.p, mUp.p);
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

