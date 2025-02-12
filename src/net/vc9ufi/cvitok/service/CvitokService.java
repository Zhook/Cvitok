package net.vc9ufi.cvitok.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import net.vc9ufi.cvitok.App;
import net.vc9ufi.cvitok.control.Camera4WallpaperService;
import net.vc9ufi.cvitok.control.RotatingCamera;
import net.vc9ufi.cvitok.generator.FlowerGenerator;
import net.vc9ufi.cvitok.render.ImplRenderer;

public class CvitokService extends GLWallpaperService {

    private FlowerGenerator flowerGenerator = new FlowerGenerator(CvitokService.this);

    private App mApp;

    private Camera4WallpaperService mWallpaperCamera;
    private RotatingCamera mPreviewCamera;

    @Override
    public void onCreate() {
        super.onCreate();

        mApp = (App) getApplicationContext();

        mWallpaperCamera = mApp.getWallpaperCamera();
        mPreviewCamera = mApp.getPreviewCamera();

        if (!mApp.hasFlower())
            mApp.setFlower(flowerGenerator.generate());

        registerReceiver(mBroadcastReceiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));
    }

    @Override
    public Engine onCreateEngine() {
        return new CvitokEngine();
    }

    class CvitokEngine extends GLWallpaperService.GLEngine {


        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            setTouchEventsEnabled(true);
            ImplRenderer renderer = new ImplRenderer(mApp.getPetalsBase());
            setRenderer(renderer);
        }

        @Override
        public void onTouchEvent(MotionEvent event) {
            if (isPreview())
                mPreviewCamera.onTouch(null, event);
        }

        @Override
        public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {
            mWallpaperCamera.setXOffset(xOffset);
        }
    }


    BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                mApp.setFlower(flowerGenerator.generate());
            }
        }
    };


}
