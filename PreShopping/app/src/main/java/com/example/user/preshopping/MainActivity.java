package com.example.user.preshopping;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends ActionBarActivity {
    Intent i;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences=getSharedPreferences("MyPrefs",0);
        String name=sharedPreferences.getString("nameKey",null);
        if(name!=null)
        {
            //stop Activity
            //instead of starting an activity, inflate this class's layout
            Intent i=new Intent(this,BroadCategory.class);
            startActivity(i);
        }
        else
            if(isConnected(getApplicationContext())==false)
            {
                i=new Intent(this, nonet.class);
                startActivity(i);
            }
    }

    //Service extends IntentService, thus destroying not required
    private boolean isConnected(Context ctx)
    {
        ConnectivityManager connMgr = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    //Button Click
    public void buttonOnClick(View v)
    {
        switch (v.getId())
        {
            case R.id.signup:
                i=new Intent(this,Signup.class);
                startActivity(i);
                break;
            case R.id.signin:
                i=new Intent(this,Signin.class);
                startActivity(i);
                break;
            case R.id.signinfb:
                //do fb login
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
