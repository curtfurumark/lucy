package se.curtrune.lucy.activities;


import static se.curtrune.lucy.util.Logger.log;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Locale;

import se.curtrune.lucy.R;
import se.curtrune.lucy.classes.CallingActivity;
import se.curtrune.lucy.classes.Estimate;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.Mental;
import se.curtrune.lucy.classes.Notification;
import se.curtrune.lucy.classes.Period;
import se.curtrune.lucy.classes.State;
import se.curtrune.lucy.dialogs.AddEstimateDialog;
import se.curtrune.lucy.dialogs.AddItemDialog;
import se.curtrune.lucy.dialogs.AddPeriodDialog;
import se.curtrune.lucy.dialogs.DurationDialog;
import se.curtrune.lucy.dialogs.NotificationDialog;
import se.curtrune.lucy.util.Constants;
import se.curtrune.lucy.util.Converter;
import se.curtrune.lucy.util.ItemStack;
import se.curtrune.lucy.util.Kronos;
import se.curtrune.lucy.workers.ItemsWorker;
import se.curtrune.lucy.workers.MentalWorker;


public class ItemSession extends AppCompatActivity implements
        Kronos.Callback,
        AddItemDialog.Callback
{
    private EditText editTextHeading;
    //comment etc
    private EditText  editTextComment;
    private EditText editTextTags;
    private TextView textViewTargetDate;
    private TextView textViewTargetTime;
    //mental
    private EditText editTextMentalComment;
    private EditText editTextEstimateHours;
    private EditText editTextEstimateMinutes;
    private EditText editTextEstimateSeconds;
    private TextView textViewEditPeriod;
    private TextView labelEstimate;
    private TextView labelPeriod;
    private TextView labelNotification;
    private TextView labelMental;
    private TextView labelInfo;
    private TextView labelComment;
    private TextView textViewPeriodDescription;
    //private TextView textViewTargetDate;
    private TextView textViewPeriodTargetDate;
    private TextView textViewNotificationInfo;
    private TextView textViewNotificationType;
    private TextView textViewCategory;
    private TextView textViewUpdated;
    private TextView textViewCreated;
    private TextView textViewTags;
    private TextView textViewID;
    private  TextView textViewAddNotification;
    private CheckBox checkBoxDone;
    private CheckBox checkBoxTemplate;
    private Button buttonTimer;
    private TextView textViewDuration;
    private TextView textViewEnergy;
    private TextView textViewNotificationDate;
    private TextView textViewNotificationTime;
    private TextView textViewStress;
    private TextView textViewAnxiety;
    private TextView textViewMood;


    private SeekBar seekBarStress;
    private SeekBar seekBarAnxiety;
    private SeekBar seekBarMood;
    private SeekBar seekBarEnergy;
    private Switch switchSaveMental;
    private ConstraintLayout layoutEstimate;
    private ConstraintLayout layoutNotification;
    private ConstraintLayout layoutPeriod;
    private ConstraintLayout layoutMental;
    private ConstraintLayout layoutInfo;
    private ConstraintLayout layoutComment;
    private FloatingActionButton fabAdd;

    private enum MentalMode{
        EDIT, CREATE
    }
    private MentalMode mentalMode = MentalMode.CREATE;
    private CallingActivity callingActivity = CallingActivity.ITEMS_ACTIVITY;
    //variables
    private Item currentItem;
    private Mental mental;
    private int anxiety;
    private int mood;
    private int stress;
    private int energy;
    private long duration;

    private final boolean VERBOSE = false;

    private Kronos kronos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_session_activity);
        log("ItemSession.onCreate()");
        setTitle("item session");
        initComponents();
        initListeners();
        //worker = ItemsWorker.getInstance();
        Intent intent = getIntent();
        kronos = Kronos.getInstance(this);
        if( intent.getBooleanExtra(Constants.INTENT_ITEM_SESSION, false)){
            handleItemSession(intent);
        }else {
            log("INTENT_ITEM_SESSION false...!!");
            Toast.makeText(this, "error intent", Toast.LENGTH_LONG).show();
        }
    }

    private void addChildItem(){
        log("ItemSession.addChildItem()");
        AddItemDialog dialog = new AddItemDialog(currentItem);
        dialog.setHeading(currentItem.getHeading());
        dialog.setCategory(currentItem.getCategory());
        dialog.show(getSupportFragmentManager(), "new assignment");

    }
    private void deleteItem(){
        log("...deleteItem()");
        if(currentItem.hasChild()) {
            Toast.makeText(this, "delete item with child not implemented", Toast.LENGTH_LONG).show();
        }
        ItemsWorker.delete(currentItem, this);
        returnToCallingActivity();
    }
    private Item getItem(){
        log("...getItem()");
        currentItem.setUpdated(LocalDateTime.now());
        currentItem.setComment(editTextComment.getText().toString());
        currentItem.setHeading(editTextHeading.getText().toString());
        currentItem.setDuration(duration);
        currentItem.setTags(editTextTags.getText().toString());
        currentItem.setIsTemplate(checkBoxTemplate.isChecked());
        currentItem.setState(checkBoxDone.isChecked() ? State.DONE: State.WIP);
        return currentItem;
    }
    private Mental getMental(){
        log("...getMental()");
        if( mental == null){
            mental = new Mental();
        }
        mental.setEnergy(seekBarEnergy.getProgress() - Constants.ENERGY_OFFSET);
        mental.setMood(seekBarMood.getProgress() - Constants.MOOD_OFFSET);
        mental.setAnxiety(seekBarAnxiety.getProgress() - Constants.ANXIETY_OFFSET);
        mental.setStress(seekBarStress.getProgress() - Constants.STRESS_OFFSET);
        mental.setComment(editTextMentalComment.getText().toString());
        mental.setHeading(currentItem.getHeading());
        mental.setCategory(currentItem.getCategory());
        mental.setItemID(currentItem.getID());
        return mental;
    }
    private void handleItemSession(Intent intent){
        log("\thandleItemSession(Intent)");
        if( intent.getBooleanExtra(Constants.CURRENT_ITEM_IS_IN_STACK, false)){
            currentItem = ItemStack.currentItem;
        }else {
            currentItem = (Item) intent.getSerializableExtra(Constants.INTENT_SERIALIZED_ITEM);
        }
        if( currentItem == null){
            Toast.makeText(this, "item is null, i surrender", Toast.LENGTH_LONG).show();
            return;
        }
        setTitle(currentItem.getHeading());
        if( VERBOSE) log(currentItem);
        //if(currentItem.isWIP()){
        kronos.setElapsedTime(currentItem.getDuration());
        //}

        callingActivity = (CallingActivity) intent.getSerializableExtra(Constants.INTENT_CALLING_ACTIVITY);
        if( callingActivity == null){
            Toast.makeText(this, "calling activity is null", Toast.LENGTH_LONG).show();
            return;
        }
        if ( VERBOSE) log("...return to calling activity: ", callingActivity.toString());
        //mental = MentalWorker.getMental(currentItem, this);
        if(!currentItem.hasMental()){
            log("...currentItem has no mental");
            mentalMode = MentalMode.CREATE;
        }else{
            log("...currentItem has mental");
            mental = currentItem.getMental();
            mentalMode = MentalMode.EDIT;
            setUserInterfaceMental(mental);
        }
        setUserInterface(currentItem);
    }

    private void initComponents(){
        log("...initComponents()");
        buttonTimer = findViewById(R.id.itemSession_buttonTimer);
        checkBoxDone = findViewById(R.id.itemSession_checkBoxDone);
        editTextComment = findViewById(R.id.itemSession_comment);
        editTextHeading = findViewById(R.id.itemSession_heading);
        editTextMentalComment = findViewById(R.id.itemSession_mentalComment);
        textViewDuration = findViewById(R.id.itemSession_duration);
        seekBarAnxiety = findViewById(R.id.itemSession_anxietySeekbar);
        seekBarMood = findViewById(R.id.itemSession_seekBarMood);
        seekBarEnergy = findViewById(R.id.itemSession_seekBarEnergy);
        seekBarStress = findViewById(R.id.itemSession_stressSeekbar);
        textViewAnxiety = findViewById(R.id.itemSession_labelAnxiety);
        textViewStress = findViewById(R.id.itemSession_labelStress);
        textViewMood = findViewById(R.id.itemSession_labelMood);
        textViewEnergy = findViewById(R.id.itemSession_labelEnergy);
        switchSaveMental = findViewById(R.id.itemSession_mentalSwitch);
        fabAdd = findViewById(R.id.itemSession_fabAdd);
        labelEstimate = findViewById(R.id.itemSession_labelEstimate);
        editTextEstimateHours = findViewById(R.id.itemSession_estimateHours);
        editTextEstimateMinutes = findViewById(R.id.itemSession_estimateMinutes);
        editTextEstimateSeconds = findViewById(R.id.itemSession_estimateSeconds);
        layoutMental = findViewById(R.id.itemSession_layoutMental);
        layoutEstimate = findViewById(R.id.itemSession_layoutEstimate);
        layoutNotification = findViewById(R.id.itemSession_layoutNotification);
        layoutPeriod = findViewById(R.id.itemSession_layoutPeriod);
        layoutInfo = findViewById(R.id.itemSession_layoutInfo);
        labelPeriod = findViewById(R.id.itemSession_labelPeriod);
        labelMental = findViewById(R.id.itemSession_labelMental);
        //comment
        layoutComment = findViewById(R.id.itemSession_layoutComment);
        labelComment = findViewById(R.id.itemSession_labelCommentEtc);
        textViewTargetDate = findViewById(R.id.itemSession_targetDate);
        textViewTargetTime = findViewById(R.id.itemSession_targetTime);
        editTextTags = findViewById(R.id.itemSession_tags);
        //notification
        textViewNotificationDate = findViewById(R.id.itemSession_notificationDate);
        textViewNotificationTime = findViewById(R.id.itemSession_notificationTime);
        textViewAddNotification = findViewById(R.id.itemSession_addNotification);
        textViewNotificationType = findViewById(R.id.itemSession_notificationType);


        labelNotification = findViewById(R.id.itemSession_labelNotifications);

        textViewPeriodDescription = findViewById(R.id.itemSession_periodDescription);
        textViewPeriodTargetDate = findViewById(R.id.itemSession_periodTargetDate);
        textViewNotificationInfo = findViewById(R.id.itemSession_notificationInfo);
        textViewEditPeriod = findViewById(R.id.itemSession_editPeriod);
        //info
        labelInfo = findViewById(R.id.itemSession_labelInfo);
        checkBoxTemplate = findViewById(R.id.itemSession_checkBoxTemplate);
        textViewUpdated = findViewById(R.id.itemSession_infoUpdated);
        textViewCreated = findViewById(R.id.itemSession_infoCreated);
        textViewTags = findViewById(R.id.itemSession_infoTags);
        textViewCategory = findViewById(R.id.itemSession_infoCategory);
        textViewID = findViewById(R.id.itemSession_infoID);
    }
    private void initDefaults(){
        log("...initDefaults()");
        mood = 0;
        anxiety = 0;
        energy = 0;
        stress = 0;

    }
    private void initListeners(){
/*        if( VERBOSE) log("...initListeners()");
        checkBoxTemplate.setOnCheckedChangeListener((buttonView, isChecked) -> {
            log("...onCheckedChanged(CompoundButton, boolean)", isChecked);
            currentItem.setIsTemplate(isChecked);
            int rowsAffected = ItemsWorker.update(currentItem, this);
            if( rowsAffected != 1){
                log("...error updating item, isTemplate");
            }
        });*/
        textViewTargetTime.setOnClickListener(view->showDateDialog());
        textViewTargetTime.setOnClickListener(view->showTimeDialog());
        checkBoxTemplate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                log("...checkBox onClick(View)");
                if( checkBoxTemplate.isChecked()){
                    log("...template check true");
                }else{
                    log("...template check false");
                }
            }
        });
        textViewAddNotification.setOnClickListener(view->showNotificationDialog());
        textViewEditPeriod.setOnClickListener(view->showAddPeriodDialog());
        labelComment.setOnClickListener(view->toggleComment());
        labelMental.setOnClickListener(view->toggleMental());
        labelPeriod.setOnClickListener(view->togglePeriod());
        labelEstimate.setOnClickListener(view->toggleEstimate());
        labelNotification.setOnClickListener(view->toggleNotifications());
        labelInfo.setOnClickListener(view->toggleInfo());
        switchSaveMental.setOnCheckedChangeListener((buttonView, isChecked) -> {
            log("...onCheckedChanged(CompoundButton, boolean)", isChecked);
            if(isChecked) {
                saveMental();
            }
        });
        buttonTimer.setOnClickListener(view -> {
            switch(kronos.getState()){
                case PENDING:
                case  STOPPED:
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
        });
        fabAdd.setOnClickListener(view->addChildItem());
        buttonTimer.setOnLongClickListener(view -> {
            kronos.stop();
            kronos.reset();
            textViewDuration.setText(R.string.hhmmss);
            buttonTimer.setText(R.string.ui_start);
            return true;
        });
        textViewDuration.setOnClickListener(v -> showDurationDialog());
        seekBarEnergy.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                energy = progress - Constants.ENERGY_OFFSET;
                textViewEnergy.setText(String.format(Locale.getDefault(), "%s %d", getString(R.string.energy), energy));
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
                stress = progress - Constants.STRESS_OFFSET;
                textViewStress.setText(String.format(Locale.getDefault(), "%s %d", getString(R.string.stress),stress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBarAnxiety.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                anxiety = progress - Constants.ANXIETY_OFFSET;
                textViewAnxiety.setText(String.format(Locale.getDefault(), "%s %d", getString(R.string.anxiety), anxiety));
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
                mood = progress - Constants.MOOD_OFFSET;
                textViewMood.setText(String.format(Locale.getDefault(), "%s %d",getString(R.string.mood), mood));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    /**
     * callback for AddItemFragment
     * @param childItem, the new bare bones item
     */
    @Override
    public void onAddItem(Item childItem) {
        log("MusicSessionActivity.onAddItem(Item)");

        childItem = ItemsWorker.insertChild(currentItem, childItem, this);
        log(childItem);
        Intent intent = new Intent(this, TodayActivity.class);
        intent.putExtra(Constants.INTENT_SHOW_CHILD_ITEMS, true);
        intent.putExtra(Constants.INTENT_SERIALIZED_ITEM, currentItem);
        startActivity(intent);
    }
    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.item_session_menu, menu);
        return true;
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@androidx.annotation.NonNull MenuItem item) {
        log("...onOptionsItemSelected(MenuItem)", item.getTitle().toString());
        if (item.getItemId() == R.id.itemSession_delete) {
            deleteItem();
        } else if (item.getItemId() == R.id.itemSession_addPeriod) {
            showAddPeriodDialog();
        } else if (item.getItemId() == R.id.itemSession_save) {
            saveItem();
        } else if (item.getItemId() == R.id.itemSession_home)  {
            returnToCallingActivity();
        } else if( item.getItemId() == R.id.itemSession_addEstimate){
            showEstimateDialog();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onPause() {
        super.onPause();
        kronos.removeCallback();
        if( VERBOSE) log("MusicSessionActivity.onPause()");
    }
    @Override
    protected void onResume() {
        super.onResume();
        setButtonText();
        kronos.setCallback(this);
        if( VERBOSE) log("ItemSession.onResume()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        kronos.setCallback(this);
        if( VERBOSE) log("ItemSession.onRestart()");
    }
    @Override
    protected void onStop() {
        super.onStop();
        kronos.removeCallback();
        if( VERBOSE) log("ItemSession.onStop()");
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

    private void returnToCallingActivity(){
        log("...returnToCallingActivity()", callingActivity.toString());
        switch (callingActivity){
            case ITEMS_ACTIVITY:
                Toast.makeText(this, "no more items activity", Toast.LENGTH_LONG).show();
                break;
            case STATISTICS_ACTIVITY:
                log("return to statistics activity");
                Intent statsIntent = new Intent(this, StatisticsMain.class);
                startActivity(statsIntent);
                break;
            case TODAY_ACTIVITY:
                log("return to today activity");
                Intent todayIntent = new Intent(this, TodayActivity.class);
                startActivity(todayIntent);
                break;
            case CALENDER_FRAGMENT:
                Intent mainIntent = new Intent(this, MainActivity.class);
                startActivity(mainIntent);
                break;
            default:
                Toast.makeText(this, "get your shit together, missing calling activity", Toast.LENGTH_LONG).show();
        }
    }
    private void setButtonText() {
        if(VERBOSE) log("MusicSession.setButtonText");
        Kronos.State state = kronos.getState();
        switch (state){
            case RUNNING:
                buttonTimer.setText(R.string.ui_pause);
                break;
            case PAUSED:
                buttonTimer.setText(R.string.ui_resume);
                break;
            case STOPPED:
                buttonTimer.setText(R.string.ui_start);
                break;

        }
    }
    private void saveItem() {
        log("...saveItem()");
        if(!validateInput()){
            return;
        }
        currentItem = getItem();
        int rowsAffected = ItemsWorker.update(currentItem, this);
        if(rowsAffected != 1){
            Toast.makeText(this, "error updating item", Toast.LENGTH_LONG).show();
            return;
        }else{
            ItemsWorker.touchParents(currentItem, this);
        }
/*        if(switchSaveMental.isChecked() ){
            saveMental();
        }*/

        kronos.reset();
        Intent intent = new Intent(this, TodayActivity.class);
        intent.putExtra(Constants.INTENT_SHOW_CHILD_ITEMS, true);
        Item parent = ItemsWorker.selectItem(currentItem.getParentId(), this);
        intent.putExtra(Constants.INTENT_SERIALIZED_ITEM, parent);
        startActivity(intent);

    }
    private void saveMental(){
        log("...saveMental()");
        if( mental == null){
            log("...mental == null");
            mentalMode = MentalMode.CREATE;
        }
        mental = getMental();
        if( mentalMode.equals(MentalMode.CREATE)){
            try {
                mental = MentalWorker.insert(mental, this);
            } catch (SQLException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }else {
            int rowsAffected = MentalWorker.update(mental, this);
            if (rowsAffected != 1) {
                Toast.makeText(this, "error updating mental", Toast.LENGTH_LONG).show();
            }
        }
    }
    private void setUserInterface(Estimate estimate){
        log("...setUserInterface(Estimate)");
        long seconds = estimate.getDuration();
        editTextEstimateHours.setText(String.valueOf(seconds / 3600));
        editTextEstimateMinutes.setText(String.valueOf((seconds % 3600) / 60));
        editTextEstimateSeconds.setText(String.valueOf(seconds % 60));
    }

    private void setUserInterface(Item item){
        log("...setUserInterface(Item item)");
        if( item == null){
            log("...called with item == null");
            return;
        }
        textViewDuration.setText(Converter.formatSecondsWithHours(item.getDuration()));
        editTextHeading.setText(item.getHeading());
        editTextComment.setText(item.getComment());
        editTextTags.setText(item.getTags());
        textViewTargetDate.setText(item.getTargetDate().toString());
        textViewTargetTime.setText(Converter.format(item.getTargetTime()));


        checkBoxDone.setChecked(item.isDone());
        log("...after setting checkbox to ", item.isDone());
        if( item.getDuration() > 0){
            buttonTimer.setText(getString(R.string.ui_resume));
        }
        if( item.hasEstimate()){
            setUserInterface(item.getEstimate());
        }
        if( item.hasPeriod()){
            setUserInterface(item.getPeriod());
        }else{
            textViewPeriodDescription.setText("no period set");
            textViewPeriodTargetDate.setVisibility(View.GONE);
        }
        if(item.hasNotification()){
            textViewNotificationInfo.setText("has notification");
        }else{
            textViewNotificationInfo.setText("no notification");
        }
        checkBoxTemplate.setChecked(item.isTemplate());
        String textTags = item.hasTags() ? item.getTags(): "no tags";
        textViewTags.setText(textTags);
        String textCreated = String.format(Locale.getDefault(), "created %s", Converter.format(item.getCreated()));
        textViewCreated.setText(textCreated);
        String textUpdated = String.format(Locale.getDefault(), "updated %s", Converter.format(item.getUpdated()));
        textViewUpdated.setText(textUpdated);
        String textCategory = item.hasCategory() ? item.getCategory(): "no category";
        textViewCategory.setText(textCategory);
        String textID = String.format(Locale.getDefault(), "id: %d", item.getID());
        textViewID.setText(textID);

    }
    private void setUserInterface(Notification notification){
        log("...setUserInterface(Notification) ");
        if( notification == null){
            log("...no notification");
            textViewAddNotification.setVisibility(View.VISIBLE);
        }else{
            textViewNotificationDate.setText(notification.getDate().toString());
            textViewNotificationTime.setText(Converter.format(notification.getTime()));
            textViewNotificationType.setText(notification.getType().toString());
            textViewAddNotification.setVisibility(View.GONE);
        }

    }
    private void setUserInterfaceMental(Mental mental){
        log("...setUserInterface(Mental)", mentalMode.toString());
        if( currentItem.hasMental()){
            switchSaveMental.setText("edit mental");
            seekBarAnxiety.setProgress(mental.getAnxiety() + Constants.ANXIETY_OFFSET);
            textViewAnxiety.setText(String.format(Locale.getDefault(), "%s %d",getString(R.string.anxiety), mental.getAnxiety()));
            seekBarEnergy.setProgress(mental.getEnergy() + Constants.ENERGY_OFFSET);
            textViewEnergy.setText(String.format(Locale.getDefault(), "%s %d",getString(R.string.energy), mental.getEnergy()));
            seekBarMood.setProgress(mental.getMood() + Constants.MOOD_OFFSET);
            textViewMood.setText(String.format(Locale.getDefault(), "%s %d",getString(R.string.mood) ,mental.getMood()));
            seekBarStress.setProgress(mental.getStress() + Constants.STRESS_OFFSET);
            textViewStress.setText(String.format(Locale.getDefault(), "%s %d",getString(R.string.stress), mental.getStress()));
            editTextMentalComment.setText(mental.getComment());

        }else{
            seekBarAnxiety.setProgress(Constants.ANXIETY_OFFSET);
            textViewAnxiety.setText(String.format(Locale.getDefault(),"%s 0",getString(R.string.anxiety) ));
            seekBarMood.setProgress(Constants.MOOD_OFFSET);
            textViewMood.setText(String.format(Locale.getDefault(), "%s 0",getString(R.string.mood)));
            seekBarStress.setProgress(Constants.STRESS_OFFSET);
            textViewStress.setText(String.format(Locale.ENGLISH, "%s 0", getString(R.string.stress)));
            seekBarEnergy.setProgress(Constants.ENERGY_OFFSET);
            textViewEnergy.setText(String.format(Locale.ENGLISH, "%s 0", getString(R.string.energy)));
        }
    }
    private void setUserInterface(Period period){
        log("...setUserInterface(Period)");
        textViewPeriodDescription.setText(period.toString());
        textViewPeriodTargetDate.setText(currentItem.getTargetDate().toString());
    }
    private void showDateDialog(){
        log("...showDateDialog()");
        DatePickerDialog datePickerDialog = new DatePickerDialog(this);
        datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                log("...onDateSet(DatePicker, int, int, int)");
                LocalDate targetDate = LocalDate.of(year, month +1, dayOfMonth);
                currentItem.setTargetDate(targetDate);
                textViewTargetDate.setText(targetDate.toString());
            }
        });
        datePickerDialog.show();

    }

    private void showDurationDialog(){
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
            kronos.setElapsedTime(duration.getSeconds());
        });
        dialog.show(getSupportFragmentManager(), "duration");
    }
/*    @Override
    public void onDurationDialog(Duration duration) {
        log("..onDurationDialog(Duration duration)", duration.toString());
        log("...seconds", duration.getSeconds());
        currentItem.setDuration(duration.getSeconds());
        textViewDuration.setText(Converter.formatSecondsWithHours(duration.getSeconds()));
    }*/
    private void showAddPeriodDialog(){
        log("...showPeriodDialog()");
        AddPeriodDialog dialog = new AddPeriodDialog();
        dialog.setListener(period -> {
            log("...onPeriod(Period)", period.toString());
            log(period);
            setUserInterface(period);
            currentItem.setPeriod(period);
            int rowsAffected = ItemsWorker.update(currentItem, this);
            if( rowsAffected != 1){
                Toast.makeText(this, "error updating item", Toast.LENGTH_LONG).show();
            }
        });
        dialog.show(getSupportFragmentManager(), "add edit period");

    }

    private void showEstimateDialog(){
        log("...showEstimateDialog()");
        AddEstimateDialog dialog = new AddEstimateDialog();
        dialog.setCallback((estimate, mode) -> {
            log("...onEstimate(Estimate)");
            currentItem.setEstimate(estimate);
            int rowsAffected = ItemsWorker.update(currentItem, this);
            log("...rowsAffected", rowsAffected);
            if( rowsAffected != 1){
                Toast.makeText(this, "error updating item", Toast.LENGTH_LONG).show();
            }
            setUserInterface(estimate);
        });
        dialog.show(getSupportFragmentManager(), "add estimate");
    }
    private void showNotificationDialog(){
        log("...showNotificationDialog()");
        NotificationDialog dialog = new NotificationDialog();
        dialog.setListener(new NotificationDialog.Callback() {
            @Override
            public void onNotification(Notification notification) {
                log("...onNotification(Notification)");
                currentItem.setNotification(notification);
                log(notification);
                setUserInterface(notification);
            }
        });
        dialog.show(getSupportFragmentManager(), "add notification");
    }
    private void showTimeDialog(){
        log("...showTimeDialog()");
        log("...showTimerPicker()");
        LocalTime rightNow = LocalTime.now();
        int minutes = rightNow.getMinute();
        int hour = rightNow.getHour();
        TimePickerDialog timePicker = new TimePickerDialog(this, (view, hourOfDay, minute) -> {
            LocalTime targetTime = LocalTime.of(hourOfDay, minute);
            textViewTargetTime.setText(targetTime.toString());
            currentItem.setTargetTime(targetTime);
        }, hour, minutes, true);
        timePicker.show();

    }
    private void toggleComment(){
        log("...toggleComment()");
        if( layoutComment.getVisibility() == View.GONE){
            layoutComment.setVisibility(View.VISIBLE);
        }else{
            layoutComment.setVisibility(View.GONE);
        }

    }
    private void toggleEstimate(){
        log("...toggleEstimate()");
        boolean visible = layoutEstimate.getVisibility() == View.VISIBLE;
        int visibility = visible ? View.GONE : View.VISIBLE;
        layoutEstimate.setVisibility(visibility);
    }
    private void toggleInfo(){
        log("...toggleInfo()");
        if(layoutInfo.getVisibility() == View.GONE){
            layoutInfo.setVisibility(View.VISIBLE);
        }else{
            layoutInfo.setVisibility(View.GONE);
        }

    }
    private void toggleMental(){
        log("...toggleMental()");
        if( layoutMental.getVisibility() == View.GONE){
            layoutMental.setVisibility(View.VISIBLE);
        }else{
            layoutMental.setVisibility(View.GONE);
        }
    }
    private void toggleNotifications(){
        log("...toggleNotifications");
        if( layoutNotification.getVisibility() == View.GONE){
            layoutNotification.setVisibility(View.VISIBLE);
        }else{
            layoutNotification.setVisibility(View.GONE);
        }
    }
    private void togglePeriod(){
        log("...togglePeriod()");
        if( layoutPeriod.getVisibility() == View.VISIBLE){
            layoutPeriod.setVisibility(View.GONE);
            textViewEditPeriod.setVisibility(View.GONE);
        }else{
            layoutPeriod.setVisibility(View.VISIBLE);
            if( currentItem.hasPeriod()){
                textViewEditPeriod.setText("edit");
            }else{
                textViewEditPeriod.setText("create");
            }
            textViewEditPeriod.setVisibility(View.VISIBLE);
        }
    }

    private boolean validateInput(){
        log("...validateInput()");
        String heading = editTextHeading.getText().toString();
        if ( heading.isEmpty()){
            Toast.makeText(this, "a heading is required", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }


}