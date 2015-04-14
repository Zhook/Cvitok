package net.vc9ufi.cvitok.views;

import android.content.Intent;
import android.content.res.Configuration;
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
import net.vc9ufi.cvitok.data.SaveNLoad;
import net.vc9ufi.cvitok.views.dialogs.FileListDialog;
import net.vc9ufi.cvitok.views.dialogs.NameDialog;
import net.vc9ufi.cvitok.views.fragments.*;
import net.vc9ufi.cvitok.views.settings.PrefActivity;


public class MainActivity extends ActionBarActivity {

    private App app;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private FlowerFragment mFlowerFragment;
    private GeneratorFragment mGeneratorFragment;

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

        mFlowerFragment = new FlowerFragment();
        mGeneratorFragment = new GeneratorFragment();

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


    private void newEmptyFlower() {
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
    }

    private void showSettingsOfGenerator() {
        if (!mGeneratorFragment.isAdded())
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.mainActivity_contentFrame, mGeneratorFragment)
                    .addToBackStack(null)
                    .commit();
    }

    private class MainListItemOnClickListener implements ListView.OnItemClickListener {

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
                    newEmptyFlower();
                    break;
                case 3:
                    showSettingsOfGenerator();
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
                case 1:
                    startActivity(new Intent(MainActivity.this, GeneratorFragment.class));
                    break;
            }
            mDrawerLayout.closeDrawers();
        }
    }
}

