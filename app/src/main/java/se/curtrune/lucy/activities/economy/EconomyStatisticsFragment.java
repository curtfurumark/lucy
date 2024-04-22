package se.curtrune.lucy.activities.economy;

import static se.curtrune.lucy.util.Logger.log;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.time.LocalDate;

import se.curtrune.lucy.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EconomyStatisticsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EconomyStatisticsFragment extends Fragment {
    private TextView textViewFirstDate;
    private TextView textViewLastDate;
    private LocalDate firstDate;
    private LocalDate lastDate;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EconomyStatisticsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EconomyStatisticsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EconomyStatisticsFragment newInstance(String param1, String param2) {
        EconomyStatisticsFragment fragment = new EconomyStatisticsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.economy_statistics_fragment, container, false);
        initDefaults();
        initComponents(view);
        setUserInterface();
        return view;
    }
    private void initComponents(View view){
        log("...initComponents()");
        textViewFirstDate = view.findViewById(R.id.ecStatistics_firstDate);
        textViewLastDate = view.findViewById(R.id.ecStatistics_lastDate);
    }
    private void initDefaults(){
        log("...initDefaults()");
        lastDate = LocalDate.now();
        firstDate = lastDate.minusDays(7);
    }
    private void setUserInterface(){
        log("...setUserInterfaceU()");
        textViewFirstDate.setText(firstDate.toString());
        textViewLastDate.setText(lastDate.toString());


    }
}