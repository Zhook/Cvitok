package net.vc9ufi.cvitok.control;

import net.vc9ufi.geometry.Quaternion;
import net.vc9ufi.geometry.Vector3f;

public abstract class RotatingCamera extends Camera4Renderer {

    private static final float STEP_ANGLE = 0.003f;
    private static final float STEP_RADIUS = 0.005f;

    private float mCameraForwardBack;
    private float mCameraLeftRight;
    private float mCameraRadius;

    private float cameraForwardBack_ = 0;
    private float cameraLeftRight_ = 0;
    private float cameraRadius_ = 0;

    public RotatingCamera(Camera camera) {
        super(camera, 20);
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
    void work(Camera camera) {
        cameraForwardBack_ *= STEP_ANGLE;
        cameraLeftRight_ *= STEP_ANGLE;
        cameraRadius_ *= STEP_RADIUS;

        float[] c = camera.getCamera();
        float[] u = camera.getUp();

        float[] orto = Vector3f.crossProduct(u, c);
        Vector3f.normalize(orto);

        Quaternion q = Quaternion.product(
                Quaternion.fromAxisAndAngle(u, cameraLeftRight_),
                Quaternion.fromAxisAndAngle(orto, cameraForwardBack_));

        c = Quaternion.rotate(c, q);
        Vector3f.divLength(c, cameraRadius_);
        camera.setCamera(c);

        u = Quaternion.rotate(u, q);
        camera.setUp(u);
    }

    @Override
    boolean workCondition() {
        if (mCameraForwardBack != 0 || mCameraLeftRight != 0 || mCameraRadius != 0) {
            cameraForwardBack_ = mCameraForwardBack;
            cameraLeftRight_ = mCameraLeftRight;
            cameraRadius_ = mCameraRadius;
            mCameraForwardBack = 0;
            mCameraLeftRight = 0;
            mCameraRadius = 0;
            return true;
        }
        return false;
    }
}

