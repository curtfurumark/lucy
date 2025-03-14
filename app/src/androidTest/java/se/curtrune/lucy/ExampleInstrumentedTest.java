package se.curtrune.lucy;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static se.curtrune.lucy.util.Logger.log;

import se.curtrune.lucy.classes.item.Item;
import se.curtrune.lucy.persist.ItemsWorker;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("se.curtrune.lucy", appContext.getPackageName());
    }
    @Test
    public void testMediaItem(){
        log("...testMediaItem()");
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Item item = ItemsWorker.selectItem(4244, appContext);
        if(item != null){
            log("item heading", item.getHeading());
        }
        assertEquals("root", item.getHeading());
    }
}