package com.example.user.ble.app;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class BleReceiver extends BroadcastReceiver {
    Intent newIntent;
    MyDBHandler dbHandler;

    public BleReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(BluetoothAdapter.ACTION_STATE_CHANGED))
        {
            if (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1) == BluetoothAdapter.STATE_ON) {
                newIntent=new Intent(context, NotificationService.class);
                context.startService(newIntent);
            }
        }
        if (intent.getAction().equals(NotificationService.ACTION_CUSTOM)) {
            dbHandler = new MyDBHandler(context, null, null, 1);
            String devName = intent.getStringExtra("name");
            String devRssi=intent.getStringExtra("rssi");
            Log.v("receiver",devRssi);
            if(devName!=null&&devRssi!=null)//if callback didnt return null
            {
                Device device = dbHandler.findDevice(devName);
                if (device != null)//means device present in database
                {
                    boolean res = dbHandler.deleteDevice(devName);
                    if (res == false)
                        Toast.makeText(context, "Some error in updating RSSI", Toast.LENGTH_SHORT).show();
                    device.setRssi(Integer.parseInt(devRssi));
                    dbHandler.addDevice(device);
                } else//device not present in database
                {
                    Device newDevice = new Device(devName, Integer.parseInt(devRssi));
                    dbHandler.addDevice(newDevice);
                }
                Intent i = new Intent(context, DisplayDatabase.class);
                PendingIntent pIntent = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
                Notify(pIntent, context);
            }
        }

    }
    public void Notify(PendingIntent pIntent,Context context)
    {
        // Build notification
        Notification noti = new Notification.Builder(context)
                .setContentTitle("New Device Found")
                .setContentText("Tap to see details")
                .setSmallIcon(R.drawable.stat_notify_more)
                .setContentIntent(pIntent)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .build();
        NotificationManager notificationManager=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        // hide the notification after its selected
        noti.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(0, noti);

    }
}


