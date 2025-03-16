package se.curtrune.lucy.dialogs;

import static se.curtrune.lucy.util.Logger.log;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Locale;

import se.curtrune.lucy.R;
import se.curtrune.lucy.web.VersionInfo;

public class UpdateDialog extends BottomSheetDialogFragment {
    private final VersionInfo versionInfo;
    private TextView textViewHeading;
    private TextView textViewInfo;
    private Button buttonUpdate;
    private Button buttonCancel;
    public interface  Callback{
        void onClick();
    }
    private final Callback callback;
    public UpdateDialog(VersionInfo versionInfo, Callback callback){
        this.versionInfo = versionInfo;
        this.callback = callback;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.update_dialog, container, false);
        initViews(view);
        initListeners();
        initUserInterface();
        return view;
    }
    private void initViews(View view){
        log("...initViews(View)");
        textViewHeading = view.findViewById(R.id.updateDialog_heading);
        textViewInfo = view.findViewById(R.id.updateDialog_versionInfo);
        buttonCancel = view.findViewById(R.id.updateDialog_cancel);
        buttonUpdate = view.findViewById(R.id.updateDialog_update);
    }
    private void initListeners(){
        buttonUpdate.setOnClickListener(view->callback.onClick());
        buttonCancel.setOnClickListener(view->dismiss());
    }
    private void initUserInterface(){
        textViewHeading.setText("update available");
        String strInfo = String.format(Locale.getDefault(),"version: %d\nname: %s\nfeatures: %s",
                versionInfo.versionCode, versionInfo.versionName, versionInfo.versionInfo);
        textViewInfo.setText(strInfo);

    }
}
