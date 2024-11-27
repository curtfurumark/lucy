package se.curtrune.lucy.statistics;

import static se.curtrune.lucy.util.Logger.log;

import android.content.Context;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import se.curtrune.lucy.app.User;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.Listable;
import se.curtrune.lucy.classes.Mental;
import se.curtrune.lucy.classes.State;
import se.curtrune.lucy.persist.LocalDB;
import se.curtrune.lucy.persist.Queeries;
import se.curtrune.lucy.util.Logger;

public class DurationStatistics {
    private LocalDate firstDate;
    private LocalDate lastDate;
    private List<Item> items;
    private List<Listable> categoryListables = new ArrayList<>();
    private List<Listable> dateListables = new ArrayList<>();
    public static boolean VERBOSE = false;

    public DurationStatistics(LocalDate firstDate, LocalDate lastDate, Context context) {
        log("DurationStatistics(LocalDate, LocalDate, Context)");
        this.firstDate = firstDate;
        this.lastDate = lastDate;
        init(firstDate, lastDate, context);
        createCategoryListables(context);
        createDateListables();
    }
    private void createCategoryListables(Context context){
        log("DurationStatistics.createCategoryListables(Context))");
        String[] categories = User.getCategories(context);
        for(String category : categories){
            List<Item> categoryItems = items.stream().filter(item->item.isCategory(category)).collect(Collectors.toList());
            categoryListables.add(new CategoryListable(category, categoryItems));
        }
    }
    private void createDateListables(){
        log("...createDateListables()");
        LocalDate currentDate = firstDate;
        do{
            LocalDate finalCurrentDate = currentDate;
            List<Item> dateItems = items.stream().filter(item->item.isUpdated(finalCurrentDate)).collect(Collectors.toList());
            dateListables.add(new DateListable(currentDate, dateItems));
            currentDate = currentDate.plusDays(1);
        }while( !currentDate.isAfter(lastDate));


    }
    public List<Listable> getCategoryListables(){
        return categoryListables;
    }
    public List<Listable> getDateListables(){
        return dateListables;
    }
    public List<Item> getItems(){
        return items;
    }
    private void init(LocalDate firstDate, LocalDate lastDate, Context context){
        log("DurationStatistics.init(LocalDate,LocalDate, Context");
        try(LocalDB db = new LocalDB(context)) {
            String queery = Queeries.selectItems(firstDate, lastDate, State.DONE);
            items = db.selectItems(queery);
            if (VERBOSE) {
                log("...items on parade");
                items.forEach(Logger::log);
            }
        }
    }

    public long getTotalDuration() {
        log("DurationStatistics.getTotalDuration()");
        return items.stream().mapToLong(Item::getDuration).sum();
    }
}
