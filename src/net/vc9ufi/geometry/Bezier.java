package net.vc9ufi.geometry;

public class Bezier {

    public static float[][] getCurve(float[] p0, float[] p1, float[] p2, float[] p3, int precision) {
        if ((p0 == null) || (p1 == null) || (p2 == null) || (p3 == null))
            throw new NullPointerException();

        int dimension = p0.length;
        if ((p1.length != dimension) || (p2.length != dimension) || (p3.length != dimension))
            throw new RuntimeException("Wrong dimension");


        float step = 1.0f / (float) (precision - 1);
        float[][] vertices = new float[precision][dimension];

        float t = 0.0f;
        float tQuad;
        float tCube;
        float antiT;
        float antiTQuad;
        float antiTCube;


        for (int i = 0; i < precision; i++) {
            float[] point = new float[dimension];
            antiT = 1.0f - t;
            antiTQuad = antiT * antiT;
            antiTCube = antiTQuad * antiT;
            tQuad = t * t;
            tCube = tQuad * t;

            for (int j = 0; j < dimension; j++) {
                point[j] = antiTCube * p0[j] + 3 * t * antiTQuad * p1[j] + 3 * tQuad * antiT * p2[j] + tCube * p3[j];
            }

            vertices[i] = point;
            t += step;
        }

        return vertices;
    }

    public static Vertex[] getCurve(Vertex[] param, int precision) {
        if (param == null) throw new NullPointerException("param");
        float[][] curve = getCurve(param[0].p, param[1].p, param[2].p, param[3].p, precision);
        Vertex[] vertices = new Vertex[precision];
        for (int i = 0; i < vertices.length; i++)
            vertices[i] = new Vertex(curve[i]);
        return vertices;
    }
}