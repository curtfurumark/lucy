package se.curtrune.lucy;

import org.junit.Test;
import static org.junit.Assert.*;

import static se.curtrune.lucy.util.Logger.log;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Period;

import se.curtrune.lucy.classes.Repeat;

public class RepeatTest {
    @Test
    public void repeatOneWeek(){
        Repeat repeat = new Repeat();
        repeat.setPeriod(2, Repeat.Period.WEEK);
        LocalDate nextDate = repeat.getNextDate(Repeat.Period.WEEK, 1);
        assertEquals(LocalDate.now().plusDays(7), nextDate);
    }
    @Test
    public void repeatEveryOtherWeek(){
        Repeat repeat = new Repeat();
        LocalDate nextDate = repeat.getNextDate(Repeat.Period.WEEK, 2);
        assertEquals(LocalDate.now().plusWeeks(2), nextDate);
    }
    @Test
    public void failEveryOtherWeek(){
        log("...failEveryOtherWeek()");
        Repeat repeat = new Repeat();
        repeat.setPeriod(2, Repeat.Period.WEEK);
        LocalDate nextDate = repeat.getNextDate();
        assertNotEquals(nextDate, LocalDate.now().plusWeeks(2));

    }
    @Test
    public void repeatOneDay(){
        log("...repeatOneDay()");
        Repeat repeat = new Repeat();
        LocalDate nextDate = repeat.getNextDate(Repeat.Period.DAY, 1);
        assertEquals(LocalDate.now().plusDays(1), nextDate);
    }
    @Test
    public void repeatDayOfWeek(){
        log("...repeatOneDay()");
        Repeat repeat = new Repeat();
        LocalDate nextDate = repeat.getNextDate(Repeat.Period.DAY, 1);
        assertEquals(LocalDate.now().plusDays(1), nextDate);
    }
    @Test
    public void repeatMonday(){
        log("...repeatMonday");
        Repeat repeat = new Repeat();
        repeat.add(DayOfWeek.MONDAY);
        //repeat.add(DayOfWeek.FRIDAY);
        ///LocalDate currentDate = LocalDate.now().
        repeat.setCurrentDate(LocalDate.of(1963, 10, 31));
        LocalDate nextDate = repeat.getNextDate(Repeat.Period.DAYS_OF_WEEK, 1);
        LocalDate expectedDate = LocalDate.of(2024, 7, 8);
        assertEquals(expectedDate, nextDate);
    }
}
