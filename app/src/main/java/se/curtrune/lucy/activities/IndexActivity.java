package se.curtrune.lucy.activities;

import static se.curtrune.lucy.util.Logger.log;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import se.curtrune.lucy.R;
import se.curtrune.lucy.app.FirstPage;
import se.curtrune.lucy.app.Settings;
import se.curtrune.lucy.util.Constants;

public class IndexActivity extends AppCompatActivity {

    private TextView textViewTodo;
    private TextView textViewAppointments;
    private TextView textViewToday;
    private TextView textViewWeek;
    private TextView textViewMonth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.index_activity);
        log("IndexActivity.onCreate(Bundle of joy)");
        initComponents();
        initListeners();
    }
    private void initComponents(){
        log("...initComponents");
        textViewAppointments = findViewById(R.id.indexActivity_appointments);
        textViewToday = findViewById(R.id.indexActivity_today);
        textViewTodo = findViewById(R.id.indexActivity_todo);
        textViewWeek = findViewById(R.id.indexActivity_week);
        textViewMonth = findViewById(R.id.indexActivity_month);
    }
    private void initListeners(){
        log("...initListeners()");
        textViewToday.setOnClickListener(view-> startActivity(FirstPage.CALENDER_DATE));
        textViewAppointments.setOnClickListener(view-> startActivity(FirstPage.CALENDER_APPOINTMENTS));
        textViewTodo.setOnClickListener(view-> startActivity(FirstPage.TODO_FRAGMENT));
        textViewWeek.setOnClickListener(view-> startActivity(FirstPage.CALENDER_WEEK));
        textViewMonth.setOnClickListener(view->startActivity(FirstPage.CALENDER_MONTH));
    }
    private void startActivity(FirstPage firstPage){
        log("...startActivity(FirstPage)", firstPage.toString());
        Intent intentTodo = new Intent(this, MainActivity.class);
        intentTodo.putExtra(Constants.MAIN_ACTIVITY_CHILD_FRAGMENT, firstPage.toString());
        startActivity(intentTodo);


    }
}