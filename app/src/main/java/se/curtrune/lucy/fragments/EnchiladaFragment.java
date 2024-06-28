package se.curtrune.lucy.fragments;

import static se.curtrune.lucy.util.Logger.log;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;

import java.util.List;
import java.util.stream.Collectors;

import se.curtrune.lucy.R;
import se.curtrune.lucy.activities.ItemSession;
import se.curtrune.lucy.adapters.ItemAdapter;
import se.curtrune.lucy.classes.CallingActivity;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.State;
import se.curtrune.lucy.dialogs.EditItemDialog;
import se.curtrune.lucy.util.Constants;
import se.curtrune.lucy.workers.ItemsWorker;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EnchiladaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EnchiladaFragment extends Fragment implements
        ItemAdapter.Callback,
        TabLayout.OnTabSelectedListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private RecyclerView recycler;
    private EditText editTextSearch;
    private ItemAdapter adapter;
    private List<Item> items;
    public static boolean VERBOSE = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        log("EnchiladaFragment.onCreateView(LayoutInflater, ViewGroup, Bundle)");
        View view = inflater.inflate(R.layout.todo_fragment, container, false);
        initComponents(view);
        items = ItemsWorker.selectItems(getContext());
        initRecycler(items);
        initListeners();
        return view;
    }

    private String mParam2;

    public EnchiladaFragment() {
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
    public static EnchiladaFragment newInstance(String param1, String param2) {
        return new EnchiladaFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            log("...getArguments != null");
        }
    }
    private void filter(String str){
        List<Item> filteredItems = items.stream().filter(item->item.contains(str)).collect(Collectors.toList());
        adapter.setList(filteredItems);
    }
    private void initComponents(View view){
        if( VERBOSE) log("...initComponents()");
        recycler = view.findViewById(R.id.todoFragment_recycler);
        editTextSearch = view.findViewById(R.id.todoFragment_search);
    }
    private void initListeners(){
        if( VERBOSE) log("...initListeners()");
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

    }

    private void initRecycler(List<Item> items){
        if( VERBOSE) log("...initRecycler(List<Item>)", items.size());
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
        Intent intent = new Intent(getContext(), ItemSession.class);
        //intent.putExtra(Constants.INTENT_ITEM_SESSION, true);
        intent.putExtra(Constants.INTENT_CALLING_ACTIVITY, CallingActivity.ENCHILADA_FRAGMENT);
        intent.putExtra(Constants.INTENT_SERIALIZED_ITEM, item);
        startActivity(intent);
    }

    /**
     * callback ItemAdapter
     * @param item, the long clicked item
     */

    @Override
    public void onLongClick(Item item) {
        log("...onLongClick(Item)", item.getHeading());
        EditItemDialog dialog = new EditItemDialog(item);
        dialog.setCallback(new EditItemDialog.Callback() {
            @Override
            public void onUpdate(Item item) {
                log("...onUpdate(Item)");
                update(item);
            }
        });
        dialog.show(getChildFragmentManager(), "edit item");
    }

    /**
     * callback for ItemAdapter
     * @param item, the clicked item, the item to be updated
     * @param checked, done or not done
     */
    @Override
    public void onCheckboxClicked(Item item, boolean checked) {
        log("...onCheckBoxClicked(Item, boolean)");
        item.setState(checked? State.DONE: State.TODO);
        update(item);

    }
    private void update(Item item){
        log("...update(Item)");
        int stat = ItemsWorker.update(item, getContext());
        if( stat != 1){
            log("ERROR updating item", item.getHeading());
            Toast.makeText(getContext(), "ERROR updating item", Toast.LENGTH_LONG).show();
        }else{
            log("...item updated ok");
        }
    }
}