package se.curtrune.lucy.activities;

import static se.curtrune.lucy.util.Logger.log;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import se.curtrune.lucy.R;
import se.curtrune.lucy.adapters.ItemAdapter;
import se.curtrune.lucy.app.Lucinda;
import se.curtrune.lucy.app.Settings;
import se.curtrune.lucy.classes.CallingActivity;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.Period;
import se.curtrune.lucy.classes.State;
import se.curtrune.lucy.dialogs.AddAppointmentDialog;
import se.curtrune.lucy.dialogs.AddItemDialog;
import se.curtrune.lucy.dialogs.AddPeriodDialog;
import se.curtrune.lucy.dialogs.AddTemplateDialog;
import se.curtrune.lucy.dialogs.EstimateItemsDialog;
import se.curtrune.lucy.dialogs.StatisticsDialog;
import se.curtrune.lucy.enums.ViewMode;
import se.curtrune.lucy.util.Constants;
import se.curtrune.lucy.workers.ItemsWorker;
import se.curtrune.lucy.workers.WebWorker;

public class TodayActivity extends AppCompatActivity implements
        AddItemDialog.Callback,
        ItemAdapter.Callback {
    private RecyclerView recycler;

    private EditText editTextSearch;
    private TextView textViewDate;

    private FloatingActionButton fabAdd;
    private ItemTouchHelper itemTouchHelper;

    private BottomNavigationView bottomNavigationView;
    private Item currentParent;
    private LocalDate currentDate;



/*    private enum Mode{
        TODAY, TODO, WIP, APPOINTMENTS, TEMPLATE, PROJECTS, CHILDREN
    }*/
    private ViewMode mode = ViewMode.TODAY;
    private ItemAdapter adapter;
   // private ItemsWorker worker;
    private List<Item> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.today_activity);
        log("TodayActivity.onCreate()");

        initDefaults();
        initComponents();
        initListeners();
        initRecycler();
        initSwipe();
        setUserInterface();
        if( savedInstanceState != null){
            log("...savedInstance != null");
            restoreInstance(savedInstanceState);
        }
        if( Lucinda.currentViewMode != null){
            log("currentViewMode != null");
            mode = Lucinda.currentViewMode;
            bottomNavigationView.setSelectedItemId(Lucinda.getItemID(mode));
        }else{
            log("WARNING, Lucinda.currentViewMode == null");
        }
        Intent intent = getIntent();
        if( intent.getBooleanExtra(Constants.INTENT_SHOW_CHILD_ITEMS, false)){
            log("INTENT_SHOW_CHILD_ITEMS");
            currentParent = (Item) intent.getSerializableExtra(Constants.INTENT_SERIALIZED_ITEM);
            Toast.makeText(this, "parent " + currentParent.getHeading(), Toast.LENGTH_LONG).show();
            showChildren(currentParent);
        }else {
            show(mode);
        }
    }
    private void ascend(){
        log("...ascend() ");
        ///log("...old parent", currentParent.getHeading());
        currentParent = ItemsWorker.getParent(currentParent, this);

        Lucinda.currentParent = currentParent;
        if(currentParent == null){
            log("...top of the world, switching to homeActivity");
            //Toast.makeText(this, "top of the world", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, HomeActivity.class));
        }else {
            //log("...new parent", currentParent.getHeading());
            show(ViewMode.CHILDREN);
        }
    }

    private boolean delete(Item item){
        log("...delete(Item)");
        if(  ItemsWorker.delete(item, this)){
            items.remove(item);
            return true;
        }
        return false;
    }

    private void filterItems(String str){
        List<Item> filteredItems = items.stream().filter(item->item.contains(str)).collect(Collectors.toList());
        adapter.setList(filteredItems);
    }
    private void initComponents(){
        log("...initComponents()");
        recycler = findViewById(R.id.todayActivity_recycler);
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        editTextSearch = findViewById(R.id.todayActivity_search);
        textViewDate = findViewById(R.id.todayActivity_date);
        fabAdd = findViewById(R.id.todayActivity_fabAdd);
    }
    private void initDefaults(){
        log("...initDefaults()");
        currentDate = LocalDate.now();

    }

    private void initListeners(){
        log("...initListeners()");
        textViewDate.setOnClickListener(view->showDateDialog());
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            log("...onNavigationItemSelected()", Objects.requireNonNull(item.getTitle()).toString());
            if (item.getItemId() == R.id.bottomNavigation_projects) {
                mode = ViewMode.PROJECTS;
                Lucinda.currentViewMode = ViewMode.PROJECTS;
                currentParent = ItemsWorker.getRootItem(Settings.Root.PROJECTS, this);
                show(mode);
            }else if( item.getItemId() == R.id.bottomNavigation_todo){
                Lucinda.currentViewMode = ViewMode.TODO;
                mode = ViewMode.TODO;
                currentParent = ItemsWorker.getRootItem(Settings.Root.TODO, this);
                show(ViewMode.TODO);
            }else if( item.getItemId() == R.id.bottomNavigation_today){
                Lucinda.currentViewMode = ViewMode.TODAY;
                mode = ViewMode.TODAY;
                currentParent = ItemsWorker.getRootItem(Settings.Root.DAILY, this);
                show(ViewMode.TODAY);
            }else if( item.getItemId() == R.id.bottomNavigation_appointments){
                currentParent = ItemsWorker.getRootItem(Settings.Root.APPOINTMENTS, this);
                mode = Lucinda.currentViewMode = ViewMode.APPOINTMENTS;
                show(ViewMode.APPOINTMENTS);
            }else if( item.getItemId() == R.id.bottomNavigation_todo){
                currentParent = ItemsWorker.getRootItem(Settings.Root.TODO, this);
                mode = ViewMode.TODO;
                show(mode);
            }else if(  item.getItemId() == R.id.bottomNavigation_enchilada){
                show(ViewMode.ENCHILADA);
            }
            return true;
        });
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filterItems(s.toString());
            }
        });
        fabAdd.setOnClickListener(view->showAddItemDialog());

    }
    private void initRecycler(){
        log("...initRecycler()");
        adapter = new ItemAdapter(items, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recycler.setLayoutManager(layoutManager);
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setAdapter(adapter);
    }

    private void initSwipe() {
        log("...initSwipe()");
        itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int index = viewHolder.getAdapterPosition();
                Item item = items.remove(index);
/*                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                builder.setTitle("delete " + item.getHeading());
                builder.setMessage("are you sure? ");
                builder.setPositiveButton(null, (dialog, which) ->{
                    log("...on positive button click");
                    if( delete(item)) {
                        adapter.notifyItemRemoved(index);
                        Toast.makeText(TodayActivity.this, "item deleted", Toast.LENGTH_LONG).show();
                    }else{
                        log("error deleting item");
                        Toast.makeText(TodayActivity.this, "error deleting item", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton(null, (dialog, which) -> log("...on negative button click"));
                AlertDialog dialog = builder.create();
                dialog.show();*/

            }
        });
        itemTouchHelper.attachToRecyclerView(recycler);
    }
    @Override
    public void onAddItem(Item item) {
        log("TodayActivity.onAddItem(Item)", mode.toString());
        switch (mode) {
            case TODAY:
            case TODO:
            case PROJECTS:
                item = ItemsWorker.insert(item, this);
                items.add(0, item);
                adapter.notifyItemInserted(0);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.today_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        log("...onOptionsItemSelected(MenuItem)", Objects.requireNonNull(item.getTitle()).toString());
        if( item.getItemId() == R.id.todayActivity_home){
            ascend();
        }else if( item.getItemId() == R.id.todayActivity_viewStatistics){
            showStatistics();
        }else if( item.getItemId() == R.id.todayActivity_showAffirmation){
            showAffirmation();
        }else if(item.getItemId() == R.id.todayActivity_showEstimate){
            showEstimate();
        }else if ( item.getItemId() == R.id.todayActivity_showPeriodDialog){
            showPeriodDialog();
        }else if( item.getItemId() == R.id.todayActivity_startSequence){
            startSequence();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(Item item) {
        log("...onItemClick(Item)", item.getHeading());
        if(item.hasChild() && !item.isTemplate()){
            setTitle(item.getHeading());
            items = ItemsWorker.selectChildItems(item, this);
            Lucinda.currentParent = item;
            currentParent = item;
            adapter.setList(items);
        }else {
            Intent intent = new Intent(this, ItemSession.class);
            intent.putExtra(Constants.INTENT_ITEM_SESSION, true);
            intent.putExtra(Constants.INTENT_CALLING_ACTIVITY, CallingActivity.TODAY_ACTIVITY);
            intent.putExtra(Constants.CURRENT_VIEW_MODE, mode);
            intent.putExtra(Constants.INTENT_SERIALIZED_ITEM, item);
            Lucinda.currentViewMode = mode;
            startActivity(intent);
        }
    }

    @Override
    public void onLongClick(Item item) {
        log("...onLongClick()");
        Intent intent = new Intent(this, ItemEditor.class);
        intent.putExtra(Constants.INTENT_EDIT_ITEM, true);
        intent.putExtra(Constants.INTENT_CALLING_ACTIVITY, CallingActivity.TODAY_ACTIVITY);
        intent.putExtra(Constants.INTENT_SERIALIZED_ITEM, item);
        Lucinda.currentViewMode = mode;
        startActivity(intent);
    }

    @Override
    public void onCheckboxClicked(Item item, boolean checked) {
        log("...onCheckBoxClicked(Item, boolean) ", checked);
        if( checked){
            item.setState(checked ? State.DONE: State.TODO);
            int stat = ItemsWorker.update(item, this);
            log("...stat (should be 1)", stat);
            if( stat != 1){
                Toast.makeText(this, "error updating item", Toast.LENGTH_LONG).show();
            }
/*            try {
                if( item.isInfinite() && !item.isDone() && checked){
                    log("...item is template");
                    ItemsWorker.handleTemplate(item, this);
                }else {
                    ItemsWorker.setItemState(item, State.DONE, this);
                    ItemsWorker.touchParents(item, this);
                }
                show(mode);
            } catch (SQLException e) {
                e.printStackTrace();
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }*/
        }else{
            Toast.makeText(this, "work to be done, which state? ", Toast.LENGTH_LONG).show();
        }
    }
    private int getItemID(ViewMode mode){
        switch (mode){
            case PROJECTS:
                return R.id.bottomNavigation_projects;
            case ENCHILADA:
                return R.id.bottomNavigation_enchilada;
            case TODO:
                return R.id.bottomNavigation_todo;
            case TODAY:
            default:
                return R.id.bottomNavigation_today;
        }
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        log("...onRestoreInstanceState(Bundle)");
        int ordinal  = savedInstanceState.getInt(Constants.VIEW_MODE_ORDINAL);
        mode = ViewMode.values()[ordinal];
        log("...mode", mode.toString());
        bottomNavigationView.setSelectedItemId(getItemID(mode));
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        log("...onSaveInstanceState(Bundle) mode", mode.toString());
        outState.putInt(Constants.VIEW_MODE_ORDINAL, mode.ordinal());
    }

    private void restoreInstance(Bundle bundle){
        log("restoreInstance()");
        int ordinal  = bundle.getInt(Constants.VIEW_MODE_ORDINAL);
        mode = ViewMode.values()[ordinal];
        log("...mode", mode.toString());
        bottomNavigationView.setSelectedItemId(getItemID(mode));
        //super.onRestoreInstanceState(savedInstanceState);

    }
    private void setUserInterface(){
        log("...setUserInterface()");
        textViewDate.setText(currentDate.toString());

    }

    /**
     * make sure to set currentParent before calling this one
     * TODO, fix projects, regarding the above statement
     * @param mode
     */
    private void show(ViewMode mode){
        log("...show(Mode)", mode.toString());
        if( currentParent != null){
            log("...currentParent", currentParent.getHeading());
        }else{
            log("...currentParent is null");
        }
        switch (mode){
            case CHILDREN:
            case APPOINTMENTS:
                items = ItemsWorker.selectChildren(currentParent, this);
                break;
            case WIP:
                items = ItemsWorker.selectItems(State.WIP, this);
                break;
            case PROJECTS:
                currentParent = ItemsWorker.getRootItem(Settings.Root.PROJECTS, this);
                items = ItemsWorker.selectChildren(currentParent, this);
                break;
/*            case TEMPLATES:
                items = ItemsWorker.selectItems(Type.TEMPLATE, this);
                break;*/
            case TODAY:
                currentParent = ItemsWorker.getTodayParent(this);
                items = ItemsWorker.selectTodayList(LocalDate.now(), this);
                break;
            case TODO:
                currentParent = ItemsWorker.getRootItem(Settings.Root.TODO, this);
                items = ItemsWorker.selectItems(State.TODO, this);
                break;
            case ENCHILADA:
                items = ItemsWorker.selectItems(this);
                break;
        }
        setTitle(currentParent.getHeading());
        items.sort(Comparator.comparingLong(Item::compare));
        adapter.setList(items);
    }
    private void showAddItemDialog(){
        log("...showAddItemDialog()", mode.toString());
        if( mode.equals(ViewMode.APPOINTMENTS)){
            AddAppointmentDialog addAppointmentDialog = new AddAppointmentDialog();
            addAppointmentDialog.setCallback(item -> {
                log("...onNewAppointment(Item)", item.getHeading());
                item = ItemsWorker.insertChild(currentParent, item, this);
                items.add(item);
                show(ViewMode.APPOINTMENTS);
            });
            addAppointmentDialog.show(getSupportFragmentManager(), "add appointment");
        }else if(mode.equals(ViewMode.TODAY)){
            AddTemplateDialog dialog = new AddTemplateDialog(currentParent);
            dialog.setCallback(item ->{
                    log("...onNewItem(Item)");
                    item = ItemsWorker.insertChild(currentParent, item, this);
                    items.add(item);
                    show(ViewMode.TODAY);
            });
            dialog.show(getSupportFragmentManager(),"add template");
        }else {
            AddItemDialog addItemDialog = new AddItemDialog(currentParent);
            switch (mode) {
                case TODAY:
                    addItemDialog.setState(State.INFINITE);
                    break;
                case WIP:
                    addItemDialog.setState(State.WIP);
                    break;
                case TODO:
                case PROJECTS:
                    addItemDialog.setState(State.TODO);
                    break;
            }
            addItemDialog.show(getSupportFragmentManager(), "add item");
        }
    }
    private void showAffirmation(){
        log("...showAffirmation");
        WebWorker.requestAffirmation(affirmation -> {
            log("...onRequest(Affirmation)", affirmation.getAffirmation());
            Toast.makeText(this, affirmation.getAffirmation(), Toast.LENGTH_LONG).show();
        });

    }

    private void showChildren(Item parent){
        log("...showChildren(Item)");
        currentParent = parent;
        items = ItemsWorker.selectChildren(currentParent, this);
        adapter.setList(items);
    }
    private void showDateDialog(){
        log("...showDateDialog()");

    }
    private void showEstimate(){
        log("...showEstimate()");
        EstimateItemsDialog estimateDateDialog = new EstimateItemsDialog(items);
        estimateDateDialog.show(getSupportFragmentManager(), "estimate me");

    }
    private void showPeriodDialog(){
        log("...showPeriodDialog()");
        AddPeriodDialog dialog = new AddPeriodDialog();
        dialog.setListener(period -> {
            log("...onPeriod(Period)", period.toString());
            log(period);
        });
        dialog.show(getSupportFragmentManager(), "add period");
    }
    private void showStatistics(){
        log("...showStatistics()");
        StatisticsDialog dialog = new StatisticsDialog(this);
        dialog.show(getSupportFragmentManager(), "you statistics lover");
    }
    private void startSequence(){
        log("...startSequence()");
        Intent intent = new Intent(this, SequenceActivity.class);
        intent.putExtra(Constants.INTENT_SEQUENCE_PARENT, currentParent);
        startActivity(intent);

    }
}