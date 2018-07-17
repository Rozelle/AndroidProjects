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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class StudentFragment extends Fragment {

    ListView lv;
    TextView tv;
    Button button;
    StudentDBHandler dbHandler;
    ArrayList<String> array_list;
    StudentListAdapter listAdapter;
    String phone;
    String[] items;

    public StudentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHandler = new StudentDBHandler(getActivity(), null, null, 1);
        array_list = dbHandler.getAllStudents();
        items = new String[]{"Call","Edit Details", "Delete"};
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_student, container, false);


        button = (Button) v.findViewById(R.id.add);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getActivity(),AddStudent.class);
                startActivity(i);
            }
        });
        lv = (ListView) v.findViewById(R.id.StudentL);
        tv = (TextView) v.findViewById(R.id.nolist);
        listAdapter = new StudentListAdapter(getActivity(), array_list,getActivity());
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
                    public void onClick(final DialogInterface dialog, int which) {
                        switch (which) {
                            case 0://call
                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setData(Uri.parse("tel:" + phone));
                                getActivity().startActivity(callIntent);
                                break;
                            case 1://edit details
                                Intent i=new Intent(getActivity(),Edit.class);
                                i.putExtra("phone",phone);
                                startActivity(i);
                                getActivity().finish();
                                break;
                            case 2://delete
                                boolean flag = dbHandler.deleteStudent(phone);
                                array_list.remove(position);
                                listAdapter.notifyDataSetChanged();
                                if (flag == false) {
                                    Toast.makeText(getActivity(), "Unable to delete item", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            default:break;
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
}
