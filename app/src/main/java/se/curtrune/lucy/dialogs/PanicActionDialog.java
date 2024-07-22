package se.curtrune.lucy.dialogs;

import static se.curtrune.lucy.util.Logger.log;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import se.curtrune.lucy.R;
import se.curtrune.lucy.app.Settings;

public class PanicActionDialog extends DialogFragment {
    private Button buttonOK;
    private TextView textViewMessage;
    private RadioButton radioButtonICE;
    private RadioButton radioButtonSequence;
    private RadioButton radioButtonURL;
    private RadioButton radioButtonGame;
    private Button buttonDismiss;
    private Settings.PanicAction panicAction;

    public interface Listener{
        void onPanicAction(Settings.PanicAction panicAction);
    }
    private Listener listener;
    public PanicActionDialog() {
        log("PanicActionDialog()");
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        log("...onCreateView(...)");
        View view = inflater.inflate(R.layout.panic_action_dialog, container);
        initComponents(view);
        initListeners();
        setDefaultPanicAction();
        setUserInterface();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        log("PanicActionDialog.onCreate(Bundle)");
        setStyle(DialogFragment.STYLE_NORMAL,
                android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }
    public void initComponents(View view){
        log("...initComponents()");
        buttonOK = view.findViewById(R.id.panicActionDialog_ok);
        buttonDismiss = view.findViewById(R.id.panicActionDialog_dismiss);
        //textViewMessage = view.findViewById(R.id.boostMe_message);
        radioButtonICE = view.findViewById(R.id.panicActionDialog_radioButtonICE);
        radioButtonURL = view.findViewById(R.id.panicActionDialog_radioURL);
        radioButtonSequence = view.findViewById(R.id.panicActionDialog_radioButtonSequence);
        radioButtonGame = view.findViewById(R.id.panicActionDialog_radioButtonGame);
    }
    private void initListeners(){
        log("...initListeners()");
        buttonDismiss.setOnClickListener(view->dismiss());
        buttonOK.setOnClickListener(view-> {
                listener.onPanicAction(panicAction);
                dismiss();
        });
        radioButtonSequence.setOnClickListener(view->onClick(Settings.PanicAction.SEQUENCE));
        radioButtonURL.setOnClickListener(view->onClick(Settings.PanicAction.URL));
        radioButtonGame.setOnClickListener(view->onClick(Settings.PanicAction.GAME));
        radioButtonICE.setOnClickListener(view->onClick(Settings.PanicAction.ICE));
    }
    private void onClick(Settings.PanicAction panicAction){
        log("...onClick(PanicAction)");
        this.panicAction = panicAction;
    }
    public void setListener(Listener listener){
        log("...setListener(Listener)");
        this.listener = listener;
    }
    private void setDefaultPanicAction(){
        log("...setDefaultPanicAction()");
        radioButtonSequence.setChecked(true);
        panicAction = Settings.PanicAction.SEQUENCE;
    }
    private void setUserInterface(){
        log("...setUserInterface()");
        //textViewMessage.setText(message);
    }

}
