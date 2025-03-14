package se.curtrune.lucy.web;

import static se.curtrune.lucy.util.Logger.log;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;

import se.curtrune.lucy.screens.message_board.Message;
import se.curtrune.lucy.persist.DB1Result;
import se.curtrune.lucy.persist.Queeries;
import se.curtrune.lucy.util.Constants;

public class UpdateMessageThread extends Thread{
    private Message message;
    private String queery;
    public interface Callback{
        void onUpdated(DB1Result result);
    }
    private Callback callback;
    public static boolean VERBOSE = false;
    public UpdateMessageThread(Message message, Callback callback){
        this.message = message;
        this.callback = callback;
        queery = Queeries.updateMessage(message);

    }

    @Override
    public void run() {
        super.run();
        try {
            HTTPRequest request = new HTTPRequest(Constants.UPDATE_MESSAGES_URL);
            request.add("sql", queery);
            String json = HTTPClient.send(request);
            //json = HTTPClient.post(request);
            if( VERBOSE) log("...json", json);
            DB1Result result = new Gson().fromJson(json, DB1Result.class);
            if (callback != null) {
                new Handler(Looper.getMainLooper()).post(() -> callback.onUpdated(result));
            } else {
                log("UpdateThread, no callback");
                log(result);
            }
        }catch (Exception e){
            log("UpdateThread EXCEPTION");
            e.printStackTrace();
            if (callback != null) {
                new Handler(Looper.getMainLooper()).post(() -> callback.onUpdated(new DB1Result(e)));
            }
        }
    }
}
