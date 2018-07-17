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
 * Created by User on 30-07-2015.
 */
public class PendingSelectListadapter extends ArrayAdapter<ModelP> {
    private final List<ModelP> list;
    private final Activity context;
    public PendingSelectListadapter(Activity context, List<ModelP> list) {
        super(context, R.layout.pending_select,list );
        this.context = context;
        this.list = list;
    }

    static class ViewHolder {
        protected TextView textN;
        protected TextView textP;
        protected TextView textA;
        protected TextView textD;
        protected TextView textT;
        protected CheckBox checkbox;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            view = inflator.inflate(R.layout.pending_select, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.textN = (TextView) view.findViewById(R.id.textN);
            viewHolder.textP = (TextView) view.findViewById(R.id.textPh);
            viewHolder.textA = (TextView) view.findViewById(R.id.textTot);
            viewHolder.textD = (TextView) view.findViewById(R.id.textDate);
            viewHolder.textT = (TextView) view.findViewById(R.id.textTxnid);
            viewHolder.checkbox = (CheckBox) view.findViewById(R.id.check);
            viewHolder.checkbox
                    .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            ModelP element = (ModelP) viewHolder.checkbox
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
        holder.textD.setText(list.get(position).getDate());
        if(list.get(position).getTxnid().equals("!"))
            holder.textT.setVisibility(View.GONE);
        else
        {
            if (list.get(position).getTxnid().equals("Paid"))
                holder.textT.setText(list.get(position).getTxnid());
            else
                holder.textT.setText("Txnid: "+list.get(position).getTxnid());
        }

        holder.checkbox.setChecked(list.get(position).isSelected());
        return view;
    }
}

