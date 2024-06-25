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



    private Button buttonPanic;
    private Button buttonOK;

    public DontPanicFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment DontPanicFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DontPanicFragment newInstance() {
        return  new DontPanicFragment();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            log("...getArguments != null");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        log("DontPanicFragment.onCreateView(LayoutInflater, ViewGroup, Bundle)");
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