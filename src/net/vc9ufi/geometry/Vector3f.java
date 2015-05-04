package net.vc9ufi.geometry;

public class Vector3f implements Cloneable {

    public float[] p = new float[3];

    public Vector3f(float x, float y, float z) {
        this.p[0] = x;
        this.p[1] = y;
        this.p[2] = z;
    }

    public Vector3f(float[] point) {
        if (point == null) throw new NullPointerException("point");
        if (point.length != 3) throw new RuntimeException(point.getClass().toString() + ": Wrong dimension");
        this.p = point.clone();
    }


    @Override
    public Vector3f clone() throws CloneNotSupportedException {
        Vector3f v = (Vector3f) super.clone();
        v.p = p.clone();
        return v;
    }

//---------------------------------------------------------


    public Vector3f add(Vector3f v) {
        p[0] += v.p[0];
        p[1] += v.p[1];
        p[2] += v.p[2];
        return this;
    }

    public Vector3f addToNew(Vector3f v) {
        return new Vector3f(
                p[0] + v.p[0],
                p[1] + v.p[1],
                p[2] + v.p[2]);
    }

    public Vector3f middle(Vector3f v) {
        return new Vector3f(
                (p[0] + v.p[0]) / 2f,
                (p[1] + v.p[1]) / 2f,
                (p[2] + v.p[2]) / 2f);
    }

    public Vector3f subtract(Vector3f v) {
        return new Vector3f(
                p[0] - v.p[0],
                p[1] - v.p[1],
                p[2] - v.p[2]);
    }

    public Vector3f crossProduct(Vector3f v) {
        return new Vector3f(
                p[1] * v.p[2] - p[2] * v.p[1],
                p[2] * v.p[0] - p[0] * v.p[2],
                p[0] * v.p[1] - p[1] * v.p[0]);
    }

    public float getLength() {
        return (float) Math.sqrt(p[0] * p[0] + p[1] * p[1] + p[2] * p[2]);
    }

    public Vector3f setLength(float length) {
        double d = Math.sqrt(p[0] * p[0] + p[1] * p[1] + p[2] * p[2]);
        if (d != 0) {
            double dl = length / d;
            for (int i = 0; i < 3; i++) {
                p[i] = (float) (p[i] * dl);
            }
        }
        return this;
    }

    public Vector3f divLength(float dr) {
        for (int i = 0; i < 3; i++) {
            p[i] = p[i] + p[i] * dr;
        }
        return this;
    }

    public Vector3f normalize() {
        double d = Math.sqrt(p[0] * p[0] + p[1] * p[1] + p[2] * p[2]);
        if (d != 0) {
            for (int i = 0; i < 3; i++) {
                p[i] = (float) (p[i] / d);
            }
        }
        return this;
    }

    public Vector3f rotate(Quaternion q) {
        return new Vector3f(Quaternion.Rotate(p, q));
    }
}
