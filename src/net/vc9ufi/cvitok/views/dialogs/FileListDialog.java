package net.vc9ufi.cvitok.views.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.*;
import net.vc9ufi.cvitok.R;
import net.vc9ufi.cvitok.data.SaveNLoad;

public class FileListDialog {

    Context context;
    AlertDialog dialogFileList;

    public FileListDialog(Context context) {
        this.context = context;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.dialog_files_title);

        String[] files = SaveNLoad.getFileList(context);

        if (files.length > 0) {
            ListView fileList = new ListView(context);
            final ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, files);
            fileList.setAdapter(adapter);
            fileList.setOnItemClickListener(onFileClickListener);
            builder.setView(fileList);
        } else {
            TextView msg = new TextView(context);
            msg.setText(R.string.dialog_files_not_found);
            builder.setView(msg);
        }

        builder.setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        dialogFileList = builder.create();
    }

    public void show() {
        dialogFileList.show();
    }

    public static void showDeleteDialog(final Context context, final String name) {
        AlertDialog.Builder delete_file = new AlertDialog.Builder(context);
        delete_file.setTitle(R.string.dialog_delete_file_title);
        TextView field = new TextView(context);
        field.setText(name);
        delete_file.setView(field);
        delete_file.setPositiveButton(R.string.Ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                SaveNLoad.deleteFile(context, name);
            }
        });
        delete_file.setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        delete_file.show();
    }

    AdapterView.OnItemClickListener onFileClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> a, View v, int position, long id) {
            final String name = a.getAdapter().getItem(position).toString();

            AlertDialog.Builder dialogFileOperations = new AlertDialog.Builder(context);
            dialogFileOperations.setTitle(name);

            dialogFileOperations.setPositiveButton(R.string.menu_files_load, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    if (SaveNLoad.load(context, name)) {
                        dialogFileList.cancel();
                    } else {
                        Toast.makeText(context, context.getString(R.string.toast_file_corrupted), Toast.LENGTH_LONG).show();
                    }
                }
            });

            dialogFileOperations.setNeutralButton(R.string.dialog_file_delete, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    if (name != null) {
                        showDeleteDialog(context, name);
                        dialogFileList.cancel();
                    }
                }
            });
            dialogFileOperations.setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            dialogFileOperations.show();
        }
    };


}
