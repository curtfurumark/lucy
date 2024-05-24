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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import java.util.ArrayList;
import java.util.List;

import se.curtrune.lucy.R;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.Notification;
import se.curtrune.lucy.classes.Repeat;
import se.curtrune.lucy.classes.State;
import se.curtrune.lucy.workers.UtilWorker;

public class AddTemplateDialog extends BottomSheetDialogFragment {

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
    private LinearLayout layoutOther;
    private EditText editTextHeading;
    private EditText editTextDays;

    private TextView textViewParent;
    private TextView labelDays;
    private TextView labelWeekDays;
    private TextView labelRepeat;
    private TextView labelOther;
    private TextView labelNotification;
    private TextView labelDateTime;
    private TextView textViewDate;
    private TextView textViewTime;
    //private TextView textViewNow;
    private Spinner spinner;
    private Button buttonSave;
    private Button buttonDismiss;
    private Button buttonAddAndContinue;
    private Button buttonRightNow;
    private CheckBox checkBoxShowInCalender;
    private ArrayAdapter<String> arrayAdapter;
    private final Item parent;
    private LocalDate date;
    private LocalTime time;

    private OnNewItemCallback callback;
    private String category;
    private Notification notification;
    public static boolean VERBOSE = false;
    private Item item;
    private boolean hasPeriod = false;

    private Repeat.Mode mode = Repeat.Mode.DAYS;

    public AddTemplateDialog(Item parent, LocalDate date) {
        assert  parent != null;
        log("AddTemplateDialog(Item) parent", parent.getHeading());
        this.parent = parent;
        this.date = date;
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
    private void addNotification(){
        log("...addNotification");
        LocalTime rightNow = LocalTime.now();
        int minutes = rightNow.getMinute();
        int hour = rightNow.getHour();
        TimePickerDialog timePicker = new TimePickerDialog(getContext(), (view, hourOfDay, minute) -> {
            LocalTime notificationTime = LocalTime.of(hourOfDay, minute);
            labelNotification.setText(notificationTime.toString());
            notification = new Notification();
            notification.setTime(notificationTime);
        }, hour, minutes, true);
        timePicker.show();

    }
    private Item createItem(){
        if( VERBOSE) log("...createItem()");
        item = new Item();
        item.setState(State.TODO);
        item.setTargetDate(date);
        item.setParent(parent);
        return item;
    }
    private Item getItem(){
        if( VERBOSE) log("...getItem()");
        item.setHeading(editTextHeading.getText().toString());
        item.setParentId(parent.getID());
        item.setIsCalenderItem(checkBoxShowInCalender.isChecked());
        item.setPeriod(getPeriod());
        item.setCategory(category);
        if( notification != null){
            log("...notification != null");
            item.setNotification(notification);
        }
        return item;
    }
    private Repeat getPeriod(){
        log("...getPeriod() hasPeriod", hasPeriod);
        Repeat repeat = null;
        if( hasPeriod){
            repeat = new Repeat();
            if( mode.equals(Repeat.Mode.DAYS)){
                repeat.setDays(Integer.parseInt(editTextDays.getText().toString()));
            }else{
                repeat.setWeekDays(getWeekDays());
            }
        }
        return repeat;
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
        if( VERBOSE) log("...initComponents(View view");
        editTextHeading = view.findViewById(R.id.addTemplateDialog_heading);

        editTextDays = view.findViewById(R.id.addTemplateDialog_days);
        textViewParent = view.findViewById(R.id.addTemplateDialog_parent);

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
        //NOTIFICATION
        labelNotification = view.findViewById(R.id.addTemplateDialog_labelNotification);
        //other
        labelOther = view.findViewById(R.id.addTemplateDialog_labelOther);
        layoutOther = view.findViewById(R.id.addTemplateDialog_layoutOther);
        spinner = view.findViewById(R.id.addTemplateDialog_spinner);
        checkBoxShowInCalender = view.findViewById(R.id.addTemplateDialog_showInCalender);
        //buttons
        buttonRightNow = view.findViewById(R.id.addTemplateDialog_buttonRightNow);
        buttonSave = view.findViewById(R.id.addTemplateDialog_button);
        buttonDismiss = view.findViewById(R.id.addTemplateDialog_dismiss);
        buttonAddAndContinue = view.findViewById(R.id.addTemplateDialog_addAndContinue);
    }

    private void initListeners(){
        if( VERBOSE) log("...initListeners()");
        buttonSave.setOnClickListener(view->save(true));
        buttonAddAndContinue.setOnClickListener(view->save(false));
        buttonDismiss.setOnClickListener(view->dismiss());
        labelDays.setOnClickListener(view->toggleDays());
        labelWeekDays.setOnClickListener(view->toggleWeekDays());
        labelDateTime.setOnClickListener(view-> toggleDateTime());
        labelRepeat.setOnClickListener(view->toggleRepeat());
        textViewDate.setOnClickListener(view->showDateDialog());
        textViewTime.setOnClickListener(view->showTimeDialog());
        labelOther.setOnClickListener(view->toggleOther());
        buttonRightNow.setOnClickListener(view->rightNow());
        labelNotification.setOnClickListener(view->addNotification());
    }

    /**
     * sets default category to the first in the category array
     */
    private void initSpinner(){
        log("...initSpinner()");
        String[] categories = UtilWorker.getCategories(getContext());
        arrayAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, categories);
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
        if( VERBOSE) log("...initUserInterface()");
        textViewParent.setText(parent.getHeading());
        setSpinnerSelection(parent.getCategory());
        if( date != null) {
            textViewDate.setText(date.toString());
        }
        checkBoxShowInCalender.setChecked(true);
    }
    private void rightNow(){
        log("...rightNow()");
        if( !validateInput()){
            return;
        }
        item = getItem();
        item.setTargetTime(LocalTime.now());
        item.setTargetDate(LocalDate.now());
        callback.onNewItem(item);
        dismiss();

    }
    private void save(boolean dismiss){
        log("...save()");
        if( !validateInput()){
            log("...missing input");
            return;
        }
        callback.onNewItem(getItem());
        if( dismiss) {
            editTextHeading.setText("");
            item = createItem();
            dismiss();
        }
    }
    public void setCallback(OnNewItemCallback callback) {
        this.callback = callback;
    }
    private void setSpinnerSelection(String category){
        log("...setSpinnerSelection(String)", category);
        spinner.setSelection(arrayAdapter.getPosition(category));
    }
    private void showDateDialog(){
        if( VERBOSE) log("...showDateDialog()");
        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext());
        datePickerDialog.setOnDateSetListener((view, year, month, dayOfMonth) -> {
            log("...onDateSet(DatePicker, int, int , int)");
            date = LocalDate.of(year, month +1, dayOfMonth);
            item.setTargetDate(date);
            textViewDate.setText(date.toString());
        });
        datePickerDialog.show();
    }
    private void showTimeDialog(){
        if( VERBOSE) log("...showTimeDialog()");
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
        if( VERBOSE) log("...toggleDays()");
        if( layoutDays.getVisibility() == View.GONE){
            log("...setting visibility to visible");
            mode = Repeat.Mode.DAYS;
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
    private void toggleOther(){
        log("...toggleOther()");
        if( layoutOther.getVisibility() == View.GONE){
            layoutOther.setVisibility(View.VISIBLE);
        }else{
            layoutOther.setVisibility(View.GONE);
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
        if( VERBOSE) log("...toggleWeekDays()");
        if( layoutWeekDays.getVisibility() == View.GONE){
            mode = Repeat.Mode.DAY_OF_WEEKS;
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
        return true;
    }
}
