package com.peepal.shifoodata;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class PendingFragment extends Fragment{

    ListView lv;
    TextView tv;
    MyDBHandler dbHandler;
    StudentDBHandler studentDBHandler;
    ArrayList<String> array_list;
    FragmentListAdapter listAdapter;
    //String[] items;
    String phone;

    /*Refresh activitycallback;
    public interface Refresh
    {
        public void onRefresh();
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbHandler = new MyDBHandler(getActivity(), null, null, 1);
        studentDBHandler= new StudentDBHandler(getActivity(),null,null,1);
        array_list = dbHandler.getAll0Messages();
        //items=new String[]{"Move to Paid","Delete"};
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_pending, container, false);

        lv=(ListView)v.findViewById(R.id.pendingPaid);
        tv=(TextView)v.findViewById(R.id.empty);
        listAdapter = new FragmentListAdapter(getActivity(), array_list,1);
        lv.setAdapter(listAdapter);
        if(lv.getCount()==0)
            tv.setVisibility(View.VISIBLE);
        else
            tv.setVisibility(View.GONE);

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                Intent intent=new Intent(getActivity(),PendingSelect.class);
                intent.putExtra("select",position);
                intent.putExtra("tab","Pending");
                intent.putExtra("list",array_list);
                startActivity(intent);

                /*String delete = array_list.get(position);
                delete = delete.substring(delete.indexOf('|') + 2, delete.length());
                phone = delete.substring(0, delete.indexOf('|'));

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which)
                        {
                            case 0://paid
                                Calendar c = Calendar.getInstance();
                                SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");
                                String formattedDate = df.format(c.getTime());

                                Message m=dbHandler.findMessage(phone);
                                m.setTxnid("Paid");
                                m.setPaid(1);
                                m.setDate(formattedDate);
                                dbHandler.addMessage(m);

                                Student s=studentDBHandler.findStudent(phone);
                                if(s!=null) {
                                    s.setDate(formattedDate);
                                    studentDBHandler.deleteStudent(phone);
                                    studentDBHandler.addStudent(s);
                                }
                                else
                                {
                                    s=new Student(m.getName(),phone,m.getAmount(),formattedDate);
                                    studentDBHandler.addStudent(s);
                                }

                                array_list.remove(position);
                                listAdapter.notifyDataSetChanged();
                                //activitycallback.onRefresh();
                                break;
                            case 1://delete
                                boolean flag = dbHandler.deleteMessage(phone);
                                array_list.remove(position);
                                listAdapter.notifyDataSetChanged();
                                if (flag == false) {
                                    Toast.makeText(getActivity(), "Unable to delete item", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            default:break;
                        }//switch ends
                    }//onClick ends
                }//builder ends
                );
                builder.create();
                builder.show();*/
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
}
