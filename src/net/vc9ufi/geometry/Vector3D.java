package net.vc9ufi.geometry;

public class Vector3D {

    public static final double[][][] NORMAL = new double[][][]{
            {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}},
            {{-1, 0, 0}, {0, -1, 0}, {0, 0, -1}}};

    public static boolean eQuals(float[] v1, float[] v2) {
        if (v1[0] != v2[0]) return false;
        if (v1[1] != v2[1]) return false;
        if (v1[2] != v2[2]) return false;

        return true;
    }

    public static boolean inCube(float[] point, float[] v1, float[] v2, float r) {
        boolean temp = false;
        if ((v1[0] <= r + point[0]) && (v2[0] + r >= point[0])
                && (v1[1] <= r + point[1]) && (v2[1] + r >= point[1])
                && (v1[2] <= r + point[2]) && (v2[2] + r >= point[2])) {
            temp = true;
        }
        return temp;
    }

    public static float[] calcNormal(float[] p1, float[] p2, float[] p3) {
        float[] n = new float[3];
        float[] v1 = subF(p1, p2);
        float[] v2 = subF(p1, p3);
        n[0] = v1[1] * v2[2] - v1[2] * v2[1];
        n[1] = v1[2] * v2[0] - v1[0] * v2[2];
        n[2] = v1[0] * v2[1] - v1[1] * v2[0];
        n = normalizeF(n);
        return n;
    }

    public static float[] normalizeF(float[] in) {
        double d = Math.sqrt(in[0] * in[0] + in[1] * in[1] + in[2] * in[2]);
        if (d != 0) {
            float[] out = new float[3];
            for (int i = 0; i < 3; i++) {
                out[i] = (float) (in[i] / d);
            }
            return out;
        } else {
            return in.clone();
        }
    }

    public static double[] normalizeD(double[] in) {
        double d = Math.sqrt(in[0] * in[0] + in[1] * in[1] + in[2] * in[2]);
        if (d != 0) {
            double[] out = new double[3];
            for (int i = 0; i < 3; i++) {
                out[i] = in[i] / d;
            }
            return out;
        } else {
            return in.clone();
        }
    }

    public static float[] sum(float[] v1, float[] v2) {
        float[] v = new float[3];
        for (int i = 0; i < 3; i++) {
            v[i] = v1[i] + v2[i];
        }
        return v;
    }

    public static float[] middle(float[] v1, float[] v2) {
        float[] v = new float[3];
        for (int i = 0; i < 3; i++) {
            v[i] = (v1[i] + v2[i]) / 2f;
        }
        return v;
    }

    public static float[] crossF(float[] p1, float[] p2, float[] p3) {
        float[] n = new float[3];
        float[] v1 = subF(p1, p2);
        float[] v2 = subF(p1, p3);
        n[0] = v1[1] * v2[2] - v1[2] * v2[1];
        n[1] = v1[2] * v2[0] - v1[0] * v2[2];
        n[2] = v1[0] * v2[1] - v1[1] * v2[0];
        return n;
    }

    public static double[] crossD(float[] p1, float[] p2, float[] p3) {
        double[] n = new double[3];
        double[] v1 = subD(p1, p2);
        double[] v2 = subD(p1, p3);
        n[0] = v1[1] * v2[2] - v1[2] * v2[1];
        n[1] = v1[2] * v2[0] - v1[0] * v2[2];
        n[2] = v1[0] * v2[1] - v1[1] * v2[0];
        return n;
    }

    public static float[] cross(float[] v1, float[] v2) {
        float[] n = new float[3];
        n[0] = v1[1] * v2[2] - v1[2] * v2[1];
        n[1] = v1[2] * v2[0] - v1[0] * v2[2];
        n[2] = v1[0] * v2[1] - v1[1] * v2[0];
        return n;
    }

    public static float dotF(float[] v1, float[] v2) {
        return v1[0] * v2[0] + v1[1] * v2[1] + v1[2] * v2[2];
    }

    public static double dotD(float[] v1, float[] v2) {
        return (double) v1[0] * (double) v2[0]
                + (double) v1[1] * (double) v2[1]
                + (double) v1[2] * (double) v2[2];
    }

    public static double dotD(double[] v1, float[] v2) {
        return v1[0] * (double) v2[0]
                + v1[1] * (double) v2[1]
                + v1[2] * (double) v2[2];
    }

    public static double dotD(double[] v1, double[] v2) {
        return v1[0] * v2[0]
                + v1[1] * v2[1]
                + v1[2] * v2[2];
    }

    public static float[] subF(float[] p1, float[] p2) {
        float[] p = new float[3];
        for (int i = 0; i < 3; i++) {
            p[i] = p2[i] - p1[i];
        }
        return p;
    }

    public static double[] subD(float[] p1, float[] p2) {
        double[] p = new double[3];
        for (int i = 0; i < 3; i++) {
            p[i] = p2[i] - p1[i];
        }
        return p;
    }

    public static double[] subD(float[] p1, double[] p2) {
        double[] p = new double[3];
        for (int i = 0; i < 3; i++) {
            p[i] = p2[i] - p1[i];
        }
        return p;
    }

    public static float length(float[] v) {
        return (float) Math.sqrt(
                v[0] * v[0] + v[1] * v[1] + v[2] * v[2]);
    }

    public static float length(double[] v) {
        return (float) Math.sqrt(
                v[0] * v[0] + v[1] * v[1] + v[2] * v[2]);
    }

    public static void setLength(float[] in, double l) {
        double d = Math.sqrt(in[0] * in[0] + in[1] * in[1] + in[2] * in[2]);
        if (d != 0) {
            for (int i = 0; i < 3; i++) {
                in[i] *= l / d;
            }
        }
    }

    public static void dLength(float[] in, double l) {
        double d = Math.sqrt(in[0] * in[0] + in[1] * in[1] + in[2] * in[2]);
        if (d != 0) {
            for (int i = 0; i < 3; i++) {
                in[i] *= (d + l) / d;
            }
        }
    }

    public static void divLength(float[] in, float dr) {
        if (dr != 0) {
            for (int i = 0; i < 3; i++) {
                in[i] *= dr;
            }
        }
    }

    public static float lengthSQR(float[] v) {
        return v[0] * v[0] + v[1] * v[1] + v[2] * v[2];
    }

    public static float lengthSQR(float[] v1, float[] v2) {
        return (v1[0] - v2[0]) * (v1[0] - v2[0])
                + (v1[1] - v2[1]) * (v1[1] - v2[1])
                + (v1[2] - v2[2]) * (v1[2] - v2[2]);
    }

    public static float[] toFloat(double[] v) {
        return new float[]{(float) v[0], (float) v[1], (float) v[2]};
    }
}
