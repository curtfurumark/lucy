package se.curtrune.lucy.classes;

import static se.curtrune.lucy.util.Logger.log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Locale;

import se.curtrune.lucy.util.Converter;

public class Notification implements Serializable {
    public enum Type{
        PENDING, ALARM, NOTIFICATION
    }
    private Type type;
    private long date;
    private int time;
    private String title;
    private String content;
    public  Notification(){
        log("Notification()");
        type = Type.NOTIFICATION;
    }
    public String getContent(){
        return this.content;
    }
    public LocalDate getDate(){
        return LocalDate.ofEpochDay(date);
    }
    public String getTitle(){
        return this.title;
    }
    public LocalTime getTime(){
        return LocalTime.ofSecondOfDay(time);
    }
    public Type getType(){
        return this.type;
    }
    public void setContent(String content){
        this.content = content;
    }
    public void setDate(LocalDate date){
        this.date = date.toEpochDay();
    }
    public void setDate(String string) {
        date = LocalDate.parse(string).toEpochDay();
    }


    public void setTitle(String title){
        this.title = title;
    }
    public void setTime(LocalTime time){
        this.time = time.toSecondOfDay();
    }
    public void setTime(String string) {
        time = LocalTime.parse(string).toSecondOfDay();
    }
    public void setType(Type type){
        this.type = type;
    }
    public String toJson(){
        return new Gson().toJson(this);
    }

    @NonNull
    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "%s %s %s", type.toString(), getDate().toString(), Converter.format(getTime()));
    }
}
