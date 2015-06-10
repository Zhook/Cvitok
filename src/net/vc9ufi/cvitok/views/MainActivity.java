package net.vc9ufi.cvitok.views;

import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import net.vc9ufi.cvitok.App;
import net.vc9ufi.cvitok.R;
import net.vc9ufi.cvitok.data.FlowerFile;
import net.vc9ufi.cvitok.data.SaveNLoad;
import net.vc9ufi.cvitok.generator.FlowerGenerator;
import net.vc9ufi.cvitok.service.CvitokService;
import net.vc9ufi.cvitok.views.dialogs.FileListDialog;
import net.vc9ufi.cvitok.views.dialogs.FileNameDialog;
import net.vc9ufi.cvitok.views.fragments.FlowerFragment;
import net.vc9ufi.cvitok.views.settings.PrefActivity;


public class MainActivity extends ActionBarActivity {

    private App mApp;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private FlowerFragment mFlowerFragment = new FlowerFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        mApp = (App) getApplicationContext();


        initActionBar();
        initDrawerLayout();


        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.mainActivity_contentFrame, mFlowerFragment)
                .commit();
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
        mDrawerMainListView.setOnItemClickListener(new MainListItemOnClickListener());

        ListView mDrawerSubListView = (ListView) findViewById(R.id.navdrawer_sub_listView);
        ListAdapter adapterSub = new ArrayAdapter<>(this, R.layout.list_item_small,
                getResources().getStringArray(R.array.navdrawer_menu_sub));
        mDrawerSubListView.setAdapter(adapterSub);
        mDrawerSubListView.setOnItemClickListener(new SubListItemOnClickListener());

        mDrawerLayout.openDrawer(findViewById(R.id.left_drawer));
    }

    private void initActionBar() {
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;

        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        View customActionBarView = View.inflate(this, R.layout.actionbar_flower, null);

        actionBar.setCustomView(customActionBarView);
        actionBar.setDisplayShowCustomEnabled(true);

        ImageButton b_screenshot = (ImageButton) customActionBarView.findViewById(R.id.actionBar_imageButton_screenshot);
        b_screenshot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mApp.getPetalsBase().makeScreenshot();
            }
        });

        ImageButton b_generate = (ImageButton) customActionBarView.findViewById(R.id.actionBar_imageButton_generate);
        b_generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncTask<Void, Void, FlowerFile>() {
                    @Override
                    protected FlowerFile doInBackground(Void... params) {
                        return new FlowerGenerator(mApp).generate();
                    }

                    @Override
                    protected void onPostExecute(FlowerFile flowerFile) {
                        super.onPostExecute(flowerFile);
                        mApp.setFlower(flowerFile);
                    }
                }.execute();
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

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(findViewById(R.id.left_drawer))) {
            mDrawerLayout.closeDrawers();
        } else {
            super.onBackPressed();
        }
    }


    private void SetUpWallpaper() {
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= 16) {
            intent.setAction(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
            intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT, new ComponentName(MainActivity.this, CvitokService.class));
        } else {
            intent.setAction(WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER);
        }
        startActivity(intent);
    }


    private class MainListItemOnClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch (position) {
                case 0:
                    mApp.sort();
                    break;
                case 1:
                    String name = mApp.getFlower().name;
                    if ((name == null) || (name.length() == 0)) {
                        (new FileNameDialog(MainActivity.this) {

                            @Override
                            protected void haveValidName(String name) {
                                mApp.getFlower().name = name;
                                SaveNLoad.save(mApp);
                            }
                        }).show();
                    } else SaveNLoad.save(mApp);
                    break;
                case 2:
                    (new FileListDialog(MainActivity.this)).show();
                    break;
                case 3:
                    new AsyncTask<Void, Void, FlowerFile>() {
                        @Override
                        protected FlowerFile doInBackground(Void... params) {
                            return new FlowerGenerator(mApp).generate();
                        }

                        @Override
                        protected void onPostExecute(FlowerFile flowerFile) {
                            super.onPostExecute(flowerFile);
                            mApp.setFlower(flowerFile);
                        }
                    }.execute();
                    break;
                case 4:
                    SetUpWallpaper();
                    break;
            }
            mDrawerLayout.closeDrawers();
        }
    }

    private class SubListItemOnClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch (position) {
                case 0:
                    startActivity(new Intent(MainActivity.this, PrefActivity.class));
                    break;
            }
            mDrawerLayout.closeDrawers();
        }
    }
}

