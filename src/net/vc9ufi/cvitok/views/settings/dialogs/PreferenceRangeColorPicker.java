package net.vc9ufi.cvitok.views.settings.dialogs;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Build;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import net.vc9ufi.cvitok.R;
import net.vc9ufi.cvitok.views.customviews.seekbars.RangeColorPicker;

import java.security.InvalidParameterException;


public class PreferenceRangeColorPicker extends DialogPreference {

    public static final String MIN_KEY = "_min";
    public static final String MAX_KEY = "_max";
    public static final String RED_KEY = "_red";
    public static final String GREEN_KEY = "_green";
    public static final String BLUE_KEY = "_blue";

    int[] defMinColor = new int[3];
    int[] gefMaxColor = new int[3];

    private RangeColorPicker mRangeColorPicker;

    public PreferenceRangeColorPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public PreferenceRangeColorPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PreferenceRangeColorPicker(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        final TypedArray attributes = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.ColorRangePickerLayout,
                0, 0);
        try {
            defMinColor[0] = attributes.getInt(R.styleable.ColorRangePickerLayout_defMinRed, 30);
            defMinColor[1] = attributes.getInt(R.styleable.ColorRangePickerLayout_defMinGreen, 30);
            defMinColor[2] = attributes.getInt(R.styleable.ColorRangePickerLayout_defMinBlue, 30);
            gefMaxColor[0] = attributes.getInt(R.styleable.ColorRangePickerLayout_defMaxRed, 60);
            gefMaxColor[1] = attributes.getInt(R.styleable.ColorRangePickerLayout_defMaxGreen, 60);
            gefMaxColor[2] = attributes.getInt(R.styleable.ColorRangePickerLayout_defMaxBlue, 60);
        } finally {
            attributes.recycle();
        }
    }

    @Override
    protected View onCreateDialogView() {

        mRangeColorPicker = (RangeColorPicker) View.inflate(getContext(), R.layout.preference_color_range, null);

        int[] minColor = mRangeColorPicker.getFirstColor();
        int[] maxColor = mRangeColorPicker.getSecondColor();
        SharedPreferences sharedPreferences = getSharedPreferences();

        minColor[0] = sharedPreferences.getInt(getKey() + MIN_KEY + RED_KEY, defMinColor[0]);
        maxColor[0] = sharedPreferences.getInt(getKey() + MAX_KEY + RED_KEY, gefMaxColor[0]);
        minColor[1] = sharedPreferences.getInt(getKey() + MIN_KEY + GREEN_KEY, defMinColor[1]);
        maxColor[1] = sharedPreferences.getInt(getKey() + MAX_KEY + GREEN_KEY, gefMaxColor[1]);
        minColor[2] = sharedPreferences.getInt(getKey() + MIN_KEY + BLUE_KEY, defMinColor[2]);
        maxColor[2] = sharedPreferences.getInt(getKey() + MAX_KEY + BLUE_KEY, gefMaxColor[2]);

        mRangeColorPicker.setColor(minColor, maxColor);

        return mRangeColorPicker;
    }



    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if (!positiveResult) return;

        int[] minColor = mRangeColorPicker.getFirstColor();
        int[] maxColor = mRangeColorPicker.getSecondColor();

        SharedPreferences.Editor editor = getEditor();
        editor.putInt(getKey() + MIN_KEY + RED_KEY, minColor[0]);
        editor.putInt(getKey() + MAX_KEY + RED_KEY, maxColor[0]);
        editor.putInt(getKey() + MIN_KEY + GREEN_KEY, minColor[1]);
        editor.putInt(getKey() + MAX_KEY + GREEN_KEY, maxColor[1]);
        editor.putInt(getKey() + MIN_KEY + BLUE_KEY, minColor[2]);
        editor.putInt(getKey() + MAX_KEY + BLUE_KEY, maxColor[2]);
        editor.apply();

        notifyChanged();
    }


    public static int[] getMinColor(Context context, SharedPreferences sharedPreferences, int id, int[] defColor) {
        if (defColor == null) throw new InvalidParameterException("defColor must be !null");
        if (defColor.length != 3) throw new InvalidParameterException("defColor.length must be 3");

        int[] result = new int[3];
        result[0] = sharedPreferences.getInt(context.getString(id) + MIN_KEY + RED_KEY, result[0]);
        result[1] = sharedPreferences.getInt(context.getString(id) + MIN_KEY + GREEN_KEY, result[1]);
        result[2] = sharedPreferences.getInt(context.getString(id) + MIN_KEY + BLUE_KEY, result[2]);

        return result;
    }

    public static int[] getMaxColor(Context context, SharedPreferences sharedPreferences, int id, int[] defColor) {
        if (defColor == null) throw new InvalidParameterException("defColor must be !null");
        if (defColor.length != 3) throw new InvalidParameterException("defColor.length must be 3");

        int[] result = new int[3];
        result[0] = sharedPreferences.getInt(context.getString(id) + MAX_KEY + RED_KEY, result[0]);
        result[1] = sharedPreferences.getInt(context.getString(id) + MAX_KEY + GREEN_KEY, result[1]);
        result[2] = sharedPreferences.getInt(context.getString(id) + MAX_KEY + BLUE_KEY, result[2]);

        return result;
    }
}
