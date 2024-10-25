package se.curtrune.lucy.fragments;

import static se.curtrune.lucy.util.Logger.log;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.ColorPickerDialog;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Locale;
import java.util.Objects;

import se.curtrune.lucy.R;
import se.curtrune.lucy.app.Lucinda;
import se.curtrune.lucy.app.User;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.Mental;
import se.curtrune.lucy.classes.MentalStats;
import se.curtrune.lucy.classes.State;
import se.curtrune.lucy.dialogs.AddItemDialog;
import se.curtrune.lucy.dialogs.ChooseCategoryDialog;
import se.curtrune.lucy.dialogs.DurationDialog;
import se.curtrune.lucy.dialogs.MentalDialog;
import se.curtrune.lucy.dialogs.NotificationDialog;
import se.curtrune.lucy.dialogs.RepeatDialog;
import se.curtrune.lucy.item_settings.ItemSetting;
import se.curtrune.lucy.item_settings.ItemSettingAdapter;
import se.curtrune.lucy.util.Converter;
import se.curtrune.lucy.util.Kronos;
import se.curtrune.lucy.viewmodel.ItemSessionViewModel;
import se.curtrune.lucy.viewmodel.LucindaViewModel;
import se.curtrune.lucy.viewmodel.MentalViewModel;
import se.curtrune.lucy.workers.DurationWorker;
import se.curtrune.lucy.workers.ItemsWorker;
import se.curtrune.lucy.workers.MentalWorker;
import se.curtrune.lucy.workers.StatisticsWorker;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ItemSessionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ItemSessionFragment extends Fragment implements Kronos.Callback{

    private EditText editTextHeading;
    private  TextView textViewType;
    private TextView textViewDuration;
    private TextView textViewID;
    private TextView textViewCreated;
    private TextView textViewUpdated;
    private TextView textViewRepeat;
    private TextView textViewParentID;
    private TextView textViewHasChild;
    private TextView textViewTags;
    private TextView textViewColor;
    private LinearLayout layoutDev;
    private Button buttonSave;
    private Button buttonTimer;
    private CheckBox checkBoxIsDone;
    //private Action currentAction;
    private ItemSetting currentItemSetting;
    private SeekBar seekBarEnergy;
    private SeekBar seekBarAnxiety;
    private SeekBar seekBarStress;
    private SeekBar seekBarMood;
    private int startEnergy;
    private int endEnergy;
    private int startAnxiety;
    private int endAnxiety;
    private int startMood;
    private int endMood;
    private int startStress;
    private int endStress;
    private RecyclerView actionRecycler;
    private FloatingActionButton buttonAddItem;
    private Item currentItem;
    private LocalTime targetTime;
    private Kronos kronos;
    private LucindaViewModel lucindaViewModel;
    private ItemSessionViewModel itemSessionViewModel;
    private MentalViewModel mentalViewModel;
    private long duration;
    public static boolean VERBOSE = false;
    private ItemSettingAdapter itemSettingAdapter;

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
        log("ItemSessionFragment.onCreateView(LayoutInflater, ViewGroup, Bundle)");
        View view = inflater.inflate(R.layout.item_session_fragment, container, false);
        initComponents(view);
        initListeners();
        initKronos();
        initViewModel();
        //initActionRecycler();
        initItemSettingRecycler();
        initMental();
        setUserInterface(currentItem);
        return view;
    }
