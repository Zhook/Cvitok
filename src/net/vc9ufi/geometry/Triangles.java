package net.vc9ufi.geometry;


import net.vc9ufi.cvitok.render.Pointers;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.LinkedList;
import java.util.ListIterator;

public class Triangles extends LinkedList<Triangle> {

    public void deleteWithTag(int tag) {
        ListIterator<Triangle> iterator = listIterator();
        while (iterator.hasNext())
            if (iterator.next().TAG == tag)
                iterator.remove();
    }

    public void deleteWithTagMoreThan(int tag) {
        ListIterator<Triangle> iterator = listIterator();
        while (iterator.hasNext())
            if (iterator.next().TAG > tag)
                iterator.remove();
    }

    public Pointers getPointers() {
        int size = 3 * this.size();
        float[] colors = new float[4 * size];
        float[] normals = new float[3 * size];
        float[] vertices = new float[3 * size];
        int cOffset = 0;
        int nOffset = 0;
        int vOffset = 0;

        for (Triangle triangle : this) {
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