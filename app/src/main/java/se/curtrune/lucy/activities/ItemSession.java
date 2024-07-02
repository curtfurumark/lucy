package se.curtrune.lucy.activities;


import static se.curtrune.lucy.util.Logger.log;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Locale;
import java.util.Objects;

import se.curtrune.lucy.R;
import se.curtrune.lucy.app.User;
import se.curtrune.lucy.classes.CallingActivity;
import se.curtrune.lucy.classes.MentalStats;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.Mental;
import se.curtrune.lucy.classes.Notification;
import se.curtrune.lucy.classes.Repeat;
import se.curtrune.lucy.classes.Reward;
import se.curtrune.lucy.classes.State;
import se.curtrune.lucy.classes.Type;
import se.curtrune.lucy.dialogs.AddCategoryDialog;
import se.curtrune.lucy.dialogs.EstimateDialog;
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
import se.curtrune.lucy.workers.NotificationsWorker;
import se.curtrune.lucy.workers.StatisticsWorker;


public class ItemSession extends AppCompatActivity implements
        Kronos.Callback,
        AddItemDialog.Callback
{
    private TextView textViewCurrentEnergy;
    private EditText editTextHeading;
    //comment etc
    private EditText  editTextComment;
    private TextView textViewEstimateTime;
    private EditText editTextTags;
    private Spinner spinnerCategories;
    //MENTAL
    private EditText editTextMentalComment;
    private SeekBar seekBarAnxiety;
    private SeekBar seekBarMood;
    private SeekBar seekBarEnergy;
    private SeekBar seekBarStress;

    //ESTIMATE
    private TextView labelEstimate;
    private EditText editTextEstimateHours;
    private EditText editTextEstimateMinutes;
    private EditText editTextEstimateSeconds;
    private SeekBar seekBarEstimatedEnergy;

    private TextView labelRepeat;
    private TextView labelNotification;
    private TextView labelMental;

    //REPEAT
    private ImageView imageViewRepeatAction;
    private TextView textViewPeriodDescription;
    private TextView textViewPeriodTargetDate;
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
    //INFO
    private LinearLayout layoutInfo;
    private TextView textViewParentID;
    private CheckBox checkBoxAppointment;
    private TextView labelInfo;
    private TextView textViewUpdateEpoch;
    private TextView textViewHasChild;
    private TextView textViewState;
    private TextView labelComment;

    //DATE AND TIME
    private TextView labelDateTime;
    private LinearLayout layoutDateTime;
    private TextView textViewTargetDate;
    private TextView textViewTargetTime;
    //REWARD
    private TextView labelReward;
    private LinearLayout layoutReward;


    //ESTIMATE
    private ConstraintLayout layoutEstimate;
    private ConstraintLayout layoutNotification;
    private ConstraintLayout layoutRepeat;
    private ConstraintLayout layoutMental;

    private ConstraintLayout layoutComment;
    private FloatingActionButton fabAdd;
    //SETTINGS
    private CheckBox checkBoxPrioritized;
    //SEARCH
    private TextView labelSearch;
    ArrayAdapter<String> categoryAdapter;
    private LinearLayout layoutSearch;

    private EditText editTextColorCode;
    private CallingActivity callingActivity = CallingActivity.CALENDER_FRAGMENT;
    //variables
    private Item currentItem;
    //MENTAL STUFF
    private Mental mental;
    private int anxiety;
    private int mood;
    private int stress;
    private int energy; //seekbar
    private String category;
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
        initSpinnerCategories();
        initListeners();
        kronos = Kronos.getInstance(this);
        handleIntent(getIntent());
    }

    private void addChildItem(){
        log("ItemSession.addChildItem()");
        AddItemDialog dialog = new AddItemDialog(currentItem, false);
        dialog.setCallback(item -> {
            log("...onAddItem(Item)");
            item = ItemsWorker.insertChild(currentItem, item, this);
            if( item.hasNotification()){
                NotificationsWorker.setNotification(item, this);
            }
            log(item);
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(Constants.INTENT_SHOW_CHILD_ITEMS, true);
            intent.putExtra(Constants.INTENT_SERIALIZED_ITEM, currentItem);
            startActivity(intent);
        });
        dialog.show(getSupportFragmentManager(), "new assignment");

    }
    private void deleteItem(){
        log("...deleteItem()");
        if(currentItem.hasChild()) {
            log("...trying to delete item that has at least one child, fix this");
            Toast.makeText(this, "delete item with child not implemented", Toast.LENGTH_LONG).show();
            return;
        }
        ItemsWorker.delete(currentItem, this);
        returnToCallingActivity();
    }

    private MentalStats getEstimate(){
        log("...getEstimate()");
        MentalStats estimate = new MentalStats();
        estimate.setDuration(getEstimatedDuration());
        estimate.setEnergy(getEstimatedEnergy());
        return estimate;
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
    private int getEstimatedEnergy(){
        return seekBarEstimatedEnergy.getProgress() - Constants.ENERGY_OFFSET;
    }

    private Item getItem(){
        if( VERBOSE) log("...getItem()");
        currentItem.setUpdated(LocalDateTime.now());
        currentItem.setComment(editTextComment.getText().toString());
        currentItem.setHeading(editTextHeading.getText().toString());
        currentItem.setDuration(duration);
        currentItem.setTags(editTextTags.getText().toString());
        currentItem.setIsTemplate(checkBoxTemplate.isChecked());
        currentItem.setState(checkBoxDone.isChecked() ? State.DONE: State.TODO);
        currentItem.setType(checkBoxAppointment.isChecked()? Type.APPOINTMENT: Type.NODE);
        currentItem.setEstimate(getEstimate());
        currentItem.setPriority(checkBoxPrioritized.isChecked()? 1:0);
        return currentItem;
    }

    private void handleIntent(Intent intent){
        log("\thandleIntent(Intent)");
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
        kronos.setElapsedTime(currentItem.getDuration());

        callingActivity = (CallingActivity) intent.getSerializableExtra(Constants.INTENT_CALLING_ACTIVITY);
        if( callingActivity == null){
            Toast.makeText(this, "calling activity is null", Toast.LENGTH_LONG).show();
            return;
        }
        if ( VERBOSE) log("...return to calling activity: ", callingActivity.toString());

        setUserInterface(currentItem);
        mental = MentalWorker.getMental(currentItem, this);
        if( mental == null){
            log("...no mental associated with currentItem, creating a new one");
            mental = new Mental(currentItem);
            mental = MentalWorker.insert(mental, this);
        }
        currentItem.setMental(mental);
        setUserInterface(mental);
        setUserInterfaceCurrentEnergy();
    }

    private void initComponents(){
        if( VERBOSE) log("...initComponents()");
        textViewCurrentEnergy = findViewById(R.id.itemSession_currentEnergy);
        buttonTimer = findViewById(R.id.itemSession_buttonTimer);
        checkBoxDone = findViewById(R.id.itemSession_checkBoxDone);
        editTextComment = findViewById(R.id.itemSession_comment);
        editTextHeading = findViewById(R.id.itemSession_heading);
        textViewEstimateTime = findViewById(R.id.itemSession_estimateTime);
        //COMMENT SECTION
        editTextMentalComment = findViewById(R.id.itemSession_mentalComment);

        layoutComment = findViewById(R.id.itemSession_layoutComment);
        labelComment = findViewById(R.id.itemSession_labelCommentEtc);


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

        //ESTIMATE
        seekBarEstimatedEnergy = findViewById(R.id.itemSession_estimateEnergySeekBar);
        layoutEstimate = findViewById(R.id.itemSession_layoutEstimate);
        editTextEstimateHours = findViewById(R.id.itemSession_estimateHours);
        editTextEstimateMinutes = findViewById(R.id.itemSession_estimateMinutes);
        editTextEstimateSeconds = findViewById(R.id.itemSession_estimateSeconds);
        labelEstimate = findViewById(R.id.itemSession_labelEstimate);

        //MENTAL
        labelMental = findViewById(R.id.itemSession_labelMental);
        layoutMental = findViewById(R.id.itemSession_layoutMental);

        layoutNotification = findViewById(R.id.itemSession_layoutNotification);
        //REPEAT
        layoutRepeat = findViewById(R.id.itemSession_layoutRepeat);
        imageViewRepeatAction = findViewById(R.id.itemSession_repeatActionIcon);
        labelRepeat = findViewById(R.id.itemSession_labelRepeat);

        //NOTIFICATION
        textViewNotificationDate = findViewById(R.id.itemSession_notificationDate);
        textViewNotificationTime = findViewById(R.id.itemSession_notificationTime);
        imageViewNotificationAction = findViewById(R.id.itemSession_notificationAction);
        //textViewNotificationType = findViewById(R.id.itemSession_notificationType);
        labelNotification = findViewById(R.id.itemSession_labelNotifications);

        //REPEAT
        textViewPeriodDescription = findViewById(R.id.itemSession_periodDescription);
        textViewPeriodTargetDate = findViewById(R.id.itemSession_periodTargetDate);
        //INFO
        labelInfo = findViewById(R.id.itemSession_labelInfo);
        checkBoxAppointment = findViewById(R.id.itemSession_checkBoxAppointment);
        checkBoxTemplate = findViewById(R.id.itemSession_checkBoxTemplate);
        textViewUpdated = findViewById(R.id.itemSession_infoUpdated);
        textViewUpdateEpoch = findViewById(R.id.itemSession_infoUpdatedEpoch);
        textViewCreated = findViewById(R.id.itemSession_infoCreated);
        textViewTags = findViewById(R.id.itemSession_infoTags);
        textViewCategory = findViewById(R.id.itemSession_infoCategory);
        textViewID = findViewById(R.id.itemSession_infoID);
        textViewParentID = findViewById(R.id.itemSession_infoParentID);
        layoutInfo = findViewById(R.id.itemSession_layoutInfo);
        textViewHasChild = findViewById(R.id.itemSession_infoHasChild);
        textViewState = findViewById(R.id.itemSession_infoState);
        //DATE AND TIME
        layoutDateTime = findViewById(R.id.itemSession_layoutDateTime);
        //imageViewDateTimeAction = findViewById(R.id.itemSession_dateTimeAction);
        labelDateTime = findViewById(R.id.itemSession_labelDateTime);
        textViewTargetDate = findViewById(R.id.itemSession_targetDate);
        textViewTargetTime = findViewById(R.id.itemSession_targetTime);
        //REWARD
        labelReward = findViewById(R.id.itemSession_labelSettings);
        layoutReward = findViewById(R.id.itemSession_layoutSettings);
        checkBoxPrioritized = findViewById(R.id.itemSession_isPrioritized);
        editTextColorCode = findViewById(R.id.itemSession_colorCode);
        //SEARCH, OR RATHER META
        labelSearch = findViewById(R.id.itemSession_labelSearchable);
        layoutSearch = findViewById(R.id.itemSession_layoutSearch);
        editTextTags = findViewById(R.id.itemSession_tags);
        spinnerCategories = findViewById(R.id.itemSession_categorySpinner);
    }
    private void initListeners(){
        if( VERBOSE) log("...initListeners()");
        imageViewRepeatAction.setOnClickListener(view->repeatAction());
        textViewTargetDate.setOnClickListener(view->showDateDialog());
        textViewTargetTime.setOnClickListener(view->showTimeDialog());
        checkBoxAppointment.setOnClickListener(view->{
            log("...checkBox appointment clicked");
        });
        checkBoxTemplate.setOnClickListener(v -> {
            log("...checkBox onClick(View)");
            if( checkBoxTemplate.isChecked()){
                log("...template check true");
            }else{
                log("...template check false");
            }
        });
        imageViewNotificationAction.setOnClickListener(view->showNotificationDialog());
       // labelComment.setOnClickListener(view->toggleComment());
        labelComment.setOnClickListener(view->showCommentDialog());
        labelMental.setOnClickListener(view->toggleMental());
        labelRepeat.setOnClickListener(view->togglePeriod());
        labelEstimate.setOnClickListener(view->toggleEstimate());
        labelNotification.setOnClickListener(view->toggleNotifications());
        labelInfo.setOnClickListener(view->toggleInfo());
        labelReward.setOnClickListener(view->toggleAward());
        labelDateTime.setOnClickListener(view->toggleDateTime());
        labelSearch.setOnClickListener(view->toggleMeta());
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
                int energy = progress - Constants.ENERGY_OFFSET;
                textViewEnergy.setText(String.format(Locale.getDefault(), "%s %d", getString(R.string.energy), energy));
                if( mental != null){
                    mental.setEnergy(energy);
                }
                setUserInterfaceCurrentEnergy();
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
                if( mental != null){
                    mental.setStress(stress);
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
                if( mental != null){
                    mental.setAnxiety(anxiety);
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
                if( mental != null){
                    mental.setMood(mood);
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
    private void initSpinnerCategories(){
        if( VERBOSE) log("...initSpinnerCategories()");
        String[] categories = User.getCategories(this);
        categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spinnerCategories.setAdapter(categoryAdapter);
        spinnerCategories.setSelection(0);
        spinnerCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = (String) spinnerCategories.getSelectedItem();
                log("...category", category);
                currentItem.setCategory(category);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * callback for AddItemFragment
     * @param childItem, the new bare bones item
     */
    @Override
    public void onAddItem(Item childItem) {
        log("ItemSession.onAddItem(Item)");

        childItem = ItemsWorker.insertChild(currentItem, childItem, this);
        log(childItem);
        Intent intent = new Intent(this, MainActivity.class);
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
    /**
     * callback to AddCategoryDialog
     * @param category, the category to be added to the relevant db table
     */
    public void onNewCategory(String category) {
        log("ItemEditor.onNewCategory(String)", category);
        User.addCategory(category, this);
        String[] categories = User.getCategories(this);
        categoryAdapter.clear();
        categoryAdapter.addAll(categories);
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@androidx.annotation.NonNull MenuItem item) {
        if( VERBOSE) log("...onOptionsItemSelected(MenuItem)", Objects.requireNonNull(item.getTitle()).toString());
        if (item.getItemId() == R.id.itemSession_delete) {
            deleteItem();
        } else if (item.getItemId() == R.id.itemSession_addPeriod) {
            showAddPeriodDialog();
        } else if (item.getItemId() == R.id.itemSession_save) {
            update();
        } else if (item.getItemId() == R.id.itemSession_home)  {
            returnToCallingActivity();
        } else if( item.getItemId() == R.id.itemSession_addEstimate){
            showEstimateDialog();
        } else if( item.getItemId() == R.id.itemSession_addCategory){
            log("...addCategory");
            showCategoryDialog();
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
            currentItem.setPeriod((Repeat) null);
            imageViewRepeatAction.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.baseline_add_24));
        }else{
            AddPeriodDialog dialog = new AddPeriodDialog();
            dialog.setListener(period -> {
                log("...onPeriod(Repeat)");
                currentItem.setPeriod(period);
                imageViewRepeatAction.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.baseline_delete_24));
                textViewPeriodDescription.setText(period.toString());
            });
            dialog.show(getSupportFragmentManager(), "add repeat");
        }

    }

    private void returnToCallingActivity(){
        log("...returnToCallingActivity()", callingActivity.toString());
        switch (callingActivity){
            case STATISTICS_ACTIVITY:
                log("return to statistics activity");
                Intent statsIntent = new Intent(this, StatisticsMain.class);
                startActivity(statsIntent);
                break;
            case APPOINTMENTS_FRAGMENT:
            case CALENDER_FRAGMENT:
            case PROJECTS_FRAGMENT:
            case TODO_FRAGMENT:
            case ENCHILADA_FRAGMENT:
                Intent mainIntent = new Intent(this, MainActivity.class);
                startActivity(mainIntent);
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
        if(VERBOSE) log("...setButtonText");
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

    private void setUserInterface(MentalStats estimate){
        log("...setUserInterface(MentalStats)");
        log(estimate);

        long seconds = estimate.getDuration();
        String stringEstimatedTime = String.format(Locale.getDefault(), "estimated time: %s", Converter.formatSecondsWithHours(estimate.getDuration()));
        textViewEstimateTime.setText(stringEstimatedTime);
        editTextEstimateHours.setText(String.valueOf(seconds / 3600));
        editTextEstimateMinutes.setText(String.valueOf((seconds % 3600) / 60));
        editTextEstimateSeconds.setText(String.valueOf(seconds % 60));
        seekBarEstimatedEnergy.setProgress(estimate.getEnergy() + Constants.ENERGY_OFFSET);
    }

    private void setUserInterface(Item item){
        log("...setUserInterface(Item item)", item.getHeading());
        textViewDuration.setText(Converter.formatSecondsWithHours(item.getDuration()));
        editTextHeading.setText(item.getHeading());
        editTextComment.setText(item.getComment());
        editTextTags.setText(item.getTags());
        textViewTargetDate.setText(item.getTargetDate().toString());
        textViewTargetTime.setText(Converter.format(item.getTargetTime()));
        checkBoxDone.setChecked(item.isDone());
        checkBoxPrioritized.setChecked(item.getPriority() == 1);

        if( VERBOSE) log("...after setting checkbox to ", item.isDone());
        if( item.getDuration() > 0){
            buttonTimer.setText(getString(R.string.ui_resume));
        }
        if( item.hasPeriod()){
            setUserInterface(item.getPeriod());
        }else{
            textViewPeriodDescription.setText("no repeat set");
            textViewPeriodTargetDate.setVisibility(View.GONE);
        }
        if(item.hasNotification()){
            setUserInterface(currentItem.getNotification());
        }
        if( item.isTemplate()){
            log("...item is template");
            setUserInterface(StatisticsWorker.getEstimate(item, this));
        }else{
            setUserInterface(item.getEstimate());
            log("...will calculate estimate");
        }
        if( item.hasReward()){
            setUserInterface(item.getReward());
        }
        if( !item.getCategory().isEmpty()){
            log("...hasCategory()");
            int position = categoryAdapter.getPosition(item.getCategory());
            spinnerCategories.setSelection(position, true);
        }

        checkBoxTemplate.setChecked(item.isTemplate());
        String textTags = item.hasTags() ? item.getTags(): "no tags";
        checkBoxAppointment.setChecked(item.getType().equals(Type.APPOINTMENT));
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

        String textParentID = String.format(Locale.getDefault(), "parent id %d", item.getParentId());
        textViewParentID.setText(textParentID);
        String textHasChild = String.format(Locale.getDefault(), "hasChild: %b", item.hasChild());
        textViewHasChild.setText(textHasChild);
        String textState = String.format(Locale.getDefault(), "state: %s", item.getState().toString());
        textViewState.setText(textState);
    }
    private void setUserInterface(Notification notification){
        log("...setUserInterface(Notification) ");
        labelNotification.setText(notification.toString());
        imageViewNotificationAction.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.baseline_delete_24));
        textViewNotificationDate.setText(notification.getDate().toString());
        textViewNotificationTime.setText(Converter.format(notification.getTime()));
    }

    /**
     * currentEnergy is the sum of energy today as gotten from the db
     * energy is the input from the user using the seekbar
     */
    private void setUserInterfaceCurrentEnergy(){
        log("...setUserInterfaceCurrentEnergy()");
        int previousEnergy  = MentalWorker.getEnergy(LocalDate.now(), this);
        int estimatedCurrent = previousEnergy + seekBarEnergy.getProgress() - Constants.ENERGY_OFFSET;
        if( estimatedCurrent <= -3){
            textViewCurrentEnergy.setTextColor(Color.parseColor("#ff0000"));
        }else if(estimatedCurrent <= 2){
            textViewCurrentEnergy.setTextColor(Color.parseColor("#ffff00"));
        }else{
            textViewCurrentEnergy.setTextColor(Color.parseColor("#00ff00"));
        }
        String textEnergy = String.format(Locale.getDefault(), "energy: %d",estimatedCurrent);
        textViewCurrentEnergy.setText(textEnergy);
    }
    private void setUserInterface(Mental mental){
        log("...setUserInterface(Mental)");
        if(mental == null){
            log("ERROR mental is null, i surrender");
            return;
        }
        anxiety = Constants.ANXIETY_OFFSET + mental.getAnxiety();
        energy =  Constants.ENERGY_OFFSET + mental.getEnergy();
        mood =  Constants.MOOD_OFFSET + mental.getMood();
        stress =  Constants.STRESS_OFFSET + mental.getStress();
        seekBarAnxiety.setProgress(anxiety);
        textViewAnxiety.setText(String.format(Locale.getDefault(),"%s %d",getString(R.string.anxiety), mental.getAnxiety() ));
        seekBarMood.setProgress(mood);
        textViewMood.setText(String.format(Locale.getDefault(), "%s %d",getString(R.string.mood), mental.getMood()));
        seekBarStress.setProgress(stress);
        textViewStress.setText(String.format(Locale.ENGLISH, "%s %d", getString(R.string.stress), mental.getStress()));
        seekBarEnergy.setProgress(energy);
        textViewEnergy.setText(String.format(Locale.ENGLISH, "%s %d", getString(R.string.energy), mental.getEnergy()));
        editTextMentalComment.setText(mental.getComment());
    }
    private void setUserInterface(Repeat repeat){
        log("...setUserInterface(Repeat)");
        imageViewRepeatAction.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.baseline_delete_24));
        labelRepeat.setText(repeat.toString());
        textViewPeriodDescription.setText(repeat.toString());
        textViewPeriodTargetDate.setText(currentItem.getTargetDate().toString());
    }
    private void setUserInterface(Reward reward){
        log("...setUserInterface(Reward)");
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
    private void showCategoryDialog(){
        log("...showCategoryDialog()");
        AddCategoryDialog dialog = new AddCategoryDialog();
        dialog.setListener(category -> {
            log("...onNewCategory(String)", category);
            User.addCategory(category, this);
            updateSpinnerCategories();
        });
        dialog.show(getSupportFragmentManager(), "add category");

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
            log("...onPeriod(Repeat)", period.toString());
            log(period);
            setUserInterface(period);
            currentItem.setPeriod(period);
            int rowsAffected = ItemsWorker.update(currentItem, this);
            if( rowsAffected != 1){
                Toast.makeText(this, "error updating item", Toast.LENGTH_LONG).show();
            }
        });
        dialog.show(getSupportFragmentManager(), "add edit repeat");
    }
    private void showCommentDialog(){
        log("...showCommentDialog()");
        Toast.makeText(this, "NOT IMPLEMENTED", Toast.LENGTH_LONG).show();
    }

    private void showEstimateDialog(){
        log("...showEstimateDialog()");
        EstimateDialog dialog = new EstimateDialog();
        dialog.setCallback((estimate, mode) -> {
            log("...onEstimate(MentalStats)", mode.toString());
            log(estimate);
            currentItem.setEstimate(estimate);
            int rowsAffected = ItemsWorker.update(currentItem, this);
            if( rowsAffected != 1){
                log("ERROR updating currentItem");
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
            labelNotification.setText(getString(R.string.notification));//TODO
            textViewNotificationTime.setText("");
            imageViewNotificationAction.setImageDrawable(AppCompatResources.getDrawable(this,  R.drawable.baseline_add_24));
        }else {
            NotificationDialog dialog = new NotificationDialog(currentItem);
            dialog.setListener(notification -> {
                log("...onNotification(Notification)");
                currentItem.setNotification(notification);
                int rowsAffected = ItemsWorker.update(currentItem, this);
                if( rowsAffected != 1){
                    log("ERROR updating item with new notification, surrendering");
                    Toast.makeText(this, "error updating item", Toast.LENGTH_LONG).show();
                }
                log(notification);
                NotificationsWorker.setNotification(currentItem, this);
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
    private void toggleMeta(){
        log("...toggleMeta()");
        if( layoutSearch.getVisibility() == View.GONE){
            layoutSearch.setVisibility(View.VISIBLE);
        }else{
            layoutSearch.setVisibility(View.GONE);
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
    private void toggleAward(){
        if( layoutReward.getVisibility() == View.VISIBLE){
            layoutReward.setVisibility(View.GONE);
        }else{
            layoutReward.setVisibility(View.VISIBLE);
        }
    }
    private void update(){
        log("...update()");
        updateItem();
        //updateMental();
        kronos.reset();

    }
    /**
     * update item and mental
     */
    private void updateItem() {
        log("...updateItem()");
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
        returnToCallingActivity();
    }
/*    private void updateMental(){
        log("...updateMental()");
        if(mental == null){
            log("ERROR mental is null, i surrender");
            return;
        }
        if( currentItem.isTemplate() && itemIsDone) {
            log("...current item is template and done, will spawn a mental child");
            Mental tmp = new Mental(mental);
            tmp.setDate(LocalDate.now());
            tmp.setTime(LocalTime.now());
            tmp = MentalWorker.insert(tmp, this);
        }else{
            log("...will simply update mental");
            int rowsAffected = MentalWorker.update(mental, this);
            if( rowsAffected != 1){
                log("ERROR updating mental");
            }
        }
    }*/
    private void updateSpinnerCategories(){
        log("...updateSpinnerCategories()");
        initSpinnerCategories();
        //spinnerCategories.setAdapter(null);
        //spinnerCategories.setAdapter(new ArrayAdapter<>());
        //categoryAdapter.addAll(CategoryWorker.getCategories(this));
    }

    private boolean validateInput(){
        if( VERBOSE) log("...validateInput()");
        String heading = editTextHeading.getText().toString();
        if ( heading.isEmpty()){
            Toast.makeText(this, "a heading is required", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }


}