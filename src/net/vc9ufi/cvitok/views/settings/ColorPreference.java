package net.vc9ufi.cvitok.views.settings;


import android.content.SharedPreferences;

public class ColorPreference {

    public static final String RED_KEY = "_red";
    public static final String GREEN_KEY = "_green";
    public static final String BLUE_KEY = "_blue";
    public static final String ALPHA_KEY = "_alpha";


    public static float[] loadColor(SharedPreferences sharedPreferences, String key, float[] defcolor) {
        if (defcolor == null) defcolor = new float[]{0.3f, 0.5f, 0.7f, 1};
        float[] color = new float[4];
        color[0] = sharedPreferences.getFloat(key + RED_KEY, defcolor[0]);
        color[1] = sharedPreferences.getFloat(key + GREEN_KEY, defcolor[1]);
        color[2] = sharedPreferences.getFloat(key + BLUE_KEY, defcolor[2]);
        color[3] = sharedPreferences.getFloat(key + ALPHA_KEY, defcolor[3]);
        return color;
    }

    public static void saveColor(SharedPreferences sharedPreferences, String key, float[] color) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(key + RED_KEY, color[0]);
        editor.putFloat(key + GREEN_KEY, color[1]);
        editor.putFloat(key + BLUE_KEY, color[2]);
        editor.putFloat(key + ALPHA_KEY, color[3]);
        editor.apply();
    }

    public static void changePart(SharedPreferences sharedPreferences, String key, float[] color) {
        if (key.contains(RED_KEY)) {
            color[0] = sharedPreferences.getFloat(key, color[0]);
        } else if (key.contains(GREEN_KEY)) {
            color[1] = sharedPreferences.getFloat(key, color[1]);
        } else if (key.contains(BLUE_KEY)) {
            color[2] = sharedPreferences.getFloat(key, color[2]);
        } else if (key.contains(ALPHA_KEY)) {
            color[3] = sharedPreferences.getFloat(key, color[3]);
        }
    }


}
