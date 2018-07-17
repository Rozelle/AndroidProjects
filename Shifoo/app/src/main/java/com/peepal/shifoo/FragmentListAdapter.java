package com.peepal.shifoo;

import android.content.Context;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by User on 01-07-2015.
 */
public class FragmentListAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final ArrayList<String> values;
    private final int show;
    MyDBHandler dbHandler;
    Message m;

    public FragmentListAdapter(Context context, ArrayList<String> values, int show) {
        super(context, R.layout.pending_paid, values);
        this.context = context;
        this.values = values;
        this.show=show;
        dbHandler=new MyDBHandler(context ,null, null, 1);
        m=new Message();
        }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.pending_paid, parent, false);

        TextView t1=(TextView)rowView.findViewById(R.id.textN);
        TextView t2=(TextView)rowView.findViewById(R.id.textPh);
        TextView t3=(TextView)rowView.findViewById(R.id.textTot);
        TextView t4=(TextView)rowView.findViewById(R.id.textDate);
        TextView t5=(TextView)rowView.findViewById(R.id.textTxnid);
        int i,len;
        String a;
        String b;
        final String phone;
        final String amt;
        final String remarks;

        len=values.get(position).length();
        i=values.get(position).indexOf('|');
        a=values.get(position).substring(0, i);
        b=values.get(position).substring(i + 2, len);
        t1.setText(a);

        len=b.length();
        i=b.indexOf('|');
        a=b.substring(0, i);
        phone=a;
        b=b.substring(i + 2, len);
        t2.setText("Ph: "+a);

        len=b.length();
        i=b.indexOf('|');
        a=b.substring(0, i);
        amt=a;
        b=b.substring(i + 2, len);
        t3.setText("Fees: "+a);

        len=b.length();
        //for Student tab search
        if(!b.contains("|")) {
            if(b.equals("!"))
                t4.setText("");
            else
                t4.setText(b);
            remarks = "";
            t5.setText("");
        }
        //for other 2 tabs
        else
        {
            i = b.indexOf('|');
            a = b.substring(0, i);
            b = b.substring(i + 2, len);
            t4.setText(a);

            len = b.length();
            i = b.indexOf('|');
            a = b.substring(0, i);
            if (a == "!")
                remarks = "";
            else
                remarks = a;
            b = b.substring(i + 2, len);
            if (!b.equals("!")) {
                if (!b.equals("Paid"))
                    t5.setText("TXN ID: " + b);
                else
                    t5.setText(b);
            }
        }

        Button b2=(Button)rowView.findViewById(R.id.buttonResend);
        if(show==0)
            b2.setVisibility(View.GONE);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SmsManager smsManager = SmsManager.getDefault();
                String message = "remind "+phone + "*" + amt + "*" + remarks;
                try {
                    smsManager.sendTextMessage("9663536784", null, message, null, null);
                    Toast.makeText(context, "SMS sent.", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(context, "SMS failed, please try again.", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");
                String formattedDate = df.format(c.getTime());
                m = dbHandler.findMessage(phone);
                m.setDate(formattedDate);
                dbHandler.addMessage(m);
                Student s=new Student(m.getName(),m.getPhone(),m.getAmount(),"!");
                StudentDBHandler studentDBHandler=new StudentDBHandler(context,null,null,1);
                studentDBHandler.addStudent(s);
            }
        });
        return rowView;
    }
}

