package com.example.user.ble.app;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
//import android.os.Handler;
//import android.support.annotation.NonNull;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
//import java.util.Collection;
import java.util.HashSet;
//import java.util.Iterator;
import java.util.Set;


public class MainActivity extends ActionBarActivity {


    private static final int REQUEST_ENABLE_BT = 1;
    private boolean enable;
    Button rssi,scan;


    ArrayList<String> btArrayList;
    BluetoothManager bluetoothManager;
    BluetoothAdapter mBluetoothAdapter;

    ArrayAdapter<String> mLeDeviceListAdapter;
    ListView lv;
    static int c=0;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initializations
        bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        btArrayList = new ArrayList<>();


        rssi=(Button)findViewById(R.id.button_update);
        rssi.setVisibility(View.GONE);
        scan=(Button)findViewById(R.id.button_scan);
    // To determine whether BLE is supported on the device
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "BLE not supported", Toast.LENGTH_SHORT).show();
            finish();
        }
        //Discoverable
        Intent discoverableIntent = new
                Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivity(discoverableIntent);

        // Ensures Bluetooth is enabled on the device
        if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);

        }

        // Initializes list view adapter.
        mLeDeviceListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, btArrayList);
        lv = (ListView) findViewById(R.id.listview_names);
        lv.setAdapter(mLeDeviceListAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String details = (String) parent.getItemAtPosition(position);
                String address = details.substring(details.length() - 17);
                BluetoothDevice dev = mBluetoothAdapter.getRemoteDevice(address);
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
                mLeDeviceListAdapter.clear();
                //deviceSet.clear();
                c=0;
                Intent i = new Intent(getApplicationContext(), PairUnpair.class);
                i.putExtra("name", dev.getName());
                i.putExtra("add", address);
                startActivity(i);
            }
        });

    }
    public void ButtonOnClick(View v) throws InterruptedException {
        switch (v.getId()) {
            case R.id.button_scan:
                enable = mBluetoothAdapter.isEnabled();
                if (enable) {
                    mBluetoothAdapter.startLeScan(mLeScanCallback);
                    scan.setVisibility(View.GONE);
                    rssi.setVisibility(View.VISIBLE);
                }
                else {
                    Context context = getApplicationContext();
                    CharSequence text = "Enable your Bluetooth first";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
                break;

            case R.id.button_update:
                mLeDeviceListAdapter.clear();
                c = 0;
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
                mBluetoothAdapter.cancelDiscovery();
                mBluetoothAdapter.startDiscovery();
                enable = mBluetoothAdapter.isEnabled();
                if (enable) {
                    mBluetoothAdapter.startLeScan(mLeScanCallback);
                }
                else {
                    Context context = getApplicationContext();
                    CharSequence text = "Enable your Bluetooth first";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
                break;

            case R.id.button_stop:
                scan.setVisibility(View.VISIBLE);
                rssi.setVisibility(View.GONE);
                mLeDeviceListAdapter.clear();
                c = 0;
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
                mBluetoothAdapter.cancelDiscovery();
                break;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent=new Intent(this,SettingNotiTime.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // User chose not to enable Bluetooth.
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            finish();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    //LeScanCallback
    BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback(){
        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi,byte[] scanRecord)
        {
            String name;
            name = device.getName() + "\nRSSI: " + rssi + "\nADDRESS: " + device.getAddress();
            btArrayList.add(c,name);
            c++;
            mLeDeviceListAdapter.notifyDataSetChanged();
        }
    };
}
