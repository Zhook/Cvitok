package net.vc9ufi.geometry;

import java.util.ArrayList;

public class VertexInTriangle extends Vertex {

    public ArrayList<Triangle> triangles;
    boolean hasNormal = false;
    Vertex normal;

    public VertexInTriangle(float[] point) {
        super(point);
    }

    public void addTriangle(Triangle triangle) {
        if (triangles == null) triangles = new ArrayList<>();
        triangles.add(triangle);
    }

    public void calcVertexNormal() {
        if (!hasNormal) {
            normal = new Vertex(0, 0, 0);
            for (Triangle triangle : triangles) {
                normal = normal.addToNew(triangle.n);
            }
            normal.normalize();
        }
        hasNormal = true;
    }

    public static Vertex calculateNotNormalizeNormal(Vertex v1, Vertex v2, Vertex v3) {
        float[] v1_ = new float[]{v1.p[0] - v2.p[0], v1.p[1] - v2.p[1], v1.p[2] - v2.p[2]};
        float[] v2_ = new float[]{v1.p[0] - v3.p[0], v1.p[1] - v3.p[1], v1.p[2] - v3.p[2]};

        v1_[0] = v1_[1] * v2_[2] - v1_[2] * v2_[1];
        v1_[1] = v1_[2] * v2_[0] - v1_[0] * v2_[2];
        v1_[2] = v1_[0] * v2_[1] - v1_[1] * v2_[0];

        return new Vertex(v1_);
    }
}
