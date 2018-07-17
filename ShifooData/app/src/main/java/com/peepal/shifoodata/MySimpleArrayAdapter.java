package com.peepal.shifoodata;

/**
 * Created by User on 23-07-2015.
 */
import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MySimpleArrayAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final Activity activity;
    private final ArrayList<String> values;
    private final TextView tv,names;
    AlertDialog alertDialog;
    View view;
    TextView textView1;
    String amt;
    StudentDBHandler studentDBHandler;

    public MySimpleArrayAdapter(Context context, ArrayList<String> values,TextView tv,TextView names,Activity activity) {
        super(context, R.layout.listlayout, values);
        this.context = context;
        this.values = values;
        this.tv=tv;
        this.names=names;
        this.activity=activity;
        studentDBHandler=new StudentDBHandler(context,null,null,1);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.listlayout, parent, false);


        textView1 = (TextView) rowView.findViewById(R.id.textName);
        final TextView textView2 = (TextView) rowView.findViewById(R.id.textNumber);
        final TextView textView3 = (TextView) rowView.findViewById(R.id.textAmount);
        final TextView textView=(TextView)rowView.findViewById(R.id.textChange);
        int i=values.get(position).indexOf('\n');
        textView1.setText(values.get(position).substring(0, i));
        String t=(values.get(position).substring(i + 1, values.get(position).length()));
        i=t.indexOf('\n');
        textView2.setText(t.substring(0,i));
        t=t.substring(i+1,t.length());
        if(t.equals("!")) {
            textView3.setText("");
            textView.setText("( Add )");
        }
        else
            textView3.setText(t);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                // Get the layout inflater
                LayoutInflater inflater = activity.getLayoutInflater();
                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                view = inflater.inflate(R.layout.dialog_fees, null);
                builder.setView(view);
                alertDialog = builder.create();
                TextView tv = (TextView) view.findViewById(R.id.tv);
                tv.setText("Change fees for " + textView1.getText().toString() + "?");
                Button done = (Button) view.findViewById(R.id.done);
                done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText e = (EditText) view.findViewById(R.id.fees);
                        amt = e.getText().toString();
                        if (amt.length() == 0) {
                            Toast.makeText(context, "Enter fees!", Toast.LENGTH_SHORT);
                            return;
                        } else {
                            textView3.setText(amt);
                            int j = 0;
                            //Correcting phone format
                            String add = "";
                            String check = textView2.getText().toString();
                            while (j < check.length()) {
                                if (check.charAt(j) != ' ')
                                    add += check.charAt(j);
                                j++;
                            }
                            if (add.length() > 10)
                                add = add.substring(add.length() - 10, add.length());
                            //Correcting student's fees in Student tab
                            Student s = studentDBHandler.findStudent(add);
                            if (s != null) {
                                s.setAmount(Integer.parseInt(amt));
                                studentDBHandler.deleteStudent(add);
                                studentDBHandler.addStudent(s);
                            }
                            //change text "Add" to "Edit"
                            textView.setText("( Edit )");
                        }//else ends
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

        Button b=(Button)rowView.findViewById(R.id.delButton);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str,name,no;
                str=values.get(position);
                name=str.substring(0, str.indexOf('\n'));
                String c;
                c=str.substring(str.indexOf('\n')+1,str.length());
                no=c.substring(0,c.indexOf('\n'));
                String text=names.getText().toString();
                String check,add="";
                while(text.contains(","))
                {
                    c=text.substring(0,text.indexOf(','));
                    check=c.substring(0,c.indexOf('-'));
                    if(!check.equals(name)&&!check.equals(no))
                        add+=check+", ";
                    text=text.substring(text.indexOf(',')+2,text.length());
                }
                text=text.substring(0,text.indexOf('-'));
                if(!text.equals(name)&&!text.equals(no))
                    add+=text;
                else
                    if (add.length() > 0)
                        add = add.substring(0, add.length() - 2);
                names.setText(add);
                values.remove(position);
                notifyDataSetChanged();
                tv.setText(values.size()+" selected");
            }
        });

        return rowView;
    }
}
