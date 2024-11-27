package se.curtrune.lucy.workers;

import static se.curtrune.lucy.util.Logger.log;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.time.LocalDate;
import java.util.List;

import se.curtrune.lucy.R;
import se.curtrune.lucy.classes.Item;
import se.curtrune.lucy.classes.Notification;
import se.curtrune.lucy.notifications.AlarmReceiver;
import se.curtrune.lucy.notifications.EasyAlarm;

public class NotificationsWorker {
    public static final String CHANNEL_ONE = "CHANNEL_ONE";
    private static final String CHANNEL_ONE_NAME = "lucinda notifications";
    public static void cancelNotification(Item item, Context context){
        log("...cancelNotification(Item, Context)", item.getHeading());
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int) item.getID(), intent, PendingIntent.FLAG_MUTABLE);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);

    }

    public static void createNotificationChannel(Context context){
        log("NotificationsWorker.createNotificationChannel(Context)");
        NotificationChannel notificationChannel1 = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationChannel1 = new NotificationChannel(CHANNEL_ONE, CHANNEL_ONE_NAME, NotificationManager.IMPORTANCE_HIGH);
            notificationChannel1.enableLights(true);
            notificationChannel1.enableVibration(true);
            notificationChannel1.setLightColor(R.color.purple_200);
            notificationChannel1.setLockscreenVisibility(android.app.Notification.VISIBILITY_PRIVATE);
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel1);
        }else{
            log("SDK_INT lower");
        }
    }

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
        log("NotificationWorker.setNotification(Item)", item.getHeading());
        EasyAlarm easyAlarm = new EasyAlarm(item);
        easyAlarm.setAlarm(context);
    }
    public void setNotification(Notification notification, Context context){
        log("...setNotification(Notification, Context)");

    }
}
