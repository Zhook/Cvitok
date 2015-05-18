package net.vc9ufi.cvitok.control;

import java.security.InvalidParameterException;

public class Camera implements Cloneable {

    private volatile float[] camera = new float[]{0, 5, 5};
    private volatile float[] target = new float[]{0, 0, 0};
    private volatile float[] up = new float[]{0, 0, 1};

    public Camera() {
    }

    public void set(float[] camera, float[] target, float[] up) {
        setCamera(camera);
        setTarget(target);
        setUp(up);
    }

    public float[] getCamera() {
        return camera.clone();
    }

    public void setCamera(float[] camera) {
        if (camera == null) throw new InvalidParameterException("camera must be not null");
        if (camera.length != 3) throw new InvalidParameterException("camera length must be 3");
        this.camera = camera.clone();
    }

    public float[] getTarget() {
        return target.clone();
    }

    public void setTarget(float[] target) {
        if (target == null) throw new InvalidParameterException("target must be not null");
        if (target.length != 3) throw new InvalidParameterException("target length must be 3");
        this.target = target.clone();
    }

    public float[] getUp() {
        return up.clone();
    }

    public void setUp(float[] up) {
        if (up == null) throw new InvalidParameterException("up must be not null");
        if (up.length != 3) throw new InvalidParameterException("up length must be 3");
        this.up = up.clone();
    }


    @Override
    public Camera clone() throws CloneNotSupportedException {
        Camera clone = (Camera) super.clone();
        clone.set(camera, target, up);
        return clone;
    }
}
