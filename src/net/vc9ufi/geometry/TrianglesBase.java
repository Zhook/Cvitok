package net.vc9ufi.geometry;


import net.vc9ufi.cvitok.data.RangeComparator;
import net.vc9ufi.cvitok.data.Calculator;
import net.vc9ufi.cvitok.data.ExecutorServiceExt;
import net.vc9ufi.cvitok.data.ThreadFactoryWithPriority;
import net.vc9ufi.cvitok.render.Pointers;

import java.util.Collections;
import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class TrianglesBase {

    ExecutorServiceExt executor = new ExecutorServiceExt(2, 2,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(), new ThreadFactoryWithPriority(Thread.MIN_PRIORITY)) {
        @Override
        public void allFinished() {
            synchronized (triangles) {
                Collections.sort(triangles, new RangeComparator(camera.clone()));
                pointers = triangles.getPointers();
            }
        }
    };

    final Triangles triangles = new Triangles();

    volatile boolean mTransparency = false;
    volatile float[] mBackground = new float[]{1, 1, 1, 1};

    volatile float[] camera = new float[]{0, 0, 0};
    volatile float[] target = new float[]{0, 0, 0};
    volatile float[] up = new float[]{0, 0, 0};

    volatile Pointers pointers;


    public void setBackgroundColor(float[] color) {
        mBackground = color;
    }

    public float[] getBackgroundColor() {
        return mBackground;
    }

    public boolean isTransparency() {
        return mTransparency;
    }

    public void setTransparency(boolean transparency) {
        this.mTransparency = transparency;
    }

    public void setLookAt(float[] camera, float[] target, float[] up) {
        this.camera = camera;
        this.target = target;
        this.up = up;
    }

    public void setLookAt(float[] camera, float[] target, float[] up, boolean sort) {
        this.camera = camera;
        this.target = target;
        this.up = up;
        if (sort) sort();
    }

    public float[] getCamera() {
        return camera;
    }

    public float[] getTarget() {
        return target;
    }

    public float[] getUp() {
        return up;
    }

    public Pointers getPointers() {
        return pointers;
    }


    public void deleteWithTagMoreThan(int tag) {
        triangles.deleteWithTagMoreThan(tag);
    }

    public void clear() {
        synchronized (triangles) {
            triangles.clear();
        }
    }

    public void add(final Calculator calculator, final int tag) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                LinkedList<Triangle> tri = calculator.calculate();
                synchronized (triangles) {
                    triangles.deleteWithTag(tag);
                    triangles.addAll(tri);
                }
            }
        };
        executor.execute(runnable);
    }

    public void sort() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
            }
        };
        executor.execute(runnable);
    }
}
