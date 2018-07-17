package com.peepal.shifoodata;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
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


public class RequestPayment extends ActionBarActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener{

    Intent intent;
    private ArrayAdapter<String> adapter;
    AutoCompleteTextView textView=null;
    ListView lv;
    EditText e3;
    TextView tv,names;
    Button b1;
    boolean flag,send;

    String toNumberValue="",name="",toAmountValue="",value;
    MySimpleArrayAdapter listAdapter;
    ArrayList<String> mArrayList;
    MyDBHandler dbHandler;
    StudentDBHandler studentDBHandler;

    // Store contacts values in these arraylist
    public static ArrayList<String> phoneValueArr = new ArrayList<>();
    public static ArrayList<String> nameValueArr = new ArrayList<>();
    public static ArrayList<String> amountValueArr = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        dbHandler = new MyDBHandler(this, null, null, 1);
        studentDBHandler = new StudentDBHandler(this, null, null, 1);
        ArrayList<String> arrayList=studentDBHandler.getAllStudents();
        for (int j=0;j<arrayList.size();j++)
        {
            value=arrayList.get(j);
            nameValueArr.add(value.substring(0,value.indexOf('|')));
            value=value.substring(value.indexOf('|')+2,value.length());
            phoneValueArr.add(value.substring(0,value.indexOf('|')));
            value=value.substring(value.indexOf('|')+2,value.length());
            amountValueArr.add(value.substring(0,value.indexOf('|')));
        }

