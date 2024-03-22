package se.curtrune.lucy.classes;

import static se.curtrune.lucy.util.Logger.log;

public class Estimate {
    private long duration;
    private int energy;

    public Estimate() {
        log("Estimate()");
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }
}
