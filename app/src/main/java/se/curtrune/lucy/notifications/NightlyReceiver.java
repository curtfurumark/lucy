package se.curtrune.lucy.notifications;

import static se.curtrune.lucy.util.Logger.log;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NightlyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        log("NightlyReceiver(Context, Intent)");
    }
}
