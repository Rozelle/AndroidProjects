package com.peepal.shifoodata;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

public class MyReceiver extends WakefulBroadcastReceiver {
    public MyReceiver() {
    }
    /*public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        Log.v("tag", "From: " + from);
        Log.v("tag", "Message: " + message);
    }*/
    @Override
    public final void onReceive(Context context, Intent intent) {
        // Explicitly specify that GcmMessageHandler will handle the intent.
        ComponentName component = new ComponentName(context.getPackageName(),
                GcmMessageHandler.class.getName());

        // Start the service, keeping the device awake while it is launching.
        startWakefulService(context, (intent.setComponent(component)));
        setResultCode(Activity.RESULT_OK);

    }
}
