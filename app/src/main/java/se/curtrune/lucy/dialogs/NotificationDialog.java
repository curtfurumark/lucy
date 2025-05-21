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
import se.curtrune.lucy.classes.item.Item;
import se.curtrune.lucy.features.notifications.Notification;
import se.curtrune.lucy.util.Converter;


public class NotificationDialog extends BottomSheetDialogFragment {
    private TextView textViewTime;
    private TextView textViewDate;
    private RadioButton radioButtonAlarm;
    private RadioButton radioButtonNotification;

    private Button buttonSave;
    private Button buttonDelete;
    private Button buttonDismiss;
    private LocalDate targetDate;
    private LocalTime targetTime;
    private Notification notification;
    private final Item item;
    public static boolean VERBOSE = false;

    public enum Action{
        INSERT, EDIT, DELETE
    }
    private final Action action;

    public interface Callback{
        void onNotification(Notification notification, Action action);
    }

    private Callback listener;

    public NotificationDialog(Item item)
    {
        assert item != null;
        log("NotificationDialog(Item)", item.getHeading());
        this.item = item;
        if( item.hasNotification()){
            action = Action.EDIT;
        }else{
            action = Action.INSERT;
        }
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
        notification.type = Notification.Type.PENDING;
        notification.title = item.getHeading();
        notification.content = item.getHeading();
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
        buttonDelete = view.findViewById(R.id.notificationDialog_buttonDelete);
    }
    private  void initDefaults(Item item){
        if( VERBOSE )log("...initDefaults(Item)");
        targetDate = item.getTargetDate();
        targetTime = item.getTargetTime();
        notification = new Notification();
        notification.setDate(targetDate);
        notification.setTime(targetTime);
        notification.type = Notification.Type.NOTIFICATION;
        notification.title = item.getHeading();
        notification.content = item.getHeading();
    }
    private void initListeners(){
        if( VERBOSE) log("...initListeners()");
        buttonSave.setOnClickListener(view1 -> {
            listener.onNotification(notification, action);
            dismiss();
        });
        buttonDismiss.setOnClickListener(view->dismiss());
        textViewTime.setOnClickListener(view->showTimeDialog());
        textViewDate.setOnClickListener(view->showDateDialog());
        buttonDelete.setOnClickListener(view->onDelete());
    }
    private void initUserInterface(){
        if( VERBOSE) log("...initUserInterface()");
        textViewTime.setText(Converter.format(targetTime));
        textViewDate.setText(targetDate.toString());
        if( action.equals(Action.EDIT)){
            buttonSave.setText(getString(R.string.update));
            if(notification.type.equals(Notification.Type.NOTIFICATION)){
                radioButtonNotification.setChecked(true);
            }else{
                radioButtonAlarm.setChecked(true);
            }
        }
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }
    private void onDelete(){
        log("...onDelete()");
        listener.onNotification(notification, Action.DELETE );
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
