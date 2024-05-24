package se.curtrune.lucy.activities;

import static se.curtrune.lucy.util.Logger.log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import se.curtrune.lucy.R;

public class LoggerActivity extends AppCompatActivity {

    private RecyclerView recycler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logger_activity);
        initComponents();
    }
    private void initComponents(){
        log("...initComponents()");
        recycler = findViewById(R.id.loggerActivity_recycler);
    }
}