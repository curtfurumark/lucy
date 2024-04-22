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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

import se.curtrune.lucy.R;
import se.curtrune.lucy.classes.Repeat;
import se.curtrune.lucy.util.Converter;


public class AddPeriodDialog extends BottomSheetDialogFragment {
    private EditText editTextDays;
    private TextView labelDays;
    private TextView labelWeekDays;
    private TextView textViewFirstDate;
    private TextView textViewLastDate;
    private Button buttonSave;
    private Button buttonDismiss;
    private Chip chipMonday;
    private Chip chipTuesday;
    private Chip chipWednesday;
    private Chip chipThursday;
    private Chip chipFriday;
    private Chip chipSaturday;
    private Chip chipSunday;
    private ConstraintLayout layoutDays;
    private ConstraintLayout layoutWeekDays;
    private LocalTime time;
    private LocalDate firstDate;
    private LocalDate lastDate;
    private TextView textViewTime;

    public interface Callback{
        void onPeriod(Repeat repeat);
    }

    private Callback listener;
    private Repeat repeat;
    private enum Mode{
        DAYS, WEEKDAYS, PENDING
    }
    private Mode mode = Mode.PENDING;

    private Repeat.Mode periodMode = Repeat.Mode.DAY_OF_WEEKS;
    public AddPeriodDialog(){
        log("AddItemFragment default constructor");
        repeat = new Repeat();
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
    private Repeat getPeriod(){
        log("...getPeriod()", mode.toString());
        if( mode.equals(Mode.DAYS)) {
            if (editTextDays.getText().toString().isEmpty()){
                Toast.makeText(getContext(), "missing value days", Toast.LENGTH_LONG).show();
            }else{
                repeat.setDays(Integer.parseInt(editTextDays.getText().toString()));
                //repeat.setTime();
            }
        }
        if( firstDate != null){
            repeat.setFirstDate(firstDate);
        }
        if( lastDate != null){
            repeat.setLastDate(lastDate);
        }
        return repeat;
    }
    private void initComponents(View view){
        log("...initComponents(View)");
        editTextDays = view.findViewById(R.id.periodDialog_days);
        buttonSave = view.findViewById(R.id.periodDialog_save);
        buttonDismiss = view.findViewById(R.id.periodDialog_buttonDismiss);
        chipMonday = view.findViewById(R.id.periodDialog_monday);
        chipTuesday = view.findViewById(R.id.periodDialog__tuesday);
        chipWednesday = view.findViewById(R.id.periodDialog_wednesday);
        chipThursday = view.findViewById(R.id.periodDialog_thursday);
        chipFriday = view.findViewById(R.id.periodDialog_friday);
        chipSaturday = view.findViewById(R.id.periodDialog_saturday);
        chipSunday = view.findViewById(R.id.periodDialog_sunday);
        textViewTime = view.findViewById(R.id.periodDialog_time);
        labelDays = view.findViewById(R.id.periodDialog_labelDays);
        labelWeekDays = view.findViewById(R.id.periodDialog_labelWeekDays);
        layoutDays = view.findViewById(R.id.periodDialog_constrainLayoutDays);
        layoutWeekDays = view.findViewById(R.id.periodDialog_layoutWeekDays);
        textViewFirstDate = view.findViewById(R.id.periodDialog_firstDate);
        textViewLastDate = view.findViewById(R.id.periodDialog_lastDate);

    }
    private void initDefaults(){
        log("...initDefaults()");
        time = LocalTime.now();
    }
    private void initListeners(){
        log("...initListeners()");
        textViewTime.setOnClickListener(view->showTimeDialog());
        textViewLastDate.setOnClickListener(view->showDateDialog());
        textViewFirstDate.setOnClickListener(view->showDateDialog());
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
        labelWeekDays.setOnClickListener(view->toggle(Mode.WEEKDAYS));
        labelDays.setOnClickListener(view->toggle(Mode.DAYS));
        buttonSave.setOnClickListener(view->{
            log("...buttonSave()");
            listener.onPeriod(getPeriod());
            log("...will dismiss?");
            dismiss();
        });
        buttonDismiss.setOnClickListener(view->{
            log("...button dismiss");
            dismiss();
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
            repeat.add(dayOfWeek);
        }else{
            repeat.remove(dayOfWeek);
        }
    }
    public void setListener(Callback callback){
        this.listener = callback;
    }
    private void setUserInterface(){
        log("...setUserInterface()");
        textViewTime.setText(Converter.format(time));

    }
    private void showDateDialog(){
        log("...showDateDialog()");

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

    /**
     * toggle between weekdays or days interval
     * TODO, month, year
     */

    private void toggle(Mode mode){
        log("...toggle(Mode)", mode.toString());
        this.mode = mode;
        if( mode.equals(Mode.DAYS)){
            layoutDays.setVisibility(View.VISIBLE);
            layoutWeekDays.setVisibility(View.GONE);
        }else if( mode.equals(Mode.WEEKDAYS)){
            layoutDays.setVisibility(View.GONE);
            layoutWeekDays.setVisibility(View.VISIBLE);
        }
    }
}
