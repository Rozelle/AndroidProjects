package com.peepal.shifoodata;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import static java.lang.Character.isAlphabetic;
import static java.lang.Character.isDigit;


public class SearchDisplay extends ActionBarActivity {

    ListView lv;
    TextView tv;
    String tab,query;
    int show;
    ArrayList<String> array_list,dispList;
    FragmentListAdapter listAdapter;
    MyDBHandler dbHandler;
    StudentDBHandler studentDBHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_display);

        Intent intent=getIntent();
        tab=intent.getStringExtra("tab");
        query=intent.getStringExtra("query");
        if(isAlphabetic(query.charAt(0)))
            query.toLowerCase();
        query=(query.substring(0,1).toUpperCase())+query.substring(1,query.length());
        if(isDigit(query.charAt(0)))
        {
            int j=0;
            String add="";
            while(j<query.length())
            {
                if(query.charAt(j)!=' ')
                    add+=query.charAt(j);
                j++;
            }
            if(add.length()>10)
                add=add.substring(add.length()-10,add.length());
            query=add;
        }
        tv=(TextView)findViewById(R.id.notfound);
        lv=(ListView)findViewById(R.id.found);
        dbHandler = new MyDBHandler(this, null, null, 1);
        studentDBHandler = new StudentDBHandler(this, null, null, 1);
        dispList=new ArrayList<>();

        if(tab.equals("Pending")) {
            array_list = dbHandler.getAll0Messages();
            show=1;
        }
        else
        if(tab.equals("Paid")){
            array_list = dbHandler.getAll1Messages();
            show=0;
        }
        else
        {
            array_list=studentDBHandler.getAllStudents();
            show=0;
        }
        for(int i=0;i<array_list.size();i++)
        {
            if(array_list.get(i).contains(query)) {
                dispList.add(array_list.get(i));
            }
        }

        listAdapter = new FragmentListAdapter(this, dispList,show);
        lv.setAdapter(listAdapter);
        if(dispList.size()<=0) {
            tv.setVisibility(View.VISIBLE);
        }
        else {
            tv.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_display, menu);
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
