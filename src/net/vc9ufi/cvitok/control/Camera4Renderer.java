package net.vc9ufi.cvitok.control;

public abstract class Camera4Renderer extends Motion implements LookAt, Runnable {
    private final int DELAY;

    volatile private Camera mCamera;

    volatile private boolean terminate = false;
    private boolean mWork = true;

    public Camera4Renderer(Camera camera, int period) {
        mCamera = camera;
        DELAY = period;
        new Thread(this).start();
    }


    public void terminate() {
        terminate = true;
    }

    @Override
    public void run() {
        while (!terminate) {
            if (workCondition()) mWork = true;

            if (mWork) {
                work(mCamera);
                result(mCamera);
                mWork = false;
            }
            wait(DELAY);
        }
    }

    private void wait(int t) {
        synchronized (Thread.currentThread()) {
            try {
                Thread.currentThread().wait(t);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    abstract boolean workCondition();

    abstract void work(Camera camera);

    public Camera getCamera(){
        return mCamera;
    }
}
