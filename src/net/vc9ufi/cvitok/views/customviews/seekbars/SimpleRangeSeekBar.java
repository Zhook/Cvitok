package net.vc9ufi.cvitok.views.customviews.seekbars;


import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import org.jetbrains.annotations.NotNull;

public class SimpleRangeSeekBar extends View {


    private RangeSeekBarCanvas mRangeSeekBarCanvas;

    public SimpleRangeSeekBar(Context context) {
        this(context, null);
    }

    public SimpleRangeSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SimpleRangeSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SimpleRangeSeekBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    protected void init(Context context, AttributeSet attrs) {

        mRangeSeekBarCanvas = new RangeSeekBarCanvas(context, attrs);

        setFocusable(true);
        setFocusableInTouchMode(true);
    }


    @Override
    public boolean onTouchEvent(@NotNull MotionEvent event) {
        super.onTouchEvent(event);

        if (!isEnabled()) {
            return false;
        }

        float x = event.getX(event.findPointerIndex(event.getPointerId(event.getPointerCount() - 1)));
        final int action = event.getAction();
        switch (action & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_DOWN:
                boolean first = isInsideFirstThumb(x);
                mRangeSeekBarCanvas.setFirstThumbPressed(first);
                if (!first)
                    mRangeSeekBarCanvas.setSecondThumbPressed(isInsideSecondThumb(x));
                invalidate();
                break;

            case MotionEvent.ACTION_MOVE:
                mRangeSeekBarCanvas.setValue(x);
                invalidate();
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (mRangeSeekBarCanvas.isFirstThumbPressed() || mRangeSeekBarCanvas.isSecondThumbPressed()) {
                    if (onChangeValuesListener != null)
                        onChangeValuesListener.OnChange(mRangeSeekBarCanvas.getFirstValue(), mRangeSeekBarCanvas.getSecondValue());
                    mRangeSeekBarCanvas.setFirstThumbPressed(false);
                    mRangeSeekBarCanvas.setSecondThumbPressed(false);
                }
                invalidate();
                break;
        }
        return true;
    }

    protected boolean isInsideFirstThumb(float x) {
        RectF rect = mRangeSeekBarCanvas.getFirstThumbRect();
        return (x >= rect.left) && (x <= rect.right);
    }

    protected boolean isInsideSecondThumb(float x) {
        RectF rect = mRangeSeekBarCanvas.getSecondThumbRect();
        return (x >= rect.left) && (x <= rect.right);
    }


    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mRangeSeekBarCanvas.draw(canvas, null);
    }


    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Point minSize = mRangeSeekBarCanvas.getMinCanvasSize();
        setMeasuredDimension(
                resolveSizeAndState(minSize.x, widthMeasureSpec, 0),
                resolveSizeAndState(minSize.y, heightMeasureSpec, 0));
    }


    public float getFirstValue() {
        return mRangeSeekBarCanvas.getFirstValue();
    }

    public void setFirstValue(float value) {
        mRangeSeekBarCanvas.setFirstValue(value);
        invalidate();
    }

    public float getSecondValue() {
        return mRangeSeekBarCanvas.getSecondValue();
    }

    public void setSecondValue(float value) {
        mRangeSeekBarCanvas.setSecondValue(value);
        invalidate();
    }


    private OnChangeValuesListener onChangeValuesListener;

    public void setOnChangeValuesListener(OnChangeValuesListener onChangeValuesListener) {
        this.onChangeValuesListener = onChangeValuesListener;
    }

    public interface OnChangeValuesListener {

        void OnChange(float leftValue, float rightValue);
    }
}
