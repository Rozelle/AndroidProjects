package com.rozelle.android.readoholic.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class View21 extends ActionBarActivity {
    String searchResult;
    JSONArray resultarray;
    static String[] objres,author,publisher,desciption,amazon;
    private ListView listview;
    public ArrayAdapter<String> myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view21);

    Intent i = getIntent();
    searchResult=i.getStringExtra("result");
        try
        {
            resultarray=new JSONArray(searchResult);
            objres=new String[resultarray.length()];
            author=new String[resultarray.length()];
            publisher=new String[resultarray.length()];
            desciption=new String[resultarray.length()];
            amazon=new String[resultarray.length()];
            for(int j=0;j<resultarray.length();j++)
            {
                objres[j]=resultarray.getJSONObject(j).getJSONArray("book_details").getJSONObject(0).getString("title");
                author[j]=resultarray.getJSONObject(j).getJSONArray("book_details").getJSONObject(0).getString("author");
                publisher[j]=resultarray.getJSONObject(j).getJSONArray("book_details").getJSONObject(0).getString("publisher");
                desciption[j]=resultarray.getJSONObject(j).getJSONArray("book_details").getJSONObject(0).getString("description");
                amazon[j]=resultarray.getJSONObject(j).getString("amazon_product_url");
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        myAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,android.R.id.text1,objres);
        listview=(ListView)findViewById(R.id.listview_books);
        listview.setAdapter(myAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView <?> adapterView, View view, int position, long l)
            {
                String book = myAdapter.getItem(position);
                Intent intent1=new Intent(getApplicationContext(), DetailedActivity.class);
                intent1.putExtra("author",author[position]);
                intent1.putExtra("book",book);
                intent1.putExtra("pub",publisher[position]);
                intent1.putExtra("des",desciption[position]);
                intent1.putExtra("ama",amazon[position]);
                startActivity(intent1);
            }
        });

}
public class booklistreceiver extends BroadcastReceiver
{
    public static final String SEARCH_RESPONSE="com.rozelle.android.readoholic.app.intent.action.SEARCH_RESPONSE";
    @Override
    public void onReceive(Context context,Intent intent)
    {
        searchResult=intent.getStringExtra("booklist");

    }
}


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view21, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
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
