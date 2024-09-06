package se.curtrune.lucy.fragments;

import static se.curtrune.lucy.util.Logger.log;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import se.curtrune.lucy.R;
import se.curtrune.lucy.adapters.CalenderWeekAdapter;
import se.curtrune.lucy.classes.calender.CalenderDate;
import se.curtrune.lucy.classes.calender.Week;
import se.curtrune.lucy.viewmodel.LucindaViewModel;
import se.curtrune.lucy.workers.CalenderWorker;

public class CalenderWeekFragment extends Fragment {
    private Button buttonNext;
    private Button buttonPrev;
    public static boolean VERBOSE = true;
    //private CalenderDateAdapter calenderDateAdapter;
    private CalenderWeekAdapter adapter;
    //private RecyclerView recyclerDates;
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
        initRecycler();
        setUserInterface();
        initViewModel();
        //setUserInterface(LocalDate.now());
        return view;
    }

    private void initComponents(View view) {
        log("...initComponents(View)");
        //buttonPrev = view.findViewById(R.id.weeklyFragment_buttonPrev);
        //buttonNext = view.findViewById(R.id.weeklyFragment_buttonNext);
        recycler = view.findViewById(R.id.calenderWeekFragment_recycler);
        textViewWeekNumber = view.findViewById(R.id.calenderWeekFragment_weekNumber);
    }
    private void initDefaults(){
        log("...initDefaults()");
        firstDate = CalenderWorker.getFirstDateOfWeek(LocalDate.now());
        lastDate = firstDate.plusDays(6);
    }
    private void initListeners(){
        log("...initListeners()");
        buttonPrev.setOnClickListener(view->previousWeek());
        buttonNext.setOnClickListener(view->nextWeek());
    }

    private void initRecycler() {
        log("...initRecycler()");
        calenderDates = CalenderWorker.getCalenderDates(firstDate, lastDate, getContext());
        adapter = new CalenderWeekAdapter(calenderDates, new CalenderWeekAdapter.Listener() {
            @Override
            public void onDateClick(LocalDate date) {
                log("CalenderWeekAdapter.onDateClick(LocalDate)", date.toString());
                viewModel.updateFragment(new CalenderDateFragment(date));
            }
        });
        //recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        //recycler.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recycler.setLayoutManager(staggeredGridLayoutManager);
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setAdapter(adapter);
        //recyclerCells.addItemDecoration( new DividerItemDecoration(getContext(), GridLayoutManager.VERTICAL));
    }
    private void initViewModel(){
        viewModel = new ViewModelProvider(requireActivity()).get(LucindaViewModel.class);
    }
    private void nextWeek(){
        log("...nextWeek()");
        currentDate = currentDate.plusDays(6);
        currentWeek = new Week(currentDate);
        setUserInterface(currentDate);
    }
    private void previousWeek(){
        log("...previousWeek");
        currentDate = currentDate.minusDays(6);
        currentWeek = new Week(currentDate);
        setUserInterface(currentDate);
    }
    private void setUserInterface(){
        log("...setUserInterface()");
        int weekNumber = CalenderWorker.getWeekNumber(LocalDate.now());
        textViewWeekNumber.setText(String.format(Locale.getDefault(), "week %d", weekNumber));

    }
    private void setUserInterface(LocalDate date){
        log("...setUserInterface(LocalDate)", date.toString());
/*        int weekNumber = date.get(WeekFields.of(Locale.getDefault()).weekOfYear());
        String textWeekNumber = String.format(Locale.getDefault(), "week %d", weekNumber);
        textViewWeekNumber.setText(textWeekNumber);
        //dateHourAdapter.setList(getDateHourCells(currentWeek));
        //sometimes i wonder why i have to do this, wny is it not sufficient to set list and notify adapter
        initRecyclerCells();*/
    }
}
