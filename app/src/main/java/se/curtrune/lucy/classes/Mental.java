package se.curtrune.lucy.classes;

import static se.curtrune.lucy.util.Logger.log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Locale;

public class Mental implements Listable, Serializable {
    private long id;
    private String heading;
    private String comment;
    private int anxiety;
    private long itemID;
    private int stress;
    private int mood;
    private int energy;
    private long date;
    private String category;
    private int time;
    private long created;
    private long updated;
    private boolean isTemplate;
    private boolean isDone;
    public static boolean VERBOSE = false;

    public Mental(int anxiety, int energy, int mood, int stress) {
        if( VERBOSE) log("Mental(int, int, int, int)");
        this.anxiety = anxiety;
        this.energy = energy;
        this.mood = mood;
        this.stress = stress;
    }

    public enum Type{
        ENERGY, ANXIETY, STRESS, MOOD
    }

    public Mental() {
        created = updated = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        date = LocalDate.now().toEpochDay();
        time = LocalTime.now().toSecondOfDay();
        isTemplate = false;
        isDone = false;
        stress = mood = energy = anxiety = 0;
    }
    public Mental(Item item){
        this();
        this.heading = item.getHeading();
        this.category = item.getCategory();
        this.itemID = item.getID();
        this.isTemplate = item.isTemplate();
        this.isDone = item.getState().equals(State.DONE);
    }

    /**
     * DOES NOT COPY ALL FIELDS, just "content"
     * @param other, the mental to partially copy
     * TODO, take a deeper look at this
     */
/*    public Mental(Mental other) {
        this();
        this.energy = other.energy;
        this.mood = other.mood;
        this.anxiety = other.anxiety;
        this.stress = other.stress;
        this.comment = other.comment;
        //this.created = other.created;
        //this.updated = other.updated;
        //this.date = other.date;
        //this.time = other.time;
        this.category = other.category;
        //this.itemID = other.itemID;
        this.heading = other.heading;
        //this.isTemplate = other.isTemplate;
    }*/

    public boolean contains(String text) {
        return (heading + comment + category).toLowerCase().contains(text.toLowerCase());
    }

    @Override
    public long compare() {
        return (date * 3600 * 24 + time) * -1;
    }
    public long compareTime(){
        return time;
    }

    public int getAnxiety() {
        return anxiety;
    }

    public String getCategory() {
        return category;
    }

    public String getComment() {
        return comment;
    }

    public LocalDateTime getCreated() {
        return LocalDateTime.ofEpochSecond(created, 0, ZoneOffset.UTC);
    }

    public long getCreatedEpoch() {
        return created;
    }

    public LocalDate getDate() {
        return LocalDate.ofEpochDay(date);
    }

    public long getDateEpoch() {
        return date;
    }

    public int getEnergy() {
        return energy;
    }

    @Override
    public String getHeading() {
        return heading;
    }

    public long getID() {
        return id;
    }

    @Override
    public String getInfo() {
        return "no info as of yet";
    }

    public long getItemID() {
        return itemID;
    }



    public int getMood() {
        return mood;
    }

    public int getStress() {
        return stress;
    }

    public LocalTime getTime() {
        return LocalTime.ofSecondOfDay(time);
    }

    public int getTimeSecondOfDay() {
        return time;
    }

    public long getUpdatedEpoch() {
        return updated;
    }

    public boolean isCategory(String category) {
        //log("MentalType.isCategory(String) ", category);
        if (this.category == null) {
            log("this.category == null returning false");
            return false;
        }
        return this.category.equalsIgnoreCase(category);
    }
    public boolean isDone(){
        return isDone;
    }
    public boolean isTemplate(){
        return isTemplate;
    }

    public void setAnxiety(int anxiety) {
        this.anxiety = anxiety;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setComment(String comment) {

        this.comment = comment;
    }

    public void setDate(LocalDate date) {
        this.date = date.toEpochDay();
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public void setID(long id) {
        this.id = id;
    }

    public void isDone(boolean isDone){
        this.isDone = isDone;
    }
    public void setIsTemplate(boolean isTemplate){
        this.isTemplate = isTemplate;
    }
    public void setItemID(long itemID) {
        this.itemID = itemID;
    }

    public void setMood(int mood) {
        this.mood = mood;
    }


    public void setStress(int stress) {
        this.stress = stress;
    }

    /**
     * @param time second of day
     */
    public void setTime(int time) {
        this.time = time;
    }

    public void setTime(LocalTime time) {
        this.time = time.toSecondOfDay();
    }


    public void setUpdated(LocalDateTime updated) {
        this.updated = updated.toEpochSecond(ZoneOffset.UTC);
    }

    public void setUpdated(long updated) {
        this.updated = updated;
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

    @NonNull
    @Override
    public String toString() {
        return String.format(Locale.getDefault(),
                "Mental{%s, e=%d, s=%d, a=%d, m=%d date: %s, time %s, done: %b}", heading,  energy, stress, anxiety, mood,LocalDate.ofEpochDay(date).toString(),LocalTime.ofSecondOfDay(time).toString(), isDone);
    }

    public void setCreated(LocalDateTime created) {
        this.created = created.toEpochSecond(ZoneOffset.UTC);
    }
}


