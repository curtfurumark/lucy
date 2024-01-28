package se.curtrune.lucy.activities;

import static se.curtrune.lucy.util.Logger.log;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.SQLException;
import java.util.List;

import se.curtrune.lucy.R;
import se.curtrune.lucy.adapters.ItemAdapter;
import se.curtrune.lucy.classes.CallingActivity;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.State;
import se.curtrune.lucy.classes.Type;
import se.curtrune.lucy.dialogs.AddItemDialog;
import se.curtrune.lucy.util.Constants;
import se.curtrune.lucy.util.ItemStack;
import se.curtrune.lucy.workers.ItemsWorker;

public class ItemsActivity extends AppCompatActivity implements ItemAdapter.Callback, AddItemDialog.Callback {
    private ItemAdapter adapter;
    private RecyclerView recycler;
    private Item currentParent;
    private EditText editTextSearch;
    //private List<Item> items;
    private ItemsWorker worker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.items_activity);
        log("ItemsActivity.onCreate(Bundle)");
        setTitle("items");
        worker = ItemsWorker.getInstance();
        initDefaults();
        initComponents();
        initListeners();
        Intent intent = getIntent();
        if( intent.getBooleanExtra(Constants.INTENT_SHOW_CHILD_ITEMS, false)){
            log("INTENT_SHOW_CHILD_ITEMS");
            currentParent = (Item) intent.getSerializableExtra(Constants.INTENT_SERIALIZED_ITEM);
            if(  currentParent == null){
                Toast.makeText(this, "currentParent is null", Toast.LENGTH_LONG).show();
            }else{
                setTitle(currentParent.getHeading());
            }
        }
        if( currentParent !=  null) {
            initRecycler(worker.selectChildItems(currentParent, this));
        }
    }
    private void addItem(){
        log("...addItem()");
        String heading = "";
        Type type = Type.PENDING;
        AddItemDialog addItemDialog = new AddItemDialog(currentParent);
        if( currentParent != null) {
            heading = currentParent.getHeading();
            type = currentParent.getType();
            addItemDialog.setHeading(heading);
            addItemDialog.setCategory(type.toString());
            worker.touch(currentParent, this);
        }

        addItemDialog.show(getSupportFragmentManager(), "add item");

    }
    private void ascend(){
        log("...ascend()");
        if(currentParent == null){
            log("currentParent is null, do not ascend");
            Toast.makeText(this, "reaching for the stars, sorry", Toast.LENGTH_LONG).show();
            return;
        }
        if( currentParent.isRoot()){
            log("currentParent is root");
            Toast.makeText(this, "top level", Toast.LENGTH_LONG).show();
            adapter.setList(worker.selectChildItems(null, this));
            return;
        }
        currentParent = currentParent.getParent();
        if( currentParent == null){
            Toast.makeText(this, "get your shit together", Toast.LENGTH_LONG);
            return;
        }
        setTitle(currentParent.getHeading());
        adapter.setList(currentParent.getChildren());
    }
    private void initComponents(){
        log("...initComponents()");
        recycler = findViewById(R.id.itemsActivity_recycler);
        editTextSearch = findViewById(R.id.itemsActivity_search);
    }
    private void initDefaults(){
        log("...initDefaults()");
        //items = new ArrayList<>();
    }
    private void initListeners(){
        log("...initListeners()");
    }
    private void initRecycler(List<Item> items){
        log("...initRecycler(List<Item>) size ", items.size());
        adapter = new ItemAdapter(items, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recycler.setLayoutManager(layoutManager);
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onAddItem(Item item) {
        log("ItemsActivity.onAddItem(Item item)");
        if( currentParent == null){
            log("...currentParent is null");
            item.setParentId(-1);
        }else {
            item.setParentId(currentParent.getID());
        }
        try {
            item = worker.insert(item, this);
            adapter.insert(item);
        }catch(SQLException e){
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();

        }
    }
    @Override
    public void onCheckboxClicked(Item item, boolean checked) {
        log("...onCheckBoxClicked(Item, boolean");
        if( checked){
            try {
                worker.setItemState(item, State.DONE, this);
            } catch (SQLException e) {
                e.printStackTrace();
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(this, "work to be done, which state? ", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //log("...onCreateOptionsMenu(Menu)");
        getMenuInflater().inflate(R.menu.items_activity, menu);
        return true;
    }

    @Override
    public void onItemClick(Item item) {
        log("...onItemClick(Item)");
        if( item.hasChild()){
            currentParent = item;
            currentParent.setChildren(worker.selectChildItems(currentParent, this));
            adapter.setList(currentParent.getChildren());
        }else {
            Intent intent = new Intent(this, ItemSession.class);
            intent.putExtra(Constants.INTENT_ITEM_SESSION, true);
            intent.putExtra(Constants.CURRENT_ITEM_IS_IN_STACK, true);
            intent.putExtra(Constants.INTENT_CALLING_ACTIVITY, CallingActivity.ITEMS_ACTIVITY);
            ItemStack.currentItem = item;
            startActivity(intent);
        }
    }

    @Override
    public void onLongClick(Item item) {
        log("...onLongClick(Item item");
        Intent intent = new Intent(this, ItemEditor.class);
        intent.putExtra(Constants.INTENT_EDIT_ITEM, true);
        intent.putExtra(Constants.INTENT_CALLING_ACTIVITY, CallingActivity.ITEMS_ACTIVITY);
        intent.putExtra(Constants.INTENT_SERIALIZED_ITEM, item);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        log("...onOptionsItemSelected(MenuItem)", item.getTitle().toString());
        if( item.getItemId() == R.id.itemsActivity_home){
            startActivity(new Intent(this, HomeActivity.class));
        }else if( item.getItemId() == R.id.itemsActivity_ascend){
            ascend();
        }else if( item.getItemId() == R.id.itemsActivity_add){
            addItem();
        }
        return super.onOptionsItemSelected(item);
    }
}