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
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import se.curtrune.lucy.R;
import se.curtrune.lucy.adapters.DateHourAdapter;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.calender.DateHourCell;
import se.curtrune.lucy.classes.calender.Week;
import se.curtrune.lucy.dialogs.ItemsDialog;
import se.curtrune.lucy.workers.ItemsWorker;

public class OldWeeklyCalenderFragment extends Fragment {
    private Button buttonNext;
    private Button buttonPrev;
    public static boolean VERBOSE = true;
    //private CalenderDateAdapter calenderDateAdapter;
    private DateHourAdapter dateHourAdapter;
    //private RecyclerView recyclerDates;
    private RecyclerView recyclerCells;
    private Week currentWeek;
    private LocalDate currentDate;
    private TextView textViewWeekNumber;
    private List<Item> items;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        log("WeeklyCalendarFragment.onCreateView(...)");
        View view = inflater.inflate(R.layout.old_weekly_fragment, container, false);
        initDefaults();
        initComponents(view);
        items  = ItemsWorker.selectItems(currentWeek, getContext());
        items.forEach(System.out::println);
        initRecyclerCells();
        initListeners();
        setUserInterface(LocalDate.now());
        return view;
    }
    private List<DateHourCell> getDateHourCells(Week week){
        log("...getDateHourCells(Week)");
        List<DateHourCell> dateHourCells = new ArrayList<>();
        int hour = 0;
        LocalDate date = week.getFirstDateOfWeek();
        //SET DATE HEADER
        for( int i = 0; i < 8; i++){
            DateHourCell dateHourCell = new DateHourCell();
            if( i == 0){
                dateHourCell.setType(DateHourCell.Type.EMPTY_CELL);
                dateHourCells.add(dateHourCell);
            }else{
                dateHourCell.setDate(date);
                dateHourCell.setType(DateHourCell.Type.DATE_CELL);
                dateHourCells.add(dateHourCell);
                date = date.plusDays(1);
            }
        }

        LocalTime time = LocalTime.of(0, 0);
        for( int i = 0; i < 8 * 24; i++){
            DateHourCell dateHourCell = new DateHourCell();
            if(  i % 8 == 0){//new row, cell time
                date = week.getFirstDateOfWeek();
                log(String.format(Locale.getDefault(),"row: %s", time.toString()));
                dateHourCell.setHour(hour);
                dateHourCell.setType(DateHourCell.Type.TIME_CELL);
                hour++;
                time = time.plusHours(1);
            }else{
                dateHourCell.setDate(date);
                dateHourCell.setHour(hour - 1);
                dateHourCell.setType(DateHourCell.Type.EVENT_CELL);
                dateHourCell.setEvents(getEvents(date, time));
                date = date.plusDays(1);
                log("EVENT_CELL");
            }
            dateHourCells.add(dateHourCell);
        }
        //Logger.logDateHourCells(dateHourCells);
        return dateHourCells;
    }


    private List<Item> getEvents(LocalDate date, LocalTime time){
        log("...getEvents(LocalDate, LocalTime)");
        return items.stream().filter(item -> item.isDateHour(date, time)).collect(Collectors.toList());
    }

    private void initComponents(View view) {
        log("...initComponents(View)");
        buttonPrev = view.findViewById(R.id.weeklyFragment_buttonPrev);
        buttonNext = view.findViewById(R.id.weeklyFragment_buttonNext);
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
        List<DateHourCell> dateHourCells = getDateHourCells(currentWeek);
        dateHourAdapter = new DateHourAdapter(dateHourCells, dateHourCell -> {
            log("...onDateHourCellSelected(DateHourCell)");
            log(dateHourCell);
            Toast.makeText(getContext(), dateHourCell.toString(), Toast.LENGTH_SHORT).show();
            showItemsDialog(dateHourCell);
        });
        recyclerCells.setLayoutManager(new GridLayoutManager(getContext(), 8));
        recyclerCells.setItemAnimator(new DefaultItemAnimator());
        recyclerCells.setAdapter(dateHourAdapter);
        recyclerCells.addItemDecoration( new DividerItemDecoration(getContext(), GridLayoutManager.VERTICAL));

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
    private void setUserInterface(LocalDate date){
        log("...setUserInterface(LocalDate)", date.toString());
        int weekNumber = date.get(WeekFields.of(Locale.getDefault()).weekOfYear());
        String textWeekNumber = String.format(Locale.getDefault(), "week %d", weekNumber);
        textViewWeekNumber.setText(textWeekNumber);
        //dateHourAdapter.setList(getDateHourCells(currentWeek));
        //sometimes i wonder why i have to do this, wny is it not sufficient to set list and notify adapter
        initRecyclerCells();
    }
    private void showItemsDialog(DateHourCell dateHourCell){
        log("...showItemsDialog()");
        ItemsDialog dialog = new ItemsDialog(dateHourCell.getEvents(), dateHourCell.getDate());
        dialog.show(getChildFragmentManager(), "show items");

    }
}
