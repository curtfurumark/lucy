package se.curtrune.lucy;
import org.junit.Test;
import static org.junit.Assert.*;

import static se.curtrune.lucy.util.Logger.log;

import java.time.LocalDate;
import java.time.YearMonth;

import se.curtrune.lucy.classes.calender.CalenderMonth;

public class  MonthCalendarTest {
    @Test
    public void testFirstDateOfMonth(){
        log("...testFirstDateOfMonth()");
        CalenderMonth calenderMonth = new CalenderMonth(YearMonth.of(2024, 9));
        assertEquals(calenderMonth.getFirstDateOfMonth(), LocalDate.of(2024, 9, 1));
    }
    @Test
    public void testLastDateOfMonth(){
        log("...testFirstDateOfMonth()");
        CalenderMonth calenderMonth = new CalenderMonth(YearMonth.of(2024, 9));
        assertEquals(calenderMonth.getLastDateOfMonth(), LocalDate.of(2024, 9, 30));
    }
    @Test
    public void testFirstDate(){
        log("testFirstDate()");
        CalenderMonth calenderMonth = new CalenderMonth(YearMonth.of(2024, 9));
        assertEquals(calenderMonth.getFirstDate(), LocalDate.of(2024, 8, 26));
    }
    @Test
    public void testLastDate(){
        log("testLastDate()");
        CalenderMonth calenderMonth = new CalenderMonth(YearMonth.of(2024, 9));
        assertEquals(calenderMonth.getLastDate(), LocalDate.of(2024, 10, 6));

    }
}
