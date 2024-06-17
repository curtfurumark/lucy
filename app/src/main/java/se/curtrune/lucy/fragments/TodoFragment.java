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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import se.curtrune.lucy.R;
import se.curtrune.lucy.activities.ItemSession;
import se.curtrune.lucy.adapters.ItemAdapter;
import se.curtrune.lucy.app.Settings;
import se.curtrune.lucy.classes.CallingActivity;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.State;
import se.curtrune.lucy.dialogs.AddItemDialog;
import se.curtrune.lucy.util.Constants;
import se.curtrune.lucy.workers.ItemsWorker;
import se.curtrune.lucy.workers.NotificationsWorker;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TodoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TodoFragment extends Fragment implements
        ItemAdapter.Callback,
        TabLayout.OnTabSelectedListener {

    private FloatingActionButton buttonAdd;
    private RecyclerView recycler;
    private EditText editTextSearch;
    private ItemAdapter adapter;
    private List<Item> items;
    private Item currentParent;
    public static boolean VERBOSE = false;

    public TodoFragment() {
        if( VERBOSE) log("ToDoFragment()");
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment ProjectsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TodoFragment newInstance() {
        return  new TodoFragment();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        log("ToDoFragment.onCreateView(LayoutInflater, ViewGroup, Bundle)");
        View view = inflater.inflate(R.layout.todo_fragment, container, false);
        requireActivity().setTitle("todo");
        initComponents(view);
        items = ItemsWorker.selectItems(State.TODO, getContext());
        items.sort(Comparator.comparingLong(Item::compare));
        initRecycler(items);
        initListeners();
        return view;
    }
    private void addItemDialog(){
        if( VERBOSE) log("...addItemDialog()");
        AddItemDialog dialog = new AddItemDialog(ItemsWorker.getRootItem(Settings.Root.TODO, getContext()));
        dialog.setCallback(new AddItemDialog.Callback() {
            @Override
            public void onAddItem(Item item) {
                log("...onAddItem(Item item)");
                log(item);
                item = ItemsWorker.insert(item, getContext());
                items.add(item);
                items.sort(Comparator.comparingLong(Item::compare));
                adapter.notifyDataSetChanged();
                if( item.hasNotification()){
                    log("...item has notification, will set notification");
                    NotificationsWorker.setNotification(item, getContext());
                }
            }
        });
        dialog.show(getChildFragmentManager(), "add item");
    }
    private void filter(String str){
        List<Item> filteredItems = items.stream().filter(item->item.contains(str)).collect(Collectors.toList());
        adapter.setList(filteredItems);
    }
    private void initComponents(View view){
        if( VERBOSE) log("...initComponents()");
        recycler = view.findViewById(R.id.todoFragment_recycler);
        editTextSearch = view.findViewById(R.id.todoFragment_search);
        buttonAdd = view.findViewById(R.id.todoFragment_buttonAdd);
    }
    private void initListeners(){
        if( VERBOSE) log("...initListeners()");
        buttonAdd.setOnClickListener(view->addItemDialog());
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
        if( item.hasChild()){
            currentParent = item;
            items = ItemsWorker.selectChildItems(currentParent, getContext());
            adapter.setList(items);
        }else{
            Intent intent = new Intent(getContext(), ItemSession.class);
            intent.putExtra(Constants.INTENT_CALLING_ACTIVITY, CallingActivity.TODO_FRAGMENT);
            //intent.putExtra(Constants.INTENT_ITEM_SESSION, true);
            intent.putExtra(Constants.INTENT_SERIALIZED_ITEM, item);
            startActivity(intent);
        }
    }

    @Override
    public void onLongClick(Item item) {

    }

    @Override
    public void onCheckboxClicked(Item item, boolean checked) {
        log("...onCheckboxClicked(Item, boolean) checked", checked);
        item.setState(checked? State.DONE:State.TODO);
        int rowsAffected = ItemsWorker.update(item, getContext());
        if( rowsAffected != 1){
            log("ERROR updating state of item");
            Toast.makeText(getContext(), "ERROR updating state of item", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getContext(), "item updated", Toast.LENGTH_LONG).show();
        }
    }
}