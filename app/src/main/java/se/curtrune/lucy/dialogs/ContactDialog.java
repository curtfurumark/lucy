package se.curtrune.lucy.dialogs;

import static se.curtrune.lucy.util.Logger.log;

import android.os.Bundle;
import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import se.curtrune.lucy.R;
import se.curtrune.lucy.classes.Contact;

public class ContactDialog extends BottomSheetDialogFragment {
    private Contact contact;
    private EditText editTextName;
    private EditText editTextPhoneNumber;
    private EditText editTextEmail;
    private EditText editTextAddress;
    private Button buttonDismiss;
    private Button buttonSave;
    public interface Callback{
        void onSave(Contact contact);
    }
    private Callback callback;
    public ContactDialog(Callback callback){
        this.callback = callback;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contact_dialog, container, false);
        initViews(view);
        initListeners();
        return view;
    }
    private Contact getContact(){
        log("...getContact()");
        contact = new Contact();
        contact.setDisplayName(editTextName.getText().toString());
        contact.setEmail(editTextEmail.getText().toString());
        contact.setPhoneNumber(editTextPhoneNumber.getText().toString());
        return contact;
    }
    private void initListeners(){
        log("...initListeners()");
        buttonDismiss.setOnClickListener(view->dismiss());
        buttonSave.setOnClickListener(view->onSave());
    }
    private void initViews(View view){
        log("...initViews(View)");
        editTextName = view.findViewById(R.id.contactDialog_displayName);
        editTextPhoneNumber= view.findViewById(R.id.contactDialog_phoneNumber);
        editTextEmail = view.findViewById(R.id.contactDialog_email);
        editTextAddress = view.findViewById(R.id.contactDialog_address);
        buttonDismiss = view.findViewById(R.id.contactDialog_buttonDismiss);
        buttonSave = view.findViewById(R.id.contactDialog_buttonSave);
    }
    private void onSave(){
        log("...onSave()");
        if( !validate()){
            return;
        }
        contact = getContact();
        callback.onSave(contact);
        dismiss();
    }
    private boolean validate(){
        log("...validate()");
        if( editTextName.getText().toString().isEmpty()){
            Toast.makeText(getContext(), "name is required", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}
