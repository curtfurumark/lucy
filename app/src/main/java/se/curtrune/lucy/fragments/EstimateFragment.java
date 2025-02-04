package se.curtrune.lucy.fragments;

import static se.curtrune.lucy.util.Logger.log;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import se.curtrune.lucy.R;
import se.curtrune.lucy.adapters.ListableAdapter;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.ItemStatistics;
import se.curtrune.lucy.classes.Listable;
import se.curtrune.lucy.classes.Mental;
import se.curtrune.lucy.screens.util.Converter;
import se.curtrune.lucy.viewmodel.EstiimateViewModel;
import se.curtrune.lucy.screens.main.MainViewModel;
import se.curtrune.lucy.workers.DurationWorker;

public class EstimateFragment extends Fragment {

    private TextView textViewEstimateDuration;
    private TextView textViewActualDuration;
    private TextView textViewEnergy;
    private TextView textViewCurrentEnergy;
    private TextView textViewStress;
    private TextView textViewCurrentStress;
    private TextView textViewAnxiety;
    private TextView textViewCurrentAnxiety;
    private TextView textViewMood;
    private TextView textViewCurrentMood;
    private TextView textViewDate;
    private TextView labelCurrent;
    private TextView labelEstimate;
    private ListableAdapter adapterDuration;
    private RecyclerView recyclerDuration;
    private EstiimateViewModel estiimateViewModel;
    private MainViewModel viewModel;
    private LocalDate currentDate;

