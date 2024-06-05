package se.curtrune.lucy.fragments;

import static se.curtrune.lucy.util.Logger.log;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import se.curtrune.lucy.R;
import se.curtrune.lucy.activities.ItemSession;
import se.curtrune.lucy.adapters.CalenderAdapter;
import se.curtrune.lucy.adapters.CalenderDateAdapter;
import se.curtrune.lucy.app.Settings;
import se.curtrune.lucy.classes.CallingActivity;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.State;
import se.curtrune.lucy.classes.calender.Week;
import se.curtrune.lucy.dialogs.AddTemplateDialog;
import se.curtrune.lucy.dialogs.OnNewItemCallback;
import se.curtrune.lucy.util.Constants;
import se.curtrune.lucy.viewmodel.LucindaViewModel;
import se.curtrune.lucy.workers.AffirmationWorker;
import se.curtrune.lucy.workers.ItemsWorker;
import se.curtrune.lucy.workers.NotificationsWorker;
import se.curtrune.lucy.workers.StatisticsWorker;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalenderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalenderFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button buttonPrev;
    private Button buttonNext;
    private Week currentWeek;
    private RecyclerView recycler;
    private RecyclerView recyclerDates;
    private CalenderAdapter adapter;
    private CalenderDateAdapter calenderDateAdapter;
    private FloatingActionButton buttonAddItem;

    private TextView labelMonthYear;

    private ItemTouchHelper itemTouchHelper;

    private LocalDate currentDate;
    private List<Item> items;
    private Month month;
    public static boolean VERBOSE = false;
    private LucindaViewModel viewModel;
    public CalenderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CalenderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CalenderFragment newInstance(String param1, String param2) {
        CalenderFragment fragment = new CalenderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        log("CalenderFragment.onCreateView(...)");
        View view = inflater.inflate(R.layout.calender_fragment, container, false);
        getActivity().setTitle("");
        initDefaults();
        initComponents(view);
        initRecycler();
        initRecyclerDates();
        initListeners();
        initSwipe();
        initViewModel();
        setUserInterface(currentDate);
        calculateEstimate();
        return view;
    }
    private void calculateEstimate(){
        if( VERBOSE)  log("...calculateEstimate()");
        StatisticsWorker.getEstimate(currentDate, getContext());

    }
    private String getMonthYear(LocalDate date){
        if( VERBOSE) log("...getMonthYear(LocalDate)");
        return String.format("%s, %d", date.getMonth().toString(), date.getYear());
    }
    private String getWeekNumber(){
        log("...getWeekNumber()");
        //Calendar calendar = Calendar.getInstance(Locale.getDefault());
        //calendar.set(currentDate.getYear(), currentDate.getMonthValue(), currentDate.getDayOfMonth());
        //int weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);
        return String.valueOf(currentWeek.getWeekNumber());

    }
    private void initComponents(View view){
        if( VERBOSE) log("...initComponents(View)");
        buttonNext = view.findViewById(R.id.calenderFragment_buttonNext);
        buttonPrev = view.findViewById(R.id.calenderFragment_buttonPrev);
        labelMonthYear = view.findViewById(R.id.calenderFragment_labelMonthYear);
        recycler = view.findViewById(R.id.calenderFragment_recycler);
        recyclerDates = view.findViewById(R.id.calenderFragment_recyclerDates);
        buttonAddItem = view.findViewById(R.id.calenderFragment_addItem);
    }
    private void initDefaults(){
        if( VERBOSE) log("...initDefaults()");
        currentDate = LocalDate.now();
        currentWeek = new Week(currentDate);
        items = new ArrayList<>();
    }
    private void initListeners(){
        if( VERBOSE) log("...initListeners()");
        buttonPrev.setOnClickListener(view->prevWeek());
        buttonNext.setOnClickListener(view->nextWeek());
        buttonAddItem.setOnClickListener(view->showAddItemDialog());
    }
    private void initRecycler(){
        if( VERBOSE) log("...initRecycler()");
        adapter = new CalenderAdapter(items, new CalenderAdapter.Callback() {
            @Override
            public void onEditTime(Item item) {
                if( VERBOSE) log("...onEditTime(Item item");
                updateTargetTime(item);
            }

            @Override
            public void onItemClick(Item item) {
                if(VERBOSE)log("...onItemClick(Item)", item.getHeading());
                Intent intent = new Intent(getContext(), ItemSession.class);
                intent.putExtra(Constants.INTENT_CALLING_ACTIVITY, CallingActivity.CALENDER_FRAGMENT);
                intent.putExtra(Constants.INTENT_SERIALIZED_ITEM, item);
                startActivity(intent);
            }

            @Override
            public void onLongClick(Item item) {
                log("...onLongClick(Item)");
            }

            @Override
            public void onCheckboxClicked(Item item, boolean checked) {
                if( VERBOSE) log("...onCheckboxClicked(Item, boolean)", checked);
                item.setState(checked ? State.DONE: State.TODO);
                item.setTargetTime(LocalTime.now());
                log(item);
                int rowsAffected = ItemsWorker.update(item, getContext());
                if( rowsAffected != 1){
                    Toast.makeText(getContext(),"error updating item", Toast.LENGTH_LONG).show();
                    return;
                }else{
                    items.sort(Comparator.comparingLong(Item::getTargetTimeSecondOfDay));
                    adapter.notifyDataSetChanged();
                }
                if(item.hasReward()){
                    switch (item.getReward().getType()){
                        case AFFIRMATION:
                            //log("...AFFIRMATION");
                            //showAffirmation();
                            break;
                        case USER_DEFINED:
                            log("...USER_DEFINED");
                            break;
                        case CONFETTI:
                            log("...CONFETTI");
                            break;
                    }
                }
                setUserInterface(currentDate);
            }
        });
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setAdapter(adapter);

    }
    private void initRecyclerDates(){
        if( VERBOSE) log("...initRecyclerDates()");
        calenderDateAdapter = new CalenderDateAdapter(currentWeek, new CalenderDateAdapter.Callback() {
            @Override
            public void onDateSelected(LocalDate date) {
                log("...onDateSelected(LocalDate)", date.toString());
                currentDate = date;
                currentWeek.setCurrentDate(currentDate);
                calenderDateAdapter.setList(currentWeek);
                setUserInterface(date);
            }
        });
        recyclerDates.setLayoutManager(new GridLayoutManager(getContext(), 7));
        recyclerDates.setItemAnimator(new DefaultItemAnimator());
        recyclerDates.setAdapter(calenderDateAdapter);
    }
    private void initSwipe() {
        if( VERBOSE) log("...initSwipe()");
        itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int index = viewHolder.getAdapterPosition();
                Item item = items.get(viewHolder.getAdapterPosition());
                //Item item = items.remove(index);
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("postbone " + item.getHeading());
                builder.setMessage("are you sure? ");
                builder.setPositiveButton("postbone", (dialog, which) ->{
                    log("...on positive button click");
                    item.setTargetDate(LocalDate.now().plusDays(1));
                    items.remove(item);
                    int rowsAffected = ItemsWorker.update(item, getContext());
                    log(item);
                    if( rowsAffected != 1){
                        log("ERROR updating item", item.getHeading());
                    }else{
                        adapter.notifyDataSetChanged();
                    }
/*
                        adapter.notifyItemRemoved(index);
                        Toast.makeText(TodayActivity.this, "item deleted", Toast.LENGTH_LONG).show();
                    }else{
                        log("error deleting item");
                        Toast.makeText(TodayActivity.this, "error deleting item", Toast.LENGTH_LONG).show();
                    }*/
                });
                builder.setNegativeButton("cancel", (dialog, which) -> {log(
                        "...on negative button click");
                        adapter.notifyDataSetChanged();
                });
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });
        itemTouchHelper.attachToRecyclerView(recycler);
    }
    private void initViewModel(){
        if( VERBOSE)  log("...initViewModel()");
        viewModel = new ViewModelProvider(requireActivity()).get(LucindaViewModel.class);

    }
    private void nextWeek(){
        log("...nextWeek()");
        currentDate = currentDate.plusWeeks(1);
        currentWeek = new Week(currentDate);
        setUserInterface(currentDate);
        calenderDateAdapter.setList(currentWeek);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        log("CalenderFragment.onOptionsItemSelected(MenuItem) ", item.getTitle().toString());
        return super.onOptionsItemSelected(item);
    }

    private void prevWeek(){
        log("...prevWeek()");
        currentDate = currentDate.minusWeeks(1);
        currentWeek = new Week(currentDate);
        setUserInterface(currentDate);
        calenderDateAdapter.setList(currentWeek);
    }
    private void setUserInterface(LocalDate date){
        if( VERBOSE) log("...setUserInterface()", date.toString());
        labelMonthYear.setText(getMonthYear(currentDate));
        if( currentDate.equals(LocalDate.now())) {
            items = ItemsWorker.selectTodayList(currentDate, getContext());
        }else{
            items = ItemsWorker.selectAppointments( currentDate, getContext());
        }
        items.sort(Comparator.comparingLong(Item::compareTargetTime));
        adapter.setList(items);
        viewModel.updateEnergy(true);
    }
    private void showAddItemDialog(){
        log("...showAddItemDialog()");
        AddTemplateDialog dialog = new AddTemplateDialog(ItemsWorker.getRootItem(Settings.Root.DAILY, getContext()), currentDate);
        dialog.setCallback(new OnNewItemCallback() {
            @Override
            public void onNewItem(Item item) {
                log("...onNewItem(Item)");
                log(item);
                item = ItemsWorker.insert(item, getContext());
                items.add(item);
                if(item.hasNotification()){
                    log("...item has notification");
                    NotificationsWorker.setNotification(item, getContext());
                }
                updateAdapter();
            }
        });
        dialog.show(getParentFragmentManager(), "add item");
    }
/*    private void showAffirmation(){
        log("...showAffirmation");
        AffirmationWorker.requestAffirmation(affirmation -> {
            log("...onRequest(Affirmation)", affirmation.getAffirmation());
            Toast.makeText(getContext(), affirmation.getAffirmation(), Toast.LENGTH_LONG).show();
        });
    }*/
    private void updateTargetTime(Item item){
        log("...updateTargetTime(Item)");
        LocalTime oldTime = item.getTargetTime();
        int minutes = oldTime.getMinute();
        int hour = oldTime.getHour();
        TimePickerDialog timePicker = new TimePickerDialog(getContext(), (view, hourOfDay, minute) -> {
            LocalTime targetTime = LocalTime.of(hourOfDay, minute);
            item.setTargetTime(targetTime);
            int rowsAffected = ItemsWorker.update(item, getContext());
            if(rowsAffected != 1){
                log("ERROR updating time of item",item.getHeading());
            }
            updateAdapter();
        }, hour, minutes, true);
        timePicker.show();
    }
    private void updateAdapter(){
        log("...updateAdapter()");
        items.sort(Comparator.comparingLong(Item::compareTargetTime));
        adapter.setList(items);
        viewModel.updateEnergy(true);
    }
}