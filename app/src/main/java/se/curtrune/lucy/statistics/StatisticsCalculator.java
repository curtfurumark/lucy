package se.curtrune.lucy.statistics;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.Mental;
import se.curtrune.lucy.persist.LocalDB;
import se.curtrune.lucy.persist.Queeries;

public class StatisticsCalculator {

    public static int getAnxiety(List<Item> items, Context context){
        //return items.stream().mapToInt(Item::getAnxiety);
        LocalDB db = new LocalDB(context);
        List<Mental> mentals = new ArrayList<>();
        for( Item item: items){
            String queery = Queeries.selectMental(item);
            mentals.add( db.selectMental(queery));
        }
        return mentals.stream().mapToInt(Mental::getAnxiety).sum();
    }
    public static long getDuration(List<Item> items){
        return items.stream().mapToLong(Item::getDuration).sum();
    }
/*    public static int getEnergy(List<Item> items){
        return items.stream().mapToInt(Item::getEnergy).sum();
    }*/
    public static int getEnergy(List<Mental> mentals){
        return mentals.stream().mapToInt(Mental::getEnergy).sum();
    }
    public static float getAverageEnergy(List<Mental> mentals){
        if( mentals.size() == 0){
            return 0;
        }
        return getEnergy(mentals) / mentals.size();
    }
}
