package se.curtrune.lucy.dialogs;

import static se.curtrune.lucy.util.Logger.log;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.textfield.TextInputLayout;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import se.curtrune.lucy.R;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.Period;
import se.curtrune.lucy.classes.State;
import se.curtrune.lucy.workers.UtilWorker;

public class AddTemplateDialog extends BottomSheetDialogFragment {
    //private ConstraintLayout layoutDays;
   // private TextInputLayout layoutDays;
    private Chip chipMonday;
    private Chip chipTuesday;
    private Chip chipWednesday;
    private Chip chipThursday;
    private Chip chipFriday;
    private Chip chipSaturday;
    private Chip chipSunday;

    private ConstraintLayout layoutWeekDays;
    private ConstraintLayout layoutDateTime;
    private ConstraintLayout layoutRepeat;
    private ConstraintLayout layoutDays;
    private EditText editTextHeading;
    private EditText editTextDays;

    private TextView textViewParent;
    private TextView labelDays;
    private TextView labelWeekDays;
    private TextView labelRepeat;
    private TextView labelDateTime;
    private TextView textViewDate;
    private TextView textViewTime;
    private Spinner spinner;
    private Button buttonSave;
    private Button buttonDismiss;
    private Button buttonAddAndContinue;
    private ArrayAdapter<String> arrayAdapter;
    private Item parent;
    private LocalDate date;
    private LocalTime time;

    private OnNewItemCallback callback;
    private String category;
    private Item item;
    private boolean hasPeriod = false;

    private Period.Mode mode = Period.Mode.DAYS;

