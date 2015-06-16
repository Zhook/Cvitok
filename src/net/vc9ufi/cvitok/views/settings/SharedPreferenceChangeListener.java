package net.vc9ufi.cvitok.views.settings;

import android.content.Context;
import android.content.SharedPreferences;
import net.vc9ufi.cvitok.R;
import net.vc9ufi.cvitok.views.settings.dialogs.PrefDialogColor;
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

        if (key.equals(mContext.getString(R.string.prefkey_enable_light))) {
            mTrianglesBase.setLightOn(sharedPreferences.getBoolean(key, true));
        }

        if (key.contains(mContext.getString(R.string.prefkey_ambient_light))) {
            int[] ambientI = PrefDialogColor.getColor(mContext, sharedPreferences, R.string.prefkey_ambient_light, new int[]{30, 30, 30});
            float[] ambientF = new float[4];
            ambientF[0] = ambientI[0] / 100f;
            ambientF[1] = ambientI[1] / 100f;
            ambientF[2] = ambientI[2] / 100f;
            ambientF[3] = 1;
            mTrianglesBase.getLight().ambient = ambientF;
        }

        if (key.contains(mContext.getString(R.string.prefkey_diffuse_light))) {
            int[] diffuseI = PrefDialogColor.getColor(mContext, sharedPreferences, R.string.prefkey_diffuse_light, new int[]{50, 50, 50});
            float[] diffuseF = new float[4];
            diffuseF[0] = diffuseI[0] / 100f;
            diffuseF[1] = diffuseI[1] / 100f;
            diffuseF[2] = diffuseI[2] / 100f;
            diffuseF[3] = 1;
            mTrianglesBase.getLight().diffuse = diffuseF;
        }

        if (key.contains(mContext.getString(R.string.prefkey_specular_light))) {
            int[] specularI = PrefDialogColor.getColor(mContext, sharedPreferences, R.string.prefkey_specular_light, new int[]{10, 10, 10});
            float[] specularF = new float[4];
            specularF[0] = specularI[0] / 100f;
            specularF[1] = specularI[1] / 100f;
            specularF[2] = specularI[2] / 100f;
            specularF[3] = 1;
            mTrianglesBase.getLight().specular = specularF;
        }

    }
}
