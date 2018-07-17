package com.example.user.preshopping;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class SelectType extends ActionBarActivity {

    String sex;
    Intent i;
    Button b1,b2,b3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_type);

        i=getIntent();
        sex=i.getStringExtra("sex");
        b1=(Button)findViewById(R.id.button1);
        b2=(Button)findViewById(R.id.button2);
        b3=(Button)findViewById(R.id.button4);
        if(sex.equals("man"))
        {
            b1.setText("T-shirts");
            b2.setText("Shirts");
            b3.setText("Belts n' Wallets");
        }
        else
        {
            b1.setText("Ethnic");
            b2.setText("Dresses n' Tops");
            b3.setText("Bags n' Sunglasses");
        }

    }

    public void LoadClick(View v)
    {
        switch (v.getId())
        {
            case R.id.button1:
                //send url
                break;
            case R.id.button2:
                //send urls
                break;
            case R.id.button3:
                //send url
                break;
            case R.id.button4:
                //send url
                break;
            case R.id.button5:
                //send url
                break;
            case R.id.button6:
                //send url
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_select_type, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_likes) {
            //load likes only
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
