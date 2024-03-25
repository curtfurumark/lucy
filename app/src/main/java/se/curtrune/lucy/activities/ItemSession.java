package se.curtrune.lucy.activities;


import static se.curtrune.lucy.util.Logger.log;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import java.time.LocalDateTime;
import java.util.Locale;

import se.curtrune.lucy.R;
import se.curtrune.lucy.adapters.ItemAdapter;
import se.curtrune.lucy.classes.CallingActivity;
import se.curtrune.lucy.classes.Estimate;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.Mental;
import se.curtrune.lucy.classes.State;
import se.curtrune.lucy.dialogs.AddItemDialog;
import se.curtrune.lucy.dialogs.DurationDialog;
import se.curtrune.lucy.dialogs.EstimateItemDialog;
import se.curtrune.lucy.util.Constants;
import se.curtrune.lucy.util.Converter;
import se.curtrune.lucy.util.ItemStack;
import se.curtrune.lucy.util.Kronos;
import se.curtrune.lucy.workers.ItemsWorker;
import se.curtrune.lucy.workers.MentalWorker;


public class ItemSession extends AppCompatActivity implements
        Kronos.Callback,
        AddItemDialog.Callback,
        DurationDialog.Callback,
        ItemAdapter.Callback
{
    private EditText editTextHeading;
    private EditText  editTextComment;
    private EditText editTextMentalComment;
    private EditText editTextEstimateHours;
    private EditText editTextEstimateMinutes;
    private TextView labelEstimate;
    private CheckBox checkBoxDone;
    private Button buttonTimer;
    private TextView textViewDuration;
    private TextView textViewEnergy;
    private TextView textViewStress;
    private TextView textViewAnxiety;
    private TextView textViewMood;

    private SeekBar seekBarStress;
    private SeekBar seekBarAnxiety;
    private SeekBar seekBarMood;
    private SeekBar seekBarEnergy;
    private Switch switchSaveMental;
    private ConstraintLayout layoutEstimate;
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

    private final boolean VERBOSE = true;

    private Kronos kronos;
    private ItemsWorker worker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_session_activity);
        log("ItemSession.onCreate()");
        setTitle("item session");
        initComponents();
        initListeners();
        worker = ItemsWorker.getInstance();
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
        //currentItem.setDuration(kronos.getElapsedTime()); 240124
        currentItem.setState(checkBoxDone.isChecked() ? State.DONE: State.WIP);
        return currentItem;
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
        if(currentItem.isWIP()){
            kronos.setElapsedTime(currentItem.getDuration());
        }

        callingActivity = (CallingActivity) intent.getSerializableExtra(Constants.INTENT_CALLING_ACTIVITY);
        if( callingActivity == null){
            Toast.makeText(this, "calling activity is null", Toast.LENGTH_LONG).show();
            return;
        }
        log("...return to calling activity: ", callingActivity.toString());
        mental = MentalWorker.getMental(currentItem, this);
        if(mental == null){
            log("...mental is null, ie no mental data associated with this activity");
            mentalMode = MentalMode.CREATE;
        }else{
            log("...got mental");
            log(mental);
            mentalMode = MentalMode.EDIT;
        }
        setUserInterfaceMental(mental);
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
        layoutEstimate = findViewById(R.id.itemSession_layoutEstimate);
    }
    private void initDefaults(){
        log("...initDefaults()");
        mood = 0;
        anxiety = 0;
        energy = 0;
        stress = 0;

    }
    private void initListeners(){
        if( VERBOSE) log("...initListeners()");
        labelEstimate.setOnClickListener(view->toggleEstimate());
        switchSaveMental.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                log("...onCheckedChanged(CompoundButton, boolean)", isChecked);
                toggleMental(isChecked);
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
    @Override
    public void onItemClick(Item item) {
        log("...onItemClick(Listable)");
    }
    @Override
    public void onLongClick(Item item) {
        log("ItemSession.onLongClick(Listable)");
    }

    @Override
    public void onCheckboxClicked(Item item, boolean checked) {
        log("...onCheckboxClicked(Item, boolean)");
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@androidx.annotation.NonNull MenuItem item) {
        log("...onOptionsItemSelected(MenuItem)", item.getTitle().toString());
        if (item.getItemId() == R.id.itemSession_delete) {
            deleteItem();
        } else if (item.getItemId() == R.id.itemSession_openInEditor) {
            openInEditor();
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
        if( VERBOSE) log("MusicSessionActivity.onResume()");
    }
    private void openInEditor(){
        log("...openInEditor()");
        Toast.makeText(this, "not implemented", Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        kronos.setCallback(this);
        if( VERBOSE) log("MusicSessionActivity.onRestart()");
    }
    @Override
    protected void onStop() {
        super.onStop();
        kronos.removeCallback();
        if( VERBOSE) log("MusicSessionActivity.onStop()");
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
            default:
                Toast.makeText(this, "get your shit together", Toast.LENGTH_LONG).show();
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
        if(switchSaveMental.isChecked() ){
            saveMental();
        }

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
            mental = new Mental();
        }
        mental.setEnergy(seekBarEnergy.getProgress() - Constants.ENERGY_OFFSET);
        mental.setMood(seekBarMood.getProgress() - Constants.MOOD_OFFSET);
        mental.setAnxiety(seekBarAnxiety.getProgress() - Constants.ANXIETY_OFFSET);
        mental.setStress(seekBarStress.getProgress() - Constants.STRESS_OFFSET);
        mental.setComment(editTextComment.getText().toString());
        mental.setHeading(currentItem.getHeading());
        mental.setCategory(currentItem.getCategory());
        mental.setItemID(currentItem.getID());
        if( mentalMode.equals(MentalMode.CREATE)){
            try {
                mental = MentalWorker.insert(mental, this);
            } catch (SQLException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }else{
            int rowsAffected = MentalWorker.update(mental, this);
            if( rowsAffected != 1){
                Toast.makeText(this, "error updating mental", Toast.LENGTH_LONG).show();
            }
        }
    }
    private void setUserInterface(Estimate estimate){
        log("...setUserInterface(Estimate)");
        long seconds = estimate.getDuration();
        //Duration duration1 = Duration.ofSeconds(seconds);
        editTextEstimateHours.setText(String.valueOf(seconds / 3600));
        editTextEstimateMinutes.setText(String.valueOf(seconds % 3600));
    }

    private void setUserInterface(Item item){
        if( VERBOSE) log("...setUserInterface(Item item)");
        if( item == null){
            log("...called with item == null");
            return;
        }
        textViewDuration.setText(Converter.formatSecondsWithHours(item.getDuration()));
        editTextHeading.setText(item.getHeading());
        editTextComment.setText(item.getComment());
        checkBoxDone.setChecked(item.isDone());
        if( item.getDuration() > 0){
            buttonTimer.setText(getString(R.string.ui_resume));
        }
        if( item.hasEstimate()){
            setUserInterface(item.getEstimate());
        }
        //setUserInterfaceMental(null);
    }
    private void setUserInterfaceMental(Mental mental){
        log("...setUserInterface(Mental)");
        if( mentalMode.equals(MentalMode.CREATE)){
            seekBarAnxiety.setProgress(Constants.ANXIETY_OFFSET);
            textViewAnxiety.setText(String.format(Locale.getDefault(),"%s 0",getString(R.string.anxiety) ));
            seekBarMood.setProgress(Constants.MOOD_OFFSET);
            textViewMood.setText(String.format(Locale.getDefault(), "%s 0",getString(R.string.mood)));
            seekBarStress.setProgress(Constants.STRESS_OFFSET);
            textViewStress.setText(String.format(Locale.ENGLISH, "%s 0", getString(R.string.stress)));
            seekBarEnergy.setProgress(Constants.ENERGY_OFFSET);
            textViewEnergy.setText(String.format(Locale.ENGLISH, "%s 0", getString(R.string.energy)));
        }else{
            seekBarAnxiety.setProgress(mental.getAnxiety() + Constants.ANXIETY_OFFSET);
            textViewAnxiety.setText(String.format(Locale.getDefault(), "%s %d",getString(R.string.anxiety), mental.getAnxiety()));
            seekBarEnergy.setProgress(mental.getEnergy() + Constants.ENERGY_OFFSET);
            textViewEnergy.setText(String.format(Locale.getDefault(), "%s %d",getString(R.string.energy), mental.getEnergy()));
            seekBarMood.setProgress(mental.getMood() + Constants.MOOD_OFFSET);
            textViewMood.setText(String.format(Locale.getDefault(), "%s %d",getString(R.string.mood) ,mental.getMood()));
            seekBarStress.setProgress(mental.getStress() + Constants.STRESS_OFFSET);
            textViewStress.setText(String.format(Locale.getDefault(), "%s %d",getString(R.string.stress), mental.getStress()));
            editTextComment.setText(mental.getComment());
        }
    }

    private void showDurationDialog(){
        log("...showDurationDialog()");
        DurationDialog dialog = new DurationDialog();
        dialog.show(getSupportFragmentManager(), "duration");
    }
    @Override
    public void onDurationDialog(Duration duration) {
        log("..onDurationDialog(Duration duration)", duration.toString());
        log("...seconds", duration.getSeconds());
        currentItem.setDuration(duration.getSeconds());
        textViewDuration.setText(Converter.formatSecondsWithHours(duration.getSeconds()));
    }

    private void showEstimateDialog(){
        log("...showEstimateDialog()");
        EstimateItemDialog dialog = new EstimateItemDialog();
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
    private void toggleEstimate(){
        log("...toggleEstimate()");
        boolean visible = layoutEstimate.getVisibility() == View.VISIBLE;
        int visibility = visible ? View.GONE : View.VISIBLE;
        layoutEstimate.setVisibility(visibility);
        //editTextEstimateMinutes.setVisibility(visibility);
        //editTextEstimateHours.setVisibility(visibility);

    }
    private void toggleMental(boolean visible){
        log("...toggleMental()");
        int visibility = visible ? View.VISIBLE: View.GONE;
        textViewStress.setVisibility(visibility);
        seekBarStress.setVisibility(visibility);
        textViewMood.setVisibility(visibility);
        seekBarMood.setVisibility(visibility);
        textViewEnergy.setVisibility(visibility);
        seekBarEnergy.setVisibility(visibility);
        textViewAnxiety.setVisibility(visibility);
        seekBarAnxiety.setVisibility(visibility);
        editTextMentalComment.setVisibility(visibility);
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