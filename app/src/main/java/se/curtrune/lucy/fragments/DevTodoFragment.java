package se.curtrune.lucy.fragments;

import static se.curtrune.lucy.util.Logger.log;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

import se.curtrune.lucy.R;
import se.curtrune.lucy.adapters.DevTodoAdapter;
import se.curtrune.lucy.classes.DevTodo;
import se.curtrune.lucy.classes.Message;
import se.curtrune.lucy.dialogs.MessageDialog;
import se.curtrune.lucy.workers.DevWorker;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DevTodoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DevTodoFragment extends Fragment implements TabLayout.OnTabSelectedListener {
    private RecyclerView recycler;
    private DevTodoAdapter adapter;
    private FloatingActionButton addToDoButton;
    private TabLayout tabLayout;
    public DevTodoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment DevTodoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DevTodoFragment newInstance() {
        return  new DevTodoFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            log("...WARNING, getArguments != nulll");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        log("DevTodoFragment.onCreateView(...)");
        View view  = inflater.inflate(R.layout.dev_todo_fragment, container, false);
        initViews(view);
        initListeners();
        requestData();
        return view;
    }
    private void initListeners(){
        log("...initListeners()");
        tabLayout.addOnTabSelectedListener(this);
        addToDoButton.setOnClickListener(view->showDialog());
    }
    private void initViews(View view){
        recycler = view.findViewById(R.id.devTodoFragment_recycler);
        addToDoButton = view.findViewById(R.id.devTodoFragment_buttonAdd);
        tabLayout = view.findViewById(R.id.devTodoFragment_tabLayout);
    }
    private void requestData(){
        log("...requestData()");
        DevWorker.requestTodoList(new DevWorker.Callback() {
            @Override
            public void onRequest(List<DevTodo> todoList) {
                log("...onRequestComplete(List<DevTodo>)");
                adapter.setList(todoList);
            }
        });

    }
    private void showDialog(){
        log("...showDialog()");
        //Toast.makeText(getContext(), "hello", Toast.LENGTH_LONG).setMentalType();
        MessageDialog dialog = new MessageDialog();
        dialog.setCallback(new MessageDialog.Callback() {
            @Override
            public void onNewMessage(Message message) {
                log("...onNewMessage(Message)");
            }
        });
        dialog.show(getChildFragmentManager(), "add message");
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        log("...onTabSelected(TabLayout.Tab tab)", tab.getText().toString());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}