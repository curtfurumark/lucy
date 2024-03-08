package se.curtrune.lucy.classes;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

public class Period {
    private enum Mode{
        DAYS, DAY_OF_WEEKS
    }
    private Mode mode = Mode.DAYS;
    private int days;
    private List<DayOfWeek> dayOfWeeks = new ArrayList<>();

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }
    public void add(DayOfWeek dayOfWeek){
        mode = Mode.DAY_OF_WEEKS;
        dayOfWeeks.add(dayOfWeek);
    }
}
