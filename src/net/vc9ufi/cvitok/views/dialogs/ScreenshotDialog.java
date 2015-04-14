package net.vc9ufi.cvitok.views.dialogs;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import net.vc9ufi.cvitok.R;


public class ScreenshotDialog extends DialogFragment {

    Bitmap bitmap;
    Uri uri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle(getResources().getText(R.string.screenshot));
        View view = inflater.inflate(R.layout.dialog_screenshot, container, false);

        ImageView imageView = (ImageView) view.findViewById(R.id.screenshot_imageView);
        imageView.setImageBitmap(bitmap);

        Button bOk = (Button) view.findViewById(R.id.screenshot_button_ok);
        bOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        ImageButton bShare = (ImageButton) view.findViewById(R.id.screenshot_button_share);
        bShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                shareIntent.setType("image/jpeg");
                startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.screenshot)));
                dismiss();
            }
        });
        Toast.makeText(getActivity(), getResources().getText(R.string.screenshot_saved), Toast.LENGTH_SHORT).show();
        return view;
    }

    public void setBitmap(Bitmap bitmap, Uri uri) {
        this.bitmap = bitmap;
        this.uri = uri;
    }
}
