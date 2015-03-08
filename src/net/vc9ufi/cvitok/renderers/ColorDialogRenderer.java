package net.vc9ufi.cvitok.renderers;

import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import net.vc9ufi.cvitok.dialogs.colordialog.ColorMatrix;
import net.vc9ufi.geometry.LookAt;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class ColorDialogRenderer implements GLSurfaceView.Renderer {
    private int width;
    private int height;
    ColorMatrix sphere;
    final static float[] BACKGROUND = {0.9f, 1.0f, 0.9f, 0.5f};
    float[] background = BACKGROUND;

    private LookAt camera;

    public ColorDialogRenderer(LookAt camera) {
        this.camera = camera;
        sphere = new ColorMatrix(1.5f, 4);
    }

    public float[] getColor(){
        return background;
    }

    public LookAt getLookAt(){
        return camera;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        gl.glShadeModel(GL10.GL_SMOOTH);             //Enable Smooth Shading
        gl.glClearDepthf(1.0f);                     //Depth Buffer Setup
        gl.glDepthFunc(GL10.GL_LEQUAL);             //The Type Of Depth Testing To Do
        //gl.glEnable(GL10.GL_LIGHTING);
        //gl.glEnable(GL10.GL_LIGHT0);
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
        gl.glClearColor(background[0], background[1], background[2], background[3]);     //Black Background
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        setupPerspective(gl);

        float[] vectors = camera.getCameraTargetUp();
        GLU.gluLookAt(gl, vectors[0], vectors[1], vectors[2], vectors[3], vectors[4], vectors[5], vectors[6], vectors[7], vectors[8]);

        //light(gl);

        //CHECKIT
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
        gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);


        sphere.paint(gl);

        background = sphere.getColor(gl, width / 2, height / 2);
        setupOrtho(gl);
        sphere.paintHUD(gl, ColorMatrix.invertColor(background), width / 20);


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

}
