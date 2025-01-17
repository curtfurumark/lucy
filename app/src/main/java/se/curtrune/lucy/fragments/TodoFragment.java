package se.curtrune.lucy.fragments;

import static se.curtrune.lucy.util.Logger.log;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import se.curtrune.lucy.R;
import se.curtrune.lucy.adapters.ItemAdapter;
import se.curtrune.lucy.app.Settings;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.State;
import se.curtrune.lucy.dialogs.AddItemDialog;
import se.curtrune.lucy.dialogs.PostponeDialog;
import se.curtrune.lucy.screens.main.LucindaViewModel;
import se.curtrune.lucy.viewmodel.TodoFragmentViewModel;
import se.curtrune.lucy.persist.ItemsWorker;
import se.curtrune.lucy.workers.NotificationsWorker;


public class TodoFragment extends Fragment implements
        ItemAdapter.Callback{

    private FloatingActionButton buttonAdd;
    private RecyclerView recycler;
    private ItemAdapter adapter;
    private Item currentParent;
    public static boolean VERBOSE = false;
    private LucindaViewModel mainViewModel;
    private TodoFragmentViewModel todoFragmentViewModel;
    private ItemTouchHelper itemTouchHelper;

    public TodoFragment() {
        if( VERBOSE) log("ToDoFragment()");
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
        initViewModels();
        initComponents(view);
        initSwipe();
        initRecycler();
        initListeners();
        return view;
    }
    private void addItemDialog(){
        if( VERBOSE) log("...addItemDialog()");
        AddItemDialog dialog = new AddItemDialog(ItemsWorker.getRootItem(Settings.Root.TODO, getContext()), false);
        dialog.setCallback(item -> {
            log("...onAddItem(Item item)");
            //log(item);
            todoFragmentViewModel.insert(item, getContext());
            if( item.hasNotification()){
                log("...item has notification, will set notification");
                NotificationsWorker.setNotification(item, getContext());
            }
        });
        dialog.show(getChildFragmentManager(), "add item");
    }

    private void initComponents(View view){
        if( VERBOSE) log("...initComponents()");
        recycler = view.findViewById(R.id.todoFragment_recycler);
        buttonAdd = view.findViewById(R.id.todoFragment_buttonAdd);
    }
    private void initListeners(){
        if( VERBOSE) log("...initListeners()");
        buttonAdd.setOnClickListener(view->addItemDialog());
        mainViewModel.getFilter().observe(requireActivity(), filter ->{
            log("TodoFragment.onFilter(String)", filter);
            todoFragmentViewModel.filter(filter);
        });
        todoFragmentViewModel.getItems().observe(requireActivity(), new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> items) {
                log("...onGetItems(List<Item>)");
                adapter.setList(items);
            }
        });
    }

    private void initRecycler(){
        if( VERBOSE) log("...initRecycler()");
        adapter = new ItemAdapter(todoFragmentViewModel.getItems().getValue(), this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(layoutManager);
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setAdapter(adapter);
        itemTouchHelper.attachToRecyclerView(recycler);
    }
    private void initSwipe(){
        log("...initSwipe()");
        itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                log("...onSwiped(...)");
                Item item = todoFragmentViewModel.getItem(viewHolder.getAdapterPosition());
                if (direction == ItemTouchHelper.LEFT) {
                    showDeleteDialog(item);
                } else if (direction == ItemTouchHelper.RIGHT) {
                    showPostponeDialog(item);
                }
            }
        });
    }
    private void initViewModels(){
        if( VERBOSE)  log("...initViewModels()");
        mainViewModel = new ViewModelProvider(requireActivity()).get(LucindaViewModel.class);
        todoFragmentViewModel = new ViewModelProvider(requireActivity()).get(TodoFragmentViewModel.class);
        todoFragmentViewModel.init(getContext());
    }

    @Override
    public void onItemClick(Item item) {
        log("...onItemClick(Item)", item.getHeading());
        if( item.hasChild()){
            Toast.makeText(getContext(), "WORKING ON IT", Toast.LENGTH_LONG).show();
/*            currentParent = item;
            items = ItemsWorker.selectChildren(currentParent, getContext());
            adapter.setList(items);*/
        }else{
            mainViewModel.updateFragment(new ItemSessionFragment(item));
        }
    }

    @Override
    public void onLongClick(Item item) {
        log("...onLongClick(Item)", item.getHeading());
/*        EditItemDialog dialog = new EditItemDialog(item);
        dialog.setCallback(updatedItem -> {
            log("...onUpdate(Item)");
            log(updatedItem);
            int rowsAffected = ItemsWorker.update(updatedItem, getContext());
            if (rowsAffected != 1) {
                log("ERROR updating item", updatedItem.getHeading());
            }
        });
        dialog.setMentalType(getChildFragmentManager(), "edit item");*/
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

        private void showDeleteDialog(Item item){
            log("...showDeleteDialog(Item)", item.getHeading());
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("delete" + item.getHeading());
            builder.setMessage("are you sure? ");
            builder.setPositiveButton("delete", (dialog, which) -> {
                log("...on positive button click");
                todoFragmentViewModel.delete(item, getContext());

            });
            builder.setNegativeButton("cancel", (dialog, which) -> {
                log("...on negative button click");
                adapter.notifyDataSetChanged();
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        private void showPostponeDialog(Item item){
            log("showPostponeDialog(Item)");
            PostponeDialog dialog = new PostponeDialog();
            dialog.setCallback(new PostponeDialog.Callback() {
                @Override
                public void postpone(PostponeDialog.Postpone postpone) {
                    log("PostponeDialog.postpone(Postpone)", postpone.toString());
                    todoFragmentViewModel.postpone(item, postpone, getContext());
                }
                @Override
                public void dismiss() {
                    log("...dismiss()");
                    adapter.notifyDataSetChanged();
                }
            });
            dialog.show(getChildFragmentManager(), "postpone");

        }
}