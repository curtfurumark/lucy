package se.curtrune.lucy.activities;

import static se.curtrune.lucy.util.Logger.log;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

import se.curtrune.lucy.R;
import se.curtrune.lucy.adapters.SequenceAdapter;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.State;
import se.curtrune.lucy.util.Constants;
import se.curtrune.lucy.workers.ItemsWorker;

public class SequenceActivity extends AppCompatActivity implements SequenceAdapter.Callback{

    //private TextView textViewHeading;
    private TextView textViewParentHeading;
    //private TextView textViewInfo;
    //private CheckBox checkBoxState;
    private TextView textViewNumberItems;
    private Item parentItem;
    private Item currentItem;
    private int currentItemIndex = 0;
    private List<Item> items;
    private RecyclerView recycler;
    private SequenceAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sequence_activity);
        log("SequenceActivity.onCreate(Bundle)");
        Intent intent = getIntent();
        parentItem = (Item) intent.getSerializableExtra(Constants.INTENT_SEQUENCE_PARENT);
        if( parentItem == null){
            log("...sorry but parentItem is null, i surrender");
            Toast.makeText(this, "no parentItem", Toast.LENGTH_LONG).show();
            return;
        }
        init();
        initComponents();
        initRecycler();
        initUserInterface();
        initListeners();
    }
    private void init(){
        log("...init()");
        items = ItemsWorker.selectChildren(parentItem, this);
        log("...number of items in sequence", items.size());
        if( items.size() < 1){
            Toast.makeText(this, "need at least one item for this thing to make sense", Toast.LENGTH_LONG).show();
            return;
        }
        currentItem = items.get(currentItemIndex);
    }
    private void initComponents(){
        log("...initComponents()");
        textViewNumberItems = findViewById(R.id.sequenceActivity_nItems);
        textViewParentHeading = findViewById(R.id.sequenceActivity_parentItem);
        recycler = findViewById(R.id.sequenceActivity_recycler);
    }
    private void initListeners(){
        log("...initListeners()");
/*        checkBoxState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( checkBoxState.isChecked()){
                    log("...checkBoxState isChecked");
                    setNextItem();
                }
            }
        });*/
    }
    private void initRecycler(){
        log("...initRecycler()");
        adapter = new SequenceAdapter(items, this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recycler.setLayoutManager(layoutManager);
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setAdapter(adapter);

    }
    private void initUserInterface(){
        log("...initUserInterface()");
        String textNumberItems = String.format(Locale.getDefault(), "number of activities in sequence %d", items.size());
        textViewNumberItems.setText(textNumberItems);
        setUserInterface(items.get(currentItemIndex));
        textViewParentHeading.setText(parentItem.getHeading());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sequence_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if( item.getItemId() == R.id.sequenceActivity_home){
            startActivity(new Intent(this, HomeActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
    private void setNextItem(){
        log("...setNextItem() ", currentItemIndex);
        if( currentItemIndex >= items.size() -1){
            log("...end of the sequence");
            Toast.makeText(this, "congrats you're done", Toast.LENGTH_LONG).show();
            return;
        }
        setUserInterface(items.get(++currentItemIndex));
    }
    private void setUserInterface(Item item){
        log("...setUserInterface(Item) index", currentItemIndex);
        String textInfo = String.format(Locale.getDefault(), "%d/%d done", currentItemIndex, items.size());
        textViewNumberItems.setText(textInfo);
        currentItem = item;
    }

    @Override
    public void onItemClick(Item item) {
        log("...onItemClick(Item)");
    }

    @Override
    public void onLongClick(Item item) {

    }

    @Override
    public void onCheckboxClicked(Item item, boolean checked) {
        log("...onCheckboxClicked(Item, boolean)", checked);
        if( checked){
            currentItem.setState(State.DONE);
            int rowsAffected = ItemsWorker.update(item, this);
            if( rowsAffected != 1){
                log("WARNING, error updating state of item");
            }
            setNextItem();
        }else{
            Toast.makeText(this, "unCheck not implemented", Toast.LENGTH_LONG).show();
        }
    }
}