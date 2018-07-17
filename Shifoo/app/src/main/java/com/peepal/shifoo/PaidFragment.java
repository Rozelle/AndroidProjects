package com.peepal.shifoo;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class PaidFragment extends Fragment{

    ListView lv;
    TextView tv;
    MyDBHandler dbHandler;
    ArrayList<String> array_list;
    FragmentListAdapter listAdapter;
    String phone;
    String[] items;

    public PaidFragment(){
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHandler = new MyDBHandler(getActivity(), null, null, 1);
        array_list = dbHandler.getAll1Messages();
        items=new String[]{"Call","Delete"};
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_paid, container, false);
        lv = (ListView) v.findViewById(R.id.Paid);
        tv = (TextView) v.findViewById(R.id.emptyP);
        listAdapter = new FragmentListAdapter(getActivity(), array_list,0);
        lv.setAdapter(listAdapter);
        if (lv.getCount() == 0)
            tv.setVisibility(View.VISIBLE);
        else
            tv.setVisibility(View.GONE);
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                String delete = array_list.get(position);
                delete = delete.substring(delete.indexOf('|') + 2, delete.length());
                phone = delete.substring(0, delete.indexOf('|'));

                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 1://delete
                                boolean flag = dbHandler.deleteMessage(phone);
                                array_list.remove(position);
                                listAdapter.notifyDataSetChanged();
                                if (flag == false) {
                                    Toast.makeText(getActivity(), "Unable to delete item", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case 0://call
                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setData(Uri.parse("tel:" + phone));
                                getActivity().startActivity(callIntent);
                            default:
                                break;
                        }//switch ends
                    }
                });
                builder.create();
                builder.show();
                return true;
            }
        });
        return v;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }
    @Override
    public void onDetach() {
        super.onDetach();
    }

    /*public void onRefresh() {
        array_list=dbHandler.getAll1Messages();
        listAdapter.notifyDataSetChanged();
    }*/
}
