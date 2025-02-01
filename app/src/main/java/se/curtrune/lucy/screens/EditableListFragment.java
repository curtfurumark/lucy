package se.curtrune.lucy.screens;


import static se.curtrune.lucy.util.Logger.log;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import se.curtrune.lucy.R;
import se.curtrune.lucy.adapters.EditableListAdapter;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.viewmodel.EditableListViewModel;


public class EditableListFragment extends Fragment {
    private RecyclerView recyclerView;
    private TextView textViewHeading;
    private Button buttonSave;
    private EditableListAdapter adapter;
    private EditableListViewModel viewModel;

    private Item parent;
    public EditableListFragment() {
        // Required empty public constructor
    }
    public EditableListFragment(Item parent){
        log("EditableListFragment(Item parent)", parent.getHeading());
        assert  parent != null;
        this.parent = parent;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.editable_list_fragment, container, false);
        try {
            initViews(view);
            initViewModel();
            initRecycler();
            initListeners();
        }catch (Exception e){
            log("EXCEPTION", e.getMessage());
            e.printStackTrace();
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return view;
    }

    private void initListeners(){
        log("...initListeners()");
        buttonSave.setOnClickListener(view->saveList());
    }

    private void initRecycler(){
        log("...initRecycler()");
        adapter = new EditableListAdapter(viewModel.getItems().getValue(), new EditableListAdapter.Callback() {
            @Override
            public void onNewLine(String heading, int index) {
                log("...onNewLine(String, int)", heading);
                log("...add an empty item, after position, ", index);
                viewModel.setHeading(heading, index, getContext());
                viewModel.addEmptyItem(index + 1);
                adapter.notifyItemInserted(index + 1);
                adapter.setFocus(index + 1);
            }

            @Override
            public void onHeadingChanged(String heading, int position) {
                viewModel.setHeading(heading, position, getContext());
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }
    private void initViewModel(){
        log("...initViewModel()");
        viewModel = new ViewModelProvider(requireActivity()).get(EditableListViewModel.class);
        viewModel.init(parent);
    }
    private void initViews(View view){
        log("...initViews()");
        recyclerView = view.findViewById(R.id.mainActivity_recycler);
        textViewHeading = view.findViewById(R.id.editableListViewFragment_heading);
        textViewHeading.setText(parent.getHeading());
        buttonSave = view.findViewById(R.id.editableListViewFragment_buttonSave);
    }
    private void saveList(){
        log("...saveList()");
        viewModel.saveAll(getContext());
        getParentFragmentManager().popBackStackImmediate();
    }
}