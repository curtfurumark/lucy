package se.curtrune.lucy.screens.affirmations;

import static se.curtrune.lucy.util.Logger.log;

import se.curtrune.lucy.classes.Affirmation;

public class AffirmationWorker {
    public interface RequestAffirmationCallback{
        void onRequest(Affirmation affirmation);
        void onError(String message);
    }
    public static void requestAffirmation(RequestAffirmationCallback callback){
        log("AffirmationWorker.requestAffirmation(RequestAffirmationCallback)");
        AffirmationThread thread = new AffirmationThread(new AffirmationThread.AffirmationThreadCallback() {
            @Override
            public void onRequestCompleted(Affirmation affirmation) {
                callback.onRequest(affirmation);
            }

            @Override
            public void onError(String message) {
                callback.onError(message);
            }
        });
        thread.start();
    }
}
