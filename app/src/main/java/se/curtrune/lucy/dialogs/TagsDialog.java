package se.curtrune.lucy.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import se.curtrune.lucy.R;

public class TagsDialog extends DialogFragment {
    private Button buttonDismiss;
    private Button buttonOK;
    private TextView textViewTags;
    public interface Callback{
        void onTags(String tags);
    }
    private Callback callback;
    public TagsDialog(Callback callback){
        this.callback = callback;

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tags_dialog, container, false);
        initViews(view);
        initListeners();
        return view;
    }
    private void initListeners(){
        buttonDismiss.setOnClickListener(view->dismiss());
        buttonOK.setOnClickListener(view->onButtonOK());
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
