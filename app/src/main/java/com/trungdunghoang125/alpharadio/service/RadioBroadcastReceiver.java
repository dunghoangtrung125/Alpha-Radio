package com.trungdunghoang125.alpharadio.service;

import static com.trungdunghoang125.alpharadio.utils.Constants.ACTION_NAME;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

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
}