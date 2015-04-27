package net.vc9ufi.cvitok.views.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import net.vc9ufi.cvitok.App;
import net.vc9ufi.cvitok.R;
import net.vc9ufi.cvitok.data.FlowerFile;
import net.vc9ufi.cvitok.petal.generator.FlowerGenerator;
import net.vc9ufi.cvitok.views.customlist.BaseItem;
import net.vc9ufi.cvitok.views.customlist.CustomArrayAdapter;
import net.vc9ufi.cvitok.views.customlist.ItemWithTwoValues;
import net.vc9ufi.cvitok.views.dialogs.TwoSeekbarsDialog;
import net.vc9ufi.cvitok.views.settings.Setting;


public class GeneratorFragment extends Fragment {

    private Context context;

    private CustomArrayAdapter mListAdapter;


    private void initListAdapter() {
        mListAdapter = new CustomArrayAdapter(context);
        mListAdapter.add(initBackgroundBrightnessSetting());
        mListAdapter.add(initQuantityOfCirclesSetting());
        mListAdapter.add(initThetaSetting());
        mListAdapter.add(initRadiusOfPetalsSetting());
        mListAdapter.add(initQuantityOfPetalSetting());
        mListAdapter.add(initPetalConvex());
        mListAdapter.add(initPetalBrightnessSetting());
    }

