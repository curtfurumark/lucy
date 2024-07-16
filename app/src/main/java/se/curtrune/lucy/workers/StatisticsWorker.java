package se.curtrune.lucy.workers;


import static se.curtrune.lucy.util.Logger.log;

import android.content.Context;

import java.time.LocalDate;
import java.util.List;

import se.curtrune.lucy.classes.MentalStats;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.Mental;
import se.curtrune.lucy.persist.LocalDB;


public class StatisticsWorker {
    public static boolean VERBOSE = false;
    public static MentalStats getMentalStatsTemplate(Item item, Context context){
        log("...getMentalStatsTemplate(Item , Context)");
        List<Item> children = ItemsWorker.selectChildren(item, context);
        List<Mental> mentals = MentalWorker.selectMentals(children, context);
        return new MentalStats(mentals);
    }

    public static MentalStats getEstimate(Item item, Context context){
        log("StaticsWorker.getEstimate(Item, Context)", item.getHeading());
        if( !item.isTemplate()){
            return item.getEstimate();
        }
        List<Item> children = ItemsWorker.selectChildren(item, context);
        List<Mental> mentals = MentalWorker.getMentals(children, context);
        MentalStats result = new MentalStats();
        log("...number of (child) mentals", mentals.size());
        mentals.forEach(System.out::println);
        for(Item item1 : children){
            result.plusEnergy(item1.getEnergy());
        }
        return result.divide(children.size());
    }

    public static MentalStats getMentalStats(Item item, Context context) {
        log("StatisticsWorker.getMentalStats(Item, Context)", item.getHeading());
        if( item.isTemplate()){
            return getMentalStatsTemplate(item, context);
        }
        return new MentalStats(item.getMental());
    }
}
