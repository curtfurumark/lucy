package se.curtrune.lucy.workers;

import static se.curtrune.lucy.util.Logger.log;

import android.content.Context;

import java.sql.SQLException;
import java.util.List;

import se.curtrune.lucy.activities.MentalListActivity;
import se.curtrune.lucy.classes.Mental;
import se.curtrune.lucy.persist.LocalDB;

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

    public List<Mental> selectMentals(Context context) {
        log("MentalWorker.selectMentals()");
        LocalDB db = new LocalDB(context);
        List<Mental> items = db.selectMental();
        return items;
    }

    public Mental insert(Mental mental, Context context) throws SQLException {
        log("MentalWorker.insert(Mental, Context)");
        LocalDB db = new LocalDB(context);
        return db.insert(mental);
    }

    public int update(Mental mental, Context context) {
        log("MentalWorker.update(Mental mental)");
        LocalDB db = new LocalDB(context);
        return db.update(mental);
    }
}
