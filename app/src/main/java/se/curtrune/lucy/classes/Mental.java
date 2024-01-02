package se.curtrune.lucy.classes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;

import se.curtrune.lucy.util.Converter;

public class Mental implements Listable{
    private long id;
    private String title;
    private String comment;
    private int anxiety;
    private long itemID;
    private int stress;
    private int depression;
    private int energy;
    private long date;
    private int time;
    private long created;
    private long updated;
    public Mental(){
        created = updated = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        date = LocalDate.now().toEpochDay();
        time = LocalTime.now().toSecondOfDay();

    }

    @Override
    public String getHeading() {
        return title;
    }

    @Override
    public String getInfo() {
        return null;
    }

    public boolean contains(String text) {
        return (title + comment).contains(text);
    }

    @Override
    public long compare() {
        return 0;
    }

    public int getAnxiety() {
        return anxiety;
    }
    public LocalDateTime getCreated(){
        return LocalDateTime.ofEpochSecond(created, 0 , ZoneOffset.UTC);
    }
    public long getDateEpoch() {
        return date;
    }

    public long getID() {
        return id;
    }
    public long getItemID() {
        return itemID;
    }

    public LocalTime getTime() {
        return LocalTime.ofSecondOfDay(time);
    }
    public int getTimeSecondOfDay(){
        return time;
    }


    public String getTitle() {
        return title;
    }



    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public long getCreatedEpoch() {
        return created;
    }
    public LocalDate getDate(){
        return LocalDate.ofEpochDay(date);
    }
    public int getStress() {
        return stress;
    }
    public long getUpdatedEpoch(){
        return updated;
    }
    public void setAnxiety(int anxiety) {
        this.anxiety = anxiety;
    }
    public void setId(long id) {
        this.id = id;
    }


    public void setStress(int stress) {
        this.stress = stress;
    }

    public int getDepression() {
        return depression;
    }



    public int getEnergy() {
        return energy;
    }


    public void setCreated(long created){
        this.created = created;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public void setDepression(int depression) {
        this.depression = depression;
    }
    public void setEnergy(int energy) {
        this.energy = energy;
    }
    public void setItemID(long ditemID){
        this.itemID = itemID;
    }

    public void setTime(int time) {
        this.time = time;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public void setUpdated(long updated){
        this.updated = updated;
    }


    public String getLabel() {
        if( LocalDate.now().toEpochDay() == date){
            return  Converter.formatTime(time);
        }
        return Converter.formatDate(date);
    }
}
