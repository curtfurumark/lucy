package se.curtrune.lucy.notifications;


import static se.curtrune.lucy.util.Logger.log;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;

import se.curtrune.lucy.R;


public class EasyNotification {
    public static final String CHANNEL_ONE = "CHANNEL_ONE";
    private static final String CHANNEL_ONE_NAME = "lucinda notifications";

    public static void createNotificationChannel(Context context){
        log("EasyNotification.createNotificationChannel(Context)");
        NotificationChannel notificationChannel1 = new NotificationChannel(CHANNEL_ONE, CHANNEL_ONE_NAME, NotificationManager.IMPORTANCE_HIGH);
        notificationChannel1.enableLights(true);
        notificationChannel1.enableVibration(true);
        notificationChannel1.setLightColor(R.color.purple_200);
        notificationChannel1.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(notificationChannel1);
    }
}