    private BaseItem initQuantityOfCirclesSetting() {
        final ItemWithTwoValues item = new ItemWithTwoValues(getString(R.string.generator_title_circles), getString(R.string.generator_min), getString(R.string.generator_max));
        final SharedPreferences sharedPreferences = Setting.getInstance().getSharedPreferences();

        int min = sharedPreferences.getInt(getString(R.string.generator_key_min_circles), 3);
        int max = sharedPreferences.getInt(getString(R.string.generator_key_max_circles), 6);

        item.setValues(String.valueOf(min), String.valueOf(max));

        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TwoSeekbarsDialog dialog = new TwoSeekbarsDialog() {
                    @Override
                    public void onClickOk(int value1, int value2) {
                        item.setValue1(String.valueOf(value1));
                        item.setValue2(String.valueOf(value2));
                        SharedPreferences.Editor editPref = sharedPreferences.edit();
                        editPref.putInt(getString(R.string.generator_key_min_circles), value1);
                        editPref.putInt(getString(R.string.generator_key_max_circles), value2);
                        editPref.apply();
                    }
                };
                dialog.setTitle(getString(R.string.generator_title_circles));
                dialog.setParameters1(getString(R.string.generator_min), 1, 10,
                        sharedPreferences.getInt(getString(R.string.generator_key_min_circles), 3));
                dialog.setParameters2(getString(R.string.generator_max), 1, 10,
                        sharedPreferences.getInt(getString(R.string.generator_key_max_circles), 6));
                dialog.show(getActivity().getSupportFragmentManager(), "dlg");
            }
        });
        return item;
    }

    private BaseItem initRadiusOfPetalsSetting() {
        final ItemWithTwoValues item = new ItemWithTwoValues(getString(R.string.generator_title_radius), getString(R.string.generator_min), getString(R.string.generator_max));
        final SharedPreferences sharedPreferences = Setting.getInstance().getSharedPreferences();

        int min = sharedPreferences.getInt(getString(R.string.generator_key_min_radius_of_petals), 30);
        int max = sharedPreferences.getInt(getString(R.string.generator_key_max_radius_of_petals), 100);

        item.setValue1(String.valueOf(min));
        item.setValue2(String.valueOf(max));

        item.setPostfix1("%");
        item.setPostfix2("%");

        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TwoSeekbarsDialog dialog = new TwoSeekbarsDialog() {
                    @Override
                    public void onClickOk(int value1, int value2) {
                        item.setValue1(String.valueOf(value1));
                        item.setValue2(String.valueOf(value2));
                        SharedPreferences.Editor editPref = sharedPreferences.edit();
                        editPref.putInt(getString(R.string.generator_key_min_radius_of_petals), value1);
                        editPref.putInt(getString(R.string.generator_key_max_radius_of_petals), value2);
                        editPref.apply();
                    }
                };
                dialog.setTitle(getString(R.string.generator_title_radius));
                dialog.setParameters1(getString(R.string.generator_min), 0, 100,
                        sharedPreferences.getInt(getString(R.string.generator_key_min_radius_of_petals), 30));
                dialog.setParameters2(getString(R.string.generator_max), 0, 100,
                        sharedPreferences.getInt(getString(R.string.generator_key_max_radius_of_petals), 100));
                dialog.show(getActivity().getSupportFragmentManager(), "dlg");
            }
        });
        return item;
    }

    private BaseItem initThetaSetting() {
        final ItemWithTwoValues item = new ItemWithTwoValues(getString(R.string.generator_title_theta), getString(R.string.generator_min), getString(R.string.generator_max));
        final SharedPreferences sharedPreferences = Setting.getInstance().getSharedPreferences();

        int min = sharedPreferences.getInt(getString(R.string.generator_key_min_theta), 0);
        int max = sharedPreferences.getInt(getString(R.string.generator_key_max_theta), 120);

        item.setValue1(String.valueOf(min));
        item.setValue2(String.valueOf(max));

        item.setPostfix1("\u00b0");
        item.setPostfix2("\u00b0");

        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TwoSeekbarsDialog dialog = new TwoSeekbarsDialog() {
                    @Override
                    public void onClickOk(int value1, int value2) {
                        item.setValue1(String.valueOf(value1));
                        item.setValue2(String.valueOf(value2));
                        SharedPreferences.Editor editPref = sharedPreferences.edit();
                        editPref.putInt(getString(R.string.generator_key_min_theta), value1);
                        editPref.putInt(getString(R.string.generator_key_max_theta), value2);
                        editPref.apply();
                    }
                };
                dialog.setTitle(getString(R.string.generator_title_theta));
                dialog.setParameters1(getString(R.string.generator_min), 0, 180,
                        sharedPreferences.getInt(getString(R.string.generator_key_min_theta), 30));
                dialog.setParameters2(getString(R.string.generator_max), 0, 180,
                        sharedPreferences.getInt(getString(R.string.generator_key_max_theta), 100));
                dialog.show(getActivity().getSupportFragmentManager(), "dlg");
            }
        });
        return item;
    }

    private BaseItem initQuantityOfPetalSetting() {
        final ItemWithTwoValues item = new ItemWithTwoValues(getString(R.string.generator_title_quantity_petals), getString(R.string.generator_min), getString(R.string.generator_max));
        final SharedPreferences sharedPreferences = Setting.getInstance().getSharedPreferences();

        int min = sharedPreferences.getInt(getString(R.string.generator_key_min_petals_in_circles), 3);
        int max = sharedPreferences.getInt(getString(R.string.generator_key_max_petals_in_circles), 6);

        item.setValues(String.valueOf(min), String.valueOf(max));

        item.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                TwoSeekbarsDialog dialog = new TwoSeekbarsDialog() {
                    @Override
                    public void onClickOk(int value1, int value2) {
                        item.setValue1(String.valueOf(value1));
                        item.setValue2(String.valueOf(value2));
                        SharedPreferences.Editor editPref = sharedPreferences.edit();
                        editPref.putInt(getString(R.string.generator_key_min_petals_in_circles), value1);
                        editPref.putInt(getString(R.string.generator_key_max_petals_in_circles), value2);
                        editPref.apply();
                    }
                };
                dialog.setTitle(getString(R.string.generator_title_quantity_petals));
                dialog.setParameters1(getString(R.string.generator_min), 1, 30,
                        sharedPreferences.getInt(getString(R.string.generator_key_min_petals_in_circles), 3));
                dialog.setParameters2(getString(R.string.generator_max), 1, 30,
                        sharedPreferences.getInt(getString(R.string.generator_key_max_petals_in_circles), 6));
                dialog.show(getActivity().getSupportFragmentManager(), "dlg");
            }
        });
        return item;
    }

    private BaseItem initBackgroundBrightnessSetting() {
        final ItemWithTwoValues item = new ItemWithTwoValues(getString(R.string.generator_title_background_brightness), getString(R.string.generator_min), getString(R.string.generator_max));
        final SharedPreferences sharedPreferences = Setting.getInstance().getSharedPreferences();

        int min = sharedPreferences.getInt(getString(R.string.generator_key_min_background_brightness), 60);
        int max = sharedPreferences.getInt(getString(R.string.generator_key_max_background_brightness), 100);

        item.setValue1(String.valueOf(min));
        item.setValue2(String.valueOf(max));

        item.setPostfix1("%");
        item.setPostfix2("%");

        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TwoSeekbarsDialog dialog = new TwoSeekbarsDialog() {
                    @Override
                    public void onClickOk(int value1, int value2) {
                        item.setValue1(String.valueOf(value1));
                        item.setValue2(String.valueOf(value2));
                        SharedPreferences.Editor editPref = sharedPreferences.edit();
                        editPref.putInt(getString(R.string.generator_key_min_background_brightness), value1);
                        editPref.putInt(getString(R.string.generator_key_max_background_brightness), value2);
                        editPref.apply();
                    }
                };
                dialog.setTitle(getString(R.string.generator_title_background_brightness));
                dialog.setParameters1(getString(R.string.generator_min), 0, 100,
                        sharedPreferences.getInt(getString(R.string.generator_key_min_background_brightness), 60));
                dialog.setParameters2(getString(R.string.generator_max), 0, 100,
                        sharedPreferences.getInt(getString(R.string.generator_key_max_background_brightness), 100));
                dialog.show(getActivity().getSupportFragmentManager(), "dlg");
            }
        });
        return item;
    }

    private BaseItem initPetalBrightnessSetting() {
        final ItemWithTwoValues item = new ItemWithTwoValues(getString(R.string.generator_title_petals_brightness), getString(R.string.generator_min), getString(R.string.generator_max));
        final SharedPreferences sharedPreferences = Setting.getInstance().getSharedPreferences();

        int min = sharedPreferences.getInt(getString(R.string.generator_key_min_petals_brightness), 0);
        int max = sharedPreferences.getInt(getString(R.string.generator_key_max_petals_brightness), 100);

        item.setValue1(String.valueOf(min));
        item.setValue2(String.valueOf(max));

        item.setPostfix1("%");
        item.setPostfix2("%");

        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TwoSeekbarsDialog dialog = new TwoSeekbarsDialog() {
                    @Override
                    public void onClickOk(int value1, int value2) {
                        item.setValue1(String.valueOf(value1));
                        item.setValue2(String.valueOf(value2));
                        SharedPreferences.Editor editPref = sharedPreferences.edit();
                        editPref.putInt(getString(R.string.generator_key_min_petals_brightness), value1);
                        editPref.putInt(getString(R.string.generator_key_max_petals_brightness), value2);
                        editPref.apply();
                    }
                };
                dialog.setTitle(getString(R.string.generator_title_petals_brightness));
                dialog.setParameters1(getString(R.string.generator_min), 0, 100,
                        sharedPreferences.getInt(getString(R.string.generator_key_min_petals_brightness), 0));
                dialog.setParameters2(getString(R.string.generator_max), 0, 100,
                        sharedPreferences.getInt(getString(R.string.generator_key_max_petals_brightness), 100));
                dialog.show(getActivity().getSupportFragmentManager(), "dlg");
            }
        });
        return item;
    }

    private BaseItem initPetalConvex() {
        final ItemWithTwoValues item = new ItemWithTwoValues(getString(R.string.generator_title_petals_convex), getString(R.string.generator_min), getString(R.string.generator_max));
        final SharedPreferences sharedPreferences = Setting.getInstance().getSharedPreferences();

        int min = sharedPreferences.getInt(getString(R.string.generator_key_min_petals_convex), -5);
        int max = sharedPreferences.getInt(getString(R.string.generator_key_max_petals_convex), 5);

        item.setValue1(String.valueOf(min));
        item.setValue2(String.valueOf(max));

        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TwoSeekbarsDialog dialog = new TwoSeekbarsDialog() {
                    @Override
                    public void onClickOk(int value1, int value2) {
                        item.setValue1(String.valueOf(value1));
                        item.setValue2(String.valueOf(value2));
                        SharedPreferences.Editor editPref = sharedPreferences.edit();
                        editPref.putInt(getString(R.string.generator_key_min_petals_convex), value1);
                        editPref.putInt(getString(R.string.generator_key_max_petals_convex), value2);
                        editPref.apply();
                    }
                };
                dialog.setTitle(getString(R.string.generator_title_petals_convex));
                dialog.setParameters1(getString(R.string.generator_min), -10, +10,
                        sharedPreferences.getInt(getString(R.string.generator_key_min_petals_convex), -5));
                dialog.setParameters2(getString(R.string.generator_max), -10, +10,
                        sharedPreferences.getInt(getString(R.string.generator_key_max_petals_convex), 5));
                dialog.show(getActivity().getSupportFragmentManager(), "dlg");
            }
        });
        return item;
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_generator, container, false);
        context = inflater.getContext();

        initActionBar();

        ListView listView = (ListView) view.findViewById(R.id.generator_listView);

        initListAdapter();
        listView.setAdapter(mListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mListAdapter.getBaseItem(position).onClick(view);
            }
        });

        Button bGenerate = (Button) view.findViewById(R.id.generator_button_generate);
        bGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AsyncTask<Void, Void, FlowerFile>() {
                    @Override
                    protected FlowerFile doInBackground(Void... params) {
                        return new FlowerGenerator(context).generate();
                    }

                    @Override
                    protected void onPostExecute(FlowerFile flowerFile) {
                        super.onPostExecute(flowerFile);
                        ((App) context.getApplicationContext()).setFlower(flowerFile);
                        getFragmentManager().popBackStack();
                    }
                }.execute();
            }
        });

        return view;
    }

    private void initActionBar() {
        android.support.v7.app.ActionBar actionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();
        assert actionBar != null;

        View customActionBarView = View.inflate(context, R.layout.actionbar_generator, null);

        actionBar.setCustomView(customActionBarView);
        actionBar.setDisplayShowCustomEnabled(true);
    }


}
