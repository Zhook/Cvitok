package net.vc9ufi.cvitok.views.customviews.seekbars;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.util.AttributeSet;
import net.vc9ufi.cvitok.R;

public class RangeSeekBarCanvas {

    public static final int ID_DEF_THUMB_NORMAL = R.drawable.seek_thumb_normal;
    public static final int ID_DEF_THUMB_PRESSED = R.drawable.seek_thumb_pressed;
    public static final int DEF_ACTIVE_LINE_COLOR = Color.argb(0xFF, 0x33, 0xB5, 0xE5);
    public static final int DEF_INACTIVE_LINE_COLOR = Color.GRAY;
    public static final int DEF_ACTIVE_LINE_WIDTH = 4;
    public static final int DEF_INACTIVE_LINE_WIDTH = 1;

    public enum THUMB_POSITION {
        ABOVE(1),
        CENTER(0),
        UNDER(-1);
        private final int value;

        THUMB_POSITION(int value) {
            this.value = value;
        }

        public static THUMB_POSITION valueOf(int value) {
            switch (value) {
                case 1:
                    return ABOVE;
                case 0:
                    return CENTER;
                case -1:
                    return UNDER;
                default:
                    return CENTER;
            }
        }
    }

    private THUMB_POSITION mThumbPosition = THUMB_POSITION.CENTER;

    private Bitmap mThumbNormal;
    private Bitmap mThumbPressed;
    private int mActiveLineColor;
    private int mActiveLineWidth;
    private int mInactiveLineColor;
    private int mInactiveLineWidth;

    private Point mMinCanvasSize = new Point();
    private RectF mInactiveLineRect = new RectF();
    private RectF mActiveLineRect = new RectF();
    private Rect mWorkRect = new Rect(0, 0, 0, 0);

    private boolean mFirstThumbPressed = false;
    private boolean mSecondThumbPressed = false;

    private Point mMaxThumbSize = new Point();

    private PointF mThumbNormalStartPaddings = new PointF(0, 0);
    private PointF mThumbPressedStartPaddings = new PointF(0, 0);

    private float mFirstValue = 0.3f;
    private float mSecondValue = 0.7f;
    private PointF mThumbFirstOffset = new PointF(0, 0);
    private PointF mThumbSecondOffset = new PointF(0, 0);

