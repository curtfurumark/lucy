package se.curtrune.lucy.classes.calender;

import java.time.LocalDate;
import java.util.List;

import se.curtrune.lucy.classes.Item;

public class DateHourCell {
    private int hour;
    private LocalDate date;
    private List<Item> events;

    public DateHourCell(int hour, LocalDate localDate) {
        this.hour = hour;
        this.date = localDate;
    }
    public DateHourCell(){

    }
    public LocalDate getDate(){
        return date;
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

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "DateHourCell{" +
                "hour=" + hour +
                ", date=" + date +
                ", events=" + events +
                '}';
    }
}
