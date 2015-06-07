package net.vc9ufi.geometry;

public class Vector3f implements Cloneable {

    private static float[] v1;
    private static float[] v2;
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

    public static float[] add(float[] in1, float[] in2) {
        float[] out = new float[3];
        out[0] = in1[0] + in2[0];
        out[1] = in1[1] + in2[1];
        out[2] = in1[2] + in2[2];
        return out;
    }


    public Vector3f middle(Vector3f v) {
        return new Vector3f(
                (p[0] + v.p[0]) / 2f,
                (p[1] + v.p[1]) / 2f,
                (p[2] + v.p[2]) / 2f);
    }


    public Vector3f subtract(Vector3f v) {
        return new Vector3f(subtract(p, v.p));
    }

    public static float[] subtract(float[] minuend, float[] subtrahend) {
        float[] result = new float[3];
        result[0] = minuend[0] - subtrahend[0];
        result[1] = minuend[1] - subtrahend[1];
        result[2] = minuend[2] - subtrahend[2];
        return result;
    }


    public Vector3f crossProduct(Vector3f v) {
        return new Vector3f(
                p[1] * v.p[2] - p[2] * v.p[1],
                p[2] * v.p[0] - p[0] * v.p[2],
                p[0] * v.p[1] - p[1] * v.p[0]);
    }

    public static float[] crossProduct(float[] v1, float[] v2) {
        return new float[]{
                v1[1] * v2[2] - v1[2] * v2[1],
                v1[2] * v2[0] - v1[0] * v2[2],
                v1[0] * v2[1] - v1[1] * v2[0]};
    }

    public float getLength() {
        return (float) Math.sqrt(p[0] * p[0] + p[1] * p[1] + p[2] * p[2]);
    }


    public Vector3f setLength(float length) {
        double d = Math.sqrt(p[0] * p[0] + p[1] * p[1] + p[2] * p[2]);
        if (d != 0) {
            double dl = length / d;
            p[0] = (float) (p[0] * dl);
            p[1] = (float) (p[1] * dl);
            p[2] = (float) (p[2] * dl);
        }
        return this;
    }

    public static float[] setLength(float[] in, float length) {
        float[] out = in.clone();
        double d = Math.sqrt(out[0] * out[0] + out[1] * out[1] + out[2] * out[2]);
        if (d != 0) {
            double dl = length / d;
            out[0] = (float) (out[0] * dl);
            out[1] = (float) (out[1] * dl);
            out[2] = (float) (out[2] * dl);
        }
        return out;
    }


    public Vector3f divLength(float dr) {
        p[0] = p[0] + p[0] * dr;
        p[1] = p[1] + p[1] * dr;
        p[2] = p[2] + p[2] * dr;
        return this;
    }

    public static float[] divLength(float[] p, float dr) {
        p[0] = p[0] + p[0] * dr;
        p[1] = p[1] + p[1] * dr;
        p[2] = p[2] + p[2] * dr;
        return p;
    }


    public Vector3f normalize() {
        float d = (float) Math.sqrt(p[0] * p[0] + p[1] * p[1] + p[2] * p[2]);
        if (d != 0) {
            p[0] = p[0] / d;
            p[1] = p[1] / d;
            p[2] = p[2] / d;
        }
        return this;
    }

    public static void normalize(float[] p) {
        float d = (float) Math.sqrt(p[0] * p[0] + p[1] * p[1] + p[2] * p[2]);
        if (d != 0) {
            p[0] = p[0] / d;
            p[1] = p[1] / d;
            p[2] = p[2] / d;
        }
    }


    public Vector3f rotate(Quaternion q) {
        return new Vector3f(Quaternion.rotate(p, q));
    }
}
