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
import se.curtrune.lucy.workers.HttpsTrustManager;
import se.curtrune.lucy.workers.MessageWorker;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MessageBoardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MessageBoardFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FloatingActionButton addMessageButton;
    private RecyclerView recyclerMessages;
    private MessageAdapter adapter;

    private List<Message> messages;

    public MessageBoardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ContactFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MessageBoardFragment newInstance(String param1, String param2) {
        MessageBoardFragment fragment = new MessageBoardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        log("ContactFragment.onCreateView(...)");
        View view =  inflater.inflate(R.layout.message_board_fragment, container, false);
        initComponents(view);
        initListeners();
        initRecycler();
        log("...will allowAllSSL");
        HttpsTrustManager.allowAllSSL();
        log("...after allowing ssl");
        selectMessages();
        return view;
    }

    private void initComponents(View view){
        log("...initComponents(View)");
        recyclerMessages = view.findViewById(R.id.messageBoardFragment_messages);
        addMessageButton = view.findViewById(R.id.messageBoardFragment_addMessage);
    }
    private void initListeners(){
        log("...initListeners()");
        addMessageButton.setOnClickListener(view->showMessageDialog());
    }
    private void initRecycler(){
        log("...initRecycler()");
        adapter = new MessageAdapter(new ArrayList<>(), message -> log("...onMessageClick(Message)"));
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerMessages.setLayoutManager(layoutManager);
        recyclerMessages.setItemAnimator(new DefaultItemAnimator());
        recyclerMessages.setAdapter(adapter);
    }
    private void insert(Message message){
        log("...insert(Message)");
        MessageWorker.insert(message, result -> {
            log("...onItemInserted(DB1Result)");
            log(result);
        });
    }
    private void selectMessages(){
        log("...selectMessages()");
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
        log("...setUserInterface(List<Message>), size", messageList.size());
        adapter.setList(messageList);
    }
    private void showMessageDialog(){
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