package net.vc9ufi.cvitok.views.dialogs;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import net.vc9ufi.cvitok.App;
import net.vc9ufi.cvitok.R;
import net.vc9ufi.cvitok.data.SaveNLoad;

public class FileDialog {

    Context context;
    App app;

    Button save;
    Button load;
    Button new_file;
    AlertDialog mainDialog;
    AlertDialog dialogFileList;

    public FileDialog(Context context) {
        this.context = context;
        app = (App) context.getApplicationContext();
    }

    public void show() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setTitle(R.string.dialog_files_title);

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.dialog_files, null);

        buttonsInit(view);

        alertBuilder.setView(view);
        alertBuilder.setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        mainDialog = alertBuilder.create();
        mainDialog.show();
    }

    private void buttonsInit(View view) {
        save = (Button) view.findViewById(R.id.dialog_file_save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveNLoad.save(app);
                mainDialog.cancel();
            }
        });

        load = (Button) view.findViewById(R.id.dialog_file_load);
        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                (new FileListDialog(context)).show();
                mainDialog.cancel();
            }
        });

        new_file = (Button) view.findViewById(R.id.dialog_file_new);
        new_file.setOnClickListener(newFileClickListener);
    }

    View.OnClickListener newFileClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            NameDialog flowerNameDialog = new NameDialog(context,
                    app.getString(R.string.dialog_flower_name_title),
                    app.getString(R.string.flower)) {
                @Override
                protected boolean onPositiveClick(String flowerName) {
                    if (flowerName.length() > 0) {
                        if (SaveNLoad.isFileExists(mContext, flowerName)) {
                            this.setMsg(mContext.getString(R.string.toast_file_exists));
                            return false;
                        } else {
                            if (app.getFlower().setNewFlower(flowerName)) return true;

                            this.setMsg("invalid name");
                            return false;
                        }
                    }
                    this.setMsg(mContext.getString(R.string.msg_input_name));
                    return false;
                }
            };
            mainDialog.cancel();
            flowerNameDialog.show();
        }
    };
}
