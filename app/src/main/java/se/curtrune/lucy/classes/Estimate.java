package se.curtrune.lucy.classes;

import static se.curtrune.lucy.util.Logger.log;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.Locale;

import se.curtrune.lucy.util.Converter;

public class Estimate implements Serializable {
    private long duration;
    private int energy;
    private int anxiety;
    private int stress;
    private int mood;
    public static boolean VERBOSE = false;
    public enum Type{
        USER, CALCULATED
    }
    private Type type =Type.USER;

    public Estimate() {
        if( VERBOSE) log("Estimate()");
    }
    public Estimate divide(int denominator){
        Estimate estimate = new Estimate();
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
    public int getMood(){
        return mood;
    }
    public int getStress(){
        return stress;
    }
    public Type getType(){
        return type;
    }

    public void plusDuration(long duration){
        this.duration += duration;
    }
    public void plusEnergy(int energy){
        this.energy += energy;
    }
    public void plus(Estimate other){
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
        return new Gson().toJson(this, Estimate.class);
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "estimated time %s, energy %d", Converter.formatSecondsWithHours(duration), energy);
    }
}
