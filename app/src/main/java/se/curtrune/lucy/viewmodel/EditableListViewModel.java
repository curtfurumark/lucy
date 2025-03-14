package se.curtrune.lucy.viewmodel;


import static se.curtrune.lucy.util.Logger.log;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import se.curtrune.lucy.classes.item.Item;
import se.curtrune.lucy.persist.ItemsWorker;


public class EditableListViewModel extends ViewModel {
    private Item parent;
    private final MutableLiveData<List<Item>> mutableItems = new MutableLiveData<>();
    public void setParent(Item parent){
        this.parent = parent;
    }
    public void init(Item parent){
        log("EditableListViewModel.init(Item)", parent.getHeading());
        this.parent = parent;
        mutableItems.setValue(new ArrayList<>());
        mutableItems.getValue().add(createChild(parent));
        log("number of items", mutableItems.getValue().size());
    }
    public void addEmptyItem(int index) {
        log("...addEmptyItem(int)", index);
        mutableItems.getValue().add(index, createChild(parent));
    }

    public LiveData<List<Item>> getItems(){
        log("EditableListViewModel.getItems()");
        return mutableItems;
    }

    public void saveAll(Context context){
        log("...saveAll()");
        List<Item> items = mutableItems.getValue();
        for(Item item: items){
            if( !item.getHeading().isEmpty()){
                ItemsWorker.insertChild(parent, item, context);
            }
        }
    }


    public void setHeading(String heading, int index, Context context){
        log("...setHeading(String, int, Context)", heading);
        mutableItems.getValue().get(index).setHeading(heading);
    }
    private Item createChild(Item parent){
        log("...createChild(Item)");
        Item child = new Item();
        child.setParent(parent);
        child.setCategory(parent.getCategory());
        return child;
    }
}
