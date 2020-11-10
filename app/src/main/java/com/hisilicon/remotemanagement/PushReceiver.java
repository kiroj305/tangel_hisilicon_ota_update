package com.hisilicon.remotemanagement;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.hisilicon.remotemanagement.utils.Command;
import com.hisilicon.remotemanagement.utils.PackageManagement;

public class PushReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            String type = intent.getStringExtra("type");
            if (TextUtils.isEmpty(type)) return;
            Log.e("t@ngel", type);
            Command newCommand = new Command();
            if (type.equalsIgnoreCase(PackageManagement.OTA_COMMAND)) {
                newCommand.type = PackageManagement.OTA_COMMAND;
                newCommand.link = intent.getStringExtra("link");
                newCommand.version = intent.getStringExtra("version");
                newCommand.device_name = intent.getStringExtra("device_name");
            }

            PackageManagement.getInstance().doCommand(RemoteManagement.mInstance, newCommand);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

