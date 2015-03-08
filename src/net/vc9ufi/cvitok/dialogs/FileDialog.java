package net.vc9ufi.cvitok.dialogs;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import net.vc9ufi.cvitok.App;
import net.vc9ufi.cvitok.R;
import net.vc9ufi.cvitok.data.Flower;
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

        View view = ((LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.dialog_files, null);

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
                showFileListDialog(context);
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
                        if (SaveNLoad.isFileExists(context, flowerName)) {
                            this.setMsg(context.getString(R.string.toast_file_exists));
                            return false;
                        } else {
                            if (Flower.getInstance().setNewFlower(flowerName)) return true;

                            this.setMsg("invalid name");
                            return false;
                        }
                    }
                    this.setMsg(context.getString(R.string.msg_input_name));
                    return false;
                }
            };
            mainDialog.cancel();
            flowerNameDialog.show();
        }
    };

    public void showFileListDialog(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.dialog_files_title);

        String[] files = SaveNLoad.getFileList(context);

        if ((files != null ? files.length : 0) > 0) {
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

            dialogFileOperations.setPositiveButton(R.string.dialog_files_load, new DialogInterface.OnClickListener() {
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
