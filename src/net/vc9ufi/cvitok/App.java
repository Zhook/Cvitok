package net.vc9ufi.cvitok;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import net.vc9ufi.cvitok.control.Camera;
import net.vc9ufi.cvitok.control.Camera4WallpaperService;
import net.vc9ufi.cvitok.control.RotatingCamera;
import net.vc9ufi.cvitok.data.FlowerFile;
import net.vc9ufi.cvitok.data.Parameters;
import net.vc9ufi.cvitok.data.PetalsCalculator;
import net.vc9ufi.cvitok.views.settings.SharedPreferenceChangeListener;
import net.vc9ufi.geometry.TrianglesBase;

public class App extends Application {

    private FlowerFile mFlower = new FlowerFile();
    private TrianglesBase mPetalsBase;
    private SharedPreferenceChangeListener sharedPreferenceChangeListener;
    private Camera4WallpaperService mWallpaperCamera;
    private RotatingCamera mPreviewCamera;

    @Override
    public void onCreate() {
        super.onCreate();


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        mPetalsBase = new TrianglesBase();
        mPetalsBase.setTransparency(sharedPreferences.getBoolean(getString(R.string.prefkey_transparency), true));

        sharedPreferenceChangeListener = new SharedPreferenceChangeListener(this, mPetalsBase);
        sharedPreferences.registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);

        Camera camera = new Camera();
        camera.set(new float[]{0, 5, 5}, new float[]{0, 0, 0}, new float[]{0, 0, 1});

        mWallpaperCamera = new Camera4WallpaperService(camera) {
            @Override
            public void result(Camera camera) {
                mPetalsBase.setLookAt(camera);
            }
        };
        mPreviewCamera = new RotatingCamera(camera) {
            @Override
            public void result(Camera camera) {
                mPetalsBase.setLookAt(camera);
                try {
                    mWallpaperCamera.setStartCameraPoint(camera.clone());
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public TrianglesBase getPetalsBase() {
        return mPetalsBase;
    }

    public FlowerFile getFlower() {
        return mFlower;
    }

    public void setFlower(FlowerFile flower) {
        this.mFlower = flower;
        if (mFlower != null && mFlower.petals != null) {
            Parameters p;

            mPetalsBase.setBackgroundColor(mFlower.background);

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            int quality = sharedPreferences.getInt(getString(R.string.prefkey_quality), 10);

            int i = 0;
            for (; i < mFlower.petals.size(); i++) {
                p = mFlower.petals.get(i);
                mPetalsBase.add(new PetalsCalculator(p, quality, i), i);
            }
            mPetalsBase.deleteWithTagMoreThan(i);
        }
    }

    public void sort() {
        mPetalsBase.sort();
    }


    public Camera4WallpaperService getWallpaperCamera() {
        return mWallpaperCamera;
    }

    public RotatingCamera getPreviewCamera() {
        return mPreviewCamera;
    }

    public boolean hasFlower() {
        return mFlower.petals.size() > 0;
    }
}
