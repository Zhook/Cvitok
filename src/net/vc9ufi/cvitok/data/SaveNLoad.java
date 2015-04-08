package net.vc9ufi.cvitok.data;

import android.content.Context;
import android.widget.Toast;
import net.vc9ufi.cvitok.App;
import net.vc9ufi.cvitok.R;
import net.vc9ufi.cvitok.views.settings.Setting;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.security.InvalidParameterException;
import java.util.ArrayList;


public class SaveNLoad {


    public static String[] getFileList(Context context) {
        ArrayList<String> result = new ArrayList<>();

        String dir = context.getFilesDir().getPath();
        File files[] = (new File(dir)).listFiles();
        if (files != null)
            for (File file : files) {
                String name = file.getName();
                if (name.length() > EXT.length()) {
                    if (name.endsWith(EXT)) {
                        String subname = name.substring(0, name.length() - EXT.length());
                        result.add(subname);
                    }
                }
            }

        return result.toArray(new String[result.size()]);
    }

    public static void save(Context context) {
        FlowerFile flower = ((App)context.getApplicationContext()).getFlower().getFlowerFile();
        if (flower == null) return;
        String filename = flower.name + EXT;
        String json = flower.toJSON();
        SaveNLoad.writeFile(context, filename, json);
    }

    public static boolean load(Context context, String name) {
        if (!isFileNameValid(name)) throw new InvalidParameterException("file name invalid");
        String file = SaveNLoad.readFile(context, name + EXT);
        FlowerFile flower = FlowerFile.getFlower(file);

        if (flower == null) return false;
        ((App)context.getApplicationContext()).getFlower().setFlower(flower);
        Setting.getInstance().setLastFlower(name);
        return true;
    }

    @NotNull
    public static String readFile(Context context, String name) {
        try {
            FileInputStream fis = context.openFileInput(name);

            int count = fis.available();
            if (count == 0) return "";

            byte[] buffer = new byte[count];
            int read = fis.read(buffer, 0, count);
            if (read == count)
                return new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void writeFile(Context context, String filename, String str) {
        try {
            FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
            fos.write(str.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean deleteFile(Context context, String name) {
        boolean result = (new File(context.getFilesDir().getPath(), name + EXT)).delete();
        if (result) Toast.makeText(context, context.getString(R.string.toast_file_deleted), Toast.LENGTH_SHORT).show();
        return result;
    }

    //--------------------------------------------


    public static boolean isFileNameValid(String name) {
        try {
            return new File(name).getCanonicalFile().getName().equals(name);
        } catch (IOException e) {
            return false;
        }
    }

    public static boolean isFileExists(Context context, String name) {
        return context.getFileStreamPath(name+EXT).exists();
    }


    final static String EXT = ".flower.json";
}
