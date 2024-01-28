package se.curtrune.lucy.activities;


import static se.curtrune.lucy.util.Logger.log;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import se.curtrune.lucy.R;
import se.curtrune.lucy.adapters.MentalAdapter;
import se.curtrune.lucy.classes.Mental;
import se.curtrune.lucy.dialogs.MentalDialog;
import se.curtrune.lucy.workers.MentalWorker;


public class MentalListActivity extends AppCompatActivity implements
        MentalAdapter.Callback,
        MentalDialog.Callback{
    private boolean on_create = false;
    public static boolean VERBOSE = false;
    private List<Mental> items = new ArrayList<>();
    private RecyclerView recycler;
    private EditText editTextSearch;
    private RadioButton radioButtonMood;
    private RadioButton radioButtonAnxiety;
    private RadioButton radioButtonStress;
    private RadioButton radioButtonEnergy;

    private MentalAdapter adapter;
    private MentalWorker worker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mental_list_activity);
        on_create = true;
        log("MentalListActivity.onCreate(Bundle)");
        setTitle("go mental");
        initComponents();
        initListeners();
        worker = MentalWorker.getInstance();
        items = worker.selectMentals(this);
        initRecycler(items);
        on_create = false;
    }
    private void initComponents(){
        log("...initComponents()");
        recycler = findViewById(R.id.mentalList_recycler);
        editTextSearch = findViewById(R.id.mentalList_search);
        radioButtonAnxiety = findViewById(R.id.mentalList_radioButtonAnxiety);
        radioButtonEnergy = findViewById(R.id. mentalList_radioButtonEnergy);
        radioButtonMood = findViewById(R.id.mentalList_radioButtonMood);
        radioButtonStress = findViewById(R.id.mentalList_radioButtonStress);

    }
    private void initListeners(){
        log("...initListeners()");
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!on_create) {
                    filterProjectList(s.toString());
                }
            }
        });
        radioButtonStress.setOnClickListener(v -> adapter.show(MentalAdapter.Mode.STRESS));
        radioButtonMood.setOnClickListener(view->adapter.show(MentalAdapter.Mode.MOOD));
        radioButtonEnergy.setOnClickListener(view->adapter.show(MentalAdapter.Mode.ENERGY));
        radioButtonAnxiety.setOnClickListener(view->adapter.show(MentalAdapter.Mode.ANXIETY));
    }

    private void initRecycler(List<Mental> items) {
        log("...initRecycler(List<Mental>) size", items.size());
        adapter = new MentalAdapter(items, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recycler.setLayoutManager(layoutManager);
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setAdapter(adapter);
    }
    private void filterProjectList(String text) {
        List<Mental> filteredList = new ArrayList<>();
        for ( Mental mental: items){
            if ( mental.contains(text)){
                filteredList.add(mental);
            }
        }
        adapter.setList(filteredList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.mental_list_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.diaryList_home){
            startActivity(new Intent(this, HomeActivity.class));
        }else if(item.getItemId() ==R.id.diaryList_add) {
            showMentalDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(Mental item) {
        log("...onItemClick(Listable)");
        MentalDialog mentalDialog = new MentalDialog(item);
        mentalDialog.show(getSupportFragmentManager(),"whatever");
    }

    @Override
    public void onItemLongClick(Mental item) {

    }
    private void showMentalDialog(){
        MentalDialog dialog = new MentalDialog();
        dialog.show(getSupportFragmentManager(), "mental");
    }

    /**
     * callback for showMentalDialog
     * @param mental, new mental, or edited mental
     * @param mode, edited or created    */
    @Override
    public void onMental(Mental mental, MentalDialog.Mode mode) {
        log("MentalListActivity.onMental(Mental, Mode)");
        if( mode.equals(MentalDialog.Mode.CREATE)) {
            try {
                mental = worker.insert(mental, this);
                items.add(0, mental);
                adapter.notifyItemInserted(0);
            } catch (SQLException e) {
                e.printStackTrace();
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }else{
            worker.update(mental, this);
            adapter.notifyDataSetChanged();

        }
    }
}