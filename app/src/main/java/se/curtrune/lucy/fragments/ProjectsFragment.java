package se.curtrune.lucy.fragments;

import static se.curtrune.lucy.util.Logger.log;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.Comparator;
import java.util.List;

import se.curtrune.lucy.R;
import se.curtrune.lucy.activities.ItemSession;
import se.curtrune.lucy.activities.SequenceActivity;
import se.curtrune.lucy.adapters.ItemAdapter;
import se.curtrune.lucy.app.Lucinda;
import se.curtrune.lucy.app.Settings;
import se.curtrune.lucy.classes.CallingActivity;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.dialogs.AddTemplateDialog;
import se.curtrune.lucy.dialogs.OnNewItemCallback;
import se.curtrune.lucy.util.Constants;
import se.curtrune.lucy.workers.ItemsWorker;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProjectsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProjectsFragment extends Fragment implements
        ItemAdapter.Callback,
        TabLayout.OnTabSelectedListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String CURRENT_PARENT = "CURRENT_PARENT";
    private RecyclerView recycler;
    private EditText editTextSearch;
    private FloatingActionButton buttonAddItem;
    private ItemAdapter adapter;
    private Item currentParent;
    private List<Item> items;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProjectsFragment() {
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
    public static ProjectsFragment newInstance(String param1, String param2) {
        ProjectsFragment fragment = new ProjectsFragment();
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
        log("ProjectsFragment.onCreateView(LayoutInflater, ViewGroup, Bundle)");
        View view = inflater.inflate(R.layout.projects_fragment, container, false);
        setHasOptionsMenu(true);
        initComponents(view);
        initListeners();
        if( savedInstanceState != null){
            log("...savedInstanceState != null");
            currentParent = (Item) savedInstanceState.getSerializable(CURRENT_PARENT);
        }else{
            currentParent = ItemsWorker.getRootItem(Settings.Root.PROJECTS, getContext());
        }
        if( currentParent == null){
            log("ERROR, currentParent is null");
            Toast.makeText(getContext(), "ERROR, currentParent is null", Toast.LENGTH_LONG).show();
            return view;
        }
        items = ItemsWorker.selectChildren(currentParent, getContext());
        initRecycler(items);
        log("...recycler initialized");
        return view;
    }
    private void initComponents(View view){
        log("...initComponents()");
        recycler = view.findViewById(R.id.projectsFragment_recycler);
        editTextSearch = view.findViewById(R.id.projectsFragment_search);
        buttonAddItem = view.findViewById(R.id.projectsFragment_addItem);

    }

    private void initRecycler(List<Item> items){
        log("...initRecycler(List<Item>)", items.size());
        adapter = new ItemAdapter(this.items, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(layoutManager);
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setAdapter(adapter);
    }
    private void  initListeners(){
        buttonAddItem.setOnClickListener(view->showAddItemDialog());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        log("...onOptionsItemSelected(MenuItem) ", item.getTitle().toString());
        if( item.getItemId() == R.id.mainActivity_startSequence){
            startSequence();
        }
        return true;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        log("ProjectsFragment.onSaveInstanceState(Bundle of joy) ");
        outState.putSerializable(CURRENT_PARENT, currentParent);
        super.onSaveInstanceState(outState);
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
        if(item.hasChild() && !item.isTemplate()){
            //setTitle(item.getHeading());
            items = ItemsWorker.selectChildItems(item, getContext());
            Lucinda.currentParent = item;
            currentParent = item;
            adapter.setList(items);
        }else {
            Lucinda.currentFragment = this;
            Intent intent = new Intent(getContext(), ItemSession.class);
            intent.putExtra(Constants.INTENT_ITEM_SESSION, true);
            intent.putExtra(Constants.INTENT_CALLING_ACTIVITY, CallingActivity.TODAY_ACTIVITY);
            intent.putExtra(Constants.INTENT_SERIALIZED_ITEM, item);
            //Lucinda.currentViewMode = mode;
            startActivity(intent);
        }
    }

    @Override
    public void onLongClick(Item item) {
        log("...onLongClick(Item item)");
    }

    @Override
    public void onCheckboxClicked(Item item, boolean checked) {
        log("...onCheckboxClicked(Item, boolean)");
    }
    private void showAddItemDialog(){
        log("...showAddItemDialog()");
        AddTemplateDialog dialog = new AddTemplateDialog(currentParent);
        dialog.setCallback(new OnNewItemCallback() {
            @Override
            public void onNewItem(Item item) {
                log("...onNewItem(Item)");
                item = ItemsWorker.insert(item, getContext());
                items.add(item);
                items.sort(Comparator.comparingLong(Item::compare));
                adapter.notifyDataSetChanged();
            }
        });
        dialog.show(getParentFragmentManager(), "add item");
    }
    private void startSequence(){
        log("...startSequence()");
        if( currentParent == null){
            log("WARNING, current parent is null, surrender");
            Toast.makeText(getContext(), "current parent is null", Toast.LENGTH_LONG).show();
            return;
        }
        Toast.makeText(getContext(), "start sequence", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getContext(), SequenceActivity.class);
        intent.putExtra(Constants.INTENT_SEQUENCE_PARENT, currentParent);
        startActivity(intent);
    }
}