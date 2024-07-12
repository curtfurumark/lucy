package se.curtrune.lucy.classes;

import static se.curtrune.lucy.util.Logger.log;

public class GlobalStats {
    private static GlobalStats instance;
    private Callback observer;
    private GlobalStats(){

    }
    public static GlobalStats getInstance() {
        if( instance == null){
            instance = new GlobalStats();
        }
        return instance;
    }

    public interface Callback{
        void onStatsChanged();
    }
    private Callback callback;
    public void register(Callback observer){
        log("GlobalStats.registers(StatsObserver)");
        this.observer = observer;
    }
    public void updateStats(){
        observer.onStatsChanged();
    }

}
