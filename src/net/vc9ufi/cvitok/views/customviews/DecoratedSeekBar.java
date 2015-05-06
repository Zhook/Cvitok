package net.vc9ufi.cvitok.views.customviews;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import net.vc9ufi.cvitok.R;

public class DecoratedSeekBar extends LinearLayout {

    private static final int DEFAULT_LAYOUT_RESOURCE_ID = R.layout.seekbar_decorated;
    public static final String DEF_DESCRIPTION = "Description";
    public static final int DEF_MIN_VALUE = 0;
    public static final int DEF_MAX_VALUE = 100;
    public static final int DEF_VALUE = 50;

    private int mMinValue = DEF_MIN_VALUE;
    private int mMaxValue = DEF_MAX_VALUE;

    private String mMinLabel;
    private String mMaxLabel;
    private boolean mTextLabel = false;

    private int mValue = DEF_VALUE;

    private String mDescription = DEF_DESCRIPTION;
    private String mPostfix = "";

    private TextView mTextViewMin;
    private TextView mTextViewMax;
    private TextView mTextViewValue;
    private TextView mTextViewDescription;
    private SeekBar mSeekBarValue;

    private OnChangeListener mOnChangeListener;


    private Formatter formatter = new Formatter(mMinValue, mMaxValue);

    public DecoratedSeekBar(Context context) {
        this(context, null);
    }

    public DecoratedSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context, attrs);
    }

    public DecoratedSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DecoratedSeekBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initViews(context, attrs);
    }

    private void initViews(Context context, AttributeSet attrs) {

        final TypedArray attributesArray = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.DecoratedSeekBar,
                0, 0);

        try {
            mMinValue = attributesArray.getInt(R.styleable.DecoratedSeekBar_minValue, DEF_MIN_VALUE);
            mMaxValue = attributesArray.getInt(R.styleable.DecoratedSeekBar_maxValue, DEF_MAX_VALUE);
            mValue = attributesArray.getInt(R.styleable.DecoratedSeekBar_defValue, DEF_VALUE);
            mDescription = attributesArray.getString(R.styleable.DecoratedSeekBar_description);
            mMinLabel = attributesArray.getString(R.styleable.DecoratedSeekBar_minLabel);
            mMaxLabel = attributesArray.getString(R.styleable.DecoratedSeekBar_maxLabel);
            mTextLabel = mMinLabel != null || mMaxLabel != null;
            if (mDescription == null) mDescription = "";
            mPostfix = attributesArray.getString(R.styleable.DecoratedSeekBar_postfix);
            if (mPostfix == null) mPostfix = "";
        } finally {
            attributesArray.recycle();
        }

        LayoutInflater.from(context).inflate(DEFAULT_LAYOUT_RESOURCE_ID, this);

        mTextViewMin = (TextView) findViewById(R.id.decoratedSeekBar_textView_min);
        mTextViewMax = (TextView) findViewById(R.id.decoratedSeekBar_textView_max);
        mTextViewValue = (TextView) findViewById(R.id.decoratedSeekBar_textView_value);

        if (mTextLabel) {
            mTextViewMin.setText(mMinLabel);
            mTextViewMax.setText(mMaxLabel);
            mTextViewValue.setText("");
        } else {
            mTextViewMin.setText(getStringValue(mMinValue));
            mTextViewMax.setText(getStringValue(mMaxValue));
            mTextViewValue.setText(getStringValue(mValue));
        }
        mTextViewDescription = (TextView) findViewById(R.id.decoratedSeekBar_textView_description);
        mTextViewDescription.setText(mDescription);

        mSeekBarValue = (SeekBar) findViewById(R.id.decoratedSeekBar_seekBar_value);
        mSeekBarValue.setMax(Math.abs(mMaxValue - mMinValue));
        mSeekBarValue.setProgress(formatter.setValue(mValue));
        mSeekBarValue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mValue = formatter.getValue(seekBar.getProgress());

                if (!mTextLabel)
                    mTextViewValue.setText(getStringValue(mValue));

                if (mOnChangeListener != null)
                    mOnChangeListener.onValueChanged(mValue);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


    public int getMaxValue() {
        return mMaxValue;
    }

    public void setMaxValue(int maxValue) {
        this.mMaxValue = maxValue;
        formatter.max = maxValue;
        if (mTextViewMax != null)
            mTextViewMax.setText(getStringValue(mMaxValue));
        if (mSeekBarValue != null)
            mSeekBarValue.setMax(Math.abs(mMaxValue - mMinValue));
    }

    public int getMinValue() {
        return mMinValue;
    }

    public void setMinValue(int minValue) {
        this.mMinValue = minValue;
        formatter.min = minValue;
        if (mTextViewMin != null)
            mTextViewMin.setText(getStringValue(mMinValue));
        if (mSeekBarValue != null)
            mSeekBarValue.setMax(Math.abs(mMaxValue - mMinValue));
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        if (description == null) description = "";
        this.mDescription = description;
        if (mTextViewDescription != null)
            mTextViewDescription.setText(mDescription);
    }

    public int getValue() {
        return mValue;
    }

    public void setValue(int mValue) {
        this.mValue = mValue;
        if (mTextViewValue != null)
            mTextViewValue.setText(getStringValue(mValue));
        if (mSeekBarValue != null) {
            mSeekBarValue.setProgress(formatter.setValue(mValue));
        }
    }

    public String getPostfix() {
        return mPostfix;
    }

    public void setPostfix(String postfix) {
        if (postfix == null) postfix = "";
        this.mPostfix = postfix;
        if (mTextViewMax != null)
            mTextViewMax.setText(getStringValue(mMaxValue));
        if (mTextViewMin != null)
            mTextViewMin.setText(getStringValue(mMinValue));
        if (mTextViewValue != null)
            mTextViewValue.setText(getStringValue(mValue));
    }

    public void setLabels(String minLabel, String maxLabel) {
        mMinLabel = minLabel;
        mMaxLabel = maxLabel;
        mTextLabel = mMinLabel != null || mMaxLabel != null;
        if (mTextLabel) {
            mTextViewMin.setText(mMinLabel);
            mTextViewMax.setText(mMaxLabel);
            mTextViewValue.setText("");
        }
    }


    private String getStringValue(int value) {
        return String.valueOf(value) + mPostfix;
    }


    class Formatter {
        int min;
        int max;

        public Formatter(int min, int max) {
            this.max = max;
            this.min = min;
        }


        public String format(int index) {
            return Integer.toString(getValue(index));
        }

        public int getValue(int index) {
            if (max - min > 0) {
                return index + min;
            } else {
                return min - index;
            }
        }

        public int setValue(int value) {
            if (max - min > 0) {
                return value - min;
            } else {
                return value + min;
            }
        }
    }


    public interface OnChangeListener {
        void onValueChanged(int value);
    }

    public void setOnChangeListener(OnChangeListener onChangeListener) {
        this.mOnChangeListener = onChangeListener;
    }
}
