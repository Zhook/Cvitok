package net.vc9ufi.geometry;

public class Quaternion {

    public Quaternion() {
    }

    public float[] p = new float[4];

    public Quaternion(float x, float y, float z, float w) {
        this.p[0] = x;
        this.p[1] = y;
        this.p[2] = z;
        this.p[3] = w;
    }

    public Quaternion(float[] v, float w) {
        this.p[0] = v[0];
        this.p[1] = v[1];
        this.p[2] = v[2];
        this.p[3] = w;
    }

    public void normalize() {
        double d = Math.sqrt(
                this.p[0] * this.p[0]
                        + this.p[1] * this.p[1]
                        + this.p[2] * this.p[2]
                        + this.p[3] * this.p[3]);
        if (d != 0) {
            for (int i = 0; i < 4; i++) {
                this.p[i] /= d;
            }
        }
    }

    public Quaternion Conjugate() {
        return new Quaternion(-this.p[0], -this.p[1], -this.p[2], this.p[3]);
    }

    public static Quaternion product(Quaternion q1, Quaternion q2) {
        Quaternion q = new Quaternion();
        q.p[0] = q1.p[3] * q2.p[0] + q1.p[0] * q2.p[3] + q1.p[1] * q2.p[2] - q1.p[2] * q2.p[1];
        q.p[1] = q1.p[3] * q2.p[1] + q1.p[1] * q2.p[3] + q1.p[2] * q2.p[0] - q1.p[0] * q2.p[2];
        q.p[2] = q1.p[3] * q2.p[2] + q1.p[2] * q2.p[3] + q1.p[0] * q2.p[1] - q1.p[1] * q2.p[0];
        q.p[3] = q1.p[3] * q2.p[3] - q1.p[0] * q2.p[0] - q1.p[1] * q2.p[1] - q1.p[2] * q2.p[2];
        return q;
    }

    public static Quaternion fromAxisAndAngle(float[] v, float rad) {
        float halfRad = rad * 0.5f;
        float sin = (float) Math.sin(halfRad);
        float cos = (float) Math.cos(halfRad);
        Quaternion q = new Quaternion(v[0] * sin, v[1] * sin, v[2] * sin, cos);
        q.normalize();
        return q;
    }

    public static float[] rotate(float[] vin, Quaternion qin) {
        Quaternion q = new Quaternion(vin, 0);
        q = product(qin.Conjugate(), q);
        q = product(q, qin);
        return new float[]{q.p[0], q.p[1], q.p[2]};
    }
}
