package net.vc9ufi.geometry;

import java.util.ArrayList;

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

    public static class Trian {
        public float[][] v = new float[3][3];
        public float[][] n = new float[3][3];

        public Trian(float[] v1, float[] v2, float[] v3) {
            this.v[0] = v1;
            this.v[1] = v2;
            this.v[2] = v3;
            calkNormalf(v1, v2, v3);
        }

        public void calkNormalf(float[] v1, float[] v2, float[] v3) {
            n[0] = Vector3D.normalizeF(v1);
            n[1] = Vector3D.normalizeF(v2);
            n[2] = Vector3D.normalizeF(v3);
        }
    }


    public static int CalcIcosahedron(ArrayList<Trian> trians, float radius, int division) {
        int count = 0;
        for (int i = 0; i < 20; i++) {
            float[] v1 = new float[]{
                    radius * VDATA[TINDICES[i][0]][0],
                    radius * VDATA[TINDICES[i][0]][1],
                    radius * VDATA[TINDICES[i][0]][2]};
            float[] v2 = new float[]{
                    radius * VDATA[TINDICES[i][1]][0],
                    radius * VDATA[TINDICES[i][1]][1],
                    radius * VDATA[TINDICES[i][1]][2]};
            float[] v3 = new float[]{
                    radius * VDATA[TINDICES[i][2]][0],
                    radius * VDATA[TINDICES[i][2]][1],
                    radius * VDATA[TINDICES[i][2]][2]};
            count += Subdivide(trians, radius, v1, v2, v3, division);
        }
        return count;
    }

    private static int Subdivide(ArrayList<Trian> trians, float radius, float[] v1, float[] v2, float[] v3, int depth) {
        if (depth == 0) {
            trians.add(new Trian(v1.clone(),v2.clone(),v3.clone()));
            return 1;
        } else {
            float[] v12 = Vector3D.sum(v1, v2);
            float[] v23 = Vector3D.sum(v2, v3);
            float[] v31 = Vector3D.sum(v3, v1);
            Vector3D.setLength(v12, radius);
            Vector3D.setLength(v23, radius);
            Vector3D.setLength(v31, radius);
            int count = Subdivide(trians, radius, v1, v12, v31, depth - 1);
            count += Subdivide(trians, radius, v2, v23, v12, depth - 1);
            count += Subdivide(trians, radius, v3, v31, v23, depth - 1);
            count += Subdivide(trians, radius, v12, v23, v31, depth - 1);
            return count;
        }
    }
}
