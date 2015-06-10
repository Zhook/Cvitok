package net.vc9ufi.cvitok.views.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import net.vc9ufi.cvitok.R;

public abstract class NameDialog {
    Context mContext;
    String mTitle;
    String mDefaultName;
    String mMessage = "";

    TextView textViewMsg;

    public NameDialog(Context context, String title, String defaultName) {
        this.mContext = context;
        this.mTitle = title;
        this.mDefaultName = defaultName;
    }

    public void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(mTitle);

        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.edit_char_n_dig, null);

        final EditText editTextName = (EditText) view.findViewById(R.id.name_dialog_edit_text);
        editTextName.setText(mDefaultName);

        textViewMsg = (TextView) view.findViewById(R.id.name_dialog_msg_textview);
        textViewMsg.setText(mMessage);


        builder.setView(view);
        builder.setPositiveButton(R.string.Ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        builder.setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        final AlertDialog nameDialog = builder.create();
        nameDialog.show();

        nameDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean wantToCloseDialog = onPositiveClick(editTextName.getText().toString());
                textViewMsg.setText(mMessage);
                if (wantToCloseDialog) nameDialog.dismiss();
            }
        });


    }

    public void setMsg(String msg) {
        mMessage = msg;
    }

    protected abstract boolean onPositiveClick(String name);
}
