package net.vc9ufi.cvitok.views.settings;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import net.vc9ufi.cvitok.App;
import net.vc9ufi.cvitok.R;

public class Setting {
    private static final Setting setting = new Setting();

    App mApp;
    private SharedPreferences mPreferences;


    private Setting() {
    }

    public static Setting getInstance() {
        return setting;
    }


    public void setApp(App a) {
        mApp = a;
        mPreferences = PreferenceManager.getDefaultSharedPreferences(mApp.getBaseContext());
        mPreferences.registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
    }

    private SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (key.equals(mApp.getString(R.string.preference_key_quality))) {
                mApp.getFlower().setQuality(getQuality());
            }
        }
    };

    public SharedPreferences getSharedPreferences(){
        return mPreferences;
    }

    public String getLastFile() {
        return mPreferences.getString(mApp.getString(R.string.preference_key_lastfile), "");
    }

    public boolean getTransparency() {
        return mPreferences.getBoolean(mApp.getString(R.string.preference_key_transparency), false);
    }

    public int getQuality() {
        return mPreferences.getInt(mApp.getString(R.string.preference_key_quality), Setting.DEF_QUALITY);
    }

    public boolean getLight() {
        return mPreferences.getBoolean(mApp.getString(R.string.preference_key_light_switch), false);
    }

    public void setLastFlower(String name) {
//        SharedPreferences.Editor editPref = mPreferences.edit();
//        editPref.putString(getString(R.string.preference_key_lastfile),name);
//        editPref.apply();
    }

    //------------------------------------------------------------------------------Color

    public static int getIntColor(float[] colorF) {
        int r = (int) (colorF[0] * 255);
        int g = (int) (colorF[1] * 255);
        int b = (int) (colorF[2] * 255);
        int a = (int) (colorF[3] * 255);
        return Color.argb(a, r, g, b);
    }

    public static float[] getFloatColor(int color) {
        float[] result = new float[4];
        result[0] = Color.red(color) / 255f;
        result[1] = Color.green(color) / 255f;
        result[2] = Color.blue(color) / 255f;
        result[3] = Color.alpha(color) / 255f;
        return result;
    }


    public static final String ANDROID_NS = "http://schemas.android.com/apk/res/android";

    //------------------------------------------------------------------------------Camera
    public float cameraSensitivityAngle;
    public static final float CAMERA_SENSITIVITY_ANGLE_DEF = 0.003f;
    public float cameraSensitivityRadius;
    public static final float CAMERA_SENSITIVITY_RADIUS_DEF = 0.01f;


    //------------------------------------------------------------------------------Modify
    public float modifySensitivityRotate;
    public static final float MODIFY_SENSITIVITY_ROTATE = 0.003f;
    public float modifySensitivityMove;
    public static final float MODIFY_SENSITIVITY_MOVE = 0.01f;

    public static final float[] DEF_COLOR_F = new float[]{0.5f, 0.5f, 0.5f, 0.5f};
    public static final int DEF_QUALITY = 10;


}
