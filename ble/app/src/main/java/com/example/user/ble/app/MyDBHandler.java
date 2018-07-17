package com.example.user.ble.app;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class MyDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "deviceDB.db";
    private static final String TABLE_NAME = "devices";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_DEVICENAME = "devicename";
    public static final String COLUMN_RSSI = "rssi";

    public MyDBHandler(Context context, String name,SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PRODUCTS_TABLE = "CREATE TABLE " +
                TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_DEVICENAME
                + " TEXT ," + COLUMN_RSSI + " INTEGER" + ")";
        db.execSQL(CREATE_PRODUCTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addDevice(Device device) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_DEVICENAME, device.getDeviceName());
        values.put(COLUMN_RSSI, device.getRssi());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public Device findDevice(String devicename) {
        String query = "Select * FROM " + TABLE_NAME + " WHERE " + COLUMN_DEVICENAME + " =  \"" + devicename + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        Device device = new Device();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            device.setID(Integer.parseInt(cursor.getString(0)));
            device.setDeviceName(cursor.getString(1));
            device.setRssi(Integer.parseInt(cursor.getString(2)));
            cursor.close();
        } else {
            device = null;
        }
        db.close();
        return device;
    }

    public boolean deleteDevice(String devicename) {

        boolean result = false;

        String query = "Select * FROM " + TABLE_NAME + " WHERE " + COLUMN_DEVICENAME + " =  \"" + devicename + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        Device device = new Device();

        if (cursor.moveToFirst()) {
            device.setID(Integer.parseInt(cursor.getString(0)));
            db.delete(TABLE_NAME, COLUMN_ID + " = ?",
                    new String[] { String.valueOf(device.getID()) });
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }
    public ArrayList<String> getAllDevices()
    {
        ArrayList<String> array_list = new ArrayList();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from devices", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(COLUMN_DEVICENAME))
                     +"\n"+res.getString(res.getColumnIndex(COLUMN_RSSI)));
            res.moveToNext();
        }
        return array_list;
    }

}