package se.curtrune.lucy.workers;


import static se.curtrune.lucy.util.Logger.log;

import android.content.Context;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.Listable;
import se.curtrune.lucy.classes.Mental;
import se.curtrune.lucy.classes.State;
import se.curtrune.lucy.classes.Type;
import se.curtrune.lucy.persist.LocalDB;
import se.curtrune.lucy.util.Logger;


public class StatisticsWorker {



    public static long  getCurrentEnergy(Context context){
        log("StatisticsWorker.getCurrentEnergy(Context)");
        String query = "SELECT * FROM mental ORDER BY updated DESC LIMIT 10";
        LocalDB db = new LocalDB(context);
        List<Mental> items = db.selectMentals(query);
        if( items.size() == 0){
            log("...no mental items in database");
            return 0;
        }
        items.forEach(Logger::log);
        long sum = items.stream().mapToLong(Mental::getEnergy).sum();
        log("...sum energy", sum);
        return sum;
    }
    public static long getEnergy(LocalDate date, Context context){
        log("StatisticsWorker.getEnergy(LocalDate)", date.toString());
        List<Mental> items = MentalWorker.getMentals(date, context);
        return items.stream().mapToInt(Mental::getEnergy).sum();
    }
}
