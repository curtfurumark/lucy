package se.curtrune.lucy.dialogs;

import static se.curtrune.lucy.util.Logger.log;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import se.curtrune.lucy.R;


public class AddPanicURLDialog extends BottomSheetDialogFragment {

    private EditText editTextURL;
    public interface Callback{
        void onNewPanicURL(String category);
    }

    public AddPanicURLDialog(){
        log("AddString default constructor");
    }

    private Callback listener;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        log("AddPanicURLDialog.onCreateView(...)");
        View view = inflater.inflate(R.layout.add_panic_ur_dialog, container, false);

        editTextURL = view.findViewById(R.id.addPanicURLDialog_url);
        //textViewCategory.setHint(hint);
        Button buttonSave = view.findViewById(R.id.addPanicURLDialog_save);
        Button buttonDismiss = view.findViewById(R.id.addPanicURLDialog_dismiss);
        buttonDismiss.setOnClickListener(v->dismiss());
        buttonSave.setOnClickListener(view1 -> {
            if( validateInput()) {
                listener.onNewPanicURL(editTextURL.getText().toString());
            }
            dismiss();
        });
        return view;
    }
    public void setListener(Callback callback){
        this.listener = callback;
    }
    private boolean validateInput(){
        if( editTextURL.getText().toString().isEmpty()){
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