/*    private void goToCalendar(){
        log("...goToCalendar()");
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.navigationDrawer_frameContainer, new CalenderDateFragment()).commit();
    }*/
    private void initItemSettingRecycler(){
        log("....initItemSettingRecycler");
        itemSettingAdapter = new ItemSettingAdapter(itemSessionViewModel.getItemSettings(currentItem), new ItemSettingAdapter.Listener() {
            @Override
            public void onClick(ItemSetting setting) {
                log("...onClick(ItemSetting)", setting.toString());
                currentItemSetting = setting;
                switch (setting.getKey()){
                    case APPOINTMENT:
                        itemSessionViewModel.setIsEvent(setting.isChecked(), getContext());
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
                        currentItem.setState(setting.isChecked()? State.DONE: State.TODO);
                        break;
                    case TEMPLATE:
                        currentItem.setIsTemplate(setting.isChecked());
                        break;
                    case PRIORITIZED:
                        currentItem.setPriority(1);
                        break;
                    case TAGS:
                        showTagsDialog();
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
    private void initKronos(){
        if( VERBOSE) log("...initKronos()");
        kronos = Kronos.getInstance(this);
    }
    private void initMental(){
        log("...initMental()");
        seekBarStress.setProgress(itemSessionViewModel.getStress());
        seekBarMood.setProgress(itemSessionViewModel.getMood());
        seekBarEnergy.setProgress(itemSessionViewModel.getEnergy());
        seekBarAnxiety.setProgress(itemSessionViewModel.getAnxiety());

    }
    private void initViewModel(){
        if( VERBOSE) log("...initViewModel()");
        lucindaViewModel = new ViewModelProvider(requireActivity()).get(LucindaViewModel.class);
        itemSessionViewModel = new ViewModelProvider(requireActivity()).get(ItemSessionViewModel.class);
        itemSessionViewModel.init(currentItem);
        mentalViewModel = new ViewModelProvider(requireActivity()).get(MentalViewModel.class);
    }

    @Override
    public void onPause() {
        super.onPause();
        kronos.removeCallback();
        if( VERBOSE) log("MusicSessionActivity.onPause()");
    }
    @Override
    public void onResume() {
        super.onResume();
        //setButtonText();
        kronos.setCallback(this);
        if( VERBOSE) log("ItemSession.onResume()");
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
        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext());
        datePickerDialog.setOnDateSetListener((view, year, month, dayOfMonth) -> {
            log("DatePickerDialog.onDateSet(...)");
            LocalDate targetDate = LocalDate.of(year, month +1, dayOfMonth);
            log("...date", targetDate.toString());
            currentItemSetting.setValue(targetDate.toString());
            currentItem.setTargetDate(targetDate);
            itemSessionViewModel.update(currentItem, getContext());
            itemSettingAdapter.notifyDataSetChanged();
        });
        datePickerDialog.show();
    }

    private void initComponents(View view){
        if( VERBOSE) log("...initComponents()");
        //textViewEstimatedTime = view.findViewById(R.id.itemSessionFragment_estimatedTime);
        editTextHeading = view.findViewById(R.id.itemSessionFragment_heading);
        checkBoxIsDone = view.findViewById(R.id.itemSessionFragment_checkboxIsDone);
        actionRecycler = view.findViewById(R.id.itemSessionFragment_actionRecycler);
        buttonTimer = view.findViewById(R.id.itemSessionFragment_buttonTimer);
        textViewDuration = view.findViewById(R.id.itemSessionFragment_textViewDuration);
        //checkBoxIsCalenderItem = view.findViewById(R.id.itemSessionFragment_checkboxIsCalendarItem);
        buttonSave = view.findViewById(R.id.itemSessionFragment_buttonSave);
        textViewID = view.findViewById(R.id.itemSessionFragment_ID);
        textViewParentID = view.findViewById(R.id.itemSessionFragment_parentID);
        textViewHasChild = view.findViewById(R.id.itemSessionFragment_hasChild);
        textViewCreated = view.findViewById(R.id.itemSessionFragment_created);
        textViewUpdated = view.findViewById(R.id.itemSessionFragment_updated);
        layoutDev = view.findViewById(R.id.itemSessionFragment_layoutDev);
        textViewType = view.findViewById(R.id.itemSessionFragment_type);
        textViewTags = view.findViewById(R.id.itemSessionFragment_tags);
        textViewRepeat = view.findViewById(R.id.itemSessionFragment_repeat);
        buttonAddItem = view.findViewById(R.id.itemSessionFragment_buttonAdd);
        textViewColor = view.findViewById(R.id.itemSessionFragment_color);
        seekBarEnergy = view.findViewById(R.id.itemSessionFragment_seekBarEnergy);
        seekBarStress = view.findViewById(R.id.itemSessionFragment_seekBarStress);
        seekBarAnxiety = view.findViewById(R.id.itemSessionFragment_seekBarAnxiety);
        seekBarMood = view.findViewById(R.id.itemSessionFragment_seekBarMood);
    }
    private void initListeners(){
        if( VERBOSE) log("...initListeners()");
        buttonTimer.setOnClickListener(view->toggleTimer());
        buttonSave.setOnClickListener(view->updateItem());
        textViewDuration.setOnClickListener(view -> showDurationDialogActual());
        buttonAddItem.setOnClickListener(view -> showAddChildItemDialog());
        seekBarEnergy.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                log("....seekbar onProgressChanged()", progress);
                if( fromUser) {
                    lucindaViewModel.setEnergy(progress - 5);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                startEnergy = seekBar.getProgress();
                log("...startEnergy", startEnergy);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                log("...onStopTrackingTouch(SeekBar)", seekBar.getProgress());
                endEnergy = seekBar.getProgress();
                int energyChange = endEnergy - startEnergy;
                log("energy changed", energyChange);
                itemSessionViewModel.updateEnergy(energyChange, getContext());
                //lucindaViewModel.updateEnergy(seekBar.getProgress(), getContext());

            }
        });
        seekBarAnxiety.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser) {
                    //lucindaViewModel.estimateAnxiety(progress - 5, getContext());
                    lucindaViewModel.setAnxiety(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                log("seekBarAnxiety on startTracking(SeekBar)", seekBar.getProgress());
                startAnxiety = seekBar.getProgress();
                //lucindaViewModel.setAnxiety(seekBar.getProgress());
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                log("seekBarAnxiety.onStopTrackingTouch(SeekBar)", seekBar.getProgress());
                endAnxiety = seekBar.getProgress();
                int changeAnxiety = endAnxiety - startAnxiety;
                itemSessionViewModel.updateAnxiety(changeAnxiety, getContext());

            }
        });
        seekBarMood.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    lucindaViewModel.estimateMood(progress -5, getContext());
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                itemSessionViewModel.updateMood(seekBar.getProgress(), getContext());
            }
        });
        seekBarStress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    lucindaViewModel.estimateStress( progress - 1, getContext());
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                itemSessionViewModel.updateStress(seekBar.getProgress(), getContext());
            }
        });
    }
    private void returnToPreviousFragment(){
        log("...returnToPreviousFragment()");
        getParentFragmentManager().popBackStackImmediate();

    }
