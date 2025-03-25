package se.curtrune.lucy.app;

import static se.curtrune.lucy.util.Logger.log;

import android.content.Context;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

import se.curtrune.lucy.LucindaApplication;
import se.curtrune.lucy.classes.item.Item;
import se.curtrune.lucy.classes.State;
import se.curtrune.lucy.classes.Type;
import se.curtrune.lucy.persist.Repository;
import se.curtrune.lucy.persist.SqliteLocalDB;
import se.curtrune.lucy.persist.ItemsWorker;

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
/*        item.setIsCalenderItem(true);
        Repeat repeat = new Repeat();
        repeat.setPeriod(days, Repeat.Unit.DAY);
        item.setRepeat(repeat);*/
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
        Repository repository = LucindaApplication.repository;
        Item root = repository.getAppointmentsRoot();
        Item misaDev = getAppointment("misa dev", LocalDate.of(2024, 4, 26), LocalTime.of(10, 0));
        Item mayTheForce = getAppointment("may the 4h be with you", LocalDate.of(2024, 5, 4), LocalTime.of(0,0));
        SqliteLocalDB db = new SqliteLocalDB(context);
        db.setItemHasChild(root.getID(), true);
        root.setHasChild(true);
        db.insertChild(root, misaDev);
        db.insertChild(root, mayTheForce);
        log("...appointments inserted maybe");
    }
    public static void insertProjects(Context context){
        log("...insertProjects(Context)");
        createReadingList(context);
        createShoppingList(context);
        createMiddagsTips(context);
    }
    public static void createReadingList(Context context){
        //println("...createReadingList(Context)");
/*        SqliteLocalDB db = new SqliteLocalDB(context);
        Item projectsRoot = ItemsWorker.getRootItem(Settings.Root.PROJECTS, context);
        Item readingList = getProjectItem("reading list");
        readingList = db.insertChild(projectsRoot, readingList);
        Item wodehouse = new Item("wodehouse");
        wodehouse.setParentId(readingList.getID());
        db.insertChild(readingList, wodehouse);
        Item ukbridge = new Item("ukbridge");
        ukbridge.setParentId(wodehouse.getID());
        db.insertChild(wodehouse, ukbridge);
        Item moneyForNothing = new Item("money for nothing");
        moneyForNothing.setParentId(wodehouse.getID());
        db.insertChild(wodehouse, moneyForNothing);

        Item douglas = getProjectItem("douglas adams");
        db.insertChild(readingList, douglas);
        Item hitchHiker = getProjectItem("hitch hikers guide to the galaxy");
        db.insertChild(douglas, hitchHiker);
        Item fish = getProjectItem("so long and thanks for all the fish");
        db.insertChild(douglas, fish);
        Item restaurant = getProjectItem("the restaurant at the end of the universe");
        db.insertChild(douglas, restaurant);*/
    }
    public static void createMiddagsTips(Context context){
        log("...createMiddagsTips()");
/*        Item projects = ItemsWorker.getProjectsRoot(context);
        Item middagsRoot = new Item("middagstips");
        middagsRoot.setParent(projects);
        ItemsWorker.insertChild(projects, middagsRoot, context);

        Item pastaCarbonara = new Item("past carbonara");
        ItemsWorker.insertChild(middagsRoot, pastaCarbonara, context);

        //INGREDIENTS
        Item carbonaraIngredients = new Item("ingredienser");
        ItemsWorker.insertChild(pastaCarbonara, carbonaraIngredients, context);

        Item bacon = new Item("bacon");
        Item pasta = new Item("pasta");
        Item egg = new Item("ägg");
        Item parmesan = new Item("parmesan");
        Item blackPepper = new Item("svartpeppar");

        ItemsWorker.insertChild(carbonaraIngredients, bacon, context);
        ItemsWorker.insertChild(carbonaraIngredients, pasta, context);
        ItemsWorker.insertChild(carbonaraIngredients, egg, context);
        ItemsWorker.insertChild(carbonaraIngredients, parmesan, context);
        ItemsWorker.insertChild(carbonaraIngredients, blackPepper, context);
        //RECIPES, TODO
        Item recipe = new Item("recept");
        ItemsWorker.insertChild(pastaCarbonara, recipe, context);

        Item step1 = new Item("1. koka pastan");
        Item step2 = new Item("2. stek bacon");
        Item step3 = new Item("3. rör ihop äggulor och parmesan");*/

    }
    public static void createShoppingList(Context context){
        log("Demo.createShoppingList()");
/*        try(SqliteLocalDB db = new SqliteLocalDB(context)){
            Item projects = ItemsWorker.getProjectsRoot(context);
            Item shoppingList = getProjectItem("shopping list");
            db.insertChild(projects, shoppingList);
            Item snus = getProjectItem("snus");
            Item eggs = getProjectItem("ägg");
            Item flour =  getProjectItem("mjöl");
            Item milk = getProjectItem("mjölk");
            Item butter = getProjectItem("smör");
            Item salt = getProjectItem("salt");
            Item jam = getProjectItem("sylt");
            db.insertChild(shoppingList, snus);
            db.insertChild(shoppingList, eggs);
            db.insertChild(shoppingList, flour);
            db.insertChild(shoppingList, milk);
            db.insertChild(shoppingList, butter);
            db.insertChild(shoppingList, salt);
            db.insertChild(shoppingList, jam);
        }*/
    }
    public static void insertPanic(Context context){
        log("...insertPanic(Context)");
/*        SqliteLocalDB db = new SqliteLocalDB(context);
        Item panicRoot = ItemsWorker.getPanicRoot(context);
        db.insertChild(panicRoot, getPanicItem("breathe") );
        db.insertChild(panicRoot, getPanicItem("go for a walk") );
        db.insertChild(panicRoot,getPanicItem("read a book"));*/
    }
    public static void insertRepeatItems(Context context){
        log("...insertRepeatItems(Context)");
/*        SqliteLocalDB db = new SqliteLocalDB(context);
        Item root = ItemsWorker.getDailyRoot(context);
        db.insertChild( root, getRepeatItem("medicin am", 1, LocalTime.of(8, 0)));
        db.insertChild(root, getRepeatItem("borsta tänderna am", 1, LocalTime.of(8, 10)));
        db.insertChild(root,getRepeatItem("borsta tänderna pm", 1, LocalTime.of(21, 0)));
        db.insertChild(root, getRepeatItem("plocka", 0, LocalTime.of(0,0)));
        db.insertChild(root, getRepeatItem("promenad", 1, LocalTime.of(14, 0)));
        db.insertChild(root, getRepeatItem("vattna blommor", 3, LocalTime.of(11, 0)));*/
    }
    public static void insertDemo(Context context) throws SQLException {
        log("DBAdmin.insertDemo(Context)");
        settings = Settings.getInstance(context);
        insertRepeatItems(context);
        insertProjects(context);
        insertPanic(context);
        insertAppointments(context);
    }
}
