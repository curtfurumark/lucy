package se.curtrune.lucy.workers;

import static se.curtrune.lucy.util.Logger.log;

import java.util.List;

import se.curtrune.lucy.classes.Item;

public class EstimateWorker {
    public static void estimate(List<Item> items){
        log("EstimateWorker(List<Item>");
        int energy = 0;
        long duration = 0;
        for( Item item: items){
            energy += item.getEnergy();
            duration += item.getDuration();
        }

    }
}