        e3=(EditText)findViewById(R.id.edit3);
        e3.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus&&flag==true)
                {
                    lv.setVisibility(View.GONE);
                    flag=false;
                }
            }
        });
        tv=(TextView)findViewById(R.id.change);
        names=(TextView)findViewById(R.id.names);
        flag=false;

        adapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_dropdown_item_1line, nameValueArr);

        textView = (AutoCompleteTextView)findViewById(R.id.toNumber);
        textView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus&&flag==true)
                {
                    lv.setVisibility(View.GONE);
                    flag=false;
                }
            }
        });
        lv=(ListView)findViewById(R.id.selected);
        mArrayList = new ArrayList<>();
        listAdapter = new MySimpleArrayAdapter(this, mArrayList,tv,names,this);
        lv.setAdapter(listAdapter);

        b1=(Button)findViewById(R.id.button1);

        //Set adapter to AutoCompleteTextView
        textView.setAdapter(adapter);
        textView.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
        textView.setOnItemClickListener((AdapterView.OnItemClickListener) this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_request_payment, menu);
        return true;
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
            return POST(params[0],params[1]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), "Data Sent!", Toast.LENGTH_LONG).show();
        }
    }
    public static String POST(String url, String message){
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
            jsonObject.accumulate("message", message);
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

    //Sending data in this function
    public void sendClick(View v)
    {
        send=true;
        if (lv.getCount() == 0)
        {
            send=false;
        }
        if ( e3.getText().length()==0)
        {
            send=false;
        }
        else
        {
            String message="shifoo ",temp,check,add,name,amt;
            int t,j;
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");
            String formattedDate = df.format(c.getTime());
            //message construction
            for (int i = 0; i < lv.getCount(); i++)
            {
                if(i>0)
                    message+=",";
                temp=lv.getItemAtPosition(i).toString();
                t=temp.indexOf('\n');
                name=temp.substring(0, t);
                temp=temp.substring(t+1,temp.length());
                check=temp.substring(0, temp.indexOf('\n'));
                amt=temp.substring(temp.indexOf('\n')+1,temp.length());
                j=0;
                add="";
                while(j<check.length())
                {
                    if(check.charAt(j)!=' ')
                        add+=check.charAt(j);
                    j++;
                }
                if(add.length()>10)
                    add=add.substring(add.length()-10,add.length());
                String n=(name.substring(0,1).toUpperCase())+(name.substring(1,name.length()).toLowerCase());
                if(amt.equals("!"))
                {
                    send=false;
                    break;
                }
                Message messagedb = new Message(n,
                        add,
                        Integer.parseInt(amt),
                        e3.getText().toString(),
                        formattedDate,
                        0,
                        "!");
                dbHandler.addMessage(messagedb);
                Student student = new Student(n,
                        add,
                        (Integer.parseInt(amt)),
                        "!");
                studentDBHandler.addStudent(student);
                message+=add+"-"+amt;
            }//for ends
            message=message+"*"+e3.getText().toString();
            if(isConnected())
            {
                if (!send)
                    Toast.makeText(this,"Request failed, enter all the fields",Toast.LENGTH_LONG).show();
                else {
                    try {
                        //send message through data
                        final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(TELEPHONY_SERVICE);
                        final String tmDevice;
                        tmDevice = "" + tm.getDeviceId();
                        message = tmDevice + "*" + message;
                        //see server url
                        new HttpAsyncTask().execute("http://advanced.shifoo.in/payment/process-data", message);
                        Toast.makeText(this, "Data sending...", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(this,
                                "Data failed, please try again.",
                                Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }//inner else ends
            }//if ends
            else
            {
                Toast.makeText(this,"Please check your Internet connection",Toast.LENGTH_SHORT).show();
            }

        }//else ends
        //cleaning view
        e3.setText("");
        names.setText("");
        textView.setText("");
        listAdapter.clear();
        //starting mainActivity
        intent=new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void cancelClick(View v)
    {
        e3.setText("");
        names.setText("");
        textView.setText("");
        tv.setText("");
        listAdapter.clear();
        intent=new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position,
                               long arg3) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        // TODO Auto-generated method stub
        // Get Array index value for selected name
        if(flag==true)
        {
            lv.setVisibility(View.GONE);
            flag=false;
        }
        int i = nameValueArr.indexOf(""+arg0.getItemAtPosition(arg2));
        // If name exist in name ArrayList
        if (i >= 0) {
            // Get Phone Number
            name=nameValueArr.get(i);
            toNumberValue = phoneValueArr.get(i);
            toAmountValue=amountValueArr.get(i);
            if(lv.getCount()==0)
            {
                names.setText(name+"-"+toAmountValue);
                mArrayList.add(name + "\n" + toNumberValue + "\n" + toAmountValue);
                listAdapter.notifyDataSetChanged();
                tv.setText("1 selected");
                textView.setText("");
            }
            else
            {
                String t=names.getText().toString();
                t+=", "+name+"-"+toAmountValue;
                names.setText(t);
                mArrayList.add(name + "\n" + toNumberValue + "\n" + toAmountValue);
                listAdapter.notifyDataSetChanged();
                tv.setText(lv.getCount() + " selected");
                textView.setText("");
            }
        }

    }

    public void buttonClick(View v) {
        if(flag==true)
        {
            lv.setVisibility(View.GONE);
            flag=false;
        }
        //checking if anything at all has been added
        if(textView.getText().toString().length()!=0) {
            toNumberValue = textView.getText().toString();
            toAmountValue="!";
            name = "Unknown";
            textView.setText("");
            if (lv.getCount() == 0) {
                names.setText(toNumberValue+"-Add Fees");
                mArrayList.add(name + "\n" + toNumberValue + "\n" + toAmountValue);
                Log.v("tag",name + "\n" + toNumberValue + "\n" + toAmountValue);
                listAdapter.notifyDataSetChanged();
                tv.setText("1 selected");
                textView.setText("");
            }
            else
            {
                String t = names.getText().toString();
                t += ", " + toNumberValue + "-Add Fees";
                names.setText(t);
                mArrayList.add(name + "\n" + toNumberValue + "\n" + toAmountValue);
                listAdapter.notifyDataSetChanged();
                tv.setText(lv.getCount() + " selected");
                textView.setText("");
            }

        }
    }

    public void showList(View v)
    {
        if(flag==false) {
            lv.setVisibility(View.VISIBLE);
            flag=true;
        }
        else
        {
            lv.setVisibility(View.GONE);
            flag=false;
        }
    }
    protected void onResume() {
        super.onResume();
    }

    protected void onDestroy() {
        super.onDestroy();
    }

}
