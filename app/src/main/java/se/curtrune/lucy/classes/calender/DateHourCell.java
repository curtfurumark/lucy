package se.curtrune.lucy.classes.calender;

import java.time.LocalDate;
import java.util.List;

import se.curtrune.lucy.classes.Item;

public class DateHourCell {
    private int hour;
    private LocalDate localDate;
    private List<Item> events;

    public DateHourCell(int hour, LocalDate localDate) {
        this.hour = hour;
        this.localDate = localDate;
    }
    public DateHourCell(){

    }
    public int getHour(){
        return this.hour;
    }
    public void setEvents(List<Item> events){
        this.events = events;

    }
    public void setHour(int hour){
        this.hour = hour;
    }
}
