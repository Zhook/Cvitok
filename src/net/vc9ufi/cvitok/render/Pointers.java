package net.vc9ufi.cvitok.render;

import java.nio.FloatBuffer;

public class Pointers {

    private FloatBuffer vertex;
    private FloatBuffer color;
    private FloatBuffer normal;

    private int size = 0;

    public Pointers(FloatBuffer color, FloatBuffer normal, FloatBuffer vertex, int size) {
        this.color = color;
        this.normal = normal;
        this.vertex = vertex;
        this.size = size;
    }

    public FloatBuffer getColor() {
        return color;
    }

    public FloatBuffer getNormal() {
        return normal;
    }

    public FloatBuffer getVertex() {
        return vertex;
    }

    public int getSize() {
        return size;
    }

}
