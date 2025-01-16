package se.curtrune.lucy.screens.dev;

import static se.curtrune.lucy.util.Logger.log;

import android.content.Context;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.Repeat;
import se.curtrune.lucy.workers.ItemsWorker;
import se.curtrune.lucy.workers.RepeatWorker;

public class RepeatTest {


    public static void createRepeatItemDecember(Context context){
        log("...createRepeatItemDecember()");
        Item item = new Item("30 days in december, repeat");
        Repeat repeat = new Repeat();
        repeat.setFirstDate(LocalDate.of(2024, 12, 1));
        repeat.setPeriod(1, Repeat.Unit.DAY);
        repeat.setInfinity(true);
        item.setRepeat(repeat);
        ItemsWorker.insert(item, context);
    }
    public static void listRepeats(Context context){
        log("...listRepeats(Context)");
        List<Repeat> repeats = ItemsWorker.selectRepeats(context);
        for(Repeat repeat: repeats){
            Item template = ItemsWorker.selectItem(repeat.getTemplateID(), context);
            if( template == null){
                log("no template for repeat with id", repeat.getID());
                continue;
            }
            log("template: ", template.getHeading());
            log(repeat);
        }
    }

    public static void listInstances(long repeatID, Context context){
        log("RepeatTest.listInstances(long, Context) repeatID", repeatID);
        Repeat repeat = ItemsWorker.selectRepeat(repeatID, context);
        if( repeat == null){
            log("ERROR repeat is null, i surrender");
            return;
        }
        log("...templateID", repeat.getTemplateID());
        Item template = ItemsWorker.selectItem(repeat.getTemplateID(), context);
        if( template == null){
            log("template not fond, i  surrender");
            return;
        }
        List<Item> instances = ItemsWorker.selectChildren(template, context);
        log("...number of instances", instances.size());
        for( Item item: instances){
            log(String.format(Locale.getDefault(), "%s %s", item.getHeading(), item.getTargetDate().toString()));
        }
    }

    public static void selectRepeat(Context context) {
        log("RepeatTest.selectRepeat(Context)");
        Repeat repeat = ItemsWorker.selectRepeat(1, context);
        if(repeat != null){
            log(repeat);
        }else{
            log("ERROR repeat is null");
        }
    }

    public static void setLastDate(long id, LocalDate lastDate, Context context){
        log("RepeatTest.setLastDate(long, LocalDate, Context)");
        Repeat repeat = ItemsWorker.selectRepeat(id, context);
        if( repeat == null){
            log("ERROR no repeat found with id", id);
            return;
        }
        repeat.setLastDate(lastDate);
        int rowsAffected = ItemsWorker.update(repeat, context);
        log("...rows Affected", rowsAffected);

    }

    public static void updateRepeats(Context context){
        log("RepeatTest.updateRepeats(Context)");
        RepeatWorker.updateRepeats(context);

    }
}
