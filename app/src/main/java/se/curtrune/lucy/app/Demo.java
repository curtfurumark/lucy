package se.curtrune.lucy.app;

import static se.curtrune.lucy.util.Logger.log;

import android.content.Context;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.Repeat;
import se.curtrune.lucy.classes.State;
import se.curtrune.lucy.classes.Type;
import se.curtrune.lucy.persist.LocalDB;
import se.curtrune.lucy.workers.ItemsWorker;

public class Demo {
    private static Settings settings;

    private static Item getAppointment(String heading, LocalDate date, LocalTime time){
        Item item = new Item(heading);
        item.setTargetDate(date);
        item.setTargetTime(time);
        item.setType(Type.APPOINTMENT);
        return item;
    }
    private static Item getRepeatItem(String heading, int days, LocalTime time){
        Item item = new Item(heading);
        item.setState(State.TODO);
        item.setIsTemplate(true);
        item.setTargetDate(LocalDate.now());
        item.setTargetTime(time);
        Repeat repeat = new Repeat();
        repeat.setDays(days);
        item.setPeriod(repeat);
        item.setParentId(settings.getRootID(Settings.Root.DAILY));
        return item;
    }
    private static Item getPanicItem(String heading){
        log("...getPanicItem(String)", heading);
        Item item = new Item(heading);
        item.setIsTemplate(true);
        item.setParentId(settings.getRootID(Settings.Root.PANIC));
        return item;
    }
    private static Item getProjectItem(String heading){
        Item item = new Item(heading);
        item.setState(State.TODO);
        item.setParentId(settings.getRootID(Settings.Root.PROJECTS));
        return item;
    }
    private static Item getTodoItem(String heading){
        Item item = new Item(heading);
        item.setState(State.TODO);
        return item;
    }
    public static void insertAppointments(Context context){
        log("...insertAppointments(Context)");
        Item root = ItemsWorker.getAppointmentsRoot(context);
        Item misaDev = getAppointment("misa dev", LocalDate.of(2024, 4, 26), LocalTime.of(10, 0));
        Item mayTheForce = getAppointment("may the 4h be with you", LocalDate.of(2024, 5, 4), LocalTime.of(0,0));
        LocalDB db = new LocalDB(context);
        db.setItemHasChild(root.getID(), true);
        root.setHasChild(true);
        db.insertChild(root, misaDev);
        db.insertChild(root, mayTheForce);
        log("...appointments inserted maybe");
    }
    public static void insertProjects(Context context){
        log("...insertProjects(Context)");
        LocalDB db = new LocalDB(context);
        Item projectsRoot = ItemsWorker.getRootItem(Settings.Root.PROJECTS, context);
        Item readStuff = getProjectItem("read stuff");
        readStuff = db.insertChild(projectsRoot, readStuff);
        Item wodehouse = new Item("wodehouse");
        wodehouse.setParentId(readStuff.getID());
        db.insertChild(readStuff, wodehouse);
        Item ukbridge = new Item("ukbridge");
        ukbridge.setParentId(wodehouse.getID());
        db.insertChild(wodehouse, ukbridge);
        Item moneyForNothing = new Item("money for nothing");
        moneyForNothing.setParentId(wodehouse.getID());
        db.insertChild(wodehouse, moneyForNothing);
    }
    public static void insertPanic(Context context){
        log("...insertPanic(Context)");
        LocalDB db = new LocalDB(context);
        Item panicRoot = ItemsWorker.getPanicRoot(context);
        db.insertChild(panicRoot, getPanicItem("breathe") );
        db.insertChild(panicRoot, getPanicItem("go for a walk") );
        db.insertChild(panicRoot,getPanicItem("read a book"));
    }
    public static void insertRepeatItems(Context context){
        log("...insertRepeatItems(Context)");
        LocalDB db = new LocalDB(context);
        Item root = ItemsWorker.getDailyRoot(context);
        db.insertChild( root, getRepeatItem("medicin am", 1, LocalTime.of(8, 0)));
        db.insertChild(root, getRepeatItem("borsta tänderna am", 1, LocalTime.of(8, 10)));
        db.insertChild(root,getRepeatItem("borsta tänderna pm", 1, LocalTime.of(21, 0)));
        db.insertChild(root, getRepeatItem("plocka", 0, LocalTime.of(0,0)));
        db.insertChild(root, getRepeatItem("promenad", 1, LocalTime.of(14, 0)));
        db.insertChild(root, getRepeatItem("vattna blommor", 3, LocalTime.of(11, 0)));
    }
    public static void insertDemo(Context context) throws SQLException {
        log("DBAdmin.insertDemo(Context)");
        settings = Settings.getInstance(context);
        insertRepeatItems(context);
        insertProjects(context);
        insertPanic(context);
        insertAppointments(context);
        //***********  insert project read stuff *************

        //*********** PANIC *********************

        //**********insert appointments*************//
/*        Item misaAdam =  getAppointment("misa workshop", LocalDate.of(2024, 4, 26), LocalTime.of(13, 0));
        db.insert(misaAdam);*/
    }
}
