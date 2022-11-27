package com.trungdunghoang125.alpharadio.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;

/**
 * Created by trungdunghoang125 on 11/27/2022.
 */
public class SleepTimer {

    public static void createSleepTimer(Context context, int minute) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            pendingIntent = RadioBroadcastReceiver.createSleepTimerPendingIntent(context);
            Long sleepTimeInMilis = System.currentTimeMillis() + minute * 1000L * 60;
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, sleepTimeInMilis, pendingIntent);
        }
    }

    public static void cancelSleepTimer(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            pendingIntent = RadioBroadcastReceiver.cancelSleepTimerPendingIntent(context);
            alarmManager.cancel(pendingIntent);
        }
    }
}
