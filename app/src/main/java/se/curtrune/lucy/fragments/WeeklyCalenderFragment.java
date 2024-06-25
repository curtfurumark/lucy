package se.curtrune.lucy.fragments;

import static se.curtrune.lucy.util.Logger.log;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import se.curtrune.lucy.R;
import se.curtrune.lucy.adapters.CalenderDateAdapter;
import se.curtrune.lucy.adapters.DateHourAdapter;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.calender.DateHourCell;
import se.curtrune.lucy.classes.calender.Week;
import se.curtrune.lucy.workers.ItemsWorker;

public class WeeklyCalenderFragment extends Fragment {
    private Button buttonNext;
    private Button buttonPrev;
    public static boolean VERBOSE = true;
    private CalenderDateAdapter calenderDateAdapter;
    private DateHourAdapter dateHourAdapter;
    private RecyclerView recyclerDates;
    private RecyclerView recyclerCells;
    private Week currentWeek;
    private LocalDate currentDate;
    private TextView textViewWeekNumber;
    private List<Item> items;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        log("WeeklyFragment.onCreateView(...)");
        View view = inflater.inflate(R.layout.weekly_fragment, container, false);
        initDefaults();
        initComponents(view);
        initRecyclerDates();
        items  = ItemsWorker.selectItems(currentWeek, getContext());
        items.forEach(System.out::println);
        initRecyclerCells();
        initListeners();
        setUserInterface(LocalDate.now());
        return view;
    }
    private List<DateHourCell> getDateHourCells(){
        log("...getDateHourCells()");
        Week week = new Week(LocalDate.now());
        List<DateHourCell> dateHourCells = new ArrayList<>();
        int hour = 0;
        LocalDate date = LocalDate.now().plusDays(7);
        for( int i = 0; i < 7 * 24; i++){
            if(  i % 7 == 0){
                log("...new Row", i);
                date = date.minusDays(7);
                hour++;
            }

            DateHourCell dateHourCell = new DateHourCell();
            dateHourCell.setHour(hour - 1);
            dateHourCell.setDate(date);
            dateHourCells.add(dateHourCell);
            date = date.plusDays(1);
        }
        return dateHourCells;
    }

    private void initRecyclerDates() {
        if (VERBOSE) log("...initRecyclerDates()");
        calenderDateAdapter = new CalenderDateAdapter(currentWeek, date -> {
            log("...onDateSelected(LocalDate)", date.toString());
            //items = ItemsWorker.selectItems(date, getContext(), State.TODO);
            //adapter.setList(items);
            currentDate = date;
            currentWeek.setCurrentDate(currentDate);
            calenderDateAdapter.setList(currentWeek);
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
        recyclerCells = view.findViewById(R.id.weeklyFragment_layoutRecyclerCells);
        textViewWeekNumber = view.findViewById(R.id.weeklyFragment_weekNumber);
    }
    private void initDefaults(){
        log("...initDefaults()");
        currentDate = LocalDate.now();
        currentWeek = new Week(currentDate);
    }
    private void initListeners(){
        log("...initListeners()");
        buttonPrev.setOnClickListener(view->previousWeek());
        buttonNext.setOnClickListener(view->nextWeek());
    }

    private void initRecyclerCells() {
        log("...initRecyclerCells()");
        List<DateHourCell> dateHourCells = getDateHourCells();
        dateHourAdapter = new DateHourAdapter(dateHourCells, dateHourCell -> {
            log("...onDateHourCellSelected(DateHourCell)");
            log(dateHourCell);
            Toast.makeText(getContext(), dateHourCell.toString(), Toast.LENGTH_LONG).show();
        });
        recyclerCells.setLayoutManager(new GridLayoutManager(getContext(), 7));
        recyclerCells.setItemAnimator(new DefaultItemAnimator());
        recyclerCells.setAdapter(dateHourAdapter);

    }
    private void nextWeek(){
        log("...nextWeek()");
        currentDate = currentDate.plusDays(6);
        setUserInterface(currentDate);
    }
    private void previousWeek(){
        log("...previousWeek");
        currentDate = currentDate.minusDays(6);
        setUserInterface(currentDate);
    }
    private void setUserInterface(LocalDate date){
        log("...setUserInterface(LocalDate)", date.toString());
        int weekNumber = date.get(WeekFields.of(Locale.getDefault()).weekOfYear());
        String textWeekNumber = String.format(Locale.getDefault(), "week %d", weekNumber);
        textViewWeekNumber.setText(textWeekNumber);

    }
}
