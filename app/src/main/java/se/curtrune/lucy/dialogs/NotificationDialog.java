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
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.time.LocalDate;
import java.time.LocalTime;

import se.curtrune.lucy.R;
import se.curtrune.lucy.classes.Item;
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
    private Item item;
    public static boolean VERBOSE = false;


    public interface Callback{
        void onNotification(Notification notification);
    }

    private Callback listener;

    public NotificationDialog(Item item)
    {
        assert item != null;
        log("NotificationDialog(Item)", item.getHeading());
        this.item = item;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        log("NotificationDialog.onCreateView(...)");
        View view = inflater.inflate(R.layout.notification_dialog, container, false);
        initDefaults(item);
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
        notification.setTitle(item.getHeading());
        notification.setContent(item.getHeading());
        return notification;
    }

    private void initComponents(View view){
        if( VERBOSE) log("...initComponents(View)");
        textViewTime = view.findViewById(R.id.notificationDialog_time);
        textViewDate = view.findViewById(R.id.notificationDialog_date);
        radioButtonAlarm = view.findViewById(R.id.notificationDialog_alarm);
        radioButtonNotification = view.findViewById(R.id.notificationDialog_notification);
        buttonSave = view.findViewById(R.id.notificationDialog_save);
        buttonDismiss = view.findViewById(R.id.notificationDialog_dismiss);
        log("...buttonDismiss is null", buttonDismiss == null ? "true": "false");
    }
    private  void initDefaults(Item item){
        if( VERBOSE )log("...initDefaults(Item)");
        targetDate = item.getTargetDate();
        targetTime = item.getTargetTime();
        notification = new Notification();
        notification.setDate(targetDate);
        notification.setTime(targetTime);
        notification.setType(Notification.Type.NOTIFICATION);
        notification.setTitle(item.getHeading());
        notification.setContent(item.getHeading());
    }
    private void initListeners(){
        if( VERBOSE) log("...initListeners()");
        buttonSave.setOnClickListener(view1 -> {
            listener.onNotification(notification);
            dismiss();
        });
        buttonDismiss.setOnClickListener(view->dismiss());
        textViewTime.setOnClickListener(view->showTimeDialog());
        textViewDate.setOnClickListener(view->showDateDialog());
    }
    private void initUserInterface(){
        if( VERBOSE) log("...initUserInterface()");
        textViewTime.setText(Converter.format(targetTime));
        textViewDate.setText(targetDate.toString());
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }
    public void setListener(Callback callback){
        this.listener = callback;
    }
    private void showDateDialog(){
        log("...showDateDialog()");
        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext());
        datePickerDialog.setOnDateSetListener((view, year, month, dayOfMonth) -> {
            if( VERBOSE) log("...onDateSet(DatePicker, int, int, int)");
            LocalDate targetDate = LocalDate.of(year, month +1, dayOfMonth);
            notification.setDate(targetDate);
            textViewDate.setText(targetDate.toString());
        });
        datePickerDialog.show();

    }
    private void showTimeDialog(){
        log("...showTimeDialog()");
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
