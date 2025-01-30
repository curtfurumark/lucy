package se.curtrune.lucy.screens.daycalendar;

import static se.curtrune.lucy.util.Logger.log;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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
import java.util.List;
import java.util.Locale;

import se.curtrune.lucy.R;
import se.curtrune.lucy.fragments.EditableListFragment;
import se.curtrune.lucy.screens.item_editor.ItemEditorFragment;
import se.curtrune.lucy.app.Settings;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.ItemStatistics;
import se.curtrune.lucy.classes.State;
import se.curtrune.lucy.classes.calender.CalenderDate;
import se.curtrune.lucy.classes.calender.OnSwipeClickListener;
import se.curtrune.lucy.classes.calender.Week;
import se.curtrune.lucy.dialogs.AddItemDialog;
import se.curtrune.lucy.dialogs.ChooseChildTypeDialog;
import se.curtrune.lucy.dialogs.ItemStatisticsDialog;
import se.curtrune.lucy.dialogs.PostponeDialog;
import se.curtrune.lucy.dialogs.ChooseActionDialog;
import se.curtrune.lucy.services.TimerService;
import se.curtrune.lucy.screens.main.LucindaViewModel;
import se.curtrune.lucy.persist.CalenderWorker;
import se.curtrune.lucy.persist.ItemsWorker;


public class CalenderDateFragmentOld extends Fragment {

    private Week currentWeek;
    private RecyclerView recycler;
    private RecyclerView recyclerDates;
    private CalenderAdapter adapter;
    private CalenderDateAdapter calenderDateAdapter;
    private FloatingActionButton buttonAddItem;

    private TextView labelMonthYear;

    private LocalDate currentDate;
    private CalenderDate calenderDate;
    //private List<Item> items;
    public static boolean VERBOSE = false;
    private LucindaViewModel lucindaViewModel;
    private CalendarDateViewModel calendarDateViewModel;
    public CalenderDateFragmentOld() {
        currentDate = LocalDate.now();
    }

