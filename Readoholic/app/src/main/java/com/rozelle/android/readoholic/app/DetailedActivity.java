package com.rozelle.android.readoholic.app;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;


public class DetailedActivity extends ActionBarActivity
{
    public String amazonStr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);
        Intent intent = this.getIntent();
        //printing book name on top
        if (intent != null)
        {
            String bookStr = intent.getStringExtra("book");
            ((TextView)findViewById(R.id.detail_text))
                    .setText(bookStr);

            String authorStr = intent.getStringExtra("author");
            ((TextView)findViewById(R.id.detail_text1))
                    .setText(authorStr);

            String publisherStr = intent.getStringExtra("pub");
            ((TextView)findViewById(R.id.detail_text2))
                    .setText(publisherStr);

            String descriptionStr = intent.getStringExtra("des");
            if(descriptionStr.length()==0)
                descriptionStr="(Not Available!)";
            ((TextView)findViewById(R.id.detail_text3))
                    .setText(descriptionStr);

            amazonStr = intent.getStringExtra("ama");
            if(amazonStr.length()==0)
                amazonStr="(Not Available!)";

            ((TextView)findViewById(R.id.detail_text4))
                    .setText(amazonStr);
            TextView tv=(TextView)findViewById(R.id.detail_text4);
            tv.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v)
                {
                    amazonmethod(v);
                }
            });
        }

    }
    public void amazonmethod(View v)
    {
        if (!amazonStr.startsWith("https://") && !amazonStr.startsWith("http://"))
        {
            amazonStr = "http://" + amazonStr;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(amazonStr));
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detailed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            //Intent intent=new Intent(getApplicationContext(),Wishlist.class);
            //intent.putExtra()
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
