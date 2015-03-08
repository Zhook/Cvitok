package net.vc9ufi.cvitok.petal;

import net.vc9ufi.cvitok.data.Parameters;
import net.vc9ufi.geometry.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Calculator extends Thread {

    private static final float[] UP = new float[]{0, 0, 1};

    Pointers pointers;
    Parameters parameters;
    private int quality = 3;
    private boolean work = false;
    private boolean terminate = false;

    Parameters parameters_;
    private int quality_ = 3;

    @Override
    public void run() {
        while (!terminate) {
            if (work) {
                synchronized (this) {
                    try {
                        parameters = parameters_.clone();
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }
                    quality = quality_;
                    work = false;
                }
                if (parameters != null) {
                    Pointers pointers_ = calculate2(parameters, quality);
                    synchronized (this) {
                        pointers = pointers_;
                    }
                }
            }
            synchronized (this) {
                try {
                    wait(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public synchronized void setParameters(Parameters p) {
        if (p != null) {
            work = true;
            parameters_ = p;
        }
    }

    public synchronized void setQuality(int quality) {
        if (quality > 0) {
            work = true;
            this.quality_ = quality;
        }
    }


    public synchronized Pointers getPointers() {
        return pointers;
    }

    //----------------------------------------

    public static Pointers calculate2(Parameters parameters, int precision) {
//        Timer timer = new Timer("Calculator");

        float[][] startColorLeft = Bezier.getCurve(
                parameters.left.colors.start,
                parameters.left.colors.p1,
                parameters.left.colors.p2,
                parameters.left.colors.finish,
                precision);
        float[][] startColorRight = Bezier.getCurve(
                parameters.right.colors.start,
                parameters.right.colors.p1,
                parameters.right.colors.p2,
                parameters.right.colors.finish,
                precision);
        float[][][] ColorGrid = getColorGrid(startColorLeft, startColorRight, precision);

//        timer.println("color");

        int petalQuantity = parameters.quantity;
        int countTriangles = 2 * (precision - 1) * (precision - 1);
        Triangle[] triangles = new Triangle[petalQuantity * countTriangles];
        int offset = 0;

        Quaternion q;
        Parameters.Coordinates leftParamCoord;
        Parameters.Coordinates rightParamCoord;
        for (int i = 0; i < petalQuantity; i++) {
            q = Quaternion.FromAxisAndAngle(UP, i * parameters.angle);
            leftParamCoord = parameters.left.coord.rotate(q);
            rightParamCoord = parameters.right.coord.rotate(q);

            Triangle[] trianglesLeft = calcPetal(leftParamCoord, rightParamCoord, ColorGrid,
                    precision, parameters.convex);

            System.arraycopy(trianglesLeft, 0, triangles, offset, trianglesLeft.length);
            offset += trianglesLeft.length;

            //timer.println("leaf" + String.valueOf(i));
        }

        Pointers pointers = makePointersFromTriangles(triangles);

//        timer.println("pointers");

//        timer.printlnFullTime();
        return pointers;
    }

    static Triangle[] calcPetal(Parameters.Coordinates line1, Parameters.Coordinates line2, float[][][] color, int precision, float convex) {
//        Timer timer = new Timer("Half Leaf");

        Vertex[] startLine1 = Bezier.getCurve(line1.toParamBezier(), precision);
        Vertex[] startLine2 = Bezier.getCurve(line2.toParamBezier(), precision);

        //timer.println("base curve");

        Vertex[] startVertexConvex = getConvexVertexLine(startLine1, startLine2, convex);

        //timer.println("base convex curve");

        VertexInTriangle[][] vertices = getGrid(startLine1, startVertexConvex, startVertexConvex, startLine2, precision);

//               timer.println("grid");

        Triangle[] triangles = makeTrianglesFromVertexArray(vertices, color, precision);

//               timer.println("triangles");

        calcVertexNormal(triangles);

//             timer.println("normals");

        return triangles;
    }

    static Vertex[] getConvexVertexLine(Vertex[] line1, Vertex[] line2, float convex) {
        if ((line1 == null) || (line2 == null)) return null;
        if (line1.length != line2.length) throw new RuntimeException("different size arrays");
        int length = line1.length;
        Vertex[] result = new Vertex[length];

        Vertex sum;
        Vertex sub;
        Vertex normal;
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

    static VertexInTriangle[][] getGrid(Vertex[] start, Vertex[] p1, Vertex[] p2, Vertex[] finish, int precision) {
        VertexInTriangle[][] grid = new VertexInTriangle[precision][];
        for (int i = 0; i < precision; i++) {
            float[][] line = Bezier.getCurve(start[i].p, p1[i].p, p2[i].p, finish[i].p, precision);
            grid[i] = new VertexInTriangle[precision];
            for (int j = 0; j < precision; j++) {
                grid[i][j] = new VertexInTriangle(line[j]);
            }
        }
        return grid;
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

    static Triangle[] makeTrianglesFromVertexArray(VertexInTriangle[][] vertices, float[][][] colors, int precision) {
        Triangle[] triangles = new Triangle[2 * (precision - 1) * (precision - 1)];
        int count = 0;
        for (int i = 0; i < precision - 1; i++) {
            for (int j = 0; j < precision - 1; j++) {
                triangles[count++] = new Triangle(
                        vertices[i][j], vertices[i + 1][j], vertices[i][j + 1],
                        colors[i][j], colors[i + 1][j], colors[i][j + 1]);
                triangles[count++] = new Triangle(
                        vertices[i][j + 1], vertices[i + 1][j], vertices[i + 1][j + 1],
                        colors[i][j + 1], colors[i + 1][j], colors[i + 1][j + 1]);
            }
        }
        return triangles;
    }

    static void calcVertexNormal(Triangle[] triangles) {
        for (Triangle triangle : triangles) {
            triangle.v1.calcVertexNormal();
            triangle.v2.calcVertexNormal();
            triangle.v3.calcVertexNormal();
        }
    }

    static Pointers makePointersFromTriangles(Triangle[] triangles) {
        int size = 3 * triangles.length;
        float[] colors = new float[4 * size];
        float[] normals = new float[3 * size];
        float[] vertices = new float[3 * size];
        int cOffset = 0;
        int nOffset = 0;
        int vOffset = 0;

        for (Triangle triangle : triangles) {
            cOffset = triangle.getColorsFloatArray(colors, cOffset);
            vOffset = triangle.getVerticesFloatArray(vertices, vOffset);
            nOffset = triangle.getNormalsFloatArray(normals, nOffset);
        }

        return new Pointers(floatToBuffer(colors), floatToBuffer(normals), floatToBuffer(vertices), size);
    }

    static FloatBuffer floatToBuffer(float[] array) {
        ByteBuffer bb = ByteBuffer.allocateDirect(array.length * 4);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer result = bb.asFloatBuffer();
        result.put(array);
        result.position(0);
        return result;
    }

}

