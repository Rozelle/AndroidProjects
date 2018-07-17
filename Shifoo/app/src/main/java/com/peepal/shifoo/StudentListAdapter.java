package com.peepal.shifoo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by User on 17-07-2015.
 */
public class StudentListAdapter extends ArrayAdapter<String>
{
    private final Context context;
    Activity activity;
    private final ArrayList<String> values;
    View view;

    public StudentListAdapter(Context context, ArrayList<String> values,Activity activity)
    {
        super(context, R.layout.student, values);
        this.context = context;
        this.values = values;
        this.activity=activity;
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
        final String phone;
        len=values.get(position).length();

        i=values.get(position).indexOf('|');
        a=values.get(position).substring(0, i);
        b=values.get(position).substring(i + 2, len);
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
        t3.setText("Fees: " + a);
        if(b.equals("!")||b.equals("null"))
            t4.setVisibility(View.GONE);
        else t4.setText("Last received on: "+b);

        final Button button = (Button) rowView.findViewById(R.id.buttonResend);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(context,RequestPayment.class);
                i.putExtra("phone",phone);
                context.startActivity(i);
            }//on click closes
        });

        return rowView;
    }


}
