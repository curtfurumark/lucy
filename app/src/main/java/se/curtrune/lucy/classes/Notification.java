package se.curtrune.lucy.classes;

import java.time.LocalDate;
import java.time.LocalTime;

public class Notification {
    public enum Type{
        PENDING, ALARM, NOTIFICATION;
    }
    private Type type;
    private LocalDate date;
    private long itemID;
    private LocalTime time;
    public void setDate(String string) {
        date = LocalDate.parse(string);
    }

    public void setItemID(long itemID){
        this.itemID = itemID;
    }
    public void setTime(String string) {
        time = LocalTime.parse(string);
    }
    public void setType(Type type){
        this.type = type;

    }

}
