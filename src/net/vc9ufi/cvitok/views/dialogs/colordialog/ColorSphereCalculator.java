package net.vc9ufi.cvitok.views.dialogs.colordialog;

import net.vc9ufi.cvitok.data2.utils.Calculator;
import net.vc9ufi.geometry.Icosahedron;
import net.vc9ufi.geometry.Triangle;
import net.vc9ufi.geometry.Vertex;

import java.util.LinkedList;

public class ColorSphereCalculator extends Calculator {

    private final float RADIUS;
    private final int QUALITY;

    int quantity = 0;                   // = 20*(4^QUALITY)


    public ColorSphereCalculator(float radius, int quality) {
        RADIUS = radius;
        QUALITY = quality;
    }

    @Override
    public LinkedList<Triangle> calculate() {
        LinkedList<Triangle> triangles = new LinkedList<>();
        quantity = Icosahedron.CalcIcosahedron(triangles, RADIUS, QUALITY);

        for (Triangle t : triangles) {
            for (int j = 0; j < 3; j++) {
                t.v1.calcVertexNormal();
                t.v2.calcVertexNormal();
                t.v3.calcVertexNormal();
                t.c1 = colorFromNorm(t.v1.normal);
                t.c2 = colorFromNorm(t.v2.normal);
                t.c3 = colorFromNorm(t.v3.normal);
            }
        }

        return triangles;
    }


    static float[] colorFromNorm(Vertex n) {
        float[] color = new float[4];
        color[0] = 0.5f + n.p[0];
        color[1] = 0.5f + n.p[1];
        color[2] = 0.5f + n.p[2];
        color[3] = 1.0f;
        return color;
    }


}
