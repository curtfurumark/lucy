package se.curtrune.lucy.workers;

import static se.curtrune.lucy.util.Logger.log;

import android.content.Context;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import se.curtrune.lucy.classes.item.Item;
import se.curtrune.lucy.classes.Repeat;
import se.curtrune.lucy.persist.ItemsWorker;
import se.curtrune.lucy.persist.SqliteLocalDB;

public class RepeatWorker {
    public static boolean VERBOSE = false;
    private static LocalDate maxDate = LocalDate.now().plusDays(60);
    public static List<Item> createInstances(Repeat repeat, Context context){
        log("RepeatWorker.createInstances(Repeat, Context)");
        assert  repeat != null;
        Item template = ItemsWorker.selectItem(repeat.getTemplateID(), context);
        assert template != null;
        List<Item> instances = new ArrayList<>();
        LocalDate currentDate = repeat.getFirstDate();
        do{
            instances.add(createInstance(template, currentDate));
        }while ((currentDate = getNextDate(currentDate, repeat)) != null);
        return instances;
    }

    private static Item createInstance(Item template, LocalDate targetDate){
        if(VERBOSE)log("RepeatWorker.createInstance(Item, LocalDate)");
        Item instance = new Item();
        instance.setTargetDate(targetDate);
        instance.setParentId(template.getID());
        instance.setHeading(template.getHeading());
        instance.setTags(template.getTags());
        instance.setTargetTime(template.getTargetTime());
        instance.setComment(template.getComment());
        instance.setColor(template.getColor());
        instance.setRepeatID(template.getRepeatID());
        instance.setCategory(template.getCategory());
        instance.setTags(template.getTags());
        instance.setMental(template.getMental());
        instance.setIsCalenderItem(template.isCalenderItem());
        return instance;
    }
    public static Item insertItemWithRepeat(Item template, Context context){
        log("RepeatTest.insertItemWithRepeat(Item)", template.getHeading());
        try(SqliteLocalDB db = new SqliteLocalDB(context)) {
            template.setIsTemplate(true);
            template = db.insert(template);
            Repeat repeat = template.getRepeat();
            if( repeat.isInfinite()){
                repeat.setLastDate(maxDate);
            }
            repeat.setTemplateID(template.getID());
            repeat = ItemsWorker.insert(repeat, context);
            if (repeat != null) {
                repeat.setUpdated(LocalDate.now());
                template.setRepeatID(repeat.getID());
                db.update(template);
                List<Item> items = RepeatWorker.createInstances(repeat, context);
                for (Item instance : items) {
                    instance.setRepeatID(repeat.getID());
                }
                ItemsWorker.insert(items, context);
            }else{
                log("ERROR inserting repeat into database");
            }
        }
        return template;
    }
    private static LocalDate getNextDate(LocalDate currentDate, Repeat repeat){
        assert repeat != null;
        log(String.format(Locale.getDefault(),"RepeatWorker.getNextDate(LocalDate %s ,Repeat %s)",currentDate.toString(), repeat.toString()));
        int qualifier = repeat.getQualifier();
        Repeat.Unit unit = repeat.getUnit();
        if( qualifier < 1){
            log("WARNING, qualifier less than one, setting it to one");
            qualifier = 1;
        }
        LocalDate nextDate = LocalDate.now();
        switch (unit){
            case DAY:
                nextDate =  currentDate.plusDays(qualifier);
                break;
            case WEEK:
                nextDate =  currentDate.plusWeeks(qualifier);
                break;
            case MONTH:
                nextDate =  currentDate.plusMonths(qualifier);
                break;
            case YEAR:
                nextDate =  currentDate.plusYears(qualifier);
                break;
            case DAYS_OF_WEEK:
                return null;
        }
        if( nextDate.isAfter(repeat.getLastDate())){
            repeat.setLastDate(currentDate);//the last "valid/repeat" date
            return null;
        }
        return nextDate;
    }
    private static void saveInstances(List<Item> items, Context context){
        log("RepeatWorker.saveInstances(List<Item>, Context)");
        ItemsWorker.insert(items, context);
    }
    public static void setMaxDate(LocalDate date){
        maxDate = date;
    }

    /**
     * this is the one
     * @param context
     */
    public static void updateRepeats(Context context){
        log("RepeatWorker.updateRepeats(Context)");
        List<Repeat> repeats = ItemsWorker.selectRepeats(context);
        for(Repeat repeat: repeats){
            if(repeat.isInfinite()){
                if( updateNeeded(repeat)){
                    log("CREATE NEW INSTANCES, repeat id", repeat.getID());
                    repeat = updateRepeat(repeat, context);
                    log(repeat);
                    List<Item> instances = createInstances(repeat, context);
                    saveInstances(instances, context);
                    log("new instances of item saved");
                    for(Item item: instances){
                        log(String.format(Locale.getDefault(), "%s, %s", item.getHeading(), item.getTargetDate().toString()));
                    }
                }else{
                    log("NO UPDATED NEEDED FOR repeat id", repeat.getID());
                }
            }
        }
    }
    public static boolean updateNeeded(Repeat repeat){
        log("...updateNeeded(Repeat)");
        LocalDate breakPointDate = repeat.getLastDate().minusMonths(1);
        if( VERBOSE){
            log("...repeat last date", repeat.getLastDate());
            log("...breakPointDate", breakPointDate);
        }
        if( LocalDate.now().isAfter(breakPointDate)){
            return true;
        }
        return false;
    }

    /**
     * resets the repeat object for the next period
     * @param repeat, the repeat object
     * @param context, context context
     * @return repeat updated in database
     */
    public static Repeat updateRepeat(Repeat repeat, Context context){
        log("...updateRepeat(Repeat) id", repeat.getID());
        log(repeat);
        LocalDate previousLastDate = repeat.getLastDate();
        repeat.setLastDate(maxDate);
        LocalDate firstDate = getNextDate(previousLastDate, repeat);
        repeat.setFirstDate(firstDate);
        repeat.setUpdated(LocalDate.now());
        int rowsAffected = ItemsWorker.update(repeat, context);
        if( rowsAffected != -1 ){
            log("ERROR, updating repeat");
        }
        return repeat;
    }
}
