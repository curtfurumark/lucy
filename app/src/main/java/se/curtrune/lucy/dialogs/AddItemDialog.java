package se.curtrune.lucy.dialogs;

import static se.curtrune.lucy.util.Logger.log;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.skydoves.colorpickerview.ColorPickerDialog;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import se.curtrune.lucy.R;
import se.curtrune.lucy.classes.Action;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.Mental;
import se.curtrune.lucy.classes.Notification;
import se.curtrune.lucy.classes.State;
import se.curtrune.lucy.item_settings.ItemSetting;
import se.curtrune.lucy.item_settings.ItemSettingAdapter;
import se.curtrune.lucy.util.Converter;
import se.curtrune.lucy.screens.item_editor.ItemSessionViewModel;


public class AddItemDialog extends BottomSheetDialogFragment {
    private EditText editText_heading;
    private TextView textViewParentList;
    private Button buttonSave;
    private Button buttonDismiss;

    private SeekBar seekBarEnergy;
    private SeekBar seekBarAnxiety;
    private SeekBar seekBarStress;
    private SeekBar seekBarMood;
    private String category;
    private LocalTime targetTime;
    private LocalDate targetDate;
    //private ActionAdapter actionAdapter;
    private ItemSettingAdapter itemSettingAdapter;
    private RecyclerView actionRecycler;
    private ItemSetting currentItemSetting;
    //private Action currentAction;
    public static boolean VERBOSE = false;
    private final Item parent;
    private Item newItem;
    private Mental mental;
    public interface Callback{
        void onAddItem(Item item);
    }
    private Callback listener;

    public AddItemDialog(Item parent, boolean isCalenderItem) {
        log("AddItemDialog(Item parent, boolean isCalenderItem)");
        assert  parent != null;
        this.parent = parent;
        this.newItem = createNewItem(parent);
        this.newItem.setTargetDate(LocalDate.ofEpochDay(0));
    }
    public AddItemDialog(Item parent, LocalDate targetDate){
        assert parent != null;
        this.parent = parent;
        this.newItem = createNewItem(parent);
        this.newItem.setTargetDate(targetDate);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        log("AddItemDialog.onCreateView(...)");
        View view = inflater.inflate(R.layout.add_item_dialog, container, false);
        initComponents(view);
        initItemSettingsRecycler();
        initListeners();
        initUserInterface(parent);
        return view;
    }
    private Item createNewItem(Item parent){
        if( VERBOSE) log("...createNewItem(Item)");
        newItem = new Item();
        newItem.setParentId(parent.getID());
        newItem.setCategory(parent.getCategory());
        return newItem;
    }
    private List<Action> getActionList(){
        if( VERBOSE)log("...getActionList()");
        Action time = new Action();
        time.setTitle(getString(R.string.time));
        time.setType(Action.Type.TIME);

        Action notification = new Action();
        notification.setTitle(getString(R.string.notification));
        notification.setType(Action.Type.NOTIFICATION);

        Action dateAction = new Action(getString(R.string.date));
        if( targetDate != null) {
            dateAction.setValue(targetDate.toString());
        }
        dateAction.setType(Action.Type.DATE);

        Action repeat = new Action();
        repeat.setTitle(getString(R.string.repeat));
        repeat.setType(Action.Type.REPEAT);

        Action categoryAction = new Action();
        if( category != null && !category.isEmpty()){
            categoryAction.setValue(category);
        }
        categoryAction.setTitle(getString(R.string.category));
        categoryAction.setType(Action.Type.CATEGORY);

        Action actionDuration = new Action();
        actionDuration.setTitle(getString(R.string.duration));
        actionDuration.setType(Action.Type.DURATION);

        Action actionMental = new Action();
        actionMental.setTitle(getString(R.string.mental));
        actionMental.setType(Action.Type.MENTAL);

        Action actionColor = new Action("COLOR");
        actionColor.setType(Action.Type.COLOR);

        ArrayList<Action> actionList = new ArrayList<>();
        actionList.add(time);
        actionList.add(dateAction);
        actionList.add(repeat);
        actionList.add(notification);
        actionList.add(categoryAction);
        actionList.add(actionDuration);
        actionList.add(actionMental);
        actionList.add(actionColor);
        return actionList;
    }

    private Item getItem(){
        if( VERBOSE) log("...getItem()");
        newItem.setHeading(editText_heading.getText().toString());
        newItem.setTags(parent.getTags());
        //newItem.setTargetDate(targetDate);
        return newItem;

    }

