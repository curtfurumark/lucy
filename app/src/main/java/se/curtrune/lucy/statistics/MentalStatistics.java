package se.curtrune.lucy.statistics;

import static se.curtrune.lucy.util.Logger.log;

import android.content.Context;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import se.curtrune.lucy.classes.Listable;
import se.curtrune.lucy.classes.Mental;
import se.curtrune.lucy.persist.LocalDB;
import se.curtrune.lucy.persist.Queeries;

public class MentalStatistics {
    private LocalDate firstDate;
    private LocalDate lastDate;
    private List<Mental> mentals;
    private List<Mental> filtered;

    public MentalStatistics(LocalDate firstDate, LocalDate lastDate, Context context) {
        log("MentalStatistics(LocalDate, LocalDate, Context)");
        this.firstDate = firstDate;
        this.lastDate = lastDate;
        init(context);
    }
    public List<Mental> getMentalList(){
        return mentals;
    }

    private void init(Context context){
        log("MentalStatistics.init()");
        LocalDB db = new LocalDB(context);
        String queery = Queeries.selectMentals(firstDate, lastDate);
        filtered = mentals = db.selectMentals(queery);
    }
    public List<Mental> filter(String str){
        filtered = mentals.stream().filter(mental -> mental.contains(str)).collect(Collectors.toList());
        return filtered;
    }
    private List<Mental> getMentals(){
        log("MentalStatistics.getMentals()");
        return mentals;
    }

    /**
     * to be use when user filters the list
     * @param mentalList
     * @return
     */
    public static int getTotalEnergy(List<Mental> mentalList){
        return mentalList.stream().mapToInt(Mental::getEnergy).sum();
    }
    public static int getTotalMood(List<Mental> mentalList){
        return mentalList.stream().mapToInt(Mental::getMood).sum();
    }
    public static int getTotalStress(List<Mental> mentalList){
        return mentalList.stream().mapToInt(Mental::getStress).sum();
    }
    public static int getTotalAnxiety(List<Mental> mentalList){
        return mentalList.stream().mapToInt(Mental::getAnxiety).sum();
    }
    public int getTotalEnergy() {
        return filtered.stream().mapToInt(Mental::getEnergy).sum();
    }

    public int getTotalStress() {
        return filtered.stream().mapToInt(Mental::getStress).sum();
    }

    public int getTotalAnxiety() {
        return filtered.stream().mapToInt(Mental::getAnxiety).sum();
    }

    public int getTotalMood() {
        return  filtered.stream().mapToInt(Mental::getMood).sum();
    }
}
