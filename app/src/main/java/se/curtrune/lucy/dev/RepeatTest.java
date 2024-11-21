package se.curtrune.lucy.dev;

import static se.curtrune.lucy.util.Logger.log;

import android.content.Context;

import java.time.LocalDate;
import java.util.List;

import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.Repeat;
import se.curtrune.lucy.persist.DBAdmin;
import se.curtrune.lucy.persist.LocalDB;
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

    public static void selectRepeat(Context context) {
        log("RepeatTest.selectRepeat(Context)");
        Repeat repeat = ItemsWorker.selectRepeat(1, context);
        if(repeat != null){
            log(repeat);
        }else{
            log("ERROR repeat is null");
        }
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
}
