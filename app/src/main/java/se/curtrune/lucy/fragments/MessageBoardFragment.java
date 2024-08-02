package se.curtrune.lucy.fragments;

import static se.curtrune.lucy.util.Logger.log;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import se.curtrune.lucy.R;
import se.curtrune.lucy.adapters.MessageAdapter;
import se.curtrune.lucy.classes.Message;
import se.curtrune.lucy.dialogs.MessageDialog;
import se.curtrune.lucy.workers.InternetWorker;
import se.curtrune.lucy.workers.MessageWorker;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MessageBoardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MessageBoardFragment extends Fragment {



    // TODO: Rename and change types of parameters

    private FloatingActionButton addMessageButton;
    private RecyclerView recyclerMessages;
    private MessageAdapter adapter;
    private List<Message> messages;
    public static boolean VERBOSE = false;

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
        initListeners();
        initRecycler();
        //TODO, http?
        log("...will allowAllSSL");
        //HttpsTrustManager.allowAllSSL();
        log("...after allowing ssl");
        selectMessages();
        return view;
    }

    private void initComponents(View view){
        recyclerMessages = view.findViewById(R.id.messageBoardFragment_messages);
        addMessageButton = view.findViewById(R.id.messageBoardFragment_addMessage);
    }
    private void initListeners(){
        addMessageButton.setOnClickListener(view->showMessageDialog());
    }
    private void initRecycler(){
        if( VERBOSE) log("...initRecycler()");
        adapter = new MessageAdapter(new ArrayList<>(), message -> log("...onMessageClick(Message)"));
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerMessages.setLayoutManager(layoutManager);
        recyclerMessages.setItemAnimator(new DefaultItemAnimator());
        recyclerMessages.setAdapter(adapter);
    }
    private void selectMessages(){
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
    }
    private void setUserInterface(List<Message> messageList){
        if( VERBOSE) log("...setUserInterface(List<Message>), size", messageList.size());
        adapter.setList(messageList);
    }
    private void showMessageDialog(){
        if( !InternetWorker.isConnected(getContext())){
            log("...trying to add a message to messageboard without internet connection");
            Toast.makeText(getContext(), getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
            return;
        }
        MessageDialog dialog = new MessageDialog();
        dialog.setCallback(message -> {
            log("...onNewMessage(Message)", message.getSubject());
            MessageWorker.insert(message, result -> {
                log("...onItemInserted(DB1Result)");
                if( result.isOK()){
                    message.setID(result.getID());
                    messages.add(0, message);
                    adapter.notifyItemInserted(0);
                }
            });
        });
        dialog.show(getChildFragmentManager(), "add message");
    }
}