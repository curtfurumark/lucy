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

import java.util.List;

import se.curtrune.lucy.R;
import se.curtrune.lucy.adapters.ItemAdapter;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.State;
import se.curtrune.lucy.screens.item_editor.ItemEditorFragment;
import se.curtrune.lucy.viewmodel.EnchiladaViewModel;
import se.curtrune.lucy.screens.main.LucindaViewModel;
import se.curtrune.lucy.persist.ItemsWorker;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EnchiladaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EnchiladaFragment extends Fragment implements
        ItemAdapter.Callback{

    private RecyclerView recycler;
    //private EditText editTextSearch;
    private ItemAdapter adapter;
    public static boolean VERBOSE = false;
    private LucindaViewModel viewModel;
    private EnchiladaViewModel enchiladaViewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        log("EnchiladaFragment.onCreateView(LayoutInflater, ViewGroup, Bundle)");
        View view = inflater.inflate(R.layout.enchilada_fragment, container, false);
        initComponents(view);
        initViewModel();
        initRecycler();
        initSwipe();
        initListeners();
        observe();
        return view;
    }

    public EnchiladaFragment() {
        log("ProjectsFragment()");
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ProjectsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EnchiladaFragment newInstance() {
        return new EnchiladaFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            log("...getArguments != null");
        }
    }

    private void initComponents(View view){
        if( VERBOSE) log("...initComponents()");
        recycler = view.findViewById(R.id.enchiladaFragment_recycler);
    }
    private void initListeners(){
        if( VERBOSE) log("...initListeners()");
    }

    private void initRecycler(){
        if( VERBOSE) log("...initRecycler()");
        adapter = new ItemAdapter(enchiladaViewModel.getItems().getValue(), this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(layoutManager);
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setAdapter(adapter);
    }
    private void initSwipe() {
        if( VERBOSE) log("...initSwipe()");
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Item item = enchiladaViewModel.getItem(viewHolder.getAdapterPosition());
                if (item.isPrioritized()) {
                    log("...item isPrioritized");
                    Toast.makeText(getContext(), "don't delete prioritized items", Toast.LENGTH_LONG).show();
                    return;
                }
                if (direction == ItemTouchHelper.LEFT) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle(getString(R.string.delete) + ":  " + item.getHeading());
                    builder.setMessage("are you sure? ");
                    builder.setPositiveButton(getString(R.string.delete), (dialog, which) -> {
                        log("...on positive button click");
                        boolean res = enchiladaViewModel.delete(item, getContext());
                        boolean deleted = ItemsWorker.delete(item, getContext());
                        if (!res) {
                            log("...error deleting item");
                            Toast.makeText(getContext(), "error deleting item", Toast.LENGTH_LONG).show();
                        } else {
                            log("...item deleted");
                            //items.remove(item);
                            //enchiladaViewModel.remove()
                            adapter.notifyDataSetChanged();
                        }
                    });
                    builder.setNegativeButton("cancel", (dialog, which) -> {
                        log("...on negative button click->do nothing");
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();

                }
            }
        });
        itemTouchHelper.attachToRecyclerView(recycler);
    }
    private void initViewModel(){
        log("...initViewModel()");
        enchiladaViewModel = new ViewModelProvider(requireActivity()).get(EnchiladaViewModel.class);
        enchiladaViewModel.init(getContext());
        viewModel = new ViewModelProvider(requireActivity()).get(LucindaViewModel.class);
        viewModel.getFilter().observe(requireActivity(), new Observer<String>() {
            @Override
            public void onChanged(String filter) {
                enchiladaViewModel.filter(filter);
            }
        });

    }

    private void observe(){
        log("...observe()");
        enchiladaViewModel.getItems().observe(requireActivity(), new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> items) {
                adapter.setList(items);
            }
        });
    }

    @Override
    public void onItemClick(Item item) {
        log("...onItemClick(Item)", item.getHeading());
        viewModel.updateFragment( new ItemEditorFragment(item));
    }

    /**
     * callback ItemAdapter
     * @param item, the long clicked item
     */

    @Override
    public void onLongClick(Item item) {
        log("...onLongClick(Item)", item.getHeading());

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