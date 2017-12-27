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
    private String classID, status,user;
    private TextView txtID;
    private FirebaseDatabase db;
    private ListView mListView;
    private TextView text;
    private String date;
    private String time;
    private User userBean;
    private String random;

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

    public static Tab2 newInstance( String id, String status,String user) {
        Tab2 result = new Tab2();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putString("status", status);
        bundle.putString("user", user);
        result.setArguments(bundle);
        return result;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        classID =  bundle.getString("id");
        status =  bundle.getString("status");
        user = bundle.getString("user");
        fCon = getContext();
    }

    @Override
    public void onActivityCreated(final Bundle state) {
        super.onActivityCreated(state);

        db = FirebaseDatabase.getInstance();
        mListView  = (ListView) getView().findViewById(R.id.listviewHW);
        Gson gS = new Gson();
        userBean = gS.fromJson(user, User.class);
        System.out.println("status : " + status);
        DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference();
        Query applesQuery2 = ref2.child("Map3").orderByChild("classId").equalTo(classID);
        applesQuery2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot tasksSnapshot) {

                final List<String> list = new ArrayList<String>();
                final List<String> list2 = new ArrayList<String>();
                final List<Homework> list3 = new ArrayList<Homework>();
                final List<String> list4 = new ArrayList<String>();

                for (DataSnapshot snapshot: tasksSnapshot.getChildren()) {
                    List<Homework> l = snapshot.getValue(MapClassAndHomework.class).getList();
                    if(l != null)
                    {
                        for (Homework data : l) {
                            list.add( data.getNameOfHW() );
                            list2.add( data.getDeliveryDate() + "  " + data.getDeliveryTime() );
                            list3.add(data);
                            if(status.equals("1"))
                            {

                                DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference();
                                Query applesQuery2 = ref2.child("Map4").orderByChild("hwID").equalTo(data.getHwId());
                                applesQuery2.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot tasksSnapshot) {
                                        int st = -1;
                                        for (DataSnapshot snapshot: tasksSnapshot.getChildren()) {
                                            List<HomeworkInfo> l = snapshot.getValue(MapHomeworkAndStudent.class).getList();
                                            if(l == null)
                                            {
                                                list4.add("");
                                            }
                                            else{
                                                for (int i = 0; i < l.size() ; i++) {
                                                    if(l.get(i).getStudentInfo().getEmail().equals(userBean.getEmail()))
                                                    {
                                                        list4.add(l.get(i).getScore());
                                                        st = 1;
                                                        break;
                                                    }
                                                }
                                            }

                                        }
                                        if(st == -1)
                                            list4.add("");
                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                                ref2.onDisconnect();

                            }
                        }

                        String[] names = new String[list.size()];
                        String[] date = new String[list.size()];
                        String[] scores = new String[list.size()];
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
                        for (int i = 0; i < list4.size(); i++) {
                            scores[i] = list4.get(i);
                        }
                        if(status.equals("0")){
                            MyAdapter myAdapter = new MyAdapter(fCon, names, date, icons);
                            mListView.setAdapter(myAdapter);
                        }
                        else{
                            System.out.println("list4.size()" + list4.size());
                            if(list4.size() > 0)
                                for (int i = 0; i < list4.size(); i++) {
                                    System.out.println(list4.get(i));
                                    System.out.println("scores[i] : " + scores[i]);
                                }
                            System.out.println("öğrenci formatı");
                            MyAdapter2 myAdapter2 = new MyAdapter2(fCon, names, date, icons, scores);
                            mListView.setAdapter(myAdapter2);
                        }

                        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            System.out.println("position : " + position);

                            Gson gS = new Gson();
                            String hwString = gS.toJson(list3.get(position));
                            System.out.println("tab2 : " + hwString);

                            if(status.equals("0"))
                            {
                                Intent intent = new Intent(fCon, HomeworkScreen.class);
                                intent.putExtra("HW", hwString);
                                intent.putExtra("CLASSID", classID);
                                startActivity(intent);
                            }
                            else
                            {
                                Intent intent = new Intent(fCon, HomeworkScreenStudent.class);
                                intent.putExtra("HW", hwString);
                                intent.putExtra("CLASSID", classID);
                                intent.putExtra("USER", user);
                                startActivity(intent);
                            }

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
        if(status.equals("0")) {
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
                            if (!txtNameOfHW.getText().toString().isEmpty()) {

                                // sinifa odev ekle
                                DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference();
                                Query applesQuery2 = ref2.child("Map3").orderByChild("classId").equalTo(classID);
                                applesQuery2.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot tasksSnapshot) {
                                        for (DataSnapshot snapshot : tasksSnapshot.getChildren()) {
                                            List<Homework> l = snapshot.getValue(MapClassAndHomework.class).getList();
                                            if (l == null) {
                                                System.out.println("gıncelle - null");
                                                l = new ArrayList<Homework>();
                                            }
                                            random = getSaltString();
                                            l.add(new Homework(txtNameOfHW.getText().toString(), date, time, "odev" + random));

                                            snapshot.getRef().child("list").setValue(l);


                                            // odev ile ogrenci eslestir
                                            DatabaseReference dbRef3 = db.getReference("Map4");
                                            String key2 = dbRef3.push().getKey();
                                            DatabaseReference dbRef4 = db.getReference("Map4/"+ key2);
                                            dbRef4.setValue(new MapHomeworkAndStudent( "odev" + random ));
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                                ref2.onDisconnect();


                                Toast.makeText(fCon, "Ödev Oluşturuldu!", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            } else {
                                Toast.makeText(fCon, "Lütfen Boş Alan Bırakmayın!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
        }
        else{
            fab.setVisibility(View.INVISIBLE);
        }

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
