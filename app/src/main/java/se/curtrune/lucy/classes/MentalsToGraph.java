package se.curtrune.lucy.classes;

import static se.curtrune.lucy.util.Logger.log;

import com.jjoe64.graphview.series.DataPoint;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class MentalsToGraph {

    //do i need this one?
    private LocalDate date;
    private int firstHour;
    private int lastHour;
    private int numberOfHours;
    private List<Mental> mentals;
    public static boolean VERBOSE = true;
    private Map<Integer, List<Mental>> mentalMap;

    /**
     * for the time being, mentals is assumed to be sorted ascending
     * @param date, you're guess is as good as mine
     * @param mentals
     */
    public MentalsToGraph(LocalDate date, List<Mental> mentals){
        this.date = date;
        this.mentals = mentals;
        //mentals.sort(Comparator.comparingLong(Mental::compareTime));
        init();
        initMentalMap();
    }
    private int calculateMentalLevel(List<Mental> mentals, Mental.Type mentalType){
        log("...calculateMentalLevel(List<Mental>, Mental.Type)");
        if( VERBOSE) mentals.forEach(System.out::println);
        int mentalLevel = 0;
        switch (mentalType){
            case ANXIETY:
                mentalLevel =  mentals.stream().mapToInt(Mental::getAnxiety).sum();
                break;
            case MOOD:
                mentalLevel =  mentals.stream().mapToInt(Mental::getMood).sum();
                break;
            case STRESS:
                mentalLevel =  mentals.stream().mapToInt(Mental::getStress).sum();
                break;
            case ENERGY:
                mentalLevel =  mentals.stream().mapToInt(Mental::getEnergy).sum();
                break;
        }
        log("...mentalLevel", mentalLevel);
        return mentalLevel;
    }
    public DataPoint[] getDataPoints(Mental.Type mentalType){
        log("...getDataPoints(Mental.Type)", mentalType.toString());
        int mentalLevel = 0;
        DataPoint[] dataPoints = new DataPoint[mentalMap.size()];
        for(Integer hour: mentalMap.keySet()){
            mentalLevel += calculateMentalLevel(mentalMap.get(hour), mentalType);
            dataPoints[hour] = new DataPoint(hour, mentalLevel);
        }
        return dataPoints;
    }
    private void init(){
        log("...init()");
        if( mentals.size() == 0){
            return;
        }
        firstHour = mentals.get(0).getTime().getHour();
        lastHour = mentals.get(mentals.size() - 1).getTime().getHour();
        numberOfHours = lastHour - firstHour + 1;
        log("...firstHour", firstHour);
        log("...lastHour", lastHour);
        log("...numberOfHours", numberOfHours);

    }
    private void initMentalMap(){
        log("...initMentalMap()");
        mentalMap = new TreeMap<>();
        int hour = LocalTime.now().getHour();
        for( int i = 0; i < hour; i++) {
            log("...currentHour", i);
            int finalI = i;
            List<Mental> filtered =  mentals.stream().filter(mental -> isHour(mental.getTime(), finalI)).collect(Collectors.toList());
            filtered.forEach(System.out::println);
            mentalMap.put(i, filtered);
        }
    }
    private boolean isHour(LocalTime time, int hour){
        return time.getHour() == hour;
    }

    public int getFirstHour(){
        return firstHour;
    }
    public String[] getXLabels(){
        String[] xLabels = new String[numberOfHours];
        int currentHour = firstHour;
        for(int i = 0; i < numberOfHours; i++){
            xLabels[i] = String.valueOf(currentHour++);
        }
        return xLabels;
    }
    public int getLastHour() {
        return lastHour;
    }
    public int getNumberOfHours(){
        return numberOfHours;
    }
}
