package com.trungdunghoang125.alpharadio.service;

import static com.trungdunghoang125.alpharadio.service.RadioPlayerService.ACTION_STOP;
import static com.trungdunghoang125.alpharadio.utils.Constants.ACTION_NAME;
import static com.trungdunghoang125.alpharadio.utils.Constants.SLEEP_TIMER_REQUEST_CODE;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

/**
 * Created by trungdunghoang125 on 11/22/2022.
 */
public class RadioBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // handle action for notification
        Intent actionIntent = new Intent(context, RadioPlayerService.class);
        actionIntent.putExtra(ACTION_NAME, intent.getAction());
        context.startService(actionIntent);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static PendingIntent createSleepTimerPendingIntent(Context context) {
        Intent intent = new Intent(context, RadioBroadcastReceiver.class);
        intent.setAction(ACTION_STOP);
        return PendingIntent.getBroadcast(context, SLEEP_TIMER_REQUEST_CODE, intent, PendingIntent.FLAG_IMMUTABLE);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static PendingIntent cancelSleepTimerPendingIntent(Context context) {
        Intent intent = new Intent(context, RadioBroadcastReceiver.class);
        intent.setAction(ACTION_STOP);
        return PendingIntent.getBroadcast(context, SLEEP_TIMER_REQUEST_CODE, intent, PendingIntent.FLAG_IMMUTABLE);
    }
}