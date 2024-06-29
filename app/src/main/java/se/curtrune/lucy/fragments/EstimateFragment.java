package se.curtrune.lucy.fragments;

import static se.curtrune.lucy.util.Logger.log;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import se.curtrune.lucy.R;
import se.curtrune.lucy.adapters.ListableAdapter;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.Listable;
import se.curtrune.lucy.classes.Mental;
import se.curtrune.lucy.classes.MentalStats;
import se.curtrune.lucy.util.Converter;
import se.curtrune.lucy.workers.DurationWorker;
import se.curtrune.lucy.workers.ItemsWorker;
import se.curtrune.lucy.workers.MentalWorker;

public class EstimateFragment extends Fragment {

    private TextView textViewDuration;
    private TextView textViewEnergy;
    private TextView textViewStress;
    private TextView textViewAnxiety;
    private TextView textViewMood;
    private TextView textViewDate;
    private ListableAdapter adapterDuration;
    private RecyclerView recyclerDuration;
    private MentalStats mentalStats;
    private LocalDate date;
    private long duration;
    private List<Mental> mentals;
    private List<Item> items;
    public static boolean VERBOSE = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
           log("WTF, getArguments != null");
        }
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        log("EstimateFragment.onCreateView(LayoutInflater, ViewGroup, Bundle)");
        View view = inflater.inflate(R.layout.estimate_fragment, container, false);
        initDefaults();
        initComponents(view);
        initRecyclerDuration();
        initListeners();
        initMentalStats(date);
        setUserInterface();
        return view;
    }
    private void initComponents(View view){
        if( VERBOSE) log("...initComponents(View) ");
        textViewDuration = view.findViewById(R.id.estimateFragment_duration);
        textViewEnergy = view.findViewById(R.id.estimateFragment_energy);
        textViewStress = view.findViewById(R.id.estimateFragment_stress);
        textViewMood = view.findViewById(R.id.estimateFragment_mood);
        textViewAnxiety = view.findViewById(R.id.estimateFragment_anxiety);
        textViewDate = view.findViewById(R.id.estimateFragment_date);
        recyclerDuration = view.findViewById(R.id.estimateFragment_recyclerDuration);
    }
    private void initDefaults(){
        if( VERBOSE) log("...initDefaults()");
        date = LocalDate.now();
    }
    private void initMentalStats(LocalDate date){
        log("...initMentalStats(LocalDate)", date.toString());
        items = ItemsWorker.selectTodayList(date, getContext());
        mentalStats = MentalWorker.getMentalStats(items, getContext());
        duration = DurationWorker.getEstimatedDuration(items, getContext());
    }
    private void initListeners(){
        if( VERBOSE)log("...initListeners()");
        textViewDate.setOnClickListener(view->showDateDialog());
    }
    private void initRecyclerDuration(){
        log("...initRecyclerDuration()");
        adapterDuration = new ListableAdapter(new ArrayList<>(), new ListableAdapter.Callback() {
            @Override
            public void onItemClick(Listable item) {
                log("...onItemClick(Listable)");
            }

            @Override
            public void onLongClick(Listable item) {
                log("...onLongClick(Listable)");
            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerDuration.setLayoutManager(layoutManager);
        recyclerDuration.setItemAnimator(new DefaultItemAnimator());
        recyclerDuration.setAdapter(adapterDuration);
    }
    private void setUserInterface(){
        log("...setUserInterface(EstimateDate)");
        String textTotalDuration = String.format(Locale.getDefault(), "total duration %s", Converter.formatSecondsWithHours(duration));
        textViewDuration.setText(textTotalDuration );

        //mentalEstimate = MentalStatistics.getEstimate(items,getContext() );
        String textEnergy =String.format(Locale.getDefault(), "%s: %d", getString(R.string.energy),mentalStats.getEnergy());
        textViewEnergy.setText(textEnergy);
        String textAnxiety =String.format(Locale.getDefault(), "%s: %d",getString(R.string.anxiety), mentalStats.getAnxiety());
        textViewAnxiety.setText(textAnxiety);
        String textStress =String.format(Locale.getDefault(), "stress: %d", mentalStats.getStress());
        textViewStress.setText(textStress);
        String textMood =String.format(Locale.getDefault(), "%s: %d",getString(R.string.mood), mentalStats.getMood());
        textViewMood.setText(textMood);
        textViewDate.setText(date.toString());
    }
    private void showDateDialog(){
        log("...showDateDialog()");

    }
}
