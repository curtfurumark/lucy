package se.curtrune.lucy.classes;

import static se.curtrune.lucy.util.Logger.log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class Repeat implements Serializable {

    public static boolean   VERBOSE = false;
    private long id;
    private long templateID;
    private LocalDate updated;
    private boolean infinity = false;
    private Unit unit = Unit.PENDING;
    private LocalDate firstDate;
    private LocalDate lastDate;
    private int qualifier = 1;

    public long getID() {
        return id;
    }
    public Unit getUnit() {
        return unit;
    }
    public boolean hasLastDate() {
        return lastDate != null;
    }

    public boolean isInfinite() {
        return infinity;
    }

    public void setID(long id) {
        this.id = id;
    }

    public long getIntervalMilliseconds() {
        switch (unit){
            case DAY:
                return 24 * 3600 * 1000 * qualifier;
            case WEEK:
                return 7 * 24 * 3600 * 1000 * qualifier;
            default:
                log("WARNING, MONTH AND SUCH NOT IMPLEMENTED");
                return 0;
        }
    }

    public enum Unit {
        DAY, WEEK, MONTH, YEAR, DAYS_OF_WEEK, PENDING, HOUR
    }

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
/*    private boolean checkPeriod(LocalDate date){
        if( infinity){
            return !date.isAfter(lastDate);
        }
        return (date.isAfter(firstDate) || date.equals(firstDate)) && (date.isBefore(lastDate) || date.equals(lastDate));
    }*/

    public LocalDate getFirstDate(){
        return this.firstDate;
    }
    public LocalDate getLastDate(){
        return this.lastDate;
    }

    public int getQualifier() {
        return qualifier;
    }

    public LocalDate getNextDate(){
        return getNextDate(unit, qualifier);
    }
    public long getTemplateID(){
        return templateID;
    }
    private boolean isNextDate(LocalDate date){
        for(DayOfWeek dayOfWeek: dayOfWeeks){
            if( dayOfWeek.equals(date.getDayOfWeek())){
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param unit, one of DAY, WEEK, MONTH and maybe YEAR
     * @param qualifier, for every other WEEK, qualifier 2
     * @return the next date that
     */
    private LocalDate getNextDate(Unit unit, int qualifier){
        assert unit != null;
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
/*    public LocalDate getNextDate(LocalDate previousDate){
        log("...getNextDate(LocalDate)", previousDate.toString());
        LocalDate nextDate = null;
        switch (unit){
            case DAY:
                nextDate =  previousDate.plusDays(qualifier);
                break;
            case WEEK:
                nextDate = previousDate.plusWeeks(qualifier);
                break;
            case MONTH:
                nextDate= previousDate.plusMonths(qualifier);
                break;
            case YEAR:
                nextDate = previousDate.plusYears(qualifier);
                break;
            case DAYS_OF_WEEK:
                return calculateNextDayOfWeek();
        }
        if( !checkPeriod(nextDate)){
            return null;
        }
        return nextDate;
    }*/
    public LocalDate getUpdated(){
        return updated;
    }

    public List<DayOfWeek> getWeekDays(){
        return dayOfWeeks;
    }

    public void remove(DayOfWeek dayOfWeek){
        if( VERBOSE) log("Repeat.remove(DayOfWeek)", dayOfWeek.toString());
        boolean found = dayOfWeeks.remove(dayOfWeek);
        log("...dayOfWeek removed? ", found);
    }
    public void setFirstDate(LocalDate firstDate){
        this.firstDate = firstDate;
    }
    public void setInfinity(boolean infinity){
        this.infinity = infinity;
    }
    public void setLastDate(LocalDate lastDate){
        this.lastDate = lastDate;
    }

    public void setUpdated(LocalDate updated){
        this.updated = updated;

    }
    public void setWeekDays(List<DayOfWeek> weekDays){
        this.unit = Unit.DAYS_OF_WEEK;
        this.dayOfWeeks = weekDays;
    }
    public void setTemplateID(long id){
        this.templateID = id;
    }

    public String toJson(){
        if( VERBOSE) log("Repeat.toJson()");
        return new Gson().toJson(this, Repeat.class);
    }
    public void setPeriod(int qualifier, Unit unit) {
        this.qualifier = qualifier;
        this.unit = unit;
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
