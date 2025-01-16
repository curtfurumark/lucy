package se.curtrune.lucy;

import static junit.framework.TestCase.assertEquals;

import org.junit.Test;

import java.time.LocalDate;

import se.curtrune.lucy.persist.CalenderWorker;

public class TestCalendarWorker {
    @Test
    public void testGetWeekNumber(){
        int weekNumber = CalenderWorker.getWeekNumber(LocalDate.of(2024, 10, 14));
        assertEquals(weekNumber, 42);
    }
}
