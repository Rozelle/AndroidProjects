package com.peepal.shifoodata;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;


public class Settings extends ActionBarActivity {
    TextView t1,t2,t3,t4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        t1=(TextView)findViewById(R.id.noti);
        t2=(TextView)findViewById(R.id.change);
        t3=(TextView)findViewById(R.id.privacy);
        t4=(TextView)findViewById(R.id.terms);
    }

    public void Notify(View v)
    {

    }
    public void ChangePh(View v)
    {

    }
    public void PrivacyP(View v)
    {
        Intent i;
        i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse("http://www.shifoo.in/site/privacy-policy"));
        startActivity(i);
    }
    public void TermsC(View v)
    {
        Intent i;
        i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse("http://www.shifoo.in/site/terms-and-conditions"));
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
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
