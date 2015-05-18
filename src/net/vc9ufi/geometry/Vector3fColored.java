package net.vc9ufi.geometry;

public class Vector3fColored extends Vector3f {

    public float[] color;

    public Vector3fColored(float[] point, float[] color) {
        super(point);
        this.color = color;
    }
}
