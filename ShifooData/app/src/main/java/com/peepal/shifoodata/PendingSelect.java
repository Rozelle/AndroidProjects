package com.peepal.shifoodata;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.provider.*;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class PendingSelect extends ActionBarActivity {

    Intent intent;
    int position;
    ListView lv;
    ArrayList<String> arrayList;
    String name,phone,amt,date,remarks,txnid;
    List<ModelP> list;
    ArrayAdapter<ModelP> adapter;
    View view;
    String message,tab;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_select);

        intent=getIntent();
        position=intent.getIntExtra("select", 0);
        arrayList=intent.getStringArrayListExtra("list");
        tab=intent.getStringExtra("tab");
        lv= (ListView) findViewById(R.id.pendingSelect);
        adapter = new PendingSelectListadapter(this, getModelP());
        lv.setAdapter(adapter);

        if(tab.equals("Paid"))
        {
            Button remind=(Button)findViewById(R.id.remind);
            remind.setText("Request");
            Button move=(Button)findViewById(R.id.paid);
            move.setText("Move to Pending");
        }
    }

    public List<ModelP> getModelP() {

        int i,len;
        String a;
        String b;
        list = new ArrayList<ModelP>();
        for(int j=0;j<arrayList.size();j++)
        {
            len=arrayList.get(j).length();
            i=arrayList.get(j).indexOf('|');
            a=arrayList.get(j).substring(0, i);
            name=a;
            b=arrayList.get(j).substring(i + 2, len);

            len=b.length();
            i=b.indexOf('|');
            a=b.substring(0, i);
            phone=a;
            b=b.substring(i + 2, len);

            len=b.length();
            i=b.indexOf('|');
            a=b.substring(0, i);
            amt=a;
            b=b.substring(i + 2, len);

            len=b.length();
            i = b.indexOf('|');
            a = b.substring(0, i);
            date=a;
            b = b.substring(i + 2, len);

            len = b.length();
            i = b.indexOf('|');
            a = b.substring(0, i);
            if (a == "!")
                remarks = "";
            else
                remarks = a;
            b = b.substring(i + 2, len);
            txnid=b;

            list.add(get(name, phone, Integer.parseInt(amt), date, remarks, txnid));
        }//for ends
        // Initially select one of the items
        list.get(position).setSelected(true);
        return list;
    }

    private ModelP get(String name,String phone,int amt,String date,String remarks,String txnid) {
        return new ModelP(name,phone,amt,date,remarks,txnid);
    }

    public void selectAllClick(View v)
    {
        Button b=(Button)findViewById(R.id.selectAll);
        if(b.getText().toString().equalsIgnoreCase("Select all")) {
            for (int j = 0; j < arrayList.size(); j++)
                list.get(j).setSelected(true);
            b.setText("Unselect all");
        }
        else
        {
            for (int j = 0; j < arrayList.size(); j++)
                list.get(j).setSelected(false);
            b.setText("Select all");
        }
        adapter.notifyDataSetChanged();
    }

    public void remindClick(View v) {

        final String keyword;
        final String android_id = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");
        String formattedDate = df.format(c.getTime());

        Message m;
        boolean flag = true;
        MyDBHandler myDBHandler = new MyDBHandler(getApplicationContext(), null, null, 1);

        if(tab.equals("Pending")) {
            keyword="remind";
            remarks=null;
            if(isConnected())
            {
                try
                {
                    for (int j = 0; j < arrayList.size(); j++) {
                        if (list.get(j).isSelected()) {
                            flag = false;
                            message = list.get(j).getPhone() + "-" + list.get(j).getAmount() + "*" + list.get(j).getRemarks();
                            //send data some how
                            m = myDBHandler.findMessage(list.get(j).getPhone());
                            m.setDate(formattedDate);
                            myDBHandler.addMessage(m);
                        }
                    }
                    new HttpAsyncTask().execute("http://advanced.shifoo.in/payment/process-data",
                            android_id,keyword,message,remarks);
                }
                catch (Exception e)
                { Toast.makeText(getApplicationContext(),
                        "Data failed, please try again.", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }//if(isConnected) ends
            else
            {
                Toast.makeText(getApplicationContext(),
                        "Please check your Internet connection", Toast.LENGTH_SHORT).show();
            }
        }//if(pending tab)ends
        else//Paid tab
        {
            keyword="shifoo";
            for (int j=0;j<arrayList.size();j++) {
                if (list.get(j).isSelected()) {
                    message += list.get(j).getPhone() + "-" + list.get(j).getAmount() + ",";
                    flag = false;
                }
            }
            if (!flag) {
                message = message.substring(0, message.length() - 1);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                // Get the layout inflater
                LayoutInflater inflater = this.getLayoutInflater();
                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                view = inflater.inflate(R.layout.dialog_remark, null);
                builder.setView(view);
                alertDialog = builder.create();
                Button done = (Button) view.findViewById(R.id.done);
                done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText editText = (EditText) view.findViewById(R.id.remark);
                        remarks = editText.getText().toString();
                        if (remarks.length() == 0) {
                            Toast.makeText(getApplicationContext(), "Enter remarks", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        //add message
                        if (isConnected()) {
                            try {
                                Toast.makeText(getApplicationContext(), "Data sending...", Toast.LENGTH_LONG).show();
                                //see server url
                                new HttpAsyncTask().execute("http://advanced.shifoo.in/payment/process-data",
                                        android_id, keyword, message, remarks);

                                Calendar c = Calendar.getInstance();
                                SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");
                                String formattedDate = df.format(c.getTime());
                                Message m;
                                MyDBHandler myDBHandler = new MyDBHandler(getApplicationContext(), null, null, 1);
                                for (int j = 0; j < arrayList.size(); j++) {
                                    if (list.get(j).isSelected()) {
                                        m = myDBHandler.findMessage(list.get(j).getPhone());
                                        if (m != null) {
                                            m.setDate(formattedDate);
                                            m.setPaid(0);
                                            m.setTxnid("!");
                                            m.setAmount(list.get(j).getAmount());
                                            m.setRemarks(remarks);
                                            myDBHandler.addMessage(m);
                                        } else {
                                            Log.v("tag", "else" + message);
                                            m = new Message(list.get(j).getName(),
                                                    list.get(j).getPhone(),
                                                    list.get(j).getAmount(),
                                                    remarks,
                                                    formattedDate,
                                                    0,
                                                    "!");
                                            myDBHandler.addMessage(m);
                                        }
                                    }//if ends
                                }//for ends
                            } //try ends
                            catch (Exception e) {
                                Toast.makeText(getApplicationContext(),
                                        "Data failed, please try again.", Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                        }//if(isConnected) ends
                        else {
                            Toast.makeText(getApplicationContext(),
                                    "Please check your Internet connection", Toast.LENGTH_SHORT).show();
                        }
                        alertDialog.dismiss();
                    }
                });
                Button discard = (Button) view.findViewById(R.id.discard);
                discard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
            }//if(!flag) closes
        }//else ends
        if (flag)
            Toast.makeText(this, "Select atleast one student", Toast.LENGTH_SHORT).show();
    }

    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            return POST(params[0],params[1],params[2],params[3],params[4]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), "Data Sent!", Toast.LENGTH_LONG).show();
        }
    }
    public static String POST(String url, String dev,String keyword,String message,String remarks){
        InputStream inputStream = null;
        String result = "";
        try {

            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);

            String json = "";

            // 3. build jsonObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("device",dev)
                    .put("keyword",keyword)
                    .put("message",message)
                    .put("remark",remarks);
            // 4. convert JSONObject to JSON to String
            json = jsonObject.toString();

            // 5. set json to StringEntity
            StringEntity se = new StringEntity(json);

            // 6. set httpPost Entity
            httpPost.setEntity(se);

            // 7. Set some headers to inform server about the type of the content
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);

            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // 10. convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        }
        catch (Exception e) {}

        // 11. return result
        return result;
    }
    private static String convertInputStreamToString(InputStream inputStream)
            throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    public void paidClick(View v)
    {
        boolean flag=true;
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");
        String formattedDate = df.format(c.getTime());

        MyDBHandler myDBHandler=new MyDBHandler(getApplicationContext(),null,null,1);
        Message m;
        StudentDBHandler studentDBHandler=new StudentDBHandler(this,null,null,1);
        Student s;
        String p;
        if(tab.equals("Pending")) {
            for (int j = 0; j < arrayList.size(); j++) {
                if (list.get(j).isSelected()) {
                    p=list.get(j).getPhone();
                    flag = false;

                    m = myDBHandler.findMessage(p);
                    m.setTxnid("Paid");
                    m.setPaid(1);
                    m.setDate(formattedDate);
                    myDBHandler.addMessage(m);

                    s = studentDBHandler.findStudent(p);
                    s.setDate(formattedDate);
                    studentDBHandler.deleteStudent(p);
                    studentDBHandler.addStudent(s);
                }
            }
        }
        else
        {
            for (int j=0;j<arrayList.size();j++) {
                if (list.get(j).isSelected()) {
                    flag = false;
                    p=list.get(j).getPhone();

                    m = myDBHandler.findMessage(p);
                    m.setTxnid("!");
                    m.setPaid(0);
                    m.setDate(formattedDate);
                    myDBHandler.addMessage(m);

                    s = studentDBHandler.findStudent(p);
                    s.setDate("!");
                    studentDBHandler.deleteStudent(p);
                    studentDBHandler.addStudent(s);
                }
            }
        }
        if(flag)
            Toast.makeText(this,"Select atleast one student",Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pending_select, menu);
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


