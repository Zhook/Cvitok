package net.vc9ufi.geometry.temp;

import net.vc9ufi.cvitok.render.Pointers;
import net.vc9ufi.geometry.Painter;

import javax.microedition.khronos.opengles.GL10;
import java.nio.FloatBuffer;
import java.util.Collection;
import java.util.LinkedList;


public class LineStripsList extends GlObjectsList {

    private static final Painter mPainter = new Painter() {
        @Override
        public void paint(GL10 gl, FloatBuffer vertex, FloatBuffer color, FloatBuffer normal, int size) {
            gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertex);
            gl.glColorPointer(4, GL10.GL_FLOAT, 0, color);
            gl.glDrawArrays(GL10.GL_LINE_STRIP, 0, size);
        }
    };

    @Override
    public boolean add(GlObject object) {
        if (!(object instanceof LineStrip))
            throw new ClassCastException("need LineStrip class");
        return super.add(object);
    }

    @Override
    public void add(int location, GlObject object) {
        if (!(object instanceof LineStrip))
            throw new ClassCastException("need LineStrip class");
        super.add(location, object);
    }

    @Override
    public void addLast(GlObject object) {
        if (!(object instanceof LineStrip))
            throw new ClassCastException("need LineStrip class");
        super.addLast(object);
    }

    @Override
    public void addFirst(GlObject object) {
        if (!(object instanceof LineStrip))
            throw new ClassCastException("need LineStrip class");
        super.addFirst(object);
    }

    @Override
    public boolean addAll(Collection<? extends GlObject> collection) {
        if (!(collection instanceof LineStripsList))
            throw new ClassCastException("need LineStripsList Collection");
        return super.addAll(collection);
    }

    @Override
    public boolean addAll(int location, Collection<? extends GlObject> collection) {
        if (!(collection instanceof LineStripsList))
            throw new ClassCastException("need LineStripsList Collection");
        return super.addAll(location, collection);
    }

    @Override
    public LinkedList<Pointers> getPointers() {
        LinkedList<Pointers> pointers = new LinkedList<>();

        for (GlObject line : this) {
            float[] vertices = ((LineStrip) line).getVerticesFloatArray();
            float[] colors = ((LineStrip) line).getColorsFloatArray();
            Pointers p = new Pointers(floatToBuffer(colors), null, floatToBuffer(vertices), ((LineStrip) line).getSize(), mPainter);
            pointers.add(p);
        }

        return pointers;
    }
}
