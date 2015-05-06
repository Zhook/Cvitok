package net.vc9ufi.cvitok.service;

import android.content.*;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import net.vc9ufi.cvitok.R;
import net.vc9ufi.cvitok.control.Camera4WallpaperService;
import net.vc9ufi.cvitok.data.FlowerFile;
import net.vc9ufi.cvitok.data.Parameters;
import net.vc9ufi.cvitok.data.PetalsCalculator;
import net.vc9ufi.cvitok.generator.FlowerGenerator;
import net.vc9ufi.cvitok.render.ImplRenderer;
import net.vc9ufi.cvitok.views.settings.SharedPreferenceChangeListener;
import net.vc9ufi.geometry.TrianglesBase;

public class CvitokService extends GLWallpaperService {

    private FlowerFile mFlower = new FlowerFile();
    private TrianglesBase mPetalsBase = new TrianglesBase();
    private Camera4WallpaperService mCamera;
    private FlowerGenerator flowerGenerator = new FlowerGenerator(CvitokService.this);
    SharedPreferenceChangeListener sharedPreferenceChangeListener;

    @Override
    public void onCreate() {
        super.onCreate();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        mPetalsBase.setTransparency(sharedPreferences.getBoolean(getString(R.string.preference_key_transparency), true));

        sharedPreferenceChangeListener = new SharedPreferenceChangeListener(this, mPetalsBase);
        sharedPreferences.registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);

        mCamera = new Camera4WallpaperService(new float[]{0, 5, 5}, new float[]{0, 0, 0}) {

            @Override
            public void result(float[] camera, float[] target, float[] up) {
                mPetalsBase.setLookAt(camera, target, up);
            }
        };
        setFlower(flowerGenerator.generate());

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

            ImplRenderer renderer = new ImplRenderer(mPetalsBase);
            setRenderer(renderer);
        }

        @Override
        public void onTouchEvent(MotionEvent event) {
            mCamera.onTouch(null, event);
            if (isPreview()) {

            }
        }

        @Override
        public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {
            mCamera.setXOffset(xOffset);
        }
    }

    private void setFlower(FlowerFile flower) {
        this.mFlower = flower;
        if (mFlower != null && mFlower.petals != null) {
            mPetalsBase.setBackgroundColor(mFlower.background);
            Parameters p;

            int quality = PreferenceManager.getDefaultSharedPreferences(this).
                    getInt(getString(R.string.preference_key_quality), 10);

            int i = 0;
            for (; i < mFlower.petals.size(); i++) {
                p = mFlower.petals.get(i);
                mPetalsBase.add(new PetalsCalculator(p, quality, i), i);
            }
            mPetalsBase.deleteWithTagMoreThan(i);
        }
    }

    BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                setFlower(flowerGenerator.generate());
            }
        }
    };


}
