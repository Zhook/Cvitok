package net.vc9ufi.cvitok.views;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import net.vc9ufi.cvitok.App;
import net.vc9ufi.cvitok.R;
import net.vc9ufi.cvitok.data.Flower;
import net.vc9ufi.cvitok.data.Light;
import net.vc9ufi.cvitok.data.SaveNLoad;
import net.vc9ufi.cvitok.petal.RandomFlowerBuilder;
import net.vc9ufi.cvitok.views.dialogs.FileListDialog;
import net.vc9ufi.cvitok.views.dialogs.NameDialog;
import net.vc9ufi.cvitok.views.dialogs.ScreenshotDialog;
import net.vc9ufi.cvitok.views.fragments.FragmentFlower;
import net.vc9ufi.cvitok.views.fragments.FragmentLight;
import net.vc9ufi.cvitok.views.fragments.FragmentPetals;
import net.vc9ufi.cvitok.views.fragments.FragmentVertices;
import net.vc9ufi.cvitok.views.render.ImplRenderer;
import net.vc9ufi.cvitok.views.render.ScreenShot;
import net.vc9ufi.cvitok.views.settings.PrefActivity;

import javax.microedition.khronos.opengles.GL10;
import java.io.File;
import java.io.IOException;


public class MainActivity extends ActionBarActivity {

    private App app;

    private FragmentFlower frag_flower;
    private FragmentLight frag_light;

    private FragmentPetals frag_petal;
    private FragmentVertices frag_vertices;

    private ImplRenderer mFlowerRenderer;
    private GLSurfaceView glSurfaceView;

    private ProgressBar progressBar;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        initActionBar();

        initDrawerLayout();


        app = (App) getApplicationContext();

