package se.curtrune.lucy.viewmodel;

import static se.curtrune.lucy.util.Logger.log;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.dialogs.PostponeDialog;
import se.curtrune.lucy.workers.ItemsWorker;

public class AppointmentsViewModel extends ViewModel {
    private final MutableLiveData<List<Item>> mutableEvents = new MutableLiveData<>();
    private List<Item> items;
    public void init(Context context) {
        items = ItemsWorker.selectAppointments(context);
        mutableEvents.setValue(items);
    }

    public void add(Item item, Context context) {
        log("AppointmentsViewModel(Item, Context)");
        item = ItemsWorker.insert(item, context);
        items.add(item);
        sort();
        mutableEvents.setValue(items);
    }
    public void filter(String filter) {
        List<Item> filteredItems = items.stream().filter(item->item.contains(filter)).collect(Collectors.toList());
        //Collections.reverse();
        mutableEvents.setValue(filteredItems);
    }
    public LiveData<List<Item>> getEvents(){
        return mutableEvents;
    }
    public Item getItem(int index) {
        return mutableEvents.getValue().get(index);
    }

    public void delete(Item item, Context context) {
        log("AppointmentsViewModel.delete(Item, Context)", item.getHeading());
        boolean stat = ItemsWorker.delete(item, context);
        if( stat){
            stat = items.remove(item);
            if( !stat){
                log("ERROR removing item");
            }
            mutableEvents.setValue(items);
        }else{
            log("ERROR deleting item", item.getHeading());
        }
    }
    public void postpone(Item item, PostponeDialog.Postpone postpone, Context context) {
        log("AppointmentsViewModel(Item, Postpone, Context)");
        LocalDate date = LocalDate.now();
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
    public void setDone(boolean checked, Context context) {
        log("AppointmentsViewModel.setDone()");
    }
    private void sort(){
        log("...sort() not implemented");
        items.sort(Comparator.comparingLong(Item::compare).reversed());
    }

    public void update(Item item, Context context){
        log("AppointmentsViewModel.update(Item, Context)");
        int rowsAffected = ItemsWorker.update(item, context);
        if( rowsAffected != 1){
            log("ERROR updating item", item.getHeading());
        }
    }



}
