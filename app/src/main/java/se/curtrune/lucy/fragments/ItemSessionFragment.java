package se.curtrune.lucy.fragments;

import static se.curtrune.lucy.util.Logger.log;

import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalTime;
import java.util.Locale;

import se.curtrune.lucy.R;
import se.curtrune.lucy.adapters.ActionAdapter;
import se.curtrune.lucy.classes.Action;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.Mental;
import se.curtrune.lucy.dialogs.ChooseCategoryDialog;
import se.curtrune.lucy.dialogs.DurationDialog;
import se.curtrune.lucy.dialogs.MentalDialog;
import se.curtrune.lucy.dialogs.NotificationDialog;
import se.curtrune.lucy.dialogs.RepeatDialog;
import se.curtrune.lucy.util.Converter;
import se.curtrune.lucy.util.Kronos;
import se.curtrune.lucy.workers.DurationWorker;
import se.curtrune.lucy.workers.ItemsWorker;
import se.curtrune.lucy.workers.MentalWorker;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ItemSessionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ItemSessionFragment extends Fragment implements Kronos.Callback{


    private TextView textViewEstimatedTime;
    private TextView textViewHeading;
    private TextView textViewDuration;
    private CheckBox checkBoxTemplate;
    private CheckBox checkBoxSave;
    private CheckBox checkBoxIsCalenderItem;
    private Button buttonTimer;
    private CheckBox checkBoxIsDone;
    private Action currentAction;
    private CheckBox checkBoxPrioritized;
    private ActionAdapter actionAdapter;
    private RecyclerView actionRecycler;
    private Item currentItem;
    private LocalTime targetTime;
    private Kronos kronos;
    private long duration;
    public static boolean VERBOSE = false;

    public ItemSessionFragment() {
        // Required empty public constructor
    }
    public ItemSessionFragment(Item item){
        assert  item != null;
        log("ItemSessionFragment(Item)", item.getHeading());
        this.currentItem = item;

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters..
     * @return A new instance of fragment ItemSessionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ItemSessionFragment newInstance(Item item) {
        return new ItemSessionFragment(item);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            log("...getArguments != null");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        log("ItemsFragment.onCreateView(LayoutInflater, ViewGroup, Bundle)");
        View view = inflater.inflate(R.layout.item_session_fragment, container, false);
        initComponents(view);
        initListeners();
        initKronos();
        initActionRecycler();
        setUserInterface(currentItem);
        return view;
    }
    private void goToCalendar(){
        log("...goToCalendar()");
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.navigationDrawer_frameContainer, new CalenderFragment()).commit();

    }
    private void initActionRecycler(){
        log("...initActionRecycler()");
        ActionAdapter.VERBOSE = true;
        actionAdapter = new ActionAdapter(ActionAdapter.getActionList(currentItem, getContext()), action -> {
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
        //RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        actionRecycler.setLayoutManager(layoutManager);
        actionRecycler.setItemAnimator(new DefaultItemAnimator());
        actionRecycler.setAdapter(actionAdapter);
        actionAdapter.notifyDataSetChanged();
    }
    private void initKronos(){
        log("...initKronos()");
        kronos = Kronos.getInstance(this);
    }
    /**
     * callback for Kronos, called once every second whenever Kronos is running
     * @param secs, number of seconds elapsed since start of timer
     */
    @Override
    public void onTimerTick(long secs) {
        if(VERBOSE) log("ItemSession.onTimerClick() secs", secs);
        this.duration = secs;
        textViewDuration.setText(Converter.formatSecondsWithHours(secs));
    }
    private void showDateDialog() {
    }

    private void initComponents(View view){
        log("...initComponents()");
        textViewEstimatedTime = view.findViewById(R.id.itemSessionFragment_estimatedTime);
        textViewHeading = view.findViewById(R.id.itemSessionFragment_heading);
        checkBoxTemplate = view.findViewById(R.id.itemSessionFragment_checkboxTemplate);
        checkBoxPrioritized = view.findViewById(R.id.itemSessionFragment_checkboxPrioritized);
        checkBoxIsDone = view.findViewById(R.id.itemSessionFragment_checkboxIsDone);
        actionRecycler = view.findViewById(R.id.itemSessionFragment_actionRecycler);
        buttonTimer = view.findViewById(R.id.itemSessionFragment_buttonTimer);
        textViewDuration = view.findViewById(R.id.itemSessionFragment_textViewDuration);
        checkBoxIsCalenderItem = view.findViewById(R.id.itemSessionFragment_checkboxIsCalendarItem);
        checkBoxSave = view.findViewById(R.id.itemSessionFragment_checkboxSave);
    }
    private void initListeners(){
        log("...initListeners()");
        textViewEstimatedTime.setOnClickListener(view->goToCalendar());
        buttonTimer.setOnClickListener(view->toggleTimer());
        checkBoxSave.setOnClickListener(view->updateItem());
    }
    private void setEstimatedTime(Item item){
        log("..setEstimatedTime()");
        long estimatedDuration = DurationWorker.getEstimatedDuration(item, getContext());
        String stringEstimatedDuration = String.format(Locale.getDefault(), "estimated duration_ %s", Converter.formatSecondsWithHours(estimatedDuration));
        textViewEstimatedTime.setText(stringEstimatedDuration);
    }
    private void setUserInterface(Item item){
        log("...setUserInterface(Item)");
        checkBoxIsCalenderItem.setChecked(item.isCalenderItem());
        textViewHeading.setText(item.getHeading());
        checkBoxTemplate.setChecked(item.isTemplate());
        checkBoxPrioritized.setChecked(item.isPrioritized());
        setEstimatedTime(item);
    }
    private void showCategoryDialog(){
        log("...showCategoryDialog()");
        ChooseCategoryDialog dialog = new ChooseCategoryDialog(currentItem.getCategory());
        dialog.setCallback(category -> {
            log("...onSelected(String)", category);
            currentAction.setTitle(category);
            currentItem.setCategory(category);
            actionAdapter.notifyDataSetChanged();
        });
        dialog.show(getChildFragmentManager(), "choose category");
    }

    public void showDurationDialog(){
        log("...showDurationDialog()");
        DurationDialog dialog = new DurationDialog();
        dialog.setCallback(duration -> {
            log("...onDurationDialog(Duration)");
            //item.setDuration(duration.getSeconds());
            currentItem.setEstimatedDuration(duration.getSeconds());
            currentAction.setTitle(Converter.formatSecondsWithHours(duration.getSeconds()));
            actionAdapter.notifyDataSetChanged();
        });
        dialog.show(getChildFragmentManager(), "duration");

    }
    public void showMentalDialog(){
        log("...showMentalDialog()");
        Mental mental = MentalWorker.getMental(currentItem, getContext());
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
        NotificationDialog dialog = new NotificationDialog(currentItem);
        dialog.setListener(notification -> {
            log("...onNotification(Notification)");
            currentAction.setTitle(notification.toString());
            currentItem.setNotification(notification);
            actionAdapter.notifyDataSetChanged();
        });
        dialog.show(getChildFragmentManager(), "notification ");

    }
    private void showRepeatDialog(){
        log("...showRepeatDialog()");
        RepeatDialog dialog = new RepeatDialog();
        dialog.setCallback(period -> {
            log("...onRepeat(Period)", period.toString());
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
            currentItem.setTargetTime(targetTime);
            currentAction.setTitle(targetTime.toString());
            actionAdapter.notifyDataSetChanged();
        }, hour, minutes, true);
        timePicker.show();
    }
    private void toggleTimer() {
        log("...toggleTimer()");
        switch (kronos.getState()) {
            case PENDING:
            case STOPPED:
                kronos.start(currentItem.getID());
                buttonTimer.setText(R.string.ui_pause);
                break;
            case RUNNING:
                kronos.pause();
                buttonTimer.setText(R.string.ui_resume);
                break;
            case PAUSED:
                kronos.resume();

                buttonTimer.setText(R.string.ui_pause);
        }
    }
    /**
     * update item and mental
     */
    private void updateItem() {
        log("...updateItem()");
/*        if(!validateInput()){
            return;
        }*/
        //currentItem = getItem();
        int rowsAffected = ItemsWorker.update(currentItem, getContext());
        if(rowsAffected != 1){
            Toast.makeText(getContext(), "error updating item", Toast.LENGTH_LONG).show();
            return;
        }else{
            ItemsWorker.touchParents(currentItem, getContext());
        }
        kronos.reset();
        //returnToCallingActivity();
    }
}