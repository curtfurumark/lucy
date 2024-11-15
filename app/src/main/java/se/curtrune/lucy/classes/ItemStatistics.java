package se.curtrune.lucy.classes;

import static se.curtrune.lucy.util.Logger.log;

import java.util.List;

public class ItemStatistics {
    private List<Item> items;
    private int energy;
    private int anxiety;
    private int mood;
    private int stress;
    private long duration;
    public ItemStatistics(List<Item> items){
        this.items = items;
        init();
    }
    private void init(){
        log("...init()");
        duration = items.stream().mapToLong(Item::getDuration).sum();
        energy = items.stream().mapToInt(Item::getEnergy).sum();
        stress = items.stream().mapToInt(Item::getStress).sum();
        anxiety = items.stream().mapToInt(Item::getStress).sum();
        mood = items.stream().mapToInt(Item::getMood).sum();
    }
    public int getAnxiety() {
        return anxiety;
    }
    public int getEnergy() {
        return energy;
    }
    public List<Item> getItems() {
        return items;
    }

    public int getMood() {
        return mood;
    }

    public int getStress() {
        return stress;
    }

    public long getDuration() {
        return duration;
    }
}
