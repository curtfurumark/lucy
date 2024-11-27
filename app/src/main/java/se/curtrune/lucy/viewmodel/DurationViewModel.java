package se.curtrune.lucy.viewmodel;

import static se.curtrune.lucy.util.Logger.log;

import android.content.Context;

import androidx.lifecycle.ViewModel;

import java.time.LocalDate;
import java.util.List;

import se.curtrune.lucy.classes.Listable;
import se.curtrune.lucy.statistics.DurationStatistics;

public class DurationViewModel extends ViewModel {
    private DurationStatistics durationStatistics;
    public void set(LocalDate firstDate, LocalDate lastDate, Context context) {
        log("DurationViewModel.set(LocalDate, LocalDate, Context)");
        durationStatistics = new DurationStatistics(firstDate, lastDate, context);
    }
    public DurationStatistics getDurationStatistics(){
        return durationStatistics;
    }
    public List<Listable> getDurationByCategory(){
        return durationStatistics.getCategoryListables();
    }
    public List<Listable> getDurationByDate(){
        return durationStatistics.getDateListables();
    }
}
