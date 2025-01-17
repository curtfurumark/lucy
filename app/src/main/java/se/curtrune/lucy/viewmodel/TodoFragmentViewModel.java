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
import se.curtrune.lucy.classes.State;
import se.curtrune.lucy.dialogs.PostponeDialog;
import se.curtrune.lucy.persist.ItemsWorker;

public class TodoFragmentViewModel extends ViewModel {
    private List<Item> items;
    private MutableLiveData<List<Item>> mutableItems = new MutableLiveData<>();


    public void delete(Item item, Context context) {
        log("TodoFragmentViewModel(Item, Context)", item.getHeading());
        boolean stat = ItemsWorker.delete(item, context);
        if( !stat){
            log("ERROR deleting item");
        }else{
            stat = mutableItems.getValue().remove(item);
            if( !stat){
                log("ERROR removing item form mutableItems");
            }
        }
    }
    public void filter(String filter){
        List<Item> filteredItems = new ArrayList<>();
        filteredItems = items.stream().filter(item->item.contains(filter)).collect(Collectors.toList());
        mutableItems.setValue(filteredItems);
    }
    public Item getItem(int index   ) {
        return mutableItems.getValue().get(index);
    }
    public LiveData<List<Item>> getItems(){
        return mutableItems;
    }

    public void init(Context context){
        items = ItemsWorker.selectItems(State.TODO, context);
        items.sort(Comparator.comparingLong(Item::compare));
        mutableItems.setValue(items);
    }

    public void insert(Item item, Context context) {
        log("TodoFragmentViewModel.insert(Item, Context)");
        item = ItemsWorker.insert(item, context);
        mutableItems.getValue().add(item);
        sort();
    }
    private void sort(){
        mutableItems.getValue().sort(Comparator.comparingLong(Item::compare));
    }
    public void postpone(Item item, PostponeDialog.Postpone postpone, Context context) {
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
    public boolean update(Item item, Context context){
        log("CalendarDateViewModel.update(Item)", item.getHeading());
        int rowsAffected = ItemsWorker.update(item, context);
        if( rowsAffected != 1){
            log("ERROR updating item");
            return false;
        }
        return true;
    }
}
