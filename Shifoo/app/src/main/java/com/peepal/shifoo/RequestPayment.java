package com.peepal.shifoo;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class RequestPayment extends ActionBarActivity {


    StudentDBHandler studentDBHandler;
    MyDBHandler dbHandler;
    TextView textView=null;
    EditText e1,e2;
    String name,number;
    int fees;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_payment);

        Intent i=getIntent();
        number=i.getStringExtra("phone");
        studentDBHandler = new StudentDBHandler(this, null, null, 1);
        Student s=studentDBHandler.findStudent(number);
        name=s.getName();

        textView=(TextView)findViewById(R.id.toNumber);
        textView.setText(name);
        e1=(EditText)findViewById(R.id.edit2);
        e1.setText(""+s.getAmount());

        dbHandler = new MyDBHandler(this, null, null, 1);
        e2=(EditText)findViewById(R.id.edit3);
        e2.requestFocus();
    }

    public void sendClick(View view)
    {
        String remarks=e2.getText().toString();
        if(e1.length()==0)
        {
            Toast.makeText(this,"Add amount!",Toast.LENGTH_SHORT).show();
            return;
        }
        if(remarks.length()==0)
        {
            Toast.makeText(this,"Enter remarks!",Toast.LENGTH_SHORT).show();
            return;
        }
        String message="shifoo "+number+"*"+e1.getText().toString()+"*"+remarks;
        try {
            SmsManager smsManager = SmsManager.getDefault();
            Message m;
            smsManager.sendTextMessage("9663536784", null, message, null, null);
            Toast.makeText(this, "SMS sent.", Toast.LENGTH_LONG).show();

            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");
            String formattedDate = df.format(c.getTime());
            m = dbHandler.findMessage(number);
            if(m!=null)
            {
                m.setDate(formattedDate);
                m.setPaid(0);
                m.setTxnid("!");
                m.setAmount(Integer.parseInt(e1.getText().toString()));
                m.setRemarks(remarks);
                dbHandler.addMessage(m);
            }
            else
            {
                m=new Message(textView.getText().toString(),
                        number,
                        Integer.parseInt(e1.getText().toString()),
                        remarks,
                        formattedDate,
                        0,
                        "!");
                dbHandler.addMessage(m);
            }
            if(!e1.getText().toString().equals(fees))
            {
                Student s=studentDBHandler.findStudent(number);
                studentDBHandler.deleteStudent(number);
                s.setAmount(Integer.parseInt(e1.getText().toString()));
                studentDBHandler.addStudent(s);
            }
            textView.setText("");
            e1.setText("");
            e2.setText("");
            Intent i= new Intent(this,MainActivity.class);
            startActivity(i);
            finish();
        }
        catch (Exception e)
        {
            Toast.makeText(this, "SMS failed, please try again.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public void cancelClick(View view)
    {
        textView.setText("");
        e1.setText("");
        e2.setText("");
        Intent i= new Intent(this,MainActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_request_payment, menu);
        return true;
    }
}
