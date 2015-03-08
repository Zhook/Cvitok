package net.vc9ufi.cvitok.settings;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import net.vc9ufi.cvitok.App;
import net.vc9ufi.cvitok.R;
import net.vc9ufi.cvitok.renderers.ColorDialogRenderer;

public class ColorDialogPref extends DialogPreference {

    Context context;
    App app;

    GLSurfaceView surface;
    ColorDialogRenderer renderer;

    int defColor;
    int color;

    public ColorDialogPref(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        app = (App) context.getApplicationContext();
        renderer = app.getColorRenderer();

        defColor = Setting.getIntColor(Setting.DEF_COLOR_F);
    }

    @Override
    protected View onCreateDialogView() {
        color = getPersistedInt(defColor);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_choice_color, null);

        init(view);

        return view;
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if (!positiveResult) return;

        float[] colorf = renderer.getColor();
        color = Setting.getIntColor(colorf);
        if (shouldPersist()) persistInt(color);

        notifyChanged();
    }

    private void init(View view) {
        surface = (GLSurfaceView) view.findViewById(R.id.colorDialog_glColor);
        surface.setRenderer(renderer);
        surface.setZOrderOnTop(true);

        surface.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return renderer.getLookAt().motionEvent(event);
            }
        });
    }

    private static final String ANDROID_NS = "http://schemas.android.com/apk/res/android";
}
