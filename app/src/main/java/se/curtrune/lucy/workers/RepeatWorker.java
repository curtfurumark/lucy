package se.curtrune.lucy.workers;

import static se.curtrune.lucy.util.Logger.log;

import android.content.Context;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.Repeat;

public class RepeatWorker {
    public static boolean VERBOSE = false;
    private static LocalDate maxDate = LocalDate.now().plusDays(30);
    public static List<Item> createInstances(Item template){
        log("RepeatWorker.createInstances(Item)", template.getHeading());
        List<Item> items = new ArrayList<>();
        if( !template.hasPeriod()){
            log("...item has no repeat, returning void");
            return items;
        }
        Repeat repeat = template.getPeriod();
        if(repeat.isInfinite()){
            repeat.setMaxDate(maxDate);
        }
        LocalDate currentDate = repeat.getFirstDate();
        if(currentDate == null){
            currentDate = LocalDate.now();
        }
        Item instance = createInstance(template, currentDate);
        items.add(instance);
        LocalDate nextDate;
        while ((nextDate =  repeat.getNextDate(currentDate)) != null){
            items.add(createInstance(template, nextDate));
            currentDate = nextDate;
        }
        return items;
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
        instance.setRepeatID(template.getRepeatID());
        instance.setCategory(template.getCategory());
        instance.setTags(template.getTags());
        instance.setMental(template.getMental());
        return instance;
    }
    public static void insertItemWithRepeat(Item item, Context context){
        log("RepeatTest.insertItemWithRepeat(Item)", item.getHeading());
        if( !item.hasPeriod()){
            log("ERROR, missing repeat");
            return;
        }
        item.setIsTemplate(true);
        item = ItemsWorker.insert(item, context);
        Repeat repeat = item.getPeriod();
        repeat.setTemplateID(item.getID());
        repeat = ItemsWorker.insert(repeat, context);
        item.setRepeatID(repeat.getID());
        ItemsWorker.update(item, context);
        List<Item> items = RepeatWorker.createInstances(item);
        for(Item instance : items){
            instance.setRepeatID(repeat.getID());
        }
        ItemsWorker.insert(items, context);
    }
    public static void setMaxDate(LocalDate date){
        maxDate = date;
    }
}
