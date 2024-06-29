package se.curtrune.lucy.workers;


import static se.curtrune.lucy.util.Logger.log;

import android.content.Context;

import java.time.LocalDate;
import java.util.List;

import se.curtrune.lucy.classes.MentalStats;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.Mental;


public class StatisticsWorker {


    public static void getEstimate(LocalDate currentDate, Context context) {
        //List<Item> items = ItemsWorker.selectItems}(currentDate, context);
    }
    public static MentalStats getEstimate(Item item, Context context){
        log("StaticsWorker.getEstimate(Item, Context)", item.getHeading());
        if( !item.isTemplate()){
            return item.getEstimate();
        }
        List<Item> children = ItemsWorker.selectChildren(item, context);
        MentalStats result = new MentalStats();
        for(Item item1 : children){
            Mental mental = MentalWorker.getMental(item1, context);
            result.plusDuration(item1.getDuration());
            result.plusEnergy(item1.getEnergy());
            //result.plusAnxiety(item1.get)
        }
        return result.divide(children.size());
    }
}
