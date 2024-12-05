package se.curtrune.lucy.dialogs;

import static se.curtrune.lucy.util.Logger.log;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import se.curtrune.lucy.R;
import se.curtrune.lucy.classes.Item;

public class ChooseActionDialog extends BottomSheetDialogFragment {
    private Button buttonEdit;
    private Button buttonGenerated;
    private Button buttonShowChildren;
    private Button buttonStatistics;
    private Button buttonAddList;
    private Button startTimer;
    private Item currentItem;
    public enum Action{
        EDIT, SHOW_CHILDREN, SHOW_STATS, START_TIMER, ADD_CONTACT, ADD_LIST, ADD_CHILD, ADD_IMAGE
    }
    public interface Callback{
        void onClick(Action action);
    }
    private Callback callback;
    public ChooseActionDialog(Item item, Callback callback){
        this.callback = callback;
        this.currentItem = item;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_action_dialog, container, false);
        initViews(view);
        initListeners();
        setUserInterface(currentItem);
        return view;
    }
    private void initViews(View view){
        buttonEdit = view.findViewById(R.id.updateChildrenDialog_buttonEdit);
        buttonGenerated = view.findViewById(R.id.updateChildrenDialog_buttonSetGenerated);
        buttonShowChildren = view.findViewById(R.id.updateChildrenDialog_showChildren);
        buttonStatistics = view.findViewById(R.id.chooseActionDialog_buttonStatistics);
        buttonAddList = view.findViewById(R.id.chooseActionDialog_buttonAddList);


    }
    private void initListeners(){
        buttonEdit.setOnClickListener(view->onClick(Action.EDIT));
        //buttonGenerated.setOnClickListener(view->onClick(Action.SET_GENERATED));
        buttonShowChildren.setOnClickListener(view->onClick(Action.SHOW_CHILDREN));
        buttonStatistics.setOnClickListener(view->onClick(Action.SHOW_STATS));
        buttonAddList.setOnClickListener(view->onClick(Action.ADD_LIST));
    }
    private void setUserInterface(Item item){
        log("...setUserInterface(Item)", item.getHeading());
        if( item.hasChild()){
            buttonShowChildren.setVisibility(View.VISIBLE);
        }
        if( item.isTemplate()){
            buttonStatistics.setVisibility(View.VISIBLE);
        }

    }
    private void onClick(Action action){
        callback.onClick(action);
        dismiss();
    }
}
