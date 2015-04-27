package net.vc9ufi.geometry;

import java.nio.FloatBuffer;

public class Triangle {

    public final int TAG;

    public VertexInTriangle v1;
    public VertexInTriangle v2;
    public VertexInTriangle v3;

    public Vertex n;

    public float[] c1;
    public float[] c2;
    public float[] c3;

    public Triangle(VertexInTriangle v1, VertexInTriangle v2, VertexInTriangle v3, float[] c1, float[] c2, float[] c3, int tag) {
        setV1(v1);
        setV2(v2);
        setV3(v3);
        n = calculateNotNormalizeNormal(v1, v2, v3);
        this.c1 = c1;
        this.c2 = c2;
        this.c3 = c3;
        TAG = tag;
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

    public static Vertex calculateNotNormalizeNormal(Vertex v1, Vertex v2, Vertex v3) {
        float[] v1_ = new float[]{v1.p[0] - v2.p[0], v1.p[1] - v2.p[1], v1.p[2] - v2.p[2]};
        float[] v2_ = new float[]{v1.p[0] - v3.p[0], v1.p[1] - v3.p[1], v1.p[2] - v3.p[2]};

        float[] n = new float[3];
        n[0] = v1_[1] * v2_[2] - v1_[2] * v2_[1];
        n[1] = v1_[2] * v2_[0] - v1_[0] * v2_[2];
        n[2] = v1_[0] * v2_[1] - v1_[1] * v2_[0];

        return new Vertex(n);
    }
}
