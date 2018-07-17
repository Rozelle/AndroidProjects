package com.example.user.actiontabs;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends FragmentActivity
{
    android.app.Fragment f1,f2,f3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        f1 =getFragmentManager().findFragmentById(R.id.frag1);
        ft.hide(f1);
        f2 =getFragmentManager().findFragmentById(R.id.frag2);
        ft.hide(f2);
        f3 =getFragmentManager().findFragmentById(R.id.frag3);
        ft.hide(f3);
        ft.commit();
    }

    public void buttonOnClick(View v)
    {
        FragmentTransaction ft=getFragmentManager().beginTransaction();
        switch (v.getId())
        {
            case R.id.button1:
                Log.v("here", "button1");
                if(!(f2.isHidden()))
                    ft.hide(f2);
                if(!(f3.isHidden()))
                    ft.hide(f3);
                if(f1.isHidden())
                    ft.show(f1);
                ft.commit();
                break;
            case R.id.button2:
                Log.v("here", "button2");
                if(!(f1.isHidden()))
                    ft.hide(f1);
                if(!(f3.isHidden()))
                    ft.hide(f3);
                if(f2.isHidden())
                    ft.show(f2);
                ft.commit();
                break;
            case R.id.button3:
                Log.v("here", "button3");
                if(!(f2.isHidden()))
                    ft.hide(f2);
                if(!(f1.isHidden()))
                    ft.hide(f1);
                if(f3.isHidden())
                    ft.show(f3);
                ft.commit();
                break;

        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity2, menu);
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
