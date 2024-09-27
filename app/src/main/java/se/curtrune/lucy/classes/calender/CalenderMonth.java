package se.curtrune.lucy.classes.calender;

import static se.curtrune.lucy.util.Logger.log;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

/**
 * represents a month of appointments with leading and trailing dates'
 * ie always starting with a monday
 * 42 dates
 * don't know if it handles locale week starts sunday
 */
public class CalenderMonth {
    private YearMonth yearMonth;
    private LocalDate firstDate;
    private LocalDate firstDateOfMonth;
    private LocalDate lastDateOfMonth;
    private LocalDate lastDate;
    private List<CalenderDate> calenderDates;

    public CalenderMonth(YearMonth yearMonth) {
        this.yearMonth = yearMonth;
        init();
    }
    public void addCalendarDate(CalenderDate calenderDate){
        CalenderDate calenderDate1 = getCalenderDate(calenderDate.getDate());
        calenderDate1.addItems(calenderDate.getItems());
    }
    public CalenderDate getCalenderDate(LocalDate date){
        return calenderDates.stream().filter(calenderDate -> calenderDate.getDate().equals(date)).findAny().orElse(null);
    }
    public List<CalenderDate> getCalenderDates(){
        return calenderDates;
    }
    public LocalDate getFirstDate(){
        return firstDate;
    }
    public LocalDate getFirstDateOfMonth(){
        return firstDateOfMonth;
    }
    public LocalDate getLastDate(){
        return lastDate;
    }
    public LocalDate getLastDateOfMonth(){
        return lastDateOfMonth;
    }
    private void init(){
        log("...init()");
        firstDateOfMonth = yearMonth.atDay(1);
        lastDateOfMonth = yearMonth.atEndOfMonth();
        int offset = firstDateOfMonth.getDayOfWeek().getValue();
        firstDate = firstDateOfMonth.minusDays(offset - 1);
        lastDate = firstDate.plusDays(41);
    }

    public void setCalenderDates(List<CalenderDate> calenderDates) {
        this.calenderDates = calenderDates;
    }
}
