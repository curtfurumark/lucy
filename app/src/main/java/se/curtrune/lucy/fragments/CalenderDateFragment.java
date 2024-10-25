package se.curtrune.lucy.fragments;

import static se.curtrune.lucy.util.Logger.log;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import se.curtrune.lucy.R;
import se.curtrune.lucy.adapters.CalenderAdapter;
import se.curtrune.lucy.adapters.CalenderDateAdapter;
import se.curtrune.lucy.app.Settings;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.State;
import se.curtrune.lucy.classes.calender.CalenderDate;
import se.curtrune.lucy.classes.calender.OnSwipeClickListener;
import se.curtrune.lucy.classes.calender.Week;
import se.curtrune.lucy.dialogs.AddItemDialog;
import se.curtrune.lucy.dialogs.PostponeDialog;
import se.curtrune.lucy.viewmodel.CalendarDateViewModel;
import se.curtrune.lucy.viewmodel.LucindaViewModel;
import se.curtrune.lucy.workers.CalenderWorker;
import se.curtrune.lucy.workers.ItemsWorker;


public class CalenderDateFragment extends Fragment {

    private Week currentWeek;
    private RecyclerView recycler;
    private RecyclerView recyclerDates;
    private CalenderAdapter adapter;
    private CalenderDateAdapter calenderDateAdapter;
    private FloatingActionButton buttonAddItem;

    private TextView labelMonthYear;

    private ItemTouchHelper itemTouchHelper;

    private LocalDate currentDate;
    private CalenderDate calenderDate;
    private List<Item> items;
    public static boolean VERBOSE = true;
    private LucindaViewModel lucindaViewModel;
    private CalendarDateViewModel calendarDateViewModel;
    public CalenderDateFragment() {
        currentDate = LocalDate.now();
    }
    public CalenderDateFragment(LocalDate date){
        this.currentDate = date;
    }
    private enum Mode{
        DEFAULT, CALENDAR_DATE
    }
    private Mode mode = Mode.DEFAULT;
    public CalenderDateFragment(CalenderDate calenderDate){
        log("CalendarDateFragment(CalendarDate)", calenderDate.getDate());
        this.calenderDate = calenderDate;
        this.currentWeek = new Week(calenderDate.getDate());
        this.currentDate = calenderDate.getDate();
        mode = Mode.CALENDAR_DATE;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        log("CalenderDateFragment.onCreateView(...)");
        View view = inflater.inflate(R.layout.calender_date_fragment, container, false);
        requireActivity().setTitle("");
        initDefaults();
        initComponents(view);
        initViewModels();
        initRecycler();
        initRecyclerDates();
        initListeners();
        initSwipeItems();
        initSwipeWeek();
        setUserInterface(currentDate);
        observe();
        return view;
    }


    /**
     * deletes and item AND its mental if such exists
     * @param item, the item to be deleted
     */
    private void deleteItem(Item item){
        log("....deleteItem(Item)", item.getHeading());
        boolean stat = calendarDateViewModel.delete(item, getContext());
        if(!stat){
            log("ERROR, deleting", item.getHeading());
            Toast.makeText(getContext(), "ERROR deleting, " + item.getHeading(), Toast.LENGTH_LONG).show();
        }
    }
    private String getMonthYear(LocalDate date){
        if( VERBOSE) log("...getMonthYear(LocalDate)");
        int weekNumber = CalenderWorker.getWeekNumber(date);
        String  str = date.format(DateTimeFormatter.ofPattern("MMM u"));
        //return String.format(Locale.getDefault(), "%s, %d", date.getMonth().toString(), date.getYear());
        return String.format(Locale.getDefault(), "< %s %s %d >", str, getString(R.string.week), weekNumber);
    }

