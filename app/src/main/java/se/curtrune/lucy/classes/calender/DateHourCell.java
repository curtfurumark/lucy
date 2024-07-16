package se.curtrune.lucy.classes.calender;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import se.curtrune.lucy.classes.Item;

public class DateHourCell {
    private LocalTime time;
    private LocalDate date;
    private List<Item> events;

    public List<Item> getEvents() {
        return events;
    }

    public boolean hasEvents() {
        return events != null && events.size() > 0;
    }


    public enum Type{
        EVENT_CELL, TIME_CELL, EMPTY_CELL, DATE_CELL
    }

    public DateHourCell(LocalTime time, LocalDate localDate) {
        this.time = time;
        this.date = localDate;
    }
    private Type type;
    public DateHourCell(){
        this.type = Type.EVENT_CELL;
    }
    public LocalDate getDate(){
        return date;
    }
    public int getHour(){
        return this.time.getHour();
    }
    public LocalTime getTime() {
        return time;
    }
    public Type getType() {
        return type;
    }
    public void setEvents(List<Item> events){
        this.events = events;

    }
    public void setHour(int hour){
        this.time = LocalTime.of(hour, 0);
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setType(Type type){
        this.type = type;
    }
    @Override
    public String toString() {
        return String.format("%s, %s events %d", date.toString(), time.toString(),events.size() );
    }
}
