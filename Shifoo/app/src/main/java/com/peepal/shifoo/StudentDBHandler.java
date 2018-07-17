package com.peepal.shifoo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

/**
 * Created by User on 17-07-2015.
 */
public class StudentDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "studentDB.db";
    private static final String TABLE_STUDENT = "student";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_AMOUNT = "amount";
    public static final String COLUMN_DATE = "date";

    public StudentDBHandler(Context context, String name,SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_STUDENT_TABLE = "CREATE TABLE " +
                TABLE_STUDENT + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_NAME
                + " TEXT," + COLUMN_PHONE + " TEXT," + COLUMN_AMOUNT
                + " INTEGER," + COLUMN_DATE + " TEXT )";
        db.execSQL(CREATE_STUDENT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENT);
        onCreate(db);
    }

    public void addStudent(Student student) {

        ContentValues values = new ContentValues();
        Student s;
        s=findStudent(student.getPhone());
        if(s==null) {
            values.put(COLUMN_NAME, student.getName());
            values.put(COLUMN_PHONE, student.getPhone());
            values.put(COLUMN_AMOUNT, student.getAmount());
            values.put(COLUMN_DATE, student.getDate());
            SQLiteDatabase db = this.getWritableDatabase();

            db.insert(TABLE_STUDENT, null, values);
            db.close();
        }
    }

    public Student findStudent(String phone) {
        String query = "Select * FROM " + TABLE_STUDENT + " WHERE " + COLUMN_PHONE + " =  \"" + phone + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        Student student = new Student();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            student.setID(Integer.parseInt(cursor.getString(0)));
            student.setName(cursor.getString(1));
            student.setPhone(cursor.getString(2));
            student.setAmount(Integer.parseInt(cursor.getString(3)));
            cursor.close();
        } else {
            student = null;
        }
        db.close();
        return student;
    }

    public boolean deleteStudent(String phone) {

        boolean result = false;

        String query = "Select * FROM " + TABLE_STUDENT + " WHERE " + COLUMN_PHONE + " =  \"" + phone + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

       Student student = new Student();

        if (cursor.moveToFirst()) {
            student.setID(Integer.parseInt(cursor.getString(0)));
            db.delete(TABLE_STUDENT, COLUMN_ID + " = ?", new String[] { String.valueOf(student.getID()) });
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }

    //split this into 2
    public ArrayList<String> getAllStudents()
    {
        ArrayList<String> array_list = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from student", null );
        res.moveToLast();

        while(res.isBeforeFirst() == false){
                array_list.add(res.getString(res.getColumnIndex(COLUMN_NAME))+"||"+
                        res.getString(res.getColumnIndex(COLUMN_PHONE))+"||"+
                        res.getInt(res.getColumnIndex(COLUMN_AMOUNT))+"||"+
                        res.getString(res.getColumnIndex(COLUMN_DATE)));
            res.moveToPrevious();
        }
        return array_list;
    }
}
