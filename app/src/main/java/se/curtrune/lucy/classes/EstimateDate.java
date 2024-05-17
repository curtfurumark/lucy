package se.curtrune.lucy.classes;

import static se.curtrune.lucy.util.Logger.log;

import android.content.Context;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import se.curtrune.lucy.workers.ItemsWorker;
import se.curtrune.lucy.workers.MentalWorker;

public class EstimateDate {
    private LocalDate date;
    private List<Item> items;
    private List<Estimate> estimates;
    private long durationEstimate;
    public EstimateDate(){
        estimates = new ArrayList<>();

    }
    public EstimateDate(LocalDate date, Context context) {
        this();
        log("EstimateDate(LocalDate)");
        this.date = date;
        calculate(date, context);
    }
    public EstimateDate(List<Item> items){
        log("EstimateDate(List<Item>) ");

    }
    private void calculate(LocalDate date, Context context){
        List<Item> items = ItemsWorker.selectTodayList(date, context);
        for( Item item: items){
            if( item.isTemplate()){

            }else{
                Mental mental = MentalWorker.getMental(item, context);
            }
        }
        for( Item item: items) {
            estimates.add(item.getEstimate());
        }
    }
    public int getAnxiety(){
        return estimates.stream().mapToInt(Estimate::getAnxiety).sum();
    }

    public LocalDate getDate() {
        return date;
    }

    public long getDurationEstimate(){
        return estimates.stream().mapToLong(Estimate::getDuration).sum();
    }
    public int  getEnergyEstimate() {
        return estimates.stream().mapToInt(Estimate::getEnergy).sum();
    }
    public int getStressEstimate(){
        return estimates.stream().mapToInt(Estimate::getStress).sum();
    }
    public int getMoodEstimate(){
        return estimates.stream().mapToInt(Estimate::getMood).sum();
    }

}
