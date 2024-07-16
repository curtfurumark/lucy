package se.curtrune.lucy.dialogs;

import static se.curtrune.lucy.util.Logger.log;

import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import se.curtrune.lucy.R;
import se.curtrune.lucy.adapters.ActionAdapter;
import se.curtrune.lucy.classes.Action;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.Mental;
import se.curtrune.lucy.classes.Type;
import se.curtrune.lucy.util.Converter;
import se.curtrune.lucy.workers.MentalWorker;


public class EditItemDialog extends BottomSheetDialogFragment {
    private EditText editText_heading;
    private TextView textViewParentList;

    private String heading;
    private Button buttonSave;
    private Button buttonDismiss;
    private CheckBox checkBoxCalendarEvent;
    private CheckBox checkBoxIsTemplate;

    private LocalTime targetTime;
    private LocalDate targetDate;
    private ActionAdapter actionAdapter;
    private RecyclerView actionRecycler;

    private Action currentAction;
    public static boolean VERBOSE = true;
    private final Item item;
    public interface Callback{
        void onUpdate(Item item);
    }

    private Callback listener;

    public EditItemDialog(Item item) {
        log("AddItemDialog(Item parent)");
        assert  item != null;
        this.item = item;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        log("AddItemDialog.onCreateView(...)");
        View view = inflater.inflate(R.layout.add_item_dialog, container, false);
        initComponents(view);
        initActionRecycler();
        initListeners();
        initDefaults();
        editText_heading.setText(heading);
        log("...targetDate", targetDate);
        initUserInterface(item);
        return view;
    }

    private List<Action> getActionList(Item item){
        log("...getActionList()");
        Action time = new Action();
        time.setTitle(item.getTargetTime().toString());
        time.setType(Action.Type.TIME);

        Action notification = new Action();
        if( item.hasNotification()){
            notification.setTitle(item.getNotification().toString());
        }else {
            notification.setTitle(getString(R.string.notification));
        }
        notification.setType(Action.Type.NOTIFICATION);

        Action dateAction = new Action();
        dateAction.setTitle(item.getTargetDate().toString());
        dateAction.setType(Action.Type.DATE);

        Action repeat = new Action();
        if( item.hasPeriod()) {
            repeat.setTitle(item.getPeriod().toString());
        }else{
            repeat.setTitle(getString(R.string.repeat));
        }
        repeat.setType(Action.Type.REPEAT);

        Action category = new Action();
        if( item.hasCategory()){
            category.setTitle(item.getCategory());
        }else {
            category.setTitle(getString(R.string.category));
        }
        category.setType(Action.Type.CATEGORY);

        Action actionDuration = new Action();
        actionDuration.setTitle(getString(R.string.duration));
        actionDuration.setType(Action.Type.DURATION);

        Action actionMental = new Action();
        actionMental.setTitle(getString(R.string.mental));
        actionMental.setType(Action.Type.MENTAL);

        ArrayList<Action> actionList = new ArrayList<>();
        actionList.add(time);
        actionList.add(dateAction);
        actionList.add(repeat);
        actionList.add(notification);
        actionList.add(category);
        actionList.add(actionDuration);
        actionList.add(actionMental);

        return actionList;
    }


