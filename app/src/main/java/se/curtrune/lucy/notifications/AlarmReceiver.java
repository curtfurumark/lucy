package se.curtrune.lucy.notifications;


import static se.curtrune.lucy.util.Logger.log;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;


public class AlarmReceiver extends BroadcastReceiver {

    private AlarmCallback callback;


    @Override
    public void onReceive(Context context, Intent intent) {
        log("AlarmReceiver(Context, Intent)");
        String message = intent.getStringExtra(EasyAlarm.INTENT_MESSAGE);
        String title = intent.getStringExtra(EasyAlarm.INTENT_TITLE);
        log("onReceive() message: " + message);
        log("onReceive() title" + title);
        NotificationHelper notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder nb = notificationHelper.getChannel1Notification(title, message);
        notificationHelper.getManager().notify(1, nb.build());
    }
}

