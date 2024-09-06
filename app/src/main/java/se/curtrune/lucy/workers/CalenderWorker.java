package se.curtrune.lucy.workers;

import static se.curtrune.lucy.util.Logger.log;

import android.content.Context;

import java.time.LocalDate;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.Type;
import se.curtrune.lucy.classes.calender.CalenderDate;

public class CalenderWorker {
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

    public static LocalDate getFirstDateOfWeek(LocalDate date) {
        TemporalField fieldISO = WeekFields.of(Locale.getDefault()).dayOfWeek();
        return date.with(fieldISO, 1);
    }

    public static int getWeekNumber(LocalDate date) {
        TemporalField woy = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
        return date.get(woy);

    }
}
