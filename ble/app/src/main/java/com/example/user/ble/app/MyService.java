package com.example.user.ble.app;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import java.util.List;
import java.util.Random;

public class MyService extends Service
{
    BluetoothGatt mBluetoothGatt;
    BluetoothManager mbluetoothManager;
    BluetoothAdapter mBluetoothAdapter;
    BluetoothDevice pairDevice;

    public static final int STATE_DISCONNECTED=0;
    public static final int STATE_CONNECTING=1;
    public static final int STATE_CONNECTED=2;
    public int mConnectionState = STATE_DISCONNECTED;


    public final static String ACTION_CONNECTED =
            "com.example.user.ble.ACTION_CONNECTED";
    public final static String ACTION_DISCONNECTED =
            "com.example.user.ble.ACTION_DISCONNECTED";
    public final static String ACTION_DATA_AVAILABLE =
            "com.example.user.ble.ACTION_DATA_AVAILABLE";
    public final static String ACTION_GATT_SERVICES_DISCOVERED =
            "com.example.user.ble.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String EXTRA_DATA =
            "com.example.user.ble.EXTRA_DATA";



    @Override
    public void onStart(Intent intent, int startId) {
        // TODO Auto-generated method stub
         super.onStart(intent, startId);
    }


    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }



    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {

       //@Override
        public void onCharacteristicChanged(BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic,int status)
        {
            if (status == BluetoothGatt.GATT_SUCCESS)
                broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
            super.onCharacteristicRead(gatt, characteristic, status);
        }

        @Override
        public void onConnectionStateChange(final BluetoothGatt gatt, final int status, final int newState) {
            String intentAction;
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                intentAction = ACTION_CONNECTED;
                mConnectionState = STATE_CONNECTED;
                broadcastUpdate(intentAction);

            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                intentAction = ACTION_DISCONNECTED;
                mConnectionState = STATE_DISCONNECTED;
                broadcastUpdate(intentAction);
            }
        }

        @Override
        public void onServicesDiscovered(final BluetoothGatt gatt, final int status)
        {
            if (status == BluetoothGatt.GATT_SUCCESS)
            {
                broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
            }
            super.onServicesDiscovered(gatt, status);
        }
    };
    //Constructor
    public MyService()
    {
    }

    //Binder
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


    public class LocalBinder extends Binder
    {
        MyService getService()
        {
            return MyService.this;
        }
    }
    private final IBinder mBinder= new LocalBinder();

    //action specifications
    public void broadcastUpdate(final String action)
    {
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }
    public void broadcastUpdate(final String action,final BluetoothGattCharacteristic characteristic)
    {
        final Intent intent=new Intent(action);
        intent.putExtra(characteristic.toString(),"true");
        sendBroadcast(intent);
    }

    public boolean connect(String address)
    {
        if(address != null && address.equals(address) && mBluetoothGatt!=null)
        {
            Toast.makeText(this, "Device already connected", Toast.LENGTH_SHORT).show();
            if (mBluetoothGatt.connect())
                return true;
            else
                return false;
        }
        final BluetoothDevice device=BluetoothAdapter.getDefaultAdapter().getRemoteDevice(address);
        if(device==null)
        {
            Toast.makeText(this, "Devive not found", Toast.LENGTH_SHORT).show();
            return false;
        }
        mBluetoothGatt=device.connectGatt(this, false, mGattCallback);
        pairDevice=device;
        mConnectionState = STATE_CONNECTING;
        return true;
    }
    public boolean disconnect(final String address)
    {
        if (address != null && address.equals(address) && mBluetoothGatt != null)
        {
            mBluetoothGatt.disconnect();
            mBluetoothGatt.close();
            return true;
        }
        return false;
    }

    //initializing bluetooth manager and adapter
    public boolean initialize()
    {
        if (mbluetoothManager == null)
        {
            mbluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if (mbluetoothManager == null)
            {
                return false;
            }
        }

        mBluetoothAdapter = mbluetoothManager.getAdapter();
        if (mBluetoothAdapter == null)
        {
            return false;
        }

        return true;
    }

    public List<BluetoothGattService> getSupportedGattServices()
    {

        if (mBluetoothGatt == null) return null;
        else
            return mBluetoothGatt.getServices();
    }

    public void readCharacteristic(BluetoothGattCharacteristic characteristic)
    {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            return;
        }

        mBluetoothGatt.readCharacteristic(characteristic);
    }
}
