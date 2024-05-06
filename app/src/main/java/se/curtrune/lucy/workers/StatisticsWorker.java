package se.curtrune.lucy.workers;


import static se.curtrune.lucy.util.Logger.log;

import android.content.Context;

import java.time.LocalDate;
import java.util.List;

import se.curtrune.lucy.classes.Estimate;
import se.curtrune.lucy.classes.Item;


public class StatisticsWorker {


    public static void getEstimate(LocalDate currentDate, Context context) {
        //List<Item> items = ItemsWorker.selectItems}(currentDate, context);
    }
    public static Estimate getEstimate(Item item, Context context){
        log("StaticsWorker.getEstimate(Item, Context)", item.getHeading());
        if( !item.isTemplate()){
            return item.getEstimate();
        }
        List<Item> children = ItemsWorker.selectChildren(item, context);
        Estimate result = new Estimate();
        for(Item item1 : children){
            result.plusDuration(item1.getDuration());
            result.plusEnergy(item1.getEnergy());
        }
        return result.divide(children.size());
    }
}
