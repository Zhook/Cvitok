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

    public void Normalize() {
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

    public static Quaternion Product(Quaternion q1, Quaternion q2) {
        Quaternion q = new Quaternion();
        q.p[0] = q1.p[3] * q2.p[0] + q1.p[0] * q2.p[3] + q1.p[1] * q2.p[2] - q1.p[2] * q2.p[1];
        q.p[1] = q1.p[3] * q2.p[1] + q1.p[1] * q2.p[3] + q1.p[2] * q2.p[0] - q1.p[0] * q2.p[2];
        q.p[2] = q1.p[3] * q2.p[2] + q1.p[2] * q2.p[3] + q1.p[0] * q2.p[1] - q1.p[1] * q2.p[0];
        q.p[3] = q1.p[3] * q2.p[3] - q1.p[0] * q2.p[0] - q1.p[1] * q2.p[1] - q1.p[2] * q2.p[2];
        return q;
    }

    public static Quaternion FromAxisAndAngle(float[] v, float rad) {
        float s = (float) Math.sin(rad * 0.5);
        Quaternion q = new Quaternion(v[0] * s, v[1] * s, v[2] * s,
                (float) Math.cos(rad / 2.0));
        q.Normalize();
        return q;
    }

    public static float[] Rotate(float[] vin, Quaternion qin) {
        Quaternion q = new Quaternion(vin, 0);
        q = Product(qin.Conjugate(), q);
        q = Product(q, qin);
        return new float[]{q.p[0], q.p[1], q.p[2]};
    }
}
