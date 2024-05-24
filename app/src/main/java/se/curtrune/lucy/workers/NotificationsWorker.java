package se.curtrune.lucy.workers;

import static se.curtrune.lucy.util.Logger.log;

import android.content.Context;

import java.time.LocalDate;
import java.util.List;

import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.Notification;
import se.curtrune.lucy.notifications.EasyAlarm;

public class NotificationsWorker {

    public static void setNotifications(LocalDate date, Context context){
        log("NotificationsWorker.setNotifications(LocalDate)");
        List<Item> items = ItemsWorker.selectTodayList(date, context);
        for( Item item: items){
            if( item.hasNotification()){
                setNotification(item, context);
            }
        }
    }
    public static void setNotification(Item item, Context context){
        log("...setNotification(Item)", item.getHeading());
        EasyAlarm easyAlarm = new EasyAlarm(item);
        easyAlarm.setAlarm(context);

    }
}
