package se.curtrune.lucy.dialogs;

import static se.curtrune.lucy.util.Logger.log;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import se.curtrune.lucy.R;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.State;
import se.curtrune.lucy.classes.Type;
import se.curtrune.lucy.util.Settings;


public class AddItemDialog extends BottomSheetDialogFragment {
    private EditText editText_heading;
    private Spinner spinnerType;
    private String heading;
    private Button buttonSave;
    private String category;
    public interface Callback{
        //void onAddItem(String text);
        void onAddItem(Item item);
    }

    public AddItemDialog(){
        log("AddItemFragment default constructor");
        this.heading = "";
    }
    public AddItemDialog(String heading, String category) {
        this.category = category;
        this.heading = heading;
    }

    private Callback listener;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        log("AddItemDialog.onCreateView(...)");
        View view = inflater.inflate(R.layout.add_item_dialog, container, false);
        if( savedInstanceState != null) {
            heading = savedInstanceState.getString("heading");
        }else{
            log("...savedInstance is null");
        }
        Switch switchDone = view.findViewById(R.id.addItem_state);
        spinnerType = view.findViewById(R.id.addItem_spinnerType);
        editText_heading = view.findViewById(R.id.addPeriod_heading);
        if( heading.isEmpty()){
            editText_heading.setHint("heading here please");
        }else {
            editText_heading.setText(heading);
        }
        buttonSave = view.findViewById(R.id.addItem_save);
        initSpinnerCategories();
        buttonSave.setOnClickListener(view1 -> {
            Item item = new Item();
            item.setHeading(editText_heading.getText().toString());
            item.setState(switchDone.isChecked()? State.DONE: State.TODO);
            //item.setType(currentType);
            item.setCategory(category);
            listener.onAddItem(item);
            dismiss();
        });
        return view;
    }
    private void initSpinnerCategories(){
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, Settings.getCategories(getContext()));
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
            spinnerType.setAdapter(arrayAdapter);
            spinnerType.setSelection(0);
            spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    category = (String) spinnerType.getSelectedItem();
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.listener = (Callback) context;
    }

    public void setHeading(String heading){
        this.heading = heading;
    }
    public void setCategory(String category){
        this.category = category;
    }
}
