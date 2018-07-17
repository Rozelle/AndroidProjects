package com.example.user.preshopping;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static android.provider.ContactsContract.CommonDataKinds.Email;


public class PasswordEmail extends ActionBarActivity {
    EditText e;
    Button b;
    TextView tv;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_email);

        e=(EditText)findViewById(R.id.editPE1);
        b=(Button)findViewById(R.id.confirmPE);
        tv=(TextView)findViewById(R.id.timeText);
        tv.setVisibility(View.INVISIBLE);
    }

    public void EmailButton(View v)
    {
        String email=e.getText().toString();
        String password="xyz";//change this after getting User_Data ready!
        boolean flag=false;
        //check in database for email, if present:flag=true and get password
        if(flag==false)
        {
            Toast.makeText(this,"Recheck password",Toast.LENGTH_LONG).show();
            e.setText("");
        }
        else
        {
            b.setVisibility(View.GONE);
            tv.setVisibility(View.VISIBLE);

            //send email containing password
            String subject="PreShopping";
            String text="You password is "+password+"\nPlease be careful!";
            final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
            emailIntent.setType("plain/text");
            emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{email});
            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
            emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, text);
            try {
                startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
            }

            //Timer
            context = this;
            new CountDownTimer(10 * 1000, 1000) {
                public void onTick(long millisUntilFinished) {
                    tv.setText("Sending Email in " + millisUntilFinished / 1000 + " second(s)");
                }

                public void onFinish() {
                    Toast.makeText(context, "Check your Email and Sign in again", Toast.LENGTH_LONG).show();
                }
            }.start();
            Intent i = new Intent(this, Signin.class);
            startActivity(i);
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_password_email, menu);
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