    private enum Mode{
        DEFAULT, CALENDAR_DATE
    }
    private Mode mode = Mode.DEFAULT;
    public CalenderDateFragmentOld(CalenderDate calenderDate){
        log("CalendarDateFragment(CalendarDate)", calenderDate.getDate());
        this.calenderDate = calenderDate;
        this.currentWeek = new Week(calenderDate.getDate());
        this.currentDate = calenderDate.getDate();
        mode = Mode.CALENDAR_DATE;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        log("CalenderDateFragmentOld.onCreateView(...)");
        View view = inflater.inflate(R.layout.calender_date_fragment, container, false);
        try {
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
            scrollToCurrentTime();
        }catch (Exception e){
            log("EXCEPTION", e.getMessage());
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return view;
    }
    private void scrollToCurrentTime(){
        log("...scrollToCurrentTime()");
        int position = calendarDateViewModel.getNextTimePosition(LocalTime.now());
        recycler.scrollToPosition(position);
    }
    private void scrollToItem(Item item){
        log("...scrollToItem(Item)");
        int position = calendarDateViewModel.getIndex(item);
        if( position == -1){
            log("STRANGENESS, item not found");
            return;
        }
        recycler.scrollToPosition(position);
    }

    private String getMonthYear(LocalDate date){
        if( VERBOSE) log("...getMonthYear(LocalDate)");
        int weekNumber = CalenderWorker.getWeekNumber(date);
        String  str = date.format(DateTimeFormatter.ofPattern("MMM u"));
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
                if( item.hasChild()){
                    calendarDateViewModel.setParent(item, getContext());
                }else{
                    lucindaViewModel.updateFragment(new ItemEditorFragment(item));
                }
            }

            @Override
            public void onLongClick(Item item) {
                log("...onLongClick(Item)", item.getHeading());
                ChooseActionDialog dialog = new ChooseActionDialog(item, action -> {
                    log("...onClick(Action), add child", action.toString());
                    switch (action){
                        case EDIT:
                            loadFragment(new ItemEditorFragment(item));
                            break;
                        case START_TIMER:
                            Toast.makeText(getContext(), "not implemented", Toast.LENGTH_LONG).show();
                            TimerService.Companion.getCurrentDuration();
                            break;
                        case SHOW_CHILDREN:
                            calendarDateViewModel.setParent(item, getContext());
                            break;
                        case ADD_LIST:
                            lucindaViewModel.updateFragment(new EditableListFragment(item));
                            break;
                        case SHOW_STATS:
                            showItemStatisticsDialog(item);
                            break;
                        case GOTO_PARENT:
                            //calendarDateViewModel.moveOnUp(0, getContext());
                            Item parent = calendarDateViewModel.getParent(item, getContext());
                            log(parent);
                            Toast.makeText(getContext(), parent.toString(), Toast.LENGTH_LONG).show();
                            break;

                    }
                });
                dialog.show(getChildFragmentManager(), "update children");
            }

            @Override
            public void onCheckboxClicked(Item item, boolean checked) {
                if( VERBOSE) log("...onCheckboxClicked(Item, boolean)", checked);
                item.setState(checked ? State.DONE: State.TODO);
                boolean stat = calendarDateViewModel.update(item, getContext());
                if( !stat){
                    log("ERROR updating item", item.getHeading());
                    Toast.makeText(getContext(), "ERROR updating item", Toast.LENGTH_SHORT).show();
                    return;
                }
/*                if(checked){
                    lucindaViewModel.resetMental(getContext(), MentalAdapter.MentalType.ENERGY);
                }*/
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
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Item item = calendarDateViewModel.getItem(viewHolder.getAdapterPosition());
                if (direction == ItemTouchHelper.LEFT) {
                    showDeleteDialog(item);
                } else if (direction == ItemTouchHelper.RIGHT) {
                    showPostponeDialog(item);
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
        lucindaViewModel.recyclerMode.observe(getViewLifecycleOwner(), recyclerMode ->{
            log("CalenderDateFragmentOld....recyclerMode", recyclerMode.toString());
            if( recyclerMode.equals(LucindaViewModel.RecyclerMode.MENTAL_COLOURS)){
                calendarDateViewModel.setEnergyItems();
            }else{
                log(" setting list to DEFAULT");
                //TODO
                //setUserInterface(currentDate);
                // calendarDateViewModel.set(currentDate, getContext());
            }
        } );
        //lucindaViewModel.updateEnergy(getContext());
        calendarDateViewModel = new ViewModelProvider(requireActivity()).get(CalendarDateViewModel.class);
        if( calenderDate != null){
            log("...calendarDate != null");
            calendarDateViewModel.set(calenderDate);
        }else {
            calendarDateViewModel.set(currentDate, getContext());
        }
        lucindaViewModel.getFilter().observe(requireActivity(), filter -> {
            log("onChanged, getFilter.(String)", filter);
            calendarDateViewModel.filter(filter);
        });
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
        calendarDateViewModel.getItems().observe(getViewLifecycleOwner(), items -> {
            log("...onItems(List<Item>)");
            adapter.setList(items);
        });
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
        //lucindaViewModel.updateEnergy(true);
    }
    private void showAddChildToItemDialog(Item item){
        ChooseChildTypeDialog dialog = new ChooseChildTypeDialog(item, new ChooseChildTypeDialog.Listener() {
            @Override
            public void onClick(ChooseChildTypeDialog.ChildType childType) {

            }
        });
        dialog.show(getChildFragmentManager(), "add child");
    }
    private void showAddItemDialog(){
        log("...showAddItemDialog()");
        Item parent = calendarDateViewModel.getCurrentParent();
        if(parent == null){
            parent = ItemsWorker.getRootItem(Settings.Root.DAILY, getContext());
        }
        AddItemDialog dialog = new AddItemDialog(parent, currentDate);
        dialog.setCallback(item -> {
            log("...onNewItem(Item)");
            calendarDateViewModel.add(item, getContext());
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
    private void showDeleteDialog(Item item){
        log("...showDeleteDialog(Item)", item.getHeading());
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.delete) + ": "  + item.getHeading());
        builder.setMessage(getString(R.string.are_you_sure));
        builder.setPositiveButton(getString(R.string.delete), (dialog, which) -> {
            log("...on positive button click");
            calendarDateViewModel.delete(item, getContext());
            if(item.isDone()){
                log("...item is done, update mental");
                //lucindaViewModel.resetMental(getContext(), MentalAdapter.MentalType.ENERGY);
            }
        });
        builder.setNegativeButton(getString(R.string.dismiss), (dialog, which) -> {
            log("...on negative button click");
            adapter.notifyDataSetChanged();
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void showItemStatisticsDialog(Item item){
        log("...showItemStatisticsDialog()");
        if( !item.isTemplate()){
            Toast.makeText(getContext(), "not a template", Toast.LENGTH_SHORT).show();
            return;
        }
        List<Item> items = ItemsWorker.selectTemplateChildren(item, getContext());
        ItemStatistics statistics = new ItemStatistics(items);
        ItemStatisticsDialog dialog = new ItemStatisticsDialog(statistics);
        dialog.show(getChildFragmentManager(), "show statistics");
        log(statistics);

    }
    private void showPostponeDialog(Item item){
        log("showPostponeDialog(Item)");
        PostponeDialog dialog = new PostponeDialog();
        dialog.setCallback(new PostponeDialog.Callback() {
            @Override
            public void postpone(PostponeDialog.Postpone postpone) {
                log("PostponeDialog.postpone(Postpone)", postpone.toString());
                CalenderDateFragmentOld.this.postpone(item, postpone);
            }
            @Override
            public void dismiss() {
                log("...dismiss()");
                adapter.notifyDataSetChanged();
            }
        });
        dialog.show(getChildFragmentManager(), "postpone");
    }

    private void updateTargetTime(Item item){
        log("...updateTargetTime(Item)");
        LocalTime oldTime = item.getTargetTime();
        int minutes = oldTime.getMinute();
        int hour = oldTime.getHour();
        TimePickerDialog timePicker = new TimePickerDialog(getContext(), (view, hourOfDay, minute) -> {
            LocalTime targetTime = LocalTime.of(hourOfDay, minute);
            item.setTargetTime(targetTime);
            boolean stat = calendarDateViewModel.update(item, getContext());
            if( !stat){
                Toast.makeText(getContext(), "ERROR updating time", Toast.LENGTH_LONG).show();
            }else{
                scrollToItem(item);
            }
        }, hour, minutes, true);
        timePicker.show();
    }
}