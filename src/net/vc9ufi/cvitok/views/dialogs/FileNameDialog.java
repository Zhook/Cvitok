package net.vc9ufi.cvitok.views.dialogs;

import android.content.Context;
import net.vc9ufi.cvitok.R;
import net.vc9ufi.cvitok.data.SaveNLoad;


public abstract class FileNameDialog extends NameDialog {



    public FileNameDialog(Context context) {
        super(context, context.getString(R.string.dialog_flower_name_title), context.getString(R.string.flower));
    }

    @Override
    protected boolean onPositiveClick(String flowerName) {
        if (flowerName.length() > 0) {
            if (SaveNLoad.isFileExists(mContext, flowerName)) {
                this.setMsg(mContext.getString(R.string.toast_file_exists));
                return false;
            } else {
                if (SaveNLoad.isFileNameValid(flowerName)) {
                    haveValidName(flowerName);
                    return true;
                }

                this.setMsg("invalid name");
                return false;
            }
        }
        this.setMsg(mContext.getString(R.string.msg_input_name));
        return false;
    }

    protected abstract void haveValidName(String name);
}