    public AddTemplateDialog(Item parent) {
        log("AddTemplateDialog(Item)");
        this.parent = parent;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_template_dialog, container, false);
        initComponents(view);
        initListeners();
        initSpinner();
        initUserInterface();
        createItem();
        return view;
    }
    private void createItem(){
        log("...createItem()");
        item = new Item();
        item.setState(State.TODO);
        //item.setIsTemplate(true);
        item.setParent(parent);
    }
    private Item getItem(){
        log("...getItem()");
        item.setHeading(editTextHeading.getText().toString());
        item.setPeriod(getPeriod());
        return item;
    }
    private Period getPeriod(){
        log("...getPeriod() hasPeriod", hasPeriod);
        Period period = null;
        if( hasPeriod){
            period = new Period();
            if( mode.equals(Period.Mode.DAYS)){
                period.setDays(Integer.parseInt(editTextDays.getText().toString()));
            }else{
                period.setWeekDays(getWeekDays());
            }

        }
        return period;
    }
    private List<DayOfWeek> getWeekDays(){
        log("...getWeekDays()");
        List<DayOfWeek> weekDays = new ArrayList<>();
        if( chipMonday.isChecked()){
            weekDays.add(DayOfWeek.MONDAY);
        }
        if( chipTuesday.isChecked()){
            weekDays.add(DayOfWeek.TUESDAY);
        }
        if( chipWednesday.isChecked()){
            weekDays.add(DayOfWeek.WEDNESDAY);
        }
        if( chipThursday.isChecked()){
            weekDays.add(DayOfWeek.THURSDAY);
        }
        if( chipFriday.isChecked()){
            weekDays.add(DayOfWeek.FRIDAY);
        }
        if( chipSaturday.isChecked()){
            weekDays.add(DayOfWeek.SATURDAY);
        }
        if( chipSunday.isChecked()){
            weekDays.add(DayOfWeek.SUNDAY);
        }
        return weekDays;
    }

    private void initComponents(View view){
        log("...initComponents(View view");
        editTextHeading = view.findViewById(R.id.addTemplateDialog_heading);
        buttonSave = view.findViewById(R.id.addTemplateDialog_button);
        buttonDismiss = view.findViewById(R.id.addTemplateDialog_dismiss);
        buttonAddAndContinue = view.findViewById(R.id.addTemplateDialog_addAndContinue);
        editTextDays = view.findViewById(R.id.addTemplateDialog_days);
        textViewParent = view.findViewById(R.id.addTemplateDialog_parent);
        spinner = view.findViewById(R.id.addTemplateDialog_spinner);
        //repeat
        layoutRepeat = view.findViewById(R.id.addTemplateDialog_layoutRepeat);
        labelDays = view.findViewById(R.id.addTemplateDialog_labelDays);
        labelWeekDays = view.findViewById(R.id.addTemplateDialog_labelWeekDays);
        layoutDays = view.findViewById(R.id.addTemplateDialog_constrainLayoutDays);
        layoutWeekDays = view.findViewById(R.id.addTemplateDialog_layoutWeekDays);
        chipMonday = view.findViewById(R.id.addTemplateDialog_monday);
        chipTuesday = view.findViewById(R.id.addTemplateDialog__tuesday);
        chipWednesday = view.findViewById(R.id.addTemplateDialog_wednesday);
        chipThursday = view.findViewById(R.id.addTemplateDialog_thursday);
        chipFriday = view.findViewById(R.id.addTemplateDialog_friday);
        chipSaturday = view.findViewById(R.id.addTemplateDialog_saturday);
        chipSunday = view.findViewById(R.id.addTemplateDialog_sunday);
        //info
        labelDateTime = view.findViewById(R.id.addTemplateDialog_labelDateTime);
        layoutDateTime = view.findViewById(R.id.addTemplateDialog_layoutDateTime);
        textViewDate = view.findViewById(R.id.addTemplateDialog_date);
        textViewTime = view.findViewById(R.id.addTemplateDialog_time);
        labelRepeat = view.findViewById(R.id.addTemplateDialog_repeat);
    }

    private void initListeners(){
        log("...initListeners()");
        buttonSave.setOnClickListener(view->save(true));
        buttonAddAndContinue.setOnClickListener(view->save(false));
        buttonDismiss.setOnClickListener(view->dismiss());
        labelDays.setOnClickListener(view->toggleDays());
        labelWeekDays.setOnClickListener(view->toggleWeekDays());
        labelDateTime.setOnClickListener(view-> toggleDateTime());
        labelRepeat.setOnClickListener(view->toggleRepeat());
        textViewDate.setOnClickListener(view->showDateDialog());
        textViewTime.setOnClickListener(view->showTimeDialog());
    }

    /**
     * sets default category to the first in the category array
     */
    private void initSpinner(){
        log("...initSpinner()");
        String[] categories = UtilWorker.getCategories(getContext());
        arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, categories);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spinner.setAdapter(arrayAdapter);
        spinner.setSelection(0);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                log("...onItemSelected()");
                category = (String) spinner.getSelectedItem();
                log("...category selected", category);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    private void initUserInterface(){
        log("...initUserInterface()");
        textViewParent.setText(parent.getHeading());
        setSpinnerSelection(parent.getCategory());
    }
    private void save(boolean dismiss){
        log("...save()");
        if( !validateInput()){
            log("...missing input");
            return;
        }
        callback.onNewItem(getItem());
        if( dismiss) {
            dismiss();
        }
    }
    public void setCallback(OnNewItemCallback callback) {
        this.callback = callback;
    }
    private void setSpinnerSelection(String category){
        log("..setSpinnerSelection(String)", category);
        spinner.setSelection(arrayAdapter.getPosition(category));
    }
    private void showDateDialog(){
        log("...showDateDialog()");
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext());
        datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                log("...onDateSet(DatePicker, int, int , int)");
                date = LocalDate.of(year, month +1, dayOfMonth);
                item.setTargetDate(date);
                textViewDate.setText(date.toString());
            }
        });
        datePickerDialog.show();
    }
    private void showTimeDialog(){
        log("...showTimeDialog()");
        int minutes = LocalTime.now().getMinute();
        int hour = LocalTime.now().getHour();
        TimePickerDialog timePicker = new TimePickerDialog(getContext(), (view, hourOfDay, minute) -> {
            time = LocalTime.of(hourOfDay, minute);
            textViewTime.setText(time.toString());
            item.setTargetTime(time);
        }, hour, minutes, true);
        timePicker.show();

    }
    private void toggleDays(){
        log("...toggleDays()");
        if( layoutDays.getVisibility() == View.GONE){
            log("...setting visibility to visible");
            mode = Period.Mode.DAYS;
            layoutDays.setVisibility(View.VISIBLE);
            layoutWeekDays.setVisibility(View.GONE);
        }else{
            layoutDays.setVisibility(View.GONE);
        }
    }
    private void toggleDateTime(){
        log("...toggleDateTime()");
        if(layoutDateTime.getVisibility() == View.GONE){
            layoutDateTime.setVisibility(View.VISIBLE);
        }else{
            layoutDateTime.setVisibility(View.GONE);
        }
    }
    private void toggleRepeat(){
        log("...toggleRepeat()");
        if ( layoutRepeat.getVisibility() == View.GONE){
            layoutRepeat.setVisibility(View.VISIBLE);
        }else{
            layoutRepeat.setVisibility(View.GONE);
        }

    }
    private void toggleWeekDays(){
        log("...toggleWeekDays()");
        if( layoutWeekDays.getVisibility() == View.GONE){
            mode = Period.Mode.DAY_OF_WEEKS;
            layoutWeekDays.setVisibility(View.VISIBLE);
            layoutDays.setVisibility(View.GONE);
        }else{
            layoutWeekDays.setVisibility(View.GONE);
        }
    }

    public boolean validateInput(){
        log("...validateInput()");
        if( editTextHeading.getText().toString().isEmpty()){
            Toast.makeText(getContext(), "missing heading", Toast.LENGTH_LONG).show();
            log("...item requires heading, and no heading is supplied...");
            return false;
        }
/*        if( mode.equals(Period.Mode.DAYS)) {
            if (editTextDays.getText().toString().isEmpty()) {
                Toast.makeText(getContext(), "missing days", Toast.LENGTH_LONG).show();
                log("...missing days");
                return false;
            }
        }*/
        return true;
    }
}
