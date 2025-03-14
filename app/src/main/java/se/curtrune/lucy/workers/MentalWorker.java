package se.curtrune.lucy.workers;

import static se.curtrune.lucy.util.Logger.log;

import android.content.Context;

import java.time.LocalDate;
import java.util.List;

import se.curtrune.lucy.classes.item.Item;
import se.curtrune.lucy.classes.Mental;
import se.curtrune.lucy.classes.State;
import se.curtrune.lucy.persist.ItemsWorker;
import se.curtrune.lucy.persist.SqliteLocalDB;
import se.curtrune.lucy.persist.Queeries;

public class MentalWorker {
    public static boolean VERBOSE = false;
    
    /*** @param date the date you wish to examine
     * @param context, context
     * @return a Mental representing the sum of all the item said date
     */
    public static Mental getMental(LocalDate date, Context context) {
        log("MentalWorker.getMental(LocalDate, Context)");
        List<Item> items = ItemsWorker.selectItems(date, context);
        int anxiety = 0, energy = 0, mood = 0, stress = 0;
        anxiety = items.stream().mapToInt(Item::getAnxiety).sum();
        energy = items.stream().mapToInt(Item::getEnergy).sum();
        mood = items.stream().mapToInt(Item::getMood).sum();
        stress = items.stream().mapToInt(Item::getStress).sum();
        return new Mental(anxiety, energy, mood, stress);
    }

    /**
     *
     * @param date the date to interrogate
     * @param context, context
     * @return Mental sum for done items
     */
    public static Mental getCurrentMental(LocalDate date, Context context) {
        log("MentalWorker.getCurrentMental(LocalDate)", date.toString());
        String queery = Queeries.selectItems(date, State.DONE);
        List<Item> items;
        int anxiety = 0;
        int energy = 0;
        int mood = 0;
        int stress = 0;
        try(SqliteLocalDB db = new SqliteLocalDB(context)){
            items = db.selectItems(queery);
            anxiety = items.stream().mapToInt(Item::getAnxiety).sum();
            energy = items.stream().mapToInt(Item::getEnergy).sum();
            mood = items.stream().mapToInt(Item::getMood).sum();
            stress = items.stream().mapToInt(Item::getStress).sum();
        }catch (Exception exception){
            exception.printStackTrace();
            log(exception.getMessage());
        }
        return new Mental(anxiety, energy, mood, stress);
    }
}
