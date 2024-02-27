package se.curtrune.lucy.fragments;

import static se.curtrune.lucy.util.Logger.log;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.material.tabs.TabLayout;

import java.time.LocalDate;
import java.util.List;

import se.curtrune.lucy.R;
import se.curtrune.lucy.adapters.ItemAdapter;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.workers.ItemsWorker;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ItemsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ItemsFragment extends Fragment implements
        ItemAdapter.Callback,
        TabLayout.OnTabSelectedListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RecyclerView recycler;
    private EditText editTextSearch;
    private ItemAdapter adapter;
    private List<Item> items;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ItemsFragment() {
        log("ItemsFragment()");
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ItemsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ItemsFragment newInstance(String param1, String param2) {
        ItemsFragment fragment = new ItemsFragment();
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
        View view = inflater.inflate(R.layout.items_fragment, container, false);
        initComponents(view);
        items = ItemsWorker.selectTodayList(LocalDate.now(), getContext());
        initRecycler(items);
        return view;
    }
    private void initComponents(View view){
        log("...initComponents()");
        recycler = view.findViewById(R.id.itemsFragment_recycler);
        editTextSearch = view.findViewById(R.id.itemsFragment_search);

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
    }

    @Override
    public void onLongClick(Item item) {

    }

    @Override
    public void onCheckboxClicked(Item item, boolean checked) {

    }
}