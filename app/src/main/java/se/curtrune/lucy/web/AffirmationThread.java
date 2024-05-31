package se.curtrune.lucy.web;

import static se.curtrune.lucy.util.Logger.log;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;

import se.curtrune.lucy.classes.Affirmation;


public class AffirmationThread extends Thread{
    private static String GET_AFFIRMATION_URL = "https://www.affirmations.dev";
    public interface AffirmationThreadCallback{
        void onRequestCompleted(Affirmation affirmation);
    }
    private AffirmationThreadCallback callback;
    private void callback(Affirmation affirmation) {
        if (callback != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    callback.onRequestCompleted(affirmation);
                }
            });
        }
    }

    public AffirmationThread(AffirmationThreadCallback callback) {
        this.callback = callback;
    }

    @Override
    public void run() {
        log("AffirmationThread.run()");
        String json = HTTPClient.get(GET_AFFIRMATION_URL);
        Affirmation affirmation = new Gson().fromJson(json, Affirmation.class);
        callback(affirmation);
    }
}
