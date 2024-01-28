package se.curtrune.lucy.classes;

import static se.curtrune.lucy.util.Logger.log;

import java.util.List;

public class MentalStatistics {
    private List<Mental> mentalList;
    public static boolean VERBOSE = true;

    public MentalStatistics(List<Mental> mentalList) {
        log("MentalStatistics(List<Mental>");
        if( mentalList == null){
            log("...mental list is null");
        }else{
            log("...size of mental list", mentalList.size());
        }
        this.mentalList = mentalList;
    }

    public float getAverageEnergy(){
        log("...getAverageEnergy()");
        if( mentalList.size() == 0){
            log("...mentalList is size 0");
            return 0;
        }
        float sum = 0;
        for( Mental mental: mentalList){
            sum += mental.getEnergy();
        }
        if( VERBOSE) {
            log("sum", sum);
            log("size", mentalList.size());
        }
        return sum / mentalList.size();
    }
    public int getMood(){
        return mentalList.get(mentalList.size()).getMood();

    }
    public float getAverageMood(){
        long  sum = mentalList.stream().mapToLong(Mental::getMood).sum();
        return (float) sum / mentalList.size();
    }
    public long getDuration(List<Item> items){
        long sum = 0;
        for( Item item: items){
            sum += item.getDuration();
        }
        return sum;
    }
    public int getStress(){
        return mentalList.get(mentalList.size()).getStress();
    }
    public float getAverageStress(){
        int sum = mentalList.stream().mapToInt(Mental::getStress).sum();
        return (float) sum / mentalList.size();

    }
}
