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

import se.curtrune.lucy.R;

public class TagsDialog extends BottomSheetDialogFragment {
    private Button buttonDismiss;
    private Button buttonOK;
    private TextView textViewTags;
    private final String tags;
    public interface Callback{
        void onTags(String tags);
    }
    private final Callback callback;

    public TagsDialog(String tags, Callback callback){
        this.tags = tags;
        this.callback = callback;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tags_dialog, container, false);
        initViews(view);
        initListeners();
        initUserInterface();
        return view;
    }
    private void initListeners(){
        buttonDismiss.setOnClickListener(view->dismiss());
        buttonOK.setOnClickListener(view->onButtonOK());
    }
    private void initUserInterface(){
        log("...initUserInterface()");
        if( tags != null && !tags.isEmpty()){
            textViewTags.setText(tags);
        }
    }
    private void initViews(View view){
        buttonDismiss = view.findViewById(R.id.tagsDialog_buttonDismiss);
        buttonOK = view.findViewById(R.id.tagsDialog_buttonOK);
        textViewTags = view.findViewById(R.id.tagsDialog_textViewTags);
    }
    private void onButtonOK(){
        String tags = textViewTags.getText().toString();
        callback.onTags(tags);
        dismiss();

    }
}
