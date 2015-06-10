package net.vc9ufi.cvitok.views.fragments;


import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.ToggleButton;
import net.vc9ufi.cvitok.App;
import net.vc9ufi.cvitok.R;
import net.vc9ufi.cvitok.control.RotatingCamera;
import net.vc9ufi.cvitok.render.ImplRenderer;
import net.vc9ufi.cvitok.render.ScreenShot;
import net.vc9ufi.cvitok.views.customviews.seekbars.DecoratedIntRangeSeekBar;
import net.vc9ufi.cvitok.views.customviews.seekbars.SimpleRangeSeekBar;
import net.vc9ufi.cvitok.views.dialogs.ScreenshotDialog;
import net.vc9ufi.geometry.TrianglesBase;

import java.io.File;
import java.io.IOException;

public class FlowerFragment extends Fragment {

    private App app;
    private Context context;

    private TrianglesBase trianglesBase;

    private ImplRenderer mFlowerRenderer;

    private ProgressBar progressBar;

    private RotatingCamera mCamera;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flower, container, false);
        context = inflater.getContext();
        app = (App) context.getApplicationContext();
        mCamera = app.getPreviewCamera();

        GLSurfaceView glSurfaceView = (GLSurfaceView) view.findViewById(R.id.glFlower);
        trianglesBase = app.getPetalsBase();

        glSurfaceView.setOnTouchListener(mCamera);

        mFlowerRenderer = new ImplRenderer(trianglesBase) {
            @Override
            public void onCaptureScreenShot(final Bitmap bitmap) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.VISIBLE);

                        new AsyncTask<String, Integer, Uri>() {

                            @Override
                            protected Uri doInBackground(String... params) {
                                String name = ScreenShot.getUniqueName(params[0]);
                                try {
                                    File file = ScreenShot.createImageFile(name, ".png");

                                    if (file != null) {
                                        ScreenShot.saveImageFile(file, bitmap);
                                        return ScreenShot.addToGallery(app, file.getAbsolutePath());
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                return null;
                            }

                            @Override
                            protected void onPostExecute(Uri uri) {
                                super.onPostExecute(uri);
                                progressBar.setVisibility(View.INVISIBLE);

                                if (uri == null) {
                                    Toast.makeText(context, getString(R.string.toast_screenshot_error), Toast.LENGTH_SHORT).show();
                                } else {
                                    ScreenshotDialog screenshotDialog = new ScreenshotDialog();
                                    screenshotDialog.setBitmap(bitmap, uri);
                                    screenshotDialog.show(getActivity().getSupportFragmentManager(), "dlg");
                                }
                            }
                        }.execute(app.getFlower().name);
                    }
                });
            }
        };
        glSurfaceView.setRenderer(mFlowerRenderer);

        progressBar = (ProgressBar) view.findViewById(R.id.mainActivity_progressBar);

        ToggleButton controlButton = (ToggleButton) view.findViewById(R.id.controlButton);
        switch (mCamera.getMode()) {
            case ROTATE:
                controlButton.setChecked(true);
                break;
            case MOVE:
                controlButton.setChecked(false);
                break;
        }
        controlButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mCamera.setMode(RotatingCamera.MODE.ROTATE);
                } else {
                    mCamera.setMode(RotatingCamera.MODE.MOVE);
                }
            }
        });

        return view;
    }


    private void setToolsFrame(android.support.v4.app.Fragment fragment) {
//        getActivity().getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.fragmentFlower_toolsFrame, fragment)
//                .commit();
    }


    private static final Fragment PLACEHOLDER = new Fragment();

}
