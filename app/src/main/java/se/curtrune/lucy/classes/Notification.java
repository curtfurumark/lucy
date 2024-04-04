package se.curtrune.lucy.classes;

import com.google.gson.Gson;

import java.time.LocalDate;
import java.time.LocalTime;

public class Notification {
    public enum Type{
        PENDING, ALARM, NOTIFICATION;
    }
    private Type type;
    private LocalDate date;
    private LocalTime time;
    public LocalDate getDate(){
        return this.date;
    }
    public LocalTime getTime(){
        return this.time;
    }
    public Type getType(){
        return this.type;
    }
    public void setDate(LocalDate date){
        this.date = date;
    }
    public void setDate(String string) {
        date = LocalDate.parse(string);
    }


    public void setTime(LocalTime time){
        this.time = time;
    }
    public void setTime(String string) {
        time = LocalTime.parse(string);
    }
    public void setType(Type type){
        this.type = type;
    }
    public String toJson(){
        return new Gson().toJson(this);
    }

}
