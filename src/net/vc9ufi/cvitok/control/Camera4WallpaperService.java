package net.vc9ufi.cvitok.control;


import net.vc9ufi.geometry.Quaternion;

public abstract class Camera4WallpaperService extends Camera4Renderer {
    private static final float[] DEF_UP = new float[]{0, 0, 1};
    private static final float DELTA_PHI = (float) (Math.PI * 60 / 180);

    volatile private Camera mStartCamera;

    volatile private float mMoveLeftOrRight;
    float camera_lr_ = 0;

    public Camera4WallpaperService(Camera camera) {
        super(camera, 20);
        try {
            mStartCamera = camera.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    public void setStartCameraPoint(Camera camera) {
        mStartCamera = camera;
    }

    public void setXOffset(float xOffset) {
        mMoveLeftOrRight = -DELTA_PHI * xOffset;
    }

    @Override
    boolean workCondition() {
        if (camera_lr_ != mMoveLeftOrRight) {
            camera_lr_ = mMoveLeftOrRight;
            return true;
        }
        return false;
    }

    @Override
    void work(Camera camera) {
        Quaternion q = Quaternion.fromAxisAndAngle(DEF_UP, camera_lr_);

        camera.setCamera(Quaternion.rotate(mStartCamera.getCamera(), q));
        camera.setUp(Quaternion.rotate(mStartCamera.getUp(), q));
    }

}
