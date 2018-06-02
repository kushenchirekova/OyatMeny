package com.example.aluad.p.MathAlarm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

import com.example.aluad.p.R;
import com.mrkostua.mathalarm.Tools.ConstantValues;


class AlarmNotifications {
    private static final String CHANNEL_ID = "media_playback_channel";
    private Context context;

    AlarmNotifications(Context context) {
        this.context = context;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChanel();
        }
    }


    //notification for MathService
    Notification NewNotification() {
        // snooze button
        Intent snoozeIntent = new Intent(ConstantValues.INSTANCE.getSNOOZE_ACTION());
        snoozeIntent.setClass(context, Alarm_Receiver.class);
        PendingIntent piSnooze = PendingIntent.getBroadcast(context, 0, snoozeIntent, 0);
        snoozeIntent.setClass(context, Alarm_Receiver.class);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);
        builder
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setContentText(context.getString(R.string.AN_snoozeContentText))
                .setContentTitle(context.getString(R.string.AN_snoozeContentTitle))
                .setSmallIcon(R.drawable.ic_alarm_white_36dp)
                .setTicker(context.getString(R.string.AN_snoozeTicker))
                .setWhen(System.currentTimeMillis())
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .addAction(R.drawable.ic_snooze_white_36dp, context.getString(R.string.AN_snoozeAction), piSnooze)
                .setAutoCancel(true);
        return builder.build();
    }

    //notification for WakeLock Service
    Notification NewNotification(String time) {
        //dismiss button
        Intent dismissIntent = new Intent(ConstantValues.INSTANCE.getDISMISS_ACTION());
        dismissIntent.setClass(context, Alarm_Receiver.class);
        PendingIntent piDismiss = PendingIntent.getBroadcast(context, 0, dismissIntent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);
        builder
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setContentText(context.getString(R.string.AN_dismissContentText, time))
                .setContentTitle(context.getString(R.string.AN_dismissContentTitle))
                .setSmallIcon(R.drawable.ic_alarm_white_36dp)
                .setTicker(context.getString(R.string.AN_dismissContentText, time))
                .addAction(R.drawable.ic_alarm_off_white_36dp, context.getString(R.string.AN_dismissActionText), piDismiss)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setWhen(System.currentTimeMillis());
        return builder.build();
    }

    void CancelNotification(Context context, int notificationID) {
        NotificationManager notificationManager = getNotificationManager(context);
        notificationManager.cancel(notificationID);
    }

    private NotificationManager getNotificationManager(Context context) {
        return (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void createNotificationChanel() {
        NotificationManager
                mNotificationManager =
                (NotificationManager) context
                        .getSystemService(Context.NOTIFICATION_SERVICE);
        // The id of the channel.
        // The user-visible name of the channel.
        CharSequence name = "Media playback";
        // The user-visible description of the channel.
        String description = "Media playback controls";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
        // Configure the notification channel.
        mChannel.setDescription(description);
        mChannel.setShowBadge(false);
        mChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        if (mNotificationManager != null) {
            mNotificationManager.createNotificationChannel(mChannel);
        }
    }

}
