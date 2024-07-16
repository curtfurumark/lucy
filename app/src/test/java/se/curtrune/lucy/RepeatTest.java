package se.curtrune.lucy;

import org.junit.Test;
import static org.junit.Assert.*;

import static se.curtrune.lucy.util.Logger.log;

import java.time.DayOfWeek;
import java.time.LocalDate;

import se.curtrune.lucy.classes.Repeat;

public class RepeatTest {
    @Test
    public void repeatOneWeek(){
        Repeat repeat = new Repeat();
        repeat.setPeriod(2, Repeat.Unit.WEEK);
        LocalDate nextDate = repeat.getNextDate();
        assertEquals(LocalDate.now().plusWeeks(1), nextDate);
    }
    @Test
    public void repeatEveryOtherWeek(){
        Repeat repeat = new Repeat();
        repeat.setPeriod(2, Repeat.Unit.WEEK);
        LocalDate nextDate = repeat.getNextDate();
        assertEquals(LocalDate.now().plusWeeks(2), nextDate);
    }
    @Test
    public void failEveryOtherWeek(){
        log("...failEveryOtherWeek()");
        Repeat repeat = new Repeat();
        repeat.setPeriod(2, Repeat.Unit.WEEK);
        LocalDate nextDate = repeat.getNextDate();
        assertNotEquals(nextDate, LocalDate.now().plusWeeks(2));

    }
    @Test
    public void repeatOneDay(){
        log("...repeatOneDay()");
        Repeat repeat = new Repeat();
        repeat.setPeriod(1, Repeat.Unit.DAY);
        LocalDate nextDate = repeat.getNextDate();
        assertEquals(LocalDate.now().plusDays(1), nextDate);
    }
    @Test
    public void repeatDayOfWeek(){
        log("...repeatOneDay()");
        Repeat repeat = new Repeat();
        repeat.setPeriod(1, Repeat.Unit.DAY);
        LocalDate nextDate = repeat.getNextDate();
        assertEquals(LocalDate.now().plusDays(1), nextDate);
    }
    @Test
    public void repeatMonday(){
        log("...repeatMonday");
/*        Repeat repeat = new Repeat();
        repeat.add(DayOfWeek.MONDAY);
        repeat.setCurrentDate(LocalDate.of(1963, 10, 31));
        LocalDate nextDate = repeat.getNextDate(Repeat.Unit.DAYS_OF_WEEK, 1);
        LocalDate expectedDate = LocalDate.of(2024, 7, 8);
        assertEquals(expectedDate, nextDate);*/
    }
}
