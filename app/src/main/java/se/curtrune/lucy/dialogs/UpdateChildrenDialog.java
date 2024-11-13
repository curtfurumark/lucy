package se.curtrune.lucy.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import se.curtrune.lucy.R;

public class UpdateChildrenDialog extends DialogFragment {
    private Button buttonEdit;
    private Button buttonGenerated;
    private Button buttonShowChildren;
    public enum Action{
        EDIT, SHOW_CHILDREN, SET_GENERATED, SHOW_GENERATED
    }
    public interface Callback{
        void onClick(Action action);
    }
    private Callback callback;
    public UpdateChildrenDialog(Callback callback){
        this.callback = callback;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.update_children_dialog, container, false);
        initViews(view);
        initListeners();
        return view;
    }
    private void initViews(View view){
        buttonEdit = view.findViewById(R.id.updateChildrenDialog_buttonEdit);
        buttonGenerated = view.findViewById(R.id.updateChildrenDialog_buttonSetGenerated);
        buttonShowChildren = view.findViewById(R.id.updateChildrenDialog_showChildren);

    }
    private void initListeners(){
        buttonEdit.setOnClickListener(view->onClick(Action.EDIT));
        buttonGenerated.setOnClickListener(view->onClick(Action.SET_GENERATED));
        buttonShowChildren.setOnClickListener(view->onClick(Action.SHOW_CHILDREN));
    }
    private void onClick(Action action){
        callback.onClick(action);
        dismiss();
    }
}
