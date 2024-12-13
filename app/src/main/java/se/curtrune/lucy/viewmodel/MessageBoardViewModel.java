package se.curtrune.lucy.viewmodel;

import static se.curtrune.lucy.util.Logger.log;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.stream.Collectors;

import se.curtrune.lucy.classes.Message;
import se.curtrune.lucy.persist.DB1Result;
import se.curtrune.lucy.web.UpdateMessageThread;
import se.curtrune.lucy.workers.MessageWorker;

public class MessageBoardViewModel extends ViewModel {
    private List<Message> messages;
    private String currentCategory;
    private final MutableLiveData<String> mutableError = new MutableLiveData<>();
    private final MutableLiveData<List<Message>> mutableMessages = new MutableLiveData<>();
    public LiveData<String> getErrorMessage(){
        return mutableError;
    }
    public LiveData<List<Message>> getMessages(){
        return mutableMessages;
    }
    public void init(String category){
        log("MessageBoardViewModel.init(String)");
        MessageWorker.selectMessages(new MessageWorker.OnMessagesSelected() {
            @Override
            public void onMessages(List<Message> messageList) {
                messages = messageList;
                currentCategory = category;
                filter(currentCategory);
                //mutableMessages.setValue(messages);
            }

            @Override
            public void onError(String errorMessage) {
                log("...onError(String)", errorMessage);
                mutableError.setValue(errorMessage);
            }
        });
    }
    public void insert(Message message, Context context) {
        log("MessageBoardViewModel.insert(Message, Context)", message.getSubject());
        MessageWorker.insert(message, result -> {
            log("...onItemInserted(DB1Result)");
            if( result.isOK()){
                message.setID(result.getID());
                messages.add(0, message);
                mutableMessages.setValue(messages);
            }else{
                log("...error inserting message");
                log(result);
                mutableError.setValue("error inserting message");
            }
        });
    }

    public void filter(String category) {
        log("MessageBoardViewModel.filter(String)", category);
        List<Message> filteredMessages = messages.stream().filter(message -> message.getCategory().equals(category)).collect(Collectors.toList());
        mutableMessages.setValue(filteredMessages);
    }

    public void update(Message message, Context context) {
        log("...update(Message, Context)", message.getSubject());
        MessageWorker.update(message, new UpdateMessageThread.Callback() {
            @Override
            public void onUpdated(DB1Result result) {
                log("...onUpdated(DBResult)", result.toString());
                if(!result.isOK()){
                    log("...error updating message");
                    mutableError.setValue(result.toString());
                    log(result);
                }else{
                    log("...update of message ok");
                }
            }
        });
    }
}
