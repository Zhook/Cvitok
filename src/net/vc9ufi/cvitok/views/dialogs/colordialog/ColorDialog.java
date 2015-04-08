package net.vc9ufi.cvitok.views.dialogs.colordialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.opengl.GLSurfaceView;
import android.view.View;
import android.widget.SeekBar;
import net.vc9ufi.cvitok.R;

public abstract class ColorDialog {

    Context context;
    ColorDialogRenderer renderer;

    GLSurfaceView surface;
    SeekBar alphaBar;


    public ColorDialog(Context context, ColorDialogRenderer renderer) {
        this.context = context;
        this.renderer = renderer;
    }

    public void show() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(R.string.dialog_color_title);

        View view = View.inflate(context, R.layout.dialog_choice_color, null);

        init(view);

        dialog.setView(view);
        dialog.setPositiveButton(R.string.Ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                float[] color = renderer.getColor();
                color[3] = alphaBar.getProgress() / 100f;
                onClickPositiveButton(color);
            }
        });
        dialog.setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                onClickNegativeButton();
                dialog.cancel();
            }
        });
        dialog.show();
    }

    private void init(View view) {
        surface = (GLSurfaceView) view.findViewById(R.id.colorDialog_glColor);
        surface.setRenderer(renderer);
        surface.setZOrderOnTop(true);
        surface.setOnTouchListener(renderer.getOnTouchListener());

        alphaBar = (SeekBar) view.findViewById(R.id.colorDialog_seekBarAlpha);
        alphaBar.setProgress(100);
    }

    public abstract void onClickPositiveButton(float[] color);

    public abstract void onClickNegativeButton();
}
