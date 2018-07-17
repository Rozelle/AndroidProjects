package com.example.user.ble.app;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class NotificationService extends Service
{

    BluetoothManager mbluetoothManager;
    BluetoothAdapter mBluetoothAdapter;
    public final static String ACTION_CUSTOM="com.example.user.ble.app.ACTION_CUSTOM";

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Time = "timeKey";

    //constructor
    public NotificationService() {
    }

    //binder
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate()
    {
        super.onCreate();
        Log.v("here","on Create");
        mbluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = mbluetoothManager.getAdapter();
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.v("here","on Destroy");
        stopScan();
    }

    @Override
    public int onStartCommand(final Intent intent, final int flags, final int startId)
    {
        Log.v("here","on START COMMAND");
        if(mBluetoothAdapter.isEnabled()||mBluetoothAdapter != null)
            startScan();
        else
            onDestroy();
        return START_STICKY;
    }

    private void startScan() {
        Toast.makeText(this, "Scanning", Toast.LENGTH_SHORT).show();
        Log.v("here","before call back");
        mBluetoothAdapter.startLeScan(mCallBack);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, 0);
        String restoredTime = sharedpreferences.getString(Time, null);
        if(restoredTime==null)
            restoredTime="5";
        int rt=Integer.parseInt(restoredTime);
        Log.v("Timer",""+rt);
        if(rt==0)
            return;
        new CountDownTimer(rt*60 * 1000, 1000) {
            int rt;
            public void onTick(long millisUntilFinished) {
                if (!mBluetoothAdapter.isEnabled()) {
                    mBluetoothAdapter.cancelDiscovery();
                    cancel();
                    onDestroy();
                }
               String restoredTime = sharedpreferences.getString(Time, null);
                if(restoredTime==null)
                    restoredTime="5";
                rt=Integer.parseInt(restoredTime);
                if(rt==0)
                   return;
            }

            public void onFinish() {
                Log.v("here", "In the timer");
                if(rt==0)
                    return;
                stopScan();
                startScan();
            }
        }.start();
        Log.v("here","after call back");
    }

    private void stopScan() {
        Toast.makeText(this, "Not Scanning", Toast.LENGTH_SHORT).show();
        mBluetoothAdapter.stopLeScan(mCallBack);
    }



    public final BluetoothAdapter.LeScanCallback mCallBack=new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord)
        {
            Intent i=new Intent();
            Log.v("rssi",""+rssi);
            i.setAction(ACTION_CUSTOM);
            i.putExtra("name", device.getAddress());
            i.putExtra("rssi",""+rssi);
            sendBroadcast(i);
            Log.v("here","in call back");

        }
    };



}
