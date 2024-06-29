package se.curtrune.lucy.workers;

import static se.curtrune.lucy.util.Logger.log;

import android.content.Context;

import java.time.LocalDate;
import java.util.List;

import se.curtrune.lucy.classes.MentalStats;
import se.curtrune.lucy.classes.Item;

public class DurationWorker {
    public static MentalStats getEstimate(Item item, Context context){
        log("StaticsWorker.getEstimate(Item, Context)", item.getHeading());
        if( !item.isTemplate()){
            return item.getEstimate();
        }
        List<Item> children = ItemsWorker.selectChildren(item, context);
        MentalStats result = new MentalStats();
        for(Item item1 : children){
            result.plusDuration(item1.getDuration());
            result.plusEnergy(item1.getEnergy());
        }
        return result.divide(children.size());
    }

    /**
     * ok, work to be done, if done get actual duration else get estimated duration
     * @param item, the item, what else
     * @param context, always the context
     * @return the estimated duration
     */

    public static long getEstimatedDuration(Item item, Context context){
        log("StaticsWorker.getEstimate(Item, Context)", item.getHeading());
        if( !item.isTemplate()){
            return item.getDuration();
        }
        long duration = 0;
        List<Item> children = ItemsWorker.selectChildren(item, context);
        //TODO, case first template, no children but user estimate available
        if( children.size() == 0){
            return 0;
        }
        for(Item item1 : children){
            duration += item1.getDuration();
        }
        return duration / children.size();
    }
    public static long getEstimatedDuration(LocalDate date, Context context){
        log("DurationWorker.getEstimate(LocalDate)", date.toString());
        List<Item> items = ItemsWorker.selectTodayList(date, context);
        long duration = 0;
        for(Item item:items){
            duration += getEstimatedDuration(item, context);
        }
        return duration;
    }

    public static long getEstimatedDuration(List<Item> items, Context context) {
        log("DurationWorker.getEstimatedDuration(List<Item>, Context)");
        long duration = 0;
        for(Item item: items){
            duration += getEstimatedDuration(item, context);
        }
        return duration;
    }
}
