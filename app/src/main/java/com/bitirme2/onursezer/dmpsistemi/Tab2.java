package com.bitirme2.onursezer.dmpsistemi;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;


/**
 * Created by OnurSezer on 20.11.2017.
 */

public class Tab2 extends Fragment {
    private Context fCon;
    private String classID;
    private TextView txtID;
    FirebaseDatabase db;
    ListView mListView;
    TextView text;
    private String date;
    private String time;

    DateFormat formatDateTime = DateFormat.getDateTimeInstance();
    Calendar dateTime = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateTime.set(Calendar.YEAR, year);
            dateTime.set(Calendar.MONTH, monthOfYear);
            dateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateTextLabel();
        }
    };

    TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            dateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            dateTime.set(Calendar.MINUTE, minute);
            updateTextLabel();
        }
    };

    public static Tab2 newInstance( String id) {
        Tab2 result = new Tab2();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        result.setArguments(bundle);
        return result;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        classID =  bundle.getString("id");
        fCon = getContext();
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);

        db = FirebaseDatabase.getInstance();
        mListView  = (ListView) getView().findViewById(R.id.listviewHW);

        DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference();
        Query applesQuery2 = ref2.child("Map3").orderByChild("classId").equalTo(classID);
        applesQuery2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot tasksSnapshot) {

                final List<String> list = new ArrayList<String>();
                final List<String> list2 = new ArrayList<String>();
                final List<Homework> list3 = new ArrayList<Homework>();

                for (DataSnapshot snapshot: tasksSnapshot.getChildren()) {
                    List<Homework> l = snapshot.getValue(MapClassAndHomework.class).getList();
                    if(l != null)
                    {
                        for (Homework data : l) {
                            list.add( data.getNameOfHW() );
                            list2.add( data.getDeliveryDate() + "  " + data.getDeliveryTime() );
                            list3.add(data);
                        }

                        String[] names = new String[list.size()];
                        String[] date = new String[list.size()];
                        Integer[] icons = new Integer[list.size()];
                        for (int i = 0; i < list.size(); i++) {
                            names[i] = list.get(i);
                        }
                        for (int i = 0; i < list2.size(); i++) {
                            date[i] = list2.get(i);
                        }
                        for (int i = 0; i < list.size(); i++) {
                            icons[i] = R.mipmap.icon_hw;
                        }
                        MyAdapter myAdapter = new MyAdapter(fCon, names,date, icons);
                        mListView.setAdapter(myAdapter);
                        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                System.out.println("position : " + position);
                                Intent intent = new Intent(fCon, HomeworkScreen.class);
                                Gson gS = new Gson();
                                String hwString = gS.toJson(list3.get(position));
                                System.out.println("tab2 : " + hwString);
                                intent.putExtra("HW", hwString);
                                intent.putExtra("CLASSID", classID);
                                startActivity(intent);
                            }
                        });
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        ref2.onDisconnect();


        FloatingActionButton fab = (FloatingActionButton) getView().findViewById(R.id.fab3);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(fCon);
                View mView = getLayoutInflater().inflate(R.layout.dialog_create_hw, null);
                final EditText txtNameOfHW = (EditText) mView.findViewById(R.id.txtNameOfHW);
                text = (TextView) mView.findViewById(R.id.textView8);
                final Button btnSetDate = (Button) mView.findViewById(R.id.btnSetDate);
                final Button btnSetTime = (Button) mView.findViewById(R.id.btnSetTime);

                btnSetDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateDate();
                    }
                });

                btnSetTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateTime();
                    }
                });

                mBuilder.setPositiveButton("Oluştur", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                mBuilder.setNegativeButton("Kapat", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#00897B"));
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#00897B"));
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!txtNameOfHW.getText().toString().isEmpty())
                        {

                            // sinifa odev ekle
                            DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference();
                            Query applesQuery2 = ref2.child("Map3").orderByChild("classId").equalTo(classID);
                            applesQuery2.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot tasksSnapshot) {
                                    for (DataSnapshot snapshot: tasksSnapshot.getChildren()) {
                                        List<Homework> l = snapshot.getValue(MapClassAndHomework.class).getList();
                                        if(l == null)
                                        {
                                            System.out.println("gıncelle - null");
                                            l = new ArrayList<Homework>();
                                        }
                                        l.add(new Homework(txtNameOfHW.getText().toString(), date, time, "odev" + getSaltString()));

                                        snapshot.getRef().child("list").setValue(l);
                                    }
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            ref2.onDisconnect();

                            Toast.makeText(fCon, "Ödev Oluşturuldu!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                        else{
                            Toast.makeText(fCon, "Lütfen Boş Alan Bırakmayın!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab2, container, false);
    }

    private void updateTextLabel(){
        //text.setText(formatDateTime.format(dateTime.getTime()));
        date =  dateTime.get(Calendar.DAY_OF_MONTH) + "/" + (dateTime.get(Calendar.MONTH)+1) + "/" + dateTime.get(Calendar.YEAR);
        time =  dateTime.get(Calendar.HOUR_OF_DAY) + ":" + dateTime.get(Calendar.MINUTE);
    }
    private void updateDate(){
        new DatePickerDialog(fCon, d, dateTime.get(Calendar.YEAR),dateTime.get(Calendar.MONTH),dateTime.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void updateTime(){
        new TimePickerDialog(fCon, t, dateTime.get(Calendar.HOUR_OF_DAY), dateTime.get(Calendar.MINUTE), true).show();
    }

    private  String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 5) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }

}
