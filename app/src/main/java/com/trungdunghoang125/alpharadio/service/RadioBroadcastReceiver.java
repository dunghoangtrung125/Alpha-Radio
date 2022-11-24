package com.trungdunghoang125.alpharadio.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by trungdunghoang125 on 11/22/2022.
 */
public class RadioBroadcastReceiver extends BroadcastReceiver {
    public static final String NOTIFICATION_CONTROL_ACTION = "com.trungdunghoang125.NOTIFICATION_CONTROL";
    public static final String ACTION_NAME = "action-name";

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent actionIntent = new Intent(NOTIFICATION_CONTROL_ACTION);
        actionIntent.putExtra(ACTION_NAME, intent.getAction());
        context.sendBroadcast(actionIntent);
    }
}