package se.curtrune.lucy.dialogs;

import static se.curtrune.lucy.util.Logger.log;

import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;

import java.time.DayOfWeek;
import java.time.LocalTime;

import se.curtrune.lucy.R;
import se.curtrune.lucy.classes.Period;
import se.curtrune.lucy.util.Converter;


public class PeriodDialog extends BottomSheetDialogFragment {
    private EditText editTextDays;
    private Button buttonSave;
    private Chip chipMonday;
    private Chip chipTuesday;
    private Chip chipWednesday;
    private Chip chipThursday;
    private Chip chipFriday;
    private Chip chipSaturday;
    private Chip chipSunday;
    private Switch switchDays;
    private LocalTime time;
    private TextView textViewTime;

    public interface Callback{
        void onPeriod(Period period);
    }

    private Callback listener;
    private Period period;
    private enum EditMode{
        CREATE, EDIT
    }

    private Period.Mode periodMode = Period.Mode.DAY_OF_WEEKS;
    public PeriodDialog(){
        log("AddItemFragment default constructor");
        period = new Period();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        log("AddItemDialog.onCreateView(...)");
        View view = inflater.inflate(R.layout.period_dialog, container, false);
        initComponents(view);
        initDefaults();
        initListeners();
        setUserInterface();
        return view;
    }
    private Period getPeriod(){
        log("...getPeriod()");
        if( periodMode.equals(Period.Mode.DAYS)) {
            if (editTextDays.getText().toString().isEmpty()){
                Toast.makeText(getContext(), "missing value days", Toast.LENGTH_LONG).show();
            }else{
                period.setDays(Integer.parseInt(editTextDays.getText().toString()));
            }
        }
        return period;

    }
    private void initComponents(View view){
        log("...initComponents(View)");
        editTextDays = view.findViewById(R.id.periodDialog_days);
        buttonSave = view.findViewById(R.id.periodDialog_save);
        chipMonday = view.findViewById(R.id.periodDialog_monday);
        chipTuesday = view.findViewById(R.id.periodDialog_tuesday);
        chipWednesday = view.findViewById(R.id.periodDialog_wednesday);
        chipThursday = view.findViewById(R.id.periodDialog_thursday);
        chipFriday = view.findViewById(R.id.periodDialog_friday);
        chipSaturday = view.findViewById(R.id.periodDialog_saturday);
        chipSunday = view.findViewById(R.id.periodDialog_sunday);
        switchDays = view.findViewById(R.id.periodDialog_switchDays);
        textViewTime = view.findViewById(R.id.periodDialog_time);
    }
    private void initDefaults(){
        log("...initDefaults()");
        time = LocalTime.now();
    }
    private void initListeners(){
        log("...initListeners()");
        buttonSave.setOnClickListener(view1 -> {
            Period period = getPeriod();
            listener.onPeriod(period);
            dismiss();
        });
        textViewTime.setOnClickListener(view->showTimeDialog());
        chipMonday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                log("...onCheckedChanged()");
                setDayOfWeek(DayOfWeek.MONDAY, isChecked);
            }
        });
        chipTuesday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                log("...onCheckedChanged()");
                setDayOfWeek(DayOfWeek.TUESDAY, isChecked);
            }
        });
        chipWednesday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                log("...onCheckedChanged()");
                setDayOfWeek(DayOfWeek.WEDNESDAY, isChecked);
            }
        });
        chipThursday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                log("...onCheckedChanged()");
                setDayOfWeek(DayOfWeek.THURSDAY, isChecked);
            }
        });
        chipFriday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                log("...onCheckedChanged()");
                setDayOfWeek(DayOfWeek.FRIDAY, isChecked);
            }
        });
        chipSaturday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                log("...onCheckedChanged()");
                setDayOfWeek(DayOfWeek.SATURDAY, isChecked);
            }
        });
        chipSunday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                log("...onCheckedChanged()");
                setDayOfWeek(DayOfWeek.SUNDAY, isChecked);
            }
        });
        switchDays.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if( isChecked){
                    periodMode = Period.Mode.DAYS;
                }else{
                    periodMode = Period.Mode.DAY_OF_WEEKS;
                }
            }
        });

    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        //this.listener = (Callback) context;
    }
    private void setDayOfWeek(DayOfWeek dayOfWeek, boolean checked){
        log("...setDayOfWeek(DayOfWeek, boolean)", dayOfWeek.toString());
        if( checked){
            period.add(dayOfWeek);
        }else{
            period.remove(dayOfWeek);
        }
    }
    public void setListener(Callback callback){
        this.listener = callback;
    }
    private void setUserInterface(){
        log("...setUserInterface()");
        textViewTime.setText(Converter.format(time));

    }

    private void showTimeDialog() {
        log("...showTimeDialog()");
        log("...showTimeDialog");
        int minutes = LocalTime.now().getMinute();
        int hour = LocalTime.now().getHour();
        TimePickerDialog timePicker = new TimePickerDialog(getContext(), (view, hourOfDay, minute) -> {
            time = LocalTime.of(hourOfDay, minute);
            textViewTime.setText(time.toString());
        }, hour, minutes, true);
        timePicker.show();
    }
}
