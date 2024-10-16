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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import se.curtrune.lucy.R;
import se.curtrune.lucy.classes.Repeat;


public class RepeatDialog extends BottomSheetDialogFragment {
    private TextView textViewEveryDay;
    private TextView textViewEveryWeek;
    private TextView textViewEveryMonth;
    private TextView textViewEveryYear;
    private TextView textViewCustom;
    private EditText editTextQualifier;
    private LinearLayout layoutSimple;
    private LinearLayout layoutCustom;
    private String dayWeekMonthYear;
    private Spinner spinnerPeriods;
    ArrayAdapter<CharSequence> adapter;
    private Button buttonOK;
    private Button buttonDismiss;

    public static boolean VERBOSE = false;
    private enum Mode{
        SIMPLE, CUSTOM
    }
    private Mode mode;

    public interface Callback{
        void onRepeat(Repeat repeat);
    }

    private Callback listener;
    public RepeatDialog(){
        if( VERBOSE) log("RepeatDialog constructor");
        mode = Mode.SIMPLE;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        log("RepeatDialog.onCreateView(...)");
        View view = inflater.inflate(R.layout.repeat_dialog, container, false);
        initComponents(view);
        initListeners();
        initSpinner();
        return view;
    }
    private void getPeriod(){
        log("...getUnit()");
        if( mode.equals(Mode.CUSTOM)){
            Repeat repeat = new Repeat();
            String stringQualifier = editTextQualifier.getText().toString();
            int qualifier = Integer.parseInt(stringQualifier);
            log("...qualifier", qualifier);
            log("...dayWeekYearMonth", dayWeekMonthYear);
            repeat.setPeriod(qualifier, Repeat.Unit.valueOf(dayWeekMonthYear.toUpperCase()) );
            listener.onRepeat(repeat);
            dismiss();
        }else{
            log("STRANGE, ");
        }
    }

    private void initComponents(View view){
        if( VERBOSE) log("...initComponents(View)");
        textViewEveryDay = view.findViewById(R.id.repeatDialog_everyDay);
        textViewEveryWeek = view.findViewById(R.id.repeatDialog_everyWeek);
        textViewEveryMonth = view.findViewById(R.id.repeatDialog_everyMonth);
        textViewEveryYear = view.findViewById(R.id.repeatDialog_everyYear);
        textViewCustom = view.findViewById(R.id.repeatDialog_custom);
        buttonDismiss = view.findViewById(R.id.repeatDialog_buttonDismiss);
        buttonOK = view.findViewById(R.id.repeatDialog_buttonOK);
        layoutCustom = view.findViewById(R.id.repeatDialog_layoutCustom);
        layoutSimple = view.findViewById(R.id.repeatDialog_layoutSimple);
        spinnerPeriods = view.findViewById(R.id.repeatDialog_spinnerPeriod);
        editTextQualifier = view.findViewById(R.id.repeatDialog_qualifier);
    }
    private void initListeners(){
        if( VERBOSE) log("...initListeners()");
        textViewEveryMonth.setOnClickListener(view->onRepeat(Repeat.Unit.MONTH));
        textViewEveryWeek.setOnClickListener(view->onRepeat(Repeat.Unit.WEEK));
        textViewEveryDay.setOnClickListener(view->onRepeat(Repeat.Unit.DAY));
        textViewEveryYear.setOnClickListener(view->onRepeat(Repeat.Unit.YEAR));
        textViewCustom.setOnClickListener(view->toggleCustom());
        buttonDismiss.setOnClickListener(view->dismiss());
        buttonOK.setOnClickListener(view->getPeriod());
    }
    private void initSpinner(){
        if( VERBOSE) log("...initSpinner");
        adapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.periods,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPeriods.setAdapter(adapter);
        spinnerPeriods.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                log("...spinner.onItemClick(...)");
                dayWeekMonthYear = adapter.getItem(position).toString();
                //log("...dayWeekMonthYear", dayWeekMonthYear);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerPeriods.setSelection(0);
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }
    private void onRepeat(Repeat.Unit unit){
        Repeat repeat = new Repeat();
        repeat.setPeriod(1, unit);
        listener.onRepeat(repeat);
        dismiss();
    }
    public void setCallback(Callback callback){
        this.listener = callback;
    }
    private void toggleCustom(){
        log("...toggleCustom()");
        if( layoutSimple.getVisibility() == View.VISIBLE){
            layoutSimple.setVisibility(View.GONE);
            layoutCustom.setVisibility(View.VISIBLE);
            mode = Mode.CUSTOM;
        }else{
            layoutCustom.setVisibility(View.GONE);
            layoutSimple.setVisibility(View.VISIBLE);
            mode = Mode.SIMPLE;
        }
    }

}
