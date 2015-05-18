package net.vc9ufi.cvitok.render;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import net.vc9ufi.cvitok.control.Camera;
import net.vc9ufi.geometry.TrianglesBase;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.LinkedList;


public class ImplRenderer implements GLSurfaceView.Renderer {

    protected int width;
    protected int height;
    protected float[] def_background = new float[]{1, 1, 1, 1};
    protected volatile boolean screenshot = false;

    private TrianglesBase mTrianglesBase;

    public ImplRenderer(TrianglesBase trianglesBD) {
        mTrianglesBase = trianglesBD;
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

        setupPerspective(gl);

        Camera camera = mTrianglesBase.getCamera();
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
        LinkedList<Pointers> pointers = mTrianglesBase.getTrianglesPointers();
        if (pointers != null)
            for (Pointers p : pointers) {
                if (p != null)
                    p.paint(gl);
            }

        if (screenshot) {
            ScreenShot screenShot = new ScreenShot(gl, width, height);
            Bitmap bitmap = screenShot.makeBitmap();
            onCaptureScreenShot(bitmap);
            screenshot = false;
        }
    }

    public void onCaptureScreenShot(Bitmap bitmap) {

    }


    private void clean(GL10 gl) {
        float[] color = mTrianglesBase.getBackgroundColor();

        if ((color != null) && (color.length == 4))
            def_background = color;

        gl.glClearColor(def_background[0], def_background[1], def_background[2], def_background[3]);
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
    }

    private void transparency(GL10 gl) {
        if (mTrianglesBase.isTransparency()) {
            gl.glEnable(GL10.GL_ALPHA_TEST);
            gl.glEnable(GL10.GL_BLEND);
            gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
            gl.glEnable(GL10.GL_POINT_SMOOTH);
            gl.glHint(GL10.GL_POINT_SMOOTH_HINT, GL10.GL_NICEST);
            gl.glEnable(GL10.GL_LINE_SMOOTH);
            gl.glHint(GL10.GL_LINE_SMOOTH_HINT, GL10.GL_NICEST);
            gl.glEnable(GL10.GL_POLYGON_SMOOTH_HINT);
            gl.glHint(GL10.GL_POLYGON_SMOOTH_HINT, GL10.GL_NICEST);
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

    public void makeScreenshot() {
        screenshot = true;
    }

    private float[] getColor(GL10 gl, int x, int y) {
        ByteBuffer pixel = ByteBuffer.allocate(4);
        pixel.order(ByteOrder.nativeOrder());
        pixel.position(0);
        gl.glReadPixels(x, y, 1, 1, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, pixel);

        float[] c = new float[4];
        for (int i = 0; i < 4; i++) {
            c[i] = (pixel.get() & 0xFF) / 255.0f;
        }
        return c;
    }
}
