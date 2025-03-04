package se.curtrune.lucy.persist;

import static se.curtrune.lucy.util.Logger.log;

import android.content.Context;
import android.graphics.Color;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.Type;
import se.curtrune.lucy.classes.calender.CalenderDate;
import se.curtrune.lucy.classes.calender.CalenderMonth;
import se.curtrune.lucy.classes.calender.Week;

public class CalenderWorker {
    public static boolean VERBOSE = false;
    /**
     * this one is supposed to return items to be shown in week or month calender
     * @param firstDate, i wonder
     * @param lastDate, your guess is as good as mine
     * @param context, i need context, we all need context, without context we are nothing
     * @return a list of appointments as calendarDates
     */
    public static List<CalenderDate> getCalenderDates(LocalDate firstDate, LocalDate lastDate, Context context) {
        log("CalendarWorker.getCalenderDates(LocalDate, LocalDate, Context)");
        List<CalenderDate> calenderDates = new ArrayList<>();
        LocalDate currentDate = firstDate;
        while (currentDate.isBefore(lastDate) || currentDate.equals(lastDate)) {
            CalenderDate calenderDate = new CalenderDate(currentDate);
            List<Item> items = ItemsWorker.selectCalenderItems(currentDate, context);
            calenderDate.setItems(items);
            calenderDates.add(calenderDate);
            currentDate = currentDate.plusDays(1);
        }
        return calenderDates;
    }

    public static List<CalenderDate> getCalenderDates(YearMonth yearMonth, Context context) {
        log("CalenderWorker.getCalenderDates(YearMonth, Context)", yearMonth.toString());
        List<CalenderDate> calenderDates = new ArrayList<>();
        LocalDate firstDateOfMonth  = yearMonth.atDay(1);
        int daysInMonth = yearMonth.lengthOfMonth();
        List<Item> itemsMonth = ItemsWorker.selectIsCalenderItems(yearMonth, context);
        if( VERBOSE) {
            log("...number of events this month", itemsMonth.size());
            itemsMonth.forEach(System.out::println);
        }
        LocalDate currentDate = firstDateOfMonth;
        int firstDate = firstDateOfMonth.getDayOfWeek().getValue();
        int offset = firstDate;
        currentDate = currentDate.minusDays(offset - 1);
        if(VERBOSE) log("firstDate", firstDate);
        for( int i = 1; i <= 42; i++){
            CalenderDate calenderDate = new CalenderDate();
            calenderDate.setDate(currentDate);
            LocalDate finalCurrentDate = currentDate;
            calenderDate.setItems(itemsMonth.stream().filter(item -> item.isDate(finalCurrentDate)).collect(Collectors.toList()));
            calenderDates.add(calenderDate);
            currentDate = currentDate.plusDays(1);
        }
        return calenderDates;
    }
}
