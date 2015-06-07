package net.vc9ufi.geometry.temp;

import android.content.Context;
import android.graphics.BitmapFactory;
import net.vc9ufi.cvitok.R;
import net.vc9ufi.cvitok.render.TexturedRectangle2D;

import javax.microedition.khronos.opengles.GL10;
import java.util.LinkedList;

public class HUD {
    private Context mContex;


    LinkedList<TexturedRectangle2D> mObjects = new LinkedList<>();
    TexturedRectangle2D button = new TexturedRectangle2D(0, 0, 1, 1);

    public HUD(Context context) {
        mContex = context;
        mObjects.add(button);
    }

    public void loadGLTexture(GL10 gl) {

        button.loadGLTexture(gl, BitmapFactory.decodeResource(mContex.getResources(), R.drawable.action_moving));
    }


    public void draw(GL10 gl) {
//        for (GlObject obj : mObjects) {
            button.draw(gl);
//        }
    }
}
