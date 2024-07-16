package se.curtrune.lucy.classes;

import static se.curtrune.lucy.util.Logger.log;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import se.curtrune.lucy.util.Converter;

public class MentalStats implements Serializable {
    private long duration;
    private int energy;
    private int anxiety;
    private int stress;
    private int mood;
    private List<Mental> mentalList = new ArrayList<>();
    public static boolean VERBOSE = false;

    public boolean remove(Mental mental) {
        return mentalList.remove(mental);
    }

    public enum Type{
        USER, CALCULATED
    }
    private Type type =Type.USER;

    public MentalStats() {
        if( VERBOSE) log("MentalStats()");
    }
    public MentalStats(List<Mental> mentals){
        if( VERBOSE) log("MentalStats(List<Mental>)");
        this.mentalList = mentals;
        calculate();
    }
    public MentalStats(Mental mental){
        calculate(mental);
    }

    public void add(Mental mental) {
        mentalList.add(mental);
    }

    /**
     * called if MentalStats is initiated with a list of mentals
     */
    private void calculate(){
        log("...calculate()");
        mentalList.forEach(System.out::println);
        if( mentalList.size() < 1){
            energy = 0;
            mood = 0;
            anxiety = 0;
            stress = 0;
        }else {
            energy = mentalList.stream().mapToInt(Mental::getEnergy).sum() / mentalList.size();
            if( VERBOSE) {
                int sumEnergy = mentalList.stream().mapToInt(Mental::getEnergy).sum();
                log("...energy", energy);
                log("...sum energy", sumEnergy);
                log(" mentals size", mentalList.size());
                log("sum/size", sumEnergy / mentalList.size());
            }
            mood = mentalList.stream().mapToInt(Mental::getMood).sum() / mentalList.size();
            anxiety = mentalList.stream().mapToInt(Mental::getAnxiety).sum() / mentalList.size();
            stress = mentalList.stream().mapToInt(Mental::getStress).sum() / mentalList.size();
        }
    }

    /**
     * not much to calculate here,
     * @param mental, the mental
     */
    private void calculate(Mental mental){
        log("...calculate(Mental)");
        energy = mental.getEnergy();
        anxiety = mental.getAnxiety();
        stress = mental.getStress();
        mood = mental.getMood();
    }
    @Deprecated
    public MentalStats divide(int denominator){
        MentalStats estimate = new MentalStats();
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
   public void plus(MentalStats other){
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
        return new Gson().toJson(this, MentalStats.class);
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "estimated time %s, energy %d", Converter.formatSecondsWithHours(duration), energy);
    }
}
