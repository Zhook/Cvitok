package net.vc9ufi.cvitok.control;

import android.view.MotionEvent;
import android.view.View;

public class Motion implements View.OnTouchListener {

    private float x = 0;
    private float y = 0;
    private float r = 0;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                x = event.getX();
                y = event.getY();
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                float dx = event.getX(0) - event.getX(1);
                float dy = event.getY(0) - event.getY(1);
                r = (float) Math.sqrt(dx * dx + dy * dy);
                break;
            case MotionEvent.ACTION_MOVE:
                if (event.getPointerCount() > 1) {
                    dx = event.getX(0) - event.getX(1);
                    dy = event.getY(0) - event.getY(1);
                    float new_r = (float) Math.sqrt(dx * dx + dy * dy);
                    float dr = new_r - r;
                    multiMove(dr, dx, dy);
                    r = new_r;
                } else {
                    dx = x - event.getX();
                    dy = y - event.getY();
                    x = event.getX();
                    y = event.getY();
                    singleMove(dx, dy);
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                if (event.getPointerId(event.getActionIndex()) == 0) {
                    x = event.getX(1);
                    y = event.getY(1);
                } else {
                    x = event.getX(0);
                    y = event.getY(0);
                }
                break;
        }
        return true;
    }

    public void singleMove(float dx, float dy){};

    public void multiMove(float dr, float dx, float dy){};
}
