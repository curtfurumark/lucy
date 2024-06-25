package se.curtrune.lucy.statistics;

import static se.curtrune.lucy.util.Logger.log;

import android.content.Context;
import android.media.MediaSession2Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.Mental;
import se.curtrune.lucy.persist.LocalDB;
import se.curtrune.lucy.persist.Queeries;
import se.curtrune.lucy.workers.MentalWorker;

public class MentalStatistics {
    private final LocalDate firstDate;
    private final LocalDate lastDate;
    private List<Mental> mentals;
    private List<Mental> filtered;

    public MentalStatistics(LocalDate firstDate, LocalDate lastDate, Context context) {
        log("MentalStatistics(LocalDate, LocalDate, Context)");
        this.firstDate = firstDate;
        this.lastDate = lastDate;
        init(context);
    }
    public MentalStatistics( LocalDate date, Context context){
        this.firstDate = date;
        this.lastDate = date;
        init(context);
    }
    public List<Mental> getMentalList(){
        return mentals;
    }

    public void add(Mental mental){
        mentals.add(mentals.size(), mental);
    }
    private void init(Context context){
        log("MentalStatistics.init()");
        try(LocalDB db = new LocalDB(context)) {
            String queery = Queeries.selectMentals(firstDate, lastDate, false, true);
            filtered = mentals = db.selectMentals(queery);
        }
    }
    public List<Mental> filter(String str){
        filtered = mentals.stream().filter(mental -> mental.contains(str)).collect(Collectors.toList());
        return filtered;
    }

    /**
     * get every fucking mental
     * @return, a list containing all the mentals
     */
    private List<Mental> getMentals(){
        log("MentalStatistics.getMentals()");
        return mentals;
    }
    public static List<Mental> getMentals(List<Item> items, Context context){
        log("...getMentals(List<Item>)");
        List<Mental> mentalList = new ArrayList<>();
        for( Item item: items){
            Mental mental = MentalWorker.getMental(item, context);
            assert  mental != null;
            mentalList.add(mental);
        }
        return mentalList;
    }

    /**
     * to be use when user filters the list
     * @param mentalList, the list of mentals to sum
     * @return, sum of energy fields
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
    public boolean remove(Mental mental){
        log("MentalStatistics.remove(Mental)");
        return mentals.remove(mental);
    }
}
