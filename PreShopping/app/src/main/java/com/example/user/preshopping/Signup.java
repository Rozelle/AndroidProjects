package com.example.user.preshopping;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class Signup extends ActionBarActivity {
    EditText e1,e2,e3,e4;

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Name = "nameKey";
    public static final String Email="emailKey";
    public static final String Password="passwordKey";
    String name,email,password,confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        e1=(EditText)findViewById(R.id.edit1);
        e2=(EditText)findViewById(R.id.edit2);
        e3=(EditText)findViewById(R.id.edit3);
        e4=(EditText)findViewById(R.id.edit4);
    }

    public void buttonClick(View v)
    {
        name=e1.getText().toString();
        email=e2.getText().toString();
        password=e3.getText().toString();
        confirm=e4.getText().toString();
        boolean start=true;

        //check if email id already exists in database created online
        //Checking password
        if(password.length()<8)
        {
            Toast.makeText(this,"Password length is not 8 characters",Toast.LENGTH_SHORT).show();
            e3.setText("");
            e4.setText("");
            start=false;
        }
        boolean flagChar=false;//special char not present
        boolean flag=false;//other char not present
        char c;
        for(int i=0;i<password.length();i++)
        {
            c=password.charAt(i);
            switch (c) {
                case '@':
                case '#':
                case '*':
                case '-':
                case '_':
                case '/':
                    flagChar = true;//special char present
                    break;
                default:
                    if(c>='a'&&c<='z'||c>='A'&&c<='Z')
                        continue;
                    else
                    {
                        flag=true;//other char present
                        break;
                    }
            }//switch ends
        }//for ends
        if(flag==true||flagChar==false)
        {
            start=false;
            Toast.makeText(this,"Password instructions not followed!",Toast.LENGTH_SHORT).show();
            e3.setText("");
            e4.setText("");
        }
        else
        {
           flag=false;
            if(password.length()!=confirm.length())
                flag=true;
            else
                for (int i=0;i<password.length();i++)
                    if(password.charAt(i)!=confirm.charAt(i))
                    {
                        flag=true;
                        break;
                    }
            if(flag==true)
            {
                start=false;
                Toast.makeText(this,"Passwords don't match",Toast.LENGTH_SHORT).show();
                e3.setText("");
                e4.setText("");
            }
        }//else ends
        if(start==true)
        {
            sharedpreferences=getSharedPreferences(MyPREFERENCES,0);
            SharedPreferences.Editor editor=sharedpreferences.edit();
            editor.putString(Name,name);
            editor.putString(Email,email);
            editor.putString(Password,password);
            editor.commit();
            Intent i=new Intent(this,BroadCategory.class);
            startActivity(i);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_signup, menu);
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
