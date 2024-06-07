package se.curtrune.lucy.activities.threads;

import static se.curtrune.lucy.util.Logger.log;

import android.os.Handler;
import android.os.Looper;

import java.io.IOException;

import se.curtrune.lucy.web.HTTPClient;
import se.curtrune.lucy.web.HTTPRequest;
import se.curtrune.lucy.web.HttpMethod;


public class SelectThread extends Thread {
    private final String query;
    public static boolean VERBOSE = false;
    public interface Callback{
        void onRequestSelectError(String errMessage);
        void onRequestSelectDone(String json);
    }
    private final Callback callback;
    public SelectThread(String query, Callback callback) {
        log("SelectThread(String query, Callback callback) query", query);
        this.callback = callback;
        this.query = query;
    }
    private void callback(String json){
        if( VERBOSE) log("SelectThread.callback(String json)");
        if( callback == null){
            log("...no callback, not ok in any way");
        }
        if ( !validateJson(json)){
            log("...reply not valid json,", json);
            new Handler(Looper.getMainLooper()).post(() -> callback.onRequestSelectError(json));
            return;

        }
        new Handler(Looper.getMainLooper()).post(() -> callback.onRequestSelectDone(json));;

    }

    @Override
    public void run() {
        if( VERBOSE) log("SelectThread.run()");
        HTTPRequest request= new HTTPRequest(HTTPClient.SELECT_URL);
        request.add("sql", query);
        request.setMethod(HttpMethod.POST);
        try {
            String json = HTTPClient.send(request);
            if( VERBOSE) log("...json", json);
            callback(json);
        } catch (IOException e) {
            new Handler(Looper.getMainLooper()).post(() -> callback.onRequestSelectError(e.toString()));
        }
    }
    private boolean validateJson(String json){
        return json.startsWith("[");
    }
}
