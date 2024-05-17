package se.curtrune.lucy.fragments;

import static se.curtrune.lucy.util.Logger.log;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.time.LocalDate;
import java.util.Locale;

import se.curtrune.lucy.R;
import se.curtrune.lucy.classes.Estimate;
import se.curtrune.lucy.classes.EstimateDate;
import se.curtrune.lucy.workers.ItemsWorker;

public class EstimateFragment extends Fragment {

    private TextView textViewDuration;
    private TextView textViewEnergy;
    private TextView textViewStress;
    private TextView textViewAnxiety;
    private TextView textViewMood;
    private EstimateDate estimateDate;
    private LocalDate date;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
           // mParam1 = getArguments().getString(ARG_PARAM1);
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        log("EstimateFragment.onCreateView(LayoutInflater, ViewGroup, Bundle)");
        View view = inflater.inflate(R.layout.estimate_fragment, container, false);
        initComponents(view);
        initEstimate(LocalDate.now());
        setUserInterface(estimateDate);
        return view;
    }
    private void initComponents(View view){
        log("...initComponents(View view) ");
        textViewDuration = view.findViewById(R.id.estimateFragment_duration);
        textViewEnergy = view.findViewById(R.id.estimateFragment_energy);
        textViewStress = view.findViewById(R.id.estimateFragment_stress);
        textViewMood = view.findViewById(R.id.estimateFragment_mood);
        textViewAnxiety = view.findViewById(R.id.estimateFragment_anxiety);
    }
    private void initEstimate(LocalDate date){
        log("...initEstimate()");
        estimateDate = new EstimateDate(date, getContext());

    }
    private void setUserInterface(EstimateDate estimateDate){
        log("...setUserInterface(EstimateDate)");
        String textTotalDuration = String.format(Locale.getDefault(), "total duration %d seconds",estimateDate.getDurationEstimate());
        textViewDuration.setText(textTotalDuration );
        estimateDate.getEnergyEstimate();
        String textEnergy =String.format(Locale.getDefault(), "energy: %d", estimateDate.getEnergyEstimate());
        textViewEnergy.setText(textEnergy);
        String textAnxiety =String.format(Locale.getDefault(), "anxiety: %d", estimateDate.getAnxiety());
        textViewAnxiety.setText(textAnxiety);
        String textStress =String.format(Locale.getDefault(), "stress: %d", estimateDate.getStressEstimate());
        textViewStress.setText(textStress);
        String textMood =String.format(Locale.getDefault(), "mood: %d", estimateDate.getMoodEstimate());
        textViewMood.setText(textMood);
    }
}
