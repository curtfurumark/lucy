package se.curtrune.lucy.classes;

import static se.curtrune.lucy.util.Logger.log;

import com.google.gson.Gson;

import java.io.Serializable;

public class Estimate implements Serializable {
    private long duration;
    private int energy;

    public Estimate() {
        log("Estimate()");
    }

    public long getDuration() {
        return duration;
    }

    public int getEnergy() {
        return energy;
    }
    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }
    public String toJson(){
        return new Gson().toJson(this, Estimate.class);
    }
}
