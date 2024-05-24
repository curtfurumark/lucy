package se.curtrune.lucy.classes;

import java.time.LocalTime;

public class LogItem {
    private String message;
    private long time;
    public LogItem(String message, LocalTime time){
        this.message = message;
        this.time = time.toSecondOfDay();
    }
    public String getMessage() {
        return message;
    }
    public LocalTime getTime(){
        return LocalTime.ofSecondOfDay(time);
    }
}
