package net.vc9ufi.geometry.temp;

import net.vc9ufi.cvitok.render.Pointers;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.LinkedList;
import java.util.ListIterator;

public abstract class GlObjectsList extends LinkedList<GlObject> {
    public void deleteWithTag(int tag) {
        ListIterator<GlObject> iterator = listIterator();
        while (iterator.hasNext())
            if (iterator.next().TAG == tag)
                iterator.remove();
    }

    public void deleteWithTagMoreThan(int tag) {
        ListIterator<GlObject> iterator = listIterator();
        while (iterator.hasNext())
            if (iterator.next().TAG > tag)
                iterator.remove();
    }

    abstract public LinkedList<Pointers> getPointers();

    static FloatBuffer floatToBuffer(float[] array) {
        ByteBuffer bb = ByteBuffer.allocateDirect(array.length * 4);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer result = bb.asFloatBuffer();
        result.put(array);
        result.position(0);
        return result;
    }
}
