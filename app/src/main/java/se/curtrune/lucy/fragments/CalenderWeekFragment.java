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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import se.curtrune.lucy.R;
import se.curtrune.lucy.adapters.CalenderWeekAdapter;
import se.curtrune.lucy.classes.Type;
import se.curtrune.lucy.classes.calender.CalenderDate;
import se.curtrune.lucy.classes.calender.OnSwipeClickListener;
import se.curtrune.lucy.classes.calender.Week;
import se.curtrune.lucy.viewmodel.LucindaViewModel;
import se.curtrune.lucy.workers.CalenderWorker;

public class CalenderWeekFragment extends Fragment {
    //private Button buttonNext;
    //private Button buttonPrev;
    //private TextView textViewNext;
    //private TextView textViewPrev;
    public static boolean VERBOSE = true;
    //private CalenderDateAdapter calenderDateAdapter;
    private CalenderWeekAdapter adapter;

    private RecyclerView recycler;
    private TextView textViewWeekNumber;
    private Week currentWeek;
    private LocalDate firstDate;
    private LocalDate lastDate;
    private LocalDate currentDate;
    private List<CalenderDate> calenderDates;
    private LucindaViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        log("CalenderWeekFragment.onCreateView(LayoutInflater, ViewGroup, Bundle)");
        View view = inflater.inflate(R.layout.calender_week_fragment, container, false);
        initDefaults();
        initComponents(view);
        initData();
        initListeners();
        initSwipeHeading();
        initRecycler();
        //setUserInterface();
        initViewModel();
        setCalender(LocalDate.now());
        //setUserInterface(LocalDate.now());
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
        calenderDates = CalenderWorker.getAppointments(firstDate, lastDate, getContext());
        calenderDates.forEach(System.out::println);
    }
    private void initDefaults(){
        log("...initDefaults()");
        currentDate = firstDate = CalenderWorker.getFirstDateOfWeek(LocalDate.now());
        lastDate = firstDate.plusDays(6);
    }
    private void initListeners(){
        log("...initListeners()");
        textViewWeekNumber.setOnClickListener(view->showWeekDialog());
    }

    private void initRecycler() {
        log("...initRecycler()");
        adapter = new CalenderWeekAdapter(calenderDates, new CalenderWeekAdapter.Listener() {
            @Override
            public void onCalenderDateClick(CalenderDate calenderDate) {
                log("CalenderWeekAdapter.onCalenderDateClick(CalenderDate)", calenderDate.getDate().toString());
                viewModel.updateFragment(new CalenderDateFragment(calenderDate.getDate()));
            }
        });
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        //recycler.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recycler.setLayoutManager(staggeredGridLayoutManager);
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setAdapter(adapter);
        //recyclerCells.addItemDecoration( new DividerItemDecoration(getContext(), GridLayoutManager.VERTICAL));
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
    }
    private void nextWeek(){
        log("...nextWeek()");
        currentDate = currentDate.plusDays(6);
        setCalender(currentDate);
    }
    private void previousWeek(){
        log("...previousWeek");
        currentDate = currentDate.minusDays(6);
        setCalender(currentDate);
    }
    private void setCalender(LocalDate date){
        log("...setCalender(LocalDate)", date.toString());
        currentWeek = new Week(date);
        firstDate = currentWeek.getFirstDateOfWeek();
        lastDate = currentWeek.getLastDateOfWeek();
        adapter.setCalenderDates(CalenderWorker.getCalenderDates(Type.APPOINTMENT, firstDate, lastDate, getContext()));
        //textViewWeekNumber.setText(String.format(Locale.getDefault(), "< %s %d >", getString(R.string.week), currentWeek.getWeekNumber()));
        textViewWeekNumber.setText(getYearMonthWeek(date));
    }
    private void showWeekDialog(){
        log("...showWeekDialog()");
        Toast.makeText(getContext(), "week dialog", Toast.LENGTH_SHORT).show();
    }
}
