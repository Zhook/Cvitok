package net.vc9ufi.cvitok.views.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import net.vc9ufi.cvitok.App;
import net.vc9ufi.cvitok.R;
import net.vc9ufi.cvitok.views.dialogs.ChoiceOfVertices;
import net.vc9ufi.cvitok.views.dialogs.ColorDialog;


public class FragmentVerticesTools extends Fragment {

    App app;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vertices_tools, container, false);
        context = inflater.getContext();
        app = (App) context.getApplicationContext();


        ImageButton b_vertices = (ImageButton) view.findViewById(R.id.fragment_vertices_button_vertces);
        b_vertices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ChoiceOfVertices(context).show();
            }
        });

        ImageButton b_color = (ImageButton) view.findViewById(R.id.fragment_vertices_button_color_vertices);
        b_color.setOnClickListener(colorOnClickListener);

        return view;
    }

    View.OnClickListener colorOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ColorDialog colorDialog = new ColorDialog() {
                @Override
                public boolean onClickOk(float[] color) {
                    return true;
                }
            };

            colorDialog.setColor(new float[]{0.3f, 0.5f, 0.7f, 1});
            colorDialog.show(getFragmentManager().beginTransaction(), "dialog");
        }
    };
}