    private void initComponents(View view){
        if( VERBOSE) log("...initComponents(View)");
        labelMonthYear = view.findViewById(R.id.calenderFragment_labelMonthYear);
        recycler = view.findViewById(R.id.calenderFragment_recycler);
        recyclerDates = view.findViewById(R.id.calenderFragment_recyclerDates);
        buttonAddItem = view.findViewById(R.id.calenderFragment_addItem);
    }
    private void initDefaults(){
        if( VERBOSE) log("...initDefaults()");
        if( currentDate == null) {
            currentDate = LocalDate.now();
        }
        currentWeek = new Week(currentDate);
        items = new ArrayList<>();
    }
    private void initListeners(){
        if( VERBOSE) log("...initListeners()");
        buttonAddItem.setOnClickListener(view->showAddItemDialog());
    }
    private void initRecycler(){
        if( VERBOSE) log("...initRecycler()");
        adapter = new CalenderAdapter(calendarDateViewModel.getItems().getValue(), new CalenderAdapter.Callback() {
            @Override
            public void onEditTime(Item item) {
                if( VERBOSE) log("...onEditTime(Item item");
                updateTargetTime(item);
            }

            @Override
            public void onItemClick(Item item) {
                if(VERBOSE)log("...onItemClick(Item)", item.getHeading());
                loadFragment(new ItemSessionFragment(item));
            }

            @Override
            public void onLongClick(Item item) {
                log("...onLongClick(Item)", item.getHeading());
/*                EditItemDialog dialog = new EditItemDialog(item);
                dialog.setCallback(item1 -> {
                    log("EditItemDialog.Callback.onUpdate(Item)");
                    log(item1);
                    int rowsAffected = ItemsWorker.update(item1, getContext());
                    if( rowsAffected != 1){
                        log("ERROR updating item", item1.getHeading());
                    }else{
                        log("...item updated");
                        items.sort(Comparator.comparingLong(Item::compareTargetTime));
                        adapter.notifyDataSetChanged();
                    }
                });
                dialog.show(getChildFragmentManager(), "edit item");*/
            }

            @Override
            public void onCheckboxClicked(Item item, boolean checked) {
                if( VERBOSE) log("...onCheckboxClicked(Item, boolean)", checked);
                item.setState(checked ? State.DONE: State.TODO);
                boolean stat = calendarDateViewModel.update(item, getContext());
                if( !stat){
                    log("ERROR updating item", item.getHeading());
                    Toast.makeText(getContext(), "ERROR updating item", Toast.LENGTH_SHORT).show();
                }else{
                    lucindaViewModel.updateEnergy(true);
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
                //setUserInterface(currentDate);
            }
        });
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setAdapter(adapter);
    }

    /**
     * the horizontal recycler with dates, monday to sunday
     */
    private void initRecyclerDates(){
        if( VERBOSE) log("...initRecyclerDates()");
        calenderDateAdapter = new CalenderDateAdapter(currentWeek, date -> {
            log("...onDateSelected(LocalDate)", date.toString());
            currentDate = date;
            calendarDateViewModel.set(currentDate, getContext());
            currentWeek.setCurrentDate(currentDate);
            calenderDateAdapter.setList(currentWeek);
            setUserInterface(date);
        });
        recyclerDates.setLayoutManager(new GridLayoutManager(getContext(), 7));
        recyclerDates.setItemAnimator(new DefaultItemAnimator());
        recyclerDates.setAdapter(calenderDateAdapter);
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerDates);
    }
    private void initSwipeItems() {
        if( VERBOSE) log("...initSwipeItems()");
        itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                //int index = viewHolder.getAdapterPosition();
                //Item item = items.get(viewHolder.getAdapterPosition());
                Item item = calendarDateViewModel.getItem(viewHolder.getAdapterPosition());
                if( direction == ItemTouchHelper.LEFT){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("delete" + item.getHeading());
                    builder.setMessage("are you sure? ");
                    builder.setPositiveButton("delete", (dialog, which) -> {
                        log("...on positive button click");
                        deleteItem(item);

                    });
                    builder.setNegativeButton("cancel", (dialog, which) -> {
                        log("...on negative button click");
                        adapter.notifyDataSetChanged();
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();

                }else if( direction == ItemTouchHelper.RIGHT) {
                    PostponeDialog dialog = new PostponeDialog();
                    dialog.setCallback(new PostponeDialog.Callback() {
                        @Override
                        public void postpone(PostponeDialog.Postpone postpone) {
                            log("PostponeDialog.postpone(Postpone)", postpone.toString());
                            CalenderDateFragment.this.postpone(item, postpone);
                        }

                        @Override
                        public void dismiss() {
                            log("...dismiss()");
                            adapter.notifyDataSetChanged();
                        }
                    });
                    dialog.show(getChildFragmentManager(), "postpone");
                }
            }
        });
        itemTouchHelper.attachToRecyclerView(recycler);
    }
    @SuppressLint("ClickableViewAccessibility")
    private void initSwipeWeek(){
        log("...initSwipeWeek()");
        OnSwipeClickListener onSwipeClickListener = new OnSwipeClickListener(getContext(), new OnSwipeClickListener.Listener() {
            @Override
            public void onSwipeRight() {
                 nextWeek();
            }

            @Override
            public void onSwipeLeft() {
                prevWeek();
            }

            @Override
            public void onClick() {
                showDatePicker();
            }
        });
        labelMonthYear.setOnTouchListener(onSwipeClickListener);
    }
    private void initViewModels(){
        if( VERBOSE)  log("...initViewModels()");
        lucindaViewModel = new ViewModelProvider(requireActivity()).get(LucindaViewModel.class);
        lucindaViewModel.setRecyclerMode(LucindaViewModel.RecyclerMode.DEFAULT);
        lucindaViewModel.getRecyclerMode().observe(getViewLifecycleOwner(), recyclerMode ->{
            log("CalenderDateFragment....recyclerMode", recyclerMode.toString());
            if( recyclerMode.equals(LucindaViewModel.RecyclerMode.MENTAL_COLOURS)){
                calendarDateViewModel.setEnergyItems();
            }else{
                log(" setting list to DEFAULT");
                //TODO
                //setUserInterface(currentDate);
                // calendarDateViewModel.set(currentDate, getContext());
            }
        } );
        lucindaViewModel.updateEnergy(getContext());
        calendarDateViewModel = new ViewModelProvider(requireActivity()).get(CalendarDateViewModel.class);
        if( calenderDate != null){
            log("...calendarDate != null");
            calendarDateViewModel.set(calenderDate);
        }else {
            calendarDateViewModel.set(currentDate, getContext());
        }
/*        calendarDateViewModel.getItems().observe(requireActivity(), items ->{
            log("...onChanged(List<Item>)");
                this.items = items;
                adapter.notifyDataSetChanged();
            });*/
    }
    private void loadFragment(Fragment fragment){
        lucindaViewModel.updateFragment(fragment);
    }
    private void nextWeek(){
        if( VERBOSE)log("...nextWeek()");
        currentDate = currentDate.plusWeeks(1);
        currentWeek = new Week(currentDate);
        calendarDateViewModel.set(currentDate, getContext());
        setUserInterface(currentDate);
    }
    private void observe(){
        log("...observe()");
        calendarDateViewModel.getItems().observe(getViewLifecycleOwner(), new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> items) {
                log("...onChanged(List<Item>");
                adapter.setList(items);
                //adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        log("CalenderDateFragment.onOptionsItemSelected(MenuItem) ", Objects.requireNonNull(item.getTitle()).toString());
        return super.onOptionsItemSelected(item);
    }
    private void postpone(Item item, PostponeDialog.Postpone postpone){
        log("...postpone(Item, Postpone)");
        if(item.isPrioritized()){
            Toast.makeText(getContext(), "no no no, don't postpone prioritized items", Toast.LENGTH_LONG).show();
            adapter.notifyDataSetChanged();
            return;
        }
        log("...currentDate", currentDate.toString());
        calendarDateViewModel.postpone(item, postpone, currentDate, getContext());
    }

    private void prevWeek(){
        if( VERBOSE) log("...prevWeek()");
        currentDate = currentDate.minusWeeks(1);
        currentWeek = new Week(currentDate);
        calendarDateViewModel.set(currentDate, getContext());
        setUserInterface(currentDate);
    }
    private void setUserInterface(LocalDate date){
        if( VERBOSE) log("...setUserInterface()", date.toString());
        currentWeek = new Week(date);
        labelMonthYear.setText(getMonthYear(currentDate));
        calenderDateAdapter.setList(currentWeek);

        lucindaViewModel.updateEnergy(true);
    }
    private void showAddItemDialog(){
        log("...showAddItemDialog()");
        AddItemDialog dialog = new AddItemDialog(ItemsWorker.getRootItem(Settings.Root.DAILY, getContext()), currentDate);
        dialog.setCallback(item -> {
            log("...onNewItem(Item)");
            //item = ItemsWorker.insert(item, getContext());
            calendarDateViewModel.add(item, getContext());
/*            items.add(item);
            log(item);
            if(item.hasNotification()){
                log("...item has notification");
                NotificationsWorker.setNotification(item, getContext());
            }
            updateAdapter();*/
        });
        dialog.show(getParentFragmentManager(), "add item");
    }
    private void showDatePicker(){
        log("...showDatePicker()");
        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext());
        datePickerDialog.setOnDateSetListener((view, year, month, dayOfMonth) -> {
            log("...onDateSet(...)");
            currentDate = LocalDate.of(year, month +1, dayOfMonth);
            setUserInterface(currentDate);
        });
        datePickerDialog.show();
    }

    private void updateTargetTime(Item item){
        log("...updateTargetTime(Item)");
        LocalTime oldTime = item.getTargetTime();
        int minutes = oldTime.getMinute();
        int hour = oldTime.getHour();
        TimePickerDialog timePicker = new TimePickerDialog(getContext(), (view, hourOfDay, minute) -> {
            LocalTime targetTime = LocalTime.of(hourOfDay, minute);
            item.setTargetTime(targetTime);
            calendarDateViewModel.update(item, getContext());
/*            int rowsAffected = ItemsWorker.update(item, getContext());
            if(rowsAffected != 1){
                log("ERROR updating time of item",item.getHeading());
            }
            updateAdapter();*/
        }, hour, minutes, true);
        timePicker.show();
    }
    private void updateAdapter(){
        log("...updateAdapter()");
        items.sort(Comparator.comparingLong(Item::compareTargetTime));
        adapter.setList(items);
        lucindaViewModel.updateEnergy(true);
    }
}