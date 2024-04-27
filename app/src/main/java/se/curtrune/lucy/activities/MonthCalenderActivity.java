package se.curtrune.lucy.activities;

import static se.curtrune.lucy.util.Logger.log;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import se.curtrune.lucy.R;
import se.curtrune.lucy.adapters.MonthAdapter;
import se.curtrune.lucy.adapters.SequenceAdapter;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.calender.CalenderDate;
import se.curtrune.lucy.workers.ItemsWorker;

public class MonthCalenderActivity extends AppCompatActivity {
    private TextView monthYearText;
    private RecyclerView recycler;
    private LocalDate selectedDate;
    private MonthAdapter adapter;
    private List<CalenderDate> calenderDates;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_calender_activity);
        setTitle("calender");
        log("MonthCalenderActivity.onCreate(Bundle)");
        initComponents();
        selectedDate = LocalDate.now();
        calenderDates = getCalenderDates();
        initRecycler(calenderDates);
        initListeners();
        //setMonthView();
        //getCalenderDates();
    }
    private List<CalenderDate> getCalenderDates(){
        log("getCalenderDate()");
        List<CalenderDate> calenderDates = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(selectedDate);
        log("...yearMonth", yearMonth.toString());
        LocalDate firstDateOfMonth =  selectedDate.withDayOfMonth( 1);
        log("...firstDateOfMonth", firstDateOfMonth.toString());
        int lengthOfMonth = yearMonth.lengthOfMonth();
        log("...lengthOfMonth", lengthOfMonth);
        List<Item> itemsMonth = ItemsWorker.selectCalenderItems(yearMonth, this);
        log("...number of events this month", itemsMonth.size());
        itemsMonth.forEach(System.out::println);
        LocalDate currentDate = firstDateOfMonth;
        int numberDays = lengthOfMonth;
        for(int i = 0; i < numberDays; i++ ){
            CalenderDate calenderDate = new CalenderDate();
            //log("...currentDate", currentDate.toString());
            calenderDate.setDate(currentDate);
            LocalDate finalCurrentDate = currentDate;
            calenderDate.setItems(itemsMonth.stream().filter(item -> item.isDate(finalCurrentDate)).collect(Collectors.toList()));
            calenderDates.add(calenderDate);
            currentDate = currentDate.plusDays(1);
        }
        return calenderDates;
    }
    private List<String> daysInMonthArray(LocalDate date){
        log("...daysInMonthArray(LocalDate)", date.toString());
        List<String> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);
        int daysInMonth = yearMonth.lengthOfMonth();
        LocalDate firstOfMonth = selectedDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();
        for( int i = 1; i <= 42; i++){
            if( i <= dayOfWeek || i > daysInMonth + dayOfWeek){
                daysInMonthArray.add("");
            }else{
                daysInMonthArray.add(String.valueOf(i - dayOfWeek));
            }

        }
        return daysInMonthArray;
    }
    private void initComponents(){
        recycler = findViewById(R.id.myCalender_recycler);
        monthYearText = findViewById(R.id.myCalender_month);
    }
    private void initListeners(){


    }
    private void initRecycler(List<CalenderDate> calenderDates){
        log("...initRecycler()");
        adapter = new MonthAdapter(calenderDates, new MonthAdapter.OnDateListener() {
            @Override
            public void onDateClick(CalenderDate calenderDate) {
                log("onDateClick(CalenderDate)", calenderDate.getDate().toString());
            }
        });

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        recycler.setLayoutManager(layoutManager);
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setAdapter(adapter);
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recycler);

    }
    private String monthYearFromDate(LocalDate date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return date.format(formatter);

    }
    public void nextMonthAction(View view){
        log("...nextMonthAction(View)");
        selectedDate = selectedDate.plusMonths(1);
        //setMonthView();

    }
    public void previousMonthAction(View view){
        log("...previousMonthAction(View)");
        selectedDate = selectedDate.minusMonths(1);
        //setMonthView();

    }
/*    private void setMonthView(){
        log("...setMonthView()");
        monthYearText.setText(monthYearFromDate(selectedDate));
        List<String> daysInMonth = daysInMonthArray(selectedDate);
        MonthAdapter adapter = new MonthAdapter(daysInMonth, (position, dayText) -> {
            log("onItemClick(int position, String dayText)", dayText);
            if( !dayText.equals("")){
                Toast.makeText(this, "selected date "+  dayText + " " + monthYearFromDate(selectedDate), Toast.LENGTH_LONG  ).show();
            }
        });
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        recycler.setLayoutManager(layoutManager);
        recycler.setAdapter(adapter);
    }*/
}