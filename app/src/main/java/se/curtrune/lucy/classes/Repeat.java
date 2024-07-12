package se.curtrune.lucy.classes;

import static se.curtrune.lucy.util.Logger.log;

import com.google.gson.Gson;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import se.curtrune.lucy.classes.calender.Week;


public class Repeat implements Serializable {

    public static boolean   VERBOSE = false;

    public Period getPeriod() {
        return period;
    }

    public void setCurrentDate(LocalDate currentDate) {
        this.currentDate = currentDate;
    }

    public void setPeriod(int qualifier, Period period) {
        this.qualifier = qualifier;
        this.period = period;
    }

    public enum Mode{
        DAYS, DAY_OF_WEEKS
    }
    public enum Period{
        DAY, WEEK, MONTH, YEAR, DAYS_OF_WEEK
    }
    private Period period;
    private Mode mode = Mode.DAYS;
    private int days;
    private LocalTime time;
    private LocalDate firstDate;
    private LocalDate currentDate;
    private LocalDate lastDate;
    private int qualifier = 1;
    private List<DayOfWeek> dayOfWeeks = new ArrayList<>();
    public Repeat(){
        currentDate = LocalDate.now();
    }
    public Repeat(LocalDate currentDate){
        this.currentDate = currentDate;

    }
    private LocalDate calculateNextDayOfWeek(){
        LocalDate date = LocalDate.now().plusDays(1);
        while( !isNextDate(date)){
            date = date.plusDays(1);
        }
        return date;
    }

    public int getDays() {
        return days;
    }
    public LocalDate getFirstDate(){
        return this.firstDate;
    }
    public LocalDate getLastDate(){
        return this.lastDate;
    }
    public Mode getMode(){
        return mode;
    }

    public void setDays(int days) {
        mode = Mode.DAYS;
        this.days = days;
    }
    public void add(DayOfWeek dayOfWeek){
        mode = Mode.DAY_OF_WEEKS;
        dayOfWeeks.add(dayOfWeek);
    }
    private boolean isNextDate(LocalDate date){
        for(DayOfWeek dayOfWeek: dayOfWeeks){
            if( dayOfWeek.equals(date.getDayOfWeek())){
                return true;
            }
        }
        return false;
    }
    public LocalDate getNextDate(){
        if( mode.equals(Mode.DAY_OF_WEEKS)) {
            return calculateNextDayOfWeek();
        }
        return LocalDate.now().plusDays(days);
    }

    /**
     *
     * @param period, one of DAY, WEEK, MONTH and maybe YEAR
     * @param qualifier, for every other WEEK, qualifier 2
     * @return the next date that
     */
    public LocalDate getNextDate(Period period, int qualifier){
        log("Repeat.getNextDate(Period)");
        if( qualifier < 1){
            log("WARNING, qualifier less than one, setting it to one");
            qualifier = 1;
        }
        LocalDate currentDate = LocalDate.now();
        switch (period){
            case DAY:
                return currentDate.plusDays(1 * qualifier);
            case WEEK:
                return currentDate.plusWeeks(1 * qualifier);
            case MONTH:
                return currentDate.plusMonths(1 * qualifier);
            case YEAR:
                return currentDate.plusYears(1 * qualifier);
            case DAYS_OF_WEEK:
                return calculateNextDayOfWeek();
        }
        return currentDate;
    }
    public LocalDate getNextWeekDay(){
        log("Repeat.getNextWeekDay()");
        return LocalDate.now();
    }
    public LocalTime getTime(){
        return time;
    }
    public List<DayOfWeek> getWeekDays(){
        return dayOfWeeks;
    }
    public boolean isMode(Mode mode){
        return this.mode.equals(mode);
    }

    public void setFirstDate(LocalDate firstDate){
        this.firstDate = firstDate;
    }
    public void setLastDate(LocalDate lastDate){
        this.lastDate = lastDate;
    }
    public void setPeriod(Period period){
        log("Repeat.setPeriod(Period) ", period.toString());
        this.period = period;
    }
    public void setWeekDays(List<DayOfWeek> weekDays){
        this.mode = Mode.DAY_OF_WEEKS;
        this.dayOfWeeks = weekDays;
    }
    public void remove(DayOfWeek dayOfWeek){
        if( VERBOSE) log("Repeat.remove(DayOfWeek)", dayOfWeek.toString());
        boolean found = dayOfWeeks.remove(dayOfWeek);
        log("...dayOfWeek removed? ", found);
    }
    public String toJson(){
        if( VERBOSE) log("Repeat.toJson()");
        return new Gson().toJson(this, Repeat.class);
    }

    @Override
    public String toString() {
        if( mode.equals(Mode.DAYS)) {
            return String.format(Locale.getDefault(), "%d %s",days,  mode.toString());
        }
        String weekDays = "";
        for( DayOfWeek dayOfWeek: dayOfWeeks){
            weekDays += dayOfWeek.toString() + " ";
        }
        return String.format(Locale.getDefault(), "%s", weekDays);
    }
}
