package se.curtrune.lucy.dialogs;


import static se.curtrune.lucy.util.Logger.log;

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

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import se.curtrune.lucy.R;
import se.curtrune.lucy.classes.MentalStatistics;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.Mental;
import se.curtrune.lucy.classes.State;
import se.curtrune.lucy.util.Converter;
import se.curtrune.lucy.util.Settings;
import se.curtrune.lucy.workers.ItemsWorker;
import se.curtrune.lucy.workers.MentalWorker;
import se.curtrune.lucy.workers.StatisticsWorker;


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


    private Button buttonSave;
    private ArrayAdapter<String> arrayAdapter;

    private LocalDate date;
    private LocalTime time;
    private List<Item> items;
    private String[] categories;
    public static boolean VERBOSE = true;
    private List<Mental> mentalList;

    public StatisticsDialog(){
        log("StatisticsDialog() constructor");
        this.date = LocalDate.now();
        this.time = LocalTime.now();
    }
    public StatisticsDialog(List<Item> items){
        this();
        this.items = items;

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
    }
    private void initListeners(){
        log("...initListeners()");
        buttonSave.setOnClickListener(view1 -> {
            dismiss();
        });
    }



    private void setUserInterface(View view){
        log("...setUserInterface()");
        mentalList = MentalWorker.getLatestMentals(10, view.getContext());
        MentalStatistics stats = new MentalStatistics(mentalList);
        List<Item> items = ItemsWorker.selectItems(LocalDate.now(), view.getContext(), State.DONE);
        long seconds = StatisticsWorker.getSum(items);
        textViewDate.setText(date.toString());
        textViewDuration.setText(Converter.formatSecondsWithHours(seconds));
        seekBarEnergy.setProgress((int) stats.getAverageEnergy() +5);
        labelEnergy.setText(String.format("energy %.1f", stats.getAverageEnergy() ));
        seekBarStress.setProgress(0);
        seekBarMood.setProgress(0);
        seekBarAnxiety.setProgress(0);

    }

    /**
     * sets labels to current progress
     */
    private void updateUserInterface(){
        log("...updateUserInterface()");
        String strEnergy = String.format("energy %d", seekBarEnergy.getProgress() - Settings.ENERGY_OFFSET);
        String strMood = String.format("mood %d", seekBarMood.getProgress() - Settings.MOOD_OFFSET);
        String strAnxiety = String.format("anxiety %d", seekBarAnxiety.getProgress());
        String strStress = String.format("stress %d", seekBarStress.getProgress());
        labelEnergy.setText(strEnergy);
        labelMood.setText(strMood);
        labelStress.setText(strStress);
        labelAnxiety.setText(strAnxiety);

    }
}
