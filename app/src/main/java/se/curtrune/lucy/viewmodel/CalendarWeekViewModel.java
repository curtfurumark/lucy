package se.curtrune.lucy.viewmodel;

import static se.curtrune.lucy.util.Logger.log;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import se.curtrune.lucy.classes.calender.CalenderDate;
import se.curtrune.lucy.classes.calender.Week;
import se.curtrune.lucy.workers.CalenderWorker;

public class CalendarWeekViewModel extends ViewModel {
    private Week currentWeek;
    private MutableLiveData<List<CalenderDate>> calenderDates;
    public LiveData<List<CalenderDate>> getCalenderDates(){
        log("CalendarWeekViewModel.getCalendarDates()");
        if( calenderDates == null){
            log("\t\tcalenderDates == null");
            calenderDates = new MutableLiveData<>();
            calenderDates.setValue(new ArrayList<>());
        }
        return calenderDates;
    }

    public void set(Week week, Context context) {
        log("CalendarWeekViewModel.set(Week, Context)");
        List<CalenderDate>  dates = CalenderWorker.getEvents(week, context);
        log("\t\tnumber of dates", dates.size());
        if( calenderDates == null){
            calenderDates = new MutableLiveData<>();
        }
        calenderDates.setValue(dates);
    }
}
