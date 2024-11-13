package se.curtrune.lucy.web;

import static se.curtrune.lucy.util.Logger.log;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;

public class CheckForUpdateThread extends Thread{
    private static final String VERSION_URL = "http://curtfurumark.se/lucinda/apk/auto.json";
    public interface Callback {
        void onRequestComplete(VersionInfo versionInfo, boolean res);
    }
    private Callback callback;

    public CheckForUpdateThread(Callback callback){
        this.callback = callback;
    }
    @Override
    public void run() {
        //super.run();
        log("...run(), check for updates");
        VersionInfo versionInfo = null;
        String reply =  HTTPClient.get(VERSION_URL);
        boolean stat = false;
        try {
            versionInfo = new Gson().fromJson(reply, VersionInfo.class);
            stat = true;
        }catch (Exception e){
            e.printStackTrace();
        }

        VersionInfo finalVersionInfo = versionInfo;
        boolean finalStat = stat;
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                callback.onRequestComplete(finalVersionInfo, finalStat);
            }
        });
    }
}