    private List<Item> items;
    public static boolean VERBOSE = false;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        log("EstimateFragment.onCreateView(LayoutInflater, ViewGroup, Bundle)");
        View view = inflater.inflate(R.layout.estimate_fragment, container, false);
        try {
            initDefaults();
            initViewModel();
            initComponents(view);
            initRecyclerDuration();
            initListeners();
            setUserInterfaceCurrent();
            setUserInterfaceEstimate();
        }catch (Exception e){
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            log("EXCEPTION ", e.getMessage());
            e.printStackTrace();
        }
        return view;
    }
    private void initComponents(View view){
        if( VERBOSE) log("...initComponents(View) ");
        textViewEstimateDuration = view.findViewById(R.id.estimateFragment_estimatedDuration);
        textViewActualDuration = view.findViewById(R.id.estimateFragment_actualDuration);
        textViewEnergy = view.findViewById(R.id.estimateFragment_energy);
        textViewStress = view.findViewById(R.id.estimateFragment_stress);
        textViewMood = view.findViewById(R.id.estimateFragment_mood);
        textViewAnxiety = view.findViewById(R.id.estimateFragment_anxiety);
        textViewDate = view.findViewById(R.id.estimateFragment_date);
        recyclerDuration = view.findViewById(R.id.estimateFragment_recyclerDuration);
        textViewCurrentAnxiety = view.findViewById(R.id.estimateFragment_currentAnxiety);
        textViewCurrentEnergy = view.findViewById(R.id.estimateFragment_currentEnergy);
        textViewCurrentStress = view.findViewById(R.id.estimateFragment_currentStress);
        textViewCurrentMood = view.findViewById(R.id.estimateFragment_currentMood);
        labelCurrent = view.findViewById(R.id.estimateFragment_labelActual);
        labelEstimate = view.findViewById(R.id.estimateFragment__labelEstimate);
    }
    private void initDefaults(){
        if( VERBOSE) log("...initDefaults()");
        currentDate = LocalDate.now();
    }
    private void initViewModel(){
        if( VERBOSE)log("...initViewModel()");
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        estiimateViewModel = new ViewModelProvider(requireActivity()).get(EstiimateViewModel.class);
        estiimateViewModel.init(currentDate, getContext());
    }

    private void initListeners(){
        if( VERBOSE)log("...initListeners()");
        textViewDate.setOnClickListener(view->showDateDialog());
        textViewActualDuration.setOnClickListener(view->printActualDuration());
        estiimateViewModel.getEstimatedItemStatistics().observe(requireActivity(), new Observer<ItemStatistics>() {
            @Override
            public void onChanged(ItemStatistics itemStatistics) {
                log("EstimateViewModel.getEstimatedStatistics.onChanged(ItemStatistics)");
                setUserInterfaceEstimate();
            }
        });
        estiimateViewModel.getMentalEstimate().observe(requireActivity(), new Observer<Mental>() {
            @Override
            public void onChanged(Mental mental) {
                log("EstimateViewModel.getItemEstimate.onChanged(Mental)");
                setUserInterfaceEstimate();
            }
        });
    }
    private void initRecyclerDuration(){
        if( VERBOSE) log("...initRecyclerDuration()");
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
    private void initUserInterface(){
        log("...initUserInterface()");
        textViewDate.setText(currentDate.toString());
    }

    private void printActualDuration(){
        log("...printActualDuration()");
        List<Item> doneItems = items.stream().filter(item -> item.isDone()).collect(Collectors.toList());
        doneItems.forEach(System.out::println);
    }

    private void printEstimatedDuration(){
        log("...printEstimatedDuration()");
        for( Item item: items){
            long estimate = DurationWorker.getEstimatedDuration(item, getContext());
            log(String.format(Locale.getDefault(),"%s %s",item.getHeading(), Converter.formatSecondsWithHours(estimate) ));
        }
    }
    private void setUserInterfaceEstimate(){
        log("...setUserInterfaceEstimate()");
        ItemStatistics estimatedStats = estiimateViewModel.getEstimatedItemStatistics().getValue();
        String stringEstimatedDuration = String.format(Locale.getDefault(), "%s: %s", getString(R.string.duration), Converter.formatSecondsWithHours(estimatedStats.getDuration()));
        textViewEstimateDuration.setText(stringEstimatedDuration );

        String textEnergy =String.format(Locale.getDefault(), "%s: %d", getString(R.string.energy),estimatedStats.getEnergy());
        textViewEnergy.setText(textEnergy);
        String textAnxiety =String.format(Locale.getDefault(), "%s: %d",getString(R.string.anxiety), estimatedStats.getAnxiety());
        textViewAnxiety.setText(textAnxiety);
        String textStress =String.format(Locale.getDefault(), "%s: %d",getString(R.string.stress), estimatedStats.getStress());
        textViewStress.setText(textStress);
        String textMood =String.format(Locale.getDefault(), "%s: %d",getString(R.string.mood), estimatedStats.getMood());
        textViewMood.setText(textMood);
        textViewDate.setText(currentDate.toString());
    }
    private void setUserInterfaceCurrent(){
        log("...setUserInterfaceCurrent()");
        ItemStatistics currentStats = estiimateViewModel.getCurrentItemStatics().getValue();
        String stringActualDuration = String.format(Locale.getDefault(), "%s: %s", getString(R.string.duration),Converter.formatSecondsWithHours(currentStats.getDuration()));
        String stringCurrentEnergy = String.format(Locale.getDefault(), "%s %d", getString(R.string.energy), currentStats.getEnergy());
        String stringCurrentAnxiety = String.format(Locale.getDefault(), "%s %d", getString(R.string.anxiety), currentStats.getAnxiety());
        String stringCurrentStress = String.format(Locale.getDefault(), "%s %d", getString(R.string.stress), currentStats.getStress());
        String stringCurrentMood = String.format(Locale.getDefault(), "%s %d", getString(R.string.mood), currentStats.getMood());
        textViewCurrentEnergy.setText(stringCurrentEnergy);
        textViewCurrentAnxiety.setText(stringCurrentAnxiety);
        textViewCurrentStress.setText(stringCurrentStress);
        textViewCurrentMood.setText(stringCurrentMood);
        textViewActualDuration.setText(stringActualDuration);
    }
    private void showDateDialog(){
        log("...showDateDialog()");
        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext());
        datePickerDialog.setOnDateSetListener((view, year, month, dayOfMonth) -> {
            log("DatePickerDialog.onDateSet(...)");
            LocalDate date = LocalDate.of(year, month +1, dayOfMonth);
            estiimateViewModel.init(date, getContext());
        });
        datePickerDialog.show();
    }
}
