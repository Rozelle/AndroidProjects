package com.peepal.shifoodata;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class Edit extends ActionBarActivity {

    String phone;
    EditText e1,e2,e3;
    Button b1,b2;
    StudentDBHandler dbHandler;
    Student s;
    String tab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Intent i=getIntent();
        phone=i.getStringExtra("phone");
        tab=i.getStringExtra("tab");

        e1=(EditText)findViewById(R.id.e1);
        e2=(EditText)findViewById(R.id.e2);
        e3=(EditText)findViewById(R.id.e3);
        b1=(Button)findViewById(R.id.b1);
        b2=(Button)findViewById(R.id.b2);

        dbHandler=new StudentDBHandler(this,null,null,1);
        s=dbHandler.findStudent(phone);
        e1.setText(s.getName());
        e2.setText(phone);
        e3.setText(""+s.getAmount());

        //Done button
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //handling this database
                s = dbHandler.findStudent(phone);
                s.setName(e1.getText().toString());
                s.setPhone(e2.getText().toString());
                s.setAmount(Integer.parseInt(e3.getText().toString()));
                dbHandler.deleteStudent(phone);
                dbHandler.addStudent(s);

                //Other database handled
                MyDBHandler myDBHandler = new MyDBHandler(getApplicationContext(), null, null, 1);
                Message m = myDBHandler.findMessage(phone);
                if (m != null) {
                    Message newM = new Message(m.getID(),
                            e1.getText().toString(),//name
                            e2.getText().toString(),//phone
                            m.getAmount(),
                            m.getRemarks(),
                            m.getDate(),
                            m.getPaid(),
                            m.getTxnid());
                    myDBHandler.deleteMessage(phone);
                    myDBHandler.addMessage(newM);
                }
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("tab",tab);
                startActivity(intent);
                finish();
            }
        });

        //discard button
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit, menu);
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
