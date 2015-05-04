package net.vc9ufi.cvitok.data;

import com.google.gson.annotations.SerializedName;
import net.vc9ufi.geometry.Quaternion;
import net.vc9ufi.geometry.Vector3f;

public class Parameters implements Cloneable {

    @SerializedName("quantity")
    public int quantity;
    @SerializedName("angle")
    public float angle;
    @SerializedName("circle")
    public boolean circle = true;

    @SerializedName("left")
    public Line left;
    @SerializedName("right")
    public Line right;

    @SerializedName("convex_left")
    public float convex;

    float quantityf;

    public Parameters() {
        quantity = 3;
        quantityf = 3;

        angle = (float) (2.0 * Math.PI / quantity);
        convex = -0.3f;

        Colors clLeft = new Colors(
                new float[]{1.0f, 0.8f, 0.8f, 1f},
                new float[]{1.0f, 0.8f, 0.8f, 1f},
                new float[]{1.0f, 0.8f, 0.8f, 0.6f},
                new float[]{1.0f, 0.8f, 0.8f, 0.2f});
        Coordinates crLeft = new Coordinates(
                new Vector3f(0, 0, 0),
                new Vector3f(0.5f, 0.5f, 0),
                new Vector3f(0.5f, 1, 0.5f),
                new Vector3f(0, 1, 1));
        left = new Line(clLeft, crLeft);

        Colors clRight = new Colors(
                new float[]{1.0f, 0.8f, 0.8f, 1f},
                new float[]{1.0f, 0.8f, 0.8f, 1f},
                new float[]{1.0f, 0.8f, 0.8f, 0.6f},
                new float[]{1.0f, 0.8f, 0.8f, 0.2f});
        Coordinates crRight = new Coordinates(
                new Vector3f(0, 0, 0),
                new Vector3f(-0.5f, 0.5f, 0),
                new Vector3f(-0.5f, 1, 0.5f),
                new Vector3f(0, 1, 1));
        right = new Line(clRight, crRight);
    }

    @Override
    public Parameters clone() throws CloneNotSupportedException {
        Parameters p = (Parameters) super.clone();
        p.quantity = quantity;
        p.angle = angle;
        p.circle = circle;
        p.convex = convex;
        p.left = left.clone();
        p.right = right.clone();
        return p;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Parameters)) return false;

        Parameters that = (Parameters) o;

        if (Float.compare(that.angle, angle) != 0) return false;
        if (circle != that.circle) return false;
        if (Float.compare(that.convex, convex) != 0) return false;
        if (quantity != that.quantity) return false;
        if (Float.compare(that.quantityf, quantityf) != 0) return false;
        if (left != null ? !left.equals(that.left) : that.left != null) return false;
        return !(right != null ? !right.equals(that.right) : that.right != null);

    }

    public void changeQuantity(float d_quantity) {
        quantityf += d_quantity * QUANTITY_STEP;
        if (quantityf < 1) quantityf = 1;
        quantity = (int) quantityf;
        angle = (float) (2 * Math.PI / quantity);
    }
    //------------------------------------------------------

    public Parameters setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public Parameters setAngle(float angle) {
        this.angle = angle;
        return this;
    }

    public Parameters setConvex(float convex) {
        this.convex = convex;
        return this;
    }

    //----------------------------subclasses

    public static class Line implements Cloneable {
        @SerializedName("coord")
        public Coordinates coord;

        @SerializedName("colors")
        public Colors colors;

        public Line(Colors colors, Coordinates coord) {
            this.colors = colors;
            this.coord = coord;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Line line = (Line) o;

            if (!colors.equals(line.colors)) return false;
            return coord.equals(line.coord);

        }

        @Override
        public int hashCode() {
            return 0;
        }

        @Override
        public Line clone() throws CloneNotSupportedException {
            Line cl = (Line) super.clone();
            cl.coord = coord.clone();
            cl.colors = colors.clone();
            return cl;
        }
    }

    public static class Coordinates implements Cloneable {
        @SerializedName("start")
        public Vector3f start;

        @SerializedName("p1")
        public Vector3f p1;

        @SerializedName("p2")
        public Vector3f p2;

        @SerializedName("finish")
        public Vector3f finish;

        public Coordinates(Vector3f start, Vector3f p1, Vector3f p2, Vector3f finish) {
            this.finish = finish;
            this.p1 = p1;
            this.p2 = p2;
            this.start = start;
        }

        @Override
        public Coordinates clone() throws CloneNotSupportedException {
            Coordinates cl = (Coordinates) super.clone();
            cl.start = start.clone();
            cl.p1 = p1.clone();
            cl.p2 = p2.clone();
            cl.finish = finish.clone();
            return cl;
        }

        public Vector3f[] toParamBezier() {
            Vector3f[] param = new Vector3f[4];
            param[0] = start;
            param[1] = p1;
            param[2] = p2;
            param[3] = finish;
            return param;
        }

        public Coordinates rotate(Quaternion q) {
            return new Coordinates(
                    start.rotate(q),
                    p1.rotate(q),
                    p2.rotate(q),
                    finish.rotate(q));
        }
    }

    public static class Colors implements Cloneable {
        @SerializedName("start")
        public float[] start;

        @SerializedName("p1")
        public float[] p1;

        @SerializedName("p2")
        public float[] p2;

        @SerializedName("finish")
        public float[] finish;

        public Colors(float[] start, float[] p1, float[] p2, float[] finish) {
            this.finish = finish;
            this.p1 = p1;
            this.p2 = p2;
            this.start = start;
        }

        @Override
        public Colors clone() throws CloneNotSupportedException {
            Colors cl = (Colors) super.clone();
            cl.start = start.clone();
            cl.p1 = p1.clone();
            cl.p2 = p2.clone();
            cl.finish = finish.clone();
            return cl;
        }
    }

    private static final float QUANTITY_STEP = 0.01f;
}
