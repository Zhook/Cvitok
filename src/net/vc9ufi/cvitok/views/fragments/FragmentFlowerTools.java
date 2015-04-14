package net.vc9ufi.cvitok.views.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import net.vc9ufi.cvitok.App;
import net.vc9ufi.cvitok.R;
import net.vc9ufi.cvitok.data.Flower;
import net.vc9ufi.cvitok.views.dialogs.colordialog.ColorDialog;
import net.vc9ufi.cvitok.views.dialogs.colordialog.ColorDialogRenderer;

public class FragmentFlowerTools extends Fragment {

    App app;
    Context context;

    private FragmentLightTools mLightToolsFragment;

    TextView tv_flowerName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLightToolsFragment = new FragmentLightTools();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_flower_tools, container, false);
        context = inflater.getContext();
        app = (App) context.getApplicationContext();
        app.getFlower().setOnTouchMode(Flower.ON_TOUCH_MODE.FLOWER);

        init(view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        tv_flowerName.setText(app.getFlower().getName());
    }

    void init(View view) {
        tv_flowerName = (TextView) view.findViewById(R.id.fragment_flower_textView_fileName);
        tv_flowerName.setText(app.getFlower().getName());

        ImageButton b_background = (ImageButton) view.findViewById(R.id.fragment_flower_imageButton_background);
        b_background.setOnClickListener(backgroundOnClickListener);

//        ImageButton b_light = (ImageButton) view.findViewById(R.id.fragment_flower_imageButton_light);
//        b_light.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                addLightToolsFragment();
//            }
//        });
    }

    View.OnClickListener backgroundOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ColorDialogRenderer renderer = app.getColorRenderer();
            ColorDialog colorDialog = new ColorDialog(context, renderer) {
                @Override
                public void onClickPositiveButton(float[] color) {
                    app.getFlower().setBackground(color);
                }

                @Override
                public void onClickNegativeButton() {

                }

            };
            colorDialog.show();
        }
    };

    public void addLightToolsFragment() {
        if (mLightToolsFragment.isAdded()) {
            setToolsFrame(PLACEHOLDER);
            app.getFlower().setOnTouchMode(Flower.ON_TOUCH_MODE.FLOWER);
        } else {
            setToolsFrame(mLightToolsFragment);
            app.getFlower().setOnTouchMode(Flower.ON_TOUCH_MODE.LIGHT);
        }
    }

    private void setToolsFrame(android.support.v4.app.Fragment fragment) {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentFlowerTools_toolsFrame, fragment)
                .commit();
    }

    private static final Fragment PLACEHOLDER = new Fragment();
}
