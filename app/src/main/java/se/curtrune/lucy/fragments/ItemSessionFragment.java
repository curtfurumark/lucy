package se.curtrune.lucy.fragments;

import static android.app.Activity.RESULT_OK;
import static se.curtrune.lucy.util.Logger.log;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.ColorPickerDialog;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.Locale;

import se.curtrune.lucy.R;
import se.curtrune.lucy.app.Lucinda;
import se.curtrune.lucy.app.User;
import se.curtrune.lucy.classes.Media;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.Mental;
import se.curtrune.lucy.classes.State;
import se.curtrune.lucy.dialogs.ChooseChildTypeDialog;
import se.curtrune.lucy.dialogs.AddItemDialog;
import se.curtrune.lucy.dialogs.ChooseCategoryDialog;
import se.curtrune.lucy.dialogs.DurationDialog;
import se.curtrune.lucy.dialogs.NotificationDialog;
import se.curtrune.lucy.dialogs.RepeatDialog;
import se.curtrune.lucy.dialogs.TagsDialog;
import se.curtrune.lucy.item_settings.ItemSetting;
import se.curtrune.lucy.item_settings.ItemSettingAdapter;
import se.curtrune.lucy.util.Constants;
import se.curtrune.lucy.util.Converter;
import se.curtrune.lucy.util.Kronos;
import se.curtrune.lucy.viewmodel.ItemSessionViewModel;
import se.curtrune.lucy.viewmodel.LucindaViewModel;
import se.curtrune.lucy.viewmodel.MentalViewModel;
import se.curtrune.lucy.workers.ItemsWorker;

public class ItemSessionFragment extends Fragment implements Kronos.Callback{

