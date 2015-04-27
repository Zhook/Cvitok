package net.vc9ufi.cvitok;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import net.vc9ufi.cvitok.data.FlowerFile;
import net.vc9ufi.cvitok.data.Parameters;
import net.vc9ufi.cvitok.data2.PetalsCalculator;
import net.vc9ufi.cvitok.views.dialogs.colordialog.ColorSphereCalculator;
import net.vc9ufi.cvitok.views.settings.Setting;
import net.vc9ufi.geometry.TrianglesBase;

public class App extends Application {

    FlowerFile mFlower = new FlowerFile();
    TrianglesBase colorSphereBase;
    TrianglesBase petalsBase;

    @Override
    public void onCreate() {
        super.onCreate();

        Setting.getInstance().setApp(this);//TODO

        colorSphereBase = new TrianglesBase();
        colorSphereBase.add(new ColorSphereCalculator(1.5f, 4), 0);

        petalsBase = new TrianglesBase();
    }

    public TrianglesBase getColorSphereBase() {
        return colorSphereBase;
    }

    public TrianglesBase getPetalsBase() {
        return petalsBase;
    }

    public FlowerFile getFlower() {
        return mFlower;
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

    private String getPreferenceString(String key, String defValue) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        return sharedPreferences.getString(key, defValue);
    }

    private boolean getPreferenceBoolean(String key, Boolean defValue) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        return sharedPreferences.getBoolean(key, defValue);
    }
}
