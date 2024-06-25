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
import java.util.Locale;
import java.util.Objects;

import se.curtrune.lucy.R;
import se.curtrune.lucy.activities.ItemSession;
import se.curtrune.lucy.adapters.CalenderAdapter;
import se.curtrune.lucy.adapters.CalenderDateAdapter;
import se.curtrune.lucy.app.Settings;
import se.curtrune.lucy.classes.CallingActivity;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.State;
import se.curtrune.lucy.classes.calender.Week;
import se.curtrune.lucy.dialogs.AddItemDialog;
import se.curtrune.lucy.dialogs.EditItemDialog;
import se.curtrune.lucy.dialogs.PostponeDialog;
import se.curtrune.lucy.util.Constants;
import se.curtrune.lucy.viewmodel.LucindaViewModel;
import se.curtrune.lucy.workers.ItemsWorker;
import se.curtrune.lucy.workers.NotificationsWorker;
import se.curtrune.lucy.workers.StatisticsWorker;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalenderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalenderFragment extends Fragment {


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
    public static boolean VERBOSE = false;
    private LucindaViewModel viewModel;
    public CalenderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment CalenderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CalenderFragment newInstance() {
        return  new CalenderFragment();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            log("...getArguments != null!");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        log("CalenderFragment.onCreateView(...)");
        View view = inflater.inflate(R.layout.calender_fragment, container, false);
        requireActivity().setTitle("");
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
        return String.format(Locale.getDefault(), "%s, %d", date.getMonth().toString(), date.getYear());
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
                log("...onLongClick(Item)", item.getHeading());
                EditItemDialog dialog = new EditItemDialog(item);
                dialog.setCallback(new EditItemDialog.Callback() {
                    @Override
                    public void onUpdate(Item item) {
                        log("...onUpdate(Item)");
                        log(item);
                        int rowsAffected = ItemsWorker.update(item, getContext());
                        if( rowsAffected != 1){
                            log("ERROR updating item", item.getHeading());
                        }else{
                            log("...item updated");
                            items.sort(Comparator.comparingLong(Item::compareTargetTime));
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
                dialog.show(getChildFragmentManager(), "edit item");
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
        calenderDateAdapter = new CalenderDateAdapter(currentWeek, date -> {
            log("...onDateSelected(LocalDate)", date.toString());
            currentDate = date;
            currentWeek.setCurrentDate(currentDate);
            calenderDateAdapter.setList(currentWeek);
            setUserInterface(date);
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
                if( direction == ItemTouchHelper.LEFT){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("delete" + item.getHeading());
                    builder.setMessage("are you sure? ");
                    builder.setPositiveButton("delete", (dialog, which) -> {
                        log("...on positive button click");
                        boolean deleted = ItemsWorker.delete(item, getContext());
                        if( !deleted){
                            log("...error deleting item");
                            Toast.makeText(getContext(), "error deleting item", Toast.LENGTH_LONG).show();
                        }else {
                            items.remove(item);
                            adapter.notifyDataSetChanged();
                        }
                    });
                    builder.setNegativeButton("cancel", (dialog, which) -> {
                        log("...on negative button click");
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();

                }else if( direction == ItemTouchHelper.RIGHT) {
                    PostponeDialog dialog = new PostponeDialog();
                    dialog.setCallback(postpone -> {
                        log("...postpone(Postpone)", postpone.toString());
                        postpone(item, postpone);
                        adapter.notifyDataSetChanged();
                    });
                    dialog.show(getChildFragmentManager(), "postpone");
                }
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
        log("CalenderFragment.onOptionsItemSelected(MenuItem) ", Objects.requireNonNull(item.getTitle()).toString());
        return super.onOptionsItemSelected(item);
    }
    private void postpone(Item item, PostponeDialog.Postpone postpone){
        log("...postpone(Item, Postpone)");
        if(item.isPrioritized()){
            Toast.makeText(getContext(), "no no no, dont postpone prioritized items", Toast.LENGTH_LONG).show();
            return;
        }
        log("...currentDate", currentDate.toString());
        switch (postpone){
            case ONE_HOUR:
                LocalTime targetTime = item.getTargetTime();
                item.setTargetTime(targetTime.plusHours(1));
                break;
            case ONE_DAY:
                item.setTargetDate(currentDate.plusDays(1));
                items.remove(item);
                break;
            case ONE_WEEK:
                item.setTargetDate(currentDate.plusWeeks(1));
                items.remove(item);
                break;
            case ONE_MONTH:
                item.setTargetDate(currentDate.plusMonths(1));
                items.remove(item);
                break;
        }
        int rowsAffected = ItemsWorker.update(item, getContext());
        log(item);
        if( rowsAffected != 1){
            Toast.makeText(getContext(), "error updating item", Toast.LENGTH_LONG).show();
        }
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
        AddItemDialog dialog = new AddItemDialog(ItemsWorker.getRootItem(Settings.Root.DAILY, getContext()), true);
        dialog.setTargetDate(currentDate);
        dialog.setCallback(item -> {
            log("...onNewItem(Item)");
            item = ItemsWorker.insert(item, getContext());
            items.add(item);
            log(item);
            if(item.hasNotification()){
                log("...item has notification");
                NotificationsWorker.setNotification(item, getContext());
            }
            updateAdapter();
        });
        dialog.show(getParentFragmentManager(), "add item");
    }

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