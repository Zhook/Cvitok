package net.vc9ufi.cvitok.views.customlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public class CustomArrayAdapter extends BaseAdapter {

    private ArrayList<BaseItem> mItems = new ArrayList<>();

    private static LayoutInflater mInflater = null;

    public CustomArrayAdapter(Context context) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    public BaseItem getBaseItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BaseItem item = mItems.get(position);
        return item.getView(mInflater, convertView, parent);
    }

    public void add(ArrayList<BaseItem> items) {
        if (items == null) return;
        mItems.addAll(items);
    }

    public void add(BaseItem item) {
        mItems.add(item);
    }


}
