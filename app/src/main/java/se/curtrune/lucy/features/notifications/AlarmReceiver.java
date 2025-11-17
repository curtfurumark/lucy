package se.curtrune.lucy.features.notifications;


import static se.curtrune.lucy.util.Logger.log;

import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import se.curtrune.lucy.R;
import se.curtrune.lucy.screens.main.MainActivity2;


public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        log("AlarmReceiver(Context, Intent)");
        String message = intent.getStringExtra(EasyAlarm.INTENT_MESSAGE);
        String title = intent.getStringExtra(EasyAlarm.INTENT_TITLE);
        long itemID = intent.getLongExtra(EasyAlarm.INTENT_ITEM_ID, -1);
        log("onReceive() message: ", message);
        log("onReceive() title" , title);
        log("...itemID", itemID);
        Intent destinationIntent = new Intent(context, MainActivity2.class);
        destinationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, destinationIntent, PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Builder nb = new NotificationCompat.Builder(context, NotificationsWorker.CHANNEL_ONE)
                //.setSmallIcon(R.drawable.ic_launcher_background)
                .setSmallIcon(R.mipmap.ic_launcher_round) //wait and see
                .setContentTitle(title)
                .setContentText(message)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            log("...checkSelfPermission. POST_NOTIFICATIONS denied");
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManagerCompat.notify(123, nb.build());
    }
}

