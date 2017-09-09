package net.ridhoperdana.asumu;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import net.ridhoperdana.asumu.activity.MainActivity;

/**
 * Created by RIDHO on 9/9/2017.
 */

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent repeating_intent = new Intent(context, MainActivity.class);
        repeating_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 100, repeating_intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(context)
                .setContentTitle("Confirm your expenses!")
                .setContentText("or It will be send automatically")
                .setSmallIcon(R.drawable.ic_notification_logo)
                .setContentIntent(pendingIntent)
                .setDefaults(NotificationCompat.DEFAULT_ALL);

        notificationManager.notify(100, notifyBuilder.build());
        Log.d("masuk sini", "notif");
//        Notification myNotification = notifyBuilder.build();
//        mNotifyManager.notify(NOTIFICATION_ID, myNotification);
    }
}
