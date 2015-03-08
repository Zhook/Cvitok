package net.vc9ufi.cvitok.dialogs.colordialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.opengl.GLSurfaceView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;
import net.vc9ufi.cvitok.App;
import net.vc9ufi.cvitok.R;
import net.vc9ufi.cvitok.renderers.ColorDialogRenderer;
import net.vc9ufi.geometry.LookAt;

public abstract class ColorDialog {

    Context context;
    App app;

    GLSurfaceView surface;
    ColorDialogRenderer renderer;
    SeekBar alphaBar;


    public ColorDialog(Context context, ColorDialogRenderer renderer) {
        this.context = context;
        this.renderer = renderer;
        app = (App) context.getApplicationContext();
    }

    public void show() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(R.string.dialog_color_title);

        View view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.dialog_choice_color, null);

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
        final LookAt camera = renderer.getLookAt();
        surface.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return camera.motionEvent(event);
            }
        });

        alphaBar = (SeekBar) view.findViewById(R.id.colorDialog_seekBarAlpha);
        alphaBar.setProgress(100);
    }

    public abstract void onClickPositiveButton(float[] color);

    public abstract void onClickNegativeButton();
}
