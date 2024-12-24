package se.curtrune.lucy.fragments;

import static se.curtrune.lucy.util.Logger.log;

import android.os.Bundle;
import android.view.LayoutInflater;
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
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

import se.curtrune.lucy.R;
import se.curtrune.lucy.adapters.CalenderMonthAdapter;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.calender.CalenderDate;
import se.curtrune.lucy.classes.calender.OnSwipeClickListener;
import se.curtrune.lucy.dialogs.EventDialog;
import se.curtrune.lucy.dialogs.ItemsDialog;
import se.curtrune.lucy.viewmodel.CalendarMonthViewModel;
import se.curtrune.lucy.viewmodel.LucindaViewModel;
import se.curtrune.lucy.workers.CalenderWorker;
import se.curtrune.lucy.workers.ItemsWorker;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalenderMonthFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalenderMonthFragment extends Fragment {
    private TextView monthYearText;

    private RecyclerView recyclerCalender;
    public static boolean VERBOSE = false;

    private LocalDate selectedDate;
    private YearMonth currentYearMonth;
    private CalenderMonthAdapter calenderMonthAdapter;
    private List<CalenderDate> calenderDates;
    private CalendarMonthViewModel calendarMonthViewModel;
    private LucindaViewModel lucindaViewModel;

    public CalenderMonthFragment() {
        log("CalenderMonthFragment()");
    }
    public CalenderMonthFragment(YearMonth yearMonth){
        log("CalenderMonthFragment(YearMonth)", yearMonth.toString());
        this.currentYearMonth = yearMonth;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.

     * @return A new instance of fragment CalenderMonthFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CalenderMonthFragment newInstance() {
        return new CalenderMonthFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            log("here comes the arguments");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.calender_month_fragment, container, false);
        initComponents(view);
        initViewModel();
        initRecycler();
        //initListeners();
        //initSwipeHeading();
        //initItemTouchHelper();
        setMonthYearLabel();
        //observe();
        return view;
    }
    private void addEvent(Item item){
        log("...addEvent(Item)");
        item = ItemsWorker.insert(item, getContext());
        for( CalenderDate calenderDate: calenderDates){
            if(calenderDate.getDate().equals(item.getTargetDate())){
                log("...found calendarDate");
                calenderDate.add(item);
                break;
            }
        }
        calenderMonthAdapter.notifyDataSetChanged();
    }
    private List<CalenderDate> getCalenderDates(LocalDate date){
        return CalenderWorker.getCalenderDates(YearMonth.from(date), getContext());
    }
    private void initComponents(View view){
        log("...initComponents(View)");
        recyclerCalender = view.findViewById(R.id.monthCalenderFragment_recyclerDates);
        monthYearText = view.findViewById(R.id.calenderMonthFragment_heading);
    }
    private void initItemTouchHelper(){
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if (direction == ItemTouchHelper.RIGHT) {
                    nextMonthAction();
                } else if (direction == ItemTouchHelper.LEFT) {
                    previousMonthAction();
                }
            }
        }).attachToRecyclerView(recyclerCalender);

    }
/*    private void initListeners(){
        log("...initListeners()");
    }*/
    private void initRecycler(){
        log("...initRecycler()");
        calenderDates = CalenderWorker.getCalenderDates(currentYearMonth, getContext());
        //calenderMonthAdapter = new CalenderMonthAdapter(calenderDates)
        //calenderMonthAdapter = new CalenderMonthAdapter(calendarMonthViewModel.getCalendarDates().getValue(), calenderDate -> {
        calenderMonthAdapter = new CalenderMonthAdapter(calenderDates, calenderDate -> {
            log("CalenderMonthAdapter.onCalenderDateClick(CalenderDate)", calenderDate.getDate().toString());
            if(calenderDate.getItems().size() == 0){
                showAppointmentsDialog(calenderDate.getDate());
            }else {
                log(" will move to calender date fragment");
                lucindaViewModel.updateFragment(new CalenderDateFragment(calenderDate));
            }
        });

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 7);
        recyclerCalender.setLayoutManager(layoutManager);
        recyclerCalender.setItemAnimator(new DefaultItemAnimator());
        recyclerCalender.setAdapter(calenderMonthAdapter);
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerCalender);
    }
    private void initViewModel(){
        log("...initViewModel()");
        //calendarMonthViewModel = new ViewModelProvider(requireActivity()).get(CalendarMonthViewModel.class);
        if( currentYearMonth == null){
            currentYearMonth = YearMonth.now();
        }
        //calendarMonthViewModel.setYearMonth(currentYearMonth, getContext());
        lucindaViewModel = new ViewModelProvider(requireActivity()).get(LucindaViewModel.class);
    }
    private void initSwipeHeading(){
        log("...initSwipeHeading()");
        OnSwipeClickListener onSwipeTouchListener = new OnSwipeClickListener(getContext(), new OnSwipeClickListener.Listener() {
            @Override
            public void onSwipeRight() {
                log("...onSwipeRight()");
                nextMonthAction();
            }

            @Override
            public void onSwipeLeft() {
                log("...onSwipeLeft()");
                previousMonthAction();
            }

            @Override
            public void onClick() {
                log("...onClick()");
                showMonthPicker();
            }
        });
        monthYearText.setOnTouchListener(onSwipeTouchListener);
/*        monthYearText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });*/
    }
    private String monthYearFromDate(LocalDate date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return date.format(formatter);

    }
    public void nextMonthAction(){
        log("...nextMonthAction()");
        selectedDate = selectedDate.plusMonths(1);
        calenderMonthAdapter.setList(getCalenderDates(selectedDate));
        setMonthYearLabel();
    }
    private void observe(){
        log("...observe()");
        calendarMonthViewModel.getCalendarDates().observe(getViewLifecycleOwner(), new Observer<List<CalenderDate>>() {
            @Override
            public void onChanged(List<CalenderDate> calenderDates) {
                log("...onChanged(List<CalenderDate>)");
                log("doing nothing");
            }
        });
    }
    public void previousMonthAction(){
        log("...previousMonthAction()");
        selectedDate = selectedDate.minusMonths(1);
        calenderMonthAdapter.setList(getCalenderDates(selectedDate));
        setMonthYearLabel();
    }
    private void setMonthYearLabel(){
        log("...setMonthYearLabel()");
        //monthYearText.setText(monthYearFromDate(selectedDate));
        monthYearText.setText(currentYearMonth.toString());
    }
    private void showAppointmentsDialog(LocalDate date){
        log("...showAppointmentsDialog()");
        EventDialog dialog = new EventDialog(date);
        dialog.setCallback(new EventDialog.OnNewAppointmentCallback() {
            @Override
            public void onNewAppointment(Item item) {
                log("...onNewAppointment(Item item)", item.getHeading());
                //calendarMonthViewModel.add(item);'
                addEvent(item);
                calenderMonthAdapter.notifyDataSetChanged();
            }
        });
        dialog.show(getChildFragmentManager(), "add appointment");
    }
    private void showItemsDialog(CalenderDate calenderDate){
        log("...showItemsDialog()");
        ItemsDialog dialog = new ItemsDialog(calenderDate.getItems(), calenderDate.getDate());
        dialog.show(getChildFragmentManager(), "setMentalType items");
    }
    private void showMonthPicker(){
        log("...showMonthPicker()");
        Toast.makeText(getContext(), "month picker", Toast.LENGTH_SHORT).show();

    }
}