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
    private Week currentWeek = new Week();
    private MutableLiveData<Week> mutableWeek = new MutableLiveData<>();
    private int numWeeks = 10;
    /**
     * should be sort of half of the total number of weeks
     * and represents initial week, the current week for the user
     */
    private final int initialWeek = 5;
    private int currentPage = initialWeek;
    private MutableLiveData<List<CalenderDate>> calenderDates = new MutableLiveData<>();
    public CalendarWeekViewModel(){
        currentWeek = new Week();
        mutableWeek.setValue(currentWeek);
    }
    public LiveData<List<CalenderDate>> getCalenderDates(){
        log("CalendarWeekViewModel.getCalendarDates()");
        return calenderDates;
    }
    public LiveData<Week> getWeek(){
        return mutableWeek;
    }

    public int getInitialPage(){
        return initialWeek;
    }
    public int getNumWeeks(){
        return numWeeks;
    }
    public void onPage(int pageIndex){
        log("CalendarWeekViewModel.onPage(int)", pageIndex);
        if( currentPage != pageIndex){
            currentWeek = currentWeek.plusWeek(pageIndex - initialWeek);
            mutableWeek.setValue(currentWeek);
            log("....new week number", currentWeek.getWeekNumber());
        }

    }
    public void set(Week week, Context context) {
        log("CalendarWeekViewModel.set(Week, Context)", week.toString());
        currentWeek = week;
        mutableWeek.setValue(week);
        List<CalenderDate>  dates = CalenderWorker.getEvents(week, context);
        log("\t\tnumber of dates", dates.size());
        if( calenderDates == null){
            calenderDates = new MutableLiveData<>();
        }
        calenderDates.setValue(dates);
    }
}
