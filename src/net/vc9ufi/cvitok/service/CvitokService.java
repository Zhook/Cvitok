package net.vc9ufi.cvitok.service;

import android.content.*;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import net.vc9ufi.cvitok.R;
import net.vc9ufi.cvitok.control.Camera4WallpaperService;
import net.vc9ufi.cvitok.data.FlowerFile;
import net.vc9ufi.cvitok.data.Parameters;
import net.vc9ufi.cvitok.data2.PetalsCalculator;
import net.vc9ufi.cvitok.petal.generator.FlowerGenerator;
import net.vc9ufi.cvitok.render.ImplRenderer;
import net.vc9ufi.geometry.TrianglesBase;

public class CvitokService extends GLWallpaperService {

    private FlowerFile mFlower = new FlowerFile();
    private TrianglesBase mPetalsBase = new TrianglesBase();
    private Camera4WallpaperService mCamera;
    private FlowerGenerator flowerGenerator = new FlowerGenerator(CvitokService.this);
//    private boolean mNeedWorkInShadow = false;


    @Override
    public void onCreate() {
        super.onCreate();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);

        mPetalsBase.setTransparency(sharedPreferences.getBoolean(getString(R.string.preference_key_transparency), true));

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
            super.onTouchEvent(event);
        }

        @Override
        public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {
            super.onOffsetsChanged(xOffset, yOffset, xOffsetStep, yOffsetStep, xPixelOffset, yPixelOffset);
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
//                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
//                String period = sharedPreferences.getString(getString(R.string.preference_key_gen_period), "1");
                setFlower(flowerGenerator.generate());
//                if (period.equals("1")) {
//                    setFlower(flowerGenerator.generate());
//                } else if (sharedPreferences.getBoolean(getString(R.string.preference_key_gen_inshadow), true)) {
//                    if (mNeedWorkInShadow) {
//                        setFlower(flowerGenerator.generate());
//                        mNeedWorkInShadow = false;
//                    }
//                }
            }
        }
    };

    private SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            String transparency = getString(R.string.preference_key_transparency);
            if (key.equals(transparency)) {
                mPetalsBase.setTransparency(sharedPreferences.getBoolean(transparency, true));
            }
        }
    };

}
