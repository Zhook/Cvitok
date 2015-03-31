package net.vc9ufi.cvitok;

import android.app.Activity;
import android.app.Application;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
import android.view.View;
import net.vc9ufi.cvitok.control.Control;
import net.vc9ufi.cvitok.data.*;
import net.vc9ufi.cvitok.renderers.ColorDialogRenderer;
import net.vc9ufi.cvitok.renderers.FlowerRenderer;
import net.vc9ufi.cvitok.settings.Setting;
import net.vc9ufi.geometry.LookAt;

public class App extends Application {

    private Setting setting;
    private Control control;
    private Flower flower;
    private Activity mainActivity;

    private FlowerRenderer flowerRenderer;
    private ColorDialogRenderer colorRenderer;

    private boolean start = true;

    @Override
    public void onCreate() {
        super.onCreate();

        setting = Setting.getInstance();
        setting.setApp(this);

        flower = Flower.getInstance();
        flower.setQuality(setting.getQuality());

        control = Control.getInstance();

        flowerRenderer = new FlowerRenderer(this);
        colorRenderer = new ColorDialogRenderer(new LookAt());

        flowerRenderer.setLight(setting.getLight());
        flowerRenderer.setTransparency(setting.getTransparency());

    }

    public void setMainActivity(Activity activity){
        mainActivity = activity;
    }

    //--------------------------------------------------------------------------------------Renderers
    public void makeFlowerRenderer(GLSurfaceView flowerSurface) {
        flowerSurface.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return control.onTouchSurface(v, event);
            }
        });
        flowerSurface.setRenderer(flowerRenderer);
    }

    public void setTransparency(boolean transparency) {
        flowerRenderer.setTransparency(transparency);
    }

    public void setLight(boolean light) {
        flowerRenderer.setLight(light);
    }

    public void makeScreenShot() {
        flowerRenderer.makeScreenshot();
    }

    public ColorDialogRenderer getColorRenderer() {
        return colorRenderer;
    }

    public boolean isStart() {
        if (start) {
            start = false;
            return true;
        }
        return false;
    }

    public void runInMainActivity(Runnable runnable){
        mainActivity.runOnUiThread(runnable);
    }
}
