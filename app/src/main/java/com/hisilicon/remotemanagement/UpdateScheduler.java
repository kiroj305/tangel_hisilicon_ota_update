package com.hisilicon.remotemanagement;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import me.pushy.sdk.Pushy;

public class UpdateScheduler extends BroadcastReceiver {
    public static boolean startService = false;
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("almond", "Update Boot Completed.");
        String action = intent.getAction();

        if (Intent.ACTION_BOOT_COMPLETED.equals(action)
                || "android.intent.action.QUICKBOOT_POWERON".equals(action)
                || "com.hisilicon.update_start".equals(action)) {
            scheduleRecommendationUpdate(context);
        }
    }

    private void scheduleRecommendationUpdate(Context context) {
        if (!startService) {
            startService = true;
            Pushy.listen(context);
        }
    }
}
