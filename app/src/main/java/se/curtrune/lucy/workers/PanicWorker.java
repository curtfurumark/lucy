package se.curtrune.lucy.workers;

import static se.curtrune.lucy.util.Logger.log;

public class PanicWorker {
    public static void performPanicAction(){
        log("...performPanicAction()");
    }
    public static String getUserDefinedUrl(){
        log("...getUserDefinedUrl()");
        return "https://bongo.cat";
    }
}
