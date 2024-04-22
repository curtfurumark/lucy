package se.curtrune.lucy.workers;

import static se.curtrune.lucy.util.Logger.log;

import android.content.Context;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.Mental;
import se.curtrune.lucy.fragments.TopTenFragment;
import se.curtrune.lucy.persist.LocalDB;
import se.curtrune.lucy.persist.Queeries;
import se.curtrune.lucy.util.Logger;

public class MentalWorker {
    private static MentalWorker instance;
    private MentalWorker(){


    }
    public static MentalWorker getInstance() {
        if( instance == null){
            instance = new MentalWorker();
        }
        return instance;
    }

    /**
     * the latest ten entries, notwithstanding date
     * @param context
     * @return
     */
    public static long getCurrentEnergy(Context context) {
        log("StatisticsWorker.getCurrentEnergy(Context)");
        String query = "SELECT * FROM mental ORDER BY updated DESC LIMIT 10";
        LocalDB db = new LocalDB(context);
        List<Mental> items = db.selectMentals(query);
        if (items.size() == 0) {
            log("...no mental items in database");
            return 0;
        }
        items.forEach(Logger::log);
        long sum = items.stream().mapToLong(Mental::getEnergy).sum();
        log("...sum energy", sum);
        return sum;
    }
    public static int getEnergy(LocalDate date, Context context){
        log("MentalWorker.getEnergy(LocalDate)", date.toString());
        List<Mental> mentals = getMentals(date,false,  context);
        return mentals.stream().mapToInt(Mental::getEnergy).sum();
    }
    public static Mental getMental(Item item, Context context){
        log("...getMental(Item)", item.getHeading());
        LocalDB db = new LocalDB(context);
        String query = String.format(Locale.ENGLISH,"SELECT * FROM mental WHERE itemID = %d", item.getID());
        return db.selectMental(query);
    }

    public static List<Mental> select(LocalDate firstDate, LocalDate lastDate, Context context) {
        log("MentalWorker.select(LocalDate, LocalDate, Context");
        String query = String.format("SELECT * FROM mental WHERE date >= %d AND date <= %d", firstDate.toEpochDay(), lastDate.toEpochDay());
        LocalDB db = new LocalDB(context);
        return db.selectMentals(query);

    }

    public static List<Mental> selectTopTen(TopTenFragment.Mode mode, Context context) {
        LocalDB db = new LocalDB(context);
        String query = Queeries.selectMentalTopTen(mode);
        return db.selectMentals(query);
    }

    public static int delete(Mental mental, Context context) {
        log("MentalWorker.delete(Mental, Context");
        LocalDB db = new LocalDB(context);
        return db.delete(mental);
    }

    public Mental getMental(String query, Context context){
        log("...getMental(String, Context)", query);
        LocalDB db = new LocalDB(context);
        return null;

    }
    public static Mental insert(Mental mental, Context context) {
        log("MentalWorker.insert(Mental, Context)");
        LocalDB db = new LocalDB(context);
        return db.insert(mental);
    }



    public static List<Mental> getLatestMentals(int limit, Context context){
        log("...getLatestMentals(int, Context) limit", limit );
        LocalDB db = new LocalDB(context);
        String query = Queeries.selectLatestMentals(limit);
        return db.selectMentals(query);
    }

    public static List<Mental> getMentals(LocalDate date, boolean includeTemplates, Context context) {
        log("StatisticsWorker.getMentals(LocalDate)", date.toString());
        if( context == null){
            log("...CONTEXT IS NULL, getMentals");
        }
        String queery = Queeries.selectMentals(date, false);
        LocalDB db = new LocalDB(context);
        return db.selectMentals(queery);
    }
    public List<Mental> selectMentals(Context context) {
        log("MentalWorker.selectMentals()");
        LocalDB db = new LocalDB(context);
        List<Mental> items = db.selectMentals();
        return items;
    }
    public static int update(Mental mental, Context context) {
        log("MentalWorker.update(Mental, Context)");
        LocalDB db = new LocalDB(context);
        return db.update(mental);
    }
}
