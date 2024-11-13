package se.curtrune.lucy.dialogs;

import static se.curtrune.lucy.util.Logger.log;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import se.curtrune.lucy.R;

public class ImageDialog extends DialogFragment {
    private ImageView imageView;
    private Button buttonOK;
    private final Bitmap bitmap;
    public ImageDialog(Bitmap bitmap){
        this.bitmap = bitmap;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.image_dialog , container, false);
        initViews(view);
        initListeners();
        initUserInterface();
        return view;
    }
    private void initListeners(){
        log("...initListeners()");
        buttonOK.setOnClickListener(view->dismiss());
    }
    private void initUserInterface(){
        imageView.setImageBitmap(bitmap);
    }
    private void initViews(View view){
        log("...initView(View)");
        imageView = view.findViewById(R.id.imageDialog_imageView);
        buttonOK = view.findViewById(R.id.imageDialog_buttonOK);
    }
}
