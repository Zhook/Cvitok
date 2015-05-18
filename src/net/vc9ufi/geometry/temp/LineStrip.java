package net.vc9ufi.geometry.temp;

import net.vc9ufi.geometry.Vector3f;

import java.security.InvalidParameterException;
import java.util.LinkedList;

public class LineStrip extends GlObject {
    LinkedList<Vector3f> mPoints;
    LinkedList<float[]> mColor;

    public LineStrip(int tag) {
        super(tag);
        mPoints = new LinkedList<>();
        mColor = new LinkedList<>();
    }

    public void addPoint(Vector3f point, float[] color) {
        if (color == null)
            throw new InvalidParameterException("color null");
        if (color.length != 4)
            throw new InvalidParameterException("color length != 4");
        mPoints.add(point);
        mColor.add(color);
    }

    public float[] getVerticesFloatArray() {
        float[] vertices = new float[mPoints.size()];
        int offset = 0;
        for (Vector3f point : mPoints) {
            vertices[offset++] = point.p[0];
            vertices[offset++] = point.p[1];
            vertices[offset++] = point.p[2];
        }
        return vertices;
    }

    public float[] getColorsFloatArray() {
        float[] colors = new float[mColor.size()];
        int offset = 0;
        for (float[] color : mColor) {
            colors[offset++] = color[0];
            colors[offset++] = color[1];
            colors[offset++] = color[2];
        }
        return colors;
    }

    public int getSize(){
        return mPoints.size();
    }
}
