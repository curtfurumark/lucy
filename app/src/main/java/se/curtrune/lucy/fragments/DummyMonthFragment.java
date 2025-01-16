package se.curtrune.lucy.fragments;

import static se.curtrune.lucy.util.Logger.log;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.time.YearMonth;
import java.util.List;

import se.curtrune.lucy.R;
import se.curtrune.lucy.classes.calender.CalenderDate;
import se.curtrune.lucy.persist.CalenderWorker;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DummyMonthFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DummyMonthFragment extends Fragment {

    private TextView textView;
    private YearMonth yearMonth;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DummyMonthFragment() {
        // Required empty public constructor
    }
    public DummyMonthFragment(YearMonth yearMonth, int position){
        log("DummyMonthFragment(YearMonth)", yearMonth.toString());
        log("\t\tposition", position);
        this.yearMonth = yearMonth;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DummyMonthFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DummyMonthFragment newInstance(String param1, String param2) {
        DummyMonthFragment fragment = new DummyMonthFragment();
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
        View view =  inflater.inflate(R.layout.fragment_dummy_month, container, false);
        log("DummyMonthFragment.onCreateView(...)");
        initViews(view);
        getCalenderDates();
        return view;
    }
    private void initViews(View view){
        log("...initViews(View)");
        textView = view.findViewById(R.id.dummyMonthFragment_month);
        textView.setText(yearMonth.toString());
    }
    private void getCalenderDates(){
        log("...getCalenderDates()");
        List<CalenderDate> calenderDateList = CalenderWorker.getCalenderDates(yearMonth, getContext());
        calenderDateList.forEach(System.out::println);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        log("...setUserVisibleHint(boolean)", isVisibleToUser);
    }
}