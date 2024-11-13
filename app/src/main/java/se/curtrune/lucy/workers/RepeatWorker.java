package se.curtrune.lucy.workers;

import static se.curtrune.lucy.util.Logger.log;

import java.time.LocalDate;

import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.Repeat;

public class RepeatWorker {
    public static int createInstances(Item item){
        log("RepeatWorker.createInstances(Item)", item.getHeading());
        int nInstances = 0;
        if( !item.hasPeriod()){
            log("...item has no repeat, returning void");
            return 0;
        }
        Repeat repeat = item.getPeriod();
        LocalDate date = null;
        if( (date = repeat.getNextDate()) !=  null){
            Item instance = createInstance(item, date);
            nInstances++;
        }
        return nInstances;
    }
    private static Item createInstance(Item item, LocalDate targetDate){
        Item instance = new Item();
        instance.setTargetDate(targetDate);
        instance.setHeading(item.getHeading());
        instance.setCategory(item.getCategory());
        instance.setTags(item.getTags());
        instance.setMental(item.getMental());
        return instance;
    }
}
