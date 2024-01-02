package se.curtrune.lucy.workers;


import static se.curtrune.lucy.util.Logger.log;

import android.content.Context;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import se.curtrune.lucy.classes.DailyStatistics;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.Listable;
import se.curtrune.lucy.classes.State;
import se.curtrune.lucy.classes.Type;
import se.curtrune.lucy.classes.TypeStatistic;
import se.curtrune.lucy.persist.LocalDB;


public class StatisticsWorker {
    private static StatisticsWorker instance;
    private List<Listable> items;
    private List<Listable> typeItems;
    private DailyStatistics dailyStatistics;
    private LocalDate date = LocalDate.now();




    public interface Callback{
        void onUpdateItems(List<Listable> items);
        void onUpdateStats(List<Listable> stats);
        void onUpdateStats(DailyStatistics statistics);
    }
    private Callback callback;
    private StatisticsWorker(){
        //PersistProjects.selectItems(date, this);
    }
    public static StatisticsWorker getInstance() {
        if( instance == null){
            instance = new StatisticsWorker();
        }
        return instance;
    }

    public List<Listable> getItems(Type type){
        log("StatisticsWorker.getItems(Type)", type.toString());
        return items.stream().filter(i->((Item)i).getType().equals(type)).collect(Collectors.toList());
    }

    public List<Listable> getTypeStatistics(LocalDate date) {
        log("WorkingStats.getTypeStatistics(LocalDate)");

        List<Listable> stats = new ArrayList<>();
        int secs = 0;
        for (Type type : Type.values()) {
            TypeStatistic stat = new TypeStatistic(0, type);
            for (Listable listable : items) {
                Item item = (Item) listable;
                if (item.getType().equals(type)) {
                    secs += item.getDuration();
                    stat.add(item.getDuration());
                }
            }
            stats.add(stat);
            secs = 0;
        }
        return stats;
    }


    private DailyStatistics generateStatistics(){
        log("StatisticsWorker.generateStatistics()");
        dailyStatistics = new DailyStatistics();
        dailyStatistics.setTypeList(getTypeStatistics(date));
        dailyStatistics.setItems(items);
        dailyStatistics.setDate(date);
        return dailyStatistics;
    }


    public void register(Callback  callback){
        this.callback = callback;
    }

    @Deprecated
    public void  requestItems() {
        log("StatisticsWorker.requestItems()");
        callback.onUpdateItems(items);
    }
    public void requestStats(LocalDate date, Context context) {
        log("StatisticsWorker.requestStats(LocalDate)", date.toString());
        this.date = date;
        LocalDB db = new LocalDB(context);
        List<Item> items = db.selectItems(date, State.DONE);
        //RemoteDB.selectItems(this.date, State.DONE ,this);
    }
    public List<Item> selectItems(LocalDate date, Context context){
        log("...selectItems(LocalDate)");
        LocalDB db = new LocalDB(context);
        return db.selectItems(date);
    }
}
