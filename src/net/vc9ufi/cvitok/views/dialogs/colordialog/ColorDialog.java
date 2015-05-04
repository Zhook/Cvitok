package net.vc9ufi.cvitok.views.dialogs.colordialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.opengl.GLSurfaceView;
import android.view.View;
import android.widget.SeekBar;
import net.vc9ufi.cvitok.R;
import net.vc9ufi.cvitok.control.RotatingCamera;
import net.vc9ufi.cvitok.render.ImplRenderer;
import net.vc9ufi.geometry.TrianglesBase;

public abstract class ColorDialog {

    private Context context;
    private TrianglesBase trianglesBase;


    public ColorDialog(Context context, TrianglesBase trianglesBase) {
        this.context = context;
        this.trianglesBase = trianglesBase;
    }

    public void show() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(R.string.dialog_color_title);

        View view = View.inflate(context, R.layout.dialog_choice_color, null);

        GLSurfaceView surface = (GLSurfaceView) view.findViewById(R.id.colorDialog_glColor);

        final ImplRenderer renderer = new ImplRenderer(trianglesBase){

        };
        surface.setRenderer(renderer);
        surface.setZOrderOnTop(true);
        surface.setOnTouchListener(new RotatingCamera() {
            @Override
            public void result(float[] camera, float[] target, float[] up) {
                trianglesBase.setLookAt(camera, target, up);
            }
        });

        SeekBar alphaBar = (SeekBar) view.findViewById(R.id.colorDialog_seekBarAlpha);
        alphaBar.setProgress(100);

        dialog.setView(view);
        dialog.setPositiveButton(R.string.Ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
//TODO
//                float[] color = renderer.getColor();
//                color[3] = alphaBar.getProgress() / 100f;
//                onClickPositiveButton(color);
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

    public abstract void onClickPositiveButton(float[] color);

    public abstract void onClickNegativeButton();
}
