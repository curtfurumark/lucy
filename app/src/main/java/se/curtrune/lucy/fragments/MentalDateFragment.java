package se.curtrune.lucy.fragments;

import static se.curtrune.lucy.util.Logger.log;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import se.curtrune.lucy.R;
import se.curtrune.lucy.adapters.MentalAdapter;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.Mental;
import se.curtrune.lucy.classes.MentalStats;
import se.curtrune.lucy.classes.State;
import se.curtrune.lucy.viewmodel.LucindaViewModel;
import se.curtrune.lucy.viewmodel.MentalDateViewModel;

/**
 * don't know what this is
 * check it out
 */

public class MentalDateFragment extends Fragment implements MentalAdapter.Callback{
    private RecyclerView recycler;
    private RadioButton radioButtonMood;
    private RadioButton radioButtonAnxiety;
    private RadioButton radioButtonStress;
    private RadioButton radioButtonEnergy;
    private MentalAdapter adapter;

    private TextView textViewDate;
    private LucindaViewModel lucindaViewModel;

    private Switch switchActual;
    private LocalDate currentDate;
    private MentalStats mentalStats;
    private MentalAdapter.MentalType mentalType;
    private enum Mode{
        ESTIMATE, ACTUAL
    }
    private Mode currentMode = Mode.ESTIMATE;
    private LucindaViewModel viewModel;
    private MentalDateViewModel mentalDateViewModel;
    public static boolean VERBOSE = false;
    public MentalDateFragment(){
        currentDate = LocalDate.now();
        currentMode = Mode.ESTIMATE;
    }
    public MentalDateFragment(LocalDate date, boolean actual){
        currentDate = date;
        currentMode = actual ? Mode.ACTUAL: Mode.ESTIMATE;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mental_day_fragment, container, false);
        log("MentalDateFragment.onCreateView(...)");
        initDefaults();
        initComponents(view);
        initViewModel();
        initRecycler();
        updateUserInterface();
        radioButtonEnergy.setChecked(true);
        initListeners();
        return view;
    }

    private void filter(String str){
        List<Mental> filteredList = mentalStats.getMentals();
        //adapter.setList(mentalStatistics.filter(str));t
        updateUserInterface();
    }
    private void initComponents(View view){
        if( VERBOSE) log("...initComponents(View)");
        recycler = view.findViewById(R.id.mentalDayFragment_recycler);
        //editTextSearch = view.findViewById(R.id.mentalFragment_filter);
        radioButtonAnxiety = view.findViewById(R.id.mentalList_radioButtonAnxiety);
        radioButtonEnergy = view.findViewById(R.id. mentalList_radioButtonEnergy);
        radioButtonMood = view. findViewById(R.id.mentalList_radioButtonMood);
        radioButtonStress = view.findViewById(R.id.mentalList_radioButtonStress);
        textViewDate = view.findViewById(R.id.mentalDayFragment_date);
        switchActual = view.findViewById(R.id.mentalDayFragment_modeSwitch);
    }
    private void initDefaults(){
        log("...initDefaults()");
        currentDate = LocalDate.now();
        mentalType = MentalAdapter.MentalType.ENERGY;
    }
    private void initRecycler(){
        if( VERBOSE)log("...initRecycler()");
        adapter = new MentalAdapter(mentalDateViewModel.getItems().getValue(), this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(layoutManager);
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setAdapter(adapter);

    }
    private void initListeners(){
        if( VERBOSE) log("...initListeners()");
        switchActual.setOnClickListener(v -> {
            log("onClick(View) switch actual that is");
            currentMode = switchActual.isChecked()? Mode.ACTUAL: Mode.ESTIMATE;
            initMentalStats(currentDate, currentMode);
        });
        radioButtonEnergy.setOnClickListener(view->{
            mentalType = MentalAdapter.MentalType.ENERGY;
            adapter.setMentalType(mentalType);
            updateLucindaViewModel();
            });
        radioButtonMood.setOnClickListener(view->{
            mentalType = MentalAdapter.MentalType.MOOD;
            adapter.setMentalType(mentalType);
            updateLucindaViewModel();
        });
        radioButtonAnxiety.setOnClickListener(view->{
            mentalType = MentalAdapter.MentalType.ANXIETY;
            adapter.setMentalType(mentalType);
            updateLucindaViewModel();
        });
        radioButtonStress.setOnClickListener(view->{
            mentalType = MentalAdapter.MentalType.STRESS;
            adapter.setMentalType(mentalType);
            updateLucindaViewModel();
        });
        textViewDate.setOnClickListener(view->showDateDialog());
        mentalDateViewModel.getItems().observe(requireActivity(), new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> items) {
                log("MentalDateFragment.onChanged(List<Item>)");
                adapter.setList(items);
                textViewDate.setText(currentDate.toString());
            }
        });
    }

    /**
     * if mode estimate, get all items that are to be done this date
     * if mode action get all items that are done today
     */
    private void initMentalStats(LocalDate date, Mode mode){
        log("...initMentalStats(LocalDate, Mode)", mode.toString());
/*        List<Item> items;
        if( date.equals(LocalDate.now())) {
            if (mode.equals(Mode.ACTUAL)) {
                items = ItemsWorker.selectDateState(date, State.DONE, getContext());
            } else {
                items = ItemsWorker.selectTodayList(date, getContext());
            }
        }else if( currentDate.isAfter(LocalDate.now())){
            items = ItemsWorker.selectDateState(date, State.TODO, getContext());
        }else{
            switchActual.setVisibility(View.GONE);
            items = ItemsWorker.selectDateState(date,State.DONE,  getContext());
        }
        items.forEach(System.out::println);
        mentalStats = MentalWorker.getStatistics(items, getContext());
        mentals = mentalStats.getMentals();
        mentalType = MentalAdapter.MentalType.ENERGY;
        radioButtonEnergy.setChecked(true);
        updateUserInterface();*/
    }
    private void initViewModel(){
        if( VERBOSE) log("...initViewModel()");
        lucindaViewModel = new ViewModelProvider(requireActivity()).get(LucindaViewModel.class);
        mentalDateViewModel = new ViewModelProvider(requireActivity()).get(MentalDateViewModel.class);
        mentalDateViewModel.setDate(currentDate, getContext());
    }

    @Override
    public void onItemClick(Item item) {
        if( VERBOSE) log("...onItemClick(Item)", item.getHeading());
        lucindaViewModel.updateFragment(new ItemSessionFragment(item));
    }

    @Override
    public void onProgress(Item item, MentalAdapter.MentalType type, int progress) {
        log(String.format("%s, %s, %d", item.getHeading(), type.toString(), progress));
        mentalDateViewModel.updateItem(item, type, progress, getContext());
        if(item.getState().equals(State.DONE)){
            lucindaViewModel.resetMental(getContext(), type);
        }
    }

    private void setSwitchActual(LocalDate date){
        log("...setSwitchActual()");
        if( date.equals(LocalDate.now())){
            switchActual.setVisibility(View.VISIBLE);
            switchActual.setChecked(currentMode.equals(Mode.ACTUAL) ? true: false);
        }else{
            switchActual.setVisibility(View.INVISIBLE);
        }


    }
    private void showDateDialog(){
        if( VERBOSE) log("...showDateDialog()");
        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext());
        datePickerDialog.setOnDateSetListener((view, year, month, dayOfMonth) ->
        {
            currentDate = LocalDate.of(year, month + 1, dayOfMonth);
            log("...onDateSet()", currentDate.toString());
            mentalDateViewModel.setDate(currentDate, getContext());
        });
        datePickerDialog.show();
    }

    /**
     */
    private void updateUserInterface(){
        if( VERBOSE) log("...updateUserInterface()", mentalType.toString());
        int total = 0;
        //setSwitchActual(currentDate);
        String mentalLabel = "Energy";
        switch (mentalType){
            case ENERGY:
                //total = mentalStats.getEnergy();
                mentalLabel =  getString(R.string.energy);
                break;
            case STRESS:
                //total = mentalStats.getStress();
                mentalLabel = getString(R.string.stress);
                break;
            case ANXIETY:
                //total = mentalStats.getAnxiety();
                mentalLabel = getString(R.string.anxiety);
                break;
            case MOOD:
                //total = mentalStats.getMood();
                mentalLabel = getString(R.string.mood);
                break;
        }
        textViewDate.setText(currentDate.toString());
    }
    private void updateLucindaViewModel(){
        log("...updateLucindaViewModel()");
        lucindaViewModel.setMentalType(mentalType);
    }
}
