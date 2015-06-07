package net.vc9ufi.cvitok.views.fragments;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import net.vc9ufi.cvitok.App;
import net.vc9ufi.cvitok.R;
import net.vc9ufi.cvitok.views.dialogs.ColorDialog;

public class FragmentFlowerTools extends Fragment {

    App app;
    Context context;

    TextView tv_flowerName;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_flower_tools, container, false);
        context = inflater.getContext();
        app = (App) context.getApplicationContext();


        init(view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    void init(View view) {
        tv_flowerName = (TextView) view.findViewById(R.id.fragment_flower_textView_fileName);


        ImageButton b_background = (ImageButton) view.findViewById(R.id.fragment_flower_imageButton_background);


//        ImageButton b_light = (ImageButton) view.findViewById(R.id.fragment_flower_imageButton_light);
//        b_light.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                addLightToolsFragment();
//            }
//        });
    }


    private void setToolsFrame(android.support.v4.app.Fragment fragment) {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentFlowerTools_toolsFrame, fragment)
                .commit();
    }

    private static final Fragment PLACEHOLDER = new Fragment();
}
