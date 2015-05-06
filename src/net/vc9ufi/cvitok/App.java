package net.vc9ufi.cvitok;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import net.vc9ufi.cvitok.data.FlowerFile;
import net.vc9ufi.cvitok.data.Parameters;
import net.vc9ufi.cvitok.data.PetalsCalculator;
import net.vc9ufi.cvitok.views.dialogs.colordialog.ColorSphereCalculator;
import net.vc9ufi.cvitok.views.settings.SharedPreferenceChangeListener;
import net.vc9ufi.geometry.TrianglesBase;

public class App extends Application {

    private FlowerFile mFlower = new FlowerFile();
    private TrianglesBase colorSphereBase;
    private TrianglesBase mPetalsBase;
    private SharedPreferenceChangeListener sharedPreferenceChangeListener;

    @Override
    public void onCreate() {
        super.onCreate();

        colorSphereBase = new TrianglesBase();
        colorSphereBase.add(new ColorSphereCalculator(1.5f, 4), 0);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        mPetalsBase = new TrianglesBase();
        mPetalsBase.setTransparency(sharedPreferences.getBoolean(getString(R.string.preference_key_transparency), true));

        sharedPreferenceChangeListener = new SharedPreferenceChangeListener(this, mPetalsBase);
        sharedPreferences.registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);
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


}
