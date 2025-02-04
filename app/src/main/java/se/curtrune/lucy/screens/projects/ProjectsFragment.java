package se.curtrune.lucy.screens.projects;

import static se.curtrune.lucy.util.Logger.log;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

import se.curtrune.lucy.R;
import se.curtrune.lucy.adapters.ItemAdapter;
import se.curtrune.lucy.app.Settings;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.State;
import se.curtrune.lucy.dialogs.AddItemDialog;
import se.curtrune.lucy.screens.item_editor.ItemEditorFragment;
import se.curtrune.lucy.fragments.SequenceFragment;
import se.curtrune.lucy.screens.main.MainViewModel;
import se.curtrune.lucy.persist.ItemsWorker;

public class ProjectsFragment extends Fragment implements
        ItemAdapter.Callback,
        TabLayout.OnTabSelectedListener {

    private RecyclerView recycler;
    private TabLayout tabLayout;
    private FloatingActionButton buttonAddItem;
    private ItemAdapter adapter;
    private ProjectsViewModel projectsViewModel;
    private Item currentParent;
    private List<Item> items = new ArrayList<>();
    private MainViewModel mainViewModel;
    public static boolean VERBOSE = false;

    public ProjectsFragment() {
        if( VERBOSE) log("ProjectsFragment()");
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        log("ProjectsFragment.onCreate(Bundle of joy))");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        log("ProjectsFragment.onCreateView(LayoutInflater, ViewGroup, Bundle)");
        View view = inflater.inflate(R.layout.projects_fragment, container, false);
        initComponents(view);
        initRecycler(items);
        initSwipe();
        initViewModel();
        initListeners();
        return view;
    }
    private void addTab(Item item){
        log("...addTab(Item)", item.getHeading());
        TabLayout.Tab tab = tabLayout.newTab();
        tab.setText(item.getHeading());
        tab.setTag(item);
        tabLayout.addTab(tab, true);
    }
    private void deleteItem(Item item){
        log("...deleteItem(Item)", item.getHeading());
/*        int rowsAffected = MentalWorker.deleteMental(item, getContext());
        if( rowsAffected != 1){
            log("WARNING mental not deleted, possibly no mental to delete...");
        }else{
            log("...mental deleted from db");
        }*/
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
        log("...descend(Item)", item.getHeading());
        items = ItemsWorker.selectChildren(item, getContext());
        currentParent = item;
        projectsViewModel.push(item);
        adapter.setList(items);
        addTab(currentParent);
    }
    private void filter(String filter){
        log("...filter(String)", filter);
        List<Item> filteredItems = items.stream().filter(item->item.contains(filter)).collect(Collectors.toList());
        adapter.setList(filteredItems);
    }
    private void initComponents(View view){
        if(VERBOSE)log("...initComponents()");
        recycler = view.findViewById(R.id.projectsFragment_recycler);
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
        if( projectsViewModel.getCurrentParent() != null){
            log("\t\tgot current parent", projectsViewModel.getCurrentParent().getHeading() );
            setTabs(projectsViewModel.getStack());
            currentParent = projectsViewModel.getCurrentParent();
        }else{
            log("\t\tempty stack, setting root/currentParent");
            currentParent = ItemsWorker.getRootItem(Settings.Root.PROJECTS, getContext());
            projectsViewModel.setRoot(currentParent);
            addTab(currentParent);
        }
        mainViewModel.getFilter().observe(requireActivity(), filter -> {
            log("ProjectsFragment.getFilter(String))", filter);
            filter(filter);
            //projectsViewModel.filter(filter);
        });
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
    private void initViewModel(){
        log("...initViewModel()");
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        projectsViewModel = new ViewModelProvider(requireActivity()).get(ProjectsViewModel.class);

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        log("...onTabSelected(TabLayout.Tag)");
        currentParent = (Item) tab.getTag();
        log("\t\ttab", Objects.requireNonNull(tab.getText()).toString());
        if( tab.getPosition() + 1 < tabLayout.getTabCount()){
            removeTabs(tab.getPosition());
        }
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
        if(item.hasChild() ){
            descend(item);
        }else {
            log("\t\titem does not have  child -> ItemEditorFragment");
            log("\t\tcurrent parent is", currentParent.getHeading());
            projectsViewModel.setCurrentParent(currentParent);
            mainViewModel.updateFragment(new ItemEditorFragment(item));
        }
    }

    @Override
    public void onLongClick(Item item) {
        log("...onLongClick(Item item)", item.getHeading());
        projectsViewModel.setCurrentParent(currentParent);
        mainViewModel.updateFragment( new ItemEditorFragment(item));
    }

    @Override
    public void onCheckboxClicked(Item item, boolean checked) {
        log("...onCheckboxClicked(Item, boolean)", checked);
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
        int tabCount = tabLayout.getTabCount();
        log("...tabCount", tabCount);
        for(int i = tabCount -1 ; i > position; i--){
            tabLayout.removeTabAt(i);
            projectsViewModel.pop();
        }
    }
    private void showAddItemDialog(){
        if(VERBOSE) log("...showAddItemDialog()");
        AddItemDialog dialog = new AddItemDialog(currentParent, false);
        dialog.setCallback(item -> {
            log("...AddItemDialog.onAddItem(Item)", item.getHeading());
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
            log("\t\ton positive button click");
            deleteItem(item);

        });
        builder.setNegativeButton(getString(R.string.dismiss), (dialog, which) -> {
            log("\t\ton negative button click");
            adapter.notifyDataSetChanged();
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }
    private void setTabs(List<Item> itemStack){
        log("...setTabs(List<Item>)");
        for(Item item: itemStack){
            addTab(item);
        }
    }
    private void startSequence(){
        log("...startSequence() currentParent", currentParent.getHeading());
        if( currentParent == null){
            log("WARNING, current parent is null, surrender");
            Toast.makeText(getContext(), "current parent is null", Toast.LENGTH_LONG).show();
            return;
        }
        mainViewModel.updateFragment(new SequenceFragment(currentParent));
    }
}