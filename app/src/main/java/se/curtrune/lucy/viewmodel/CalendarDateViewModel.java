package se.curtrune.lucy.viewmodel;

import static se.curtrune.lucy.util.Logger.log;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.time.LocalDate;
import java.util.List;

import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.calender.CalenderDate;
import se.curtrune.lucy.workers.ItemsWorker;

public class CalendarDateViewModel extends ViewModel {
    private MutableLiveData<List<Item>> mutableItems =  new MutableLiveData<>();
    private LocalDate date;

    public boolean delete(Item item){
        log("CalendarDateViewModel(Item)");
        return false;
    }
    public LiveData<List<Item>> getItems(){
        return mutableItems;
    }
    public void set(LocalDate date, Context context) {
        this.date = date;
        List<Item> items = ItemsWorker.selectItems(date, context);
        mutableItems.setValue(items);
    }

    public void set(CalenderDate calenderDate) {
        this.date = calenderDate.getDate();
        mutableItems.setValue(calenderDate.getItems());
    }
}
