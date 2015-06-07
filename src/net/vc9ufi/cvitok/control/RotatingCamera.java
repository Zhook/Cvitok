package net.vc9ufi.cvitok.control;

import net.vc9ufi.geometry.Quaternion;
import net.vc9ufi.geometry.Vector3f;

public abstract class RotatingCamera extends Camera4Renderer {
    public enum MODE {ROTATE, MOVE}

    private static final float STEP_ANGLE = 0.003f;
    private static final float STEP_RADIUS = 0.005f;
    private static final float STEP_MOVE = 0.005f;


    private float mCameraUpDown;
    private float mCameraLeftRight;
    private float mCameraRadius;

    private float cameraUpDown_ = 0;
    private float cameraLeftRight_ = 0;
    private float cameraRadius_ = 0;

    private MODE mMode = MODE.ROTATE;

    public RotatingCamera(Camera camera) {
        super(camera, 20);
    }


    public synchronized void setAngle(float x, float y) {
        mCameraUpDown -= y;
        mCameraLeftRight -= x;
    }

    public void setMode(MODE mode) {
        this.mMode = mode;
    }

    public MODE getMode() {
        return mMode;
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
    Camera calculate(Camera oldCamera) {
        switch (mMode) {
            case ROTATE:
                return rotate(oldCamera);

            case MOVE:
                return move(oldCamera);

        }
        return oldCamera;
    }

    @Override
    boolean needCalculate() {
        if (mCameraUpDown != 0 || mCameraLeftRight != 0 || mCameraRadius != 0) {
            cameraUpDown_ = mCameraUpDown;
            cameraLeftRight_ = mCameraLeftRight;
            cameraRadius_ = mCameraRadius;
            mCameraUpDown = 0;
            mCameraLeftRight = 0;
            mCameraRadius = 0;
            return true;
        }
        return false;
    }

    private Camera rotate(Camera camera) {
        cameraUpDown_ *= STEP_ANGLE;
        cameraLeftRight_ *= STEP_ANGLE;
        cameraRadius_ *= STEP_RADIUS;

        float[] c = camera.getCamera();
        float[] u = camera.getUp();

        float[] orto = Vector3f.crossProduct(u, c);
        Vector3f.normalize(orto);

        Quaternion q = Quaternion.product(
                Quaternion.fromAxisAndAngle(u, cameraLeftRight_),
                Quaternion.fromAxisAndAngle(orto, cameraUpDown_));

        c = Quaternion.rotate(c, q);
        Vector3f.divLength(c, cameraRadius_);
        camera.setCamera(c);

        float[] t = camera.getTarget();
        t = Quaternion.rotate(t, q);
        camera.setTarget(t);

        u = Quaternion.rotate(u, q);
        camera.setUp(u);
        return camera;
    }

    private Camera move(Camera camera) {
        cameraUpDown_ *= STEP_MOVE;
        cameraLeftRight_ *= STEP_MOVE;
        cameraRadius_ *= STEP_RADIUS;

        float[] c = camera.getCamera();
        float[] u = camera.getUp();
        float[] t = camera.getTarget();

        if (cameraUpDown_ != 0) {
            float[] du_y = Vector3f.setLength(u, cameraUpDown_);
            c = Vector3f.add(c, du_y);
            t = Vector3f.add(t, du_y);
        }

        if (cameraLeftRight_ != 0) {
            float[] orto = Vector3f.crossProduct(u, c);
            float[] du_x = Vector3f.setLength(orto, -cameraLeftRight_);
            c = Vector3f.add(c, du_x);
            t = Vector3f.add(t, du_x);
        }

        Vector3f.divLength(c, cameraRadius_);
        camera.setCamera(c);
        camera.setTarget(t);

        return camera;
    }
}

