package se.curtrune.lucy.classes;

import java.time.LocalDate;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static se.curtrune.lucy.util.Logger.log;

public class Week {
    private final LocalDate firstDate;
    private final LocalDate currentDate;
    private List<LocalDate> dates;
    public Week(LocalDate date){
        log("Week(LocalDate)", date.toString());
        this.currentDate = date;
        firstDate = calculateFirstDateOfTheWeek(currentDate);
        dates = getDates();
    }
    private LocalDate calculateFirstDateOfTheWeek(LocalDate date){
        TemporalField dayOfWeek = WeekFields.ISO.dayOfWeek();
        return date.with(dayOfWeek, dayOfWeek.range().getMinimum());
    }
    public LocalDate getCurrentDate(){
        return currentDate;
    }
    public List<LocalDate> getDates(){
        dates = new ArrayList<>();
        dates.add(firstDate);
        for( int i = 1; i <= 6;i++){
            dates.add(firstDate.plusDays(i));
        }
        return dates;
    }
    public LocalDate getFirstDateOfWeek(){
        return firstDate;
    }
    public LocalDate getMonday(){
        return firstDate;
    }
    public int getWeekNumber(){
        return currentDate.get(WeekFields.of(Locale.getDefault()).weekOfYear());
    }
}
