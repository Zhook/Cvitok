package net.vc9ufi.cvitok;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import net.vc9ufi.cvitok.data.FlowerFile;
import net.vc9ufi.cvitok.data.Parameters;
import net.vc9ufi.cvitok.data2.PetalsCalculator;
import net.vc9ufi.cvitok.views.dialogs.colordialog.ColorSphereCalculator;
import net.vc9ufi.geometry.TrianglesBase;

public class App extends Application {

    private FlowerFile mFlower = new FlowerFile();
    private TrianglesBase colorSphereBase;
    private TrianglesBase mPetalsBase;

    @Override
    public void onCreate() {
        super.onCreate();

        colorSphereBase = new TrianglesBase();
        colorSphereBase.add(new ColorSphereCalculator(1.5f, 4), 0);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);

        mPetalsBase = new TrianglesBase();
        mPetalsBase.setTransparency(sharedPreferences.getBoolean(getString(R.string.preference_key_transparency), true));
    }

    public TrianglesBase getColorSphereBase() {
        return colorSphereBase;
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
            int quality = sharedPreferences.getInt(getString(R.string.preference_key_quality), 10);

            int i = 0;
            for (; i < mFlower.petals.size(); i++) {
                p = mFlower.petals.get(i);
                mPetalsBase.add(new PetalsCalculator(p, quality, i), i);
            }
            mPetalsBase.deleteWithTagMoreThan(i);
        }
    }


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
