package se.curtrune.lucy.statistics;

import static se.curtrune.lucy.util.Logger.log;

import android.content.Context;

import java.util.List;

import se.curtrune.lucy.classes.Mental;
import se.curtrune.lucy.persist.LocalDB;
import se.curtrune.lucy.persist.Queeries;

public class TopTenStatistics {
    public static List<Mental> getItems(Context context) {
        log("...getItems()");
        String query = Queeries.selectTopTen();
        LocalDB db = new LocalDB(context);
        return db.selectMentals(query);
    }
}
