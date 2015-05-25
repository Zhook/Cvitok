package net.vc9ufi.cvitok.views.dialogs;


import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import net.vc9ufi.cvitok.R;
import net.vc9ufi.cvitok.views.customviews.ColorPickerLayout;
import net.vc9ufi.cvitok.views.settings.ColorPreference;

public abstract class ColorDialog extends android.support.v4.app.DialogFragment {

    private static final int DEFAULT_LAYOUT_RESOURCE_ID = R.layout.dialog_color_picker;

    protected String mTitle = "";
    protected float[] mColor;

    protected ColorPickerLayout colorPickerLayout;


    public static void showPreference(FragmentManager fragmentManager, final SharedPreferences sharedPreferences, final String key, String title) {
        ColorDialog colorDialog = new ColorDialog() {
            @Override
            public boolean onClickOk(float[] color) {
                ColorPreference.saveColor(
                        sharedPreferences,
                        key,
                        colorPickerLayout.getColor());
                return true;
            }
        };

        float[] color = ColorPreference.loadColor(
                sharedPreferences,
                key,
                null);
        colorDialog.setColor(color);
        colorDialog.setTitle(title);
        colorDialog.show(fragmentManager.beginTransaction(), "dialog");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle(mTitle);

        View view = inflater.inflate(DEFAULT_LAYOUT_RESOURCE_ID, container, false);

        colorPickerLayout = (ColorPickerLayout) view.findViewById(R.id.colorPicker);
        colorPickerLayout.setOnColorListener(new ColorPickerLayout.OnColorListener() {
            @Override
            public void onColorChanged(float[] color) {
                mColor = color;
            }
        });
        if (mColor != null) {
            colorPickerLayout.setColor(mColor);
        } else {
            mColor = colorPickerLayout.getColor();
        }


        Button buttonOk = (Button) view.findViewById(R.id.buttonOk);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickOk(mColor))
                    dismiss();
            }
        });

        Button buttonCancel = (Button) view.findViewById(R.id.buttonCancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickCancel(mColor))
                    dismiss();
            }
        });

        return view;
    }


    public boolean onClickOk(float[] color) {
        return true;
    }

    public boolean onClickCancel(float[] color) {
        return true;
    }


    public void setColor(float[] color) {
        mColor = color;
        if (colorPickerLayout != null)
            colorPickerLayout.setColor(mColor);
    }

    public float[] getColor() {
        return mColor;
    }


    public void setTitle(String title) {
        this.mTitle = title;
        Dialog dialog = getDialog();
        if (dialog != null)
            dialog.setTitle(mTitle);
    }
}
