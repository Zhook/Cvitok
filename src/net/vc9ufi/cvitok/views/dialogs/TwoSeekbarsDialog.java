package net.vc9ufi.cvitok.views.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import net.vc9ufi.cvitok.R;
import net.vc9ufi.cvitok.views.customviews.DecoratedSeekBar;


public abstract class TwoSeekbarsDialog extends TwoIntValuesDialog {

    private DecoratedSeekBar mSeekBar1;
    private DecoratedSeekBar mSeekBar2;

    private String mTitle = "";
    private String mDescription1 = "";
    private String mDescription2 = "";
    private int mMinValue1 = 0;
    private int mMinValue2 = 0;
    private int mMaxValue1 = 10;
    private int mMaxValue2 = 10;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle(mTitle);

        View view = inflater.inflate(R.layout.dialog_2seekbars, container, false);

        mSeekBar1 = (DecoratedSeekBar) view.findViewById(R.id.decSeekBar1);
        mSeekBar1.setMinValue(mMinValue1);
        mSeekBar1.setMaxValue(mMaxValue1);
        mSeekBar1.setValue(mValue1);
        mSeekBar1.setDescription(mDescription1);
        mSeekBar1.setOnChangeListener(new DecoratedSeekBar.OnChangeListener() {
            @Override
            public void onValueChanged(int value) {
                mValue1 = value;
            }
        });

        mSeekBar2 = (DecoratedSeekBar) view.findViewById(R.id.decSeekBar2);
        mSeekBar2.setMinValue(mMinValue2);
        mSeekBar2.setMaxValue(mMaxValue2);
        mSeekBar2.setValue(mValue2);
        mSeekBar2.setDescription(mDescription2);
        mSeekBar2.setOnChangeListener(new DecoratedSeekBar.OnChangeListener() {
            @Override
            public void onValueChanged(int value) {
                mValue2 = value;
            }
        });

        Button buttonOk = (Button) view.findViewById(R.id.buttonOk);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickOk(mValue1, mValue2);
                dismiss();
            }
        });

        Button buttonCancel = (Button) view.findViewById(R.id.buttonCancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }

    public abstract void onClickOk(int value1, int value2);

    public void setDescription1(String description1) {
        this.mDescription1 = description1;
        if (mSeekBar1 != null)
            mSeekBar1.setDescription(mDescription1);
    }

    public void setDescription2(String description2) {
        this.mDescription2 = description2;
        if (mSeekBar2 != null)
            mSeekBar2.setDescription(mDescription2);
    }

    public void setMaxValue1(int maxValue1) {
        this.mMaxValue1 = maxValue1;
        if (mSeekBar1 != null)
            mSeekBar1.setMaxValue(mMaxValue1);
    }

    public void setMaxValue2(int maxValue2) {
        this.mMaxValue2 = maxValue2;
        if (mSeekBar2 != null)
            mSeekBar2.setMaxValue(mMaxValue2);
    }

    public void setMinValue1(int minValue1) {
        this.mMinValue1 = minValue1;
        if (mSeekBar1 != null)
            mSeekBar1.setMinValue(mMinValue1);
    }

    public void setMinValue2(int minValue2) {
        this.mMinValue2 = minValue2;
        if (mSeekBar2 != null)
            mSeekBar2.setMinValue(mMinValue2);
    }

    public void setTitle(String title) {
        this.mTitle = title;
        Dialog dialog = getDialog();
        if (dialog != null)
            dialog.setTitle(mTitle);
    }

    public void setValue1(int value1) {
        this.mValue1 = value1;
        if (mSeekBar1 != null)
            mSeekBar1.setValue(mValue1);
    }

    public void setValue2(int value2) {
        this.mValue2 = value2;
        if (mSeekBar2 != null)
            mSeekBar2.setValue(mValue2);
    }
}