package se.curtrune.lucy.classes;

import static se.curtrune.lucy.util.Logger.log;

import java.time.LocalDate;

public class EstimateDate {
    private LocalDate date;
    private int energyEstimate;
    private long durationEstimate;
    public EstimateDate(LocalDate date) {
        log("EstimateDate(LocalDate)");
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }

    public long getDurationEstimate(){
        return durationEstimate;
    }
    public int  getEnergyEstimate() {
        return energyEstimate;
    }

}
