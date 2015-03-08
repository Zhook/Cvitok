package net.vc9ufi.cvitok.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import net.vc9ufi.cvitok.App;
import net.vc9ufi.cvitok.MainActivity;
import net.vc9ufi.cvitok.R;
import net.vc9ufi.cvitok.data.Flower;
import net.vc9ufi.cvitok.dialogs.colordialog.ColorDialog;
import net.vc9ufi.cvitok.renderers.ColorDialogRenderer;

public class FragmentFlower extends Fragment {

    App app;
    Context context;
    MainActivity mainActivity;

    TextView tv_flowerName;


    public void setAppNMainActivity(App app, MainActivity mainActivity){
        this.app = app;
        this.mainActivity = mainActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_flower, container, false);
        context = inflater.getContext();

        init(view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        tv_flowerName.setText(Flower.getInstance().getName());
    }

    void init(View view) {
        tv_flowerName = (TextView) view.findViewById(R.id.fragment_flower_textView_fileName);
        tv_flowerName.setText(Flower.getInstance().getName());

        ImageButton b_background = (ImageButton) view.findViewById(R.id.fragment_flower_imageButton_background);
        b_background.setOnClickListener(backgroundOnClickListener);

        ImageButton b_light = (ImageButton) view.findViewById(R.id.fragment_flower_imageButton_light);
        b_light.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.addLightFragment();
            }
        });
    }

    View.OnClickListener backgroundOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ColorDialogRenderer renderer = app.getColorRenderer();
            ColorDialog colorDialog = new ColorDialog(context, renderer) {
                @Override
                public void onClickPositiveButton(float[] color) {
                    Flower.getInstance().setBackground(color);
                }

                @Override
                public void onClickNegativeButton() {

                }

            };
            colorDialog.show();
        }
    };
}
