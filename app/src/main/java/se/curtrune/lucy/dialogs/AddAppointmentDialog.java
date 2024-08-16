package se.curtrune.lucy.dialogs;

import static se.curtrune.lucy.util.Logger.log;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.time.LocalDate;
import java.time.LocalTime;

import se.curtrune.lucy.R;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.Type;
import se.curtrune.lucy.util.Converter;

public class AddAppointmentDialog extends BottomSheetDialogFragment {
    private TextView textViewDate;
    private TextView textViewTime;
    private LocalTime targetTime;
    private LocalDate targetDate;
    private Button buttonAdd;
    private EditText editTextHeading;
    private EditText editTextComment;
    private EditText editTextLocation;

    public interface OnNewAppointmentCallback{
        void onNewAppointment(Item item);
    }
    private OnNewAppointmentCallback callback;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_appointment_dialog, container, false);
        initComponents(view);
        initListeners();
        initDefaults();
        initUserInterface();
        return view;
    }
    private Item getItem(){
        log("...getItem()");
        Item item = new Item();
        item.setHeading(editTextHeading.getText().toString());
        item.setType(Type.APPOINTMENT);
        item.setComment(editTextComment.getText().toString());
        item.setDescription(editTextLocation.getText().toString());
        item.setTargetTime(targetTime);
        item.setTargetDate(targetDate);
        return item;
    }
    private void initComponents(View view){
        log("...initComponents()");
        textViewDate = view.findViewById(R.id.addAppointmentDialog_date);
        textViewTime = view.findViewById(R.id.addAppointmentDialog_time);
        editTextHeading = view.findViewById(R.id.addAppointmentDialog_heading);
        editTextComment = view.findViewById(R.id.addAppointmentDialog_comment);
        editTextLocation = view.findViewById(R.id.addAppointmentDialog_location);
        buttonAdd = view.findViewById(R.id.addAppointmentDialog_button);
    }
    private void initDefaults(){
        targetDate = LocalDate.now();
        targetTime = LocalTime.now();
    }
    private void initListeners(){
        textViewDate.setOnClickListener(view->showDateDialog());
        textViewTime.setOnClickListener(view->showTimeDialog());
        buttonAdd.setOnClickListener(view->{
            log("...on button add click");
            callback.onNewAppointment(getItem());
            dismiss();
        });
    }
    private void initUserInterface(){
        textViewTime.setText(Converter.format(targetTime));
        textViewDate.setText(targetDate.toString());
    }
    public void setCallback(OnNewAppointmentCallback callback){
        this.callback = callback;
    }
    private void showDateDialog(){
        log("...showDateDialog()");
        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext());
        datePickerDialog.setOnDateSetListener((view, year, month, dayOfMonth) -> {
            log("...onDateSet(...)");
            targetDate = LocalDate.of(year, month +1, dayOfMonth);
            textViewDate.setText(targetDate.toString());
        });
        datePickerDialog.show();
    }
    private void showTimeDialog(){
        log("...showTimeDialog");
        int minutes = LocalTime.now().getMinute();
        int hour = LocalTime.now().getHour();
        TimePickerDialog timePicker = new TimePickerDialog(getContext(), (view, hourOfDay, minute) -> {
            targetTime = LocalTime.of(hourOfDay, minute);
            textViewTime.setText(targetTime.toString());
        }, hour, minutes, true);
        timePicker.show();
    }
}
