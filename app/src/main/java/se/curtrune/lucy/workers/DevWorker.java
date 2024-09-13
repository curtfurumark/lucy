package se.curtrune.lucy.workers;

import java.util.List;

import se.curtrune.lucy.classes.DevTodo;

public class DevWorker {
    public interface Callback{
        void onRequest(List<DevTodo> todoList);
    }
    public static void requestTodoList(Callback callback){


    }
}
