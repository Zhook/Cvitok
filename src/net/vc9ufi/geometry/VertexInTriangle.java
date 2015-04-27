package net.vc9ufi.geometry;

import java.util.ArrayList;

public class VertexInTriangle extends Vertex {

    public ArrayList<Triangle> triangles;
    private boolean hasNormal = false;
    public Vertex normal;

    public VertexInTriangle(float x, float y, float z) {
        super(x, y, z);
    }

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


}
