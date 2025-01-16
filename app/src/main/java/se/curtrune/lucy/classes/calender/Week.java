package se.curtrune.lucy.classes.calender;

import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static se.curtrune.lucy.util.Logger.log;

public class Week {
    private final LocalDate firstDate;
    private final  LocalDate lastDate;
    private LocalDate currentDate;
    private int weekNumber;
    private List<LocalDate> dates;
    public static boolean VERBOSE = false;
    public Week(){
        currentDate = LocalDate.now();
        firstDate = calculateFirstDateOfTheWeek(currentDate);
        lastDate = firstDate.plusDays(6);
    }
    public Week(LocalDate date){
        if( VERBOSE) log("Week(LocalDate)", date.toString());
        this.currentDate = date;
        firstDate = calculateFirstDateOfTheWeek(currentDate);
        weekNumber = getWeekNumber();
        lastDate = firstDate.plusDays(6);
        dates = getDates();
    }
    private LocalDate calculateFirstDateOfTheWeek(LocalDate date){
        TemporalField dayOfWeek = WeekFields.ISO.dayOfWeek();
        return date.with(dayOfWeek, dayOfWeek.range().getMinimum());
    }
    public LocalDate getCurrentDate(){
        return currentDate;
    }
    public List<LocalDate> getDates(){
        dates = new ArrayList<>();
        dates.add(firstDate);
        for( int i = 1; i <= 6;i++){
            dates.add(firstDate.plusDays(i));
        }
        return dates;
    }
    public LocalDate getFirstDateOfWeek(){
        return firstDate;
    }
    public LocalDate getLastDateOfWeek(){return lastDate;}
    public LocalDate getMonday(){
        return firstDate;
    }
    public int getWeekNumber(){
        return firstDate.get(WeekFields.of(Locale.getDefault()).weekOfYear());
    }

    public void setCurrentDate(LocalDate currentDate) {
        this.currentDate = currentDate;
    }

    public Month getMonth(){
        return currentDate.getMonth();
    }
    public Week getNextWeek() {
        return  new Week(firstDate.plusWeeks(1));
    }

    public Week getPreviousWeek() {
        return new Week(firstDate.minusWeeks(1));
    }
    public Week minusWeeks(int weeks){
        return new Week(firstDate.minusWeeks(weeks));
    }
    public Week plusWeek(int weeks) {
        return new Week(firstDate.plusWeeks(weeks));
    }

    @Override
    public String toString() {
        return "Week{" +
                "firstDate=" + firstDate +
                ", lastDate=" + lastDate +
                ", weekNumber=" + weekNumber +
                '}';
    }


    /**
     * well, for most of the time, but every now and then it's indeterminate
     * but it will return the year of the current date, which most ot the time amounts to the first date of the week
     *
     */
    public int getYear() {
        return firstDate.getYear();
    }
}
