package se.curtrune.lucy.dialogs;

import static se.curtrune.lucy.util.Logger.log;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.time.LocalDate;
import java.time.LocalTime;

import se.curtrune.lucy.R;
import se.curtrune.lucy.classes.Notification;
import se.curtrune.lucy.util.Converter;


public class NotificationDialog extends BottomSheetDialogFragment {
    private TextView textViewTime;
    private TextView textViewDate;
    private RadioButton radioButtonAlarm;
    private RadioButton radioButtonNotification;

    private Button buttonSave;
    private Button buttonDismiss;
    private LocalDate targetDate;
    private LocalTime targetTime;
    private Notification notification;

    public interface Callback{
        void onNotification(Notification notification);
    }

    private Callback listener;
    private enum Mode{
        CREATE, EDIT
    }
    public NotificationDialog(){
        log("AddItemFragment default constructor");
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        log("AddItemDialog.onCreateView(...)");
        View view = inflater.inflate(R.layout.notification_dialog, container, false);

        initDefaults();
        initComponents(view);
        initListeners();
        initUserInterface();

        return view;
    }
    private Notification getNotification(){
        log("...getNotification()");
        Notification notification = new Notification();
        notification.setDate(textViewDate.getText().toString());
        notification.setTime(textViewTime.getText().toString());
        notification.setType(Notification.Type.PENDING);
        return notification;
    }

    private void initComponents(View view){
        log("...initComponents(View)");
        textViewTime = view.findViewById(R.id.notificationDialog_time);
        textViewDate = view.findViewById(R.id.notificationDialog_date);
        radioButtonAlarm = view.findViewById(R.id.notificationDialog_alarm);
        radioButtonNotification = view.findViewById(R.id.notificationDialog_notification);
        buttonSave = view.findViewById(R.id.notificationDialog_save);
        buttonDismiss = view.findViewById(R.id.notificationDialog_dismiss);
        log("...buttonDismiss is null", buttonDismiss == null ? "true": "false");
    }
    private  void initDefaults(){
        log("...initDefaults()");
        targetDate = LocalDate.now();
        targetTime = LocalTime.now();
        notification = new Notification();
        notification.setDate(targetDate);
        notification.setTime(targetTime);
        notification.setType(Notification.Type.ALARM);

    }
    private void initListeners(){
        log("...initListeners()");
        buttonSave.setOnClickListener(view1 -> {
            //Notification notification = getNotification();
            listener.onNotification(notification);
            dismiss();
        });
        buttonDismiss.setOnClickListener(view->dismiss());
        textViewTime.setOnClickListener(view->showTimeDialog());
        textViewDate.setOnClickListener(view->showDateDialog());
    }
    private void initUserInterface(){
        log("...initUserInterface()");
        textViewTime.setText(Converter.format(targetTime));
        textViewDate.setText(targetDate.toString());
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        //this.listener = (Callback) context;
    }
    public void setListener(Callback callback){
        this.listener = callback;
    }
    private void showDateDialog(){
        log("...showDateDialog()");
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext());
        datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                log("...onDateSet(DatePicker, year, month, dayOfMonth");
                targetDate = LocalDate.of(year, month +1, dayOfMonth);
                textViewDate.setText(targetDate.toString());
                notification.setDate(targetDate);
            }
        });
        datePickerDialog.show();
    }
    private void showTimeDialog(){
        log("...showTimeDialog()");
        //AtomicReference<LocalTime> targetTime = new AtomicReference<>(LocalTime.now());
        int minutes = targetTime.getMinute();
        int hour = targetTime.getHour();
        TimePickerDialog timePicker = new TimePickerDialog(getContext(), (view, hourOfDay, minute) -> {
            targetTime = LocalTime.of(hourOfDay, minute);
            textViewTime.setText(targetTime.toString());
            notification.setTime(targetTime);
        }, hour, minutes, true);
        timePicker.show();
    }


}
