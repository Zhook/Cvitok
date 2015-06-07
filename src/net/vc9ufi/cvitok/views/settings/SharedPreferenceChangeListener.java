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

    }
}
