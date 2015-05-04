package net.vc9ufi.cvitok.views.fragments;


import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;
import net.vc9ufi.cvitok.App;
import net.vc9ufi.cvitok.R;
import net.vc9ufi.cvitok.control.RotatingCamera;
import net.vc9ufi.cvitok.render.ImplRenderer;
import net.vc9ufi.cvitok.render.ScreenShot;
import net.vc9ufi.cvitok.views.dialogs.ScreenshotDialog;
import net.vc9ufi.geometry.TrianglesBase;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class FlowerFragment extends Fragment {

    private App app;
    private Context context;

    private TrianglesBase trianglesBase;

    private FragmentFlowerTools mFlowerTools;
    private FragmentPetalsTools mPetalTools;

    private ImplRenderer mFlowerRenderer;

    private ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFlowerTools = new FragmentFlowerTools();
        mPetalTools = new FragmentPetalsTools();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flower, container, false);
        context = inflater.getContext();
        app = (App) context.getApplicationContext();

        GLSurfaceView glSurfaceView = (GLSurfaceView) view.findViewById(R.id.glFlower);
        trianglesBase = app.getPetalsBase();
        RotatingCamera lookAt = new RotatingCamera() {
            @Override
            public void result(float[] camera, float[] target, float[] up) {
                trianglesBase.setLookAt(camera, target, up);
                System.out.println("myout: " + Arrays.toString(camera));
            }
        };
        glSurfaceView.setOnTouchListener(lookAt);

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

        initActionBar();


        return view;
    }

    private void initActionBar() {
        android.support.v7.app.ActionBar actionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();
        assert actionBar != null;

        View customActionBarView = View.inflate(context, R.layout.actionbar_flower, null);

        actionBar.setCustomView(customActionBarView);
        actionBar.setDisplayShowCustomEnabled(true);

        ImageButton b_screenshot = (ImageButton) customActionBarView.findViewById(R.id.actionBar_imageButton_screenshot);
        b_screenshot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFlowerRenderer.makeScreenshot();
            }
        });

        ImageButton b_flower = (ImageButton) customActionBarView.findViewById(R.id.actionBar_imageButton_flower);
        b_flower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFlowerToolsFragment();
            }
        });

        ImageButton b_petals = (ImageButton) customActionBarView.findViewById(R.id.actionBar_imageButton_petals);
        b_petals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPetalsToolsFragment();
            }
        });
    }


    private void addFlowerToolsFragment() {
        if (mFlowerTools.isAdded()) {
            setToolsFrame(PLACEHOLDER);

        } else {
            setToolsFrame(mFlowerTools);
        }
    }

    private void addPetalsToolsFragment() {
        if (mPetalTools.isAdded()) {
            setToolsFrame(PLACEHOLDER);

        } else {
            setToolsFrame(mPetalTools);
        }
    }

    private void setToolsFrame(android.support.v4.app.Fragment fragment) {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentFlower_toolsFrame, fragment)
                .commit();
    }


    private static final Fragment PLACEHOLDER = new Fragment();

}
