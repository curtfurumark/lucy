package se.curtrune.lucy.workers;

import static se.curtrune.lucy.util.Logger.log;

import android.content.Context;

import java.util.List;

import se.curtrune.lucy.classes.Item;

public class DurationWorker {
    public static boolean VERBOSE = false;
    /**
     * returns an estimated duration based on previous uses of this template
     * checks for template children, adds their actual duration and divides by number of children
     * @param template, the item for which you wish to get a duration estimate
     * @param context, jkjkjkjikjk
     * @return duration estimate in seconds
     */
    public static long calculateTemplateDuration(Item template, Context context){
        log("DurationWorker.calculateTemplateDuration()");
        long duration = 0;
        List<Item> children = ItemsWorker.selectChildren(template, context);
        //template that never has been actualized
        if( children.size() == 0){
            return template.getEstimatedDuration();
        }
        for( Item child: children){
            duration += child.getDuration();
        }
        return duration / children.size();
    }
/*    public static MentalStats getEstimate(Item item, Context context){
        log("Duration.getEstimate(Item, Context)", item.getHeading());
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
    }*/

    /**
     * calculates an estimated duration based on factors such as whether the item is a template
     * and whether the item is done or not
     * @param item, the item, what else
     * @param context, always the context
     * @return the estimated duration
     */

    public static long getEstimatedDuration(Item item, Context context){
        log("StaticsWorker.getEstimate(Item, Context)", item.getHeading());
        if( item.isTemplate()){
            return calculateTemplateDuration(item, context);
        }
        if(item.isDone()){
            return item.getDuration();
        }
        return item.getEstimatedDuration();
    }

    /**
     * if done actual duration,
     * if not done, estimatedDuration if there is such
     * or if template, makes an estimate based on previous instantiations of the template
     * @param items items
     * @param context context
     * @return number of seconds
     */
    public static long getEstimatedDuration(List<Item> items, Context context) {
        log("DurationWorker.getEstimatedDuration(List<Item>, Context)");
        long duration = 0;
        for(Item item: items){
            duration += getEstimatedDuration(item, context);
        }
        return duration;
    }
}
