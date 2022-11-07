package com.trungdunghoang125.radiobrowserokhttp;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by trungdunghoang125 on 11/4/2022.
 */
public class Utility {
    public static class ShowToast {
        public static void initToast(Context context, Toast toast, String message) {
            cancelToast(toast);
            toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
            toast.show();
        }

        public static void cancelToast(Toast toast) {
            if (toast != null) {
                toast.cancel();
            }
        }
    }
}
