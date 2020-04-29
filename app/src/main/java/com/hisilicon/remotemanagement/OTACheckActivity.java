package com.hisilicon.remotemanagement;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RecoverySystem;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLConnection;

public class OTACheckActivity extends Activity{
    String LOG_TAG = "OTACheckActivity";
    String downloadPath = "/cache/recovery/";

    String packageUrl = "";
    String version = "";

    TextView updateInfo;
    Button update;

    private void updateOTAImage() {
        try {
            RecoverySystem.installPackage(this, new File(downloadPath, "OTAImage.zip"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("HandlerLeak")
    Handler updateHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            ProgressDialog.show(OTACheckActivity.this, "Update Progress", "Updating Now...").setCancelable(false);

            new OTAUpdateExecutor().execute();
        }
    };

    @SuppressLint("StaticFieldLeak")
    class OTAUpdateExecutor extends AsyncTask<Void, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            Boolean flag = false;
            try {
                URL url = new URL(packageUrl);

                URLConnection c = url.openConnection();

                File outputFile = new File(downloadPath, "OTAImage.zip");

                Log.e(LOG_TAG, "OTA Update Link: " + packageUrl);

                if (outputFile.exists()) {
                    outputFile.delete();
                }

                FileOutputStream fos = new FileOutputStream(outputFile);

                int total_size = c.getContentLength();//size of apk
                Log.e(LOG_TAG, "OTA Update Total Size: " + total_size);
                DataInputStream is = new DataInputStream(url.openStream());

                byte[] buffer = new byte[4096];
                int len;
                while ((len = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                }
                Log.e(LOG_TAG, "OTA Update Downloaded Success");
                fos.close();
                is.close();

                flag = true;
            } catch (Exception e) {
                flag = false;
                Log.e(LOG_TAG, "OTA Update Downloaded Failed: " + e.toString());
            }
            return flag;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            if (aBoolean) {
                updateOTAImage();
            }
            else {
                finish();
                Toast.makeText(OTACheckActivity.this, R.string.update_failed, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ota_check_dialog);
        Intent intent = getIntent();

        if (intent != null) {
            packageUrl = intent.getStringExtra("packageurl");
            version = intent.getStringExtra("version");
        }
        else {
            finish();
        }

        updateInfo = findViewById(R.id.update_info);
        update = findViewById(R.id.update);

        updateInfo.setText(getText(R.string.update_info) + " " + version);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateHandler.sendEmptyMessage(0);
            }
        });
    }
}
