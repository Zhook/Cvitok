package net.vc9ufi.geometry;

import java.nio.FloatBuffer;

public class Triangle {

    public VertexInTriangle v1;
    public VertexInTriangle v2;
    public VertexInTriangle v3;

    public Vertex n;

    public float[] c1;
    public float[] c2;
    public float[] c3;

    public Triangle(VertexInTriangle v1, VertexInTriangle v2, VertexInTriangle v3, float[] c1, float[] c2, float[] c3) {
        setV1(v1);
        setV2(v2);
        setV3(v3);
        n = VertexInTriangle.calculateNotNormalizeNormal(v1, v2, v3);
        this.c1 = c1;
        this.c2 = c2;
        this.c3 = c3;
    }

    public void setV1(VertexInTriangle v1) {
        this.v1 = v1;
        v1.addTriangle(this);
    }

    public void setV2(VertexInTriangle v2) {
        this.v2 = v2;
        v2.addTriangle(this);
    }

    public void setV3(VertexInTriangle v3) {
        this.v3 = v3;
        v3.addTriangle(this);
    }

    public void getVerticesFloatBuffer(FloatBuffer verticesBuffer) {
        verticesBuffer
                .put(v1.p)
                .put(v2.p)
                .put(v3.p);
    }

    public void getNormalsFloatBuffer(FloatBuffer normalsBuffer) {
        normalsBuffer
                .put(v1.normal.p)
                .put(v2.normal.p)
                .put(v3.normal.p);
    }

    public void getColorsFloatBuffer(FloatBuffer colorsBuffer) {
        colorsBuffer
                .put(c1)
                .put(c2)
                .put(c3);
    }


    public int getVerticesFloatArray(float[] vertices, int offset) {
        for (int i = 0; i < 3; i++)
            vertices[offset++] = v1.p[i];
        for (int i = 0; i < 3; i++)
            vertices[offset++] = v2.p[i];
        for (int i = 0; i < 3; i++)
            vertices[offset++] = v3.p[i];
        return offset;
    }

    public int getNormalsFloatArray(float[] normals, int offset) {
        for (int i = 0; i < 3; i++)
            normals[offset++] = v1.normal.p[i];
        for (int i = 0; i < 3; i++)
            normals[offset++] = v2.normal.p[i];
        for (int i = 0; i < 3; i++)
            normals[offset++] = v3.normal.p[i];
        return offset;
    }

    public int getColorsFloatArray(float[] colors, int offset) {
        for (int i = 0; i < 4; i++)
            colors[offset++] = c1[i];
        for (int i = 0; i < 4; i++)
            colors[offset++] = c2[i];
        for (int i = 0; i < 4; i++)
            colors[offset++] = c3[i];
        return offset;
    }

//    public void rotate(Quaternion q) {
//        v1 = new Vertex(Quaternion.Rotate(v1.p, q));
//        v1.addTriangle(this);
//        v2 = new Vertex(Quaternion.Rotate(v2.p, q));
//        v2.addTriangle(this);
//        v3 = new Vertex(Quaternion.Rotate(v3.p, q));
//        v3.addTriangle(this);
//        n = new Vertex(Quaternion.Rotate(n.p, q));
//        n1 = new Vertex(Quaternion.Rotate(n1.p, q));
//        n2 = new Vertex(Quaternion.Rotate(n2.p, q));
//        n3 = new Vertex(Quaternion.Rotate(n3.p, q));
//    }
//
//    public static Triangle rotate(Triangle triangle, Quaternion q) {
//        Triangle result = new Triangle();
//        result.v1 = new Vertex(Quaternion.Rotate(triangle.v1.p, q));
//        result.v2 = new Vertex(Quaternion.Rotate(triangle.v2.p, q));
//        result.v3 = new Vertex(Quaternion.Rotate(triangle.v3.p, q));
//        result.n1 = new Vertex(Quaternion.Rotate(triangle.n1.p, q));
//        result.n2 = new Vertex(Quaternion.Rotate(triangle.n2.p, q));
//        result.n3 = new Vertex(Quaternion.Rotate(triangle.n3.p, q));
//        result.c1 = triangle.c1.clone();
//        result.c2 = triangle.c3.clone();
//        result.c3 = triangle.c2.clone();
//        return result;
//    }
}
