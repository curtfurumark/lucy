package se.curtrune.lucy.fragments;

import static se.curtrune.lucy.util.Logger.log;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import se.curtrune.lucy.R;
import se.curtrune.lucy.adapters.MessageAdapter;
import se.curtrune.lucy.classes.Message;
import se.curtrune.lucy.dialogs.MessageDialog;
import se.curtrune.lucy.viewmodel.MessageBoardViewModel;
import se.curtrune.lucy.workers.InternetWorker;
import se.curtrune.lucy.workers.MessageWorker;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MessageBoardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MessageBoardFragment extends Fragment implements TabLayout.OnTabSelectedListener {



    // TODO: Rename and change types of parameters

    private FloatingActionButton addMessageButton;
    private RecyclerView recyclerMessages;
    private MessageAdapter adapter;
    private List<Message> messages;
    private TabLayout tabLayout;
    private TabLayout.Tab tabMessages;
    private TabLayout.Tab tabWip;
    private TabLayout.Tab tabSuggestions;
    private TabLayout.Tab tabBugs;
    public static boolean VERBOSE = false;
    private MessageBoardViewModel messageBoardViewModel;

    public MessageBoardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment ContactFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MessageBoardFragment newInstance() {
        return new MessageBoardFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            log("...getArguments() != null");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        log("MessageBoardFragment.onCreateView(...)");
        View view =  inflater.inflate(R.layout.message_board_fragment, container, false);
        initComponents(view);
        initTabs();
        initViewModels();

        initRecycler();
        //selectMessages();
        initListeners();
        observe();
        return view;
    }
    private void initComponents(View view){
        log("...initComponents(View)");
        recyclerMessages = view.findViewById(R.id.messageBoardFragment_messages);
        addMessageButton = view.findViewById(R.id.messageBoardFragment_addMessage);
        tabLayout = view.findViewById(R.id.messageBoardFragment_tabLayout);
    }
    private void initListeners(){
        log("...initListeners()");
        addMessageButton.setOnClickListener(view->showMessageDialog());
        tabLayout.addOnTabSelectedListener(this);
        tabMessages.select();
    }
    private void initViewModels(){
        log("...initViewModels()");
        messageBoardViewModel = new ViewModelProvider(requireActivity()).get(MessageBoardViewModel.class);
        messageBoardViewModel.init("message");
    }
    private void initRecycler(){
        if( VERBOSE) log("...initRecycler()");
        adapter = new MessageAdapter(new ArrayList<>(), message ->
        {
            //log("...onMessageClick(Message)");
            //Toast.makeText(getContext(), message.toString(), Toast.LENGTH_LONG).show();
            showMessageDialog(message);
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerMessages.setLayoutManager(layoutManager);
        recyclerMessages.setItemAnimator(new DefaultItemAnimator());
        recyclerMessages.setAdapter(adapter);
    }
    private void initTabs(){
        log("...initTabs()");
        tabMessages = tabLayout.newTab();
        tabMessages.setText("messages");
        tabMessages.setTag("message");

        tabBugs = tabLayout.newTab();
        tabBugs.setText("bugs");
        tabBugs.setTag("bug");

        tabSuggestions = tabLayout.newTab();
        tabSuggestions.setText("suggestions");
        tabSuggestions.setTag("suggestion");

        tabWip = tabLayout.newTab();
        tabWip.setText("wip");
        tabWip.setTag("wip");

        tabLayout.addTab(tabMessages);
        tabLayout.addTab(tabBugs);
        tabLayout.addTab(tabSuggestions);
        tabLayout.addTab(tabWip);
    }
/*    private void selectMessages(){
        log("...selectMessages()");
        if(!InternetWorker.isConnected(getContext())){
            log("...not connected to the internet, cannot get messages");
            Toast.makeText(getContext() , requireContext().getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
            return;
        }
        MessageWorker.selectMessages(new MessageWorker.OnMessagesSelected() {
            @Override
            public void onMessages(List<Message> messageList) {
                log("...onMessages(List<Message>) size",messageList.size() );
                messages = messageList;
                setUserInterface(messages);
            }

            @Override
            public void onError(String message) {
                log("...onError(String message)");
                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
            }
        });
    }*/
    private void observe(){
        messageBoardViewModel.getMessages().observe(requireActivity(), new Observer<List<Message>>() {
            @Override
            public void onChanged(List<Message> messages) {
                log("...observe messages ");
                messages.forEach(System.out::println);
                adapter.setList(messages);
            }
        });
    }
    private void selectTab(String category){
        log("...selectTab()", category);
        if( category.equals("bug")){
            tabBugs.select();
        }else if( category.equals("suggestion")){
            tabSuggestions.select();
        }else if( category.equals("wip")){
            tabWip.select();
        }else if( category.equals("message")){
            tabMessages.select();
        }else{
            log("WARNING NO SUCH TAB");
        }


    }
    private void setUserInterface(List<Message> messageList){
        if( VERBOSE) log("...setUserInterface(List<Message>), size", messageList.size());
        adapter.setList(messageList);
    }
    private void setUserInterface(String category){
        log("...setUserInterface(String category)", category);
        adapter.setList(messages.stream().filter(message -> message.getCategory().equals(category)).collect(Collectors.toList()));

    }
    private void showMessageDialog(){
        if( !InternetWorker.isConnected(getContext())){
            log("...trying to add a message to message board without internet connection");
            Toast.makeText(getContext(), getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
            return;
        }
        MessageDialog dialog = new MessageDialog();
        dialog.setCallback(new MessageDialog.Callback() {
            @Override
            public void onMessage(Message message, MessageDialog.Mode mode) {
                log("MessageDialog.onMessage(Message, Mode)", message.getSubject());
                messageBoardViewModel.insert(message, getContext());
            }
        });
        dialog.show(getChildFragmentManager(), "add message");
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        log("...onTabSelected(TabLayout.Tab)", tab.getText().toString());
        //log("\t\tmessages size", messages.size());
        //setUserInterface(tab.getTag().toString());
        messageBoardViewModel.filter((String) tab.getTag());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
    private void showMessageDialog(Message message){
        log("...showMessageDialog(Message)");
        MessageDialog dialog = new MessageDialog(message);
        dialog.setCallback(new MessageDialog.Callback() {
            @Override
            public void onMessage(Message message, MessageDialog.Mode mode) {
                log("...onMessage updated(Message, Mode)");
                messageBoardViewModel.update(message, getContext());
            }
        });
        dialog.show(getChildFragmentManager(), "edit message");

    }
}