package se.curtrune.lucy.activities;

import static se.curtrune.lucy.util.Logger.log;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import se.curtrune.lucy.R;
import se.curtrune.lucy.adapters.MyCalenderAdapter;

public class MyCalenderActivity extends AppCompatActivity {
    private TextView monthYearText;
    private RecyclerView recycler;
    private LocalDate selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_calender_activity);
        setTitle("calender");
        log("MyCalenderActivity.onCreate(Bundle)");
        initComponents();
        selectedDate = LocalDate.now();
        setMonthView();
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
    private String monthYearFromDate(LocalDate date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return date.format(formatter);

    }
    public void nextMonthAction(View view){
        log("...nextMonthAction(View)");
        selectedDate = selectedDate.plusMonths(1);
        setMonthView();

    }
    public void previousMonthAction(View view){
        log("...previousMonthAction(View)");
        selectedDate = selectedDate.minusMonths(1);
        setMonthView();

    }
    private void setMonthView(){
        log("...setMonthView()");
        monthYearText.setText(monthYearFromDate(selectedDate));
        List<String> daysInMonth = daysInMonthArray(selectedDate);
        MyCalenderAdapter adapter = new MyCalenderAdapter(daysInMonth, (position, dayText) -> {
            log("onItemClick(int position, String dayText)", dayText);
            if( !dayText.equals("")){
                Toast.makeText(this, "selected date "+  dayText + " " + monthYearFromDate(selectedDate), Toast.LENGTH_LONG  ).show();
            }
        });
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        recycler.setLayoutManager(layoutManager);
        recycler.setAdapter(adapter);
    }
}