package se.curtrune.lucy.dev;

import static se.curtrune.lucy.util.Logger.log;

import android.content.Context;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.Repeat;
import se.curtrune.lucy.dialogs.ImageDialog;
import se.curtrune.lucy.persist.DBAdmin;
import se.curtrune.lucy.persist.LocalDB;
import se.curtrune.lucy.util.Logger;
import se.curtrune.lucy.workers.ItemsWorker;
import se.curtrune.lucy.workers.RepeatWorker;

public class RepeatTest {
    public static void addColumnRepeatID(Context context){
        log("RepeatTest.addColumnRepeatID()");
        DBAdmin.addColumnRepeatIdToTableItems(context);
        listColumns("items", context);
    }
    public static void listColumns(String table, Context context){
        log("RepeatTest.listColumns(String table)");
        try(LocalDB db = new LocalDB(context)){
            db.getColumns(table);
        }
    }
    public static void createRepeatTable(Context context){
        log("RepeatTest.createRepeatTable()");
        DBAdmin.createRepeatTable(context);
    }
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
    public static void dropTableRepeat(Context context) {
        log("RepeatTest.dropTableRepeat()");
        DBAdmin.dropTableRepeat(context);
    }

    public static  void repeatTest01(Context context){
        log("...repeatTest01()");
        RepeatTest.createRepeatTable(context);
        //((printTableNames();
    }


    public static void insertRepeatTest(Context context) {
        log("RepeatTest.insertRepeatTest(Context)");
        Repeat repeat = new Repeat();
        repeat.setInfinity(true);
        repeat.setPeriod(1, Repeat.Unit.DAY);
        repeat.setFirstDate(LocalDate.now());
        repeat = ItemsWorker.insert(repeat, context);
        log(repeat);
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
    public static void selectRepeats(Context context){
        log("...selectRepeats()");
        try(LocalDB db = new LocalDB(context)){
            List<Repeat> repeats = db.selectRepeats();
            repeats.forEach(Logger::log);
            //repeats.stream().filter(Repeat::
            for(Repeat repeat: repeats){
                Item template = ItemsWorker.selectItem(repeat.getTemplateID(), context);
                if( template == null){
                    log("ERROR found repeat without template, continuing");
                    continue;
                }
                log("...will check template", template.getHeading());
                log("...isInfinite ", repeat.isInfinite());
                log("...lastDate", repeat.getLastDate());
                if( repeat.isInfinite() && repeat.hasLastDate() && repeat.getLastDate().isAfter(LocalDate.now().minusDays(30))){
                    log("log time to create new instances of this template", repeat.toString());
                }
            }
            //RepeatWorker.createInstances();
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
    public static void updateRepeat(Context context){
        log("RepeatTest.updateRepeat()");
        Repeat repeat = ItemsWorker.selectRepeat(1, context);
        if( repeat == null){
            log("ERROR selecting Repeat with id 1");
            return;
        }
        repeat.setPeriod(2, Repeat.Unit.DAY);
        int rowsAffected = ItemsWorker.update(repeat, context);
        if( rowsAffected != 1){
            log("ERROR updating repeat");
            return;
        }
        Repeat updatedRepeat = ItemsWorker.selectRepeat(1, context);
        if( updatedRepeat.getQualifier() != 2 || !updatedRepeat.getUnit().equals(Repeat.Unit.DAY)){
            log("SORRY TEST FAILED");
            log(updatedRepeat);
        }else{
            log("TEST OK!!!");
        }
    }
    public static void updateRepeats(Context context){
        log("RepeatTest.updateRepeats(Context)");
        RepeatWorker.updateRepeats(context);

    }
}
