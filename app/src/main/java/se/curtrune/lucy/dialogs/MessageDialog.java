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
    private Spinner spinnerCategories;
    public static boolean VERBOSE = false;

    public interface Callback{
        void onNewMessage(Message message);
    }

    private Callback listener;

    public MessageDialog(){
        if( VERBOSE) log("MessageDialog constructor");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        log("MessageDialog.onCreateView(...)");
        View view = inflater.inflate(R.layout.message_dialog, container, false);
        initComponents(view);
        initSpinner();
        initListeners();
        return view;
    }
    private Message getMessage(){
        if( VERBOSE) log("...getMessage()");
        Message message = new Message();
        message.setUser(editTextUser.getText().toString());
        message.setContent(editTextContent.getText().toString());
        message.setSubject(editTextSubject.getText().toString());
        message.setCreated(LocalDateTime.now());
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
            listener.onNewMessage(message);
            dismiss();
        });
        buttonDismiss.setOnClickListener(view->dismiss());
    }
    private void initSpinner(){
        log("...initSpinner()");
        String[] messageCategories = getContext().getResources().getStringArray(R.array.message_categories);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item,messageCategories );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spinnerCategories.setAdapter(adapter);
        spinnerCategories.setSelection(0);
        spinnerCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                log("...onItemSelected(...)", adapter.getItem(position).toString());
                category = adapter.getItem(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }
    public void setCallback(Callback callback){
        this.listener = callback;
    }

}
