package se.curtrune.lucy.dialogs;

import static se.curtrune.lucy.util.Logger.log;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import se.curtrune.lucy.R;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.State;
import se.curtrune.lucy.workers.UtilWorker;

public class AddTemplateDialog extends BottomSheetDialogFragment {
    private EditText editTextHeading;
    private EditText editTextDays;
    private TextView textViewParent;
    private Spinner spinner;
    private Button buttonSave;
    private ArrayAdapter<String> arrayAdapter;
    private Item parent;

    private OnNewItemCallback callback;
    private String category;


    public AddTemplateDialog(Item parent) {
        this.parent = parent;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_template_dialog, container, false);
        initComponents(view);
        initListeners();
        initSpinner();
        initUserInterface();
        return view;
    }
    private Item getItem(){
        Item item = new Item();
        item.setHeading(editTextHeading.getText().toString());
        item.setState(State.INFINITE);
        item.setDays(Integer.parseInt(editTextDays.getText().toString()));
        item.setParent(parent);
        return item;
    }

    private void initComponents(View view){
        log("...initComponents(View view");
        editTextHeading = view.findViewById(R.id.addTemplateDialog_heading);
        buttonSave = view.findViewById(R.id.addTemplateDialog_button);
        editTextDays = view.findViewById(R.id.addTemplateDialog_days);
        textViewParent = view.findViewById(R.id.addTemplateDialog_parent);
        spinner = view.findViewById(R.id.addTemplateDialog_spinner);
    }

    private void initListeners(){
        log("...initListeners()");
        buttonSave.setOnClickListener(view->save());
    }

    /**
     * sets default category to the first in the category array
     */
    private void initSpinner(){
        log("...initSpinner()");
        String[] categories = UtilWorker.getCategories(getContext());
        arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, categories);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spinner.setAdapter(arrayAdapter);
        spinner.setSelection(0);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                log("...onItemSelected()");
                category = (String) spinner.getSelectedItem();
                log("...category selected", category);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    private void initUserInterface(){
        log("...initUserInterface()");
        textViewParent.setText(parent.getHeading());
        setSpinnerSelection(parent.getCategory());
    }
    private void save(){
        log("...save()");
        if( !validateInput()){
            log("...missing input");
            return;
        }
        callback.onNewItem(getItem());
        dismiss();
    }
    public void setCallback(OnNewItemCallback callback) {
        this.callback = callback;
    }
    private void setSpinnerSelection(String category){
        log("..setSpinnerSelection(String)", category);
        spinner.setSelection(arrayAdapter.getPosition(category));

    }
    public boolean validateInput(){
        log("...validateInput()");
        if( editTextHeading.getText().toString().isEmpty()){
            Toast.makeText(getContext(), "missing heading", Toast.LENGTH_LONG).show();
            return false;
        }
        if( editTextDays.getText().toString().isEmpty()){
            Toast.makeText(getContext(), "missing days", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}
