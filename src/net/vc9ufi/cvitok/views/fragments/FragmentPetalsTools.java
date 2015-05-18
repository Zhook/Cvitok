package net.vc9ufi.cvitok.views.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import net.vc9ufi.cvitok.App;
import net.vc9ufi.cvitok.R;
import net.vc9ufi.cvitok.data.FlowerFile;
import net.vc9ufi.cvitok.views.dialogs.NameDialog;

public class FragmentPetalsTools extends Fragment {

    private Context context;
    private FragmentVerticesTools frag_vertices;

    Spinner sp_petals;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_petals_tools, container, false);
        context = inflater.getContext();
        FlowerFile flower = ((App) context.getApplicationContext()).getFlower();

        frag_vertices = new FragmentVerticesTools();

        sp_petals = (Spinner) view.findViewById(R.id.fragment_petals_spinner_petals);
        sp_petals.setPrompt(getString(R.string.petal));
        sp_petals.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        ImageButton b_add = (ImageButton) view.findViewById(R.id.fragment_petals_imageButton_addPetal);
        b_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ImageButton b_del = (ImageButton) view.findViewById(R.id.fragment_petals_imageButton_delPetal);
        b_del.setOnClickListener(new OnDeleteClickListener());

        ImageButton b_vertices = (ImageButton) view.findViewById(R.id.fragment_petals_button_vertices);
        b_vertices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addVerticesToolsFragment();
            }
        });

        return view;
    }


    class OnDeleteClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            AlertDialog.Builder deleteDialog = new AlertDialog.Builder(context);
            deleteDialog.setTitle(R.string.dialog_delete_petal_title);
            TextView field = new TextView(context);

            deleteDialog.setView(field);
            deleteDialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                }
            });
            deleteDialog.setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            deleteDialog.show();
        }
    }


    public void addVerticesToolsFragment() {
        if (frag_vertices.isAdded()) {
            setToolsFrame(PLACEHOLDER);

        } else {
            setToolsFrame(frag_vertices);
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
