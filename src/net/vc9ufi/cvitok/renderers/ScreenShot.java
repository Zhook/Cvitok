package net.vc9ufi.cvitok.renderers;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.view.Gravity;
import android.widget.Toast;
import net.vc9ufi.cvitok.App;
import net.vc9ufi.cvitok.R;

import javax.microedition.khronos.opengles.GL10;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class ScreenShot {

    App app;
    private int width;
    private int height;
    int size;
    int[] mPixelsBuffer;
    String name;
    File file = null;


    public ScreenShot(App app, GL10 gl, int width, int height, String name) {
        this.app = app;
        this.width = width;
        this.height = height;
        size = width * height;
        this.name = name;

        ByteBuffer mByteBuffer = ByteBuffer.allocateDirect(size * 4);
        mByteBuffer.order(ByteOrder.nativeOrder());
        gl.glReadPixels(0, 0, width, height, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, mByteBuffer);

        mPixelsBuffer = new int[size];
        mByteBuffer.asIntBuffer().get(mPixelsBuffer);
    }

    public void addPngScreenShotToAlbum() {
        Bitmap bitmap = makeBitmap();

        try {
            file = createImageFile(name, ".png");

            if (file != null) {
                saveImageFile(file, bitmap);

                galleryAddPic(app, file.getAbsolutePath());

                app.runInMainActivity(new Runnable() {
                    @Override
                    public void run() {
                        showResultToast(app, file);
                    }
                });
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private Bitmap makeBitmap() {

        swapRedNBlue(mPixelsBuffer);

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(mPixelsBuffer, size - width, -width, 0, 0, width, height);

        return bitmap;
    }

    public static File createImageFile(String name, String exp) throws IOException {
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        if (!storageDir.exists())
            if (!storageDir.mkdir())
                return null;

        return new File(storageDir, name + exp);
    }

    public static void saveImageFile(File imagefile, Bitmap bitmap) throws IOException {
        FileOutputStream outputStream = new FileOutputStream(imagefile);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        outputStream.close();
    }

    private void galleryAddPic(Context context, String path) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(path);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }

    private void showResultToast(Context context, File file) {
        if (file == null) {
            Toast toast = Toast.makeText(
                    context,
                    R.string.toast_screenshot_error,
                    Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else {
            Toast toast = Toast.makeText(
                    context,
                    context.getString(R.string.toast_screenshot_saved) + file.getName(),
                    Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }


    private static void swapRedNBlue(int[] pixelsBuffer) {
        for (int i = 0; i < pixelsBuffer.length; ++i) {
            pixelsBuffer[i] = (pixelsBuffer[i] & 0xff00ff00) |
                    ((pixelsBuffer[i] & 0x000000ff) << 16) |
                    ((pixelsBuffer[i] & 0x00ff0000) >> 16);
        }
    }

    static SimpleDateFormat formater = new SimpleDateFormat("_yyyy_MM_dd_HH_mm_ss_");

    public static String getUniqueName(String name) {
        String date = formater.format(new Date());
        String ran = String.valueOf(new Random().nextInt(100));
        return name + date + ran;
    }
}
