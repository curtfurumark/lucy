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


    public enum Mode{
        DAYS, DAY_OF_WEEKS
    }
    private Mode mode = Mode.DAYS;
    private int days;
    private LocalTime time;
    private LocalDate nextDate;
    private final List<DayOfWeek> dayOfWeeks = new ArrayList<>();
    public Period(){
        //log("Period() constructor");
    }

    public int getDays() {
        return days;
    }
    public Mode getMode(){
        return mode;
    }

    public void setDays(int days) {
        //log("Period.setDays(int))", days);
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

    public void print() {
        log("Period.print()", mode.toString());
        if( mode.equals(Mode.DAYS)){
            log("...number of days: ", days);
        }else{
            dayOfWeeks.forEach(System.out::println);
            log("next date ", calculateNextDate());
        }
    }
    public void remove(DayOfWeek dayOfWeek){
        log("Period.remove(DayOfWeek)", dayOfWeek.toString());
        boolean found = dayOfWeeks.remove(dayOfWeek);
        log("...dayOfWeek removed? ", found);
    }
    public String toJson(){
        log("Period.toJson()");
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
