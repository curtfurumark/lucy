package se.curtrune.lucy.dialogs;

import static se.curtrune.lucy.util.Logger.log;

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
import androidx.fragment.app.DialogFragment;

import se.curtrune.lucy.R;

public class PasswordDialog extends DialogFragment {
    private Button buttonOK;
    private Button buttonDismiss;
    private EditText editTextPassword;

    public interface Callback{
        void onPassword(String str);
    }
    private Callback callback;
    public static boolean VERBOSE = false;
    public PasswordDialog(){
        log("PasswordDialog()");
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        log("...onCreateView(...)");
        View view = inflater.inflate(R.layout.password_dialog, container);
        initComponents(view);
        initListeners();
        //setUserInterface();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        log("BoostDialog.onCreate(Bundle)");
        setStyle(DialogFragment.STYLE_NORMAL,
                android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }
    public void initComponents(View view){
        log("...initComponents()");
        buttonOK = view.findViewById(R.id.passwordDialog_buttonOK);
        buttonDismiss = view.findViewById(R.id.passwordDialog_buttonDismiss);
        editTextPassword = view.findViewById(R.id.passwordDialog_password);
    }
    private void initListeners(){
        log("...initListeners()");
        buttonOK.setOnClickListener(view->onPassword());
        buttonDismiss.setOnClickListener(view->dismiss());

    }
    private void onPassword(){
        log("...onPassword()");
        if( !validateInput()){
            log("...too short password, i surrender");
            return;
        }
        callback.onPassword(editTextPassword.getText().toString());
        dismiss();
    }
    public void setCallback(Callback callback){
        this.callback = callback;
    }

    /**
     * at least 8 characters
     * @return
     */
    private boolean validateInput(){
        if( editTextPassword.getText().toString().length() < 8){
            Toast.makeText(getContext(), "password needs to be at least 8 characters long", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

}
