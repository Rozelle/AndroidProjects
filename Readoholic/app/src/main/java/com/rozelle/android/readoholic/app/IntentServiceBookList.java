package com.rozelle.android.readoholic.app;

import android.app.IntentService;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.nfc.Tag;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;


public class IntentServiceBookList extends IntentService
{

    public IntentServiceBookList()
    {
        super("IntentServiceBookList");
    }
    @Override
    protected void onHandleIntent(Intent intent) {
       String url= intent.getStringExtra("url");
        HttpClient httpClient=new DefaultHttpClient();
        HttpGet request = new HttpGet();
        URI website = null;
        try {
            website = new URI(url);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        request.setURI(website);
        try
        {
            HttpResponse response=httpClient.execute(request);
            InputStream is=response.getEntity().getContent();
            InputStreamReader isr=new InputStreamReader(is);
            BufferedReader br=new BufferedReader(isr);
            String result=br.readLine();
            JSONObject jresponse=new JSONObject(result);
            JSONArray resultset=jresponse.getJSONArray("results");
            Intent broadcastIntent=new Intent();
            broadcastIntent.setAction(View21.booklistreceiver.SEARCH_RESPONSE);
            broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
            broadcastIntent.putExtra("booklist",resultset.toString());
            sendBroadcast(broadcastIntent);
        }
        catch(ClientProtocolException e)
        {
            e.printStackTrace();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }
    }
}
