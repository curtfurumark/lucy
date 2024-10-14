package se.curtrune.lucy.workers;

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
    public static final int COLOR_MINUS_5 = Color.RED;
    public static final int COLOR_MINUS_4 = Color.rgb(219, 13, 41);
    public static final int COLOR_MINUS_3 = Color.rgb(180, 13, 41 );
    public static final int COLOR_MINUS_2 = Color.rgb( 217, 85, 9);
    public static final int COLOR_MINUS_1 = Color.rgb(217, 85, 9);
    public static final int COLOR_0 = Color.rgb(217, 217, 11);
    public static final int COLOR_PLUS_1 = Color.rgb(9, 120, 5);

    public static final int COLOR_PLUS_2 = Color.rgb(9, 120, 5);
    public static final int COLOR_PLUS_3 = Color.rgb(9, 120, 5);
    public static final int COLOR_PLUS_4 = Color.rgb(9, 120, 5);
    public static final int COLOR_PLUS_5 = Color.rgb(9, 120, 5);
    public static List<CalenderDate> getAppointments(LocalDate firstDate, LocalDate lastDate, Context context) {
        log("CalenderWorker.getAppointments(LocalDate, LocalDate, Context)");
        List<CalenderDate> calenderDates = new ArrayList<>();
        LocalDate currentDate = firstDate;
        while (currentDate.isBefore(lastDate) || currentDate.equals(lastDate)) {
            CalenderDate calenderDate = new CalenderDate(currentDate);
            List<Item> items = ItemsWorker.selectAppointments(currentDate, context);
            calenderDate.setItems(items);
            calenderDates.add(calenderDate);
            currentDate = currentDate.plusDays(1);
        }
        return calenderDates;
    }
    public static List<CalenderDate> getCalenderDates(LocalDate firstDate, LocalDate lastDate, Context context) {
        log("...getCalenderDates()");
        List<CalenderDate> calenderDates = new ArrayList<>();
        LocalDate currentDate = firstDate;
        while (currentDate.isBefore(lastDate) || currentDate.equals(lastDate)) {
            CalenderDate calenderDate = new CalenderDate(currentDate);
            List<Item> items = ItemsWorker.selectCalenderItems(currentDate, context);
            //List<Item> items = ItemsWorker.selectItems(Type.APPOINTMENT, )
            calenderDate.setItems(items);
            calenderDates.add(calenderDate);
            currentDate = currentDate.plusDays(1);
        }
        return calenderDates;
    }
    public  static List<CalenderDate> getCalenderDates(Type type, LocalDate firstDate, LocalDate lastDate,Context context){
        log("...getCalenderDates(Type, LocalDate, LocalDate, Context");
        List<CalenderDate> calenderDates = new ArrayList<>();
        LocalDate currentDate = firstDate;
        while (currentDate.isBefore(lastDate) || currentDate.equals(lastDate)) {
            CalenderDate calenderDate = new CalenderDate(currentDate);
            List<Item> items = ItemsWorker.selectAppointments(currentDate, context);
            calenderDate.setItems(items);
            calenderDates.add(calenderDate);
            currentDate = currentDate.plusDays(1);
        }
        return calenderDates;
    }
    public static List<CalenderDate> getCalenderDates(Week week,Context context){
        log("CalendarWorker.getCalenderDates(Week)");
        return getCalenderDates(week.getFirstDateOfWeek(), week.getLastDateOfWeek(), context);

    }

    public static List<CalenderDate> getEvents(Week week, Context context) {
        log("CalendarWorker.getEvents(Week, Context)");
        //List<Item> items = ItemsWorker.selectEvents(week, context);
        return getCalenderDates(week, context);
    }

    private CalenderMonth getCalenderMonth(YearMonth yearMonth, Context context){
        log("...getCalenderMonth(YearMonth)", yearMonth.toString());
        CalenderMonth calenderMonth = new CalenderMonth(yearMonth);
        calenderMonth.setCalenderDates(getAppointments(calenderMonth.getFirstDate(), calenderMonth.getLastDate(), context));
        return calenderMonth;
    }
    private static int getEnergyColor(int energy){
        if( energy < - 5){
            return COLOR_MINUS_5;
        }
        if( energy > 5){
            return COLOR_PLUS_5;
        }
        switch (energy){
            case -5:
                return COLOR_MINUS_5;
            case -4:
                return COLOR_MINUS_4;
            case -3:
                return  COLOR_MINUS_3;
            case -2:
                return COLOR_MINUS_2;
            case -1:
                return COLOR_MINUS_1;
            case 0:
                return COLOR_0;
            case 1:
                return COLOR_PLUS_1;
            case 2:
                return COLOR_PLUS_2;
            case 3:
                return COLOR_PLUS_3;
            case 4:
                return COLOR_PLUS_4;
            case 5:
                return COLOR_PLUS_5;
        }
        return 0;
    }
    public static List<Item> getMentalColour(List<Item> items){
        log("...getMentalColor(List<Item>)");
        List<Item> colouredItems = new ArrayList<>();
        int currentEnergy = 0;
        for(int i = 0; i < items.size(); i++){
            Item item = items.get(i);
            currentEnergy += item.getEnergy();
            item.setColor(getEnergyColor(currentEnergy));
            colouredItems.add(item);
        }
        return colouredItems;
    }

    public static LocalDate getFirstDateOfWeek(LocalDate date) {
        TemporalField fieldISO = WeekFields.of(Locale.getDefault()).dayOfWeek();
        return date.with(fieldISO, 1);
    }

    public static int getWeekNumber(LocalDate date) {
        TemporalField woy = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
        return date.get(woy);

    }


    public static List<CalenderDate> getCalenderDates(YearMonth yearMonth, Context context) {
        log("...getCalenderDates(YearMonth)", yearMonth.toString());
        List<CalenderDate> calenderDates = new ArrayList<>();
        //yearMonth.atDay(1)
        log("...yearMonth", yearMonth.toString());
        //LocalDate firstDateOfMonth =  date.withDayOfMonth( 1);
        LocalDate firstDateOfMonth  = yearMonth.atDay(1);
        log("...firstDateOfMonth", firstDateOfMonth.toString());
        int daysInMonth = yearMonth.lengthOfMonth();
        log("...daysInMonth", daysInMonth);
        //List<Item> itemsMonth = ItemsWorker.selectCalenderItems(yearMonth, getContext());
        List<Item> itemsMonth = ItemsWorker.selectAppointments(yearMonth, context);
        log("...number of events this month", itemsMonth.size());
        itemsMonth.forEach(System.out::println);
        LocalDate currentDate = firstDateOfMonth;
        //int numberDays = daysInMonth;
        int firstDate = firstDateOfMonth.getDayOfWeek().getValue();
        int offset = firstDate;
        currentDate = currentDate.minusDays(offset - 1);
        log("firstDate", firstDate);
        for( int i = 1; i <= 42; i++){
            CalenderDate calenderDate = new CalenderDate();
            calenderDate.setDate(currentDate);
            //calenderDate.setItems(new ArrayList<>());
            LocalDate finalCurrentDate = currentDate;
            calenderDate.setItems(itemsMonth.stream().filter(item -> item.isDate(finalCurrentDate)).collect(Collectors.toList()));
            calenderDates.add(calenderDate);
            currentDate = currentDate.plusDays(1);
        }
        return calenderDates;
    }
}
