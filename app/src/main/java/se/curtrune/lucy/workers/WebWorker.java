package se.curtrune.lucy.workers;

import static se.curtrune.lucy.util.Logger.log;

import com.google.gson.Gson;

import se.curtrune.lucy.classes.Affirmation;
import se.curtrune.lucy.web.AffirmationThread;
import se.curtrune.lucy.web.HTTPBasic;

public class WebWorker {
    public interface RequestAffirmationCallback{
        void onRequest(Affirmation affirmation);
    }
    public static void requestAffirmation(RequestAffirmationCallback callback){
        log("WebWorker.requestAffirmation(RequestAffirmationCallback)");
        AffirmationThread thread = new AffirmationThread(new AffirmationThread.AffirmationThreadCallback() {
            @Override
            public void onRequestCompleted(Affirmation affirmation) {
                callback.onRequest(affirmation);
            }
        });
        thread.start();
    }
}
