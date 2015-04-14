package net.vc9ufi.geometry;


public class Vector2f {
    float x = 0, y = 0;

    public Vector2f() {
    }

    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2f add(Vector2f v) {
        x += v.x;
        y += v.y;
        return this;
    }

    public Vector2f addToNew(Vector2f v) {
        return new Vector2f(x + v.x, y + v.y);
    }

    public Vector2f middle(Vector2f v) {
        return new Vector2f((x + v.x) / 2f, (y + v.y) / 2f);
    }

    public Vector2f subtract(Vector2f v) {
        return new Vector2f(x - v.x, y - v.y);
    }

    public float cross(Vector2f v) {
        return x * v.y - y * v.x;
    }

    public float dot(Vector2f v) {
        return x * v.x + y * v.y;
    }

    public double getLength() {
        return Math.sqrt(x * x + y * y);
    }

    public Vector2f setLength(float length) {
        double d = getLength();
        if (d != 0) {
            double dl = length / d;
            x = (float) (x * dl);
            y = (float) (y * dl);
        }
        return this;
    }

    public Vector2f normalize() {
        double d = getLength();
        if (d != 0) {
            x = (float) (x / d);
            y = (float) (y / d);
        }
        return this;
    }

}
