package net.vc9ufi.cvitok.views.settings;

import android.content.Context;
import android.content.SharedPreferences;
import net.vc9ufi.cvitok.App;
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
        if (key.equals(mContext.getString(R.string.prefkey_transparency))) {
            mTrianglesBase.setTransparency(sharedPreferences.getBoolean(key, true));
        }

        if (key.contains(mContext.getString(R.string.prefkey_def_background))) {
            float[] background = ((App) mContext.getApplicationContext()).getPetalsBase().getBackgroundColor().clone();
            ColorPreference.changePart(sharedPreferences, key, background);
            ((App) mContext.getApplicationContext()).getPetalsBase().setBackgroundColor(background);
        }

        if (key.contains(mContext.getString(R.string.prefkey_gen_background))) {
            if (!sharedPreferences.getBoolean(key, true)) {
                float[] background = ColorPreference.loadColor(
                        sharedPreferences,
                        mContext.getString(R.string.prefkey_def_background),
                        null);
                ((App) mContext.getApplicationContext()).getPetalsBase().setBackgroundColor(background);
            }
        }
    }
}
