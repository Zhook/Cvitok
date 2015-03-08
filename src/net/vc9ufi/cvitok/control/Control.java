package net.vc9ufi.cvitok.control;

import android.view.MotionEvent;
import android.view.View;
import net.vc9ufi.cvitok.data.Flower;
import net.vc9ufi.cvitok.petal.Petal;
import net.vc9ufi.geometry.LookAt;

public class Control {

    private static final Control control = new Control();

    LookAt flower_lookat = new LookAt();

    private Control() {
    }

    public static Control getInstance() {
        return control;
    }

    public boolean onTouchSurface(View v, MotionEvent event) {

        switch (mode) {
            case NULL:
            case FLOWER:
                return flower_lookat.motionEvent(event);
            case PETAL:
                return motionEventPetals.motionEvent(event);
            case VERTEX:
                return motionEventVertices.motionEvent(event);
            case LIGHT:
                return motionEventLight.motionEvent(event);
        }
        return true;
    }

    public float[] getLookAtVectors() {
        return flower_lookat.getCameraTargetUp();
    }


    Motion motionEventPetals = new Motion() {
        @Override
        public void singleMove(float dx, float dy) {
            Petal petal = Flower.getInstance().getSelectedPetal();
            if (petal != null) petal.movePetal(dx, dy);
        }

        @Override
        public void multiMove(float dr, float dx, float dy) {
            Petal petal = Flower.getInstance().getSelectedPetal();
            if (petal != null) petal.changeQuantity(dr);
        }
    };

    Motion motionEventVertices = new Motion() {
        @Override
        public void singleMove(float dx, float dy) {
            Flower flower = Flower.getInstance();
            Petal petal = flower.getSelectedPetal();
            if (petal != null) petal.rotatePoints(dx, dy, flower.getLeft(), flower.getRight());
        }

        @Override
        public void multiMove(float dr, float dx, float dy) {
            Flower flower = Flower.getInstance();
            Petal petal = flower.getSelectedPetal();
            if (petal != null) petal.movePoints(dr, flower.getLeft(), flower.getRight());
        }
    };

    Motion motionEventLight = new Motion() {
        @Override
        public void singleMove(float dx, float dy) {
            Flower.getInstance().getLight().single(dx, dy);
        }

        @Override
        public void multiMove(float dr, float dx, float dy) {
            Flower.getInstance().getLight().multi(dr);
        }
    };


    //--------------------------------------------------------------------------------------Mode
    public void setMode(MODE mode) {
        this.mode = mode;
    }

    public boolean isMode(MODE m) {
        return mode == m;
    }

    private MODE mode = MODE.NULL;

    public enum MODE {NULL, FLOWER, PETAL, VERTEX, LIGHT}
}
