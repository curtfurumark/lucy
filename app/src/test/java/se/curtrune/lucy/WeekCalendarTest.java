package se.curtrune.lucy;

import static org.junit.Assert.assertEquals;
import static se.curtrune.lucy.util.Logger.log;

import org.junit.Test;

import java.time.LocalDate;
import java.time.Month;

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
    @Test
    public void testPlusWeeks(){
        log("...testPlusWeeks()");
        Week week = new Week(LocalDate.of(2024, 12, 18));
        Week nextWeek = week.plusWeek(1);
        assertEquals(LocalDate.of(2024, 12, 23), nextWeek.getFirstDateOfWeek());
    }
    @Test
    public void testYear(){
        log("...testYear()");
        Week week = new Week(LocalDate.of(2025, 12, 18));
        int year = week.getYear();
        assertEquals(year, 2025);
    }
    @Test
    public void testMonth(){
        log("...testMonth()");
        Week week = new Week(LocalDate.of(2025, 12, 18));
        assertEquals(Month.DECEMBER, week.getMonth());
    }
}
