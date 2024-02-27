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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import se.curtrune.lucy.R;
import se.curtrune.lucy.classes.Categories;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.State;
import se.curtrune.lucy.persist.LocalDB;
import se.curtrune.lucy.workers.CategoryWorker;


public class AddItemDialog extends BottomSheetDialogFragment {
    private EditText editText_heading;
    private TextView textViewParentItem;
    private Spinner spinnerCategory;
    private Spinner spinnerState;
    private State state;
    private String heading;
    private Button buttonSave;
    private Button buttonDismiss;
    private Button buttonEdit;
    private String category;
    private Categories categories;

    private Item parent;
    public interface Callback{
        void onAddItem(Item item);
    }

    private Callback listener;

    public AddItemDialog(Item parent) {
        log("AddItemDialog(Item parent)");
        if( parent == null){
            log("...item parent is null, this is a project");
        }
        this.parent = parent;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        log("AddItemDialog.onCreateView(...)");
        View view = inflater.inflate(R.layout.add_item_dialog, container, false);
        initComponents(view);
        initListeners();
        initDefaults();
        editText_heading.setText(heading);
        initSpinnerCategories();
        initSpinnerState();
        initUserInterface(parent);

        return view;
    }
    private void startEditor(Item item){
        log("...startEditor(Item)");
    }
    private String getCategory(){
        return (String) spinnerCategory.getSelectedItem();
    }
    private Item getItem(){
        log("...getItem()");
        Item item = new Item();
        item.setHeading(editText_heading.getText().toString());
        if( parent != null) {
            log("...parent is not null");
            item.setTags(parent.getTags());
            item.setParentId(parent.getID());
        }else{
            log("...parent is null, project");
            item.setParentId(0);

        }
        item.setCategory(getCategory());
        item.setState(state);
        return item;

    }
    private void initComponents(View view){
        log("...initComponents(View)");
        spinnerCategory = view.findViewById(R.id.addItem_spinnerType);
        spinnerState = view.findViewById(R.id.addItemDialog_spinnerState);
        editText_heading = view.findViewById(R.id.periodDialog_days);
        buttonSave = view.findViewById(R.id.periodDialog_save);
        buttonDismiss = view.findViewById(R.id.addItemDialog_dismiss);
        buttonEdit = view.findViewById(R.id.addItemDialog_edit);
        textViewParentItem = view.findViewById(R.id.addItem_parentItem);
    }
    private void initDefaults(){
        log("...initDefaults()");
        state = State.PENDING;
        categories = new Categories(CategoryWorker.getCategories(getContext()));
    }
    private void initListeners(){
        log("...initListeners()");
        buttonSave.setOnClickListener(view1 -> {
            log("...saveItem()");
            Item item = getItem();
            log(item);
            listener.onAddItem(item);
            dismiss();
        });
        buttonDismiss.setOnClickListener(view->dismiss());
        buttonEdit.setOnClickListener(view->Toast.makeText(getContext(), "not implemented", Toast.LENGTH_LONG).show());

    }

    private void initSpinnerCategories() {
        log("...initSpinnerCategories()");
        LocalDB db = new LocalDB(getContext());
        String[] categories = db.getCategories();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, categories);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spinnerCategory.setAdapter(arrayAdapter);
        spinnerCategory.setSelection(0);
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = (String) spinnerCategory.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void initSpinnerState(){
        log("...initSpinnerState()");
        ArrayAdapter<State> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, State.values());
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spinnerState.setAdapter(arrayAdapter);
        spinnerState.setSelection(state.ordinal());
        spinnerState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                state = (State) spinnerState.getSelectedItem();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    public void initUserInterface(Item parentItem){
        log("...initUserInterface()");
        if( parentItem != null) {
            editText_heading.setText(parentItem.getHeading());
            spinnerState.setSelection(parentItem.getState().ordinal());
            spinnerCategory.setSelection(categories.getIndex(parentItem.getCategory()));
            textViewParentItem.setText(parentItem.getHeading());
        }else{
            textViewParentItem.setText("no parent, no good");
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.listener = (Callback) context;
    }

    public void setState(State state){
        log("AddItemDialog.setState(State) ", state.toString());
        this.state = state;
    }
    public void setHeading(String heading){
        this.heading = heading;
    }
    public void setCategory(String category){
        this.category = category;
    }
}
