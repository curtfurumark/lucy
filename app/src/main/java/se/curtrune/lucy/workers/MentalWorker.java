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
    public boolean hasMental(Item item){
        log("MentalWorker(Item)");
        return false;
    }



    public static Mental insert(Mental mental, Context context) {
        log("MentalWorker.insert(Mental, Context)");
        LocalDB db = new LocalDB(context);
        return db.insert(mental);
    }

    public static int update(Mental mental, Context context) {
        log("MentalWorker.update(Mental, Context)");
        LocalDB db = new LocalDB(context);
        return db.update(mental);
    }

    public static List<Mental> getLatestMentals(int limit, Context context){
        log("...getLatestMentals(int, Context) limit", limit );
        LocalDB db = new LocalDB(context);
        String query = Queeries.selectLatestMentals(limit);
        return db.selectMentals(query);
    }

    public static List<Mental> getMentals(LocalDate date, Context context) {
        log("StatisticsWorker.getMentals(LocalDate)", date.toString());
        if( context == null){
            log("...CONTEXT IS NULL, getMentals");
        }
        LocalDB db = new LocalDB(context);
        return db.selectMentals(date);
    }
    public List<Mental> selectMentals(Context context) {
        log("MentalWorker.selectMentals()");
        LocalDB db = new LocalDB(context);
        List<Mental> items = db.selectMentals();
        return items;
    }
}
