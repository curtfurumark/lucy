package se.curtrune.lucy.workers;

import static se.curtrune.lucy.util.Logger.log;

import android.content.Context;

import java.util.List;

import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.persist.LocalDB;

public class PanicWorker {
    public static void performPanicAction(){
        log("...performPanicAction()");
    }
    public static String getUserDefinedUrl(){
        log("...getUserDefinedUrl()");
        return "https://bongo.cat";
    }

    public static List<Item> getPanicList(Context context) {
        log("PanicWorker.getPanicList()");
        //return ItemsWorker.selectItem(741, context);
        return ItemsWorker.selectChildren(741, context);
    }


}
