package se.curtrune.lucy.viewmodel;

import static se.curtrune.lucy.util.Logger.log;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.calender.CalenderDate;
import se.curtrune.lucy.classes.calender.CalenderMonth;
import se.curtrune.lucy.workers.CalenderWorker;

public class CalendarMonthViewModel extends ViewModel {
    private MutableLiveData< List<CalenderDate>> mutableCalendarDates;
    private List<CalenderDate> calenderDates;
    private YearMonth yearMonth;

    public void setYearMonth(YearMonth yearMonth) {
        this.yearMonth = yearMonth;
    }

    public void add(Item item) {
        log("...add(Item)");
        CalenderDate calenderDate = getCalendarDate(item.getTargetDate());
        calenderDate.add(item);
    }
    private CalenderDate getCalendarDate(LocalDate date){
        List<CalenderDate> calenderDates = mutableCalendarDates.getValue();
        for(int i = 0; i < calenderDates.size(); i++){
            if( calenderDates.get(i).getDate().equals(date)){
                return calenderDates.get(i);
            }
        }
        return null;
    }
    public LiveData<List<CalenderDate>> getCalendarDates(){
        return mutableCalendarDates;
    }
    public List<CalenderDate> getCalendarDates(YearMonth yearMonth, Context context){
        //CalenderMonth calenderMonth = new CalenderMonth(yearMonth);
        this.yearMonth = yearMonth;
        calenderDates =  CalenderWorker.getCalenderDates(yearMonth, context);
        return calenderDates;
    }
}
