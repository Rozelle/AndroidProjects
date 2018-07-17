package com.example.user.preshopping;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class Signin extends ActionBarActivity {
    EditText e1,e2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        e1=(EditText)findViewById(R.id.editSI1);
        e2=(EditText)findViewById(R.id.editSI2);
    }
    public void ButtonClick(View v)
    {
        boolean flag=false;
        String email,pass;
        email=e1.getText().toString();
        pass=e2.getText().toString();
        //check in database if email id and password match, yes:flag=true

        if(flag==true)
        {
            Intent i=new Intent(this,BroadCategory.class);
            startActivity(i);
        }
        else {
            e2.setText("");
            Toast.makeText(this, "Invalid Password", Toast.LENGTH_SHORT).show();
        }
    }

    public void textClick(View v)
    {
        Intent i=new Intent(this,PasswordEmail.class);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_signin, menu);
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
