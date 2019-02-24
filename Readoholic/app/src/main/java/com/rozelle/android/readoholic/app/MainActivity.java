package com.rozelle.android.readoholic.app;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Button;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity //implements View.OnClickListener
{
    Intent i;
    String urlname,searchResult;
    String[] main_string = {};
    private booklistreceiver receiver;
    ProgressDialog progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
            progress=new ProgressDialog(this);
            progress.setTitle("Loading");
            progress.setMessage("Please wait...");
        }

        if(isConnected(getApplicationContext())==false)
        {
            i=new Intent(MainActivity.this, nonet.class);
            startActivity(i);
        }
        i = new Intent(MainActivity.this, View21.class);
        IntentFilter filter = new IntentFilter(booklistreceiver.SEARCH_RESPONSE);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver=new booklistreceiver();

        registerReceiver(receiver, filter);

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

    public class booklistreceiver extends BroadcastReceiver
    {
        public static final String SEARCH_RESPONSE="com.rozelle.android.readoholic.app.intent.action.SEARCH_RESPONSE";

        @Override
        public void onReceive(Context context,Intent intent)
        {
            searchResult=intent.getStringExtra("booklist");
            i.putExtra("result", searchResult);
            progress.dismiss();
            startActivity(i);
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

    public void ButtonOnClick(View v) {
        switch (v.getId()) {
            case R.id.fictionB:
                urlname="http://api.nytimes.com/svc/books/v2/lists.json?list=combined-print-fiction&api-key=3DeoFAQ5ok9y7qOF8bIz5ynO2MLRMePi";
                transfer();
                break;
            case R.id.nonfictionB:
                urlname="http://api.nytimes.com/svc/books/v2/lists.json?list=combined-print-nonfiction&api-key=3DeoFAQ5ok9y7qOF8bIz5ynO2MLRMePi";
                transfer();
                break;
            case R.id.crimeB:
                urlname="http://api.nytimes.com/svc/books/v2/lists.json?list=crime-and-punishment&api-key=3DeoFAQ5ok9y7qOF8bIz5ynO2MLRMePi";
                transfer();
                break;
            case R.id.religionB:
                urlname="http://api.nytimes.com/svc/books/v2/lists.json?list=religion-spirituality-and-faith&api-key=3DeoFAQ5ok9y7qOF8bIz5ynO2MLRMePi";
                transfer();
                break;
            case R.id.scienceficB:
                urlname="http://api.nytimes.com/svc/books/v2/lists.json?list=science&api-key=3DeoFAQ5ok9y7qOF8bIz5ynO2MLRMePi";
                transfer();
                break;
            case R.id.politicsB:
                urlname="http://api.nytimes.com/svc/books/v2/lists.json?list=hardcover-political-books&api-key=3DeoFAQ5ok9y7qOF8bIz5ynO2MLRMePi";
                transfer();
                break;
            case R.id.romanticB:
                urlname="http://api.nytimes.com/svc/books/v2/lists.json?list=relationships&api-key=3DeoFAQ5ok9y7qOF8bIz5ynO2MLRMePi";
                transfer();
                break;
            case R.id.travelB:
                urlname="http://api.nytimes.com/svc/books/v2/lists.json?list=travel&api-key=3DeoFAQ5ok9y7qOF8bIz5ynO2MLRMePi";
                transfer();
                break;
            case R.id.humourB:
                urlname="http://api.nytimes.com/svc/books/v2/lists.json?list=humor&api-key=3DeoFAQ5ok9y7qOF8bIz5ynO2MLRMePi";
                transfer();
                break;
            case R.id.businessB:
                urlname="http://api.nytimes.com/svc/books/v2/lists.json?list=paperback-business-books&api-key=3DeoFAQ5ok9y7qOF8bIz5ynO2MLRMePi";
                transfer();
                break;
            case R.id.foodfitnessB:
                urlname="http://api.nytimes.com/svc/books/v2/lists.json?list=food-and-fitness&api-key=3DeoFAQ5ok9y7qOF8bIz5ynO2MLRMePi";
                transfer();
                break;


        }
        ArrayList<String[]> ar = new ArrayList<String[]>();
        ar.add(main_string);


    }
    public void transfer()
    {

        progress.show();
        Intent intentService = new Intent(getApplicationContext(), IntentServiceBookList.class);
        intentService.putExtra("url",urlname);
        startService(intentService);

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }
}

