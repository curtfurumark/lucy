package se.curtrune.lucy.workers;

import se.curtrune.lucy.classes.Message;
import se.curtrune.lucy.persist.DB1Result;

public class MessageWorker {
    public interface OnInsertedCallback{
        void onInserted(Message message, DB1Result db1Result);
    }
    public static void insert(Message message, OnInsertedCallback callback){

    }
}
