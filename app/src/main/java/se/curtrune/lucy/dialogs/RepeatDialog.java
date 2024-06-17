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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.time.Duration;

import se.curtrune.lucy.R;
import se.curtrune.lucy.classes.Repeat;


public class RepeatDialog extends BottomSheetDialogFragment {
    private TextView textViewEveryDay;
    private TextView textViewEveryWeek;
    private TextView textViewEveryMonth;
    private TextView textViewEveryYear;
    private TextView textViewCustom;

    public static boolean VERBOSE = false;

    public interface Callback{
        void onRepeat(Repeat.Period period);
    }

    private Callback listener;
    public RepeatDialog(){

        if( VERBOSE) log("RepeatDialog constructor");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        log("RepeatDialog.onCreateView(...)");
        View view = inflater.inflate(R.layout.repeat_dialog, container, false);
        initComponents(view);
        initListeners();
        return view;
    }

    private void initComponents(View view){
        if( VERBOSE) log("...initComponents(View)");
        textViewEveryDay = view.findViewById(R.id.repeatDialog_everyDay);
        textViewEveryWeek = view.findViewById(R.id.repeatDialog_everyWeek);
        textViewEveryMonth = view.findViewById(R.id.repeatDialog_everyMonth);
        textViewEveryYear = view.findViewById(R.id.repeatDialog_everyYear);
        textViewCustom = view.findViewById(R.id.repeatDialog_custom);
    }
    private void initListeners(){
        if( VERBOSE) log("...initListeners()");
        textViewEveryMonth.setOnClickListener(view->onRepeat(Repeat.Period.MONTH));
        textViewEveryWeek.setOnClickListener(view->onRepeat(Repeat.Period.WEEK));
        textViewEveryDay.setOnClickListener(view->onRepeat(Repeat.Period.DAY));
        textViewEveryYear.setOnClickListener(view->onRepeat(Repeat.Period.YEAR));
        textViewCustom.setOnClickListener(view->onRepeat(Repeat.Period.CUSTOM));
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }
    private void onRepeat(Repeat.Period period){
        listener.onRepeat(period);
        dismiss();
    }
    public void setCallback(Callback callback){
        this.listener = callback;
    }

}
