package se.curtrune.lucy.util;


import static se.curtrune.lucy.util.Logger.log;

import android.os.Handler;
import android.os.Looper;

import java.util.Timer;
import java.util.TimerTask;


public class Kronos {
    private Timer timer;
    private long ownerItemID;
    private TimerTask timerTask;
    private static final boolean VERBOSE = false;
    //private boolean ACTIVITY_VISIBLE = true;

    private long seconds;
    private long startTime;

    public enum State{
        STOPPED, RUNNING, PAUSED, PENDING
    }
    private State timerState = State.PENDING;
    public interface Callback{
        void onTimerTick(long secs);
    }
    private Callback callback;


    private static Kronos instance;
    private Kronos(Callback callback){
        this.callback = callback;
    }
    public  static Kronos getInstance(Callback callback){
        if ( instance == null){
            instance = new Kronos(callback);
        }
        return instance;
    }
    public long getElapsedTime(){
        return seconds;
    }
    public State getState(){
        return timerState;
    }

    public void pause(){
        if( VERBOSE ) log("Kronos.pause()");
        timer.cancel();
        timerTask.cancel();
        timerState = State.PAUSED;
    }

    public void reLoadTimer(Callback callback){
        if( VERBOSE) log("Kronos.reLoadTimer(Callback)");
        if(timer != null){
            timer.cancel();
        }
        if(timerTask != null){
            timerTask.cancel();
        }
        instance = new Kronos(callback);
        timerState = State.PENDING;
        seconds = 0;


    }
    public void resume(){
        if(VERBOSE) log("Kronos.resume()");
        start(ownerItemID);
    }

    public void removeCallback(){
        if( VERBOSE) log("Kronos.removeCallback()");
        this.callback = null;
    }

    public void reset(){
        if( VERBOSE) log("Kronos.reset()");
        seconds = 0;
        timerState = State.PENDING;
        if(timer == null){
            log("timer is null");
            return;
        }
        timer.cancel();
        timerTask.cancel();
    }

    public void setCallback(Callback callback) {
        if( VERBOSE) log("Kronos.setCallback(Callback)");
        this.callback = callback;
    }
    public void setElapsedTime(long seconds){
        this.seconds = seconds;
    }
    public void start(long ownerItemID){
        if( VERBOSE) log("Kronos.start() item id ", ownerItemID);
        this.ownerItemID = ownerItemID;
        timerState = State.RUNNING;
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                startTime = System.currentTimeMillis() / 1000;
                seconds++;
                if ( callback != null){
                    //TODO, kraschar, tappar callbacken
                    new Handler(Looper.getMainLooper()).post(() -> callback.onTimerTick(seconds));
                }
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0 , 1000);
    }

    /**
     * stops the timer, and resets it, maybe there is a better way
     */
    public void stop(){
        if( VERBOSE) log("Kronos.stop()");
        timerState = State.PENDING;
        seconds = 0;
        if( timer != null) {
            timer.cancel();
        }else{
            log("WARNING, Kronos.stop(), timer is null");
        }
        if( timerTask != null ) {
            timerTask.cancel();
        }else{
            log("WARNING, Kronos.stop(), timerTask is null");
        }
    }


}
