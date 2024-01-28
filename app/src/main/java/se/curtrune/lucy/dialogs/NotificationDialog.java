package se.curtrune.lucy.dialogs;

import static se.curtrune.lucy.util.Logger.log;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import se.curtrune.lucy.R;
import se.curtrune.lucy.classes.Notification;
import se.curtrune.lucy.classes.Period;


public class NotificationDialog extends BottomSheetDialogFragment {
    private TextView textViewTime;
    private TextView textViewDate;
    private RadioButton radioButtonAlarm;
    private RadioButton radioButtonNotification;

    private Button buttonSave;
    private Button buttonDismiss;

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

        initComponents(view);
        initListeners();

        return view;
    }
    private Notification getNotification(){
        log("...getNotification()");
        Notification notification = new Notification();
        notification.setDate(textViewDate.getText().toString());
        notification.setTime(textViewTime.getText().toString());
        notification.setItemID(432);
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
    private void initListeners(){
        log("...initListeners()");
        buttonSave.setOnClickListener(view1 -> {
            Notification notification = getNotification();
            listener.onNotification(notification);
            dismiss();
        });
        buttonDismiss.setOnClickListener(view->dismiss());

    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.listener = (Callback) context;
    }


}
