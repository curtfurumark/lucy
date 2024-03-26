package se.curtrune.lucy.classes;

import static se.curtrune.lucy.util.Logger.log;

import java.time.LocalDate;
import java.util.List;

public class EstimateItems {

    private int energyEstimate;
    private long durationEstimate;
    public EstimateItems(List<Item> items) {
        log("EstimateDate(LocalDate)");
        calculateEstimate(items);
    }

    private void calculateEstimate(List<Item> items){
        log("...calculateEstimate(List<Item>");
        for( Item item: items){
            if( item.hasEstimate()){
                log("...item has estimate", item.getHeading());
                energyEstimate += item.getEstimate().getEnergy();
                durationEstimate += item.getEstimate().getDuration();
            }
        }
    }

    public long getDurationEstimate(){
        return durationEstimate;
    }
    public int  getEnergyEstimate() {
        return energyEstimate;
    }

}
