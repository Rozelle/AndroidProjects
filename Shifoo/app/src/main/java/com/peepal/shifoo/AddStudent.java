package com.peepal.shifoo;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;


public class AddStudent extends ActionBarActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener{

    public static ArrayList<String> phoneValueArr = new ArrayList<String>();
    public static ArrayList<String> nameValueArr = new ArrayList<String>();
    private ArrayAdapter<String> adapter;
    AutoCompleteTextView textView=null;
    String name,phone,amt;
    EditText e1,e2;
    Button b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        e1=(EditText)findViewById(R.id.number);
        e2=(EditText)findViewById(R.id.amount);

        b=(Button)findViewById(R.id.doneButton);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name=textView.getText().toString();
                phone=e1.getText().toString();
                int j=0;
                String add="";
                while(j<phone.length())
                {
                    if(phone.charAt(j)!=' ')
                        add+=phone.charAt(j);
                    j++;
                }
                if(add.length()>10)
                    add=add.substring(add.length()-10,add.length());
                phone=add;
                amt=e2.getText().toString();
                if(name.length()==0||phone.length()==0||amt.length()==0)
                    Toast.makeText(getApplicationContext(),"Enter all details",Toast.LENGTH_SHORT).show();
                else
                {
                    Student s=new Student(name,phone,Integer.parseInt(amt),"!");
                    StudentDBHandler dbHandler=new StudentDBHandler(getApplicationContext(), null, null, 1);
                    Student present=dbHandler.findStudent(phone);
                    if(present!=null)
                        Toast.makeText(getApplicationContext(),"Student already present!",Toast.LENGTH_LONG).show();
                    else
                    {
                        dbHandler.addStudent(s);
                        MyDBHandler myDBHandler=new MyDBHandler(getApplicationContext(),null,null,1);
                        Message message=myDBHandler.findMessage(phone);
                        if(message!=null)
                        {
                            message.setName(name);
                            myDBHandler.addMessage(message);
                        }
                        textView.setText("");
                        e1.setText("");
                        e2.setText("");
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                }
            }
        });

        /*b2=(Button)findViewById(R.id.discardButton);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("");
                e1.setText("");
                e2.setText("");
                Intent i=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
            }
        });*/
        readContactData();
        adapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_dropdown_item_1line, nameValueArr);

        textView = (AutoCompleteTextView)findViewById(R.id.autoComp);
        textView.setAdapter(adapter);
        textView.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
        textView.setOnItemClickListener((AdapterView.OnItemClickListener) this);
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position,
                               long arg3) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        // TODO Auto-generated method stub
        // Get Array index value for selected name

        int i = nameValueArr.indexOf(""+arg0.getItemAtPosition(arg2));
        // If name exist in name ArrayList
        if (i >= 0) {
            // Get Phone Number
            phone = phoneValueArr.get(i);
            name = arg0.getItemAtPosition(arg2).toString();
            textView.setText(name);
            e1.setText(phone);
        }
    }

    // Read phone contact name and phone numbers
    private void readContactData() {
        try
        {
            String phoneNumber = "";
            ContentResolver cr = getBaseContext()
                    .getContentResolver();

            //Query to get contact name

            Cursor cur = cr
                    .query(ContactsContract.Contacts.CONTENT_URI,
                            null,
                            null,
                            null,
                            null);

            // If data data found in contacts
            if (cur.getCount() > 0) {
                int k=0;
                String name = "";

                while (cur.moveToNext())
                {

                    String id = cur
                            .getString(cur
                                    .getColumnIndex(ContactsContract.Contacts._ID));
                    name = cur
                            .getString(cur
                                    .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                    //Check contact have phone number
                    if (Integer
                            .parseInt(cur
                                    .getString(cur
                                            .getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0)
                    {

                        //Create query to get phone number by contact id
                        Cursor pCur = cr
                                .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                        null,
                                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                                + " = ?",
                                        new String[] { id },
                                        null);
                        int j=0;

                        while (pCur
                                .moveToNext())
                        {
                            // Sometimes get multiple data
                            if(j==0)
                            {
                                // Get Phone number
                                phoneNumber =""+pCur.getString(pCur
                                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                                // Add names and phone numbers to ArrayLists
                                phoneValueArr.add(phoneNumber.toString());
                                nameValueArr.add(name.toString());

                                j++;
                                k++;
                            }
                        }  // End while loop
                        pCur.close();
                    } // End if

                }  // End while loop

            } // End Cursor value check
            cur.close();
        }
        catch (Exception e)
        {
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_student, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }
}
