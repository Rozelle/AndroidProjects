package com.example.user.serviceexample;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class ScanReceiver extends BroadcastReceiver {
    static int num;
    Intent newIntent;
    public ScanReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(BluetoothAdapter.ACTION_STATE_CHANGED))
        {
            if (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1) == BluetoothAdapter.STATE_ON) {
                newIntent=new Intent(context, MyService.class);
                context.startService(newIntent);
            }
            /*if(intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1) == BluetoothAdapter.STATE_OFF) {
                context.stopService(newIntent);
            }*/
        }
        if (intent.getAction().equals(MyService.ACTION_CUSTOM)) {
            String res = intent.getStringExtra("details");
            Intent i = new Intent(context, MainActivity.class);
            i.putExtra("details", res);
            Log.v("here",res);
            PendingIntent pIntent = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
            Notify(pIntent, context);
        }
    }
    public void Notify(PendingIntent pIntent,Context context)
    {
        // Build notification
        Notification noti = new Notification.Builder(context)
                .setContentTitle("New Device Found")
                .setContentText("Tap to see details ")
                .setSmallIcon(R.drawable.example)
                .setContentIntent(pIntent)
                .setNumber(++num)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .build();
        NotificationManager notificationManager=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        // hide the notification after its selected
        noti.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(0, noti);

    }
}
