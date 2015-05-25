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
import net.vc9ufi.cvitok.views.customviews.DecoratedSeekBar;

public class PreferenceDialogTwoDerorSeekBars extends DialogPreference {


    private DecoratedSeekBar mDecoratedSeekBar1;
    private String mKey1 = "";
    private String mDescription1 = DecoratedSeekBar.DEF_DESCRIPTION;
    private int mMinValue1 = DecoratedSeekBar.DEF_MIN_VALUE;
    private int mMaxValue1 = DecoratedSeekBar.DEF_MAX_VALUE;
    private int mDefValue1 = DecoratedSeekBar.DEF_VALUE;
    private String mMinLabel1;
    private String mMaxLabel1;
    private String mPostfix1 = "";

    private DecoratedSeekBar mDecoratedSeekBar2;
    private String mKey2 = "";
    private String mDescription2 = DecoratedSeekBar.DEF_DESCRIPTION;
    private int mMinValue2 = DecoratedSeekBar.DEF_MIN_VALUE;
    private int mMaxValue2 = DecoratedSeekBar.DEF_MAX_VALUE;
    private int mDefValue2 = DecoratedSeekBar.DEF_VALUE;
    private String mMinLabel2;
    private String mMaxLabel2;
    private String mPostfix2 = "";

    public PreferenceDialogTwoDerorSeekBars(Context context, AttributeSet attrs) {
        super(context, attrs);
        readAttributes(attrs);
    }

    public PreferenceDialogTwoDerorSeekBars(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        readAttributes(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PreferenceDialogTwoDerorSeekBars(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        readAttributes(attrs);
    }


    void readAttributes(AttributeSet attrs) {

        final TypedArray attArrDecSeekBar = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.TwoDecoratedSeekBars,
                0, 0);
        try {
            mKey1 = attArrDecSeekBar.getString(R.styleable.TwoDecoratedSeekBars_key1);
            mMinValue1 = attArrDecSeekBar.getInt(R.styleable.TwoDecoratedSeekBars_minLabel1, mMinValue1);
            mMaxValue1 = attArrDecSeekBar.getInt(R.styleable.TwoDecoratedSeekBars_maxValue1, mMaxValue1);
            mDefValue1 = attArrDecSeekBar.getInt(R.styleable.TwoDecoratedSeekBars_defValue1, mDefValue1);
            mDescription1 = attArrDecSeekBar.getString(R.styleable.TwoDecoratedSeekBars_description1);
            mPostfix1 = attArrDecSeekBar.getString(R.styleable.TwoDecoratedSeekBars_postfix1);
            mMinLabel1 = attArrDecSeekBar.getString(R.styleable.TwoDecoratedSeekBars_minLabel1);
            mMaxLabel1 = attArrDecSeekBar.getString(R.styleable.TwoDecoratedSeekBars_maxLabel1);

            mKey2 = attArrDecSeekBar.getString(R.styleable.TwoDecoratedSeekBars_key2);
            mMinValue2 = attArrDecSeekBar.getInt(R.styleable.TwoDecoratedSeekBars_minLabel2, mMinValue2);
            mMaxValue2 = attArrDecSeekBar.getInt(R.styleable.TwoDecoratedSeekBars_maxValue2, mMaxValue2);
            mDefValue2 = attArrDecSeekBar.getInt(R.styleable.TwoDecoratedSeekBars_defValue2, mDefValue2);
            mDescription2 = attArrDecSeekBar.getString(R.styleable.TwoDecoratedSeekBars_description2);
            mPostfix2 = attArrDecSeekBar.getString(R.styleable.TwoDecoratedSeekBars_postfix2);
            mMinLabel2 = attArrDecSeekBar.getString(R.styleable.TwoDecoratedSeekBars_minLabel2);
            mMaxLabel2 = attArrDecSeekBar.getString(R.styleable.TwoDecoratedSeekBars_maxLabel2);
        } finally {
            attArrDecSeekBar.recycle();
        }
    }


    @Override
    protected View onCreateDialogView() {
        SharedPreferences sharedPreferences = getSharedPreferences();
        View view = View.inflate(getContext(), R.layout.preference_dialog_2decorseekbars, null);

        int value1 = sharedPreferences.getInt(mKey1, mDefValue1);
        int value2 = sharedPreferences.getInt(mKey2, mDefValue2);

        mDecoratedSeekBar1 = (DecoratedSeekBar) view.findViewById(R.id.decSeekBar1);
        mDecoratedSeekBar1.setDescription(mDescription1);
        mDecoratedSeekBar1.setMinValue(mMinValue1);
        mDecoratedSeekBar1.setMaxValue(mMaxValue1);
        mDecoratedSeekBar1.setValue(value1);
        mDecoratedSeekBar1.setPostfix(mPostfix1);
        mDecoratedSeekBar1.setLabels(mMinLabel1, mMaxLabel1);

        mDecoratedSeekBar2 = (DecoratedSeekBar) view.findViewById(R.id.decSeekBar2);
        mDecoratedSeekBar2.setDescription(mDescription2);
        mDecoratedSeekBar2.setMinValue(mMinValue2);
        mDecoratedSeekBar2.setMaxValue(mMaxValue2);
        mDecoratedSeekBar2.setValue(value2);
        mDecoratedSeekBar2.setPostfix(mPostfix2);
        mDecoratedSeekBar2.setLabels(mMinLabel2, mMaxLabel2);

        return view;
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if (!positiveResult) return;

        int value1 = mDecoratedSeekBar1.getValue();
        int value2 = mDecoratedSeekBar2.getValue();

        SharedPreferences.Editor editor = getEditor();
        editor.putInt(mKey1, value1);
        editor.putInt(mKey2, value2);
        editor.apply();

        notifyChanged();
    }
}
