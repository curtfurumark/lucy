package se.curtrune.lucy.web;


import static se.curtrune.lucy.util.Logger.log;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;

import se.curtrune.lucy.persist.DB1Result;


public class InsertThread extends Thread{
    private String sql;
    public static boolean VERBOSE = false;
    public interface Callback{
        void onItemInserted(DB1Result result);
    }
    private Callback callback;
    public InsertThread(String sql, Callback callback) {
        log("InsertThread() sql", sql);
        this.sql = sql;
        this.callback = callback;
    }

    private void callback(DB1Result result){
        if( VERBOSE) log("InsertThread.callback(DB1Result)");
        if( callback != null){
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    callback.onItemInserted(result);
                }
            });

        }else{
            log("missing callback,but thats ok");
        }
    }


    public void run() {
        if( VERBOSE) log("InsertThread.run()");
        HTTPRequest request= new HTTPRequest(HTTPClient.INSERT_URL);
        request.add("sql", sql);
        request.setMethod(HttpMethod.POST);
        try {
            String json = HTTPClient.send(request);
            if( VERBOSE) log("...json", json);
            Gson gson = new Gson();
            DB1Result result = gson.fromJson(json, DB1Result.class);
            if(VERBOSE) log("...after result to json");
            callback(result);
        } catch (Exception e) {
            log("...exception", e.getMessage());
            e.printStackTrace();
            callback(new DB1Result(e));
        }
    }
}
