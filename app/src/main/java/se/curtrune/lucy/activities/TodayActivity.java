package se.curtrune.lucy.activities;

import static se.curtrune.lucy.util.Logger.log;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
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

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import se.curtrune.lucy.R;
import se.curtrune.lucy.adapters.ItemAdapter;
import se.curtrune.lucy.classes.App;
import se.curtrune.lucy.classes.CallingActivity;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.State;
import se.curtrune.lucy.classes.Type;
import se.curtrune.lucy.dialogs.AddItemDialog;
import se.curtrune.lucy.dialogs.StatisticsDialog;
import se.curtrune.lucy.enums.ViewMode;
import se.curtrune.lucy.util.Constants;
import se.curtrune.lucy.workers.ItemsWorker;
import se.curtrune.lucy.workers.StatisticsWorker;

public class TodayActivity extends AppCompatActivity implements
        AddItemDialog.Callback,
        ItemAdapter.Callback {
    private RecyclerView recycler;
    private TextView textViewDate;
    private EditText editTextSearch;
    private TextView textViewCurrentEnergy;
    private ItemTouchHelper itemTouchHelper;
    /*
    private RadioButton radioButtonTodo;
    private RadioButton radioButtonWip;
    private RadioButton radioButtonAppointments;
    private RadioButton radioButtonToday;*/
    private BottomNavigationView bottomNavigationView;
    private Item currentParent;



/*    private enum Mode{
        TODAY, TODO, WIP, APPOINTMENTS, TEMPLATE, PROJECTS, CHILDREN
    }*/
    private ViewMode mode = ViewMode.TODAY;
    private ItemAdapter adapter;
    private ItemsWorker worker;
    private List<Item> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.today_activity);
        log("TodayActivity.onCreate()");
        initComponents();
        initListeners();
        initRecycler();
        initSwipe();
        worker = ItemsWorker.getInstance();
        //radioButtonToday.setChecked(true);
        show(mode);
    }
    private void ascend(){
        log("...ascend()");
        currentParent = ItemsWorker.getParent(currentParent, this);
        if(currentParent == null){
            log("...top of the world");
            Toast.makeText(this, "top of the world", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        }else {
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
        textViewDate = findViewById(R.id.todayActivity_date);
        textViewCurrentEnergy = findViewById(R.id.todayActivity_currentEnergy);
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        editTextSearch = findViewById(R.id.todayActivity_search);
    }

    private void initListeners(){
        log("...initListeners()");
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            log("...onNavigationItemSelected()", item.getTitle().toString());
            if (item.getItemId() == R.id.bottomNavigation_projects){
                show( ViewMode.PROJECTS);
            }else if( item.getItemId() == R.id.bottomNavigation_today){
                show(ViewMode.TODAY);
            }else if( item.getItemId() == R.id.bottomNavigation_statistics){
                //startActivity(new Intent(this, StatisticsActivity.class));
                Toast.makeText(this, "not implemented", Toast.LENGTH_LONG).show();
            }else if( item.getItemId() == R.id.bottomNavigation_todo){
                show(ViewMode.TODO);
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
                if( delete(item)) {
                    adapter.notifyItemRemoved(index);
                    Toast.makeText(TodayActivity.this, "item deleted", Toast.LENGTH_LONG).show();
                }else{
                    log("error deleting item");
                    Toast.makeText(TodayActivity.this, "error deleting item", Toast.LENGTH_LONG).show();
                }
            }
        });
        itemTouchHelper.attachToRecyclerView(recycler);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.today_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        log("...onOptionsItemSelected(MenuItem)", item.getTitle().toString());
        if( item.getItemId() == R.id.todayActivity_home){
            ascend();
        }else if( item.getItemId() == R.id.todayActivity_addItem){
            showAddItemDialog();
        }else if( item.getItemId() == R.id.todayActivity_viewStatistics){
            showStatistics();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(Item item) {
        log("...onItemClick(Item)");
        if(item.hasChild()){
            setTitle(item.getHeading());
            items = worker.selectChildItems(item, this);
            currentParent = item;
            adapter.setList(items);
        }else {
            Intent intent = new Intent(this, ItemSession.class);
            intent.putExtra(Constants.INTENT_ITEM_SESSION, true);
            intent.putExtra(Constants.INTENT_CALLING_ACTIVITY, CallingActivity.TODAY_ACTIVITY);
            intent.putExtra(Constants.INTENT_SERIALIZED_ITEM, item);
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
        startActivity(intent);
    }

    @Override
    public void onCheckboxClicked(Item item, boolean checked) {
        log("...onCheckBoxClicked(Item, boolean");
        if( checked){
            try {
                worker.setItemState(item, State.DONE, this);
                show(mode);
            } catch (SQLException e) {
                e.printStackTrace();
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
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
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        outPersistentState.putInt(Constants.VIEW_MODE_ORDINAL, mode.ordinal());
        super.onSaveInstanceState(outState, outPersistentState);
    }

    private void show(ViewMode mode){
        log("...show(Mode)", mode.toString());
        setTitle(mode.toString());
        textViewCurrentEnergy.setText(String.valueOf(StatisticsWorker.getCurrentEnergy(this)));
        switch (mode){
            case CHILDREN:
                items = ItemsWorker.selectChildren(currentParent, this);
            case WIP:
                items = ItemsWorker.selectItems(State.WIP, this);
                break;
            case PROJECTS:
                currentParent = null;
                items = ItemsWorker.selectChildren(currentParent, this);
                break;
            case TEMPLATES:
                items = ItemsWorker.selectItems(Type.TEMPLATE, this);
                break;
            case TODAY:
                currentParent = ItemsWorker.getTodayParent(this);
                items = ItemsWorker.selectTodayList(LocalDate.now(), this);
                break;
            case TODO:
                items = ItemsWorker.selectItems(State.TODO, this);
                break;
            case ENCHILADA:
                items = ItemsWorker.selectItems(this);
                break;
        }
        items.sort(Comparator.comparingLong(Item::compare));
        adapter.setList(items);
    }
    private void showAddItemDialog(){
        log("...showAddItemDialog()", mode.toString());
        AddItemDialog addItemDialog = new AddItemDialog( currentParent);
        switch (mode){
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
    @Override
    public void onAddItem(Item item) {
        log("...onAddItem(Item)", mode.toString());
        try {
            switch (mode) {
                case TODAY:
                case TODO:
                    item = ItemsWorker.insert(item, this);
                    items.add(0, item);
                    adapter.notifyItemInserted(0);
            }
        } catch (SQLException e) {
            log("exception", e.getMessage());
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    private void showStatistics(){
        log("...showStatistics()");
        Toast.makeText(this, "wip show stats", Toast.LENGTH_LONG).show();
        StatisticsDialog dialog = new StatisticsDialog();
        dialog.show(getSupportFragmentManager(), "you statistics lover");

    }
}