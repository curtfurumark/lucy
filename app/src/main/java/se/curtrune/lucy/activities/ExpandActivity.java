package se.curtrune.lucy.activities;

import static se.curtrune.lucy.util.Logger.log;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import se.curtrune.lucy.R;

public class ExpandActivity extends AppCompatActivity {

    private TextView textViewHeading;
    private TextView textViewDuration;
    private SeekBar seekBarEnergy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expand_activity);
        initComponents();
        initListeners();
    }
    private void initComponents(){
        log("...initComponents()");
        textViewHeading = findViewById(R.id.expandActivity_heading);
        seekBarEnergy = findViewById(R.id.expandActivity_seekBar);
        textViewDuration = findViewById(R.id.expandActivity_duration);
    }
    private void initListeners(){
        log("...initListeners()");
        textViewHeading.setOnClickListener(view->toggleEstimate());

    }
    private void toggleEstimate(){
        log("...toggleEstimate()");
        if( seekBarEnergy.getVisibility() == View.GONE) {
            seekBarEnergy.setVisibility(View.VISIBLE);
            textViewDuration.setVisibility(View.VISIBLE);
        }else{
            seekBarEnergy.setVisibility(View.GONE);
            textViewDuration.setVisibility(View.GONE);
        }
    }

}