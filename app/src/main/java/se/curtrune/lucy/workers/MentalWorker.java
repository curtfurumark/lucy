package se.curtrune.lucy.workers;

import static se.curtrune.lucy.util.Logger.log;

import android.content.Context;

import com.jjoe64.graphview.series.DataPoint;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.Mental;
import se.curtrune.lucy.classes.State;
import se.curtrune.lucy.fragments.TopTenFragment;
import se.curtrune.lucy.persist.LocalDB;
import se.curtrune.lucy.persist.Queeries;

public class MentalWorker {
    public static boolean VERBOSE = false;


    @Deprecated
    public static List<Mental> selectTopTen(TopTenFragment.Mode mode, Context context) {
        LocalDB db = new LocalDB(context);
        String query = Queeries.selectMentalTopTen(mode);
        //return db.selectMentals(query);
        return null;
    }

    @Deprecated
    public static List<Mental> getLatestMentals(int limit, Context context){
        if( VERBOSE) log("...getLatestMentals(int, Context) limit", limit );
        LocalDB db = new LocalDB(context);
        String query = Queeries.selectLatestMentals(limit);
        //return db.selectMentals(query);
        return null;
    }

    @Deprecated
    public static List<Mental> getMentals(LocalDate date, boolean includeTemplates, boolean done, Context context) {
        if( VERBOSE) log("MentalWorker.getMentals(LocalDate,boolean, boolean)", date.toString());
        String queery = Queeries.selectMentals(date, false, true);
        LocalDB db = new LocalDB(context);
        //return db.selectMentals(queery);
        return null;
    }
    public static DataPoint[] getMentalsAsDataPoints(LocalDate date, Context context){
        log("MentalWorker.getMentalAdDataPoints(LocalDate, Context)", date.toString());
        List<Mental> mentals = getMentals(date,false, true, context);
        log("...number of mentals", mentals.size());
        DataPoint[] dataPoints = new DataPoint[mentals.size()];
        int currentEnergy = 0;
        for(int i = 0; i < mentals.size(); i++){
            currentEnergy +=  mentals.get(i).getEnergy();
            log(String.format(Locale.getDefault(), "adding point %d,%d", i, currentEnergy));
            dataPoints[i]  = new DataPoint(i, currentEnergy);
        }
        return dataPoints;
    }
    public static DataPoint[] getMentalsAsDataPoints(LocalDate date, Mental.Type mentalType, Context context){
        log("MentalWorker.getMentalAdDataPoints(LocalDate, Mental.Type, Context)", date.toString());
        log("...mentalType", mentalType.toString());
        List<Mental> mentals = getMentals(date,false, true, context);
        log("...number of mentals", mentals.size());
        DataPoint[] dataPoints = new DataPoint[mentals.size()];
        int currentLevel = 0;
        for(int i = 0; i < mentals.size(); i++){
            switch (mentalType){
                case ENERGY:
                    currentLevel +=  mentals.get(i).getEnergy();
                    break;
                case MOOD:
                    currentLevel +=  mentals.get(i).getMood();
                    break;
                case STRESS:
                    currentLevel +=  mentals.get(i).getStress();
                    break;
                case ANXIETY:
                    currentLevel +=  mentals.get(i).getAnxiety();
                    break;
            }

            log(String.format(Locale.getDefault(), "adding point %d,%d", i, currentLevel));
            dataPoints[i]  = new DataPoint(i, currentLevel);
        }
        return dataPoints;
    }


    /**
     *
     * @param date the date you wish to examine
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
        try(LocalDB db = new LocalDB(context)){
            items = db.selectItems(queery);
            //items.forEach(Logger::log);
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
