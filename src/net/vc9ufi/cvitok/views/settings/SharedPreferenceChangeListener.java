package net.vc9ufi.cvitok.views.settings;

import android.content.Context;
import android.content.SharedPreferences;
import net.vc9ufi.cvitok.R;
import net.vc9ufi.geometry.TrianglesBase;


public class SharedPreferenceChangeListener implements SharedPreferences.OnSharedPreferenceChangeListener {

    private Context mContext;
    private TrianglesBase mTrianglesBase;

    public SharedPreferenceChangeListener(Context context, TrianglesBase trianglesBase) {
        mContext = context;
        mTrianglesBase = trianglesBase;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(mContext.getString(R.string.preference_key_transparency))) {
            mTrianglesBase.setTransparency(sharedPreferences.getBoolean(key, true));
        }

        if (key.equals(mContext.getString(R.string.generator_key_background_brightness))) {
            int brightness = sharedPreferences.getInt(key, 90);
            int min = brightness - 10;
            if (min < 0) min = 0;
            int max = brightness + 10;
            if (max > 100) max = 100;
            SharedPreferences.Editor editPref = sharedPreferences.edit();
            editPref.putInt(mContext.getString(R.string.generator_key_adv_min_background_brightness), min);
            editPref.putInt(mContext.getString(R.string.generator_key_adv_max_background_brightness), max);
            editPref.apply();
        }

        if (key.equals(mContext.getString(R.string.generator_key_petals_brightness))) {
            int brightness = sharedPreferences.getInt(key, 10);
            int min = brightness - 10;
            if (min < 0) min = 0;
            int max = brightness + 10;
            if (max > 100) max = 100;

            SharedPreferences.Editor editPref = sharedPreferences.edit();
            editPref.putInt(mContext.getString(R.string.generator_key_adv_min_petals_brightness), min);
            editPref.putInt(mContext.getString(R.string.generator_key_adv_max_petals_brightness), max);
            editPref.apply();
        }

        if (key.equals(mContext.getString(R.string.generator_key_petals))) {
            int quantity = sharedPreferences.getInt(key, 5);
            int minCir = (int) (10 * quantity / 100.0f);
            if (minCir < 1) minCir = 1;
            int maxCir = (int) (10 * quantity / 100.0f) + 4;
            if (maxCir > 10) maxCir = 10;
            int minPet = (int) (13 * quantity / 100.0f);
            if (minPet < 1) minPet = 1;
            int maxPet = (int) (13 * quantity / 100.0f) + 9;
            if (maxPet > 13) maxPet = 13;

            SharedPreferences.Editor editPref = sharedPreferences.edit();
            editPref.putInt(mContext.getString(R.string.generator_key_adv_min_petals_in_circles), minPet);
            editPref.putInt(mContext.getString(R.string.generator_key_adv_max_petals_in_circles), maxPet);
            editPref.putInt(mContext.getString(R.string.generator_key_adv_min_circles), minCir);
            editPref.putInt(mContext.getString(R.string.generator_key_adv_max_circles), maxCir);
            editPref.apply();
        }
    }
}
