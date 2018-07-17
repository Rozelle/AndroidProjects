package com.example.user.ble.app;

import android.app.ActivityManager;
import android.app.ApplicationErrorReport;
import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class PairUnpair extends ActionBarActivity
{
    String devname,address;
    BluetoothDevice pairDevice;
    private TextView connectionStateTv,mData;
    boolean mConnected=false;

    BluetoothManager bluetoothManager;
    BluetoothAdapter mBluetoothAdapter;
    private boolean bolBroacastRegistred;

    public IntentFilter intentFilter;
    connectionReceiver reciever;

    MyService mService;
    private ArrayAdapter<BluetoothGattService> btGattServiceAdapter;
    private ArrayAdapter<BluetoothGattCharacteristic> btGattCharacteristicAdapter ;
    private ListView serviceListView,characteristicListView ;
    private List<BluetoothGattService> btGattServices ;
    private List<BluetoothGattCharacteristic> btGattCharacteristics ;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pair_unpair);

        bluetoothManager=(BluetoothManager)getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter= bluetoothManager.getAdapter();
        mService=new MyService();

        reciever=new connectionReceiver();
        connectionStateTv=(TextView)findViewById(R.id.ConnectionState);
        mData=(TextView) findViewById(R.id.textView_data);

        Intent i=getIntent();
        devname=i.getStringExtra("name");
        address=i.getStringExtra("add");
        pairDevice=mBluetoothAdapter.getRemoteDevice(address);
        String out=devname+"\n"+address;
        ((TextView)findViewById(R.id.details)).setText(out);

        intentFilter = new IntentFilter();
        intentFilter.addAction(MyService.ACTION_CONNECTED);
        intentFilter.addAction(MyService.ACTION_DISCONNECTED);
        intentFilter.addAction(MyService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(MyService.ACTION_DATA_AVAILABLE);

        serviceListView= (ListView) findViewById(R.id.listView_Services);
        characteristicListView=(ListView) findViewById(R.id.listView_characteristics);
        mData=(TextView) findViewById(R.id.textView_data);
        btGattServices=new ArrayList<BluetoothGattService>();
        btGattCharacteristics= new ArrayList<BluetoothGattCharacteristic>();

        btGattServiceAdapter= new ArrayAdapter<BluetoothGattService>(PairUnpair.this, android.R.layout.simple_list_item_1, btGattServices);
        btGattCharacteristicAdapter= new ArrayAdapter<BluetoothGattCharacteristic>(PairUnpair.this, android.R.layout.simple_list_item_checked, btGattCharacteristics);

        serviceListView.setAdapter(btGattServiceAdapter);
        characteristicListView.setAdapter(btGattCharacteristicAdapter);

        serviceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapter, View view,
                                    int position, long id) {

                BluetoothGattService service= (BluetoothGattService) adapter.getItemAtPosition(position);

                displayGattCharacteristics(service);
            }
        });


    }
    public class connectionReceiver extends BroadcastReceiver
    {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                final String action = intent.getAction();
                if (MyService.ACTION_CONNECTED.equals(action))
                {
                    mConnected = true;
                    connectionStateTv.setText(R.string.connected);
                }
                else if (MyService.ACTION_DISCONNECTED.equals(action))
                {

                    mConnected = false;
                    connectionStateTv.setText("Disconnected");
                } else if (MyService.ACTION_GATT_SERVICES_DISCOVERED.equals(action))
                {
                // Show all the supported services and characteristics on the user interface
                    displayGattServices(mService.getSupportedGattServices());
                }
                else if (MyService.ACTION_DATA_AVAILABLE.equals(action))
                {
                    displayData(intent.getStringExtra(mService.EXTRA_DATA));
                }
            }
    };
    public void buttonOnClick(View v) {
        switch (v.getId()) {
            case R.id.button_pair:
                registerReceiver(reciever, intentFilter);
                Intent intent = new Intent(this, MyService.class);
                bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
                bolBroacastRegistred=true;
                break;


            case R.id.button_unpair:
                mService.disconnect(address);
                if (isMyServiceRunning( MyService.class))
                {
                    unbindService(mConnection);
                    btGattServices.clear();
                    btGattServiceAdapter.notifyDataSetChanged();
                }
                else
                    Toast.makeText(this, "No devices paired", Toast.LENGTH_SHORT).show();

        }
    }
    @Override
    protected void onDestroy() {

        super.onDestroy();

        if(bolBroacastRegistred)
        {
            this.unregisterReceiver(this.reciever);
        }
        if(isMyServiceRunning(MyService.class))
        {
            unbindService(mConnection);
        }


    }


    /** Defines callbacks for service binding, passed to bindService() */
         ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService=((MyService.LocalBinder)service).getService();
            if (!mService.initialize()) {
                finish();
            }
            mService.initialize();
            mService.connect(address);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService.disconnect(address)	;
            mService = null;

        }
    };


    private void displayData(String data) {
        if (data != null) {
            mData.setText(data);
        }
    }
    //It checks whether a Service is running or not!!
    private boolean isMyServiceRunning(Class<?> serviceClass)
    {
        //final ActivityManager activityManager = (ActivityManager)this.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE))
            if (serviceClass.getName().equals(service.service.getClassName()))
                return true;
        return false;
    }

    private void displayGattServices(List<BluetoothGattService> gattServices)
    {
        if (gattServices == null) return;

        else
        {
            this.mService= (MyService) gattServices;
            btGattServiceAdapter.notifyDataSetChanged();
        }


    }
    private void  displayGattCharacteristics(BluetoothGattService myService)
    {

        btGattCharacteristics=myService.getCharacteristics();
        btGattCharacteristicAdapter.notifyDataSetChanged();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pair_unpair, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
