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
import se.curtrune.lucy.classes.MentalStats;
import se.curtrune.lucy.fragments.TopTenFragment;
import se.curtrune.lucy.persist.LocalDB;
import se.curtrune.lucy.persist.Queeries;
import se.curtrune.lucy.util.Logger;

public class MentalWorker {
    private static MentalWorker instance;
    public static boolean VERBOSE = false;
    private MentalWorker(){

    }
    public static MentalWorker getInstance() {
        if( instance == null){
            instance = new MentalWorker();
        }
        return instance;
    }

    public static  int calculateEnergy(List<Mental> mentals){
        return mentals.stream().mapToInt(Mental::getEnergy).sum();
    }
    public static int delete(Mental mental, Context context) {
        if( VERBOSE) log("MentalWorker.delete(MentalType, Context");
        LocalDB db = new LocalDB(context);
        return db.delete(mental);
    }

    /**
     * deletes the mental associated with said item,
     * if theres no mental to delete, this method just returns
     * @param item the item for which you want to delete it's mental
     * @param context context context context and more context
     * @return 0 if no mental to delete, otherwise rowsAffected ( 1 = success, 0 = trouble)
     */
    public static int deleteMental(Item item, Context context){
        log("MentalWorker.deleteMental(Item, Context)");
        if( item.getMental() == null){
            log("WARNING, no mental to delete");
            return 0;
        }
        try(LocalDB db = new LocalDB(context)){
            return db.delete(item.getMental());
        }
    }
    public static int getAnxiety(LocalDate date, Context context){
        if( VERBOSE) log("MentalWorker.getAnxiety(LocalDate)", date.toString());
        List<Mental> mentals = getMentals(date,false, true,  context);
        return mentals.stream().mapToInt(Mental::getAnxiety).sum();
    }

    /**
     * the latest ten entries, notwithstanding date
     * @param context, the f-ing context, what did you expect=
     * @return energy as calculated from the ten latest mental entries
     */
    public static long getCurrentEnergy(Context context) {
        log("MentalWorker.getCurrentEnergy(Context)");
        long sum;
        String query = "SELECT * FROM mental ORDER BY updated DESC LIMIT 10";
        try(LocalDB db = new LocalDB(context)) {
            List<Mental> items = db.selectMentals(query);
            if (items.size() == 0) {
                log("...no mental items in database");
                return 0;
            }
            items.forEach(Logger::log);
            sum = items.stream().mapToLong(Mental::getEnergy).sum();
            log("...sum energy", sum);
        }
        return sum;
    }
    public static int getEnergy(LocalDate date, Context context){
        if( VERBOSE) log("MentalWorker.getEnergy(LocalDate)", date.toString());
        List<Mental> mentals = getMentals(date,false, true,  context);
        if( VERBOSE){
            mentals.forEach(System.out::println);
        }
        return mentals.stream().mapToInt(Mental::getEnergy).sum();
    }

