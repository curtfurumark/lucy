package se.curtrune.lucy.viewmodel;

import static se.curtrune.lucy.util.Logger.log;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import se.curtrune.lucy.adapters.MentalAdapter;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.Mental;
import se.curtrune.lucy.classes.State;
import se.curtrune.lucy.workers.ItemsWorker;

public class MentalDateViewModel  extends ViewModel {
    private MutableLiveData<List<Item>> mutableItems = new MutableLiveData<>();
    private List<Item> items;
    public void setDate(LocalDate date, Context context) {
        log("MentalDateViewModel.setDate(LocalDate, Context)", date.toString());
        items = ItemsWorker.selectItems(date, State.DONE ,context);
        items.sort(Comparator.comparingLong(Item::compareTargetTime));
        mutableItems.setValue(items);
    }
    public LiveData<List<Item>> getItems(){
        return mutableItems;
    }

    public void updateItem(Item item, MentalAdapter.MentalType type, int progress, Context  context) {
        log("MentalDateViewModel.updateItem(Item, MentalType, int)");
        switch (type){
            case ENERGY:
                item.setEnergy(progress -5);
                break;
            case ANXIETY:
                item.setAnxiety(progress -5);
                break;
            case STRESS:
                item.setStress(progress - 5);
                break;
            case MOOD:
                item.setMood(progress -5);
                break;
        }
        int rowsAffected = ItemsWorker.update(item, context);
        if( rowsAffected != 1){
            log("ERROR updating item");
        }else{
            log("update of item ok");
        }
    }
}
