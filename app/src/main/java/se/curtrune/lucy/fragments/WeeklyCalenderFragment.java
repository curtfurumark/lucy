package se.curtrune.lucy.fragments;

import static se.curtrune.lucy.util.Logger.log;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.List;

import se.curtrune.lucy.R;
import se.curtrune.lucy.adapters.CalenderDateAdapter;
import se.curtrune.lucy.classes.Mental;
import se.curtrune.lucy.classes.calender.Week;
import se.curtrune.lucy.statistics.TopTenStatistics;
import se.curtrune.lucy.util.Logger;

public class WeeklyCalenderFragment extends Fragment {
    private Button buttonNext;
    private Button buttonPrev;
    public static boolean VERBOSE = true;
    private CalenderDateAdapter calenderDateAdapter;
    private RecyclerView recyclerDates;
    private Week currentWeek;
    private LocalDate currentDate;
    private TextView textViewLabelWeek;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        log("WeeklyFragment.onCreateView(...)");
        View view = inflater.inflate(R.layout.weekly_fragment, container, false);
        initDefaults();
        initComponents(view);
        initRecyclerDates();
        initRecycler();
        return view;
    }

    private void initRecyclerDates() {
        if (VERBOSE) log("...initRecyclerDates()");
        calenderDateAdapter = new CalenderDateAdapter(currentWeek, new CalenderDateAdapter.Callback() {
            @Override
            public void onDateSelected(LocalDate date) {
                log("...onDateSelected(LocalDate)", date.toString());
                //items = ItemsWorker.selectItems(date, getContext(), State.TODO);
                //adapter.setList(items);
                currentDate = date;
                currentWeek.setCurrentDate(currentDate);
                calenderDateAdapter.setList(currentWeek);
                //setUserInterface(date);
            }
        });
        recyclerDates.setLayoutManager(new GridLayoutManager(getContext(), 7));
        recyclerDates.setItemAnimator(new DefaultItemAnimator());
        recyclerDates.setAdapter(calenderDateAdapter);
    }

    private void initComponents(View view) {
        log("...initComponents(View)");
        buttonPrev = view.findViewById(R.id.weeklyFragment_buttonPrev);
        buttonNext = view.findViewById(R.id.weeklyFragment_buttonNext);
        recyclerDates = view.findViewById(R.id.weeklyFragment_recyclerDays);
        //textViewLabelWeek = view.findViewById(R.id.weeklyFragment_buttonPrev);
    }
    private void initDefaults(){
        log("...initDefaults()");
        currentDate = LocalDate.now();
        currentWeek = new Week(currentDate);
    }

    private void initRecycler() {
        log("...initRecycler()");

    }
}
