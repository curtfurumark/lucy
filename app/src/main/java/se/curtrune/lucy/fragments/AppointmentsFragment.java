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
import se.curtrune.lucy.adapters.AppointmentAdapter;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.State;
import se.curtrune.lucy.dialogs.AppointmentDialog;
import se.curtrune.lucy.dialogs.PostponeDialog;
import se.curtrune.lucy.viewmodel.AppointmentsViewModel;
import se.curtrune.lucy.viewmodel.LucindaViewModel;
import se.curtrune.lucy.workers.ItemsWorker;

public class AppointmentsFragment extends Fragment implements
        AppointmentAdapter.Callback{
    private RecyclerView recycler;
    public static boolean VERBOSE = false;
    private AppointmentAdapter adapter;
    private FloatingActionButton buttonAdd;
    private LucindaViewModel mainViewModel;
    private AppointmentsViewModel appointmentsViewModel;
    private ItemTouchHelper itemTouchHelper;

    public AppointmentsFragment() {
        if( VERBOSE) log("AppointmentsFragment()");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        log("AppointmentsFragment.onCreateView(LayoutInflater, ViewGroup, Bundle)");
        View view = inflater.inflate(R.layout.appintments_fragment, container, false);
        initViewModel();
        initComponents(view);
        requireActivity().setTitle(getString(R.string.appointments));
        initSwipe();
        initRecycler();
        initListeners();

        return view;
    }
    private void addAppointment(){
        if( VERBOSE)log("...addAppointment()");
        AppointmentDialog dialog = new AppointmentDialog();
        dialog.setCallback(item -> {
            log("...onNewAppointment(Item item");
            appointmentsViewModel.add(item, getContext());

        });
        dialog.show(getParentFragmentManager(), "add appointment");

    }

    private void initComponents(View view){
        if( VERBOSE) log("...initComponents()");
        recycler = view.findViewById(R.id.appointmentsFragment_recycler);
        buttonAdd = view.findViewById(R.id.appointmentsFragment_addButton);

    }
    private void initListeners(){
        if( VERBOSE) log("...initListeners()");
        buttonAdd.setOnClickListener(view->addAppointment());
        mainViewModel.getFilter().observe(requireActivity(), filter -> {
            log("...onFilter(String)", filter);
            appointmentsViewModel.filter(filter);
        });
        appointmentsViewModel.getEvents().observe(requireActivity(), items -> {
            log("...getEvents(List<Item>)");
            adapter.setList(items);
        });
    }

    private void initRecycler(){
        if( VERBOSE) log("...initRecycler()");
        adapter = new AppointmentAdapter(appointmentsViewModel.getEvents().getValue(), this);
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
                Item item = appointmentsViewModel.getItem(viewHolder.getAdapterPosition());
                if (direction == ItemTouchHelper.LEFT) {
                    showDeleteDialog(item);
                } else if (direction == ItemTouchHelper.RIGHT) {
                    showPostponeDialog(item);
                }
            }
        });
    }
    private void initViewModel(){
        if( VERBOSE)  log("...initViewModel()");
        mainViewModel = new ViewModelProvider(requireActivity()).get(LucindaViewModel.class);
        appointmentsViewModel = new ViewModelProvider(requireActivity()).get(AppointmentsViewModel.class);
        appointmentsViewModel.init(getContext());
    }

    @Override
    public void onEditTime(Item item) {
        log("...onEditTime(Item item)");
    }

    @Override
    public void onItemClick(Item item) {
        if( VERBOSE) log("...onItemClick(Item)", item.getHeading());
        mainViewModel.updateFragment(new ItemSessionFragment(item));
    }

    @Override
    public void onLongClick(Item item) {
        log("...onLongClick(Item)");
    }


    @Override
    public void onCheckboxClicked(Item item, boolean checked) {
        log("...onCheckBoxClicked(Item, boolean)", checked);
        item.setState(checked? State.DONE: State.TODO);
        appointmentsViewModel.update(item, getContext());
    }
    private void showDeleteDialog(Item item){
        log("...showDeleteDialog(Item)", item.getHeading());
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("delete" + item.getHeading());
        builder.setMessage("are you sure? ");
        builder.setPositiveButton("delete", (dialog, which) -> {
            log("...on positive button click");
            appointmentsViewModel.delete(item, getContext());

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
                appointmentsViewModel.postpone(item, postpone, getContext());
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