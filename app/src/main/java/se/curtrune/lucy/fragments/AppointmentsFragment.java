package se.curtrune.lucy.fragments;

import static se.curtrune.lucy.util.Logger.log;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.List;
import java.util.stream.Collectors;

import se.curtrune.lucy.R;
import se.curtrune.lucy.activities.ItemSession;
import se.curtrune.lucy.adapters.ItemAdapter;
import se.curtrune.lucy.app.Lucinda;
import se.curtrune.lucy.classes.CallingActivity;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.State;
import se.curtrune.lucy.dialogs.AddAppointmentDialog;
import se.curtrune.lucy.util.Constants;
import se.curtrune.lucy.workers.ItemsWorker;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AppointmentsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AppointmentsFragment extends Fragment implements
        ItemAdapter.Callback,
        TabLayout.OnTabSelectedListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RecyclerView recycler;
    //private EditText editTextSearch;
    private ItemAdapter adapter;
    private List<Item> items;
    private FloatingActionButton buttonAdd;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AppointmentsFragment() {
        log("ProjectsFragment()");
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProjectsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AppointmentsFragment newInstance(String param1, String param2) {
        AppointmentsFragment fragment = new AppointmentsFragment();
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
        log("AppointmentsFragment.onCreateView(LayoutInflater, ViewGroup, Bundle)");
        View view = inflater.inflate(R.layout.appintments_fragment, container, false);
        initComponents(view);
        getActivity().setTitle("appointments");
        items = ItemsWorker.selectAppointments(getContext());
        initRecycler(items);
        log("...recycler initialized");
        initListeners();
        return view;
    }
    private void addAppointment(){
        log("...addAppointment()");
        AddAppointmentDialog dialog = new AddAppointmentDialog();
        dialog.setCallback(new AddAppointmentDialog.OnNewAppointmentCallback() {
            @Override
            public void onNewAppointment(Item item) {
                log("...onNewAppointment(Item item");
                item = ItemsWorker.insert(item, getContext());
                log(item);
                items.add(item);
                adapter.notifyDataSetChanged();

            }
        });
        dialog.show(getParentFragmentManager(), "add appointment");

    }



    private void filter(String str){
        List<Item> filteredItems = items.stream().filter(item->item.contains(str)).collect(Collectors.toList());
        adapter.setList(filteredItems);
    }
    private void initComponents(View view){
        log("...initComponents()");
        recycler = view.findViewById(R.id.appointmentsFragment_recycler);
        buttonAdd = view.findViewById(R.id.appointmentsFragment_addButton);

    }
    private void initListeners(){
        log("...initListeners()");
        buttonAdd.setOnClickListener(view->addAppointment());
/*
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                log("...onTextChanged(CharSequence, int, int, int)");
                filter(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
*/

    }

    private void initRecycler(List<Item> items){
        log("...initRecycler(List<Item>)", items.size());
        adapter = new ItemAdapter(this.items, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(layoutManager);
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setAdapter(adapter);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        log("...onTabSelected()");
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onItemClick(Item item) {
        log("...onItemClick(Item)", item.getHeading());
        Lucinda.currentFragment = this;
        Intent intent = new Intent(getContext(), ItemSession.class);
        intent.putExtra(Constants.INTENT_ITEM_SESSION, true);
        intent.putExtra(Constants.INTENT_SERIALIZED_ITEM, item);
        intent.putExtra(Constants.INTENT_CALLING_ACTIVITY, CallingActivity.APPOINTMENTS_FRAGMENT);
        startActivity(intent);
    }

    @Override
    public void onLongClick(Item item) {

    }

    @Override
    public void onCheckboxClicked(Item item, boolean checked) {

    }

}