package se.curtrune.lucy;

import static org.junit.Assert.assertEquals;
import static se.curtrune.lucy.util.Logger.log;

import org.junit.Test;

import java.time.LocalTime;

import se.curtrune.lucy.classes.MedicineContent;

public class MedicineTest {
    @Test
    public void testMedicineOne(){
        log("testMedicineOne()");
        MedicineContent content = new MedicineContent();
        content.setName("brintellix");
        content.setTimes(LocalTime.of(8, 0));
        int numTimes = content.getNumTimes();
        assertEquals(1, numTimes);
    }
}
