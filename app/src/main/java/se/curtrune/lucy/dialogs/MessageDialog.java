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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.time.LocalDateTime;

import se.curtrune.lucy.R;
import se.curtrune.lucy.app.Settings;
import se.curtrune.lucy.app.User;
import se.curtrune.lucy.classes.Message;


public class MessageDialog extends BottomSheetDialogFragment {
    private EditText editTextSubject;
    private EditText editTextContent;
    private EditText editTextUser;
    private Button buttonSave;
    private Button buttonDismiss;
    private String category;
    private Message message;
    public enum Mode{
        CREATE, EDIT
    }
    private Mode mode = Mode.CREATE;
    private Spinner spinnerCategories;
    private ArrayAdapter<String> categoryAdapter;
    public static boolean VERBOSE = false;

    public interface Callback{
        void onMessage(Message message, Mode mode);
    }

    private Callback listener;

    public MessageDialog(){
        if( VERBOSE) log("MessageDialog constructor");
        message = new Message();
        mode = Mode.CREATE;
    }
    public MessageDialog(Message message){
        this.message = message;
        mode = Mode.EDIT;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        log("MessageDialog.onCreateView(...)");
        View view = inflater.inflate(R.layout.message_dialog, container, false);
        initComponents(view);
        initSpinner();
        initListeners();
        setUserInterface();
        return view;
    }
    private Message getMessage(){
        if( VERBOSE) log("...getMessage()");
        if(mode.equals(Mode.CREATE)) {
            Message message = new Message();
            message.setCreated(LocalDateTime.now());
        }
        message.setUser(editTextUser.getText().toString());
        message.setContent(editTextContent.getText().toString());
        message.setSubject(editTextSubject.getText().toString());
        message.setCategory(category);
        return message;

    }
    private void initComponents(View view){
        editTextSubject = view.findViewById(R.id.messageDialog_subject);
        editTextContent = view.findViewById(R.id.messageDialog_content);
        editTextUser = view.findViewById(R.id.messageDialog_user);
        buttonSave = view.findViewById(R.id.messageDialog_publish);
        buttonDismiss = view.findViewById(R.id.messageDialog_dismiss);
        spinnerCategories = view.findViewById(R.id.messageDialog_spinnerCategories);
    }
    private void initListeners(){
        if( VERBOSE) log("...initListeners()");
        buttonSave.setOnClickListener(view1 -> {
            Message message = getMessage();
            listener.onMessage(message, mode);
            dismiss();
        });
        buttonDismiss.setOnClickListener(view->dismiss());
    }
    private void initSpinner(){
        log("...initSpinner()");
        String[] messageCategories = getContext().getResources().getStringArray(R.array.message_categories);
        categoryAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item,messageCategories );
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spinnerCategories.setAdapter(categoryAdapter);
        spinnerCategories.setSelection(0);
        spinnerCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                log("...onItemSelected(...)", categoryAdapter.getItem(position).toString());
                category = categoryAdapter.getItem(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    private void setUserInterface(){
        log("...setUserInterface()");
        if(mode.equals(Mode.EDIT)){
            editTextUser.setText(message.getUser());
            editTextContent.setText(message.getContent());
            editTextSubject.setText(message.getSubject());
            int position = categoryAdapter.getPosition(message.getCategory());
            spinnerCategories.setSelection(position);
            buttonSave.setText(getString(R.string.update));
        }
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }
    public void setCallback(Callback callback){
        this.listener = callback;
    }

}
