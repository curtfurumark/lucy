package se.curtrune.lucy.classes;

import static se.curtrune.lucy.util.Logger.log;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;

import se.curtrune.lucy.util.Converter;

public class Mental implements Listable{
    private long id;
    private String heading;
    private String comment;
    private int anxiety;
    private long itemID;
    private int stress;
    //private int depression;
    private int mood;
    private int energy;
    private long date;
    private String category;
    private int time;
    private long created;
    private long updated;
    public Mental(){
        created = updated = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        date = LocalDate.now().toEpochDay();
        time = LocalTime.now().toSecondOfDay();

    }
    public boolean contains(String text) {
        return (heading + comment + category).toLowerCase().contains(text.toLowerCase());
    }
    @Override
    public long compare() {
        return (date *  3600 * 24 + time) * -1;
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
    public LocalDate getDate(){
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

    public String getLabel() {
        if( LocalDate.now().toEpochDay() == date){
            return  Converter.formatTime(time);
        }
        return Converter.formatDate(date);
    }
    public int getMood(){
        return mood;
    }

    public int getStress() {
        return stress;
    }

    public LocalTime getTime() {
        return LocalTime.ofSecondOfDay(time);
    }
    public int getTimeSecondOfDay(){
        return time;
    }

    public long getUpdatedEpoch(){
        return updated;
    }

    public boolean isCategory(String category){
        //log("Mental.isCategory(String) ", category);
        if( this.category == null){
            log("this.category == null returning false");
            return false;
        }
        return this.category.equalsIgnoreCase(category);
    }
    public void setAnxiety(int anxiety) {
        this.anxiety = anxiety;
    }
    public void setCategory(String category){
        this.category = category;
    }
    public void setComment(String comment) {

        this.comment = comment;
    }

    public void setDate(LocalDate date) {
        this.date = date.toEpochDay();
    }
    public void setCreated(long created){
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
    public void setItemID(long itemID){
        this.itemID = itemID;
    }
    public void setMood(int mood){
        this.mood = mood;
    }


    public void setStress(int stress) {
        this.stress = stress;
    }

    /**
     *
     * @param time second of day
     */
    public void setTime(int time) {
        this.time = time;
    }
    public void setTime(LocalTime time) {
        this.time = time.toSecondOfDay();
    }


    public void setUpdated(long updated){
        this.updated = updated;
    }






}
