package se.curtrune.lucy.activities;

import static se.curtrune.lucy.util.Logger.log;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import se.curtrune.lucy.R;

public class ExpandActivity extends AppCompatActivity {

    private TextView textViewHeading;
    private LinearLayout layoutPeriod;
    private TextView textViewDuration;
    private TextView textViewPeriod;
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
        textViewPeriod = findViewById(R.id.expandActivity_labelPeriod);
        layoutPeriod = findViewById(R.id.expandActivity_layoutPeriod);
    }
    private void initListeners(){
        log("...initListeners()");
        textViewHeading.setOnClickListener(view->toggleEstimate());
        textViewPeriod.setOnClickListener(view->togglePeriod());

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
    private void togglePeriod(){
        log("...togglePeriod()");
        boolean visible = layoutPeriod.getVisibility() == View.VISIBLE;
        int visibility = visible ? View.GONE: View.VISIBLE;
        layoutPeriod.setVisibility(visibility);

    }

}