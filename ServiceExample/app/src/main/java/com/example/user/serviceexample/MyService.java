package com.example.user.serviceexample;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class MyService extends Service
{

    BluetoothManager mbluetoothManager;
    BluetoothAdapter mBluetoothAdapter;
    String result;

    public final static String ACTION_CUSTOM=
            "com.example.user.serviceexample.ACTION_CUSTOM";

    //constructor
    public MyService() {
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
        new CountDownTimer(60 * 1000, 1000) {

            public void onTick(long millisUntilFinished) {
                if (!mBluetoothAdapter.isEnabled()) {
                    mBluetoothAdapter.cancelDiscovery();
                    cancel();
                    onDestroy();
                }
            }

            public void onFinish() {
                Log.v("here", "In the timer");
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
            result=device.getName()+"\nRSSI: "+rssi+"\nAddress: "+device.getAddress();
            Intent i=new Intent();
            i.setAction(ACTION_CUSTOM);
            i.putExtra("details", result);
            Log.v("here",result);
            sendBroadcast(i);
            Log.v("here","in call back");

        }
    };



}
