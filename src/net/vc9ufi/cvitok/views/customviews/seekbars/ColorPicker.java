package net.vc9ufi.cvitok.views.customviews.seekbars;

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

import java.security.InvalidParameterException;

public  class ColorPicker extends LinearLayout {
    public static final int DEFAULT_LAYOUT_RESOURCE_ID = R.layout.color_picker;

    private String defRedHeader;
    private String defGreenHeader;
    private String defBlueHeader;

    private int[] mColor = new int[3];
    protected OnColorListener onColorListener;

    protected SeekBar redSeekBar;
    protected SeekBar greenSeekBar;
    protected SeekBar blueSeekBar;
    protected TextView redHeader;
    protected TextView greenHeader;
    protected TextView blueHeader;

    public ColorPicker(Context context) {
        this(context, null);
    }

    public ColorPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ColorPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ColorPicker(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        defRedHeader = context.getString(R.string.color_dialog_description_red);
        defGreenHeader = context.getString(R.string.color_dialog_description_green);
        defBlueHeader = context.getString(R.string.color_dialog_description_blue);

        initView(context);
        setAttributes(context, attrs);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(DEFAULT_LAYOUT_RESOURCE_ID, this);

        redHeader = (TextView) findViewById(R.id.redHeader);
        greenHeader = (TextView) findViewById(R.id.greenHeader);
        blueHeader = (TextView) findViewById(R.id.blueHeader);


        redSeekBar = (SeekBar) findViewById(R.id.redSeekBar);
        redSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mColor[0] = progress;
                onChange();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        greenSeekBar = (SeekBar) findViewById(R.id.greenSeekBar);
        greenSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mColor[1] = progress;
                onChange();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        blueSeekBar = (SeekBar) findViewById(R.id.blueSeekBar);
        blueSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mColor[2] = progress;
                onChange();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        setColor();
    }

    public void setAttributes(Context context, AttributeSet attrs) {
        final TypedArray attributes = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.ColorPickerLayout,
                0, 0);
        try {
            mColor[0] = attributes.getInt(R.styleable.ColorPickerLayout_defRed, 50);
            mColor[1] = attributes.getInt(R.styleable.ColorPickerLayout_defGreen, 50);
            mColor[2] = attributes.getInt(R.styleable.ColorPickerLayout_defBlue, 50);
        } finally {
            attributes.recycle();
        }

        setColor();
    }


    public int[] getColor() {
        return mColor.clone();
    }

    public void setColor(int[] color) {
        if (color == null) throw new InvalidParameterException("color must be !null");
        if (color.length != 3) throw new InvalidParameterException("color.length must be 3");
        mColor = color.clone();

        setColor();
    }

    private void setColor() {
        redSeekBar.setProgress(mColor[0]);
        greenSeekBar.setProgress(mColor[1]);
        blueSeekBar.setProgress(mColor[2]);

        onChange();
    }


    private void onChange() {
        redHeader.setText(defRedHeader + " " + String.valueOf(mColor[0]) + "%");
        greenHeader.setText(defGreenHeader + " " + String.valueOf(mColor[1]) + "%");
        blueHeader.setText(defBlueHeader + " " + String.valueOf(mColor[2]) + "%");

        if (onColorListener != null)
            onColorListener.onColorChanged(mColor.clone());
    }

    public void setOnColorListener(OnColorListener onColorListener) {
        this.onColorListener = onColorListener;
    }

    public interface OnColorListener {
        void onColorChanged(int[] color);
    }
}
