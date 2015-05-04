package net.vc9ufi.geometry;

import java.util.ArrayList;

public class Vector3fInTriangle extends Vector3f {

    public ArrayList<Triangle> triangles;
    private boolean hasNormal = false;
    public Vector3f normal;

    public Vector3fInTriangle(float x, float y, float z) {
        super(x, y, z);
    }

    public Vector3fInTriangle(float[] point) {
        super(point);
    }

    public void addTriangle(Triangle triangle) {
        if (triangles == null) triangles = new ArrayList<>();
        triangles.add(triangle);
    }

    public void calcVertexNormal() {
        if (!hasNormal) {
            normal = new Vector3f(0, 0, 0);
            for (Triangle triangle : triangles) {
                normal = normal.addToNew(triangle.n);
            }
            normal.normalize();
        }
        hasNormal = true;
    }


}
