package org.sairaa.androidtest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class NotificationReceiver extends BroadcastReceiver {
    private Context context;
    public NotificationReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        updateNotification(intent);
    }

    private void updateNotification(Intent intent) {

        String intentAction = intent.getAction();
        if (intentAction != null) {
            switch (intentAction){
                case Intent.ACTION_POWER_CONNECTED:
                    Toast.makeText(context, "Connected", Toast.LENGTH_SHORT).show();
                    break;
                case Intent.ACTION_POWER_DISCONNECTED:
                    Toast.makeText(context, "Disconnected", Toast.LENGTH_SHORT).show();
                    break;
                case MainActivity.ACTION_UPDATE_NOTIFICATION:
                    Toast.makeText(context, "Custom BroadCast", Toast.LENGTH_SHORT).show();
                    break;

            }
        }
    }


}
