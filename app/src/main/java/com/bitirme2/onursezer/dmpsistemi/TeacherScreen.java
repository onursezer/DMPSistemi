package com.bitirme2.onursezer.dmpsistemi;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TeacherScreen extends AppCompatActivity {

    FirebaseDatabase db;
    ListView mListView;
    User teacher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseDatabase.getInstance();
        setContentView(R.layout.activity_teacher_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Sınıflar");
        toolbar.setBackgroundColor(Color.parseColor("#00897B"));
        mListView  = (ListView) findViewById(R.id.listview);


        DatabaseReference dbRef = db.getReference("Class");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<String> list = new ArrayList<String>();
                for (DataSnapshot data: dataSnapshot.getChildren()) {
                    list.add(data.getValue(ClassBean.class).getClassName());
                }
                String[] countyNames = new String[list.size()];
                Integer[] countyFlags = new Integer[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    countyNames[i] = list.get(i);
                }
                for (int i = 0; i < list.size(); i++) {
                    countyFlags[i] = R.mipmap.class_teacher_icon;
                }
                MyAdapter myAdapter = new MyAdapter(TeacherScreen.this, countyNames, countyFlags);
                mListView.setAdapter(myAdapter);
                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent mIntent = new Intent(TeacherScreen.this, ClassScreen.class);
                        startActivity(mIntent);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        Gson gS = new Gson();
        String target = getIntent().getStringExtra("USER");
        teacher = gS.fromJson(target, User.class);

        System.out.println("deneme : " + teacher);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(TeacherScreen.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_create_class, null);
                final EditText className = (EditText) mView.findViewById(R.id.txtClassName);
                final EditText classBranchName = (EditText) mView.findViewById(R.id.txtClassBranchName);

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
                        if(!className.getText().toString().isEmpty() && !classBranchName.getText().toString().isEmpty())
                        {
                            List<User> student = new ArrayList<User>();
                            student.add(new User("ali", "akyuz", "ali.akyuz@xyz.com"));
                            student.add(new User("recep", "sivri", "recep.sivir@xyz.com"));
                            student.add(new User("hasan", "bilgin", "hasan.bilgin@xyz.com"));


                            // silme
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                            Query applesQuery = ref.child("Class").orderByChild("classId").equalTo("HK546");

                            applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                                        appleSnapshot.getRef().removeValue();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    System.out.println("DELETE : CANCEL");
                                }
                            });
                            // sınıf oluştur
                            DatabaseReference dbRef = db.getReference("Class");
                            String key = dbRef.push().getKey();
                            DatabaseReference dbRef2 = db.getReference("Class/" + key);
                            dbRef2.setValue(new ClassBean(className.getText().toString(),classBranchName.getText().toString(),
                                    getSaltString(), teacher,student));

                            Toast.makeText(TeacherScreen.this, "Sınıf Oluşturuldu!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                        else{
                            Toast.makeText(TeacherScreen.this, "Lütfen Boş Alan Bırakmayın!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
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
