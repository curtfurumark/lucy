package se.curtrune.lucy.classes;

import static se.curtrune.lucy.util.Logger.log;

import android.content.res.Resources;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import se.curtrune.lucy.R;


public class Repeat implements Serializable {

    public static boolean   VERBOSE = false;

    public Unit getUnit() {
        return unit;
    }
    private LocalDate untilDate;


    public void setPeriod(int qualifier, Unit unit) {
        this.qualifier = qualifier;
        this.unit = unit;
    }

    public boolean hasLastDate() {
        return lastDate != null;
    }

    public enum Unit {
        DAY, WEEK, MONTH, YEAR, DAYS_OF_WEEK, PENDING
    }
    private Unit unit = Unit.PENDING;
    private int days;
    private LocalDate firstDate;
    private LocalDate lastDate;
    private int qualifier = 1;
    private List<DayOfWeek> dayOfWeeks = new ArrayList<>();
    public Repeat(){
        if(VERBOSE)log("Repeat() constructor");
    }

    public void add(DayOfWeek dayOfWeek){
        unit = Unit.DAYS_OF_WEEK;
        dayOfWeeks.add(dayOfWeek);
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

    public int getQualifier() {
        return qualifier;
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
        return getNextDate(unit, qualifier);
    }

    /**
     *
     * @param unit, one of DAY, WEEK, MONTH and maybe YEAR
     * @param qualifier, for every other WEEK, qualifier 2
     * @return the next date that
     */
    private LocalDate getNextDate(Unit unit, int qualifier){
        if( unit == null){
            log("ERROR getNextDate(Unit, int)");
            unit = Unit.DAY;//TODO, fix this HACK
        }
        log("Repeat.getNextDate(Unit)", unit.toString());
        if( qualifier < 1){
            log("WARNING, qualifier less than one, setting it to one");
            qualifier = 1;
        }
        LocalDate currentDate = LocalDate.now();
        switch (unit){
            case DAY:
                return currentDate.plusDays(qualifier);
            case WEEK:
                return currentDate.plusWeeks(qualifier);
            case MONTH:
                return currentDate.plusMonths(qualifier);
            case YEAR:
                return currentDate.plusYears(qualifier);
            case DAYS_OF_WEEK:
                return calculateNextDayOfWeek();
        }
        return currentDate;
    }

    public List<DayOfWeek> getWeekDays(){
        return dayOfWeeks;
    }

    public void setFirstDate(LocalDate firstDate){
        this.firstDate = firstDate;
    }
    public void setLastDate(LocalDate lastDate){
        this.lastDate = lastDate;
    }
    public void setUnit(Unit unit){
        log("Repeat.setPeriod(Unit) ", unit.toString());
        this.unit = unit;
    }
    public void setWeekDays(List<DayOfWeek> weekDays){
        this.unit = Unit.DAYS_OF_WEEK;
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

    @NonNull
    @Override
    public String toString() {
        return  String.format(Locale.getDefault(), "%s %d %s",
                //Resources.getSystem().getString(R.string.every),//String resource not found
                "each",
                qualifier,
                unit.toString());
    }
}
