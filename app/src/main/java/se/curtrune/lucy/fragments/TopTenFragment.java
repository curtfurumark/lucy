package se.curtrune.lucy.fragments;

import static se.curtrune.lucy.util.Logger.log;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import se.curtrune.lucy.R;
import se.curtrune.lucy.adapters.ListableAdapter;
import se.curtrune.lucy.classes.Listable;
import se.curtrune.lucy.classes.Mental;
import se.curtrune.lucy.statistics.TopTenStatistics;
import se.curtrune.lucy.util.Logger;
import se.curtrune.lucy.workers.MentalWorker;

public class TopTenFragment extends Fragment implements ListableAdapter.Callback {
    private RecyclerView recycler;
    private ListableAdapter adapter;
    private RadioButton radioButtonEnergy;
    private RadioButton radioButtonStress;
    private RadioButton radioButtonAnxiety;
    private RadioButton radioButtonMood;
    public enum Mode{
        ANXIETY, MOOD, ENERGY, STRESS
    }
    private Mode mode = Mode.ENERGY;
    private List<Listable> items = new ArrayList<>();
    public TopTenFragment(){
        log("TopTenFragment()");
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        log("TopTenFragment.onCreate()");
    }    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        log("TopTenFragment.onCreateView(...)");
        View view =  inflater.inflate(R.layout.tag_fragment, container, false);
        initComponents(view);
        initListeners();
        initRecycler();
        List<Mental> mentals = TopTenStatistics.getItems(getContext());
        mentals.forEach(Logger::log);
        return view;
    }
    private void initComponents(View view){
        log("...initComponents(View)");
        recycler = view.findViewById(R.id.topTenActivity_recycler);
        radioButtonAnxiety = view.findViewById(R.id.topTen_radioButtonAnxiety);
        radioButtonStress = view.findViewById(R.id.topTen_radioButtonStress);
        radioButtonMood = view.findViewById(R.id.topTen_radioButtonMood);
        radioButtonEnergy = view.findViewById(R.id.topTen_radioButtonEnergy);
        radioButtonEnergy.setChecked(true);
    }
    private void initListeners(){
        log("...initListeners()");
        radioButtonEnergy.setOnClickListener(view->show(Mode.ENERGY));
        radioButtonAnxiety.setOnClickListener(view->show(Mode.ANXIETY));
        radioButtonStress.setOnClickListener(view->show(Mode.STRESS));
        radioButtonMood.setOnClickListener(view->show(Mode.MOOD));
    }
    private void initRecycler(){
        log(",,,initRecycler()");
        adapter = new ListableAdapter(items, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(layoutManager);
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setAdapter(adapter);
    }

    @Override
    public void onItemClick(Listable item) {
        log("...onItemClick()");
    }

    @Override
    public void onLongClick(Listable item) {

    }
    private void show(Mode mode){
        log("...show()", mode.toString());
        List<Mental> mentals = MentalWorker.selectTopTen(mode, getContext());
        adapter.setList(new ArrayList<>(mentals));
    }
}
