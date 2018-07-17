package com.peepal.shifoodata;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import java.util.List;

/**
 * Created by User on 29-07-2015.
 */
public class StudentSelectListadapter extends ArrayAdapter<Model> {
    private final List<Model> list;
    private final Activity context;
    public StudentSelectListadapter(Activity context, List<Model> list) {
        super(context, R.layout.student_select, list);
        this.context = context;
        this.list = list;
    }

    static class ViewHolder {
        protected TextView textN;
        protected TextView textP;
        protected TextView textA;
        protected TextView textD;
        protected CheckBox checkbox;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            view = inflator.inflate(R.layout.student_select, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.textN = (TextView) view.findViewById(R.id.textN);
            viewHolder.textP = (TextView) view.findViewById(R.id.textPh);
            viewHolder.textA = (TextView) view.findViewById(R.id.textTot);
            viewHolder.textD = (TextView) view.findViewById(R.id.textDate);
            viewHolder.checkbox = (CheckBox) view.findViewById(R.id.check);
            viewHolder.checkbox
                    .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            Model element = (Model) viewHolder.checkbox
                                    .getTag();
                            element.setSelected(buttonView.isChecked());

                        }
                    });
            view.setTag(viewHolder);
            viewHolder.checkbox.setTag(list.get(position));
        } else {
            view = convertView;
            ((ViewHolder) view.getTag()).checkbox.setTag(list.get(position));
        }
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.textN.setText(list.get(position).getName());
        holder.textP.setText("Ph: "+list.get(position).getPhone());
        holder.textA.setText("Fees: "+list.get(position).getAmount());
        if(list.get(position).getDate().equals("!")||list.get(position).getDate().equals("null"))
            holder.textD.setVisibility(View.GONE);
        else
            holder.textD.setText("Last received on: "+list.get(position).getDate());
        holder.checkbox.setChecked(list.get(position).isSelected());
        return view;
    }
}

