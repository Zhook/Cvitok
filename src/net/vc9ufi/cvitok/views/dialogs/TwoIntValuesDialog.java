package net.vc9ufi.cvitok.views.dialogs;


import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import net.vc9ufi.cvitok.R;

public abstract class TwoIntValuesDialog extends android.support.v4.app.DialogFragment {

    protected String title = "";
    protected String des1 = "";
    protected String des2 = "";
    protected int value1 = 0;
    protected int value2 = 0;
    protected int min1 = 0;
    protected int min2 = 0;
    protected int max1 = 10;
    protected int max2 = 10;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setParameters1(String des1, int min1, int max1, int value1) {
        setDes1(des1);
        setMinValue1(min1);
        setMaxValue1(max1);
        setValue1(value1);
    }

    public void setParameters2(String des2, int min2, int max2, int value2) {
        setDes2(des2);
        setMinValue2(min2);
        setMaxValue2(max2);
        setValue2(value2);
    }

    public void setDes2(String des2) {
        this.des2 = des2;
    }

    public void setDes1(String des1) {
        this.des1 = des1;
    }

    public void setMaxValue1(int max1) {
        this.max1 = max1;
    }

    public void setMaxValue2(int max2) {
        this.max2 = max2;
    }

    public void setMinValue1(int min1) {
        this.min1 = min1;
    }

    public void setMinValue2(int min2) {
        this.min2 = min2;
    }

    public void setValue1(int value1) {
        this.value1 = value1;
    }

    public void setValue2(int value2) {
        this.value2 = value2;
    }

}
