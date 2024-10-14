package se.curtrune.lucy;

import static org.junit.Assert.assertEquals;
import static se.curtrune.lucy.util.Logger.log;

import org.junit.Test;

import java.time.LocalDate;

import se.curtrune.lucy.classes.calender.Week;

public class WeekCalendarTest {
    @Test
    public void testNextWeek(){
        log("WeekCalendarTest.testNextWeek()");
        LocalDate date = LocalDate.of(2024, 10, 11);
        Week week = new Week(date);
        week = week.getNextWeek();
        int weekNumber = week.getWeekNumber();
        assertEquals(weekNumber, 42);
    }
    @Test
    public void testPreviousWeek(){
        log("...testPreviousWeek()");
        LocalDate date = LocalDate.of(2024, 10, 11);
        Week week = new Week(date);
        week = week.getPreviousWeek();
        int weekNumber = week.getWeekNumber();
        assertEquals(weekNumber, 40);

    }
}
