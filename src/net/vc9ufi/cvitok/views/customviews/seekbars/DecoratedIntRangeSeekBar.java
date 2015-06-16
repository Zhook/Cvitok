package net.vc9ufi.cvitok.views.customviews.seekbars;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import net.vc9ufi.cvitok.R;
import org.jetbrains.annotations.NotNull;

public class DecoratedIntRangeSeekBar extends View {

    public static final int DEF_TEXT_SIZE = 14;
    public static final int DEF_TEXT_COLOR = Color.WHITE;
    public static final int DEF_MIN_VALUE = 0;
    public static final int DEF_MAX_VALUE = 100;
    public static final int DEF_FIRST_VALUE = 10;
    public static final int DEF_SECOND_VALUE = 30;


    private int mTextSize;
    private int mTextColor;
    private String mMinLabel;
    private String mMaxLabel;
    private boolean mValuelessMinMax;
    private boolean mShowMinMax;


    private float mMaxLabelWidth;

    private Formatter formatter;

    private Rect mWorkRect = new Rect();

    private RangeSeekBarCanvas mRangeSeekBarCanvas;
    private Rect mRangeSeekBarRect;


    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public DecoratedIntRangeSeekBar(Context context) {
        this(context, null);
    }

    public DecoratedIntRangeSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public DecoratedIntRangeSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DecoratedIntRangeSeekBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }


    protected void init(Context context, AttributeSet attrs) {


        mRangeSeekBarCanvas = new RangeSeekBarCanvas(context, attrs);
        mRangeSeekBarRect = new Rect();

        setAttributes(context, attrs);

        setFocusable(true);
        setFocusableInTouchMode(true);
    }

    public void setAttributes(Context context, AttributeSet attrs) {
        final TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.DecoratedSeekBar, 0, 0);
        String mPostfix = "";
        int minValue;
        int maxValue;
        int firstValue;
        int secondValue;
        try {
            mTextSize = attributes.getDimensionPixelSize(R.styleable.DecoratedSeekBar_textSize, DEF_TEXT_SIZE);
            mTextColor = attributes.getInt(R.styleable.DecoratedSeekBar_textColor, DEF_TEXT_COLOR);
            mPostfix = attributes.getString(R.styleable.DecoratedSeekBar_postfix);
            minValue = attributes.getInt(R.styleable.DecoratedSeekBar_minValue, DEF_MIN_VALUE);
            maxValue = attributes.getInt(R.styleable.DecoratedSeekBar_maxValue, DEF_MAX_VALUE);
            firstValue = attributes.getInt(R.styleable.DecoratedSeekBar_defFirstValue, DEF_FIRST_VALUE);
            secondValue = attributes.getInt(R.styleable.DecoratedSeekBar_defSecondValue, DEF_SECOND_VALUE);
            mMinLabel = attributes.getString(R.styleable.DecoratedSeekBar_minLabel);
            mMaxLabel = attributes.getString(R.styleable.DecoratedSeekBar_maxLabel);
            mShowMinMax = attributes.getBoolean(R.styleable.DecoratedSeekBar_showMinMax, true);
        } finally {
            attributes.recycle();
        }

        formatter = new Formatter(minValue, maxValue, mPostfix);

        mRangeSeekBarCanvas.setAttributes(context, attrs);
        mRangeSeekBarCanvas.setFirstValue(formatter.getFloatValue(firstValue));
        mRangeSeekBarCanvas.setSecondValue(formatter.getFloatValue(secondValue));
    }


    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        calcAllShit();

        Log.i("myout", "min: " + String.valueOf(formatter.min) + ", max: " + String.valueOf(formatter.max));
        Log.i("myout", "first: " + String.valueOf(mRangeSeekBarCanvas.getFirstValue()) + " / " + String.valueOf(formatter.getIntValue(mRangeSeekBarCanvas.getFirstValue())));
        Log.i("myout", "second: " + String.valueOf(mRangeSeekBarCanvas.getSecondValue()) + " / " + String.valueOf(formatter.getIntValue(mRangeSeekBarCanvas.getSecondValue())));

        mRangeSeekBarCanvas.draw(canvas, mRangeSeekBarRect);

        float textWidth;
        String text;
        RectF rect;
        mPaint.setTextSize(mTextSize);
        mPaint.setColor(mTextColor);

        if (mShowMinMax) {
            if (mValuelessMinMax) {
                text = mMinLabel;
                textWidth = mPaint.measureText(text);
                canvas.drawText(text, mWorkRect.left + mMaxLabelWidth - textWidth, mTextSize, mPaint);

                text = mMaxLabel;
                canvas.drawText(text, mWorkRect.right - mMaxLabelWidth, mTextSize, mPaint);
            } else {
                text = formatter.format(formatter.min);
                textWidth = mPaint.measureText(text);
                canvas.drawText(text, mWorkRect.left + mMaxLabelWidth - textWidth, mRangeSeekBarRect.bottom + mTextSize, mPaint);

                text = formatter.format(formatter.max);
                canvas.drawText(text, mWorkRect.right - mMaxLabelWidth, mRangeSeekBarRect.bottom + mTextSize, mPaint);

                text = formatter.format(mRangeSeekBarCanvas.getFirstValue());
                textWidth = mPaint.measureText(text);
                rect = mRangeSeekBarCanvas.getFirstThumbRect();
                canvas.drawText(text, rect.left + (rect.right - rect.left) / 2f - textWidth, mWorkRect.top + mTextSize, mPaint);

                text = formatter.format(mRangeSeekBarCanvas.getSecondValue());
                rect = mRangeSeekBarCanvas.getSecondThumbRect();
                canvas.drawText(text, rect.left + (rect.right - rect.left) / 2f, mWorkRect.top + mTextSize, mPaint);
            }
        } else {
            text = formatter.format(mRangeSeekBarCanvas.getFirstValue());
            textWidth = mPaint.measureText(text);
            rect = mRangeSeekBarCanvas.getFirstThumbRect();
            canvas.drawText(text, rect.left + (rect.right - rect.left) / 2f - textWidth, mWorkRect.top + mTextSize, mPaint);

            text = formatter.format(mRangeSeekBarCanvas.getSecondValue());
            rect = mRangeSeekBarCanvas.getSecondThumbRect();
            canvas.drawText(text, rect.left + (rect.right - rect.left) / 2f, mWorkRect.top + mTextSize, mPaint);
        }

    }


    private void calcAllShit() {
        mValuelessMinMax = (mMinLabel != null && mMaxLabel != null);

        mPaint.setTextSize(mTextSize);
        float minLabelWidth;
        float maxLabelWidth;
        if (mShowMinMax && mValuelessMinMax) {
            minLabelWidth = mPaint.measureText(mMinLabel);
            maxLabelWidth = mPaint.measureText(mMaxLabel);
        } else {
            minLabelWidth = mPaint.measureText(formatter.format(formatter.min));
            maxLabelWidth = mPaint.measureText(formatter.format(formatter.max));
        }

        mMaxLabelWidth = Math.max(minLabelWidth, maxLabelWidth);

        mWorkRect.left = getPaddingLeft();
        mWorkRect.top = getPaddingTop();
        mWorkRect.right = getWidth() - getPaddingRight();
        mWorkRect.bottom = getHeight() - getPaddingBottom();

        Point seekBarSize = mRangeSeekBarCanvas.getMinCanvasSize();
        mRangeSeekBarRect.left = mWorkRect.left;
        mRangeSeekBarRect.right = mWorkRect.right;
        if (!mValuelessMinMax)
            if (seekBarSize.x / 2f < mMaxLabelWidth) {
                mRangeSeekBarRect.left += (int) (mMaxLabelWidth - seekBarSize.x / 2f);
                mRangeSeekBarRect.right -= (int) (mMaxLabelWidth - seekBarSize.x / 2f);
            }
        mRangeSeekBarRect.top = mWorkRect.top + mTextSize;


        mRangeSeekBarRect.bottom = mRangeSeekBarRect.top + seekBarSize.y;
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
                        onChangeValuesListener.OnChange(
                                formatter.getIntValue(mRangeSeekBarCanvas.getFirstValue()),
                                formatter.getIntValue(mRangeSeekBarCanvas.getSecondValue()));
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
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Point seekBarSize = mRangeSeekBarCanvas.getMinCanvasSize();
        int width = getPaddingLeft() + getPaddingRight()
                + (int) Math.max(mMaxLabelWidth, seekBarSize.x);
        int height = getPaddingTop() + getPaddingBottom()
                + seekBarSize.y + mTextSize;
        if (mShowMinMax && !mValuelessMinMax)
            height += mTextSize;

        setMeasuredDimension(
                resolveSizeAndState(width, widthMeasureSpec, 0),
                resolveSizeAndState(height, heightMeasureSpec, 0));
    }


    public int getFirstValue() {
        return formatter.getIntValue(mRangeSeekBarCanvas.getFirstValue());
    }

    public void setFirstValue(int value) {
        mRangeSeekBarCanvas.setFirstValue(formatter.getFloatValue(value));
        invalidate();
    }

    public int getSecondValue() {
        return formatter.getIntValue(mRangeSeekBarCanvas.getSecondValue());
    }

    public void setSecondValue(int value) {
        mRangeSeekBarCanvas.setSecondValue(formatter.getFloatValue(value));
        invalidate();
    }


    public void setTextColor(int mTextColor) {
        this.mTextColor = mTextColor;
    }

    public void setTextSize(int mTextSize) {
        this.mTextSize = mTextSize;
    }

    public void setPostfix(String postfix) {
        formatter.setPostfix(postfix);
    }

    public void setMinValue(int value) {
        formatter.min = value;
        invalidate();
    }

    public void setMaxValue(int value) {
        formatter.max = value;
        invalidate();
    }

    public void setMaxLabel(String label) {
        this.mMaxLabel = label;
    }

    public void setMinLabel(String label) {
        this.mMinLabel = label;
    }


    class Formatter {
        int min;
        int max;
        private String postfix = "";

        public Formatter(int min, int max, String postfix) {
            this.max = max;
            this.min = min;
            setPostfix(postfix);
        }


        public String format(float value) {
            return Integer.toString(getIntValue(value)) + postfix;
        }

        public String format(int value) {
            return value + postfix;
        }

        public int getIntValue(float value) {
            int l = Math.abs(max - min);
            if (max > min) {
                return (int) (min + value * l);
            } else {
                return (int) (max + value * l);
            }
        }

        public float getFloatValue(int value) {
            if (max == min) return 0;

            float l = Math.abs(max - min);
            if (max > min) {
                return (value - min) / l;
            } else {
                return (value - max) / l;
            }
        }

        public void setPostfix(String postfix) {
            if (postfix != null)
                this.postfix = postfix;
        }
    }


    private OnChangeValuesListener onChangeValuesListener;

    public void setOnChangeValuesListener(OnChangeValuesListener onChangeValuesListener) {
        this.onChangeValuesListener = onChangeValuesListener;
    }

    public interface OnChangeValuesListener {
        void OnChange(int leftValue, int rightValue);
    }
}
