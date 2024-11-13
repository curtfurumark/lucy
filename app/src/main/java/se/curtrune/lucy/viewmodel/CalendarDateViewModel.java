package se.curtrune.lucy.viewmodel;

import static se.curtrune.lucy.util.Logger.log;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.calender.CalenderDate;
import se.curtrune.lucy.dialogs.PostponeDialog;
import se.curtrune.lucy.workers.CalenderWorker;
import se.curtrune.lucy.workers.ItemsWorker;
import se.curtrune.lucy.workers.MentalWorker;

public class CalendarDateViewModel extends ViewModel {
    private MutableLiveData<List<Item>> mutableItems =  new MutableLiveData<>();
    private MutableLiveData<String> mutableError = new MutableLiveData<>();
    private List<Item> items;
    private Item currentParent;
    private LocalDate date;
    public static boolean VERBOSE = false;

    public void add(Item item, Context context) {
        log("CalendarDateViewModel.add(Item, Context)", item.getHeading());
        item = ItemsWorker.insert(item, context);
        if(VERBOSE)log(item);
        items.add(item);
        sort();
        mutableItems.setValue(items);
    }

    public boolean delete(Item item, Context context){
        log("CalendarDateViewModel.delete(Item, Context)", item.getHeading());
        boolean stat = ItemsWorker.delete(item, context);
        if(!stat){
            log("ERROR deleting item", item.getHeading());
            return false;
        }
        items.remove(item);
        mutableItems.setValue(items);
        return true;
    }
    public Item getCurrentParent() {
        return currentParent;
    }
    public Item getItem(int index) {
        log("CalendarDateViewModel.getItem(int)", index);
        return mutableItems.getValue().get(index);
    }
    public LiveData<List<Item>> getItems(){
        return mutableItems;
    }
    public void selectGenerated(Item parent, Context context) {
        List<Item> items = ItemsWorker.selectTemplateChildren(parent, context);
        mutableItems.setValue(items);
    }
    public void set(LocalDate date, Context context) {
        log("CalendarDateViewModel.set(LocalDate, Context)");
        this.date = date;
        if( date.equals(LocalDate.now())) {
            items = ItemsWorker.selectTodayList(date, context);
        }else{
            items = ItemsWorker.selectItems(date, context);
        }
        sort();
        mutableItems.setValue(items);
    }
    public void setParent(Item parent, Context context) {
        log("CalendarDateViewModel.setParent(Item, Context)");
        this.currentParent = parent;
        items = ItemsWorker.selectChildren(parent, context);
        mutableItems.setValue(items);
    }
    public void sort(){
        log("CalendarDateViewModel.sort()");
        items.sort(Comparator.comparingLong(Item::compareTargetTime));
    }

    public void set(CalenderDate calenderDate) {
        log("CalendarDateViewModel.set(CalendarDate)");
        this.date = calenderDate.getDate();
        this.items = calenderDate.getItems();
        mutableItems.setValue(items);
    }
    public void setEnergyItems(){
        log("CalendarDateViewModel.setEnergyItems()");
        List<Item> colouredItems = new ArrayList<>();
        int currentEnergy = 0;
        for(int i = 0; i < items.size(); i++){
            Item item = items.get(i);
            currentEnergy += item.getEnergy();
            item.setColor(CalenderWorker.getEnergyColor(currentEnergy));
            colouredItems.add(item);
        }
        mutableItems.setValue(colouredItems);
    }



    public void postpone(Item item, PostponeDialog.Postpone postpone, LocalDate date, Context context) {
        switch (postpone){
            case ONE_HOUR:
                LocalTime targetTime = item.getTargetTime();
                item.setTargetTime(targetTime.plusHours(1));
                items.sort(Comparator.comparingLong(Item::compareTargetTime));
                break;
            case ONE_DAY:
                item.setTargetDate(date.plusDays(1));
                items.remove(item);
                break;
            case ONE_WEEK:
                item.setTargetDate(date.plusWeeks(1));
                items.remove(item);
                break;
            case ONE_MONTH:
                item.setTargetDate(date.plusMonths(1));
                items.remove(item);
                break;
        }
        update(item, context);
    }

    public void filter(String filter) {
        log("CalendarDateViewModel.filter(String)");
        mutableItems.setValue(items.stream().filter(item->item.contains(filter)).collect(Collectors.toList()));
    }
    public boolean update(Item item, Context context){
        log("CalendarDateViewModel.update(Item)", item.getHeading());
        int rowsAffected = ItemsWorker.update(item, context);
        if( rowsAffected != 1){
            log("ERROR updating item");
            return false;
        }
        set(date, context);
        return true;
    }



}
