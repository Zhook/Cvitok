package net.vc9ufi.cvitok.views.customviews.seekbars;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import net.vc9ufi.cvitok.R;

import java.security.InvalidParameterException;


public class RangeColorPicker extends LinearLayout {

    public static final int DEFAULT_LAYOUT_RESOURCE_ID = R.layout.layout_color_range;

    protected DecoratedIntRangeSeekBar redSeekBar;
    protected DecoratedIntRangeSeekBar greenSeekBar;
    protected DecoratedIntRangeSeekBar blueSeekBar;

    private int[] mFirstColor = new int[3];
    private int[] mSecondColor = new int[3];


    protected OnColorListener onColorListener;

    public RangeColorPicker(Context context) {
        this(context, null);
    }

    public RangeColorPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RangeColorPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RangeColorPicker(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        initView(context);
        setAttributes(context, attrs);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(DEFAULT_LAYOUT_RESOURCE_ID, this);


        redSeekBar = (DecoratedIntRangeSeekBar) findViewById(R.id.redRangeSeekBar);
        redSeekBar.setOnChangeValuesListener(new DecoratedIntRangeSeekBar.OnChangeValuesListener() {
            @Override
            public void OnChange(int leftValue, int rightValue) {
                mFirstColor[0] = leftValue;
                mSecondColor[0] = rightValue;
                onChange();
            }
        });

        greenSeekBar = (DecoratedIntRangeSeekBar) findViewById(R.id.greenRangeSeekBar);
        greenSeekBar.setOnChangeValuesListener(new DecoratedIntRangeSeekBar.OnChangeValuesListener() {
            @Override
            public void OnChange(int leftValue, int rightValue) {
                mFirstColor[1] = leftValue;
                mSecondColor[1] = rightValue;
                onChange();
            }
        });

        blueSeekBar = (DecoratedIntRangeSeekBar) findViewById(R.id.blueRangeSeekBar);
        blueSeekBar.setOnChangeValuesListener(new DecoratedIntRangeSeekBar.OnChangeValuesListener() {
            @Override
            public void OnChange(int leftValue, int rightValue) {
                mFirstColor[2] = leftValue;
                mSecondColor[2] = rightValue;
                onChange();
            }
        });

        setColor();
    }

    public void setAttributes(Context context, AttributeSet attrs) {
        final TypedArray attributes = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.ColorRangePickerLayout,
                0, 0);
        try {
            mFirstColor[0] = attributes.getInt(R.styleable.ColorRangePickerLayout_defMinRed, 30);
            mFirstColor[1] = attributes.getInt(R.styleable.ColorRangePickerLayout_defMinGreen, 30);
            mFirstColor[2] = attributes.getInt(R.styleable.ColorRangePickerLayout_defMinBlue, 30);
            mSecondColor[0] = attributes.getInt(R.styleable.ColorRangePickerLayout_defMaxRed, 60);
            mSecondColor[1] = attributes.getInt(R.styleable.ColorRangePickerLayout_defMaxGreen, 60);
            mSecondColor[2] = attributes.getInt(R.styleable.ColorRangePickerLayout_defMaxBlue, 60);
        } finally {
            attributes.recycle();
        }

        setColor();
    }


    public void setColor(int[] firstColor, int[] secondColor) {
        if (firstColor == null) throw new InvalidParameterException("firstColor must be !null");
        if (firstColor.length != 3) throw new InvalidParameterException("firstColor.length must be 3");
        if (secondColor == null) throw new InvalidParameterException("secondColor must be !null");
        if (secondColor.length != 3) throw new InvalidParameterException("secondColor.length must be 3");
        mFirstColor = firstColor.clone();
        mSecondColor = secondColor.clone();

        setColor();
    }

    private void setColor() {
        redSeekBar.setFirstValue(mFirstColor[0]);
        redSeekBar.setSecondValue(mSecondColor[0]);
        greenSeekBar.setFirstValue(mFirstColor[1]);
        greenSeekBar.setSecondValue(mSecondColor[1]);
        blueSeekBar.setFirstValue(mFirstColor[2]);
        blueSeekBar.setSecondValue(mSecondColor[2]);
    }

    public int[] getFirstColor() {
        return mFirstColor.clone();
    }

    public int[] getSecondColor() {
        return mSecondColor.clone();
    }


    private void onChange() {
        if (onColorListener != null)
            onColorListener.onColorChanged(mFirstColor.clone(), mSecondColor.clone());
    }

    public void setOnColorListener(OnColorListener onColorListener) {
        this.onColorListener = onColorListener;
    }

    public interface OnColorListener {
        void onColorChanged(int[] firstColor, int[] secondColor);
    }
}
