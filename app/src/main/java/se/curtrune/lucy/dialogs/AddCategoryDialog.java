package se.curtrune.lucy.dialogs;

import static se.curtrune.lucy.util.Logger.log;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import se.curtrune.lucy.R;
import se.curtrune.lucy.classes.Type;


public class AddCategoryDialog extends BottomSheetDialogFragment {
    private EditText editTextParentID;
    private long parentID;

    private Button buttonSave;
    private Type currentType = Type.PENDING;
    public interface Callback{
        void onUpdateParentID(long parentID);
    }

    public AddCategoryDialog(){
        log("AddItemFragment default constructor");
    }
    public AddCategoryDialog(long parentID) {
        this.parentID = parentID;
    }

    private Callback listener;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        log("AddItemFragment.onCreateView(...)");
        View view = inflater.inflate(R.layout.add_item_fragment, container, false);

        editTextParentID = view.findViewById(R.id.editParentID_parentID);
        buttonSave = view.findViewById(R.id.editParentID_buttonSave);
        editTextParentID.setText(String.valueOf(parentID));
        buttonSave.setOnClickListener(view1 -> {
            parentID = Long.valueOf(editTextParentID.getText().toString());
            listener.onUpdateParentID(parentID);
            dismiss();
        });
        return view;
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.listener = (Callback) context;
    }
}
