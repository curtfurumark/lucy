package se.curtrune.lucy;

import static org.junit.Assert.assertEquals;
import static se.curtrune.lucy.util.Logger.log;

import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.Repeat;
import se.curtrune.lucy.workers.RepeatWorker;

public class RepeatWorkerTest {
    @Test
    public void testRepeatWorker(){
        log("testRepeatWorker()");
/*        Item item = new Item();
        Repeat repeat = new Repeat();
        repeat.setPeriod(2, Repeat.Unit.WEEK);
        repeat.setLastDate(LocalDate.of(2024, 12, 31));
        repeat.setFirstDate(LocalDate.of(2025, 11, 9));
        item.setRepeat(repeat);
        List<Item> items =  RepeatWorker.createInstances(repeat);
        log("...number of items", items.size());
        assertEquals(items.size(), 1);*/
    }

    /**
     * this is what i expect
     * 20241104
     * 20241111
     * 20241118
     * 20241125,
     */
    @Test
    public void testEachWeek01(){
        log("...testEachWeek01()");
/*        Repeat repeat = new Repeat();
        repeat.setPeriod(1, Repeat.Unit.WEEK);
        //start monday 4 november 2024
        repeat.setFirstDate(LocalDate.of(2024, 11, 4));
        //last time
        repeat.setLastDate(LocalDate.of(2024, 11, 25));
        Item item = new Item("4 mondays");
        item.setRepeat(repeat);
        List<Item> items = RepeatWorker.createInstances2(item);
        printDates(items);
        assertEquals(4, items.size());*/
    }
    /**
     * this is what i expect
     * 20241104
     * 20241111
     * 20241118
     */
    @Test
    public void testEachWeek02(){
        log("...testEachWeek02()");
/*        Repeat repeat = new Repeat();
        repeat.setPeriod(1, Repeat.Unit.WEEK);
        //start monday 4 november 2024
        repeat.setFirstDate(LocalDate.of(2024, 11, 4));
        //end sunday 24 november 2024
        repeat.setLastDate(LocalDate.of(2024, 11, 24));
        Item item = new Item("3 mondays");
        item.setRepeat(repeat);
        List<Item> items = RepeatWorker.createInstances2(item);
        printDates(items);
        assertEquals(3, items.size());*/
    }

    /**
     * expect
     * 2024-01-01
     * ...
     * 2024-01-31
     */
    @Test
    public void testInfinity() {
        log("RepeatWorkerTest.testInfinity()");
/*        Repeat repeat = new Repeat();
        repeat.setFirstDate(LocalDate.of(2024, 1, 1));
        repeat.setPeriod(1, Repeat.Unit.DAY);
        repeat.setInfinity(true);
        Item item = new Item("brush your teeth");
        item.setRepeat(repeat);
        RepeatWorker.setMaxDate(LocalDate.of(2024, 1, 30));
        List<Item> items = RepeatWorker.createInstances2(item);
        assertEquals(30, items.size());
        printDates(items)*/;
        //log(repeat);
    }
    @Test
    public void testLogRepeat(){
        log("...testLogRepeat()");
        Repeat repeat = new Repeat();
        repeat.setPeriod(1, Repeat.Unit.DAY);
        repeat.setFirstDate(LocalDate.now());
        repeat.setInfinity(true);
        log(repeat);
        assertEquals(repeat.getUnit(), Repeat.Unit.DAY);
    }
    @Test
    public void testCheckUpdateNeededTrue(){
        log("...testCheckUpdateNeededTrue()");
        Repeat repeat = new Repeat();
        repeat.setFirstDate(LocalDate.now().minusMonths(1));
        repeat.setLastDate(LocalDate.now().plusMonths(1).minusDays(1));
        RepeatWorker.VERBOSE = true;
        boolean updateNeeded = RepeatWorker.updateNeeded(repeat);
        assertEquals(true, updateNeeded);
    }
    @Test
    public void testCheckUpdateNeededFalse(){
        Repeat repeat = new Repeat();
        repeat.setFirstDate(LocalDate.now().minusMonths(1));
        repeat.setLastDate(LocalDate.now().plusMonths(2));
        RepeatWorker.VERBOSE = true;
        boolean updateNeeded = RepeatWorker.updateNeeded(repeat);
        assertEquals(false, updateNeeded);
    }
    @Test
    public void testUpdate(){
        log("...testUpdate()");
    }
    private void printDates(List<Item> items){
        log("...printDates(List<Item>)");
        for (Item item:items) {
            log("\t\tdate", item.getTargetDate());
        }
    }
}
