package net.vc9ufi.cvitok.views.customviews;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import net.vc9ufi.cvitok.R;

import java.security.InvalidParameterException;

public class ColorPickerLayout extends LinearLayout {
    public static final int DEFAULT_LAYOUT_RESOURCE_ID = R.layout.layout_color_picker;

    protected float[] mColor = new float[]{0.3f, 0.5f, 0.7f, 1};

    protected DecoratedSeekBar redSeekBar;
    protected DecoratedSeekBar greenSeekBar;
    protected DecoratedSeekBar blueSeekBar;
    protected DecoratedSeekBar transparencySeekBar;
    protected TextView colorTextView;

    protected OnColorListener onColorListener;

    public ColorPickerLayout(Context context) {
        this(context, null);
    }

    public ColorPickerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ColorPickerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ColorPickerLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        initView(context);
        setAttributes(context, attrs);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(DEFAULT_LAYOUT_RESOURCE_ID, this);

        colorTextView = (TextView) findViewById(R.id.colorTextView);

        redSeekBar = (DecoratedSeekBar) findViewById(R.id.redSeekBar);
        redSeekBar.setOnChangeListener(new DecoratedSeekBar.OnChangeListener() {
            @Override
            public void onValueChanged(int value) {
                mColor[0] = value / 100.0f;
                paintColor();
                if (onColorListener != null)
                    onColorListener.onColorChanged(mColor.clone());
            }
        });

        greenSeekBar = (DecoratedSeekBar) findViewById(R.id.greenSeekBar);
        greenSeekBar.setOnChangeListener(new DecoratedSeekBar.OnChangeListener() {
            @Override
            public void onValueChanged(int value) {
                mColor[1] = value / 100.0f;
                paintColor();
                if (onColorListener != null)
                    onColorListener.onColorChanged(mColor.clone());
            }
        });

        blueSeekBar = (DecoratedSeekBar) findViewById(R.id.blueSeekBar);
        blueSeekBar.setOnChangeListener(new DecoratedSeekBar.OnChangeListener() {
            @Override
            public void onValueChanged(int value) {
                mColor[2] = value / 100.0f;
                paintColor();
                if (onColorListener != null)
                    onColorListener.onColorChanged(mColor.clone());

            }
        });

        transparencySeekBar = (DecoratedSeekBar) findViewById(R.id.transparencySeekBar);
        transparencySeekBar.setOnChangeListener(new DecoratedSeekBar.OnChangeListener() {
            @Override
            public void onValueChanged(int value) {
                mColor[3] = (100 - value) / 100.0f;
                paintColor();
                if (onColorListener != null)
                    onColorListener.onColorChanged(mColor.clone());
            }
        });

        setColor();
        paintColor();
    }

    public void setAttributes(Context context, AttributeSet attrs) {

        final TypedArray attArrDecSeekBar = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.ColorPickerLayout,
                0, 0);
        try {
            mColor[0] = attArrDecSeekBar.getFloat(R.styleable.ColorPickerLayout_defRed, 0.3f);
            mColor[1] = attArrDecSeekBar.getFloat(R.styleable.ColorPickerLayout_defGreen, 0.5f);
            mColor[2] = attArrDecSeekBar.getFloat(R.styleable.ColorPickerLayout_defBlue, 0.7f);
            mColor[3] = attArrDecSeekBar.getFloat(R.styleable.ColorPickerLayout_defAlpha, 1);
        } finally {
            attArrDecSeekBar.recycle();
            setColor();
        }
    }


    private void paintColor() {
        int r = (int) (mColor[0] * 255);
        int g = (int) (mColor[1] * 255);
        int b = (int) (mColor[2] * 255);
        int a = (int) (mColor[3] * 255);

        int c = Color.argb(a, r, g, b);
        colorTextView.setBackgroundColor(c);

        c = Color.argb(255, 255 - r, 255 - g, 255 - b);
        colorTextView.setTextColor(c);
    }

    public void setColor(float[] color) {
        if (color == null) throw new InvalidParameterException("mColor must be !null");
        if (color.length != 4) throw new InvalidParameterException("mColor.length must be 4");
        mColor = color;

        setColor();
        paintColor();
    }

    private void setColor() {
        redSeekBar.setValue((int) (mColor[0] * 100));
        greenSeekBar.setValue((int) (mColor[1] * 100));
        blueSeekBar.setValue((int) (mColor[2] * 100));
        transparencySeekBar.setValue((int) (100 - mColor[3] * 100));
    }

    public float[] getColor() {
        return mColor.clone();
    }


    public void setOnColorListener(OnColorListener onColorListener) {
        this.onColorListener = onColorListener;
    }

    public interface OnColorListener {
        void onColorChanged(float[] color);
    }
}
