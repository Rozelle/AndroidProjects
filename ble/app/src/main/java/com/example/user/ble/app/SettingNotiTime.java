package com.example.user.ble.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class SettingNotiTime extends ActionBarActivity {
    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Time = "timeKey";
    String restoredTime;

    Button b;

    TextView text1;
    RadioGroup rg;
    RadioButton rb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_noti_time);

        rg=(RadioGroup)findViewById(R.id.radio);
        rg.setVisibility(View.INVISIBLE);

        b=(Button)findViewById(R.id.setting_save);
        b.setVisibility(View.INVISIBLE);


        sharedPreferences = getSharedPreferences(MyPREFERENCES,0);
        restoredTime = sharedPreferences.getString(Time, null);
        if(restoredTime==null) {
            restoredTime = "5";
        }
        text1=(TextView)findViewById(R.id.timeText);
        text1.setText("You are receiving notifications after "+restoredTime+" minutes");
    }
    public void changePref(View view) {
        rg.setVisibility(View.VISIBLE);
        b.setVisibility(View.VISIBLE);
    }
    public void saveClick(View view)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        int selected=rg.getCheckedRadioButtonId();
        rb=(RadioButton)findViewById(selected);
        if(rb==null)
            editor.putString(Time,restoredTime);
        else
            switch(rb.getId())
            {
                case R.id.radio1:
                    editor.putString(Time, "0").commit();
                    restoredTime="0";
                    break;
                case R.id.radio2:
                    editor.putString(Time, "5").commit();
                    restoredTime="5";
                    break;
                case R.id.radio3:
                    editor.putString(Time, "10").commit();
                    restoredTime="10";
                    break;
                case R.id.radio4:
                    editor.putString(Time, "15").commit();
                    restoredTime="15";
                    break;
                case R.id.radio5:
                    editor.putString(Time, "20").commit();
                    restoredTime="20";
                    break;
                default:
                    editor.putString(Time, restoredTime).commit();
            }
        text1.setText("You are receiving notifications after "+restoredTime+" minutes");
        rg.setVisibility(View.INVISIBLE);
        b.setVisibility(View.INVISIBLE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_setting_noti_time, menu);
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
