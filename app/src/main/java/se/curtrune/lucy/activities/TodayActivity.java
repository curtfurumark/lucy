package se.curtrune.lucy.activities;

import static se.curtrune.lucy.util.Logger.log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import se.curtrune.lucy.R;
import se.curtrune.lucy.adapters.ItemAdapter;
import se.curtrune.lucy.classes.CallingActivity;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.State;
import se.curtrune.lucy.classes.Type;
import se.curtrune.lucy.dialogs.AddItemDialog;
import se.curtrune.lucy.util.Constants;
import se.curtrune.lucy.workers.ItemsWorker;

public class TodayActivity extends AppCompatActivity implements
        AddItemDialog.Callback,
        ItemAdapter.Callback {
    private RecyclerView recycler;
    private TextView textViewDate;
    private RadioButton radioButtonTodo;
    private RadioButton radioButtonWip;
    private RadioButton radioButtonAppointments;
    private RadioButton radioButtonToday;

    private enum Mode{
        TODAY, TODO, WIP, APPOINTMENTS
    }
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
        worker = ItemsWorker.getInstance();
        //items = ItemsWorker.selectItems(LocalDate.now(),this,  State.INFINITE);
        radioButtonTodo.setChecked(true);
        show(Mode.TODAY);

    }

    private void addItem(){
        log("...addItem()");
        AddItemDialog addItemDialog = new AddItemDialog( );
        addItemDialog.show(getSupportFragmentManager(), "add item");

    }
    private void initComponents(){
        log("...initComponents()");
        recycler = findViewById(R.id.todayActivity_recycler);
        textViewDate = findViewById(R.id.todayActivity_date);
        radioButtonToday = findViewById(R.id.todayActivity_todayButton);
        radioButtonTodo = findViewById(R.id.todayActivity_todoButton);
        radioButtonWip = findViewById(R.id.todayActivity_wipButton);
        radioButtonAppointments = findViewById(R.id.todayActivity_appointmentsButton);
    }

    private void initListeners(){
        radioButtonToday.setOnClickListener(view->show(Mode.TODAY));
        radioButtonTodo.setOnClickListener(view->show(Mode.TODO));
        radioButtonAppointments.setOnClickListener(view->show(Mode.APPOINTMENTS));
        radioButtonWip.setOnClickListener(view->show(Mode.WIP));


    }
    private void initRecycler(){
        log("...initRecycler()");
        adapter = new ItemAdapter(items, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recycler.setLayoutManager(layoutManager);
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setAdapter(adapter);
    }
    @Override
    public void onAddItem(Item item) {
        log("...onAddItem(Item)");
        try {
            item = ItemsWorker.insert(item, this);
            items.add(0, item);
            adapter.notifyItemInserted(0);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //log("...onCreateOptionsMenu(Menu)");
        getMenuInflater().inflate(R.menu.today_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if( item.getItemId() == R.id.todayActivity_home){
            startActivity(new Intent(this, HomeActivity.class));
        }else if( item.getItemId() == R.id.todayActivity_addItem){
            addItem();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(Item item) {
        log("...onItemClick(Item)");
        Intent intent = new Intent(this, ItemSession.class);
        intent.putExtra(Constants.INTENT_ITEM_SESSION, true);
        intent.putExtra(Constants.INTENT_CALLING_ACTIVITY, CallingActivity.TODAY_ACTIVITY);
        intent.putExtra(Constants.INTENT_SERIALIZED_ITEM, item);
        startActivity(intent);
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
            worker.setItemState(item, State.DONE, this);
        }else{
            Toast.makeText(this, "work to be done, which state? ", Toast.LENGTH_LONG).show();
        }
    }

    private void show(Mode mode){
        log("...show(Mode)", mode.toString());
        switch (mode){
            case WIP:
                items = ItemsWorker.selectItems(State.WIP, this);
                break;
            case TODAY:
                items = ItemsWorker.selectDateState( LocalDate.now(), State.INFINITE, this);
                break;
            case APPOINTMENTS:
                //ItemsWorker.selectItems(Type.APPOINTMENT, this);
                break;
            case TODO:
                items = ItemsWorker.selectItems(State.TODO, this);
                break;
        }
        adapter.setList(items);
    }


}