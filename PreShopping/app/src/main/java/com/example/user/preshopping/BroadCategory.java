package com.example.user.preshopping;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class BroadCategory extends ActionBarActivity {

    Intent i;

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Name = "nameKey";
    public static final String Email="emailKey";
    public static final String Password="passwordKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broad_category);
    }

    public void ManCategory(View v)
    {
        i=new Intent(this,SelectType.class);
        i.putExtra("sex","man");
        startActivity(i);
    }
    public void WomanCategory(View v)
    {
        i=new Intent(this,SelectType.class);
        i.putExtra("sex","woman");
        startActivity(i);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_broad_category, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_switch) {
            sharedpreferences=getSharedPreferences(MyPREFERENCES,0);
            SharedPreferences.Editor editor=sharedpreferences.edit();
            editor.putString(Name,null);
            editor.putString(Email,null);
            editor.putString(Password,null);
            editor.commit();
            i=new Intent(this,MainActivity.class);
            startActivity(i);
            return true;
        }
        else
        if(id==R.id.action_account)
        {
            i=new Intent(this,AccountSettings.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
