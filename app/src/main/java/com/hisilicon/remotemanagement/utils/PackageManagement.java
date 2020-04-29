package com.hisilicon.remotemanagement.utils;

import android.content.Context;
import android.content.Intent;
import android.os.SystemProperties;
import android.text.TextUtils;

import com.hisilicon.remotemanagement.OTACheckActivity;

public class PackageManagement {
    public static String LOG_TAG = "PackageManagement";
    public static PackageManagement mInstance;

    public static String OTA_COMMAND = "OTA_UPDATE";

    public static PackageManagement getInstance() {
        if (mInstance == null) {
            mInstance = new PackageManagement();
        }

        return mInstance;
    }

    public void doCommand(Context mContext, Command command) {
        if (command.type.equals(OTA_COMMAND)) {
            startOTAUpdatingCheck(mContext, command);
        }
    }

    private void startOTAUpdatingCheck(Context mContext, Command command) {
        String version = SystemProperties.get("store.ota.version", "1970000000");

        if (Long.parseLong(version) < Long.parseLong(command.version)
                && !TextUtils.isEmpty(command.link)) {
            Intent intent = new Intent(mContext, OTACheckActivity.class);
            intent.putExtra("version", command.version);
            intent.putExtra("packageurl", command.link);

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        }
    }
}
