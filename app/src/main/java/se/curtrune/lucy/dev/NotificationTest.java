package se.curtrune.lucy.dev;

import static se.curtrune.lucy.util.Logger.log;

import android.content.Context;

import java.time.LocalDate;
import java.time.LocalDateTime;

import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.Notification;
import se.curtrune.lucy.notifications.EasyAlarm;
import se.curtrune.lucy.workers.ItemsWorker;
import se.curtrune.lucy.workers.NotificationsWorker;

public class NotificationTest {
    public static boolean notificationSet = false;
    public static Item notificationItem;
    public static void cancelAlarm(long itemID, Context context){
        log("NotificationTest.cancelAlarm(long, Context), itemID", itemID);
        Item item = ItemsWorker.selectItem(itemID, context);
        if( item == null){
            log("ERROR item is null");
            return;
        }
        //log(item);
        NotificationsWorker.cancelNotification(item, context);

    }
    public Item createItemWithNotification(){
        log("...createItemWithNotification()");

        return null;
    }

    public static void setNotification(String heading, String content, LocalDateTime dateTime, Context context) {
        log("NotificationTest.setNotification(String, String, LocalDateTime, Context)");
        Item item = new Item(heading);
        item.setComment(content);
        item.setTargetTime(dateTime.toLocalTime());
        item.setTargetDate(dateTime.toLocalDate());
        Notification notification = new Notification();
        notification.setType(Notification.Type.NOTIFICATION);
        notification.setTitle(heading);
        notification.setContent(content);
        notification.setDate(dateTime.toLocalDate());
        notification.setTime(dateTime.toLocalTime());
        item.setNotification(notification);
        item = ItemsWorker.insert(item, context);
        EasyAlarm easyAlarm = new EasyAlarm(item);
        easyAlarm.setAlarm(context);
        log(item);
        notificationItem = item;
        notificationSet = true;
    }
}
