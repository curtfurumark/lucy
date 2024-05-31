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

import java.time.Duration;
import java.time.LocalDateTime;

import se.curtrune.lucy.R;
import se.curtrune.lucy.classes.Message;


public class MessageDialog extends BottomSheetDialogFragment {
    private EditText editTextSubject;
    private EditText editTextContent;
    private EditText editTextUser;
    private Button buttonSave;
    private Button buttonDismiss;
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
        return message;

    }
    private void initComponents(View view){
        if( VERBOSE) log("...initComponents(View)");
        editTextSubject = view.findViewById(R.id.messageDialog_subject);
        editTextContent = view.findViewById(R.id.messageDialog_content);
        editTextUser = view.findViewById(R.id.messageDialog_user);
        buttonSave = view.findViewById(R.id.messageDialog_publish);
        buttonDismiss = view.findViewById(R.id.durationDialog_dismiss);
    }
    private void initListeners(){
        if( VERBOSE) log("...initListeners()");
        buttonSave.setOnClickListener(view1 -> {
            Message message = getMessage();
            listener.onNewMessage(message);
            dismiss();
        });
        //buttonDismiss.setOnClickListener(view->dismiss());
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }
    public void setCallback(Callback callback){
        this.listener = callback;
    }

}
