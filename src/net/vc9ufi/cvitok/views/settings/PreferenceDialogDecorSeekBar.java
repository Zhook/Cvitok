package net.vc9ufi.cvitok.views.settings;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import net.vc9ufi.cvitok.R;
import net.vc9ufi.cvitok.views.customviews.DecoratedSeekBar;

public class PreferenceDialogDecorSeekBar extends DialogPreference {

    private String mDescription = DecoratedSeekBar.DEF_DESCRIPTION;
    private int mMinValue = DecoratedSeekBar.DEF_MIN_VALUE;
    private int mMaxValue = DecoratedSeekBar.DEF_MAX_VALUE;
    private int mDefValue = DecoratedSeekBar.DEF_VALUE;
    private String mPostfix = "";
    private String mSummaryFormat = "";

    private DecoratedSeekBar mDecoratedSeekBar;


    public PreferenceDialogDecorSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        readAttributes(attrs);
    }

    public PreferenceDialogDecorSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        readAttributes(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PreferenceDialogDecorSeekBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        readAttributes(attrs);
    }


    void readAttributes(AttributeSet attrs) {
        mDefValue = attrs.getAttributeIntValue("http://schemas.android.com/apk/res/android", "defaultValue", DecoratedSeekBar.DEF_VALUE);

        final TypedArray attArrDecSeekBar = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.DecoratedSeekBar,
                0, 0);
        try {
            mMinValue = attArrDecSeekBar.getInt(R.styleable.DecoratedSeekBar_minValue, 0);
            mMaxValue = attArrDecSeekBar.getInt(R.styleable.DecoratedSeekBar_maxValue, 100);
            mDescription = attArrDecSeekBar.getString(R.styleable.DecoratedSeekBar_description);
            mPostfix = attArrDecSeekBar.getString(R.styleable.DecoratedSeekBar_postfix);
        } finally {
            attArrDecSeekBar.recycle();
        }

        final TypedArray attArrPreference = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.Preference,
                0, 0);
        try {
            mSummaryFormat = attArrDecSeekBar.getString(R.styleable.Preference_summaryFormat);
            if (mSummaryFormat == null) mSummaryFormat = "";
        } finally {
            attArrPreference.recycle();
        }


        int value = getPersistedInt(mDefValue);
        setSummary(String.format(mSummaryFormat, value));
    }


    @Override
    protected View onCreateDialogView() {
        int value = getPersistedInt(mDefValue);

        View view = View.inflate(getContext(), R.layout.preference_dialog_decorseekbar, null);

        mDecoratedSeekBar = (DecoratedSeekBar) view.findViewById(R.id.decSeekBar);
        mDecoratedSeekBar.setDescription(mDescription);
        mDecoratedSeekBar.setMinValue(mMinValue);
        mDecoratedSeekBar.setMaxValue(mMaxValue);
        mDecoratedSeekBar.setValue(value);
        mDecoratedSeekBar.setPostfix(mPostfix);

        return view;
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if (!positiveResult) return;

        int value = mDecoratedSeekBar.getValue();
        setSummary(String.format(mSummaryFormat, value));
        if (shouldPersist()) persistInt(value);

        notifyChanged();
    }
}
