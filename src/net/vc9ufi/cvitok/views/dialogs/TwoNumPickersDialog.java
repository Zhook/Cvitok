package net.vc9ufi.cvitok.views.dialogs;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import net.vc9ufi.cvitok.R;

public abstract class TwoNumPickersDialog extends TwoIntValuesDialog {

    private NumberPicker numberPicker1;
    private NumberPicker numberPicker2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle(title);

        View view = inflater.inflate(R.layout.dialog_2numberpickers, container, false);

        numberPicker1 = (NumberPicker) view.findViewById(R.id.dialog_2numberPickers_value1);
        numberPicker1.setMinValue(min1);
        numberPicker1.setMaxValue(max1);
        numberPicker1.setValue(value1);

        numberPicker2 = (NumberPicker) view.findViewById(R.id.dialog_2numberPickers_value2);
        numberPicker2.setMinValue(min2);
        numberPicker2.setMaxValue(max2);
        numberPicker2.setValue(value2);

        TextView textViewDes1 = (TextView) view.findViewById(R.id.dialog_2value_des1);
        textViewDes1.setText(des1);
        TextView textViewDes2 = (TextView) view.findViewById(R.id.dialog_2value_des2);
        textViewDes2.setText(des2);

        Button buttonOk = (Button) view.findViewById(R.id.buttonOk);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickOk(numberPicker1.getValue(), numberPicker2.getValue());
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
}
