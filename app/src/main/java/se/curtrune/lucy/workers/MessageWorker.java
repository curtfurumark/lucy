package se.curtrune.lucy.workers;

import static se.curtrune.lucy.util.Logger.log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import se.curtrune.lucy.activities.threads.SelectThread;
import se.curtrune.lucy.screens.message_board.Message;
import se.curtrune.lucy.persist.Queeries;
import se.curtrune.lucy.web.InsertThread;
import se.curtrune.lucy.web.UpdateMessageThread;

public class MessageWorker {



    public interface OnMessagesSelected{
        void onMessages(List<Message> messageList);
        void onError(String message);
    }

    public static void insert(Message message, InsertThread.Callback callback){
        String queery = Queeries.insert(message);
        InsertThread thread = new InsertThread(queery, callback);
        thread.start();
    }
    public static void selectMessages(OnMessagesSelected callback) {
        String query = "SELECT * FROM messages ORDER BY created DESC";
        SelectThread thread = new SelectThread(query, new SelectThread.Callback() {
            @Override
            public void onRequestSelectError(String errMessage) {
                log("...onRequestSelectError(String)", errMessage);
                callback.onError(errMessage);
            }

            @Override
            public void onRequestSelectDone(String json) {
                log("...onRequestSelectDone(String json)", json);
                List<Message> messages = new ArrayList<>(Arrays.asList(new Gson().fromJson(json, Message[].class)));
                callback.onMessages(messages);
            }
        });
        thread.start();
    }
    public static void update(Message message, UpdateMessageThread.Callback callback) {
        log("MessageWorker.update(Message)");
        UpdateMessageThread thread = new UpdateMessageThread(message, callback);
        thread.start();

    }
}