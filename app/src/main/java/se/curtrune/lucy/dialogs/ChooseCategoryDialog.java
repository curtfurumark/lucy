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
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import se.curtrune.lucy.R;
import se.curtrune.lucy.persist.LocalDB;


public class ChooseCategoryDialog extends DialogFragment {
    private Button buttonOK;

    private String currentCategory;
    public interface Callback{
        void onSelected(String category);
    }
    private Callback callback;
    private Spinner spinnerCategory;
    public ChooseCategoryDialog(String category){
        log("....ChoseCategoryDialog(String), category", category);
        this.currentCategory = category;

    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_category_dialog, container);
        initComponents(view);
        initSpinnerCategories();
        initListeners();
        return view;
    }
    private void initComponents(View view){
        spinnerCategory = view.findViewById(R.id.chooseCategoryDialog_spinnerCategories);
        buttonOK = view.findViewById(R.id.chooseCategoryDialog_buttonOK);
    }
    private void initListeners(){
        buttonOK.setOnClickListener(view->{
                callback.onSelected(currentCategory);
                dismiss();
            });
    }

    private void initSpinnerCategories() {
        log("...initSpinnerCategories()");
        LocalDB db = new LocalDB(getContext());
        String[] categories = db.getCategories();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, categories);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spinnerCategory.setAdapter(arrayAdapter);
        spinnerCategory.setSelection(0);
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentCategory = (String) spinnerCategory.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });    //private void setSpinnerSelection(String category){
            //if( VERBOSE) log("...setSpinnerSelection(String)", category);
            spinnerCategory.setSelection(arrayAdapter.getPosition(currentCategory));
        //}
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }
}