    /**
     * get the mental instance associated with this item
     * @param item, the item you wish to get item for
     * @param context so tired of contexts
     * @return the mental, null if none?
     */
    public static Mental getMental(Item item, Context context){
        if( VERBOSE) log("MentalWorker.getMental(Item)", item.getHeading());
        LocalDB db = new LocalDB(context);
        String query = String.format(Locale.ENGLISH,"SELECT * FROM mental WHERE itemID = %d", item.getID());
        return db.selectMental(query);
    }
    public static MentalStats getMentalStats(List<Item> items, Context context) {
        log("MentalStats.getMentalStats(List<Item>, Context)");
        MentalStats mentalEstimate = new MentalStats();
        for( Item item: items){
            if(item.isTemplate()){
                log("item isTemplate, TODO: something intelligent");
            }
            Mental mental = MentalWorker.getMental(item, context);
            mentalEstimate.add(mental);
            mentalEstimate.plusEnergy(mental.getEnergy());
            mentalEstimate.plusAnxiety(mental.getAnxiety());
            mentalEstimate.plusStress(mental.getStress());
            mentalEstimate.plusMood(mental.getMood());
        }
        return mentalEstimate;
    }
    public static List<Mental> getMentals(List<Item> items, Context context){
        log("...getMentals(List<Item>)");
        List<Mental> mentalList = new ArrayList<>();
        for( Item item: items){
            Mental mental = MentalWorker.getMental(item, context);
            assert  mental != null;
            mentalList.add(mental);
        }
        return mentalList;
    }
    public static int getMood(LocalDate date, Context context){
        if( VERBOSE) log("MentalWorker.getMood(LocalDate)", date.toString());
        List<Mental> mentals = getMentals(date,false, true,  context);
        return mentals.stream().mapToInt(Mental::getMood).sum();
    }
    public static MentalStats getStatistics(List<Item> items, Context context){
        log("MentalWorker.getStatistics(List<Item>, Context))");
        MentalStats stats = new MentalStats();
        for( Item item: items){
            //if(item.isTemplate()){
            //    log("item isTemplateTODO, something intelligent");
            //}
            Mental mental = MentalWorker.getMental(item, context);
            if( mental == null){
                log("...mental is null for item", item.getHeading());
            }
            stats.add(mental);
            stats.plusEnergy(mental.getEnergy());
            stats.plusAnxiety(mental.getAnxiety());
            stats.plusStress(mental.getStress());
            stats.plusMood(mental.getMood());
        }

        return stats;
    }
    public static int getStress(LocalDate date, Context context){
        if( VERBOSE) log("MentalWorker.getStress(LocalDate)", date.toString());
        List<Mental> mentals = getMentals(date,false, true,  context);
        return mentals.stream().mapToInt(Mental::getStress).sum();
    }

    public static List<Mental> select(LocalDate firstDate, LocalDate lastDate, Context context) {
        if( VERBOSE) log("MentalWorker.select(LocalDate, LocalDate, Context");
        String query = String.format("SELECT * FROM mental WHERE date >= %d AND date <= %d", firstDate.toEpochDay(), lastDate.toEpochDay());
        try(LocalDB db = new LocalDB(context)){
            return db.selectMentals(query);
        }
    }

    public static List<Mental> selectTopTen(TopTenFragment.Mode mode, Context context) {
        LocalDB db = new LocalDB(context);
        String query = Queeries.selectMentalTopTen(mode);
        return db.selectMentals(query);
    }



    public static Mental insert(Mental mental, Context context) {
        if( VERBOSE) log("MentalWorker.insert(MentalType, Context)");
        LocalDB db = new LocalDB(context);
        return db.insert(mental);
    }


    public static List<Mental> getLatestMentals(int limit, Context context){
        if( VERBOSE) log("...getLatestMentals(int, Context) limit", limit );
        LocalDB db = new LocalDB(context);
        String query = Queeries.selectLatestMentals(limit);
        return db.selectMentals(query);
    }

    public static List<Mental> getMentals(LocalDate date, boolean includeTemplates, boolean done, Context context) {
        if( VERBOSE) log("MentalWorker.getMentals(LocalDate,boolean, boolean)", date.toString());
        if( context == null){
            log("...CONTEXT IS NULL, getMentals");
        }
        String queery = Queeries.selectMentals(date, false, true);
        LocalDB db = new LocalDB(context);
        return db.selectMentals(queery);
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
     * update a mental item whatever
     * @param mental, the mental item to updated
     * @param context and context is still just context
     * @return rowsaffected, in other words, 1 is ok, all other is fail
     */
    public static int update(Mental mental, Context context) {
        if( VERBOSE) log("MentalWorker.update(MentalType, Context)");
        LocalDB db = new LocalDB(context);
        return db.update(mental);
    }

    /**
     * selects all the "generated" mentals...sorry but this is conceptually wrong
     *
     * @param item, the parent
     * @param context context,
     * @return a list
     */
    public static List<Mental> getMentalChildren(Item item, Context context) {
        log("...getMentalChildren(Item, Context)");
        String queery = Queeries.selectMentalChildren(item);
        try(LocalDB db = new LocalDB(context)){
            return db.selectMentals(queery);
        }
    }

    public static List<Mental> selectMentals(List<Item> children, Context context) {
        log("...selectMentals(List<Item>, Context)");
        List<Mental> mentals = new ArrayList<>();
        for( Item item: children){
            mentals.add(MentalWorker.getMental(item, context));
        }
        return mentals;
    }
}
