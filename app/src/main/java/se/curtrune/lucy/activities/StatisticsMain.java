package se.curtrune.lucy.activities;

import static se.curtrune.lucy.util.Logger.log;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.tabs.TabLayout;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import se.curtrune.lucy.R;
import se.curtrune.lucy.adapters.ListableAdapter;
import se.curtrune.lucy.classes.Listable;
import se.curtrune.lucy.fragments.DurationFragment;
import se.curtrune.lucy.fragments.TopTenFragment;
import se.curtrune.lucy.workers.StatisticsWorker;


public class StatisticsMain extends AppCompatActivity  {
    private TabLayout tabLayout;
    private LocalDate date;
    public static boolean VERBOSE = true;
    private ListableAdapter adapter;
    private List<Listable> items = new ArrayList<>();
    private StatisticsWorker worker;
    private enum Mode{
        PENDING, TYPE_STATISTICS, ITEMS
    }
    private Mode mode = Mode.TYPE_STATISTICS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistics_main);
        log("StatisticsMain.onCreate(Bundle)");
        setTitle(R.string.statistics);
        initComponents();
        initListeners();
/*        initRecycler(items);
        setDefaults();
        initUserInterface();
        worker = StatisticsWorker.getInstance();
        List<Item> items = worker.selectItems(LocalDate.now(), this);
        log("...number of items today", items.size());
        if( items.size() == 0){
            Toast.makeText(this, "no items yet", Toast.LENGTH_LONG).show();
        }
        List<Listable> listables = new ArrayList<>();
        items.forEach(Logger::log);
        for( Item item : items){
            listables.add(item);
        }
        setUserInterface(listables);*/

    }
    private void initComponents(){
        log("...initComponents()");
        tabLayout = findViewById(R.id.statisticsMain_tabLayout);
    }
   private void initListeners(){
        log("...initListeners()");
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position =  tab.getPosition();
                log("...onTabSelected(Tab) position", position);
                switch (position){
                    case 0:
                        getSupportFragmentManager().beginTransaction().replace(R.id.statisticsMain_fragmentContainer, new DurationFragment()).commit();
                        break;
                    case 1:
                        //getSupportFragmentManager().beginTransaction().replace(R.id.statisticsMain_fragmentContainer, new MentalFragment()).commit();
                        log(" MentalFragment, discontinued");
                        break;
                    case 2:
                        getSupportFragmentManager().beginTransaction().replace(R.id.statisticsMain_fragmentContainer, new TopTenFragment()).commit();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                log("...onTabUnselected(TabLayout)");
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                log("...onTabReselected(TabLayout)");
            }
        });
    }
/*    private void initRecycler(List<Listable> items){
        if( VERBOSE) log("ItemsActivity.initRecycler(List<Item>)");
        adapter = new ListableAdapter(items, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recycler.setLayoutManager(layoutManager);
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void initUserInterface(){
        if( date == null){
            Toast.makeText(this, "please set defaults", Toast.LENGTH_LONG).show();
            return;
        }
        textViewDate.setText(date.toString());

    }
*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.statistics_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
/*
    @Override
    public void onItemClick(Listable item) {
        log("StatisticsMain.onItemClick(Listable)");
        if( item instanceof TypeStatistic){
            TypeStatistic typeStatistic = (TypeStatistic) item;
            adapter.setList(worker.getItems(((TypeStatistic) item).getType()));
        }else if(item instanceof Item){
            Intent intent = new Intent(this, ItemEditor.class);
            intent.putExtra(Constants.INTENT_EDIT_ITEM, true);
            intent.putExtra(Constants.INTENT_CALLING_ACTIVITY, CallingActivity.STATISTICS_ACTIVITY);
            intent.putExtra(Constants.INTENT_SERIALIZED_ITEM, (Item) item);
            ItemStack.currentItem = (Item) item;
            startActivity(intent);
        }
    }

    @Override
    public void onLongClick(Listable item) {

    }
    */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if( item.getItemId() == R.id.statistics_home){
            startActivity(new Intent(this, DevActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

/*
    private void showDateDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this);
        datePickerDialog.setOnDateSetListener((view1, year, month, dayOfMonth) -> {
            date = LocalDate.of(year, month + 1, dayOfMonth);
            textViewDate.setText(date.toString());
            worker.requestStats(date, this);
        });
        datePickerDialog.show();
    }
    @Override
    public void onUpdateItems(List<Listable> items) {
        log("StatisticsMain.onUpdateItems()");
        this.items = items;
        items.sort((item1, item2) -> Long.compare(item2.compare(), item1.compare()));
        adapter.setList(items);
    }

    @Override
    public void onUpdateStats(List<Listable> stats) {
        log("MentalStatistics.onUpdateStats(List<Listable>)");
        stats.sort((item1, item2) -> Long.compare(item2.compare(), item1.compare()));
        adapter.setList(stats);
    }

    @Override
    public void onUpdateStats(DailyStatistics statistics) {
        log("StatisticsMain.onUpdateStats(DailyStatistics)");
        if( statistics == null){
            Toast.makeText(this, "bummer statistics is null", Toast.LENGTH_LONG).show();
            return;
        }
        //adapter.setList(statistics.);
        textViewTotal.setText(Converter.formatSecondsWithHours(statistics.getTotalSeconds()));
    }

    private void setDefaults(){
        log("StatisticsMain.setDefaults()");
        date = LocalDate.now();
    }
    private void setUserInterface(List<Listable> listables){
        log("...setUserInterface(List<Listable>", listables.size());
        long duration = 0;
        for(Listable listable: listables){
            duration += ((Item)listable).getDuration();
        }
        textViewTotal.setText(Converter.formatSecondsWithHours(duration));
        adapter.setList(listables);*/
    //}
}