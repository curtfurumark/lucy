package se.curtrune.lucy.dialogs;

import static se.curtrune.lucy.util.Logger.log;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Locale;

import se.curtrune.lucy.R;
import se.curtrune.lucy.classes.Item;

public class ChooseChildTypeDialog extends BottomSheetDialogFragment {
    private RadioButton radioButtonChild;
    private TextView textViewHeading;
    private Item parent;
    private RadioButton radioButtonList;
    private RadioButton radioButtonPhotograph;
    public interface Listener{
        void onClick(ChildType childType);
    }
    private Listener listener;
    public enum ChildType{
        CHILD, LIST, PHOTOGRAPH;
    }
    public ChooseChildTypeDialog(Item parent, Listener listener){
        log("ChooseChildTypeDialog(Item, Listener)");
        this.listener = listener;
        this.parent = parent;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_child_to_item_dialog, container, false);
        initViews(view);
        initListeners();
        setUserInterface();
        return view;
    }
    private void initListeners(){
        log("...initListeners()");
        radioButtonChild.setOnClickListener(view->onClick(ChildType.CHILD));
        radioButtonList.setOnClickListener(view->onClick(ChildType.LIST));
        radioButtonPhotograph.setOnClickListener(view->onClick(ChildType.PHOTOGRAPH));
    }
    private void initViews(View view){
        radioButtonChild = view.findViewById(R.id.addChildToItemDialog_radioButton_child);
        radioButtonList = view.findViewById(R.id.addChildToItemDialog_radioButton_list);
        radioButtonPhotograph = view.findViewById(R.id.addChildToItemDialog_radioButton_photograph);
        textViewHeading = view.findViewById(R.id.chooseChildTypeDialog_heading);
    }
    private void onClick(ChildType type){
        log("...onClick(ChildType)", type.toString());
        listener.onClick(type);
        dismiss();
    }
    private void setUserInterface(){
        log("...setUserInterface()");
        String strHeading = String.format(Locale.getDefault(),"add to item %s", parent.getHeading());
        textViewHeading.setText(strHeading);
    }
}
