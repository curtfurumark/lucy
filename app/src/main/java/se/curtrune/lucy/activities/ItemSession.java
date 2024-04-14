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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.SQLException;
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
import se.curtrune.lucy.util.EasyNotification;
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

    //MENTAL
    private ImageView imageViewMentalAction;
    private EditText editTextMentalComment;


    //ESTIMATE
    private TextView labelEstimate;    private EditText editTextEstimateHours;
    private EditText editTextEstimateMinutes;
    private EditText editTextEstimateSeconds;

    private TextView labelRepeat;
    private TextView labelNotification;
    private TextView labelMental;
    //INFO
    private TextView labelInfo;
    private TextView textViewUpdateEpoch;
    private TextView textViewHasChild;
    private TextView textViewState;
    private TextView labelComment;
    //repeat
    private ImageView imageViewRepeatAction;
    private TextView textViewPeriodDescription;
    private TextView textViewPeriodTargetDate;
    private TextView textViewNotificationType;
    private TextView textViewCategory;
    private TextView textViewUpdated;
    private TextView textViewCreated;
    private TextView textViewTags;
    private TextView textViewID;
    private CheckBox checkBoxDone;
    private CheckBox checkBoxTemplate;
    private Button buttonTimer;
    private TextView textViewDuration;
    private TextView textViewEnergy;
    //NOTIFICATION
    private  ImageView imageViewNotificationAction;
    private TextView textViewNotificationDate;
    private TextView textViewNotificationTime;
    private TextView textViewStress;
    private TextView textViewAnxiety;
    private TextView textViewMood;

    //DATE AND TIME
    private TextView labelDateTime;
    private ImageView imageViewDateTimeAction;
    private LinearLayout layoutDateTime;
    private TextView textViewTargetDate;
    private TextView textViewTargetTime;

    private SeekBar seekBarStress;
    private SeekBar seekBarAnxiety;
    private SeekBar seekBarMood;
    private SeekBar seekBarEnergy;
    //private Switch switchSaveMental;
    //ESTIMATE
    private ImageView imageViewEstimateAction;
    private ConstraintLayout layoutEstimate;
    private ConstraintLayout layoutNotification;
    private ConstraintLayout layoutRepeat;
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
    private void estimateAction(){
        log("...estimateAction() hasEstimate", currentItem.hasEstimate());
        if( currentItem.hasEstimate()){
            currentItem.setEstimate((Estimate) null);
            imageViewEstimateAction.setImageDrawable(getDrawable(R.drawable.baseline_add_24));
        }else{
            showEstimateDialog();
        }

    }

    private long getEstimatedDuration(){
        log("...getEstimatedDuration()");
        //TODO validate input and maybe add seconds
        int hours = Integer.parseInt(editTextEstimateHours.getText().toString());
        int minutes = Integer.parseInt(editTextEstimateMinutes.getText().toString());
        int duration = (hours * 3600) + (minutes * 60);
        log("...estimated duration", duration);
        return duration;

    }

    private Item getItem(){
        log("...getItem()");
        currentItem.setUpdated(LocalDateTime.now());
        currentItem.setComment(editTextComment.getText().toString());
        currentItem.setHeading(editTextHeading.getText().toString());
        currentItem.setDuration(duration);
        currentItem.setTags(editTextTags.getText().toString());
        currentItem.setIsTemplate(checkBoxTemplate.isChecked());
        currentItem.setState(checkBoxDone.isChecked() ? State.DONE: State.TODO);
        if( currentItem.hasMental()){
            currentItem.getMental().setComment(editTextMentalComment.getText().toString());
        }
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

        fabAdd = findViewById(R.id.itemSession_fabAdd);

        //estimate
        imageViewEstimateAction = findViewById(R.id.itemSession_estimateAction);
        layoutEstimate = findViewById(R.id.itemSession_layoutEstimate);
        editTextEstimateHours = findViewById(R.id.itemSession_estimateHours);
        editTextEstimateMinutes = findViewById(R.id.itemSession_estimateMinutes);
        editTextEstimateSeconds = findViewById(R.id.itemSession_estimateSeconds);
        labelEstimate = findViewById(R.id.itemSession_labelEstimate);

        //mental
        labelMental = findViewById(R.id.itemSession_labelMental);
        imageViewMentalAction = findViewById(R.id.itemSession_mentalAction);
        layoutMental = findViewById(R.id.itemSession_layoutMental);

        layoutNotification = findViewById(R.id.itemSession_layoutNotification);
        //repeat
        layoutRepeat = findViewById(R.id.itemSession_layoutRepeat);
        imageViewRepeatAction = findViewById(R.id.itemSession_repeatActionIcon);
        labelRepeat = findViewById(R.id.itemSession_labelRepeat);


        //COMMENT
        layoutComment = findViewById(R.id.itemSession_layoutComment);
        labelComment = findViewById(R.id.itemSession_labelCommentEtc);
        editTextTags = findViewById(R.id.itemSession_tags);

        //NOTIFICATION
        textViewNotificationDate = findViewById(R.id.itemSession_notificationDate);
        textViewNotificationTime = findViewById(R.id.itemSession_notificationTime);
        imageViewNotificationAction = findViewById(R.id.itemSession_notificationAction);
        textViewNotificationType = findViewById(R.id.itemSession_notificationType);
        labelNotification = findViewById(R.id.itemSession_labelNotifications);

        //REPEAT
        textViewPeriodDescription = findViewById(R.id.itemSession_periodDescription);
        textViewPeriodTargetDate = findViewById(R.id.itemSession_periodTargetDate);
        //INFO
        labelInfo = findViewById(R.id.itemSession_labelInfo);
        checkBoxTemplate = findViewById(R.id.itemSession_checkBoxTemplate);
        textViewUpdated = findViewById(R.id.itemSession_infoUpdated);
        textViewUpdateEpoch = findViewById(R.id.itemSession_infoUpdatedEpoch);
        textViewCreated = findViewById(R.id.itemSession_infoCreated);
        textViewTags = findViewById(R.id.itemSession_infoTags);
        textViewCategory = findViewById(R.id.itemSession_infoCategory);
        textViewID = findViewById(R.id.itemSession_infoID);
        layoutInfo = findViewById(R.id.itemSession_layoutInfo);
        textViewHasChild = findViewById(R.id.itemSession_infoHasChild);
        textViewState = findViewById(R.id.itemSession_infoState);
        //DATE AND TIME
        layoutDateTime = findViewById(R.id.itemSession_layoutDateTime);
        imageViewDateTimeAction = findViewById(R.id.itemSession_dateTimeAction);
        labelDateTime = findViewById(R.id.itemSession_labelDateTime);
        textViewTargetDate = findViewById(R.id.itemSession_targetDate);
        textViewTargetTime = findViewById(R.id.itemSession_targetTime);
    }
    private void initDefaults(){
        log("...initDefaults()");
        mood = 0;
        anxiety = 0;
        energy = 0;
        stress = 0;

    }
    private void initListeners(){
        log("...initListeners()");
        imageViewMentalAction.setOnClickListener(view->mentalAction());
        imageViewEstimateAction.setOnClickListener(view->estimateAction());
        imageViewRepeatAction.setOnClickListener(view->repeatAction());
        textViewTargetDate.setOnClickListener(view->showDateDialog());
        textViewTargetTime.setOnClickListener(view->showTimeDialog());
        checkBoxTemplate.setOnClickListener(v -> {
            log("...checkBox onClick(View)");
            if( checkBoxTemplate.isChecked()){
                log("...template check true");
            }else{
                log("...template check false");
            }
        });
        imageViewNotificationAction.setOnClickListener(view->showNotificationDialog());
        //textViewEditPeriod.setOnClickListener(view->showAddPeriodDialog());
        labelComment.setOnClickListener(view->toggleComment());
        labelMental.setOnClickListener(view->toggleMental());
        labelRepeat.setOnClickListener(view->togglePeriod());
        labelEstimate.setOnClickListener(view->toggleEstimate());
        labelNotification.setOnClickListener(view->toggleNotifications());
        labelInfo.setOnClickListener(view->toggleInfo());
        labelDateTime.setOnClickListener(view->toggleDateTime());
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
                if( currentItem.hasMental()){
                    currentItem.getMental().setEnergy(energy);
                }
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
                if( currentItem.hasMental()){
                    currentItem.getMental().setStress(stress);
                }
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
                if( currentItem.hasMental()){
                    currentItem.getMental().setAnxiety(anxiety);
                }
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
                if( currentItem.hasMental()){
                    currentItem.getMental().setMood(mood);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void mentalAction(){
        log("...mentalAction()");
        if( currentItem.hasMental()){
            int rowsAffected = MentalWorker.delete(currentItem.getMental(), this);
            if( rowsAffected != 1){
                log("ERROR deleting mental");
            }
            currentItem.setMental((Mental) null);
            imageViewMentalAction.setImageDrawable(getDrawable(R.drawable.baseline_add_24));

            setUserInterfaceMental(currentItem.getMental());
        }else{
            imageViewMentalAction.setImageDrawable(getDrawable(R.drawable.baseline_delete_24));
            mental = new Mental();
        }
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
    private void repeatAction(){
        log("...repeatAction()");
        if( currentItem.hasPeriod()){
            currentItem.setPeriod((Period) null);
            imageViewRepeatAction.setImageDrawable(getDrawable(R.drawable.baseline_add_24));
        }else{
            AddPeriodDialog dialog = new AddPeriodDialog();
            dialog.setListener(period -> {
                log("...onPeriod(Period)");
                currentItem.setPeriod(period);
                imageViewRepeatAction.setImageDrawable(getDrawable(R.drawable.baseline_delete_24));
            });
            dialog.show(getSupportFragmentManager(), "add repeat");
        }

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
            case ENCHILADA_FRAGMENT:
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra(Constants.INTENT_SHOW_FRAGMENT, CallingActivity.ENCHILADA_FRAGMENT);
                startActivity(intent);
                break;
            case SEQUENCE_ACTIVITY:
                Intent intentSequence = new Intent(this, SequenceActivity.class);
                startActivity(intentSequence);
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


        kronos.reset();
        Intent intent = new Intent(this, MainActivity.class);
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
        imageViewEstimateAction.setImageDrawable(getDrawable(R.drawable.baseline_delete_24));
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
        log(item);
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
        if( item.hasMental()){
            imageViewMentalAction.setImageDrawable(getDrawable(R.drawable.baseline_delete_24));
        }
        if( item.hasEstimate()){
            setUserInterface(item.getEstimate());
            setUserInterfaceMental(item.getMental());
        }
        if( item.hasPeriod()){
            setUserInterface(item.getPeriod());
        }else{
            textViewPeriodDescription.setText("no period set");
            textViewPeriodTargetDate.setVisibility(View.GONE);
        }
        if(item.hasNotification()){
            setUserInterface(currentItem.getNotification());
        }
        if( item.hasEstimate()){
            setUserInterface(item.getEstimate());
        }
        checkBoxTemplate.setChecked(item.isTemplate());
        String textTags = item.hasTags() ? item.getTags(): "no tags";
        textViewTags.setText(textTags);
        String textCreated = String.format(Locale.getDefault(), "created %s", Converter.format(item.getCreated()));
        textViewCreated.setText(textCreated);
        String textUpdated = String.format(Locale.getDefault(), "updated %s", Converter.format(item.getUpdated()));
        textViewUpdated.setText(textUpdated);
        String textUpdatedEpoch = String.format(Locale.getDefault(), "updated epoch %d", item.getUpdatedEpoch());
        textViewUpdateEpoch.setText(textUpdatedEpoch);
        String textCategory = item.hasCategory() ? item.getCategory(): "no category";
        textViewCategory.setText(textCategory);
        String textID = String.format(Locale.getDefault(), "id: %d", item.getID());
        textViewID.setText(textID);
        String textHasChild = String.format(Locale.getDefault(), "hasChild: %b", item.hasChild());
        textViewHasChild.setText(textHasChild);
        String textState = String.format(Locale.getDefault(), "state: %s", item.getState().toString());
        textViewState.setText(textState);

    }
    private void setUserInterface(Notification notification){
        log("...setUserInterface(Notification) ");
        imageViewNotificationAction.setImageDrawable(getDrawable(R.drawable.baseline_delete_24));
        textViewNotificationDate.setText(notification.getDate().toString());
        textViewNotificationTime.setText(Converter.format(notification.getTime()));
        textViewNotificationType.setText(notification.getType().toString());
    }
    private void setUserInterfaceMental(Mental mental){
        log("...setUserInterface(Mental)", mentalMode.toString());
        if( currentItem.hasMental()){
            //TODO
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
        imageViewRepeatAction.setImageDrawable(getDrawable(R.drawable.baseline_delete_24));
        textViewPeriodDescription.setText(period.toString());
        textViewPeriodTargetDate.setText(currentItem.getTargetDate().toString());
    }
    private void showDateDialog(){
        log("...showDateDialog()");
        DatePickerDialog datePickerDialog = new DatePickerDialog(this);
        datePickerDialog.setOnDateSetListener((view, year, month, dayOfMonth) -> {
            log("...onDateSet(DatePicker, int, int, int)");
            LocalDate targetDate = LocalDate.of(year, month +1, dayOfMonth);
            currentItem.setTargetDate(targetDate);
            textViewTargetDate.setText(targetDate.toString());
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

    /**
     * deletes notification if item has one
     * shows notification dialog if item does not have one
     * TODO, a better name please
     */
    private void showNotificationDialog(){
        log("...showNotificationDialog()");
        if( currentItem.hasNotification()){
            currentItem.setNotification((Notification)null );
            textViewNotificationDate.setText("");
            textViewNotificationTime.setText("");
            textViewNotificationType.setText("");
            imageViewNotificationAction.setImageDrawable(getDrawable( R.drawable.baseline_add_24));

        }else {
            NotificationDialog dialog = new NotificationDialog(currentItem);
            dialog.setListener(notification -> {
                log("...onNotification(Notification)");
                currentItem.setNotification(notification);
                log(notification);
                EasyNotification easyNotification = new EasyNotification(this);
                easyNotification.sendNotificationCH1(notification);
                setUserInterface(notification);
            });
            dialog.show(getSupportFragmentManager(), "add notification");
        }
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
    private void toggleDateTime(){
        log("...toggleDateTime()");
        if( layoutDateTime.getVisibility() == View.GONE){
            layoutDateTime.setVisibility(View.VISIBLE);
        }else{
            layoutDateTime.setVisibility(View.GONE);
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
        if( layoutRepeat.getVisibility() == View.VISIBLE){
            layoutRepeat.setVisibility(View.GONE);
            //textViewEditPeriod.setVisibility(View.GONE);
        }else{
            layoutRepeat.setVisibility(View.VISIBLE);
            //textViewEditPeriod.setVisibility(View.VISIBLE);
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