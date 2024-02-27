package se.curtrune.lucy.statistics;

import java.time.LocalDate;
import java.util.List;

import se.curtrune.lucy.classes.Mental;

public class MentalDateListable {
    private LocalDate date;
    private List<Mental> mentals;

    public MentalDateListable(LocalDate date, List<Mental> mentals) {
        this.date = date;
        this.mentals = mentals;
    }
}
