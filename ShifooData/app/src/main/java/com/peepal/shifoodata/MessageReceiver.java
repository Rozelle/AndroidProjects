package com.peepal.shifoodata;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MessageReceiver extends BroadcastReceiver {

    // Get the object of SmsManager
    final SmsManager sms = SmsManager.getDefault();
    //get database handler and message object
    MyDBHandler dbHandler;
    StudentDBHandler studentDBHandler;

    //default constructor
    public MessageReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //Initialization
        dbHandler=new MyDBHandler(context, null, null, 1);
        Message m;
        studentDBHandler=new StudentDBHandler(context, null, null, 1);
        Student s;
        String phone;

        // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();

        try {
            if (bundle != null)
            {
                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                for (int i = 0; i < pdusObj.length; i++)
                {
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String senderNumber = currentMessage.getDisplayOriginatingAddress();
                    String message = currentMessage.getDisplayMessageBody();
                    if (senderNumber.equals("DZ-Shifoo") || senderNumber.equals("IK-Shifoo")) {
                        Calendar c = Calendar.getInstance();
                        SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");
                        String formattedDate = df.format(c.getTime());
                        String contactName;
                        //breaking message
                        int part1 = message.indexOf('(');
                        int part2 = message.indexOf(')');
                        phone = message.substring(part1 + 5, part2);
                        //t=after bracket part
                        String t = message.substring(part2 + 1, message.length());
                        //t=after 'Rs.' part
                        t=t.substring(t.indexOf('R')+3 ,t.length());
                        String amt=t.substring(0,t.indexOf('.'));
                        //txnid = after 'ID:' part
                        String txnid=t.substring(t.indexOf(':')+1, t.length());

                        //updating pending paid db
                        m = dbHandler.findMessage(phone);
                        if (m != null)
                        {
                            contactName=m.getName();
                            m.setAmount(Integer.parseInt(amt));
                            m.setDate(formattedDate);
                            m.setTxnid(txnid);
                            m.setPaid(1);
                            dbHandler.addMessage(m);
                        }
                        else
                        {
                            contactName = "Unknown";
                            ContentResolver cr = context.getContentResolver();
                            Uri uri = Uri.withAppendedPath(
                                    ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phone));
                            Cursor cursor = cr.query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);
                            if (cursor == null) {
                            }
                            if(cursor.moveToFirst()) {
                                contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
                            }
                            if(cursor != null && !cursor.isClosed()) {
                                cursor.close();
                            }
                            m=new Message(contactName,phone,Integer.parseInt(amt),"",formattedDate,1,txnid);
                            dbHandler.addMessage(m);
                        }

                        //updating student databse
                        s=studentDBHandler.findStudent(phone);
                        if(s==null)
                        {
                            s=new Student(contactName,phone,Integer.parseInt(amt),formattedDate);
                            studentDBHandler.addStudent(s);
                        }
                        else
                        {
                            studentDBHandler.deleteStudent(phone);
                            s.setDate(formattedDate);
                            studentDBHandler.addStudent(s);
                        }
                    }//if ends
                } // end for loop
            } // bundle is null

        }//try ends
        catch (Exception e) {
            Toast.makeText(context,"Shifoo is unable to read the message!",Toast.LENGTH_SHORT).show();

        }//catch ends
    }//onReceive ends
}
