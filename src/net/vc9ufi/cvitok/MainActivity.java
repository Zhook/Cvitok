package net.vc9ufi.cvitok;

import android.app.*;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import net.vc9ufi.cvitok.control.Control;
import net.vc9ufi.cvitok.data.Flower;
import net.vc9ufi.cvitok.data.SaveNLoad;
import net.vc9ufi.cvitok.dialogs.FileDialog;
import net.vc9ufi.cvitok.dialogs.FileListDialog;
import net.vc9ufi.cvitok.dialogs.NameDialog;
import net.vc9ufi.cvitok.fragments.FragmentFlower;
import net.vc9ufi.cvitok.fragments.FragmentLight;
import net.vc9ufi.cvitok.fragments.FragmentPetals;
import net.vc9ufi.cvitok.fragments.FragmentVertices;
import net.vc9ufi.cvitok.petal.RandomFlowerBuilder;
import net.vc9ufi.cvitok.settings.PrefActivity;


public class MainActivity extends Activity {

    App app;

    FragmentFlower frag_flower;
    FragmentLight frag_light;

    FragmentPetals frag_petal;
    FragmentVertices frag_vertices;

    AlertDialog dialogFileList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        app = (App) getApplicationContext();
        app.makeFlowerRenderer((GLSurfaceView) findViewById(R.id.glFlower));

        ActionBar actionBar = getActionBar();
        assert actionBar != null;

        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);

        View customActionBarView = LayoutInflater.from(this).inflate(R.layout.main_actionbar, null);

        actionBar.setCustomView(customActionBarView);
        actionBar.setDisplayShowCustomEnabled(true);

        frag_flower = new FragmentFlower();
        frag_flower.setAppNMainActivity(app, this);
        frag_light = new FragmentLight();

        frag_petal = new FragmentPetals();
        frag_petal.setAppNMainActivity(app, this);
        frag_vertices = new FragmentVertices();

        init(customActionBarView);

        if (app.isStart()) (new FileDialog(this)).show();
    }

    void init(View customActionBarView) {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case (R.id.menu_settings):
                startActivity(new Intent(MainActivity.this, PrefActivity.class));
                return true;
            case (R.id.menu_file_save):
                SaveNLoad.save(app);
                return true;
            case (R.id.menu_file_load):
                (new FileListDialog(this)).show();
                return true;
            case (R.id.menu_file_new):
                NameDialog flowerNameDialog = new NameDialog(this,
                        getString(R.string.dialog_flower_name_title),
                        getString(R.string.flower)) {
                    @Override
                    protected boolean onPositiveClick(String flowerName) {
                        if (flowerName.length() > 0) {
                            if (SaveNLoad.isFileExists(app, flowerName)) {
                                this.setMsg(app.getString(R.string.toast_file_exists));
                                return false;
                            } else {
                                if (Flower.getInstance().setNewFlower(flowerName))
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
                return true;
            case (R.id.menu_file_new_random):
                NameDialog randomFlowerNameDialog = new NameDialog(this,
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
                                    Flower.getInstance().setFlower(flowerBuilder.build());
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
                return true;
        }


        return super.onOptionsItemSelected(item);
    }

    //-------------------------------------------------

    public void addFlowerFragment() {
        if (frame1 == FRAME1.FLOWER) {
            Control.getInstance().setMode(Control.MODE.NULL);
            frame1 = FRAME1.NULL;
            setFrame1(new Fragment());
            frame2 = FRAME2.NULL;
            setFrame2(new Fragment());
        } else {
            Control.getInstance().setMode(Control.MODE.FLOWER);
            frame1 = FRAME1.FLOWER;
            setFrame1(frag_flower);
            frame2 = FRAME2.NULL;
            setFrame2(new Fragment());
        }
    }

    public void addPetalsFragment() {
        if (frame1 == FRAME1.PETAL) {
            Control.getInstance().setMode(Control.MODE.NULL);
            frame1 = FRAME1.NULL;
            setFrame1(new Fragment());
            frame2 = FRAME2.NULL;
            setFrame2(new Fragment());
        } else {
            Control.getInstance().setMode(Control.MODE.PETAL);
            frame1 = FRAME1.PETAL;
            setFrame1(frag_petal);
            frame2 = FRAME2.NULL;
            setFrame2(new Fragment());
        }
    }

    public void addLightFragment() {
        if (frame2 == FRAME2.LIGHT) {
            Control.getInstance().setMode(Control.MODE.FLOWER);
            frame2 = FRAME2.NULL;
            setFrame2(new Fragment());
        } else {
            Control.getInstance().setMode(Control.MODE.LIGHT);
            frame2 = FRAME2.LIGHT;
            setFrame2(frag_light);
        }
    }

    public void addVerticesFragment() {
        if (frame2 == FRAME2.VERTICES) {
            Control.getInstance().setMode(Control.MODE.PETAL);
            frame2 = FRAME2.NULL;
            setFrame2(new Fragment());
        } else {
            Control.getInstance().setMode(Control.MODE.VERTEX);
            frame2 = FRAME2.VERTICES;
            setFrame2(frag_vertices);
        }
    }

    void setFrame1(Fragment fragment) {
        FragmentTransaction fTrans;
        fTrans = getFragmentManager().beginTransaction();
        fTrans.replace(R.id.mainActivity_frame1, fragment);
        fTrans.addToBackStack(null);
        fTrans.commit();
    }

    void setFrame2(Fragment fragment) {
        FragmentTransaction fTrans;
        fTrans = getFragmentManager().beginTransaction();
        fTrans.replace(R.id.mainActivity_frame2, fragment);
        fTrans.addToBackStack(null);
        fTrans.commit();
    }

    private FRAME1 frame1 = FRAME1.NULL;
    private FRAME2 frame2 = FRAME2.NULL;

    private enum FRAME1 {NULL, FLOWER, PETAL}

    private enum FRAME2 {NULL, LIGHT, VERTICES}
}

