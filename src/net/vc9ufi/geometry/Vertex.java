package net.vc9ufi.geometry;

public class Vertex implements Cloneable {

    public float[] p = new float[3];

    public Vertex(float x, float y, float z) {
        this.p[0] = x;
        this.p[1] = y;
        this.p[2] = z;
    }

    public Vertex(float[] point) {
        if (point == null) throw new NullPointerException("point");
        if (point.length != 3) throw new RuntimeException(point.getClass().toString() + ": Wrong dimension");
        this.p = point.clone();
    }


    @Override
    public Vertex clone() throws CloneNotSupportedException {
        Vertex v = (Vertex) super.clone();
        v.p = p.clone();
        return v;
    }

//---------------------------------------------------------


    public Vertex add(Vertex v) {
        p[0] += v.p[0];
        p[1] += v.p[1];
        p[2] += v.p[2];
        return this;
    }

    public Vertex addToNew(Vertex v) {
        return new Vertex(
                p[0] + v.p[0],
                p[1] + v.p[1],
                p[2] + v.p[2]);
    }

    public Vertex middle(Vertex v) {
        return new Vertex(
                (p[0] + v.p[0]) / 2f,
                (p[1] + v.p[1]) / 2f,
                (p[2] + v.p[2]) / 2f);
    }

    public Vertex subtract(Vertex v) {
        return new Vertex(
                p[0] - v.p[0],
                p[1] - v.p[1],
                p[2] - v.p[2]);
    }

    public Vertex crossProduct(Vertex v) {
        return new Vertex(
                p[1] * v.p[2] - p[2] * v.p[1],
                p[2] * v.p[0] - p[0] * v.p[2],
                p[0] * v.p[1] - p[1] * v.p[0]);
    }

    public float getLength() {
        return (float) Math.sqrt(p[0] * p[0] + p[1] * p[1] + p[2] * p[2]);
    }

    public Vertex setLength(float length) {
        double d = Math.sqrt(p[0] * p[0] + p[1] * p[1] + p[2] * p[2]);
        if (d != 0) {
            double dl = length / d;
            for (int i = 0; i < 3; i++) {
                p[i] = (float) (p[i] * dl);
            }
        }
        return this;
    }

    public Vertex divLength(float dr) {
        for (int i = 0; i < 3; i++) {
            p[i] = p[i] + p[i] * dr;
        }
        return this;
    }

    public Vertex normalize() {
        double d = Math.sqrt(p[0] * p[0] + p[1] * p[1] + p[2] * p[2]);
        if (d != 0) {
            for (int i = 0; i < 3; i++) {
                p[i] = (float) (p[i] / d);
            }
        }
        return this;
    }

    public Vertex rotate(Quaternion q) {
        return new Vertex(Quaternion.Rotate(p, q));
    }
}
