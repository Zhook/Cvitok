package net.vc9ufi.cvitok.service;

import android.content.SharedPreferences;
import android.os.AsyncTask;
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

import java.util.concurrent.TimeUnit;

public class CvitokService extends GLWallpaperService {

    FlowerFile mFlower = new FlowerFile();
    TrianglesBase petalsBase = new TrianglesBase();
    Camera4WallpaperService mCamera;

    @Override
    public void onCreate() {
        super.onCreate();

        petalsBase.setTransparency(true);
        mCamera = new Camera4WallpaperService(new float[]{0, 5, 5}, new float[]{0, 0, 0}, new float[]{0, 0.3f, 0.3f}) {

            @Override
            public void result(float[] camera, float[] target, float[] up) {
                petalsBase.setLookAt(camera, target, up);
            }
        };

        final FlowerGenerator flowerGenerator = new FlowerGenerator(CvitokService.this);
        new Thread() {
            public void run() {
                while (true) {
                    setFlower(flowerGenerator.generate());
                    try {
                        Thread.sleep(TimeUnit.MINUTES.toMillis(1));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

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

            ImplRenderer renderer = new ImplRenderer(petalsBase);
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

    public void setFlower(FlowerFile flower) {
        this.mFlower = flower;
        if (mFlower != null && mFlower.petals != null) {
            Parameters p;
            int quality = getPreferenceInt(getString(R.string.preference_key_quality), 10);
            int i = 0;
            for (; i < mFlower.petals.size(); i++) {
                p = mFlower.petals.get(i);
                petalsBase.add(new PetalsCalculator(p, quality, i), i);
            }
            petalsBase.deleteWithTagMoreThan(i);
        }
    }

    public int getPreferenceInt(String key, int defValue) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        return sharedPreferences.getInt(key, defValue);
    }

    public long getPreferenceLong(String key, long defValue) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        return sharedPreferences.getLong(key, defValue);
    }

    private String getPreferenceString(String key, String defValue) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        return sharedPreferences.getString(key, defValue);
    }

    private boolean getPreferenceBoolean(String key, Boolean defValue) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        return sharedPreferences.getBoolean(key, defValue);
    }

    private void newFlower() {
        new AsyncTask<Void, Void, FlowerFile>() {
            @Override
            protected FlowerFile doInBackground(Void... params) {
                return new FlowerGenerator(CvitokService.this).generate();
            }

            @Override
            protected void onPostExecute(FlowerFile flowerFile) {
                super.onPostExecute(flowerFile);
                setFlower(flowerFile);
            }
        }.execute();
    }


}
