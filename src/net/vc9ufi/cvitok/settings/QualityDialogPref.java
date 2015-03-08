package net.vc9ufi.cvitok.settings;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import net.vc9ufi.cvitok.R;

public class QualityDialogPref extends DialogPreference {

    Context context;

    NumberPicker picker;

    int defQuality;
    int quality;

    public QualityDialogPref(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        defQuality = attrs.getAttributeIntValue(Setting.ANDROID_NS, "defaultValue", Setting.DEF_QUALITY);
    }

    @Override
    protected View onCreateDialogView() {
        quality = getPersistedInt(defQuality);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_quality, null);

        picker = (NumberPicker) view.findViewById(R.id.quality_numberPicker);
        picker.setMinValue(3);
        picker.setMaxValue(30);
        picker.setValue(quality);
        picker.setWrapSelectorWheel(false);
        return view;
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if (!positiveResult) return;

        if (shouldPersist()) persistInt(picker.getValue());

        notifyChanged();
    }



}

