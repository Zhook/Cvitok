package net.vc9ufi.geometry;

import java.util.LinkedList;

public class Icosahedron {

    public static final float X = 0.525731112119133606f;
    public static final float Z = 0.850650808352039932f;
    public static final float[][] VDATA = {
            {-X, 0, Z}, {X, 0, Z}, {-X, 0, -Z}, {X, 0, -Z},
            {0, Z, X}, {0, Z, -X}, {0, -Z, X}, {0, -Z, -X},
            {Z, X, 0}, {-Z, X, 0}, {Z, -X, 0}, {-Z, -X, 0}};
    public static final int[][] TINDICES = {
            {0, 4, 1}, {0, 9, 4}, {9, 5, 4}, {4, 5, 8}, {4, 8, 1},
            {8, 10, 1}, {8, 3, 10}, {5, 3, 8}, {5, 2, 3}, {2, 7, 3},
            {7, 10, 3}, {7, 6, 10}, {7, 11, 6}, {11, 0, 6}, {0, 1, 6},
            {6, 1, 10}, {9, 0, 11}, {9, 11, 2}, {9, 2, 5}, {7, 2, 11}};

    public static int CalcIcosahedron(LinkedList<Triangle> trians, float radius, int division) {
        int count = 0;
        for (int i = 0; i < 20; i++) {
            VertexInTriangle v1 = new VertexInTriangle(
                    radius * VDATA[TINDICES[i][0]][0],
                    radius * VDATA[TINDICES[i][0]][1],
                    radius * VDATA[TINDICES[i][0]][2]);
            VertexInTriangle v2 = new VertexInTriangle(
                    radius * VDATA[TINDICES[i][1]][0],
                    radius * VDATA[TINDICES[i][1]][1],
                    radius * VDATA[TINDICES[i][1]][2]);
            VertexInTriangle v3 = new VertexInTriangle(
                    radius * VDATA[TINDICES[i][2]][0],
                    radius * VDATA[TINDICES[i][2]][1],
                    radius * VDATA[TINDICES[i][2]][2]);
            count += Subdivide(trians, radius, v1, v2, v3, division);
        }
        return count;
    }

    private static int Subdivide(LinkedList<Triangle> trians, float radius, VertexInTriangle v1, VertexInTriangle v2, VertexInTriangle v3, int depth) {
        if (depth == 0) {
            trians.add(new Triangle(v1, v2, v3, COLOR_PLACEHOLDER, COLOR_PLACEHOLDER, COLOR_PLACEHOLDER, 0));
            return 1;
        } else {
            VertexInTriangle v12 = new VertexInTriangle(v1.addToNew(v2).p);
            VertexInTriangle v23 = new VertexInTriangle(v2.addToNew(v3).p);
            VertexInTriangle v31 = new VertexInTriangle(v3.addToNew(v1).p);
            v12.setLength(radius);
            v23.setLength(radius);
            v31.setLength(radius);
            int count;
            count = Subdivide(trians, radius, v1, v12, v31, depth - 1);
            count += Subdivide(trians, radius, v2, v23, v12, depth - 1);
            count += Subdivide(trians, radius, v3, v31, v23, depth - 1);
            count += Subdivide(trians, radius, v12, v23, v31, depth - 1);
            return count;
        }
    }

    private final static float[] COLOR_PLACEHOLDER = new float[]{0, 0, 0, 0};
}
