package se.curtrune.lucy.classes;

import com.google.gson.Gson;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Locale;

import se.curtrune.lucy.util.Converter;

public class Notification {
    public enum Type{
        PENDING, ALARM, NOTIFICATION;
    }
    private Type type;
    private LocalDate date;
    private LocalTime time;
    private String title;
    private String content;
    public String getContent(){
        return this.content;
    }
    public LocalDate getDate(){
        return this.date;
    }
    public String getTitle(){
        return this.title;
    }
    public LocalTime getTime(){
        return this.time;
    }
    public Type getType(){
        return this.type;
    }
    public void setContent(String content){
        this.content = content;
    }
    public void setDate(LocalDate date){
        this.date = date;
    }
    public void setDate(String string) {
        date = LocalDate.parse(string);
    }


    public void setTitle(String title){
        this.title = title;
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

    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "%s %s %s", type.toString(), date.toString(), Converter.format(time));
    }
}
