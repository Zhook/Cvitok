package net.vc9ufi.cvitok.views.customlist;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BaseItem {

    private int mViewId;
    private View.OnClickListener mOnClickListener;

    public BaseItem(int mViewId) {
        this.mViewId = mViewId;
    }

    public View getView(LayoutInflater inflater, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) view = inflater.inflate(mViewId, null);
        return view;
    }

    public void setOnClickListener(View.OnClickListener OnClickListener) {
        this.mOnClickListener = OnClickListener;
    }

    public void onClick(View v) {
        if (mOnClickListener != null) mOnClickListener.onClick(v);
    }
}
