package se.curtrune.lucy.fragments;

import static se.curtrune.lucy.util.Logger.log;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import se.curtrune.lucy.R;
import se.curtrune.lucy.adapters.CalenderWeekAdapter;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.Type;
import se.curtrune.lucy.classes.calender.CalenderDate;
import se.curtrune.lucy.classes.calender.OnSwipeClickListener;
import se.curtrune.lucy.classes.calender.Week;
import se.curtrune.lucy.dialogs.AppointmentDialog;
import se.curtrune.lucy.viewmodel.CalendarWeekViewModel;
import se.curtrune.lucy.viewmodel.LucindaViewModel;
import se.curtrune.lucy.workers.CalenderWorker;
import se.curtrune.lucy.workers.ItemsWorker;

public class CalenderWeekFragment extends Fragment {
    public static boolean VERBOSE = true;
    //private CalenderDateAdapter calenderDateAdapter;
    private CalenderWeekAdapter adapter;

    private RecyclerView recycler;
    private TextView textViewWeekNumber;
    private Week currentWeek;
    //private List<CalenderDate> calenderDates = new ArrayList<>();
    private LucindaViewModel viewModel;
    private CalendarWeekViewModel calendarWeekViewModel;

    public CalenderWeekFragment(){
        log("CalenderWeekFragment()");
        currentWeek = new Week(LocalDate.now());

    }
    public CalenderWeekFragment(LocalDate date) {
        log("CalenderWeekFragment(LocalDate)", date.toString());
        currentWeek = new Week(date);
    }
    public CalenderWeekFragment(CalenderDate calendarDate){
        log("CalenderWeekFragment(CalenderDate)");
        currentWeek = new Week(calendarDate.getDate());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        log("CalenderWeekFragment.onCreateView(LayoutInflater, ViewGroup, Bundle)");
        View view = inflater.inflate(R.layout.calender_week_fragment, container, false);
        initComponents(view);
        initViewModel();
        //initData();
        initListeners();
        //initSwipeHeading();
        //initRecycler();

        setCalendar(currentWeek);
        return view;
    }

    private String getYearMonthWeek(LocalDate date) {
        if (VERBOSE) log("...getYearMonthWeek(LocalDate)");
        int weekNumber = CalenderWorker.getWeekNumber(date);
        String str = date.format(DateTimeFormatter.ofPattern("MMM u"));
        return String.format(Locale.getDefault(), "< %s %s %d >", str, getString(R.string.week), weekNumber);
    }

    private void initComponents(View view) {
        log("...initComponents(View)");
        recycler = view.findViewById(R.id.calenderWeekFragment_recycler);
        textViewWeekNumber = view.findViewById(R.id.calenderWeekFragment_weekNumber);
    }
    private void initData(){
        log("...initData()");
        //currentDate = firstDate = CalenderWorker.getFirstDateOfWeek(currentDate);
        //lastDate = firstDate.plusDays(6);
        //calenderDates = CalenderWorker.getAppointments(firstDate, lastDate, getContext());
        //calenderDates = CalenderWorker.getEvents(currentWeek, getContext());
    }

    private void initListeners(){
        log("...initListeners()");
        textViewWeekNumber.setOnClickListener(view->showWeekDialog());
    }

    private void initRecycler() {
        log("...initRecycler()");
        adapter = new CalenderWeekAdapter(calendarWeekViewModel.getCalenderDates().getValue(), new CalenderWeekAdapter.Listener() {
            @Override
            public void onCalenderDateClick(CalenderDate calenderDate) {
                log("CalenderWeekAdapter.onCalenderDateClick(CalenderDate)", calenderDate.getDate().toString());
                if(calenderDate.getItems().size() == 0){
                    showAppointmentDialog(calenderDate);
                }else{
                    viewModel.updateFragment(new CalenderDateFragment(calenderDate.getDate()));
                }
            }
        });
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        recycler.setLayoutManager(staggeredGridLayoutManager);
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setAdapter(adapter);
    }
    @SuppressLint("ClickableViewAccessibility")
    private void initSwipeHeading(){
        log("...initSwipeHeading()");
        OnSwipeClickListener onSwipeTouchListener = new OnSwipeClickListener(getContext(), new OnSwipeClickListener.Listener() {
            @Override
            public void onSwipeRight() {
                nextWeek();
            }

            @Override
            public void onSwipeLeft() {
                previousWeek();
            }

            @Override
            public void onClick() {
                showWeekDialog();
            }
        });
        textViewWeekNumber.setOnTouchListener(onSwipeTouchListener);
    }
    private void initViewModel(){
        viewModel = new ViewModelProvider(requireActivity()).get(LucindaViewModel.class);
        calendarWeekViewModel = new ViewModelProvider(requireActivity()).get(CalendarWeekViewModel.class);

        calendarWeekViewModel.set(currentWeek, getContext());
        initRecycler();
        calendarWeekViewModel.getCalenderDates().observe(requireActivity(), new Observer<List<CalenderDate>>() {
            @Override
            public void onChanged(List<CalenderDate> calenderDates) {
                adapter.setCalenderDates(calenderDates);
            }
        });

    }
    private void nextWeek(){
        log("...nextWeek()");
        currentWeek = currentWeek.getNextWeek();
        calendarWeekViewModel.set(currentWeek, getContext());
        setCalendar(currentWeek);
    }
    private void previousWeek(){
        log("...previousWeek");
        currentWeek = currentWeek.getPreviousWeek();
        //currentDate = currentDate.minusDays(6);
        calendarWeekViewModel.set(currentWeek, getContext());
        setCalendar(currentWeek);
    }
/*    private void setCalender(LocalDate date){
        log("...setCalender(LocalDate)", date.toString());
        currentWeek = new Week(date);
        //firstDate = currentWeek.getFirstDateOfWeek();
        lastDate = currentWeek.getLastDateOfWeek();
        adapter.setCalenderDates(CalenderWorker.getCalenderDates(Type.APPOINTMENT, firstDate, lastDate, getContext()));
        textViewWeekNumber.setText(getYearMonthWeek(date));
    }*/
    private void setCalendar(Week week){
        log("...setCalendar(Week)");
        //calenderDates = CalenderWorker.getEvents(week, getContext());
        //adapter.notifyDataSetChanged();
        textViewWeekNumber.setText(getYearMonthWeek(week.getFirstDateOfWeek()));
    }
    private void showAppointmentDialog(CalenderDate calenderDate){
        log("...showAppointmentDialog()");
        AppointmentDialog dialog = new AppointmentDialog(calenderDate.getDate());
        dialog.setCallback(new AppointmentDialog.OnNewAppointmentCallback() {
            @Override
            public void onNewAppointment(Item item) {
                log("...onNewAppointment(Item item");
                item = ItemsWorker.insert(item, getContext());
                calenderDate.add(item);
                adapter.notifyDataSetChanged();

            }
        });
        dialog.show(getChildFragmentManager(), "add appointment");
    }

    private void showWeekDialog(){
        log("...showWeekDialog()");
        Toast.makeText(getContext(), "week dialog", Toast.LENGTH_SHORT).show();
    }
}
