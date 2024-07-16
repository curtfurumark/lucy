package se.curtrune.lucy.dialogs;

import static se.curtrune.lucy.util.Logger.log;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import se.curtrune.lucy.R;

public class PostponeDialog extends DialogFragment {
    private Button buttonOK;
    private Button buttonDismiss;
    private RadioButton radioButtonOneHour;
    private RadioButton radioButtonOneDay;
    private RadioButton radioButtonOneWeek;
    private RadioButton radioButtonOneMonth;
    public enum Postpone{
        ONE_HOUR, ONE_DAY, ONE_WEEK, ONE_MONTH
    }

    public interface Callback{
        void postpone(Postpone postpone);
        void dismiss();
    }
    private Callback callback;
    public static boolean VERBOSE = false;
    public PostponeDialog(){
        log("PostponeDialog()");
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        log("...onCreateView(...)");
        View view = inflater.inflate(R.layout.postpone_dialog, container);
        initComponents(view);
        initListeners();
        //setUserInterface();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        log("PoneDialog.onCreate(Bundle)");
        //setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }
    public void initComponents(View view){
        if( VERBOSE) log("...initComponents()");
        buttonOK = view.findViewById(R.id.postponeDialog_buttonOK);
        buttonDismiss = view.findViewById(R.id.postponeDialog_buttonDismiss);
        radioButtonOneHour = view.findViewById(R.id.postponeDialog_oneHour);
        radioButtonOneDay   = view.findViewById(R.id.postponeDialog_oneDay);
        radioButtonOneWeek = view.findViewById(R.id.postponeDialog_oneWeek);
        radioButtonOneMonth = view.findViewById(R.id.postponeDialog_oneMonth);
    }
    private void initListeners(){
        if( VERBOSE) log("...initListeners()");
        buttonOK.setOnClickListener(view->postpone());
        buttonDismiss.setOnClickListener(view->{
            callback.dismiss();
            dismiss();
        });
    }
    private void postpone(){
        log("...postpone()");
        Postpone postpone = Postpone.ONE_DAY;
        if (radioButtonOneHour.isChecked()){
            postpone = Postpone.ONE_HOUR;
        }else if( radioButtonOneWeek.isChecked()){
            postpone = Postpone.ONE_WEEK;
        }else if( radioButtonOneMonth.isChecked()){
            postpone = Postpone.ONE_MONTH;
        }
        callback.postpone(postpone);
        dismiss();

    }
    public void setCallback(Callback callback){
        this.callback = callback;
    }

}
