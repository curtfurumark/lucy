package se.curtrune.lucy.app;

import static se.curtrune.lucy.util.Logger.log;

import android.content.Context;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.Repeat;
import se.curtrune.lucy.classes.State;
import se.curtrune.lucy.persist.LocalDB;
import se.curtrune.lucy.workers.ItemsWorker;

public class Demo {
    private static Settings settings;
    private static Item getAppointment(String heading, LocalDate date, LocalTime time){
        Item item = new Item(heading);
        item.setTargetDate(date);
        item.setTargetTime(time);
        return item;
    }
    private static Item getInfiniteItem(String heading, int days){
        Item item = new Item(heading);
        item.setState(State.INFINITE);
        item.setIsTemplate(true);
        Repeat repeat = new Repeat();
        repeat.setDays(1);
        item.setPeriod(repeat);
        item.setParentId(settings.getRootID(Settings.Root.DAILY));
        return item;
    }
    private static Item getProjectItem(String heading){
        Item item = new Item(heading);
        item.setState(State.WIP);
        item.setParentId(settings.getRootID(Settings.Root.PROJECTS));
        return item;
    }
    private static Item getTodoItem(String heading){
        Item item = new Item(heading);
        item.setState(State.TODO);
        return item;
    }
    public static void insertDemo(Context context) throws SQLException {
        log("DBAdmin.insertDemo(Context)");
        settings = Settings.getInstance(context);
        LocalDB db = new LocalDB(context);
        db.insert(getInfiniteItem("medicin am", 1));
        db.insert(getInfiniteItem("borsta tänderna am", 1));
        db.insert(getInfiniteItem("borsta tänderna pm", 1));
        db.insert(getInfiniteItem("plocka", 0));
        db.insert(getInfiniteItem("promenad", 1));
        db.insert(getInfiniteItem("vattna blommor", 3));
        //***********  insert project read stuff *************
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
        //**********insert appointments*************//
        Item misaAdam =  getAppointment("adam", LocalDate.of(2024, 3, 1), LocalTime.of(13, 0));
        db.insert(misaAdam);
    }
}
