package com.peepal.shifoodata;

/**
 * Created by User on 23-07-2015.
 */
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
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

public class StudentListAdapter extends ArrayAdapter<String>
{
    private final Context context;
    Activity activity;
    private final ArrayList<String> values;
    private Student s;
    private StudentDBHandler dbHandler;
    Message m;
    MyDBHandler myDBHandler;
    View view;
    String remarks;
    AlertDialog alertDialog;

    public StudentListAdapter(Context context, ArrayList<String> values,Activity activity)
    {
        super(context, R.layout.student, values);
        this.context = context;
        this.values = values;
        this.activity=activity;
        dbHandler=new StudentDBHandler(context ,null, null, 1);
        myDBHandler=new MyDBHandler(context ,null, null, 1);
        s=new Student();
    }

    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
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
            Toast.makeText(context, "Data Sent!", Toast.LENGTH_LONG).show();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
       final View rowView = inflater.inflate(R.layout.student, parent, false);

        TextView t1 = (TextView) rowView.findViewById(R.id.textN);
        TextView t2 = (TextView) rowView.findViewById(R.id.textPh);
        TextView t3 = (TextView) rowView.findViewById(R.id.textTot);
        TextView t4 = (TextView) rowView.findViewById(R.id.textDate);

        int len,i;
        String a,b;
        final String name,phone,amt;
        len=values.get(position).length();

        i=values.get(position).indexOf('|');
        a=values.get(position).substring(0, i);
        b=values.get(position).substring(i + 2, len);
        name=a;
        t1.setText(a);

        len=b.length();
        i=b.indexOf('|');
        a=b.substring(0, i);
        b=b.substring(i + 2, len);
        phone=a;
        t2.setText("Ph: "+a);

        len=b.length();
        i=b.indexOf('|');
        a=b.substring(0, i);
        b=b.substring(i + 2, len);
        amt=a;
        t3.setText("Fees: "+a);
        if(b.equals("!")||b.equals("null"))
            t4.setVisibility(View.GONE);
        else t4.setText("Last received on: "+b);

        final FrameLayout holder=(FrameLayout)rowView.findViewById(R.id.holder);
        final Button flowdown=(Button)rowView.findViewById(R.id.flowdown);
        final Button flowup=(Button)rowView.findViewById(R.id.flowup);

        flowdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.setVisibility(View.VISIBLE);
                flowdown.setVisibility(View.GONE);
                flowup.setVisibility(View.VISIBLE);
            }
        });
        flowup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.setVisibility(View.GONE);
                flowdown.setVisibility(View.VISIBLE);
                flowup.setVisibility(View.GONE);
            }
        });

        final Button call=(Button)rowView.findViewById(R.id.buttonCall);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + phone));
                context.startActivity(callIntent);
            }
        });

        final Button button = (Button) rowView.findViewById(R.id.buttonResend);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                // Get the layout inflater
                LayoutInflater inflater = activity.getLayoutInflater();
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
                            Toast.makeText(context, "Enter remarks", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        //add message
                        String message = phone + "-" + amt;
                        if(isConnected())
                        {
                            try {
                                Toast.makeText(context, "Data sending...", Toast.LENGTH_LONG).show();
                                String android_id = android.provider.Settings.Secure.getString
                                        (context.getContentResolver(),
                                        android.provider.Settings.Secure.ANDROID_ID);

                                //see server url
                                new HttpAsyncTask().execute("http://advanced.shifoo.in/payment/process-data",
                                        android_id,"shifoo",message,remarks);

                                Calendar c = Calendar.getInstance();
                                SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");
                                String formattedDate = df.format(c.getTime());
                                m = myDBHandler.findMessage(phone);
                                if (m != null) {
                                    m.setDate(formattedDate);
                                    m.setPaid(0);
                                    m.setTxnid("!");
                                    m.setAmount(Integer.parseInt(amt));
                                    m.setRemarks(remarks);
                                    myDBHandler.addMessage(m);
                                } else {
                                    m = new Message(name,
                                            phone,
                                            Integer.parseInt(amt),
                                            remarks,
                                            formattedDate,
                                            0,
                                            "!");
                                    myDBHandler.addMessage(m);
                                }
                            } catch (Exception e) {
                                Toast.makeText(context, "Data failed, please try again.", Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                        }
                        else
                        {
                            Toast.makeText(context,"Please check your Internet connection",Toast.LENGTH_SHORT).show();
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
            }//on click closes
        });

        final Button view=(Button)rowView.findViewById(R.id.buttonView);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(context,Edit.class);
                i.putExtra("phone",phone);
                context.startActivity(i);
            }
        });
        return rowView;
    }


}
