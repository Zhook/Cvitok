package net.vc9ufi.cvitok.views.fragments;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import net.vc9ufi.cvitok.App;
import net.vc9ufi.cvitok.R;
import net.vc9ufi.cvitok.views.dialogs.colordialog.ColorDialog;
import net.vc9ufi.cvitok.views.dialogs.colordialog.ColorDialogRenderer;

public class FragmentLightTools extends Fragment {

    App app;
    Context context;

    Button b_ambient;
    Button b_diffuse;
    Button b_specular;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_light_tools, container, false);
        context = inflater.getContext();
        app = (App) context.getApplicationContext();

        b_ambient = (Button) view.findViewById(R.id.fragment_light_button_ambient);
        b_ambient.setOnClickListener(ambientOnClickListener);
        b_diffuse = (Button) view.findViewById(R.id.fragment_light_button_diffuse);
        b_diffuse.setOnClickListener(diffuseOnClickListener);
        b_specular = (Button) view.findViewById(R.id.fragment_light_button_specular);
        b_specular.setOnClickListener(specularOnClickListener);

        return view;
    }

    View.OnClickListener ambientOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ColorDialogRenderer renderer = app.getColorRenderer();
            ColorDialog colorDialog = new ColorDialog(context, renderer) {
                @Override
                public void onClickPositiveButton(float[] color) {
                    app.getFlower().getLight().ambient = color;
                }

                @Override
                public void onClickNegativeButton() {
                }

            };
            colorDialog.show();
        }
    };

    View.OnClickListener diffuseOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ColorDialogRenderer renderer = app.getColorRenderer();
            ColorDialog colorDialog = new ColorDialog(context, renderer) {
                @Override
                public void onClickPositiveButton(float[] color) {
                    app.getFlower().getLight().diffuse = color;
                }

                @Override
                public void onClickNegativeButton() {
                }

            };
            colorDialog.show();
        }
    };

    View.OnClickListener specularOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ColorDialogRenderer renderer = app.getColorRenderer();
            ColorDialog colorDialog = new ColorDialog(context, renderer) {
                @Override
                public void onClickPositiveButton(float[] color) {
                    app.getFlower().getLight().specular = color;
                }

                @Override
                public void onClickNegativeButton() {
                }

            };
            colorDialog.show();
        }
    };
}
