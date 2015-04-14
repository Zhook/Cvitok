package net.vc9ufi.cvitok.views.fragments;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import net.vc9ufi.cvitok.App;
import net.vc9ufi.cvitok.R;
import net.vc9ufi.cvitok.data.Flower;
import net.vc9ufi.cvitok.views.dialogs.ChoiceOfVertices;
import net.vc9ufi.cvitok.views.dialogs.colordialog.ColorDialog;
import net.vc9ufi.cvitok.views.dialogs.colordialog.ColorDialogRenderer;

public class FragmentVerticesTools extends Fragment {

    App app;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vertices_tools, container, false);
        context = inflater.getContext();
        app = (App) context.getApplicationContext();
        app.getFlower().setOnTouchMode(Flower.ON_TOUCH_MODE.VERTEX);

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
            ColorDialogRenderer renderer = app.getColorRenderer();

            ColorDialog colorDialog = new ColorDialog(context, renderer) {
                @Override
                public void onClickPositiveButton(float[] color) {
                    app.getFlower().setVerticesColor(color);
                }

                @Override
                public void onClickNegativeButton() {

                }
            };
            colorDialog.show();
        }
    };
}