        final Flower flower = app.getFlower();
        mFlowerRenderer = new ImplRenderer(flower.getFlowerOnTouchListener()) {
            @Override
            public void paint(GL10 gl) {
                flower.paint(gl);
            }

            @Override
            public void onCaptureScreenShot(final Bitmap bitmap) {
                runOnUiThread(new Runnable() {
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
                                    Toast.makeText(MainActivity.this, getString(R.string.toast_screenshot_error), Toast.LENGTH_SHORT).show();
                                } else {
                                    ScreenshotDialog screenshotDialog = new ScreenshotDialog();
                                    screenshotDialog.setBitmap(bitmap, uri);
                                    screenshotDialog.show(getFragmentManager(), "dlg");
                                }
                            }
                        }.execute(flower.getName());
                    }
                });
            }

            @Override
            public float[] background() {
                return flower.getBackground();
            }

            @Override
            public Light light() {
                return flower.getLight();
            }
        };

        glSurfaceView = (GLSurfaceView) findViewById(R.id.glFlower);
        glSurfaceView.setOnTouchListener(app.getFlower().getOnTouchListener(App.MODE.NULL));
        glSurfaceView.setRenderer(mFlowerRenderer);

        frag_flower = new FragmentFlower();
        frag_flower.setAppNMainActivity(app, this);
        frag_light = new FragmentLight();

        frag_petal = new FragmentPetals();
        frag_petal.setAppNMainActivity(app, this);
        frag_vertices = new FragmentVertices();

        progressBar = (ProgressBar) findViewById(R.id.mainActivity_progressBar);


        if (savedInstanceState == null) mDrawerLayout.openDrawer(findViewById(R.id.left_drawer));
    }

    private void initActionBar() {
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        View customActionBarView = View.inflate(this, R.layout.main_actionbar, null);

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
                addFlowerFragment();
            }
        });

        ImageButton b_petals = (ImageButton) customActionBarView.findViewById(R.id.actionBar_imageButton_petals);
        b_petals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPetalsFragment();
            }
        });
    }

    private void initDrawerLayout() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.main_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                //invalidateOptionsMenu();
            }


            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //invalidateOptionsMenu();
            }

        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        ListView mDrawerMainListView = (ListView) findViewById(R.id.navdrawer_main_listView);
        ListAdapter adapterMain = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.navdrawer_menu_main));
        mDrawerMainListView.setAdapter(adapterMain);
        mDrawerMainListView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        SaveNLoad.save(app);
                        break;
                    case 1:
                        (new FileListDialog(MainActivity.this)).show();
                        break;
                    case 2:
                        NameDialog flowerNameDialog = new NameDialog(MainActivity.this,
                                getString(R.string.dialog_flower_name_title),
                                getString(R.string.flower)) {
                            @Override
                            protected boolean onPositiveClick(String flowerName) {
                                if (flowerName.length() > 0) {
                                    if (SaveNLoad.isFileExists(app, flowerName)) {
                                        this.setMsg(app.getString(R.string.toast_file_exists));
                                        return false;
                                    } else {
                                        if (app.getFlower().setNewFlower(flowerName))
                                            return true;

                                        this.setMsg("invalid name");
                                        return false;
                                    }
                                }
                                this.setMsg(app.getString(R.string.msg_input_name));
                                return false;
                            }
                        };
                        flowerNameDialog.show();
                        break;
                    case 3:
                        NameDialog randomFlowerNameDialog = new NameDialog(MainActivity.this,
                                getString(R.string.dialog_flower_name_title),
                                getString(R.string.flower)) {
                            @Override
                            protected boolean onPositiveClick(String flowerName) {
                                if (flowerName.length() > 0) {
                                    if (SaveNLoad.isFileExists(app, flowerName)) {
                                        this.setMsg(app.getString(R.string.toast_file_exists));
                                        return false;
                                    } else {
                                        if (SaveNLoad.isFileNameValid(flowerName)) {
                                            RandomFlowerBuilder flowerBuilder = new RandomFlowerBuilder(flowerName);
                                            flowerBuilder
                                                    .setBackground()
                                                    .addPetals();
                                            app.getFlower().setFlower(flowerBuilder.build());
                                            return true;
                                        }

                                        this.setMsg("invalid name");
                                        return false;
                                    }
                                }
                                this.setMsg(app.getString(R.string.msg_input_name));
                                return false;
                            }
                        };
                        randomFlowerNameDialog.show();
                        break;
                }
                mDrawerLayout.closeDrawers();
            }
        });

        ListView mDrawerSubListView = (ListView) findViewById(R.id.navdrawer_sub_listView);
        ListAdapter adapterSub = new ArrayAdapter<>(this, R.layout.list_item_small,
                getResources().getStringArray(R.array.navdrawer_menu_sub));
        mDrawerSubListView.setAdapter(adapterSub);
        mDrawerSubListView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        startActivity(new Intent(MainActivity.this, PrefActivity.class));
                        break;
                    case 1:
                        break;
                }
                mDrawerLayout.closeDrawers();
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    //-------------------------------------------------

    public void addFlowerFragment() {
        if (frame1 == FRAME1.FLOWER) {
            glSurfaceView.setOnTouchListener(app.getFlower().getOnTouchListener(App.MODE.NULL));
            frame1 = FRAME1.NULL;
            setFrame1(new Fragment());
            frame2 = FRAME2.NULL;
            setFrame2(new Fragment());
        } else {
            glSurfaceView.setOnTouchListener(app.getFlower().getOnTouchListener(App.MODE.FLOWER));
            frame1 = FRAME1.FLOWER;
            setFrame1(frag_flower);
            frame2 = FRAME2.NULL;
            setFrame2(new Fragment());
        }
    }

    public void addPetalsFragment() {
        if (frame1 == FRAME1.PETAL) {
            glSurfaceView.setOnTouchListener(app.getFlower().getOnTouchListener(App.MODE.NULL));
            frame1 = FRAME1.NULL;
            setFrame1(new Fragment());
            frame2 = FRAME2.NULL;
            setFrame2(new Fragment());
        } else {
            glSurfaceView.setOnTouchListener(app.getFlower().getOnTouchListener(App.MODE.PETAL));
            frame1 = FRAME1.PETAL;
            setFrame1(frag_petal);
            frame2 = FRAME2.NULL;
            setFrame2(new Fragment());
        }
    }

    public void addLightFragment() {
        if (frame2 == FRAME2.LIGHT) {
            glSurfaceView.setOnTouchListener(app.getFlower().getOnTouchListener(App.MODE.FLOWER));
            frame2 = FRAME2.NULL;
            setFrame2(new Fragment());
        } else {
            glSurfaceView.setOnTouchListener(app.getFlower().getOnTouchListener(App.MODE.LIGHT));
            frame2 = FRAME2.LIGHT;
            setFrame2(frag_light);
        }
    }

    public void addVerticesFragment() {
        if (frame2 == FRAME2.VERTICES) {
            glSurfaceView.setOnTouchListener(app.getFlower().getOnTouchListener(App.MODE.PETAL));
            frame2 = FRAME2.NULL;
            setFrame2(new Fragment());
        } else {
            glSurfaceView.setOnTouchListener(app.getFlower().getOnTouchListener(App.MODE.VERTEX));
            frame2 = FRAME2.VERTICES;
            setFrame2(frag_vertices);
        }
    }

    void setFrame1(Fragment fragment) {
        FragmentTransaction fTrans;
        fTrans = getSupportFragmentManager().beginTransaction();
        fTrans.replace(R.id.mainActivity_frame1, fragment);
        fTrans.addToBackStack(null);
        fTrans.commit();
    }

    void setFrame2(Fragment fragment) {
        FragmentTransaction fTrans;
        fTrans = getSupportFragmentManager().beginTransaction();
        fTrans.replace(R.id.mainActivity_frame2, fragment);
        fTrans.addToBackStack(null);
        fTrans.commit();
    }

    private FRAME1 frame1 = FRAME1.NULL;
    private FRAME2 frame2 = FRAME2.NULL;

    private enum FRAME1 {NULL, FLOWER, PETAL}

    private enum FRAME2 {NULL, LIGHT, VERTICES}
}

