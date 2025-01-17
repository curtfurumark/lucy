package se.curtrune.lucy.statistics;

import static se.curtrune.lucy.util.Logger.log;

import android.content.Context;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.Mental;
import se.curtrune.lucy.persist.ItemsWorker;

public class StatisticsPeriod {
    private LocalDate firstDate;
    private LocalDate lastDate;
    private List<Item> items;
    private List<Mental> mentalList;

    public StatisticsPeriod(LocalDate firstDate, LocalDate lastDate, Context context) {
        this.firstDate = firstDate;
        this.lastDate = lastDate;
        initStats(context);
    }
    public int getAnxiety(){
        return mentalList.stream().mapToInt(Mental::getAnxiety).sum();
    }
    public float getAverageAnxiety(){
        log("...getAverageAnxiety");
        if( mentalList.size() == 0){
            return 0;
        }
        int sumAnxiety = mentalList.stream().mapToInt(Mental::getAnxiety).sum();
        return sumAnxiety / mentalList.size();
    }

    public int getEnergy(){
        return mentalList.stream().mapToInt(Mental::getEnergy).sum();
    }
    public float getAverageEnergy(){
        log("...getAverageEnergy()");
        if( mentalList.size() == 0){
            return 0;
        }
        int sumEnergy = mentalList.stream().mapToInt(Mental::getEnergy).sum();
        return sumEnergy / mentalList.size();
    }
    public int getMood(){
        return mentalList.stream().mapToInt(Mental::getMood).sum();
    }
    public float getAverageMood(){
        if( mentalList.size() == 0){
            return 0;
        }
        int sumMood = mentalList.stream().mapToInt(Mental::getEnergy).sum();
        return sumMood / mentalList.size();

    }
    public int getStress(){
        return mentalList.stream().mapToInt(Mental::getStress).sum();
    }
    public float getAverageStress(){
        if( mentalList.size() == 0){
            return 0;
        }
        int sumStress = mentalList.stream().mapToInt(Mental::getEnergy).sum();
        return sumStress / mentalList.size();

    }
    public Duration getDuration() {
        log("StatisticsPeriod.getDuration()");
        long seconds = items.stream().mapToLong(Item::getDuration).sum();
        return Duration.ofSeconds(seconds);
    }
    public LocalDate getFirstDate(){
        return firstDate;
    }
    public List<Item> getItems(){
        return items;
    }
    public LocalDate getLastDate(){
        return lastDate;
    }
    public List<Mental> getMentalList(){
        return mentalList;
    }
    public int getNumberItems() {
        return items.size();
    }
    public int getNumberMentals() {
        return mentalList.size();
    }
    private void initStats(Context context){
        log("...initStats()");
        items = ItemsWorker.selectItems(firstDate, lastDate, context);
        log("...number of items", items.size());
        //mentalList = MentalWorker.select(firstDate, lastDate, context);
        log("...number of mentals", mentalList.size());
    }

}
