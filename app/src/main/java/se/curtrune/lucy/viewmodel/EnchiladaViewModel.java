package se.curtrune.lucy.viewmodel;

import static se.curtrune.lucy.util.Logger.log;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.stream.Collectors;

import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.workers.ItemsWorker;

public class EnchiladaViewModel extends ViewModel {
    private List<Item> items;
    private MutableLiveData<List<Item>> mutableItems = new MutableLiveData<>();
    public EnchiladaViewModel(){
        log("EnchiladaViewModel()");
    }
    public void filter(String filter) {
        List<Item> filteredItems = items.stream().filter(item->item.contains(filter)).collect(Collectors.toList());
        mutableItems.setValue(filteredItems);
    }
    public Item getItem(int position) {
        return items.get(position);
    }
    public LiveData<List<Item>> getItems(){
        return mutableItems;
    }
    public void init(Context context){
        log("...init()");
        items = ItemsWorker.selectItems(context);
        mutableItems.setValue(items);
    }


    public boolean delete(Item item, Context context) {
        log("EnchiladaViewModel.delete(Item, Context)");
        boolean stat = ItemsWorker.delete(item, context);
        if( stat) {
            items.remove(item);
            mutableItems.setValue(items);
        }else{
            log("ERROR deleting item", item.getHeading());
        }
        return stat;
    }
}
