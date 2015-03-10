package net.vc9ufi.cvitok.data;

import com.google.gson.annotations.SerializedName;
import net.vc9ufi.geometry.Quaternion;
import net.vc9ufi.geometry.Vertex;

public class Parameters implements Cloneable {

    @SerializedName("name")
    public String name;

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

    public Parameters(String name) {
        this.name = name;
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
                new Vertex(0, 0, 0),
                new Vertex(0.5f, 0.5f, 0),
                new Vertex(0.5f, 1, 0.5f),
                new Vertex(0, 1, 1));
        left = new Line(clLeft, crLeft);

        Colors clRight = new Colors(
                new float[]{1.0f, 0.8f, 0.8f, 1f},
                new float[]{1.0f, 0.8f, 0.8f, 1f},
                new float[]{1.0f, 0.8f, 0.8f, 0.6f},
                new float[]{1.0f, 0.8f, 0.8f, 0.2f});
        Coordinates crRight = new Coordinates(
                new Vertex(0, 0, 0),
                new Vertex(-0.5f, 0.5f, 0),
                new Vertex(-0.5f, 1, 0.5f),
                new Vertex(0, 1, 1));
        right = new Line(clRight, crRight);
    }

    @Override
    public Parameters clone() throws CloneNotSupportedException {
        Parameters p = (Parameters) super.clone();
        p.name = name;
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
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (right != null ? !right.equals(that.right) : that.right != null) return false;

        return true;
    }

    public void changeQuantity(float d_quantity) {
        quantityf += d_quantity * QUANTITY_STEP;
        if (quantityf < 1) quantityf = 1;
        quantity = (int) quantityf;
        angle = (float) (2 * Math.PI / quantity);
    }
    //------------------------------------------------------

    public Parameters setName(String name) {
        this.name = name;
        return this;
    }

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
            if (!coord.equals(line.coord)) return false;

            return true;
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
        public Vertex start;

        @SerializedName("p1")
        public Vertex p1;

        @SerializedName("p2")
        public Vertex p2;

        @SerializedName("finish")
        public Vertex finish;

        public Coordinates(Vertex start, Vertex p1, Vertex p2, Vertex finish) {
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

        public Vertex[] toParamBezier() {
            Vertex[] param = new Vertex[4];
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
