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
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import se.curtrune.lucy.R;
import se.curtrune.lucy.adapters.MentalAdapter;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.Mental;
import se.curtrune.lucy.classes.MentalEstimate;
import se.curtrune.lucy.dialogs.MentalDialog;
import se.curtrune.lucy.statistics.MentalStatistics;
import se.curtrune.lucy.workers.ItemsWorker;
import se.curtrune.lucy.workers.MentalWorker;

public class MentalFragment extends Fragment implements MentalAdapter.Callback {
    private RecyclerView recycler;
    private EditText editTextSearch;
    private RadioButton radioButtonMood;
    private RadioButton radioButtonAnxiety;
    private RadioButton radioButtonStress;
    private RadioButton radioButtonEnergy;
    private MentalAdapter adapter;
    private TextView textViewMentalLabel;
    private TextView textViewMentalTotal;
    private TextView textViewFirstDate;
    private TextView textViewLastDate;
    private FloatingActionButton buttonAddMental;
    private List<Mental> mentals = new ArrayList<>();

    private LocalDate firstDate;
    private LocalDate lastDate;
    private MentalStatistics mentalStatistics;
    private MentalAdapter.Mental mentalType;

    private enum Mode{
        ESTIMATE, ACTUAL
    }
    private Mode mode = Mode.ESTIMATE;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mental_fragment, container, false);
        log("MentalFragment.onCreateView(...)");
        initComponents(view);
        initRecycler();
        initListeners();
        initStuff();
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
        log("...initComponents(View)");
        recycler = view.findViewById(R.id.mentalList_recycler);
        editTextSearch = view.findViewById(R.id.mentalFragment_filter);
        radioButtonAnxiety = view.findViewById(R.id.mentalList_radioButtonAnxiety);
        radioButtonEnergy = view.findViewById(R.id. mentalList_radioButtonEnergy);
        radioButtonMood = view. findViewById(R.id.mentalList_radioButtonMood);
        radioButtonStress = view.findViewById(R.id.mentalList_radioButtonStress);
        textViewMentalLabel = view.findViewById(R.id.mentalFragment_mentalLabel);
        textViewMentalTotal = view.findViewById(R.id.mentalFragment__mentalTotal);
        buttonAddMental = view.findViewById(R.id.mentalFragment_addMentalButton);
        textViewFirstDate = view.findViewById(R.id.mentalFragment_firstDate);
        textViewLastDate = view.findViewById(R.id.mentalFragment_lastDate);
    }
    private void initRecycler(){
        log("...initRecycler()");
        adapter = new MentalAdapter(mentals, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(layoutManager);
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setAdapter(adapter);

    }
    private void initListeners(){
        log("...initListeners()");
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
        textViewFirstDate.setOnClickListener(view->showDateDialog(true));
        textViewLastDate.setOnClickListener(view->showDateDialog(false));
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

    /**
     * sorry but the naming of this on, and it is a mixed bag, it should not be a mixed bag but it is
     * TODO, please do something about it
     */
    private void initStuff(){
        log("...initStuff()");
        lastDate = LocalDate.now();
        firstDate = lastDate.minusDays(7);
        mentalStatistics = new MentalStatistics(firstDate, lastDate, getContext());
        mentals = mentalStatistics.getMentalList();
        mentalType = MentalAdapter.Mental.ENERGY;
        radioButtonEnergy.setChecked(true);
        adapter.setList(mentals);
        updateUserInterface();
    }
    private void initEstimateStuff(){
        log("...initEstimateStuff()");
        List<Item> items = ItemsWorker.selectTodayList(LocalDate.now(), getContext());
        MentalEstimate estimate = MentalStatistics.getEstimate(items,getContext() );

    }

    @Override
    public void onItemClick(Mental item) {
        log("...onItemClick(Mental)");
        MentalDialog dialog = new MentalDialog(item);
        dialog.setCallback((mental, mode) ->{
            log("...mental dialog callback ", mode.toString());
            switch (mode){
                case DELETE:
                    delete(mental);
                    break;
                case EDIT:
                    MentalWorker.update(mental, getContext());
                    adapter.notifyDataSetChanged();
                    break;
                case CREATE_WITH_ITEM:
                case CREATE:
                    mental = MentalWorker.insert(mental, getContext());
                    mentals.add(0, mental);
                    adapter.notifyDataSetChanged();
                    break;

            }


        });
        dialog.show(requireActivity().getSupportFragmentManager(), "hello mental");
    }


    private void showDateDialog(boolean setFirstDate){
        log("...showDateDialog");
        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext());
        datePickerDialog.setOnDateSetListener((view, year, month, dayOfMonth) ->
        {
            log("...onDateSet()");
            if( setFirstDate) {
                firstDate = LocalDate.of(year, month + 1, dayOfMonth);
            }else{
                lastDate = LocalDate.of(year, month +1, dayOfMonth);
            }
        });
        datePickerDialog.show();
        updateStatistics();
    }
    private void showMentalDialog(){
        log("...showMentalDialog()");
        MentalDialog dialog = new MentalDialog();
        dialog.setCallback((mental, mode) -> {
            log("...onMental()", mode.toString());
            log(mental);
            switch (mode){
                case DELETE:
                    delete(mental);
                    break;
                case EDIT:
                    MentalWorker.update(mental, getContext());
                    adapter.notifyDataSetChanged();
                    break;
                case CREATE_WITH_ITEM:
                case CREATE:
                    MentalWorker.insert(mental, getContext());
                    mentals.add(0, mental);
                    adapter.notifyDataSetChanged();

            }
        });
        dialog.show(getChildFragmentManager(), "mental dialog");

    }

    /**
     */
    private void updateUserInterface(){
        log("...updateUserInterface()");
        int total = 0;
        switch (mentalType){
            case ENERGY:
                total = mentalStatistics.getTotalEnergy();
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
        textViewLastDate.setText(lastDate.toString());
        textViewFirstDate.setText(firstDate.toString());

    }
/*    @Override
    public void onMental(Mental mental, MentalDialog.Mental mentalType) {
        log("MentalFragment.onMental, MentalDialog.Mental");
    }*/
    private void updateStatistics(){
        log("...updateStatistics()");
        mentalStatistics = new MentalStatistics(firstDate, lastDate, getContext());
        mentals = mentalStatistics.getMentalList();
        updateUserInterface();
    }

}
