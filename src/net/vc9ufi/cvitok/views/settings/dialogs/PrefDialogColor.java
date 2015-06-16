package net.vc9ufi.cvitok.views.settings.dialogs;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Build;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import net.vc9ufi.cvitok.R;
import net.vc9ufi.cvitok.views.customviews.seekbars.ColorPicker;

import java.security.InvalidParameterException;

public class PrefDialogColor extends DialogPreference {

    public static final String RED_KEY = "_red";
    public static final String GREEN_KEY = "_green";
    public static final String BLUE_KEY = "_blue";

    int[] defColor = new int[3];

    private ColorPicker mColorPicker;


    public PrefDialogColor(Context context) {
        this(context, null);
    }

    public PrefDialogColor(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public PrefDialogColor(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PrefDialogColor(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        setAttribute(attrs);
    }

    public void setAttribute(AttributeSet attrs) {
        final TypedArray attributes = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.ColorPickerLayout,
                0, 0);
        try {
            defColor[0] = attributes.getInt(R.styleable.ColorPickerLayout_defRed, 50);
            defColor[1] = attributes.getInt(R.styleable.ColorPickerLayout_defGreen, 50);
            defColor[2] = attributes.getInt(R.styleable.ColorPickerLayout_defBlue, 50);
        } finally {
            attributes.recycle();
        }
    }

    @Override
    protected View onCreateDialogView() {
        mColorPicker = new ColorPicker(getContext());

        int[] color = mColorPicker.getColor();
        SharedPreferences sharedPreferences = getSharedPreferences();
        color[0] = sharedPreferences.getInt(getKey() + RED_KEY, defColor[0]);
        color[1] = sharedPreferences.getInt(getKey() + GREEN_KEY, defColor[1]);
        color[2] = sharedPreferences.getInt(getKey() + BLUE_KEY, defColor[2]);

        mColorPicker.setColor(color);

        return mColorPicker;
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if (!positiveResult) return;

        int[] color = mColorPicker.getColor();


        SharedPreferences.Editor editor = getEditor();
        editor.putInt(getKey() + RED_KEY, color[0]);
        editor.putInt(getKey() + GREEN_KEY, color[1]);
        editor.putInt(getKey() + BLUE_KEY, color[2]);
        editor.apply();

        notifyChanged();
    }


    public static int[] getColor(Context context, SharedPreferences sharedPreferences, int id, int[] defColor) {
        if (defColor == null) throw new InvalidParameterException("defColor must be !null");
        if (defColor.length != 3) throw new InvalidParameterException("defColor.length must be 3");

        int[] result = new int[3];
        result[0] = sharedPreferences.getInt(context.getString(id) + RED_KEY, defColor[0]);
        result[1] = sharedPreferences.getInt(context.getString(id) + GREEN_KEY, defColor[1]);
        result[2] = sharedPreferences.getInt(context.getString(id) + BLUE_KEY, defColor[2]);

        return result;
    }
}
