package se.curtrune.lucy.fragments;

import static se.curtrune.lucy.util.Logger.log;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import se.curtrune.lucy.R;
import se.curtrune.lucy.activities.ItemSession;
import se.curtrune.lucy.adapters.ItemAdapter;
import se.curtrune.lucy.app.Lucinda;
import se.curtrune.lucy.classes.CallingActivity;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.dialogs.AddAppointmentDialog;
import se.curtrune.lucy.dialogs.EditItemDialog;
import se.curtrune.lucy.util.Constants;
import se.curtrune.lucy.viewmodel.LucindaViewModel;
import se.curtrune.lucy.workers.ItemsWorker;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AppointmentsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AppointmentsFragment extends Fragment implements
        ItemAdapter.Callback{
    private RecyclerView recycler;
    public static boolean VERBOSE = false;
    private ItemAdapter adapter;
    private List<Item> items;
    private FloatingActionButton buttonAdd;
    private LucindaViewModel viewModel;

    public AppointmentsFragment() {

        if( VERBOSE) log("AppointmentsFragment()");
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     */
    // TODO: Rename and change types and number of parameters
    public static AppointmentsFragment newInstance() {
        return new AppointmentsFragment();
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
        log("AppointmentsFragment.onCreateView(LayoutInflater, ViewGroup, Bundle)");
        View view = inflater.inflate(R.layout.appintments_fragment, container, false);
        initComponents(view);
        requireActivity().setTitle(getString(R.string.appointments));
        items = ItemsWorker.selectAppointments(getContext());
        initRecycler(items);
        initListeners();
        initViewModel();
        return view;
    }
    private void addAppointment(){
        if( VERBOSE)log("...addAppointment()");
        AddAppointmentDialog dialog = new AddAppointmentDialog();
        dialog.setCallback(item -> {
            log("...onNewAppointment(Item item");
            item = ItemsWorker.insert(item, getContext());
            log(item);
            items.add(item);
            adapter.notifyDataSetChanged();

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
    }

    private void initRecycler(List<Item> items){
        if( VERBOSE) log("...initRecycler(List<Item>)", items.size());
        adapter = new ItemAdapter(this.items, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(layoutManager);
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setAdapter(adapter);
    }
    private void initViewModel(){
        if( VERBOSE)  log("...initViewModel()");
        viewModel = new ViewModelProvider(requireActivity()).get(LucindaViewModel.class);
    }

    @Override
    public void onItemClick(Item item) {
        if( VERBOSE) log("...onItemClick(Item)", item.getHeading());
        Lucinda.currentFragment = this;
        Lucinda.Dev = true;
        if( Lucinda.Dev ){
            viewModel.updateFragment(new ItemSessionFragment(item));
        }else {
            Intent intent = new Intent(getContext(), ItemSession.class);
            intent.putExtra(Constants.INTENT_SERIALIZED_ITEM, item);
            intent.putExtra(Constants.INTENT_CALLING_ACTIVITY, CallingActivity.APPOINTMENTS_FRAGMENT);
            startActivity(intent);
        }
    }

    @Override
    public void onLongClick(Item item) {
        assert item != null;
        log("...onLongClick(ItemI)", item.getHeading());
        EditItemDialog dialog = new EditItemDialog(item);
        dialog.setCallback(item1 -> {
            log("...onUpdate(Item)");
            log(item1);
        });
        dialog.show(getChildFragmentManager(), "edit item");
    }

    @Override
    public void onCheckboxClicked(Item item, boolean checked) {

    }

}