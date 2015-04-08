package net.vc9ufi.cvitok.petal;

import android.view.View;
import net.vc9ufi.cvitok.control.Motion;
import net.vc9ufi.cvitok.data.Flower;
import net.vc9ufi.cvitok.data.Parameters;
import net.vc9ufi.cvitok.data.SelectedVertices;
import net.vc9ufi.cvitok.views.settings.Setting;
import net.vc9ufi.geometry.Quaternion;
import net.vc9ufi.geometry.Vector3D;

public class Petal {

    private Parameters parameters;
    private Calculator calculator = new Calculator();
    private View.OnTouchListener mPetalOnTouchListener;
    private View.OnTouchListener mVerticesOnTouchListener;

    public Petal(Parameters parameters, int precision, final Flower flower) {
        mPetalOnTouchListener = new Motion() {

            @Override
            public void singleMove(float dx, float dy) {
                movePetal(dx, dy);
            }

            @Override
            public void multiMove(float dr, float dx, float dy) {
                changeQuantity(dr);
            }
        };

        mVerticesOnTouchListener = new Motion() {

            @Override
            public void singleMove(float dx, float dy) {
                rotatePoints(dx, dy, flower.getLeft(), flower.getRight());
            }

            @Override
            public void multiMove(float dr, float dx, float dy) {
                movePoints(dr, flower.getLeft(), flower.getRight());
            }
        };

        this.parameters = parameters;
        try {
            calculator.setParameters(parameters.clone());
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        calculator.setQuality(precision);
        calculator.start();
    }


    public void setName(String name) {
        parameters.setName(name);
    }

    public String getName() {
        return parameters.name;
    }

    public void setQuality(int quality) {
        calculator.setQuality(quality);
    }

    public View.OnTouchListener getPetalOnTouchListener() {
        return mPetalOnTouchListener;
    }

    public View.OnTouchListener getVerticesOnTouchListener() {
        return mVerticesOnTouchListener;
    }

    //------------------------------------
    public void changeQuantity(float dr) {
        parameters.changeQuantity(dr);
        try {
            calculator.setParameters(parameters.clone());
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    public void rotatePoints(float dx, float dy, SelectedVertices left, SelectedVertices right) {
        boolean need_calc = false;


        if (left.start) {
            parameters.left.coord.start.p = rotatePoint(dx, dy, UP, ROTATE_STEP, parameters.left.coord.start.p);
            need_calc = true;
        }
        if (left.p1) {
            parameters.left.coord.p1.p = rotatePoint(dx, dy, UP, ROTATE_STEP, parameters.left.coord.p1.p);
            need_calc = true;
        }
        if (left.p2) {
            parameters.left.coord.p2.p = rotatePoint(dx, dy, UP, ROTATE_STEP, parameters.left.coord.p2.p);
            need_calc = true;
        }
        if (left.finish) {
            parameters.left.coord.finish.p = rotatePoint(dx, dy, UP, ROTATE_STEP, parameters.left.coord.finish.p);
            need_calc = true;
        }


        if (right.start) {
            parameters.right.coord.start.p = rotatePoint(dx, dy, UP, ROTATE_STEP, parameters.right.coord.start.p);
            need_calc = true;
        }
        if (right.p1) {
            parameters.right.coord.p1.p = rotatePoint(dx, dy, UP, ROTATE_STEP, parameters.right.coord.p1.p);
            need_calc = true;
        }
        if (right.p2) {
            parameters.right.coord.p2.p = rotatePoint(dx, dy, UP, ROTATE_STEP, parameters.right.coord.p2.p);
            need_calc = true;
        }
        if (right.finish) {
            parameters.right.coord.finish.p = rotatePoint(dx, dy, UP, ROTATE_STEP, parameters.right.coord.finish.p);
            need_calc = true;
        }

        if (need_calc)
            try {
                calculator.setParameters(parameters.clone());
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
    }

    public void movePoints(float dr, SelectedVertices left, SelectedVertices right) {
        boolean need_calc = false;
        dr = dr * Setting.MODIFY_SENSITIVITY_MOVE;
        if (left.start) {
            parameters.left.coord.start.divLength(dr);
            need_calc = true;
        }
        if (left.p1) {
            parameters.left.coord.p1.divLength(dr);
            need_calc = true;
        }
        if (left.p2) {
            parameters.left.coord.p2.divLength(dr);
            need_calc = true;
        }
        if (left.finish) {
            parameters.left.coord.finish.divLength(dr);
            need_calc = true;
        }


        if (right.start) {
            parameters.right.coord.start.divLength(dr);
            need_calc = true;
        }
        if (right.p1) {
            parameters.right.coord.p1.divLength(dr);
            need_calc = true;
        }
        if (right.p2) {
            parameters.right.coord.p2.divLength(dr);
            need_calc = true;
        }
        if (right.finish) {
            parameters.right.coord.finish.divLength(dr);
            need_calc = true;
        }

        if (need_calc)
            try {
                calculator.setParameters(parameters.clone());
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
    }

    public void movePetal(float dx, float dy) {
        Quaternion q;
        if (Math.abs(dx) > Math.abs(dy)) {
            q = Quaternion.FromAxisAndAngle(UP, dx * ROTATE_STEP);
        } else {
            float[] left = Vector3D.cross(UP, parameters.left.coord.finish.p);
            q = Quaternion.FromAxisAndAngle(left, dy * ROTATE_STEP);
        }

        parameters.left.coord.p1.p = Quaternion.Rotate(parameters.left.coord.p1.p, q);
        parameters.right.coord.p1.p = Quaternion.Rotate(parameters.right.coord.p1.p, q);
        parameters.left.coord.p2.p = Quaternion.Rotate(parameters.left.coord.p2.p, q);
        parameters.right.coord.p2.p = Quaternion.Rotate(parameters.right.coord.p2.p, q);
        parameters.left.coord.finish.p = Quaternion.Rotate(parameters.left.coord.finish.p, q);
        parameters.right.coord.finish.p = Quaternion.Rotate(parameters.right.coord.finish.p, q);

        try {
            calculator.setParameters(parameters.clone());
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    private static float[] rotatePoint(float dx, float dy, float[] up, float step, float[] point) {
        Quaternion q;
        float[] out;
        if (Math.abs(dx) > Math.abs(dy)) {
            q = Quaternion.FromAxisAndAngle(up, dx * step);
        } else {
            float[] left = Vector3D.cross(up, point);
            q = Quaternion.FromAxisAndAngle(left, dy * step);
        }
        out = Quaternion.Rotate(point, q);
        return out;
    }


    public void setColor(float[] color, SelectedVertices left, SelectedVertices right) {
        boolean need_calc = false;

        if (left.start) {
            parameters.left.colors.start = color;
            need_calc = true;
        }
        if (left.p1) {
            parameters.left.colors.p1 = color;
            need_calc = true;
        }
        if (left.p2) {
            parameters.left.colors.p2 = color;
            need_calc = true;
        }
        if (left.finish) {
            parameters.left.colors.finish = color;
            need_calc = true;
        }


        if (right.start) {
            parameters.right.colors.start = color;
            need_calc = true;
        }
        if (right.p1) {
            parameters.right.colors.p1 = color;
            need_calc = true;
        }
        if (right.p2) {
            parameters.right.colors.p2 = color;
            need_calc = true;
        }
        if (right.finish) {
            parameters.right.colors.finish = color;
            need_calc = true;
        }

        if (need_calc)
            try {
                calculator.setParameters(parameters.clone());
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
    }


    public Pointers getPointers() {
        return calculator.getPointers();
    }

    public Parameters getParameters() {
        return parameters;
    }


    private static final float[] UP = new float[]{0, 0, 1};
    private static final float ROTATE_STEP = 0.003f;
}