    private static final int CAMERA_REQUEST_CODE = 101;
    private EditText editTextHeading;
    private EditText editTextComment;
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
    private TextView textViewAnxiety;
    private TextView textViewStress;
    private TextView textViewMood;
    private TextView textViewEnergy;
    private LinearLayout layoutDev;
    private Button buttonSave;
    private Button buttonTimer;
    private CheckBox checkBoxIsDone;
    private ItemSetting currentItemSetting;
    private SeekBar seekBarEnergy;
    private SeekBar seekBarAnxiety;
    private SeekBar seekBarStress;
    private SeekBar seekBarMood;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        log("ItemSessionFragment.onCreateView(LayoutInflater, ViewGroup, Bundle)");
        View view = inflater.inflate(R.layout.item_session_fragment, container, false);
        initViewModel();
        initComponents(view);
        initListeners();
        initItemSettingRecycler();
        setUserInterface(currentItem);
        return view;
    }
    private String formatMental(String label, int value){
        return String.format(Locale.getDefault(), "%s: %d",label, value);
    }
    private void initItemSettingRecycler(){
        log("....initItemSettingRecycler");
        itemSettingAdapter = new ItemSettingAdapter(itemSessionViewModel.getItemSettings(currentItem, getContext()), new ItemSettingAdapter.Listener() {
            @Override
            public void onClick(ItemSetting setting) {
                log("...onClick(ItemSetting)", setting.toString());
                currentItemSetting = setting;
                switch (setting.getKey()){
                    case IS_CALENDAR_ITEM:
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
                        itemSessionViewModel.setDone(setting.isChecked());
                        currentItem.setState(setting.isChecked()? State.DONE: State.TODO);
                        break;
                    case TEMPLATE:
                        itemSessionViewModel.setIsTemplate(setting.isChecked(), getContext());
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
        log(itemSessionViewModel.getMental());
        seekBarStress.setProgress(itemSessionViewModel.getStress() + Constants.STRESS_OFFSET);
        setMentalLabel(itemSessionViewModel.getStress(), Mental.Type.STRESS);
        seekBarMood.setProgress(itemSessionViewModel.getMood() + Constants.MOOD_OFFSET);
        setMentalLabel(itemSessionViewModel.getMood(), Mental.Type.MOOD);
        seekBarEnergy.setProgress(itemSessionViewModel.getEnergy() + Constants.ENERGY_OFFSET);
        setMentalLabel(itemSessionViewModel.getEnergy(), Mental.Type.ENERGY);
        seekBarAnxiety.setProgress(itemSessionViewModel.getAnxiety() +  Constants.ANXIETY_OFFSET);
        setMentalLabel(itemSessionViewModel.getAnxiety(), Mental.Type.ANXIETY);

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


    private void initComponents(View view){
        if( VERBOSE) log("...initComponents()");
        editTextHeading = view.findViewById(R.id.itemSessionFragment_heading);
        editTextComment = view.findViewById(R.id.itemSessionFragment_comment);
        checkBoxIsDone = view.findViewById(R.id.itemSessionFragment_checkboxIsDone);
        actionRecycler = view.findViewById(R.id.itemSessionFragment_actionRecycler);
        buttonTimer = view.findViewById(R.id.itemSessionFragment_buttonTimer);
        textViewDuration = view.findViewById(R.id.itemSessionFragment_textViewDuration);
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
        textViewAnxiety = view.findViewById(R.id.itemSessionFragment_labelAnxiety);
        textViewEnergy = view.findViewById(R.id.itemSessionFragment_labelEnergy);
        textViewMood = view.findViewById(R.id.itemSessionFragment_labelMood);
        textViewStress = view.findViewById(R.id.itemSessionFragment_labelStress);

    }
    private void initListeners(){
        if( VERBOSE) log("...initListeners()");
        buttonTimer.setOnClickListener(view->toggleTimer());
        buttonSave.setOnClickListener(view->updateItem());
        textViewDuration.setOnClickListener(view -> showDurationDialogActual());
        buttonAddItem.setOnClickListener(view -> showChooseChildTypeDialog(currentItem));
        seekBarEnergy.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                log("....seekbar onProgressChanged()", progress);
                if( fromUser) {
                    //int energyChange = progress - startEnergy;
                    if(currentItem.isDone()){
                        itemSessionViewModel.update(currentItem, getContext());
                    }else{
                        lucindaViewModel.estimateEnergy(progress - Constants.ENERGY_OFFSET);
                        setMentalLabel(progress - Constants.ENERGY_OFFSET, Mental.Type.ENERGY);
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar){
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                log("...onStopTrackingTouch(SeekBar)", seekBar.getProgress());
                itemSessionViewModel.updateEnergy(seekBar.getProgress(), getContext());

            }
        });
        seekBarAnxiety.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser) {
                    setMentalLabel(progress - Constants.ANXIETY_OFFSET, Mental.Type.ANXIETY);
                    lucindaViewModel.estimateAnxiety(progress - Constants.ANXIETY_OFFSET, getContext());
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                log("seekBarAnxiety on startTracking(SeekBar)", seekBar.getProgress());
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                log("seekBarAnxiety.onStopTrackingTouch(SeekBar)", seekBar.getProgress());
                itemSessionViewModel.updateAnxiety(seekBar.getProgress(), getContext());

            }
        });
        seekBarMood.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    lucindaViewModel.estimateMood(progress - Constants.MOOD_OFFSET, getContext());
                    setMentalLabel(progress - Constants.MOOD_OFFSET, Mental.Type.MOOD);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                itemSessionViewModel.updateMood(seekBar.getProgress() , getContext());
            }
        });
        seekBarStress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    lucindaViewModel.estimateStress( progress - Constants.STRESS_OFFSET , getContext());
                    setMentalLabel(progress - Constants.STRESS_OFFSET, Mental.Type.STRESS);
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
        itemSessionViewModel.getError().observe(requireActivity(), new Observer<String>() {
            @Override
            public void onChanged(String error) {
                log("...getError(String)", error);
                Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
            }
        });
    }
    private void returnToPreviousFragment(){
        log("...returnToPreviousFragment()");
        getParentFragmentManager().popBackStackImmediate();
    }
    private void setKronos(long duration){
        log("...setKronos(long)", duration);
        kronos = Kronos.getInstance(this);
        if(duration > 0){
            kronos.setElapsedTime(duration);
            buttonTimer.setText(getString(R.string.ui_resume));
        }

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
        editTextComment.setText(item.getComment());
        checkBoxIsDone.setChecked(item.isDone());
        textViewDuration.setText(Converter.formatSecondsWithHours(item.getDuration()));
        log("Lucinda.Dev ", Lucinda.Dev);
        if(User.isDevMode(getContext())){
            setUserInterfaceDev(item);
        }
        if( !item.isDone()){
            lucindaViewModel.estimateEnergy(item.getEnergy());
        }
        setKronos(item.getDuration());
        initMental();
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
    private void showChooseChildTypeDialog(Item item){
        log("...showChooseChildTypeDialog(Item)");
        ChooseChildTypeDialog dialog = new ChooseChildTypeDialog(item, new ChooseChildTypeDialog.Listener() {
            @Override
            public void onClick(ChooseChildTypeDialog.ChildType childType) {
                log("...onClick(ChildType)", childType.toString());
                switch (childType){
                    case CHILD:
                        showAddChildItemDialog();
                        break;
                    case LIST:
                        lucindaViewModel.updateFragment(new EditableListFragment(currentItem));
                        break;
                    case PHOTOGRAPH:
                        //Toast.makeText(getContext(), "NOT IMPLEMENTED", Toast.LENGTH_LONG).show();
                        //takePhoto();
                        openCameraAndSaveImage();
                        break;
                }
            }
        });
        dialog.show(getChildFragmentManager(), "choose type");
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
        TagsDialog dialog = new TagsDialog(new TagsDialog.Callback() {
            @Override
            public void onTags(String tags) {
                log("...onTags(String)", tags);
                itemSessionViewModel.addTags(tags, getContext());
                //itemSessionViewModel.update();
            }
        });
        dialog.show(getChildFragmentManager(), "add edit tags");
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
        currentItem.setComment(editTextComment.getText().toString());
        currentItem.setState(checkBoxIsDone.isChecked() ? State.DONE: State.TODO);
        currentItem.setDuration(duration);
        return currentItem;
    }
    /**
     * update item
     */
    private void updateItem() {
        log("...updateItem()", currentItem.getHeading());
        if( !validate()){
            log("....item did not validate, i surrender, missing heading?");
            return;
        }
        currentItem = getItem();
        itemSessionViewModel.update(currentItem, getContext());
/*        log(currentItem);
        int rowsAffected = ItemsWorker.update(currentItem, getContext());
        if(rowsAffected != 1){
            Toast.makeText(getContext(), "error updating item", Toast.LENGTH_LONG).show();
            return;
        }else{
            ItemsWorker.touchParents(currentItem, getContext());
        }*/

        lucindaViewModel.updateEnergy(true);
        requireActivity().getSupportFragmentManager().popBackStackImmediate();
        kronos.reset();
    }
    private void setMentalLabel(int energy, Mental.Type type){
        if( VERBOSE) log("...setMentalLabel(int)", energy);
        switch(type) {
            case ENERGY:
                textViewEnergy.setText(formatMental(getString(R.string.energy), energy));
                break;
            case  ANXIETY:
                textViewAnxiety.setText(formatMental(getString(R.string.anxiety), energy));
                break;
            case STRESS:
                textViewStress.setText(formatMental(getString(R.string.stress), energy));
                break;
            case MOOD:
                textViewMood.setText(formatMental(getString(R.string.mood), energy));
                break;
        }
    }
    private void takePhoto(){
        log("...takePhoto()");
        askCameraPermission();
    }
    private void openCameraAndSaveImage(){
        log("...openCameraAndSaveImage()");
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = null;
        try {
            photoFile = createImageFile();
            log("...photoFile path", photoFile.getAbsolutePath());
        } catch (IOException e) {
            log("EXCEPTION occurred creating image file");
            e.printStackTrace();
            return;
        }
        if( photoFile == null){
            log("ERROR no photoFile created");
            return;
        }
        Uri photoUri = FileProvider.getUriForFile(getContext(), "curtfurumark.se", photoFile);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        cameraIntent.putExtra(Constants.IMAGE_FILE_PATH, photoFile.getAbsolutePath());
        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
    }
    private void askCameraPermission(){
        log("...askCameraPermissionU()");
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            log("...camera permission not granted");
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);

        } else {
            //Toast.makeText(getContext(), "YOU GOT CAMERA PERMISSION", Toast.LENGTH_SHORT).show();
            //dispatchTakePictureIntent();
            openCamera();
            //openCameraAndSaveImage();
        }
    }
    private void openCamera() {
        log("...openCamera()");
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
/*        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException e) {
            log("EXCEPTION occurred creating image file");
            e.printStackTrace();
            return;
        }
        if( photoFile == null){
            log("ERROR no photoFile created");
            return;
        }
        Uri photoUri = FileProvider.getUriForFile(getContext(), "curtfurumark.se", photoFile);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);*/
        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
    }
    private File createImageFile() throws IOException {
        log("...createImageFile()");
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        String currentPhotoPath = image.getAbsolutePath();
        return image;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        log("...onActivityResult(...)");
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CAMERA_REQUEST_CODE:
                    log("PICTURE TAKEN, DO SOMETHING");
                    // Handle the image taken from the camera
                    Media media = new Media();
                    String imagePath =data.getStringExtra(Constants.IMAGE_FILE_PATH);
                    media.setFilePath(imagePath);
                    media.setFileType(Media.FileType.IMAGE_JPEG);
                    Item item = new Item("my first image");
                    item.setContent(media);
                    item = ItemsWorker.insert(item, getContext());
                    log("item inserted with id", item.getID());
/*                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    ImageDialog dialog = new ImageDialog(imageBitmap);
                    dialog.show(getChildFragmentManager(), "show image");*/
                    //item.setContent(new Media());
                    //Bundle extras = data.getExtras();
                    //Item
                    //Bitmap imageBitmap = (Bitmap) extras.get("data");
                    //imageView.setImageBitmap(imageBitmap);
                    //addImageToGallery();
                    break;

            }
        } else {
            log("Result code was not OK: ",resultCode);
        }
    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //openCamera();
                Toast.makeText(getContext(), "YEAH CAMERA PERMISSION GRANTED", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getContext(), "Camera permission is required to use camera.", Toast.LENGTH_SHORT).show();
            }
        }
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