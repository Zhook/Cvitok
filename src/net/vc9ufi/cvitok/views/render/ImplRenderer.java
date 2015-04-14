package net.vc9ufi.cvitok.views.render;

import android.graphics.Bitmap;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.view.View;
import net.vc9ufi.cvitok.control.LookAt;
import net.vc9ufi.cvitok.data.Flower;
import net.vc9ufi.cvitok.data.Light;
import net.vc9ufi.cvitok.views.settings.Setting;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


public abstract class ImplRenderer implements GLSurfaceView.Renderer {

    private int width;
    private int height;

    private volatile boolean screenshot = false;

    private LookAt camera;

    public ImplRenderer(LookAt camera) {
        this.camera = camera;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        gl.glShadeModel(GL10.GL_SMOOTH);
        gl.glEnable(GL10.GL_DEPTH_TEST);
        gl.glClearDepthf(1.0f);
        gl.glDepthFunc(GL10.GL_LEQUAL);
        gl.glEnable(GL10.GL_COLOR_MATERIAL);
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        if (height == 0) {                         //Prevent A Divide By Zero By
            height = 1;                         //Making Height Equal One
        }

        this.width = width;
        this.height = height;

        gl.glViewport(0, 0, width, height);     //Reset The Current Viewport
        gl.glMatrixMode(GL10.GL_PROJECTION);     //Select The Projection Matrix
        gl.glLoadIdentity();                     //Reset The Projection Matrix

        GLU.gluPerspective(gl, 45.0f, (float) width / (float) height, 0.1f, 100.0f);

    }

    @Override
    public void onDrawFrame(GL10 gl) {

        clean(gl);
        transparency(gl);
        makeLight(gl);

        setupPerspective(gl);

        float[] c = camera.getCamera();
        float[] t = camera.getTarget();
        float[] u = camera.getUp();
        GLU.gluLookAt(gl, c[0], c[1], c[2], t[0], t[1], t[2], u[0], u[1], u[2]);


        //CHECKIT
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
        gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);

//        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT, new float[]{0.2f, 0.2f, 0.2f, 0.2f}, 0);
//        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, new float[]{1.0f, 1.0f, 1.0f, 1}, 0);
//        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SPECULAR, new float[]{0.2f, 0.2f, 0.2f, 1}, 0);

        paint(gl);

        if (screenshot) {
            ScreenShot screenShot = new ScreenShot(gl, width, height);
            Bitmap bitmap = screenShot.makeBitmap();
            onCaptureScreenShot(bitmap);
            screenshot = false;
        }
    }

    public abstract void paint(GL10 gl);

    public void onCaptureScreenShot(Bitmap bitmap) {

    }


    private void clean(GL10 gl) {
        float[] background = background();
        if ((background == null) || (background.length != 4))
            background = Flower.BACKGROUND;
        gl.glClearColor(background[0], background[1], background[2], background[3]);
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
    }

    public abstract float[] background();


    private void makeLight(GL10 gl) {
        if (Setting.getInstance().getLight()) {
            Light l = light();
            if (l == null) return;
            gl.glEnable(GL10.GL_LIGHTING);
            l.lightOn(gl);
        } else {
            gl.glDisable(GL10.GL_LIGHTING);
        }
    }

    public abstract Light light();


    private void transparency(GL10 gl) {
        if (Setting.getInstance().getTransparency()) {
            gl.glEnable(GL10.GL_ALPHA_TEST);
            gl.glEnable(GL10.GL_BLEND);
            gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        } else {
            gl.glDisable(GL10.GL_ALPHA_TEST);
            gl.glDisable(GL10.GL_BLEND);
        }
    }

    private void setupPerspective(GL10 gl) {
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glPopMatrix();
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glPopMatrix();
        gl.glEnable(GL10.GL_DEPTH_TEST);
        gl.glLoadIdentity();
    }

    protected void setupOrtho(GL10 gl) {
        gl.glDisable(GL10.GL_DEPTH_TEST);
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glPushMatrix();
        gl.glLoadIdentity();
        GLU.gluOrtho2D(gl, width / 2, -width / 2, height / 2, -height / 2);
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glPushMatrix();
        gl.glLoadIdentity();
    }


    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public LookAt getOnTouchListener() {
        return camera;
    }

    public void makeScreenshot() {
        screenshot = true;
    }
}
