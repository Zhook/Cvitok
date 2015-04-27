package net.vc9ufi.cvitok.views.customlist;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import net.vc9ufi.cvitok.R;

public class ItemWithTwoValues extends BaseItem {
    private String title;
    private String description1;
    private String description2;
    private String postfix1 = "";
    private String postfix2 = "";
    private TextView textView_value1;
    private TextView textView_value2;
    private String value1;
    private String value2;


    public ItemWithTwoValues(String title, String description1, String description2) {
        super(R.layout.listitem_two_values);
        this.title = title;
        this.description1 = description1;
        this.description2 = description2;
    }

    @Override
    public View getView(LayoutInflater inflater, View convertView, ViewGroup parent) {
        View view = super.getView(inflater, convertView, parent);

        TextView textView_title = (TextView) view.findViewById(R.id.listitem_two_values_textView_title);
        textView_title.setText(title);
        TextView textView_des1 = (TextView) view.findViewById(R.id.listitem_two_values_textView_des1);
        textView_des1.setText(description1);
        TextView textView_des2 = (TextView) view.findViewById(R.id.listitem_two_values_textView_des2);
        textView_des2.setText(description2);
        TextView textView_postfix1 = (TextView) view.findViewById(R.id.listitem_two_values_textView_postfix1);
        textView_postfix1.setText(postfix1);
        TextView textView_postfix2 = (TextView) view.findViewById(R.id.listitem_two_values_textView_postfix2);
        textView_postfix2.setText(postfix2);

        textView_value1 = (TextView) view.findViewById(R.id.listitem_two_values_textView_value1);
        textView_value1.setText(value1);
        textView_value2 = (TextView) view.findViewById(R.id.listitem_two_values_textView_value2);
        textView_value2.setText(value2);

        return view;
    }

    public void setPostfix1(String postfix) {
        postfix1 = postfix;
    }

    public void setPostfix2(String postfix) {
        postfix2 = postfix;
    }

    public void setValues(String value1, String value2) {
        setValue1(value1);
        setValue2(value2);
    }

    public void setValue1(String value) {
        value1 = value;
        if (textView_value1 != null)
            textView_value1.setText(String.valueOf(value1));

    }

    public void setValue2(String value) {
        value2 = value;
        if (textView_value2 != null)
            textView_value2.setText(String.valueOf(value2));
    }

}
