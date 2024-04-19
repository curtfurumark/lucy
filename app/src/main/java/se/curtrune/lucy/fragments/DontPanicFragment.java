package se.curtrune.lucy.fragments;

import static se.curtrune.lucy.util.Logger.log;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import se.curtrune.lucy.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DontPanicFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DontPanicFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button buttonPanic;
    private Button buttonOK;

    public DontPanicFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DontPanicFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DontPanicFragment newInstance(String param1, String param2) {
        DontPanicFragment fragment = new DontPanicFragment();
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
        View view = inflater.inflate(R.layout.dont_panic_fragment, container, false);
        initComponents(view);
        initListeners();
        return view;
    }
    private void goToCalender(){


    }
    private  void initComponents(View view){
        log("...initComponents()");
        buttonOK = view.findViewById(R.id.dontPanicFragment_buttonOK);
        buttonPanic = view.findViewById(R.id.dontPanicFragment_buttonStillPanicking);
    }
    private void initListeners(){
        log("....initListeners()");
        buttonOK.setOnClickListener(view->goToCalender());
        buttonPanic.setOnClickListener(view->panic());
    }
    private void panic(){


    }
}