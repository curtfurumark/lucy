package se.curtrune.lucy.classes;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class MedicineContent implements Content{
    private String name;
    private String type;
    private String doctor;
    private String dosage;
    private String bipacksedel;
    private List<LocalTime> takeYourMedicineTimes = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public String getBipacksedel() {
        return bipacksedel;
    }

    public void setBipacksedel(String bipacksedel) {
        this.bipacksedel = bipacksedel;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public void setTimes(LocalTime ... times) {
        for(LocalTime time: times){
            takeYourMedicineTimes.add(time);
        }
    }

    public int getNumTimes() {
        return takeYourMedicineTimes.size();
    }
}
