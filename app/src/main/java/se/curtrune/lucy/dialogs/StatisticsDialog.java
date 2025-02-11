package se.curtrune.lucy.dialogs;


import static se.curtrune.lucy.util.Logger.log;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import se.curtrune.lucy.R;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.Mental;
import se.curtrune.lucy.statistics.StatisticsPeriodOld;
import se.curtrune.lucy.util.Constants;
import se.curtrune.lucy.screens.util.Converter;


public class StatisticsDialog extends BottomSheetDialogFragment {


    private SeekBar seekBarEnergy;
    private SeekBar seekBarAnxiety;
    private SeekBar seekBarStress;
    private SeekBar seekBarMood;
    private TextView textViewDepression;
    private TextView textViewDuration;

    private TextView textViewEnergy;
    private EditText editTextHeading;

    private TextView textViewDate;
    private TextView textViewTime;
    private TextView labelEnergy;
    private TextView labelAnxiety;
    private TextView labelStress;
    private TextView labelMood;
    private TextView textViewActivitiesDone;
    private TextView textViewNumberMentals;
    private Button buttonSave;
    private ArrayAdapter<String> arrayAdapter;

    private LocalDate date;
    private LocalTime time;
    private List<Item> items;
    private String[] categories;
    public static boolean VERBOSE = true;
    private List<Mental> mentalList;
    private StatisticsPeriodOld statisticsPeriod;

    public StatisticsDialog(Context context){
        log("StatisticsDialog() constructor");
        this.date = LocalDate.now();
        this.time = LocalTime.now();
        this.statisticsPeriod = new StatisticsPeriodOld(LocalDate.now(), LocalDate.now(),context);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if( VERBOSE) log("MentalDialog.onCreateView(...)");
        View view = inflater.inflate(R.layout.current_statistics_dialog, container, false);
        initComponents(view );
        initListeners();
        //
        setUserInterface(view);
        return view;
    }


    private void initComponents(View view){
        log("...initComponents()");
        buttonSave = view.findViewById(R.id.mentalDialog_button);
        textViewEnergy = view.findViewById(R.id.mentalDialog_labelEnergy);
        seekBarEnergy = view.findViewById(R.id.mentalDialog_energy);
        textViewDepression = view.findViewById(R.id.mentalDialog_labelMood);
        seekBarMood = view.findViewById(R.id.mentalDialog_mood);
        seekBarStress = view.findViewById(R.id.mentalDialog_stress);
        seekBarAnxiety = view.findViewById(R.id.mentalDialog_anxiety);
        editTextHeading = view.findViewById(R.id.mentalDialog_heading);
        textViewDate = view.findViewById(R.id.mentalDialog_date);
        textViewTime = view.findViewById(R.id.mentalDialog_time);
        labelEnergy = view.findViewById(R.id.mentalDialog_labelEnergy);
        labelAnxiety = view.findViewById(R.id.mentalDialog_labelAnxiety);
        labelStress = view.findViewById(R.id.mentalDialog_labelStress);
        labelMood = view.findViewById(R.id.mentalDialog_labelMood);
        textViewDuration = view.findViewById(R.id.currentStatistics_duration);
        textViewActivitiesDone = view.findViewById(R.id.statisticsDialog_activitiesDone);
        textViewNumberMentals = view.findViewById(R.id.statisticsDialog_numberMentals);
    }
    private void initListeners(){
        log("...initListeners()");
        buttonSave.setOnClickListener(view1 -> {
            dismiss();
        });
    }



    private void setUserInterface(View view){
        log("...setUserInterface()");
        log(statisticsPeriod);
        Duration duration = statisticsPeriod.getDuration();
        textViewDate.setText(date.toString());
        textViewTime.setText(Converter.format(LocalTime.now()));
        String strNumberMentals = String.format("%d mentals", statisticsPeriod.getNumberMentals());
        textViewNumberMentals.setText(strNumberMentals);
        String strNumberDoneActivities = String.format("%d activiites done", statisticsPeriod.getNumberItems());
        textViewActivitiesDone.setText(strNumberDoneActivities);
        textViewDuration.setText(Converter.formatSecondsWithHours(duration.getSeconds()));
        statisticsPeriod.getNumberMentals();

        int energy = statisticsPeriod.getEnergy() + Constants.ENERGY_OFFSET;
        seekBarEnergy.setProgress(energy);
        labelEnergy.setText(String.format("energy %d", statisticsPeriod.getEnergy() ));

        seekBarStress.setProgress((int) statisticsPeriod.getStress() + Constants.STRESS_OFFSET);
        labelStress.setText(String.format("stress %d", statisticsPeriod.getStress() ));

        seekBarMood.setProgress((int) statisticsPeriod.getMood() + Constants.MOOD_OFFSET);
        labelMood.setText(String.format("mood %d", statisticsPeriod.getMood() ));

        seekBarAnxiety.setProgress((int) statisticsPeriod.getAnxiety() + Constants.ANXIETY_OFFSET);
        labelAnxiety.setText(String.format("anxiety %d", statisticsPeriod.getAnxiety() ));

    }

}
