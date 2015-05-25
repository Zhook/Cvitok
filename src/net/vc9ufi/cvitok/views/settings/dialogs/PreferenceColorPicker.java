package net.vc9ufi.cvitok.views.settings.dialogs;


import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import net.vc9ufi.cvitok.R;
import net.vc9ufi.cvitok.views.customviews.ColorPickerLayout;
import net.vc9ufi.cvitok.views.settings.ColorPreference;

public class PreferenceColorPicker extends DialogPreference {

    ColorPickerLayout colorPickerLayout;
    AttributeSet attributes;
    Context context;


    public PreferenceColorPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public PreferenceColorPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PreferenceColorPicker(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    void init(Context context, AttributeSet attrs) {
        attributes = attrs;
        this.context = context;
    }


    @Override
    protected View onCreateDialogView() {
        View view = View.inflate(getContext(), R.layout.preference_color_dialog, null);

        colorPickerLayout = (ColorPickerLayout) view.findViewById(R.id.colorPicker);
        colorPickerLayout.setAttributes(context, attributes);
        colorPickerLayout.setColor(
                ColorPreference.loadColor(
                        getSharedPreferences(),
                        getKey(),
                        colorPickerLayout.getColor())
        );

        return view;
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if (!positiveResult) return;

        ColorPreference.saveColor(
                getSharedPreferences(),
                getKey(),
                colorPickerLayout.getColor());

        notifyChanged();
    }

}
