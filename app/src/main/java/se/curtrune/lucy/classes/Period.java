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


public class Period implements Serializable {


    public static boolean   VERBOSE = false;
    public enum Mode{
        DAYS, DAY_OF_WEEKS
    }
    private Mode mode = Mode.DAYS;
    private int days;
    private LocalTime time;
    //private LocalDate nextDate;
    private LocalDate firstDate;
    private LocalDate lastDate;
    private List<DayOfWeek> dayOfWeeks = new ArrayList<>();
    public Period(){
        //log("Period() constructor");
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
    private LocalDate calculateNextDate(){
        LocalDate date = LocalDate.now().plusDays(1);
        while( !isNextDate(date)){
            date = date.plusDays(1);
        }
        return date;
    }
    public LocalDate getNextDate(){
        if( mode.equals(Mode.DAY_OF_WEEKS)) {
            return calculateNextDate();
        }
        return LocalDate.now().plusDays(days);
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
    public void setWeekDays(List<DayOfWeek> weekDays){
        this.mode = Mode.DAY_OF_WEEKS;
        this.dayOfWeeks = weekDays;
    }
    public void remove(DayOfWeek dayOfWeek){
        if( VERBOSE) log("Period.remove(DayOfWeek)", dayOfWeek.toString());
        boolean found = dayOfWeeks.remove(dayOfWeek);
        log("...dayOfWeek removed? ", found);
    }
    public String toJson(){
        if( VERBOSE) log("Period.toJson()");
        return new Gson().toJson(this, Period.class);
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
