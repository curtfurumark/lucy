package se.curtrune.lucy.screens.index;

import static se.curtrune.lucy.util.Logger.log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import se.curtrune.lucy.R;
import se.curtrune.lucy.screens.main.MainActivity;
import se.curtrune.lucy.screens.index20.IndexActivityKt;
import se.curtrune.lucy.app.FirstPage;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.index_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if( item.getItemId() == R.id.indexActivity_jetpackCompose){
            startActivity(new Intent(this, IndexActivityKt.class));
        }
        return super.onOptionsItemSelected(item);
    }

    private void startActivity(FirstPage firstPage){
        log("...startActivity(FirstPage)", firstPage.toString());
        Intent intentTodo = new Intent(this, MainActivity.class);
        intentTodo.putExtra(Constants.MAIN_ACTIVITY_CHILD_FRAGMENT, firstPage.toString());
        startActivity(intentTodo);


    }
}