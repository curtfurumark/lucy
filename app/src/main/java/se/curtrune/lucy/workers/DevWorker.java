package se.curtrune.lucy.workers;

import static se.curtrune.lucy.util.Logger.log;

import java.util.List;

import se.curtrune.lucy.activities.threads.SelectThread;
import se.curtrune.lucy.classes.DevTodo;
import se.curtrune.lucy.persist.Queeries;

public class DevWorker {
    public interface Callback{
        void onRequest(List<DevTodo> todoList);
    }
    private  Callback callback;
    public static void requestTodoList(Callback callback){
        log("...requestTodoList(Callback)");
        String queery = Queeries.selectLucindaTodo();
        SelectThread thread = new SelectThread(queery, new SelectThread.Callback() {
            @Override
            public void onRequestSelectError(String errMessage) {

            }

            @Override
            public void onRequestSelectDone(String json) {
                log("...onRequestSelectDone(String)", json);
                callback.onRequest(null);
            }
        });

    }
}
