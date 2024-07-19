package se.curtrune.lucy.fragments;

import static se.curtrune.lucy.util.Logger.log;

import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import se.curtrune.lucy.R;
import se.curtrune.lucy.adapters.ItemAdapter;
import se.curtrune.lucy.app.Lucinda;
import se.curtrune.lucy.app.Settings;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.State;
import se.curtrune.lucy.dialogs.AddItemDialog;
import se.curtrune.lucy.util.Constants;
import se.curtrune.lucy.viewmodel.LucindaViewModel;
import se.curtrune.lucy.workers.ItemsWorker;
import se.curtrune.lucy.workers.MentalWorker;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProjectsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProjectsFragment extends Fragment implements
        ItemAdapter.Callback,
        TabLayout.OnTabSelectedListener {

    private static final String CURRENT_PARENT = "CURRENT_PARENT";
    private RecyclerView recycler;
    private TabLayout tabLayout;
    private FloatingActionButton buttonAddItem;
    private ItemAdapter adapter;
    private Item currentParent;
    private List<Item> items;
    private LucindaViewModel viewModel;
    public static boolean VERBOSE = false;

    public ProjectsFragment() {
        if( VERBOSE) log("ProjectsFragment()");
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param parent, show children to this item
     * @return A new instance of fragment ProjectsFragment.
     */
    public static ProjectsFragment newInstance(Item parent) {
        log("ProjectsFragment.newInstance(Item parent) ", parent.getHeading());
        ProjectsFragment fragment = new ProjectsFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constants.INTENT_SERIALIZED_ITEM, parent);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        log("ProjectsFragment.onCreate(Bundle of joy))");
        if (getArguments() != null) {
            //TODO, solve this another way
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                currentParent = getArguments().getSerializable(Constants.INTENT_SERIALIZED_ITEM, Item.class);
            }
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
        initViewModel();
        initSwipe();
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
        initTabs();
        return view;
    }
    private void addTab(Item item){
        if( VERBOSE) log("...addTab(Item)", item.getHeading());
        TabLayout.Tab tab = tabLayout.newTab();
        tab.setText(item.getHeading());
        tab.setTag(item);
        tabLayout.addTab(tab, true);
    }
    private void deleteItem(Item item){
        if( VERBOSE) log("...deleteItem(Item)", item.getHeading());
        int rowsAffected = MentalWorker.deleteMental(item, getContext());
        if( rowsAffected != 1){
            log("WARNING mental not deleted, possibly no mental to delete...");
        }else{
            log("...mental deleted from db");
        }
        boolean deleted = ItemsWorker.delete(item, getContext());
        if( !deleted){
            log("...error deleting item");
            Toast.makeText(getContext(), "error deleting item", Toast.LENGTH_LONG).show();
        }else {
            log("...item deleted from DB");
            items.remove(item);
            adapter.notifyDataSetChanged();
        }
    }
    private void descend(Item item){
        if( VERBOSE) log("...descend(Item)", item.getHeading());
        items = ItemsWorker.selectChildren(item, getContext());
        Lucinda.currentParent = item;
        currentParent = item;
        adapter.setList(items);
        addTab(currentParent);

    }
    private void initComponents(View view){
        if(VERBOSE)log("...initComponents()");
        recycler = view.findViewById(R.id.projectsFragment_recycler);
        //don't delete this, might return
        //editTextSearch = view.findViewById(R.id.projectsFragment_search);
        buttonAddItem = view.findViewById(R.id.projectsFragment_addItem);
        tabLayout = view.findViewById(R.id.projectsFragment_tabLayout);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

    }

    private void initRecycler(List<Item> items){
        if( VERBOSE) log("...initRecycler(List<Item>)", items.size());
        adapter = new ItemAdapter(this.items, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(layoutManager);
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setAdapter(adapter);
    }
    private void  initListeners(){
        if(VERBOSE) log("...initListeners()");
        buttonAddItem.setOnClickListener(view->showAddItemDialog());
        tabLayout.addOnTabSelectedListener(this);
    }
    private void initSwipe(){
        if( VERBOSE) log("...initSwipe()");
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT ) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Item item = items.get(viewHolder.getAdapterPosition());
                if( direction == ItemTouchHelper.LEFT){
                    showDeleteDialog(item);
                }
            }
        });
        itemTouchHelper.attachToRecyclerView(recycler);
    }
    private void initTabs(){
        log("...initTabs()");
        addTab(currentParent);

    }
    private void initViewModel(){
        if( VERBOSE) log("...initViewModel()");
        viewModel = new ViewModelProvider(requireActivity()).get(LucindaViewModel.class);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if( VERBOSE) log("...onOptionsItemSelected(MenuItem) ", Objects.requireNonNull(item.getTitle()).toString());
        if( item.getItemId() == R.id.mainActivity_startSequence){
            startSequence();
        }
        return true;
    }

    @Override
    public void onPause() {
        log("ProjectsFragment.onPause()");
        super.onPause();
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
        log("...tab", Objects.requireNonNull(tab.getText()).toString());
        removeTabs(tab.getPosition());
        showChildren(tab);
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
            descend(item);
        }else {
            viewModel.updateFragment(new ItemSessionFragment(item));
        }
    }

    @Override
    public void onLongClick(Item item) {
        log("...onLongClick(Item item)");
    }

    @Override
    public void onCheckboxClicked(Item item, boolean checked) {
        if( VERBOSE) log("...onCheckboxClicked(Item, boolean)", checked);
        item.setState(checked ? State.DONE: State.TODO);
        int rowsAffected = ItemsWorker.update(item, getContext());
        if( rowsAffected != 1){
            log("ERROR updating state of item", item.getHeading());
        }
        items = ItemsWorker.selectChildren(currentParent, getContext());
        items.sort(Comparator.comparingLong(Item::compare));
        adapter.notifyDataSetChanged();

    }
    private void removeTabs(int position){
        log("...removeTabs(int)", position);
        for(int i = position + 1; i < tabLayout.getTabCount(); i++){
            tabLayout.removeTabAt(i);
        }
    }
    private void showAddItemDialog(){
        if(VERBOSE) log("...showAddItemDialog()");
        AddItemDialog dialog = new AddItemDialog(currentParent, false);
        dialog.setCallback(item -> {
            log("AddItemDialog.onAddItem(Item)", item.getHeading());
            item = ItemsWorker.insert(item, getContext());
            if(VERBOSE) log(item);
            items.add(item);
            items.sort(Comparator.comparingLong(Item::compare));
            adapter.notifyDataSetChanged();
        });
        dialog.show(getChildFragmentManager(), "add item");
    }
    private void showChildren(TabLayout.Tab tab){
        log("...showChildren(Tab)", Objects.requireNonNull(tab.getText()).toString());
        Item parent = (Item) tab.getTag();
        items = ItemsWorker.selectChildren(parent, getContext());
        adapter.setList(items);
    }
    private void showDeleteDialog(Item item){
        assert  item != null;
        log("...showDeleteDialog(Item)", item.getHeading());
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        String stringDialogTitle = String.format(Locale.getDefault(), "%s %s?", getString(R.string.delete), item.getHeading());
        builder.setTitle(stringDialogTitle);
        builder.setMessage(R.string.are_you_sure);
        builder.setPositiveButton(getString(R.string.delete), (dialog, which) -> {
            log("...on positive button click");
            deleteItem(item);

        });
        builder.setNegativeButton(getString(R.string.dismiss), (dialog, which) -> {
            log("...on negative button click");
            adapter.notifyDataSetChanged();
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }
    private void startSequence(){
        log("...startSequence()");
        if( currentParent == null){
            log("WARNING, current parent is null, surrender");
            Toast.makeText(getContext(), "current parent is null", Toast.LENGTH_LONG).show();
            return;
        }
        viewModel.updateFragment(new SequenceFragment(currentParent));
    }
}