package se.curtrune.lucy.statistics;

import static se.curtrune.lucy.util.Logger.log;

import android.content.Context;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    private List<Mental> mentals;
    private List<Listable> categoryListables = new ArrayList<>();
    private List<Listable> dateListables = new ArrayList<>();

    public DurationStatistics(LocalDate firstDate, LocalDate lastDate, Context context) {
        this.firstDate = firstDate;
        this.lastDate = lastDate;
        init(firstDate, lastDate, context);
        createCategoryListables(context);
        createDateListables();
    }
    private void createCategoryListables(Context context){
        log("createCategoryListables(List<Item>");
        LocalDB localDB = new LocalDB(context);
        String[] categories = localDB.getCategories();
        for(String category : categories){
            List<Item> categoryItems = items.stream().filter(item->item.isCategory(category)).collect(Collectors.toList());
            List<Mental> categoryMentals = mentals.stream().filter(mental->mental.isCategory(category)).collect(Collectors.toList());
/*            log("category", category);
            log("number of mentals", categoryMentals.size());
            log("...number of items", categoryItems.size());*/
            categoryListables.add(new CategoryListable(category, categoryItems, categoryMentals));
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
    private void init(LocalDate firstDate, LocalDate lastDate, Context context){
        log("DurationStatistics.init(LocalDate,LocalDate, Context");
        LocalDB db = new LocalDB(context);
        String queery = Queeries.selectItems(firstDate, lastDate, State.DONE);
        items =  db.selectItems(queery);
        log("...items on parade");
        items.forEach(Logger::log);
        queery = Queeries.selectMentals(firstDate, lastDate, false);
        mentals = db.selectMentals(queery);
    }

    public long getTotalDuration() {
        log("DurationStatistics.getTotalDuration()");
        return items.stream().mapToLong(Item::getDuration).sum();
    }
}
