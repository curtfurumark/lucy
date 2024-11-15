package se.curtrune.lucy.classes;

import static se.curtrune.lucy.util.Logger.log;

import android.content.Context;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import se.curtrune.lucy.workers.ItemsWorker;

@Deprecated
public class EstimateDate {
    private LocalDate date;
    private List<Item> items;
    private List<MentalStats> estimates;
    private long durationEstimate;
    public static boolean VERBOSE = false;
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
    public static long calculateDuration(LocalDate date, Context context){
        log("EstimateDate.calculateDuration(LocalDate, Context)", date.toString());
        long totalDuration = 0;
        List<Item> items = ItemsWorker.selectTodayList(date, context);
        for( Item item: items){
            if( item.isDone()){
                totalDuration += item.getDuration();
            }else{
                totalDuration += item.getEstimatedDuration();
            }

        }
        return totalDuration;
    }

    /**
     * no good at all, does nothing, abandoned i reckon
     * maybe it does something after all, but please rewrite
     * @param date the date to estimate
     * @param context, context context context
     */
    private void calculate(LocalDate date, Context context){
        log("...calculate(LocalDate, Context)");
        List<Item> items = ItemsWorker.selectTodayList(date, context);
/*        for( Item item: items){
            if( item.isTemplate()){
                log("...itemIsTemplate()");
            }else{
                MentalType mental = MentalWorker.getMental(item, context);
            }
        }*/
/*        for( Item item: items) {
            estimates.add(item.getEstimate());
        }*/
    }
    public int getAnxiety(){
        return estimates.stream().mapToInt(MentalStats::getAnxiety).sum();
    }

    public LocalDate getDate() {
        return date;
    }

    public long getDurationEstimate(){
        return estimates.stream().mapToLong(MentalStats::getDuration).sum();
    }
    public int  getEnergyEstimate() {
        return estimates.stream().mapToInt(MentalStats::getEnergy).sum();
    }
    public int getStressEstimate(){
        return estimates.stream().mapToInt(MentalStats::getStress).sum();
    }
    public int getMoodEstimate(){
        return estimates.stream().mapToInt(MentalStats::getMood).sum();
    }

}
