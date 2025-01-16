package se.curtrune.lucy.screens.affirmations;

import static se.curtrune.lucy.util.Logger.log;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;

import se.curtrune.lucy.classes.Affirmation;
import se.curtrune.lucy.web.HTTPClient;


public class AffirmationThread extends Thread{
    private static final String GET_AFFIRMATION_URL = "https://www.affirmations.dev";
    public interface AffirmationThreadCallback{
        void onRequestCompleted(Affirmation affirmation);
        void onError(String message);
    }
    private final AffirmationThreadCallback callback;


    public AffirmationThread(AffirmationThreadCallback callback) {
        this.callback = callback;
    }

    @Override
    public void run() {
        log("AffirmationThread.run()");
        try {
            String json = HTTPClient.get(GET_AFFIRMATION_URL);
            log("...json", json);
            if( !json.startsWith("{")){
                new Handler(Looper.getMainLooper()).post(() -> callback.onError(json));
                return;
            }
            Affirmation affirmation = new Gson().fromJson(json, Affirmation.class);
            new Handler(Looper.getMainLooper()).post(() -> callback.onRequestCompleted(affirmation));
        }catch (Exception e){
            new Handler(Looper.getMainLooper()).post(() -> callback.onError(e.getMessage()));
        }
    }
}
