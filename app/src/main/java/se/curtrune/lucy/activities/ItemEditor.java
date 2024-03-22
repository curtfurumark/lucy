package se.curtrune.lucy.activities;


import static se.curtrune.lucy.util.Logger.log;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Locale;

import se.curtrune.lucy.R;
import se.curtrune.lucy.classes.CallingActivity;
import se.curtrune.lucy.classes.Categories;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.Period;
import se.curtrune.lucy.classes.State;
import se.curtrune.lucy.classes.Type;
import se.curtrune.lucy.dialogs.AddCategoryDialog;
import se.curtrune.lucy.dialogs.AddItemDialog;
import se.curtrune.lucy.dialogs.EditParentIdDialog;
import se.curtrune.lucy.dialogs.PeriodDialog;
import se.curtrune.lucy.enums.ViewMode;
import se.curtrune.lucy.persist.LocalDB;
import se.curtrune.lucy.util.Constants;
import se.curtrune.lucy.util.Converter;
import se.curtrune.lucy.util.Kronos;
import se.curtrune.lucy.workers.CategoryWorker;
import se.curtrune.lucy.workers.ItemsWorker;


public class ItemEditor extends AppCompatActivity implements
        AddCategoryDialog.Callback,
        AddItemDialog.Callback,
        //PeriodDialog.Callback,
        DatePickerDialog.OnDateSetListener,
        Kronos.Callback/*,
        TextWatcher */
{

    private EditText editTextHeading;

    private EditText editTextComment;
    private EditText editTextTags;
    private TextView textViewCreated;
    private TextView textViewUpdated;
    private TextView textViewId;
    private TextView textViewParentId;
    private TextView textViewHasChild;
    private TextView textViewPeriod;
    private EditText editTextDuration;
    private TextView textViewTargetDate;
    private TextView textViewTargetTime;
    private TextView textViewNotification;
    private TextView textViewAddCategory;
    private ArrayAdapter<String> categoryAdapter;
    private LocalDate targetDate;
    private LocalTime targetTime = LocalTime.now();

    private Spinner spinnerCategories;
    private Spinner spinnerState;
    private Spinner spinnerType;

    private Type type = Type.NODE;
    private State state = State.PENDING;
    private static final boolean VERBOSE = true;
    private CallingActivity callingActivity;
    private Item currentItem;
    private Categories categories;
    private ViewMode viewMode;
    private Kronos kronos;
    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_editor);
        setTitle("currentItem editor");
        log("ItemEditorActivity.onCreate()");

        initDefaults();
        initComponents();
        initSpinnerType();
        initSpinnerCategories();
        initSpinnerState();
        initListeners();
        kronos = Kronos.getInstance(this);
        Intent intent = getIntent();
        if( intent.getBooleanExtra(Constants.INTENT_EDIT_ITEM, false)){
            editItem(intent);
        }
    }

    public void addChildItem(){
        log("...addChildItem()");
        AddItemDialog addItemDialog = new AddItemDialog(currentItem);
        addItemDialog.show(getSupportFragmentManager(), "currentItem editor");
    }
    private void deleteItem() {
        log("...deleteItem()");
        LocalDB db = new LocalDB(this);
        db.delete(currentItem);
    }
    private void editItem(Intent intent){
        log ("...editItem(Intent)");
        currentItem = (Item) intent.getSerializableExtra(Constants.INTENT_SERIALIZED_ITEM);
        if(currentItem == null){
            Toast.makeText(this, "currentItem is null, surrender", Toast.LENGTH_LONG).show();
            return;
        }
        callingActivity = (CallingActivity) intent.getSerializableExtra(Constants.INTENT_CALLING_ACTIVITY);
        if( callingActivity == null){
            log("...callingActivity is null, defaulting to 'TODAY_ACTIVITY'");
            callingActivity = CallingActivity.TODAY_ACTIVITY;
        }
        if( VERBOSE) log(currentItem);
        setUserInterface(currentItem);
        setTitle(currentItem.getHeading());
    }
    private Item getItem(){
        log("...getItem()");
        currentItem.setComment(editTextComment.getText().toString());
        currentItem.setHeading(editTextHeading.getText().toString());
        currentItem.setUpdated(LocalDateTime.now());
        currentItem.setType((Type) spinnerType.getSelectedItem());
        currentItem.setState((State) spinnerState.getSelectedItem());
        currentItem.setTags(editTextTags.getText().toString());
        log("selected item",(String) spinnerCategories.getSelectedItem());
        currentItem.setCategory((String) spinnerCategories.getSelectedItem());
        //TODO, fix this when youre brain is working
        currentItem.setDuration(Converter.stringToSeconds(editTextDuration.getText().toString()));
        //currentItem.setDays(Integer.parseInt(textViewPeriod.getText().toString()));
        currentItem.setTargetDate(targetDate);
        currentItem.setTargetTime(targetTime);
        return currentItem;

    }
    private void initComponents(){
        log("...initComponents()");
        editTextHeading = findViewById(R.id.itemEditor_heading);
        editTextComment = findViewById(R.id.itemEditor_comment);
        editTextTags = findViewById(R.id.itemEditor_tags);
        textViewCreated = findViewById(R.id.itemEditor_created);
        textViewUpdated = findViewById(R.id.itemEditor_updated);
        textViewId = findViewById(R.id.itemEditor_itemID);
        textViewParentId = findViewById(R.id.itemEditor_parent_id);
        textViewHasChild = findViewById(R.id.itemEditor_hasChild);
        spinnerCategories = findViewById(R.id.itemEditor_spinnerCategory);
        spinnerState = findViewById(R.id.itemEditor_spinnerState);
        editTextDuration = findViewById(R.id.itemEditor_duration);
        textViewTargetDate = findViewById(R.id.itemEditor_targetDate);
        textViewTargetTime = findViewById(R.id.itemEditor_targetTime);
        spinnerType = findViewById(R.id.itemEditor_spinnerType);
        textViewNotification = findViewById(R.id.itemEditor_notification);
        textViewPeriod = findViewById(R.id.itemEditor_labelPersiod);
        textViewAddCategory = findViewById(R.id.itemEditor_addCategory);
    }
    private void initDefaults() {
        log("...initDefaults()");
        targetTime = LocalTime.now();
        categories = new Categories(CategoryWorker.getCategories(this));
    }
    private void initListeners(){
        log("...initListeners()");
        textViewTargetDate.setOnClickListener(click-> showDatePickerDialog());
        textViewTargetTime.setOnClickListener(view->showTimePicker());
        textViewParentId.setOnClickListener(v -> showParentIDDialog());
        textViewNotification.setOnClickListener(view->showNotificationDialog());
        textViewPeriod.setOnClickListener(view->showPeriodDialog());
        textViewAddCategory.setOnClickListener(view->showAddCategoryDialog());
    }
    private void initSpinnerState() {
        log("...initSpinnerState()");
        ArrayAdapter<State> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, State.values());
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spinnerState.setAdapter(arrayAdapter);
        spinnerState.setSelection(State.PENDING.ordinal());
        spinnerState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                state = (State) spinnerState.getSelectedItem();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void initSpinnerType() {
        log("...initSpinnerType()", Type.PENDING.toString());
        ArrayAdapter<Type> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Type.values());
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spinnerType.setAdapter(arrayAdapter);
        spinnerType.setSelection(Type.PENDING.ordinal());
        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                log("type", (Type) spinnerType.getSelectedItem());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initSpinnerCategories() {
        log("...initSpinnerCategories()");
        String[] categories = CategoryWorker.getCategories(this);
        categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spinnerCategories.setAdapter(categoryAdapter);
        spinnerCategories.setSelection(0);
        spinnerCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //ItemEditor.this.type = Type.values()[position];
                String category = (String) spinnerCategories.getSelectedItem();
                log("...category", category);
                //arrayAdapter.
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * callback for AddItemDialog
     * @param child, child to current currentItem
     */
    @Override
    public void onAddItem(Item child) {
        log("...onAddItem(Item child)");
        if( currentItem == null){
            Toast.makeText(this, "currentItem is null WTF", Toast.LENGTH_LONG).show();
            log("currentItem is null, i surrender");
            return;
        }
        child.setParent(currentItem);
        child.setType(currentItem.getType());
        child.setTags(currentItem.getTags());
        if(!currentItem.hasChild()){
            LocalDB db = new LocalDB(this);
            db.setItemHasChild(currentItem.getID(), true);
        }
        if( VERBOSE) log("...will log new child item");
        if( VERBOSE) log(child);

        LocalDB db  = new LocalDB(this);
        child = db.insert(child);
        currentItem.addChild(child);
        returnToCallingActivity();
    }
    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.item_editor_menu, menu);
        return true;
    }

    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
        String date = String.format(Locale.getDefault(),"%d-%02d-%02d", year, month + 1, dayOfMonth);
        targetDate = LocalDate.of(year, month + 1, dayOfMonth);
        textViewTargetDate.setText(date);
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@androidx.annotation.NonNull MenuItem menuItem) {
        log("...onOptionsItemSelected(...)");
        if( currentItem == null){
            log("CURRENT ITEM IS NULL");
            Toast.makeText(this, "currentItem is null WTF", Toast.LENGTH_LONG).show();
            return false;
        }
        log("CURRENT ITEM FOLLOWS");
        log(currentItem);
        if( menuItem.getItemId() == R.id.itemEditor_home){
            returnToCallingActivity();
        }else if( menuItem.getItemId() == R.id.itemEditor_save){
            updateItem();
        }else if( menuItem.getItemId() == R.id.itemEditor_delete){
            deleteItem();
        }else if(menuItem.getItemId() == R.id.itemEditor_add_child_item){
            addChildItem();
        }
        return super.onOptionsItemSelected(menuItem);
    }
    @Override
    protected void onPause() {
        super.onPause();
        kronos.removeCallback();
    }


    public void onTimerTick(long secs) {
        editTextDuration.setText(Converter.formatSecondsWithHours(secs));
    }

    private void returnToCallingActivity(){
        log("...returnToCallingActivity()");
        switch(callingActivity){
            case ITEMS_ACTIVITY:
            case TODAY_ACTIVITY:
                startActivity(new Intent(this, TodayActivity.class));
                break;
            case STATISTICS_ACTIVITY:
                startActivity(new Intent(this, StatisticsMain.class));
                break;
            default:
                Toast.makeText(this, "strange, no enum calling activity", Toast.LENGTH_LONG).show();
                break;
        }

    }
    private void showAddCategoryDialog(){
        log("...showAddCategoryDialog()");
        AddCategoryDialog dialog = new AddCategoryDialog();
        dialog.show(getSupportFragmentManager(), "add category");
    }

    /**
     * callback to AddCategoryDialog
     * @param category
     */
    @Override
    public void onNewCategory(String category) {
        log("ItemEditor.onNewCategory(String)", category);
        CategoryWorker.insertCategory(category, this);
        String[] categories = CategoryWorker.getCategories(this);
        categoryAdapter.clear();
        categoryAdapter.addAll(categories);
    }
    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this);
        datePickerDialog.setOnDateSetListener(this);
        datePickerDialog.show();
    }
    private void showNotificationDialog(){
        log("...showNotificationDialog()");

    }
    private void showParentIDDialog(){
        EditParentIdDialog dialog = new EditParentIdDialog(currentItem.getParentId());
        dialog.show(getSupportFragmentManager(), "whatever");
    }
    private void showTimePicker(){
        log("...showTimerPicker()");
        int minutes = targetTime.getMinute();
        int hour = targetTime.getHour();
        TimePickerDialog timePicker = new TimePickerDialog(this, (view, hourOfDay, minute) -> {
            targetTime = LocalTime.of(hourOfDay, minute);
            textViewTargetTime.setText(targetTime.toString());
        }, hour, minutes, true);
        timePicker.show();
    }

    private void setUserInterface(Item item) {
        log("...setUserInterface(Item)");
        if( VERBOSE) log(item);
        editTextHeading.setText(item.getHeading());
        editTextComment.setText(item.getComment() == null ? "" : item.getComment());
        editTextTags.setText(item.getTags() == null ? "" : item.getTags());
        textViewCreated.setText(Converter.formatDateTimeUI(item.getCreatedEpoch()));
        textViewUpdated.setText(Converter.formatUI(item.getUpdated()));
        textViewId.setText(String.valueOf(item.getID()));
        targetDate = item.getTargetDate();
        textViewTargetDate.setText(targetDate.toString());
        targetTime = item.getTargetTime();
        textViewTargetTime.setText(targetTime.toString());
        if( item.isInfinite()) {
            textViewPeriod.setText(String.valueOf(item.getDays()));
        }
        textViewParentId.setText(String.valueOf(item.getParentId()));
        textViewHasChild.setText(String.valueOf(item.hasChild()));
        String category = item.getCategory();
        spinnerCategories.setSelection(categories.getIndex(category));

        spinnerState.setSelection(item.getState().ordinal());
        spinnerType.setSelection(item.getType().ordinal());
        String strDuration = String.format(Locale.ENGLISH, "%s", Converter.formatSecondsWithHours(item.getDuration()));
        editTextDuration.setText(strDuration);
    }

    private void setUserInterface(Period period){
        log("...setUserInterface(Period)");
        textViewPeriod.setText(String.valueOf(period.getDays()));
    }
    private void showPeriodDialog(){
        log("...showPeriodDialog()");
        PeriodDialog dialog = new PeriodDialog();
        dialog.setListener(period -> {
            log("...onPeriod(Period)");
            log(period);
            currentItem.setPeriod(period);
            int res = ItemsWorker.update(currentItem, this);
            log("...res", res);
        });
        dialog.show(getSupportFragmentManager(), "hello");
    }

/*    @Override
    public void onPeriod(Period period) {
        log("...onPeriod(Period)", period.toString());
        currentItem.setDays(period.getDays());
        setUserInterface(period);
    }*/
    private void showProjectIdDialog(){
        log("...showProjectIdDialog()");
        Toast.makeText(this, "not implemented", Toast.LENGTH_LONG).show();
    }

    private void updateItem() {
        log("...updateItem()");
        currentItem = getItem();
        kronos.reset();
        kronos.removeCallback();
        editTextDuration.setText("00:00:00");
        ItemsWorker worker = ItemsWorker.getInstance();
        int rowsAffected = worker.update(currentItem, this);
        if( rowsAffected != 1){
            log("rowsAffected", rowsAffected);
            Toast.makeText(this, "error updating item", Toast.LENGTH_LONG).show();
        }else{
            log("item updated");
        }
        startActivity(new Intent(this, TodayActivity.class));
    }
}