package se.curtrune.lucy.fragments;

import static se.curtrune.lucy.util.Logger.log;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import se.curtrune.lucy.R;
import se.curtrune.lucy.adapters.MentalAdapter;
import se.curtrune.lucy.classes.Mental;
import se.curtrune.lucy.dialogs.MentalDialog;
import se.curtrune.lucy.statistics.MentalStatistics;
import se.curtrune.lucy.viewmodel.LucindaViewModel;
import se.curtrune.lucy.workers.MentalWorker;

public class MentalDayFragment extends Fragment implements MentalAdapter.Callback, MentalDialog.Callback{
    private RecyclerView recycler;
    private EditText editTextSearch;
    private RadioButton radioButtonMood;
    private RadioButton radioButtonAnxiety;
    private RadioButton radioButtonStress;
    private RadioButton radioButtonEnergy;
    private MentalAdapter adapter;
    private TextView textViewMentalLabel;
    private TextView textViewMentalTotal;
    private TextView textViewDate;
    private FloatingActionButton buttonAddMental;
    private List<Mental> mentals = new ArrayList<>();
    //private LucindaViewModel viewModel = new ViewModelProvider(this).get(LucindaViewModel.class);

    private LocalDate date;
    private MentalStatistics mentalStatistics;
    private MentalAdapter.Mental mentalType;
    private enum Mode{
        ESTIMATE, ACTUAL
    }
    private Mode mode = Mode.ESTIMATE;
    LucindaViewModel viewModel;
    public static boolean VERBOSE = false;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mental_day_fragment, container, false);
        log("MentalDayFragment.onCreateView(...)");
        initDefaults();
        initComponents(view);
        initRecycler();
        initListeners();
        initViewModel();
        initStuff();
        updateUserInterface();
        return view;
    }
    private void delete(Mental mental){
        log("...delete(Mental)", mental.getHeading());
        int rowsAffected = MentalWorker.delete(mental, getContext());
        if( rowsAffected != 1) {
            log("...error deleting mental id", mental.getID());
            Toast.makeText(getContext(), "error deleting mental", Toast.LENGTH_LONG).show();
            return;
        }
        log("...mental delete ok");
        if( !mentalStatistics.remove(mental)){
            log("...error removing mental from statistics list");
            Toast.makeText(getContext(), "error removing mental from list", Toast.LENGTH_LONG).show();
            return;
        }
        log("...remove ok");
        adapter.setList(mentalStatistics.getMentalList());
    }

    private void filter(String str){
        adapter.setList(mentalStatistics.filter(str));
        updateUserInterface();
    }
    private void initComponents(View view){
        if( VERBOSE) log("...initComponents(View)");
        recycler = view.findViewById(R.id.mentalList_recycler);
        editTextSearch = view.findViewById(R.id.mentalFragment_filter);
        radioButtonAnxiety = view.findViewById(R.id.mentalList_radioButtonAnxiety);
        radioButtonEnergy = view.findViewById(R.id. mentalList_radioButtonEnergy);
        radioButtonMood = view. findViewById(R.id.mentalList_radioButtonMood);
        radioButtonStress = view.findViewById(R.id.mentalList_radioButtonStress);
        textViewMentalLabel = view.findViewById(R.id.mentalFragment_mentalLabel);
        textViewMentalTotal = view.findViewById(R.id.mentalFragment__mentalTotal);
        buttonAddMental = view.findViewById(R.id.mentalFragment_addMentalButton);
        textViewDate = view.findViewById(R.id.mentalFragment_firstDate);
    }
    private void initDefaults(){
        log("...initDefaults()");
        date = LocalDate.now();

    }
    private void initRecycler(){
        if( VERBOSE)log("...initRecycler()");
        adapter = new MentalAdapter(new ArrayList<>(), this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(layoutManager);
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setAdapter(adapter);

    }
    private void initListeners(){
        if( VERBOSE) log("...initListeners()");
        radioButtonEnergy.setOnClickListener(view->{
            mentalType = MentalAdapter.Mental.ENERGY;
            updateUserInterface();
            });
        radioButtonMood.setOnClickListener(view->{
            mentalType = MentalAdapter.Mental.MOOD;
            updateUserInterface();
        });
        radioButtonAnxiety.setOnClickListener(view->{
            mentalType = MentalAdapter.Mental.ANXIETY;
            updateUserInterface();
        });
        radioButtonStress.setOnClickListener(view->{
            mentalType = MentalAdapter.Mental.STRESS;
            updateUserInterface();
        });
        textViewDate.setOnClickListener(view->showDateDialog());
        //textViewLastDate.setOnClickListener(view->showDateDialog(false));
        buttonAddMental.setOnClickListener(view->showMentalDialog());
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private void initStuff(){
        if( VERBOSE) log("...initStuff()");
        date = LocalDate.now();
        if( mode.equals(Mode.ACTUAL)) {
            mentalStatistics = new MentalStatistics(date, getContext());
            mentals = mentalStatistics.getMentalList();
        }else{
            //mentalStatistics = MentalStatistics.kjkjkjk;
        }
        mentalType = MentalAdapter.Mental.ENERGY;
        radioButtonEnergy.setChecked(true);
        adapter.setList(mentals);
        updateUserInterface();
    }
    private void initViewModel(){
        if( VERBOSE) log("...initViewModel()");
        viewModel = new ViewModelProvider(getActivity() ).get(LucindaViewModel.class);
    }

    @Override
    public void onItemClick(Mental item) {
        if( VERBOSE) log("...onItemClick(Mental)", item.getHeading());
        MentalDialog dialog = new MentalDialog(item);
        dialog.setCallback(this);
        dialog.show(requireActivity().getSupportFragmentManager(), "hello mental");
    }

    /**
     * callback for mentalDialog
     * @param mental, the edited, created or deleted mental
     * @param mode edit, create or delete mental
     */
    @Override
    public void onMental(Mental mental, MentalDialog.Mode mode) {
        log("MentalDayFragment.onMental(Mental, Mental", mode.toString());
        log("...onMental()", mode.toString());
        log(mental);
        switch (mode){
            case DELETE:
                delete(mental);
                mentalStatistics.remove(mental);
                updateUserInterface();
                break;
            case EDIT:
                MentalWorker.update(mental, getContext());
                adapter.notifyDataSetChanged();
                updateUserInterface();
                break;
            case CREATE_WITH_ITEM:
            case CREATE:
                MentalWorker.insert(mental, getContext());
                mentalStatistics.add(mental);
                adapter.notifyDataSetChanged();
                updateUserInterface();
        }
        viewModel.setEnergy(42);
    }
    private void showDateDialog(){
        if( VERBOSE) log("...showDateDialog()");
        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext());
        datePickerDialog.setOnDateSetListener((view, year, month, dayOfMonth) ->
        {

            date = LocalDate.of(year, month + 1, dayOfMonth);
            log("...onDateSet()", date.toString());
            updateStatistics();
        });
        datePickerDialog.show();
    }
    private void showMentalDialog(){
        log("...showMentalDialog()");
        MentalDialog dialog = new MentalDialog();
        dialog.setCallback(this);
        dialog.show(getChildFragmentManager(), "mental dialog");
    }

    /**
     */
    private void updateUserInterface(){
        log("...updateUserInterface()", mentalType.toString());
        int total = 0;
        switch (mentalType){
            case ENERGY:
                total = mentalStatistics.getTotalEnergy();
                //viewModel.setEnergy(total);
                break;
            case STRESS:
                total = mentalStatistics.getTotalStress();
                break;
            case ANXIETY:
                total = mentalStatistics.getTotalAnxiety();
                break;
            case MOOD:
                total = mentalStatistics.getTotalMood();
                break;
        }
        adapter.show(mentalType);
        textViewMentalLabel.setText(mentalType.toString());
        textViewMentalTotal.setText(String.valueOf(total));
        textViewDate.setText(date.toString());
    }
    private void updateStatistics(){
        log("...updateStatistics()", date.toString());
        mentalStatistics = new MentalStatistics(date, date, getContext());
        adapter.setList(mentalStatistics.getMentalList());
        updateUserInterface();
    }


}
