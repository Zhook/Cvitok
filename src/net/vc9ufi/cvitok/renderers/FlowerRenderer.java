package net.vc9ufi.cvitok.renderers;

import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import net.vc9ufi.cvitok.App;
import net.vc9ufi.cvitok.control.Control;
import net.vc9ufi.cvitok.data.Flower;
import net.vc9ufi.cvitok.data.Light;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


public class FlowerRenderer implements GLSurfaceView.Renderer {

    private int width;
    private int height;
    private App app;

    private volatile boolean transparency = false;
    private volatile boolean light = false;

    public FlowerRenderer(App app) {
        this.app = app;
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
        light(gl);

        setupPerspective(gl);

        float[] camera = Control.getInstance().getLookAtVectors();
        GLU.gluLookAt(gl, camera[0], camera[1], camera[2], camera[3], camera[4], camera[5], camera[6], camera[7], camera[8]);


        //CHECKIT
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
        gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);

//        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT, new float[]{0.2f, 0.2f, 0.2f, 0.2f}, 0);
//        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, new float[]{1.0f, 1.0f, 1.0f, 1}, 0);
//        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SPECULAR, new float[]{0.2f, 0.2f, 0.2f, 1}, 0);

        Flower.getInstance().paint(gl);
    }

    private void clean(GL10 gl) {
        float[] background = Flower.getInstance().getBackground();
        if ((background == null) || (background.length != 4))
            background = Flower.BACKGROUND;
        gl.glClearColor(background[0], background[1], background[2], background[3]);
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
    }

    private void light(GL10 gl) {
        if (light) {
            Light l = Flower.getInstance().getLight();
            gl.glEnable(GL10.GL_LIGHTING);
            l.lightOn(gl);
        } else {
            gl.glDisable(GL10.GL_LIGHTING);
        }
    }

    public void transparency(GL10 gl) {
        if (transparency) {
            gl.glEnable(GL10.GL_ALPHA_TEST);
            gl.glEnable(GL10.GL_BLEND);
            gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        } else {
            gl.glDisable(GL10.GL_ALPHA_TEST);
            gl.glDisable(GL10.GL_BLEND);
        }
    }

    public void setupPerspective(GL10 gl) {
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glPopMatrix();
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glPopMatrix();
        gl.glEnable(GL10.GL_DEPTH_TEST);
        gl.glLoadIdentity();
    }

    public void setupOrtho(GL10 gl) {
        gl.glDisable(GL10.GL_DEPTH_TEST);
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glPushMatrix();
        gl.glLoadIdentity();
        GLU.gluOrtho2D(gl, width / 2, -width / 2, height / 2, -height / 2);
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glPushMatrix();
        gl.glLoadIdentity();
    }


    public void setTransparency(boolean transparency) {
        this.transparency = transparency;
    }

    public void setLight(boolean light) {
        this.light = light;
    }

}
