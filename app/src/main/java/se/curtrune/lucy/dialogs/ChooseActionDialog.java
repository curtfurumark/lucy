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
import se.curtrune.lucy.classes.Item;

public class ChooseActionDialog extends DialogFragment {
    private Button buttonEdit;
    private Button buttonGenerated;
    private Button buttonShowChildren;
    private Button buttonStatistics;
    private Button startTimer;
    public enum Action{
        EDIT, SHOW_CHILDREN, SHOW_STATS, START_TIMER, ADD_CONTACT, ADD_LIST, ADD_CHILD, ADD_IMAGE
    }
    public interface Callback{
        void onClick(Action action);
    }
    private Callback callback;
    public ChooseActionDialog(Item item, Callback callback){
        this.callback = callback;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_action_dialog, container, false);
        initViews(view);
        initListeners();
        return view;
    }
    private void initViews(View view){
        buttonEdit = view.findViewById(R.id.updateChildrenDialog_buttonEdit);
        buttonGenerated = view.findViewById(R.id.updateChildrenDialog_buttonSetGenerated);
        buttonShowChildren = view.findViewById(R.id.updateChildrenDialog_showChildren);
        buttonStatistics = view.findViewById(R.id.chooseActionDialog_buttonStatistics);


    }
    private void initListeners(){
        buttonEdit.setOnClickListener(view->onClick(Action.EDIT));
        //buttonGenerated.setOnClickListener(view->onClick(Action.SET_GENERATED));
        buttonShowChildren.setOnClickListener(view->onClick(Action.SHOW_CHILDREN));
        buttonStatistics.setOnClickListener(view->onClick(Action.SHOW_STATS));
    }
    private void onClick(Action action){
        callback.onClick(action);
        dismiss();
    }
}
