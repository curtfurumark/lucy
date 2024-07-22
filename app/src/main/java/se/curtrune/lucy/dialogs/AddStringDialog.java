package se.curtrune.lucy.dialogs;

import static se.curtrune.lucy.util.Logger.log;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import se.curtrune.lucy.R;


public class AddStringDialog extends BottomSheetDialogFragment {

    private Button buttonSave;
    private Button buttonDismiss;
    private TextView textViewCategory;
    public interface Callback{
        void onNewString(String category);
    }

    public AddStringDialog(){
        log("AddCategory default constructor");
    }

    private Callback listener;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        log("AddItemFragment.onCreateView(...)");
        View view = inflater.inflate(R.layout.add_category_dialog, container, false);

        textViewCategory = view.findViewById(R.id.addCategoryDialog_category);
        buttonSave = view.findViewById(R.id.addCategoryDialog_save);
        buttonDismiss = view.findViewById(R.id.addCategoryDialog_dismiss);
        buttonDismiss.setOnClickListener(v->dismiss());
        buttonSave.setOnClickListener(view1 -> {
            if( validateInput()) {
                listener.onNewString(textViewCategory.getText().toString());
            }
            dismiss();
        });
        return view;
    }
    public void setListener(Callback callback){
        this.listener = callback;
    }
    private boolean validateInput(){
        if( textViewCategory.getText().toString().isEmpty()){
            Toast.makeText(getContext(), "missing category", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }
}
