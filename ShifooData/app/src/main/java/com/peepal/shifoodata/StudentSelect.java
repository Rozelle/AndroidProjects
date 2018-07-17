package com.peepal.shifoodata;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class StudentSelect extends ActionBarActivity {

    Intent i;
    int position;
    ListView lv;
    ArrayList<String> arrayList;
    String n,p,amt,d;
    List<Model> list;
    ArrayAdapter<Model> adapter;
    View view;
    String remarks,message;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_select);

        i=getIntent();
        position=i.getIntExtra("select", 0);
        arrayList=i.getStringArrayListExtra("list");
        lv= (ListView) findViewById(R.id.studentSelect);
        adapter = new StudentSelectListadapter(this, getModel());
        lv.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_student_select, menu);
        return true;
    }
    public List<Model> getModel() {
        list = new ArrayList<Model>();
        int len,t;
        String a,b;
        for(int j=0;j<arrayList.size();j++)
        {
            len=arrayList.get(j).length();
            t=arrayList.get(j).indexOf('|');
            a=arrayList.get(j).substring(0, t);
            b=arrayList.get(j).substring(t + 2, len);
            n=a;

            len=b.length();
            t=b.indexOf('|');
            a=b.substring(0, t);
            b=b.substring(t + 2, len);
            p=a;

            len=b.length();
            t=b.indexOf('|');
            a=b.substring(0, t);
            b=b.substring(t + 2, len);
            amt=a;
            d=b;

            list.add(get(n,p,Integer.parseInt(amt),d));
        }
        // Initially select one of the items
        list.get(position).setSelected(true);
        return list;
    }

    private Model get(String n,String p,int amt,String date) {
        return new Model(n,p,amt,date);
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

    public void remindClick(View v)
    {
        message="";
        for (int j=0;j<arrayList.size();j++)
        {
            if(list.get(j).isSelected())
                message+=list.get(j).getPhone()+"-"+list.get(j).getAmount()+",";
        }
        if (message.equals(""))
            Toast.makeText(this,"Select atleast one students",Toast.LENGTH_SHORT).show();
        else {
            message=message.substring(0,message.length()-1);
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
                    if(isConnected())
                    {
                        try {
                            Toast.makeText(getApplicationContext(), "Data sending...", Toast.LENGTH_LONG).show();
                            final TelephonyManager tm = (TelephonyManager) getApplicationContext()
                                    .getSystemService(getApplicationContext().TELEPHONY_SERVICE);
                            final String tmDevice;
                            tmDevice = "" + tm.getDeviceId();
                            //see server url
                            new HttpAsyncTask().execute("http://advanced.shifoo.in/payment/process-data",
                                    tmDevice,"shifoo",message,remarks);

                            Calendar c = Calendar.getInstance();
                            SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");
                            String formattedDate = df.format(c.getTime());
                            Message m;
                            MyDBHandler myDBHandler=new MyDBHandler(getApplicationContext(),null,null,1);
                            for (int j=0;j<arrayList.size();j++)
                            {
                                if(list.get(j).isSelected())
                                {
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
                        catch (Exception e)
                        {
                            Toast.makeText(getApplicationContext(),
                                    "Data failed, please try again.", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }//if ends
                    else
                    {
                        Toast.makeText(getApplicationContext(),
                                "Please check your Internet connection",Toast.LENGTH_SHORT).show();
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
        }//else ends
    }//function ends

    public void deleteClick(View v)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        view = inflater.inflate(R.layout.dialog_delete, null);
        builder.setView(view);
        alertDialog = builder.create();
        Button done = (Button) view.findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //deletes from app itself
                boolean flag = true;
                StudentDBHandler studentDBHandler = new StudentDBHandler(getApplicationContext(), null, null, 1);
                MyDBHandler myDBHandler = new MyDBHandler(getApplicationContext(), null, null, 1);
                for (int j = 0; j < arrayList.size(); j++) {
                    if (list.get(j).isSelected()) {
                        flag = false;
                        studentDBHandler.deleteStudent(list.get(j).getPhone());
                        myDBHandler.deleteMessage(list.get(j).getPhone());
                    }
                }//for ends
                if (flag)
                    Toast.makeText(getApplicationContext(), "Select at least one student!", Toast.LENGTH_SHORT).show();
                else {
                    i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                    finish();
                }
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
    public static String POST(String url, String dev,String keyword,String message,String remark){
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
                    .put("remark",remark);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }
}