/*    private void setEstimatedTime(Item item){
        if( VERBOSE)log("...setEstimatedTime()");
        long estimatedDuration = DurationWorker.getEstimatedDuration(item, getContext());
        String stringEstimatedDuration = String.format(Locale.getDefault(), "estimated duration %s", Converter.formatSecondsWithHours(estimatedDuration));
        log("..." , stringEstimatedDuration);
        //textViewEstimatedTime.setText(stringEstimatedDuration);
    }*/
    private void setUserInterface(Item item){
        if( VERBOSE) log("...setUserInterface(Item)");
        editTextHeading.setText(item.getHeading());
        checkBoxIsDone.setChecked(item.isDone());
        textViewDuration.setText(Converter.formatSecondsWithHours(item.getDuration()));
        log("Lucinda.Dev ", Lucinda.Dev);
        if(User.isDevMode(getContext())){
            setUserInterfaceDev(item);
        }
    }
/*    private void setEstimatedEnergy(Item item){
        if( VERBOSE) log("...setEstimatedEnergy(Item)");
        MentalStats stats = StatisticsWorker.getMentalStats(item, getContext());
        String stringEstimatedEnergy = String.format(Locale.getDefault(), "estimated energy %s", stats.getEnergy());
        log("...", stringEstimatedEnergy);
        //textViewEstimatedEnergy.setText(stringEstimatedEnergy);
    }*/

    /**
     * everything about the item, things that are interesting to me, but not for you average user
     */
    private void setUserInterfaceDev(Item item){
        log("...setUserInterfaceDev()");
        layoutDev.setVisibility(View.VISIBLE);
        String stringID = String.format(Locale.getDefault(), "id: %d", item.getID());
        String stringParentID = String.format(Locale.getDefault(), "parent id: %d", item.getParentId());
        String stringHasChild = String.format(Locale.getDefault(), "has child: %b", item.hasChild());
        String stringCreated = String.format(Locale.getDefault(), "created: %s", Converter.format(item.getCreated()));
        String stringUpdated = String.format(Locale.getDefault(), "updated: %s", Converter.format(item.getUpdated()));
        String stringType = String.format(Locale.getDefault(), "type: %s", item.getType().toString());
        String stringTags = String.format(Locale.getDefault(), "tags: %s", item.getTags());
        String stringRepeat = String.format(Locale.getDefault(), "repeat: %b", item.hasPeriod());
        String stringColor = String.format(Locale.getDefault(),"color; %d", item.getColor());
        textViewID.setText(stringID);
        textViewParentID.setText(stringParentID);
        textViewCreated.setText(stringCreated);
        textViewUpdated.setText(stringUpdated);
        textViewHasChild.setText(stringHasChild);
        textViewType.setText(stringType);
        textViewTags.setText(stringTags);
        textViewRepeat.setText(stringRepeat);
        textViewColor.setText(stringColor);
    }

    private void showAddChildItemDialog(){
        log("...showAddChildItemDialog()");
        AddItemDialog dialog = new AddItemDialog(currentItem, false);
        dialog.setCallback(item -> {
            log("AddItemDialog.onAddItem(Item)");
            item = ItemsWorker.insertChild(currentItem, item, getContext());
            if( VERBOSE ) log(item);
            returnToPreviousFragment();
        });
        dialog.show(requireActivity().getSupportFragmentManager(), "add child");

    }
    private void showCategoryDialog(){
        log("...showCategoryDialog()");
        ChooseCategoryDialog dialog = new ChooseCategoryDialog(currentItem.getCategory());
        dialog.setCallback(category -> {
            log("...onSelected(String)", category);
            currentItemSetting.setValue(category);
            itemSessionViewModel.setCategory(category, getContext());
            itemSettingAdapter.notifyDataSetChanged();
        });
        dialog.show(getChildFragmentManager(), "choose category");
    }

    private void showColorDialog(){
        log("...showColorDialog()");
        //newItem.setColor(Color.RED);
        new ColorPickerDialog.Builder(getContext())
                .setTitle("ColorPicker Dialog")
                .setPreferenceName("MyColorPickerDialog")
                .setPositiveButton(getString(R.string.ok),
                        new ColorEnvelopeListener() {
                            @Override
                            public void onColorSelected(ColorEnvelope envelope, boolean fromUser) {
                                log("...onColorSelected(ColorEnvelope, boolean)");
                                //currentItemSetting.setColor(envelope.getColor());
                                currentItemSetting.setValue(String.valueOf(envelope.getColor()));
                                currentItem.setColor(envelope.getColor());
                                itemSettingAdapter.notifyDataSetChanged();
                            }
                        })
                .setNegativeButton(getString(R.string.dismiss),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                .attachAlphaSlideBar(true) // the default value is true.
                .attachBrightnessSlideBar(true)  // the default value is true.
                .setBottomSpace(12) // set a bottom space between the last slidebar and buttons.
                .show();
    }
    public void showDurationDialog(){
        log("...showDurationDialog()");
        DurationDialog dialog = new DurationDialog();
        dialog.setCallback(duration -> {
            log("...onDurationDialog(Duration)");
            currentItem.setEstimatedDuration(duration.getSeconds());
            currentItemSetting.setValue(Converter.formatSecondsWithHours(duration.getSeconds()));
            itemSettingAdapter.notifyDataSetChanged();
        });
        dialog.show(getChildFragmentManager(), "duration");

    }
    private void showDurationDialogActual(){
        log("...showDurationDialog()");
        DurationDialog dialog = new DurationDialog();
        dialog.setCallback(duration -> {
            log("...onDurationDialog(Duration)");
            log("..onDurationDialog(Duration duration)", duration.toString());
            log("...seconds", duration.getSeconds());
            this.duration = duration.getSeconds();
            currentItem.setDuration(duration.getSeconds());
            textViewDuration.setText(Converter.formatSecondsWithHours(duration.getSeconds()));
            buttonTimer.setText(getString(R.string.ui_resume));
            itemSessionViewModel.setDuration(duration, getContext());
            kronos.setElapsedTime(duration.getSeconds());
        });
        dialog.show(getChildFragmentManager(), "actual duration");
    }
    public void showMentalDialog(){
        log("...showMentalDialog()");
        Mental mental = MentalWorker.getMental(currentItem, getContext());
        MentalDialog dialog = new MentalDialog(mental);
        dialog.setCallback((mental1, mode) -> {
            log("...onMental(MentalType, MentalType)", mode.toString());
            log("should only be mode edit");
            currentItem.setMental(mental1);
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
            currentItemSetting.setValue(notification.toString());
            currentItem.setNotification(notification);
            itemSettingAdapter.notifyDataSetChanged();
        });
        dialog.show(getChildFragmentManager(), "notification ");

    }
    private void showRepeatDialog(){
        log("...showRepeatDialog()");
        RepeatDialog dialog = new RepeatDialog();
        dialog.setCallback(repeat -> {
            log("...onRepeat(Unit)", repeat.toString());
            currentItemSetting.setValue(repeat.toString());
            currentItem.setRepeat(repeat);
            currentItem.setIsTemplate(true);
            itemSessionViewModel.update(currentItem, getContext());
            itemSettingAdapter.notifyDataSetChanged();
        });
        dialog.show(getChildFragmentManager(), "repeat");

    }
    private void showTagsDialog(){
        log("...showTagsDialog()");
        Toast.makeText(getContext(), "NOT IMPLEMENTED", Toast.LENGTH_LONG).show();

    }
    private void showTimeDialog(){
        log("...showTimeDialog()");
        LocalTime now = LocalTime.now();
        int minutes = now.getMinute();
        int hour = now.getHour();
        TimePickerDialog timePicker = new TimePickerDialog(getContext(), (view, hourOfDay, minute) -> {
            targetTime = LocalTime.of(hourOfDay, minute);
            currentItemSetting.setValue(targetTime.toString());
            itemSessionViewModel.setTargetTime(targetTime, getContext());
            itemSettingAdapter.notifyDataSetChanged();
        }, hour, minutes, true);
        timePicker.show();
    }
    private void toggleTimer() {
        log("...toggleTimer()", kronos.getState().toString());
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
    private Item getItem(){
        log("...getItem()");
        currentItem.setHeading(editTextHeading.getText().toString());
        currentItem.setState(checkBoxIsDone.isChecked() ? State.DONE: State.TODO);
        //currentItem.setIsCalenderItem(checkBoxIsCalenderItem.isChecked());
        //currentItem.setIsTemplate(checkBoxTemplate.isChecked());
        currentItem.setDuration(duration);
/*        if( checkBoxAppointment.isChecked()) {
            currentItem.setType(Type.APPOINTMENT);
        }else{
            currentItem.setType(Type.NODE);
        }*/
        return currentItem;
    }
    /**
     * update item and mental
     */
    private void updateItem() {
        log("...updateItem()", currentItem.getHeading());
        if( !validate()){
            log("....item did not validate, i surrender");
            return;
        }
        currentItem = getItem();
        log(currentItem);
        int rowsAffected = ItemsWorker.update(currentItem, getContext());
        if(rowsAffected != 1){
            Toast.makeText(getContext(), "error updating item", Toast.LENGTH_LONG).show();
            return;
        }else{
            ItemsWorker.touchParents(currentItem, getContext());
        }

        lucindaViewModel.updateEnergy(true);
        requireActivity().getSupportFragmentManager().popBackStackImmediate();
        kronos.reset();
    }
    private boolean validate(){
        if( VERBOSE) log("...validate");
        if( editTextHeading.getText().toString().isEmpty()){
            Toast.makeText(getContext(), getString(R.string.missing_heading), Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}