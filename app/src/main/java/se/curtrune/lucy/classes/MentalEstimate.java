package se.curtrune.lucy.classes;

import static se.curtrune.lucy.util.Logger.log;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import se.curtrune.lucy.util.Converter;

public class MentalEstimate implements Serializable {
    private long duration;
    private int energy;
    private int anxiety;
    private int stress;
    private int mood;
    private List<Mental> mentalList = new ArrayList<>();
    public static boolean VERBOSE = false;

    public enum Type{
        USER, CALCULATED
    }
    private Type type =Type.USER;

    public MentalEstimate() {
        if( VERBOSE) log("MentalEstimate()");
    }

    public void add(Mental mental) {
        mentalList.add(mental);
    }
    public MentalEstimate divide(int denominator){
        MentalEstimate estimate = new MentalEstimate();
        estimate.setType(Type.CALCULATED);
        if( denominator == 0){
            return estimate;
        }
        estimate.setEnergy(this.energy / denominator);
        estimate.setDuration(this.duration / denominator);
        estimate.setAnxiety(this.anxiety / denominator);
        //estimate.setMood
        return estimate;
    }
    public int getAnxiety(){
        return anxiety;
    }
    public long getDuration() {
        return duration;
    }

    public int getEnergy() {
        return energy;
    }
    public List<Mental> getMentals(){
        return mentalList;
    }
    public int getMood(){
        return mood;
    }
    public int getStress(){
        return stress;
    }
    public Type getType(){
        return type;
    }

    public void plusAnxiety(int anxiety) {
        this.anxiety += anxiety;
    }
    public void plusDuration(long duration){
        this.duration += duration;
    }
    public void plusEnergy(int energy){
        this.energy += energy;
    }
    public void plusMood(int mood){
        this.mood += mood;
    }
    public void plusStress(int stress){
        this.stress += stress;
    }
   public void plus(MentalEstimate other){
        this.duration += other.duration;
        this.energy += other.energy;
    }
    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }
    public void setAnxiety(int anxiety){
        this.anxiety = anxiety;
    }
    public void setType(Type type){
        this.type = type;
    }
    public String toJson(){
        return new Gson().toJson(this, MentalEstimate.class);
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "estimated time %s, energy %d", Converter.formatSecondsWithHours(duration), energy);
    }
}
