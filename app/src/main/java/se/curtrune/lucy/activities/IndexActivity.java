package se.curtrune.lucy.activities;

import static se.curtrune.lucy.util.Logger.log;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import se.curtrune.lucy.R;

public class IndexActivity extends AppCompatActivity {

    private TextView textViewTodo;
    private TextView textViewAppointments;
    private TextView textViewToday;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.index_activity);
        log("IndexActivity.onCreate(Bundle of joy)");
        initComponents();
        initListeners();
    }
    private void initComponents(){
        textViewAppointments = findViewById(R.id.indexActivity_appointments);
        textViewToday = findViewById(R.id.indexActivity_today);
        textViewTodo = findViewById(R.id.indexActivity_todo);
    }
    private void initListeners(){
        textViewToday.setOnClickListener(view-> startActivity(new Intent(this, MainActivity.class)));
        Intent intentTodo = new Intent(this, MainActivity.class);


    }
}