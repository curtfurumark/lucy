package se.curtrune.lucy.util;


import static se.curtrune.lucy.util.Logger.log;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;

import androidx.core.app.NotificationCompat;

import se.curtrune.lucy.R;


public class EasyNotification {
    private static final String CHANNEL_ONE = "channel1";
    private static final String CHANNEL_ONE_NAME = "42:1";
    private static final String CHANNEL_TWO = "channel2";
    private static final String CHANNEL_TWO_NAME = "42:2";

    private NotificationManager notificationManager;
    private final Context context;


    public EasyNotification(Context context) {
        log("EasyNotification(Context");
        this.context = context;
        createChannels();

    }

    private void createChannels() {
        log("EasyNotification.createChannels()");
        initNotificationChannels();

    }

    public NotificationManager getManager() {
        log("EasyNotification.getManager()");
        if (notificationManager == null) {
            notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return notificationManager;
    }
    private void initNotificationChannels(){
        log("...initNotificationChannels()");
        NotificationChannel notificationChannel1 = new NotificationChannel(CHANNEL_ONE, CHANNEL_ONE_NAME, NotificationManager.IMPORTANCE_HIGH);
        notificationChannel1.enableLights(true);
        notificationChannel1.enableVibration(true);
        notificationChannel1.setLightColor(R.color.purple_200);
        notificationChannel1.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getManager().createNotificationChannel(notificationChannel1);

        NotificationChannel notificationChannel2 = new NotificationChannel(CHANNEL_TWO, CHANNEL_TWO_NAME, NotificationManager.IMPORTANCE_DEFAULT);
        notificationChannel2.enableLights(true);
        notificationChannel2.enableVibration(true);
        notificationChannel2.setLightColor(R.color.purple_200);
        notificationChannel2.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getManager().createNotificationChannel(notificationChannel2);


    }

    public void sendNotificationCH1(String title, String message) {
        log("sendNotificationCH1(String, String)");
        NotificationCompat.Builder nb = getChannel1Notification(title, message);
        getManager().notify(1, nb.build());
    }

    public void sendNotificationCH2(String title, String message) {
        log("...sendNotificationCH2() " + title + ", " + message);
        NotificationCompat.Builder nb = getChannel2Notification(title, message);
        getManager().notify(2, nb.build());
    }

    public NotificationCompat.Builder getChannel1Notification(String title, String message) {
        log(String.format("EasyNotification.getChannel1Notification(%s, %s)", title, message));
        return new NotificationCompat.Builder(context, CHANNEL_ONE)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_baseline_icecream_24);
    }

    public NotificationCompat.Builder getChannel2Notification(String title, String message) {
        return new NotificationCompat.Builder(context, CHANNEL_TWO)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_baseline_icecream_24);
    }

    public void sendNotificationCH1(se.curtrune.lucy.classes.Notification notification) {
        log("...sendNotificationCH1(Notification)");
        sendNotificationCH1(notification.getTitle(), notification.getContent());
    }
}


