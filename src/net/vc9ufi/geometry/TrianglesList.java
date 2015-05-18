package net.vc9ufi.geometry;


import net.vc9ufi.cvitok.render.Pointers;

import javax.microedition.khronos.opengles.GL10;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.LinkedList;
import java.util.ListIterator;

public class TrianglesList extends LinkedList<Triangle> {

    private static final Painter mPainter = new Painter() {
        @Override
        public void paint(GL10 gl, FloatBuffer vertex, FloatBuffer color, FloatBuffer normal, int size) {
            gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertex);
            gl.glColorPointer(4, GL10.GL_FLOAT, 0, color);
            gl.glNormalPointer(GL10.GL_FLOAT, 0, normal);
            gl.glDrawArrays(GL10.GL_TRIANGLES, 0, size);
        }
    };

    public LinkedList<Pointers> getPointers() {
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
        LinkedList<Pointers> pointers = new LinkedList<>();
        pointers.add(new Pointers(floatToBuffer(colors), floatToBuffer(normals), floatToBuffer(vertices), size, mPainter));
        return pointers;
    }


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


    static FloatBuffer floatToBuffer(float[] array) {
        ByteBuffer bb = ByteBuffer.allocateDirect(array.length * 4);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer result = bb.asFloatBuffer();
        result.put(array);
        result.position(0);
        return result;
    }
}