package net.vc9ufi.cvitok;

import android.app.Application;
import android.graphics.Bitmap;
import android.opengl.GLSurfaceView;
import net.vc9ufi.cvitok.control.LookAt;
import net.vc9ufi.cvitok.data.Flower;
import net.vc9ufi.cvitok.data.Light;
import net.vc9ufi.cvitok.views.dialogs.colordialog.ColorDialogRenderer;
import net.vc9ufi.cvitok.views.render.ImplRenderer;
import net.vc9ufi.cvitok.views.render.ScreenShot;
import net.vc9ufi.cvitok.views.settings.Setting;

import javax.microedition.khronos.opengles.GL10;
import java.io.File;
import java.io.IOException;

public class App extends Application {

    Flower flower;


    private ColorDialogRenderer colorRenderer;

    private boolean start = true;

    @Override
    public void onCreate() {
        super.onCreate();

        Setting setting = Setting.getInstance();
        setting.setApp(this);

        flower = new Flower();
        flower.setQuality(setting.getQuality());

        colorRenderer = new ColorDialogRenderer(new LookAt());
    }


    public Flower getFlower() {
        return flower;
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

    public enum MODE {NULL, FLOWER, PETAL, VERTEX, LIGHT}
}