    private void initActionRecycler(){
        log("...initActionRecycler()");
        //ActionAdapter.VERBOSE = true;
        actionAdapter = new ActionAdapter(getActionList(item), action -> {
            log("...onAction(Action)", action.getTitle());
            log("...type", action.getType().toString());
            currentAction = action;
            switch (action.getType()){
                case NOTIFICATION:
                    showNotificationDialog();
                    break;
                case CATEGORY:
                    showCategoryDialog();
                    break;
                case TIME:
                    showTimeDialog();
                    break;
                case REPEAT:
                    showRepeatDialog();
                    break;
                case DATE:
                    showDateDialog();
                    break;
                case DURATION:
                    showDurationDialog();
                    break;
                case MENTAL:
                    showMentalDialog();
                    break;
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        actionRecycler.setLayoutManager(layoutManager);
        actionRecycler.setItemAnimator(new DefaultItemAnimator());
        actionRecycler.setAdapter(actionAdapter);
        actionAdapter.notifyDataSetChanged();
    }
    private void initComponents(View view){
        log("...initComponents(View)");
        editText_heading = view.findViewById(R.id.addItemDialog_heading);
        buttonSave = view.findViewById(R.id.addItemDialog_buttonOK);
        buttonDismiss = view.findViewById(R.id.addItemDialog_buttonDismiss);
        actionRecycler = view.findViewById(R.id.addItemDialog_actionRecycler);
        textViewParentList = view.findViewById(R.id.addItemDialog_parentList);
        checkBoxCalendarEvent = view.findViewById(R.id.addItemDialog_checkBoxCalendarEvent);
        checkBoxIsTemplate = view.findViewById(R.id.addItemDialog_checkBoxIsTemplate);
    }
    private void initDefaults(){
        log("...initDefaults()");
        targetDate = LocalDate.now();
    }
    private void initListeners(){
        log("...initListeners()");
        buttonSave.setOnClickListener(view1 -> {
            log("...saveItem()");
            if( checkBoxCalendarEvent.isChecked()){
                item.setType(Type.APPOINTMENT);
            }
            item.setHeading(editText_heading.getText().toString());
            listener.onUpdate(item);
            dismiss();
        });
        buttonDismiss.setOnClickListener(view->dismiss());
    }

    public void initUserInterface(Item item){
        log("...initUserInterface(Item)");
        //String strParentList = String.format(Locale.getDefault(), "%s: %s",getString(R.string.add_to_list), parentItem.getHeading());
        //textViewParentList.setText(strParentList);
        editText_heading.setText(item.getHeading());
        checkBoxCalendarEvent.setChecked(item.getType().equals(Type.APPOINTMENT));
        checkBoxIsTemplate.setChecked(item.isTemplate());
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }
    public void setCallback( Callback callback){
        this.listener = callback;
    }

    public void setHeading(String heading){
        this.heading = heading;
    }



    private void showCategoryDialog(){
        log("...showCategoryDialog()");
        ChooseCategoryDialog dialog = new ChooseCategoryDialog(item.getCategory());
        dialog.setCallback(category -> {
            log("...onSelected(String)", category);
            currentAction.setTitle(category);
            item.setCategory(category);
            actionAdapter.notifyDataSetChanged();
        });
        dialog.show(getChildFragmentManager(), "choose category");
    }
    public void showDateDialog(){
        log("...showDateDialog()");

    }
    public void showDurationDialog(){
        log("...showDurationDialog()");
        DurationDialog dialog = new DurationDialog();
        dialog.setCallback(duration -> {
            log("...onDurationDialog(Duration)");
            //item.setDuration(duration.getSeconds());
            item.setEstimatedDuration(duration.getSeconds());
            currentAction.setTitle(Converter.formatSecondsWithHours(duration.getSeconds()));
            actionAdapter.notifyDataSetChanged();
        });
        dialog.show(getChildFragmentManager(), "duration");

    }
    public void showMentalDialog(){
        log("...showMentalDialog()");
        Mental mental = MentalWorker.getMental(item, getContext());
        MentalDialog dialog = new MentalDialog(mental);
        dialog.setCallback((mental1, mode) -> {
            log("...onMental(Mental, Mental)", mode.toString());
            log("should only be mode edit");
            int res = MentalWorker.update(mental1, getContext());
            if( res != 1){
                log("ERROR updating mental");
                Toast.makeText(getContext(), "ERROR updating mental", Toast.LENGTH_LONG).show();
            }else{
                log("...mental updated ok");
            }
        });
        dialog.show(getChildFragmentManager(), "edit mental");
    }

    public void showNotificationDialog(){
        log("...showNotificationDialog()");
        NotificationDialog dialog = new NotificationDialog(item);
        dialog.setListener(notification -> {
            log("...onNotification(Notification)");
            currentAction.setTitle(notification.toString());
            item.setNotification(notification);
            actionAdapter.notifyDataSetChanged();
        });
        dialog.show(getChildFragmentManager(), "notification ");

    }
    private void showRepeatDialog(){
        log("...showRepeatDialog()");
        RepeatDialog dialog = new RepeatDialog();
        dialog.setCallback(period -> {
            log("...onRepeat(Unit)", period.toString());
            currentAction.setTitle(period.toString());
            actionAdapter.notifyDataSetChanged();
        });
        dialog.show(getChildFragmentManager(), "repeat");

    }
    private void showTimeDialog(){
        log("...showTimeDialog()");
        LocalTime now = LocalTime.now();
        int minutes = now.getMinute();
        int hour = now.getHour();
        TimePickerDialog timePicker = new TimePickerDialog(getContext(), (view, hourOfDay, minute) -> {
            targetTime = LocalTime.of(hourOfDay, minute);
            item.setTargetTime(targetTime);
            currentAction.setTitle(targetTime.toString());
            actionAdapter.notifyDataSetChanged();
        }, hour, minutes, true);
        timePicker.show();
    }
}
