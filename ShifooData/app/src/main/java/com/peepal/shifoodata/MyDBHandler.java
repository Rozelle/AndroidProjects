package com.peepal.shifoodata;

/**
 * Created by User on 23-07-2015.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

public class MyDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "messageDB.db";
    private static final String TABLE_MESSAGE = "message";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_AMOUNT = "amount";
    public static final String COLUMN_REMARKS = "remarks";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_PAID="paid";
    public static final String COLUMN_TXNID="txnid";

    public MyDBHandler(Context context, String name,SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_MESSAGE_TABLE = "CREATE TABLE " +
                TABLE_MESSAGE + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_NAME
                + " TEXT," + COLUMN_PHONE + " TEXT," + COLUMN_AMOUNT
                + " INTEGER," + COLUMN_REMARKS + " TEXT,"+ COLUMN_DATE
                + " TEXT,"+ COLUMN_PAID + " INTEGER," + COLUMN_TXNID
                + " TEXT)";
        db.execSQL(CREATE_MESSAGE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGE);
        onCreate(db);
    }

    public void addMessage(Message message) {
        ContentValues values = new ContentValues();
        Message m;
        m=findMessage(message.getPhone());
        if(m!=null)
        {
            deleteMessage(m.getPhone());
        }
        values.put(COLUMN_NAME, message.getName());
        values.put(COLUMN_PHONE, message.getPhone());
        values.put(COLUMN_AMOUNT, message.getAmount());
        values.put(COLUMN_REMARKS, message.getRemarks());
        values.put(COLUMN_DATE, message.getDate());
        values.put(COLUMN_PAID, message.getPaid());
        values.put(COLUMN_TXNID, message.getTxnid());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_MESSAGE, null, values);
        db.close();
    }

    public Message findMessage(String phone) {
        String query = "Select * FROM " + TABLE_MESSAGE + " WHERE " + COLUMN_PHONE + " =  \"" + phone + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        Message message = new Message();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            message.setID(Integer.parseInt(cursor.getString(0)));
            message.setName(cursor.getString(1));
            message.setPhone(cursor.getString(2));
            message.setAmount(Integer.parseInt(cursor.getString(3)));
            message.setRemarks(cursor.getString(4));
            message.setDate(cursor.getString(5));
            message.setPaid(Integer.parseInt(cursor.getString(6)));
            message.setTxnid(cursor.getString(7));
            cursor.close();
        } else {
            message = null;
        }
        db.close();
        return message;
    }

    public boolean deleteMessage(String phone) {

        boolean result = false;

        String query = "Select * FROM " + TABLE_MESSAGE + " WHERE " + COLUMN_PHONE + " =  \"" + phone + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        Message message=new Message();

        if (cursor.moveToFirst()) {
            message.setID(Integer.parseInt(cursor.getString(0)));
            db.delete(TABLE_MESSAGE, COLUMN_ID + " = ?",
                    new String[] { String.valueOf(message.getID()) });
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }

    //split this into 2
    public ArrayList<String> getAll0Messages()
    {
        ArrayList<String> array_list = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from message", null );
        res.moveToLast();

        while(res.isBeforeFirst() == false){
            if(res.getInt(res.getColumnIndex(COLUMN_PAID))==0)
                array_list.add(res.getString(res.getColumnIndex(COLUMN_NAME))+"||"+
                        res.getString(res.getColumnIndex(COLUMN_PHONE))+"||"+
                        res.getInt(res.getColumnIndex(COLUMN_AMOUNT))+"||"+
                        res.getString(res.getColumnIndex(COLUMN_DATE))+"||"+
                        res.getString(res.getColumnIndex(COLUMN_REMARKS))+"||"+
                        "!");
            res.moveToPrevious();
        }
        return array_list;
    }

    public ArrayList<String> getAll1Messages()
    {
        ArrayList<String> array_list = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from message", null );
        res.moveToLast();

        while(res.isBeforeFirst() == false){
            if(res.getInt(res.getColumnIndex(COLUMN_PAID))==1)
                array_list.add(res.getString(res.getColumnIndex(COLUMN_NAME))+"||"+
                        res.getString(res.getColumnIndex(COLUMN_PHONE))+"||"+
                        res.getInt(res.getColumnIndex(COLUMN_AMOUNT))+"||"+
                        res.getString(res.getColumnIndex(COLUMN_DATE))+"||"+
                        res.getString(res.getColumnIndex(COLUMN_REMARKS))+"||"+
                        res.getString(res.getColumnIndex(COLUMN_TXNID)));
            res.moveToPrevious();
        }
        return array_list;
    }

}