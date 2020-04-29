package com.hisilicon.remotemanagement;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import me.pushy.sdk.Pushy;

public class RemoteManagement extends Application {
    public static RemoteManagement mInstance;
    public static String deviceToken;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        Log.e("almond", "Subscribe Pushy.me");
        if (!Pushy.isRegistered(getApplicationContext())) {
            new RegisterForPushNotificationsAsync().execute();
        }
    }

    private class RegisterForPushNotificationsAsync extends AsyncTask<Void, Void, Exception> {
        protected Exception doInBackground(Void... params) {
            try {
                // Assign a unique token to this device
                deviceToken = Pushy.register(getApplicationContext());
                Log.e("almond", deviceToken);
            }
            catch (Exception exc) {
                // Return exc to onPostExecute
                return exc;
            }

            // Success
            return null;
        }

        @Override
        protected void onPostExecute(Exception exc) {
            // Failed?
            if (exc != null) {
                // Show error as toast message
                Toast.makeText(getApplicationContext(), exc.toString(), Toast.LENGTH_LONG).show();
                return;
            }

            // Succeeded, optionally do something to alert the user
        }
    }
}
