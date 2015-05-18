package net.vc9ufi.cvitok.data;

import net.vc9ufi.geometry.*;

import java.util.LinkedList;

public class PetalsCalculator extends Calculator {

    private static final float[] UP = new float[]{0, 0, 1};
    private final Parameters PETAL;
    private final int PRECISION;
    private final int TAG;

    public PetalsCalculator(Parameters petal, int precision, int tag) {
        this.PETAL = petal;
        this.PRECISION = precision;
        TAG = tag;
    }

    @Override
    public LinkedList<Triangle> calculate() {

        int petalQuantity = PETAL.quantity;

        float[][] startColorLeft = Bezier.getCurve(
                PETAL.left.colors.start,
                PETAL.left.colors.p1,
                PETAL.left.colors.p2,
                PETAL.left.colors.finish,
                PRECISION);
        float[][] startColorRight = Bezier.getCurve(
                PETAL.right.colors.start,
                PETAL.right.colors.p1,
                PETAL.right.colors.p2,
                PETAL.right.colors.finish,
                PRECISION);
        float[][][] ColorGrid = getColorGrid(startColorLeft, startColorRight, PRECISION);

        LinkedList<Triangle> triangles = new LinkedList<>();

        Quaternion q;
        Parameters.Coordinates leftParamCoord;
        Parameters.Coordinates rightParamCoord;
        for (int i = 0; i < petalQuantity; i++) {
            q = Quaternion.fromAxisAndAngle(UP, i * PETAL.angle);
            leftParamCoord = PETAL.left.coord.rotate(q);
            rightParamCoord = PETAL.right.coord.rotate(q);

            calcPetal(triangles, leftParamCoord, rightParamCoord, ColorGrid, PRECISION, PETAL.convex, TAG);
        }

        for (Triangle triangle : triangles) {
            triangle.v1.calcVertexNormal();
            triangle.v2.calcVertexNormal();
            triangle.v3.calcVertexNormal();
        }

        return triangles;
    }

    static void calcPetal(LinkedList<Triangle> triangles, Parameters.Coordinates line1, Parameters.Coordinates line2, float[][][] color, int precision, float convex, int tag) {
        Vector3f[] startLine1 = Bezier.getCurve(line1.toParamBezier(), precision);
        Vector3f[] startLine2 = Bezier.getCurve(line2.toParamBezier(), precision);
        Vector3f[] startVertexConvex = getConvexVertexLine(startLine1, startLine2, convex);
        Vector3fInTriangle[][] vertices = getGrid(startLine1, startVertexConvex, startVertexConvex, startLine2, precision);
        makeTrianglesFromVertexArray(triangles, vertices, color, precision, tag);
    }


    static Vector3f[] getConvexVertexLine(Vector3f[] line1, Vector3f[] line2, float convex) {
        if ((line1 == null) || (line2 == null)) return null;
        if (line1.length != line2.length) throw new RuntimeException("different size arrays");
        int length = line1.length;
        Vector3f[] result = new Vector3f[length];

        Vector3f sum;
        Vector3f sub;
        Vector3f normal;
        float convexSize;
        for (int i = 0; i < length; i++) {
            sum = line1[i].middle(line2[i]);
            sub = line1[i].subtract(line2[i]);
            normal = line1[i].crossProduct(line2[i]);
            convexSize = sub.getLength() * convex;
            normal.setLength(convexSize);
            result[i] = sum.addToNew(normal);
        }

        return result;
    }

    static Vector3fInTriangle[][] getGrid(Vector3f[] start, Vector3f[] p1, Vector3f[] p2, Vector3f[] finish, int precision) {
        Vector3fInTriangle[][] grid = new Vector3fInTriangle[precision][];
        for (int i = 0; i < precision; i++) {
            float[][] line = Bezier.getCurve(start[i].p, p1[i].p, p2[i].p, finish[i].p, precision);
            grid[i] = new Vector3fInTriangle[precision];
            for (int j = 0; j < precision; j++) {
                grid[i][j] = new Vector3fInTriangle(line[j]);
            }
        }
        return grid;
    }

    static void makeTrianglesFromVertexArray(LinkedList<Triangle> triangles, Vector3fInTriangle[][] vertices, float[][][] colors, int precision, int tag) {
        for (int i = 0; i < precision - 1; i++) {
            for (int j = 0; j < precision - 1; j++) {
                triangles.add(new Triangle(
                        vertices[i][j], vertices[i + 1][j], vertices[i][j + 1],
                        colors[i][j], colors[i + 1][j], colors[i][j + 1], tag));
                triangles.add(new Triangle(
                        vertices[i][j + 1], vertices[i + 1][j], vertices[i + 1][j + 1],
                        colors[i][j + 1], colors[i + 1][j], colors[i + 1][j + 1], tag));
            }
        }
    }


    static float[][][] getColorGrid(float[][] start, float[][] finish, int precision) {
        float[][][] grid = new float[precision][precision][4];
        float[] k = new float[4];

        for (int i = 0; i < precision; i++) {
            for (int j = 0; j < 4; j++)
                k[j] = (finish[i][j] - start[i][j]) / precision;
            for (int j = 0; j < precision; j++)
                for (int l = 0; l < 4; l++)
                    grid[i][j][l] = start[i][l] + k[l] * j;
        }
        return grid;
    }


}
