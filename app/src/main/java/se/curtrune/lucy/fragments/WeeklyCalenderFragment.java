package se.curtrune.lucy.fragments;

import static se.curtrune.lucy.util.Logger.log;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.List;

import se.curtrune.lucy.R;
import se.curtrune.lucy.classes.Mental;
import se.curtrune.lucy.statistics.TopTenStatistics;
import se.curtrune.lucy.util.Logger;

public class WeeklyCalenderFragment extends Fragment {
    private Button buttonNext;
    private Button buttonPrev;
    private TextView textViewLabelWeek;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        log("TopTenFragment.onCreateView(...)");
        View view =  inflater.inflate(R.layout.weekly_fragment, container, false);
        initComponents(view);
        //initListeners();
        initRecycler();
        List<Mental> mentals = TopTenStatistics.getItems(getContext());
        mentals.forEach(Logger::log);
        //show(TopTenFragment.Mode.ENERGY);
        return view;
    }
    private void initComponents(View view){
        log("...initComponents(View)");
        buttonPrev = view.findViewById(R.id.weeklyFragment_buttonPrev);
        buttonNext = view.findViewById(R.id.weeklyFragment_buttonNext);
        //textViewLabelWeek = view.findViewById(R.id.weeklyFragment_buttonPrev);
    }
    private void initRecycler(){
        log("...initRecycler()");

    }
}