    private int mWorkLength;

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);


    public RangeSeekBarCanvas(Context context, AttributeSet attrs) {
        setAttributes(context, attrs);
    }

    public void setAttributes(Context context, AttributeSet attrs) {
        final TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.DecoratedSeekBar, 0, 0);
        int idThumbNormal;
        int idThumbPressed;
        try {
            mFirstValue = attributes.getFloat(R.styleable.DecoratedSeekBar_defFirstValueF, mFirstValue);
            mSecondValue = attributes.getFloat(R.styleable.DecoratedSeekBar_defSecondValueF, mSecondValue);
            idThumbNormal = attributes.getInt(R.styleable.DecoratedSeekBar_thumbNormal, ID_DEF_THUMB_NORMAL);
            idThumbPressed = attributes.getInt(R.styleable.DecoratedSeekBar_thumbPressed, ID_DEF_THUMB_PRESSED);
            mActiveLineColor = attributes.getInt(R.styleable.DecoratedSeekBar_progressColor, DEF_ACTIVE_LINE_COLOR);
            mActiveLineWidth = attributes.getDimensionPixelSize(R.styleable.DecoratedSeekBar_activeLineWidth, DEF_ACTIVE_LINE_WIDTH);
            mInactiveLineColor = attributes.getInt(R.styleable.DecoratedSeekBar_backgroundLineColor, DEF_INACTIVE_LINE_COLOR);
            mInactiveLineWidth = attributes.getDimensionPixelSize(R.styleable.DecoratedSeekBar_inactiveLineWidth, DEF_INACTIVE_LINE_WIDTH);
            mThumbPosition = THUMB_POSITION.valueOf(attributes.getInt(R.styleable.DecoratedSeekBar_thumbPosition, THUMB_POSITION.CENTER.value));
        } finally {
            attributes.recycle();
        }

        mThumbNormal = BitmapFactory.decodeResource(context.getResources(), idThumbNormal);
        mThumbPressed = BitmapFactory.decodeResource(context.getResources(), idThumbPressed);

        if (mThumbNormal.getWidth() > mThumbPressed.getWidth()) {
            mMaxThumbSize.x = mThumbNormal.getWidth();
            mThumbPressedStartPaddings.x = (mThumbNormal.getWidth() - mThumbPressed.getWidth()) / 2f;
        } else {
            mMaxThumbSize.x = mThumbPressed.getWidth();
            mThumbNormalStartPaddings.x = (mThumbPressed.getWidth() - mThumbNormal.getWidth()) / 2f;
        }

        if (mThumbNormal.getHeight() > mThumbPressed.getHeight()) {
            mMaxThumbSize.y = mThumbNormal.getHeight();
            mThumbPressedStartPaddings.y = (mThumbNormal.getHeight() - mThumbPressed.getHeight()) / 2f;
        } else {
            mMaxThumbSize.y = mThumbPressed.getHeight();
            mThumbNormalStartPaddings.y = (mThumbPressed.getHeight() - mThumbNormal.getHeight()) / 2f;
        }

        int maxLineWidth = Math.max(mActiveLineWidth, mInactiveLineWidth);
        mMinCanvasSize.x = mMaxThumbSize.x;
        mMinCanvasSize.y = Math.max(mMaxThumbSize.y, maxLineWidth);
    }


    public void draw(Canvas canvas, Rect rect) {
        if (rect == null) {
            mWorkRect.left = 0;
            mWorkRect.right = canvas.getWidth();
            mWorkRect.top = 0;
            mWorkRect.bottom = canvas.getHeight();
        } else {
            mWorkRect = rect;
        }

        mWorkLength = mWorkRect.right - mWorkRect.left - mMaxThumbSize.x;

        calkInactiveLine();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mInactiveLineColor);
        mPaint.setAntiAlias(true);
        canvas.drawRect(mInactiveLineRect, mPaint);

        calkFirstThumbOffset();
        calkSecondThumbOffset();

        calkActiveLine();
        mPaint.setColor(mActiveLineColor);
        canvas.drawRect(mActiveLineRect, mPaint);

        drawFirstThumb(canvas);
        drawSecondThumb(canvas);
    }


    private void calkInactiveLine() {
        mInactiveLineRect.left = mWorkRect.left + mMaxThumbSize.x / 2f;
        mInactiveLineRect.right = mInactiveLineRect.left + mWorkLength;
        float top;
        float max = Math.max(mActiveLineWidth, mInactiveLineWidth);
        if (mActiveLineWidth > mInactiveLineWidth) {
            top = (mActiveLineWidth - mInactiveLineWidth) / 2f;
        } else {
            top = 0;
        }
        mInactiveLineRect.top = mWorkRect.top;
        switch (mThumbPosition) {
            case ABOVE:
                mInactiveLineRect.top += mMaxThumbSize.y - max + top;
                break;
            case CENTER:
                mInactiveLineRect.top += mMaxThumbSize.y / 2f - mInactiveLineWidth / 2f;
                break;
            case UNDER:
                mInactiveLineRect.top += top;
                break;
        }
        mInactiveLineRect.bottom = mInactiveLineRect.top + mInactiveLineWidth;
    }

    private void calkActiveLine() {
        mActiveLineRect.left = mMaxThumbSize.x / 2f + mThumbFirstOffset.x;
        mActiveLineRect.right = mMaxThumbSize.x / 2f + mThumbSecondOffset.x;
        float top;
        float max = Math.max(mActiveLineWidth, mInactiveLineWidth);
        if (mActiveLineWidth < mInactiveLineWidth) {
            top = (mInactiveLineWidth - mActiveLineWidth) / 2f;
        } else {
            top = 0;
        }
        mActiveLineRect.top = mWorkRect.top;
        switch (mThumbPosition) {
            case ABOVE:
                mActiveLineRect.top += mMaxThumbSize.y - max + top;
                break;
            case CENTER:
                mActiveLineRect.top += mMaxThumbSize.y / 2f - mActiveLineWidth / 2f;
                break;
            case UNDER:
                mActiveLineRect.top += top;
                break;
        }
        mActiveLineRect.bottom = mActiveLineRect.top + mActiveLineWidth;
    }

    private void calkFirstThumbOffset() {
        mThumbFirstOffset.x = mWorkRect.left + mWorkLength * mFirstValue;
        mThumbFirstOffset.y = mWorkRect.top;
    }

    private void calkSecondThumbOffset() {
        mThumbSecondOffset.x = mWorkRect.left + mWorkLength * mSecondValue;
        mThumbSecondOffset.y = mWorkRect.top;
    }


    private void drawFirstThumb(Canvas canvas) {
        float leftPadding;
        Bitmap thumb;
        if (mFirstThumbPressed) {
            leftPadding = mThumbPressedStartPaddings.x;
            thumb = mThumbPressed;
        } else {
            leftPadding = mThumbNormalStartPaddings.x;
            thumb = mThumbNormal;
        }
        canvas.drawBitmap(thumb, leftPadding + mThumbFirstOffset.x, mThumbFirstOffset.y, mPaint);
    }

    private void drawSecondThumb(Canvas canvas) {
        float leftPadding;
        Bitmap thumb;
        if (mSecondThumbPressed) {
            leftPadding = mThumbPressedStartPaddings.x;
            thumb = mThumbPressed;
        } else {
            leftPadding = mThumbNormalStartPaddings.x;
            thumb = mThumbNormal;
        }
        canvas.drawBitmap(thumb, leftPadding + mThumbSecondOffset.x, mThumbSecondOffset.y, mPaint);
    }


    public Point getMinCanvasSize() {
        return mMinCanvasSize;
    }

    public Point getMaxThumbSize() {
        return mMaxThumbSize;
    }

    public RectF getFirstThumbRect() {
        RectF rect = new RectF();
        if (mFirstThumbPressed) {
            rect.left = mThumbPressedStartPaddings.x + mThumbFirstOffset.x;
            rect.right = rect.left + mThumbPressed.getWidth();
            rect.top = mThumbPressedStartPaddings.y + mThumbFirstOffset.y;
            rect.bottom = rect.top + mThumbPressed.getHeight();
        } else {
            rect.left = mThumbNormalStartPaddings.x + mThumbFirstOffset.x;
            rect.right = rect.left + mThumbNormal.getWidth();
            rect.top = mThumbNormalStartPaddings.y + mThumbFirstOffset.y;
            rect.bottom = rect.top + mThumbNormal.getHeight();
        }
        return rect;
    }

    public RectF getSecondThumbRect() {
        RectF rect = new RectF();
        if (mSecondThumbPressed) {
            rect.left = mThumbPressedStartPaddings.x + mThumbSecondOffset.x;
            rect.right = rect.left + mThumbPressed.getWidth();
            rect.top = mThumbPressedStartPaddings.y + mThumbSecondOffset.y;
            rect.bottom = rect.top + mThumbPressed.getHeight();
        } else {
            rect.left = mThumbNormalStartPaddings.x + mThumbSecondOffset.x;
            rect.right = rect.left + mThumbNormal.getWidth();
            rect.top = mThumbNormalStartPaddings.y + mThumbSecondOffset.y;
            rect.bottom = rect.top + mThumbNormal.getHeight();
        }
        return rect;
    }


    public boolean isFirstThumbPressed() {
        return mFirstThumbPressed;
    }

    public void setFirstThumbPressed(boolean pressed) {
        this.mFirstThumbPressed = pressed;
    }

    public boolean isSecondThumbPressed() {
        return mSecondThumbPressed;
    }

    public void setSecondThumbPressed(boolean pressed) {
        this.mSecondThumbPressed = pressed;
    }


    public void setValue(float x) {
        if (mWorkLength <= 0) return;

        float value = (x - mMaxThumbSize.x / 2f - mWorkRect.left) / mWorkLength;
        if (value < 0) value = 0;
        if (value > 1) value = 1;

        if (mFirstThumbPressed) {
            mFirstValue = value;
            if (mFirstValue > mSecondValue) mSecondValue = mFirstValue;
        }

        if (mSecondThumbPressed) {
            mSecondValue = value;
            if (mFirstValue > mSecondValue) mFirstValue = mSecondValue;
        }
    }

    public void setSecondValue(float value) {
        this.mSecondValue = value;
    }

    public void setFirstValue(float value) {
        this.mFirstValue = value;
    }

    public float getFirstValue() {
        return mFirstValue;
    }

    public float getSecondValue() {
        return mSecondValue;
    }
}
