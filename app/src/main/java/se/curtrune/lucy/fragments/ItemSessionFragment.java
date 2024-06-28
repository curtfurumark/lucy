package se.curtrune.lucy.fragments;

import static se.curtrune.lucy.util.Logger.log;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import se.curtrune.lucy.R;
import se.curtrune.lucy.classes.Item;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ItemSessionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ItemSessionFragment extends Fragment {


    private TextView textViewEstimatedTime;
    private TextView textViewHeading;

    private Item currentItem;

    public ItemSessionFragment() {
        // Required empty public constructor
    }
    public ItemSessionFragment(Item item){
        assert  item != null;
        log("ItemSessionFragment(Item)", item.getHeading());
        this.currentItem = item;

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters..
     * @return A new instance of fragment ItemSessionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ItemSessionFragment newInstance(Item item) {
        return new ItemSessionFragment(item);

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
        log("ItemsFragment.onCreateView(LayoutInflater, ViewGroup, Bundle)");
        View view = inflater.inflate(R.layout.item_session_fragment, container, false);
        initComponents(view);
        initListeners();
        setUserInterface(currentItem);
        return view;
    }
    private void goToCalendar(){
        log("...goToCalendar()");
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.navigationDrawer_frameContainer, new CalenderFragment()).commit();

    }
    private void initComponents(View view){
        log("...initComponents()");
        textViewEstimatedTime = view.findViewById(R.id.itemSessionFragment_estimatedTime);
        textViewHeading = view.findViewById(R.id.itemSessionFragment_heading);
    }
    private void initListeners(){
        log("...initListeners()");
        textViewEstimatedTime.setOnClickListener(view->goToCalendar());
    }
    public void setUserInterface(Item item){
        log("...setUserInterface(Item)");
        textViewHeading.setText(item.getHeading());

    }
}