    private void initComponents(View view){
        if( VERBOSE) log("...initComponents(View)");
        editText_heading = view.findViewById(R.id.addItemDialog_heading);
        buttonSave = view.findViewById(R.id.addItemDialog_buttonOK);
        buttonDismiss = view.findViewById(R.id.addItemDialog_buttonDismiss);
        actionRecycler = view.findViewById(R.id.addItemDialog_actionRecycler);
        textViewParentList = view.findViewById(R.id.addItemDialog_parentList);
        seekBarAnxiety = view.findViewById(R.id.addItemDialog_seekBarAnxiety);
        seekBarEnergy = view.findViewById(R.id.addItemDialog_seekBarEnergy);
        seekBarMood = view.findViewById(R.id.addItemDialog_seekBarMood);
        seekBarStress = view.findViewById(R.id.addItemDialog_seekBarStress);
    }
    private void initItemSettingsRecycler(){
        log("...initItemSettingsRecycler()");
        log("....initItemSettingRecycler");
        ItemSessionViewModel itemSessionViewModel = new ViewModelProvider(requireActivity()).get(ItemSessionViewModel.class);
        itemSettingAdapter = new ItemSettingAdapter(itemSessionViewModel.getItemSettings(newItem, getContext()), new ItemSettingAdapter.Listener() {
            @Override
            public void onClick(ItemSetting setting) {
                log("...onClick(ItemSetting)", setting.toString());
                currentItemSetting = setting;
                switch (setting.getKey()){
                    case IS_CALENDAR_ITEM:
                        newItem.setIsCalenderItem(setting.isChecked());
                        break;
                    case CATEGORY:
                        showCategoryDialog();
                        break;
                    case COLOR:
                        showColorDialog();
                        break;
                    case NOTIFICATION:
                        showNotificationDialog();
                        break;
                    case REPEAT:
                        showRepeatDialog();
                        break;
                    case TIME:
                        showTimeDialog();
                        break;
                    case DATE:
                        showDateDialog();
                        break;
                    case DONE:
                        newItem.setState(setting.isChecked()? State.DONE: State.TODO);
                        break;
                    case TEMPLATE:
                        newItem.setIsTemplate(setting.isChecked());
                        break;
                    case PRIORITIZED:
                        newItem.setPriority(1);
                        break;
                    case TAGS:
                        //showTagsDialog();
                        break;

                }
            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        actionRecycler.setLayoutManager(layoutManager);
        actionRecycler.setItemAnimator(new DefaultItemAnimator());
        actionRecycler.setAdapter(itemSettingAdapter);
        itemSettingAdapter.notifyDataSetChanged();

    }

    private void initListeners(){
        if( VERBOSE) log("...initListeners()");
        buttonSave.setOnClickListener(view1 -> {
            log("...buttonSave.onClick()");
            if( !validateInput()){
                return;
            }
            Item item = getItem();
            if( VERBOSE)log(item);
            listener.onAddItem(item);
            dismiss();
        });
        buttonDismiss.setOnClickListener(view->dismiss());
        seekBarAnxiety.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBarEnergy.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBarMood.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBarStress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


    private void initUserInterface(Item parentItem){
        log("...initUserInterface(Item)");
        String strParentList = String.format(Locale.getDefault(), "%s: %s",getString(R.string.add_to_list), parentItem.getHeading());
        textViewParentList.setText(strParentList);
        //checkBoxCalendarEvent.setChecked(isCalenderItem);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }
    public void setCallback( Callback callback){
        this.listener = callback;
    }


    public void setCategory(String category){
        this.category = category;
    }
    public void setTargetDate(LocalDate targetDate){
        log("AddItemDialog.setTargetDate(LocalDate)");
        this.targetDate = targetDate;
    }

    private void showCategoryDialog(){
        log("...showCategoryDialog()");
        ChooseCategoryDialog dialog = new ChooseCategoryDialog(parent.getCategory());
        dialog.setCallback(category -> {
            log("...onSelected(String category)", category);
            currentItemSetting.setValue(category);
            newItem.setCategory(category);
            itemSettingAdapter.notifyDataSetChanged();
        });
        dialog.show(getChildFragmentManager(), "choose category");
    }
    private void showColorDialog(){
        log("...showColorDialog()");
        Toast.makeText(getContext(), "COLOR DIALOG", Toast.LENGTH_LONG).show();
        newItem.setColor(Color.RED);
        new ColorPickerDialog.Builder(getContext())
                .setTitle("ColorPicker Dialog")
                .setPreferenceName("MyColorPickerDialog")
                .setPositiveButton(getString(R.string.ok),
                        (ColorEnvelopeListener) (envelope, fromUser) -> {
                            log("...onColorSelected(ColorEnvelope, boolean)");
/*                            currentAction.setColor(envelope.getColor());
                            currentAction.setValue(String.valueOf(envelope.getColor()));*/
                            newItem.setColor(envelope.getColor());
                            itemSettingAdapter.notifyDataSetChanged();
                        })
                .setNegativeButton(getString(R.string.dismiss),
                        (dialogInterface, i) -> dialogInterface.dismiss())
                .attachAlphaSlideBar(true) // the default value is true.
                .attachBrightnessSlideBar(true)  // the default value is true.
                .setBottomSpace(12) // set a bottom space between the last slide bar and buttons.
                .show();
    }
    public void showDateDialog() {
        log("...showDateDialog()");
        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext());
        datePickerDialog.setOnDateSetListener((view, year, month, dayOfMonth) -> {
            log("DatePickerDialog.onDateSet(...)");
            targetDate = LocalDate.of(year, month + 1, dayOfMonth);
            newItem.setTargetDate(targetDate);
            currentItemSetting.setValue(targetDate.toString());
            itemSettingAdapter.notifyDataSetChanged();
        });
        datePickerDialog.show();
    }
    public void showDurationDialog(){
        log("...showDurationDialog()");
        DurationDialog dialog = new DurationDialog(Duration.ofSeconds(0), new DurationDialog.Callback() {
            @Override
            public void onDurationDialog(Duration duration) {
                log("...onDurationDialog(Duration)", duration.getSeconds());
                newItem.setEstimatedDuration(duration.getSeconds());
                currentItemSetting.setValue(Converter.formatSecondsWithHours(duration.getSeconds()));
                itemSettingAdapter.notifyDataSetChanged();
            }
        });
        dialog.show(getChildFragmentManager(), "duration");
    }

    /**
     * shows the MentalDialog and if mental created replies with a new mental, not saved to db
     * cannot save until, the item has been saved
     */

    private void showMentalDialog(){
        if(VERBOSE) log("...showMentalDialog()");
        MentalDialog dialog = new MentalDialog();
        dialog.setCallback((mental, mode) -> {
            log("...onMental(MentalType, Mode)", mode.toString());
            log(mental);
            //newItem.setMental( mental);
        });
        dialog.show(getChildFragmentManager(), "add mental");
    }
    private void showNotificationDialog(){
        if( VERBOSE) log("...showNotificationDialog()");
        NotificationDialog dialog = new NotificationDialog(parent);
        dialog.setListener(new NotificationDialog.Callback() {
            @Override
            public void onNotification(Notification notification, NotificationDialog.Action action) {
                log("...onNotification(Notification, Action )", notification.toString());
                switch (action){
                    case INSERT:
                        currentItemSetting.setValue(notification.toString());
                        newItem.setNotification(notification);
                        itemSettingAdapter.notifyDataSetChanged();
                        break;
                    case DELETE:
                        Toast.makeText(getContext(), "delete not implemented", Toast.LENGTH_LONG).show();
                        break;
                    case EDIT:
                        Toast.makeText(getContext(), "edit not implemented", Toast.LENGTH_LONG).show();
                        break;

                }

            }
        });
        dialog.show(getChildFragmentManager(), "notification/alarm ");

    }
    private void showRepeatDialog(){
        log("...showRepeatDialog()");
        RepeatDialog dialog = new RepeatDialog();
        dialog.setCallback(repeat -> {
            log("...onRepeat(Unit)", repeat.toString());
            currentItemSetting.setValue(repeat.toString());
            newItem.setRepeat(repeat);
            itemSettingAdapter.notifyDataSetChanged();
            log(repeat);
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
            newItem.setTargetTime(targetTime);
            currentItemSetting.setValue(targetTime.toString());
            itemSettingAdapter.notifyDataSetChanged();
        }, hour, minutes, true);
        timePicker.show();
    }
    private boolean validateInput(){
        if(editText_heading.getText().toString().isEmpty()){
            Toast.makeText(getContext(), R.string.missing_heading, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}
