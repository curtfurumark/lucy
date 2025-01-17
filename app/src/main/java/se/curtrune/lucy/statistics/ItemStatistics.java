package se.curtrune.lucy.statistics;

import android.content.Context;

import java.util.List;

import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.persist.ItemsWorker;

public class ItemStatistics {
    private List<Item> items;
    public ItemStatistics(Item item, Context context){
        if( item.isTemplate()){
            items = ItemsWorker.selectChildren(item, context);
        }
    }
    public float getEnergyAverage(){
        return 0.0f;
    }
}
