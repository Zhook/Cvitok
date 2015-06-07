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
import net.vc9ufi.cvitok.views.customviews.seekbars.DecoratedIntRangeSeekBar;

public class PreferenceDialogRangeSeekBar extends DialogPreference {

    public static final String FIRST_KEY = "_first";
    public static final String SECOND_KEY = "_second";

    private DecoratedIntRangeSeekBar rangeSeekBar;
    private int firstValue;
    private int secondValue;
    private String postfix = "";
    private int minValue;
    private int maxValue;


    public PreferenceDialogRangeSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public PreferenceDialogRangeSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PreferenceDialogRangeSeekBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }


    void init(AttributeSet attrs) {
        final TypedArray attributes = getContext().obtainStyledAttributes(attrs, R.styleable.DecoratedSeekBar, 0, 0);
        try {
            postfix = attributes.getString(R.styleable.DecoratedSeekBar_postfix);
            minValue = attributes.getInt(R.styleable.DecoratedSeekBar_minValue, DecoratedIntRangeSeekBar.DEF_MIN_VALUE);
            maxValue = attributes.getInt(R.styleable.DecoratedSeekBar_maxValue, DecoratedIntRangeSeekBar.DEF_MAX_VALUE);
            firstValue = attributes.getInt(R.styleable.DecoratedSeekBar_defFirstValue, DecoratedIntRangeSeekBar.DEF_FIRST_VALUE);
            secondValue = attributes.getInt(R.styleable.DecoratedSeekBar_defSecondValue, DecoratedIntRangeSeekBar.DEF_SECOND_VALUE);
        } finally {
            attributes.recycle();
        }
    }

    @Override
    protected View onCreateDialogView() {
        rangeSeekBar = (DecoratedIntRangeSeekBar) View.inflate(getContext(), R.layout.preference_range_seekbar, null);

        SharedPreferences sharedPreferences = getSharedPreferences();
        firstValue = sharedPreferences.getInt(getKey() + FIRST_KEY, firstValue);
        secondValue = sharedPreferences.getInt(getKey() + SECOND_KEY, secondValue);

        rangeSeekBar.setMinValue(minValue);
        rangeSeekBar.setMaxValue(maxValue);
        rangeSeekBar.setFirstValue(firstValue);
        rangeSeekBar.setSecondValue(secondValue);
        rangeSeekBar.setPostfix(postfix);

        return rangeSeekBar;
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if (!positiveResult) return;

        int firstValue = rangeSeekBar.getFirstValue();
        int secondValue = rangeSeekBar.getSecondValue();

        SharedPreferences.Editor editor = getEditor();
        editor.putInt(getKey() + FIRST_KEY, firstValue);
        editor.putInt(getKey() + SECOND_KEY, secondValue);
        editor.apply();

        notifyChanged();
    }


    public static int getFirstValue(Context context, SharedPreferences sharedPreferences, int id, int defValue) {
        return sharedPreferences.getInt(
                context.getString(id) + PreferenceDialogRangeSeekBar.FIRST_KEY,
                defValue);
    }

    public static int getSecondValue(Context context, SharedPreferences sharedPreferences, int id, int defValue) {
        return sharedPreferences.getInt(
                context.getString(id) + PreferenceDialogRangeSeekBar.SECOND_KEY,
                defValue);
    }
}
