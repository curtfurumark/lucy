package se.curtrune.lucy.dialogs;

import static se.curtrune.lucy.util.Logger.log;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import se.curtrune.lucy.R;

public class BoostDialog extends DialogFragment {
    private String message;
    private Button buttonOK;
    private TextView textViewMessage;
    public static boolean VERBOSE = false;
    public BoostDialog(String message){
        this.message = message;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        log("...onCreateView(...)");
        View view = inflater.inflate(R.layout.boost_me_layout, container);
        initComponents(view);
        initListeners();
        setUserInterface();
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
        if( VERBOSE) log("...initComponents()");
        buttonOK = view.findViewById(R.id.boostMe_buttonOK);
        textViewMessage = view.findViewById(R.id.boostMe_message);
    }
    private void initListeners(){
        if( VERBOSE)log("...initListeners()");
        buttonOK.setOnClickListener(view->dismiss());

    }
    private void setUserInterface(){
        textViewMessage.setText(message);
    }

}
