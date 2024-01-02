package se.curtrune.lucy.activities;


import static se.curtrune.lucy.util.Logger.log;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.SQLException;
import java.time.LocalDateTime;

import se.curtrune.lucy.R;
import se.curtrune.lucy.adapters.ItemAdapter;
import se.curtrune.lucy.classes.CallingActivity;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.Mental;
import se.curtrune.lucy.classes.State;
import se.curtrune.lucy.dialogs.AddItemDialog;
import se.curtrune.lucy.util.Constants;
import se.curtrune.lucy.util.Converter;
import se.curtrune.lucy.util.ItemStack;
import se.curtrune.lucy.util.Kronos;
import se.curtrune.lucy.workers.ItemsWorker;


public class ItemSession extends AppCompatActivity implements
        Kronos.Callback,
        AddItemDialog.Callback,
        ItemAdapter.Callback
{
    private EditText editTextHeading;
    private EditText  editTextComment;
    private CheckBox checkBoxDone;

    private Button buttonTimer;
    private TextView textView_timer;
    private SeekBar seekBarStress;
    private SeekBar seekBarAnxiety;
    private SeekBar seekBarDepression;
    private SeekBar seekBarEnergy;

    private CallingActivity callingActivity = CallingActivity.ITEMS_ACTIVITY;
    //variables
    private Integer n_repetitions = 0;
    private Item currentItem;
    private int anxiety;
    private int depression;
    private int stress;
    private int energy;

    private final boolean VERBOSE = false;

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
        new AddItemDialog().show(getSupportFragmentManager(), "new assignment");

    }
    private void deleteItem(){
        log("...deleteItem()");
        if(currentItem.hasChild()) {
            Toast.makeText(this, "delete item with child not implemented", Toast.LENGTH_LONG).show();
        }
        worker.delete(currentItem, this);
    }
    private Item getItem(){
        log("...getItem()");
        currentItem.setUpdated(LocalDateTime.now());
        currentItem.setComment(editTextComment.getText().toString());
        currentItem.setHeading(editTextHeading.getText().toString());
        currentItem.setDuration(kronos.getElapsedTime());
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
        if( VERBOSE) log(currentItem);
        if(currentItem.isWIP()){
            kronos.setElapsedTime(currentItem.getDuration());
        }
        setTitle(currentItem.getHeading());
        callingActivity = (CallingActivity) intent.getSerializableExtra(Constants.INTENT_CALLING_ACTIVITY);
        if( callingActivity == null){
            Toast.makeText(this, "calling activity is null", Toast.LENGTH_LONG).show();
            return;
        }
        log("...return to calling activity: ", callingActivity.toString());
        setUserInterface(currentItem);
    }

    private void initComponents(){
        log("...initComponents()");
        buttonTimer = findViewById(R.id.itemSession_buttonTimer);
        checkBoxDone = findViewById(R.id.itemSession_checkBoxDone);
        editTextComment = findViewById(R.id.itemSession_comment);
        editTextHeading = findViewById(R.id.itemSession_heading);
        textView_timer = findViewById(R.id.itemSession_duration);
        seekBarAnxiety = findViewById(R.id.itemSession_anxietySeekbar);
        seekBarDepression = findViewById(R.id.itemSession_depression);
        seekBarEnergy = findViewById(R.id.itemSession_energy);
        seekBarStress = findViewById(R.id.itemSession_stressSeekbar);
    }
    private void initListeners(){
        if( VERBOSE) log("...initListeners()");
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
        buttonTimer.setOnLongClickListener(view -> {
            kronos.stop();
            kronos.reset();
            textView_timer.setText(R.string.hhmmss);
            buttonTimer.setText(R.string.ui_start);
            return true;
        });
        textView_timer.setOnClickListener(v -> Toast.makeText(this,"not implemented", Toast.LENGTH_LONG).show());
    }

    /**
     * callback for AddItemFragment
     * @param childItem, the new bare bones item
     */
    @Override
    public void onAddItem(Item childItem) {
        log("MusicSessionActivity.onAddItem(Item)");
        Toast.makeText(this, "work in progress, but basically not done", Toast.LENGTH_LONG).show();
        childItem.setParent(currentItem);
        if( !currentItem.hasChild()) {
            worker.setHasChild(currentItem, this);
        }
        try {
            childItem = worker.insert(childItem, this);
            Intent intent = new Intent(this, ItemsActivity.class);
            intent.putExtra(Constants.INTENT_SHOW_CHILD_ITEMS, true);
            intent.putExtra(Constants.INTENT_SERIALIZED_ITEM, currentItem);
            startActivity(intent);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@androidx.annotation.NonNull MenuItem item) {
        log("...onOptionsItemSelected(MenuItem)", item.getTitle().toString());
        if (item.getItemId() == R.id.itemSession_addChild) {
            addChildItem();
        } else if (item.getItemId() == R.id.itemSession_delete) {
            deleteItem();
        } else if (item.getItemId() == R.id.itemSession_openInEditor) {
            openInEditor();
        } else if (item.getItemId() == R.id.itemSession_save) {
            saveItem();
        } else if (item.getItemId() == R.id.itemsActivity_home)  {
            returnToCallingActivity();
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
        //textView_timer.
        textView_timer.setText(Converter.formatSecondsWithHours(secs));
    }

    private void returnToCallingActivity(){
        log("...returnToCallingActivity()", callingActivity.toString());
        switch (callingActivity){
            case ITEMS_ACTIVITY:
                Intent intent = new Intent(this, ItemsActivity.class);
                intent.putExtra(Constants.INTENT_SHOW_SIBLINGS, true);
                intent.putExtra(Constants.CURRENT_ITEM_IS_IN_STACK,  true);
                ItemStack.currentItem = currentItem;
                startActivity(intent);
                break;
            case STATISTICS_ACTIVITY:
                log("return to statistics activity");
                break;
            case TODAY_ACTIVITY:
                log("return to today activity");
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
        int rowsAffected = worker.update(currentItem, this);
        if(rowsAffected != 1){
            Toast.makeText(this, "error updating item", Toast.LENGTH_LONG).show();
            return;
        }
        saveMental();
        returnToCallingActivity();
        kronos.reset();
    }
    private void saveMental(){
        log("...saveMental()");
        energy = seekBarEnergy.getProgress();

    }
    private void setUserInterface(Item item){
        if( VERBOSE) log("...setUserInterface(Item item)");
        if( item == null){
            log("...called with item == null");
            return;
        }
        textView_timer.setText(Converter.formatSecondsWithHours(item.getDuration()));
        editTextHeading.setText(item.getHeading());
        editTextComment.setText(item.getComment());
        checkBoxDone.setChecked(item.isDone());
        if( item.getDuration() > 0){
            buttonTimer.setText("resume");
        }
    }
    private void setUserInterface(Mental mental){


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