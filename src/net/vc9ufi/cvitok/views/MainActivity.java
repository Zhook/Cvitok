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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import net.vc9ufi.cvitok.App;
import net.vc9ufi.cvitok.R;
import net.vc9ufi.cvitok.data.FlowerFile;
import net.vc9ufi.cvitok.data.SaveNLoad;
import net.vc9ufi.cvitok.petal.generator.FlowerGenerator;
import net.vc9ufi.cvitok.service.CvitokService;
import net.vc9ufi.cvitok.views.dialogs.FileListDialog;
import net.vc9ufi.cvitok.views.dialogs.colordialog.FileNameDialog;
import net.vc9ufi.cvitok.views.fragments.FlowerFragment;
import net.vc9ufi.cvitok.views.settings.PrefActivity;


public class MainActivity extends ActionBarActivity {

    private App app;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private FlowerFragment mFlowerFragment = new FlowerFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        app = (App) getApplicationContext();

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

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
                    String name = app.getFlower().name;
                    if ((name == null) || (name.length() == 0)) {
                        (new FileNameDialog(app) {

                            @Override
                            protected void haveValidName(String name) {
                                app.getFlower().name = name;
                                SaveNLoad.save(app);
                            }
                        }).show();
                    } else SaveNLoad.save(app);
                    break;
                case 1:
                    (new FileListDialog(MainActivity.this)).show();
                    break;
                case 2:
                    new AsyncTask<Void, Void, FlowerFile>() {
                        @Override
                        protected FlowerFile doInBackground(Void... params) {
                            return new FlowerGenerator(app).generate();
                        }

                        @Override
                        protected void onPostExecute(FlowerFile flowerFile) {
                            super.onPostExecute(flowerFile);
                            app.setFlower(flowerFile);
                        }
                    }.execute();
                    break;
                case 3:
                    SetUpWallpaper();
                    System.out.println("myout: set up wallpaper");
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

