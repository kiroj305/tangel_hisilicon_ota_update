package com.hisilicon.remotemanagement;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.FileWriter;
import java.util.Locale;

import me.pushy.sdk.Pushy;

public class MainActivity extends Activity {
    Button update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        update = findViewById(R.id.update);

        Pushy.listen(this);

        Intent intent = new Intent("com.hisilicon.update_start");
        intent.setPackage("com.hisilicon.remotemanagement");
        this.sendBroadcast(intent);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateOTAImage();
            }
        });
    }

    private void updateOTAImage() {
        try {
            FileWriter writer = new FileWriter("/cache/recovery/command");

            String updateCommand = "--update_package=/data/local/tmp/otatest.zip" +
                    "\n--locale=" + Locale.getDefault().toString();

            try {
                writer.write(updateCommand);
                writer.write("\n");
            }
            finally {
                writer.close();
            }
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            pm.reboot("recovery");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
