package com.bitirme2.onursezer.dmpsistemi;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
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

public class StudentScreen extends AppCompatActivity {

    FirebaseDatabase db;
    ListView mListView;
    User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseDatabase.getInstance();
        setContentView(R.layout.activity_student_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Dersler");
        toolbar.setBackgroundColor(Color.parseColor("#00897B"));
        mListView  = (ListView) findViewById(R.id.listview);

        Gson gS = new Gson();
        final String target = getIntent().getStringExtra("USER");
        user = gS.fromJson(target, User.class);
        String useMail = user.getEmail();

        DatabaseReference dbRef = db.getReference("Users");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<String> list = new ArrayList<String>();
                final List<String> list2 = new ArrayList<String>();
                final List<ClassBean> list3 = new ArrayList<ClassBean>();
                for (DataSnapshot data: dataSnapshot.getChildren()) {
                    List<ClassBean>  listClass = new ArrayList<ClassBean>();
                    listClass = data.getValue(User.class).getStudentClasses();
                    if (listClass != null && listClass.size() > 0) {
                        if (data.getValue(User.class).getEmail().equals(user.getEmail())) {
                            for (ClassBean cBean : data.getValue(User.class).getStudentClasses()) {
                                list.add(cBean.getClassName());
                                list2.add(cBean.getClassBranch());
                                list3.add(cBean);
                            }
                        }
                    }
                    String[] countyNames = new String[list.size()];
                    String[] branchNames = new String[list.size()];
                    Integer[] countyFlags = new Integer[list.size()];
                    for (int i = 0; i < list.size(); i++) {
                        countyNames[i] = list.get(i);
                    }
                    for (int i = 0; i < list2.size(); i++) {
                        branchNames[i] = list2.get(i);
                    }
                    for (int i = 0; i < list.size(); i++) {
                        countyFlags[i] = R.mipmap.class_teacher_icon;
                    }
                    MyAdapter myAdapter = new MyAdapter(StudentScreen.this, countyNames, branchNames, countyFlags);
                    mListView.setAdapter(myAdapter);
                    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            System.out.println("position : " + position);
                            Intent intent = new Intent(getBaseContext(), ClassScreen.class);
                            Gson gS = new Gson();
                            String classString = gS.toJson(list3.get(position));
                            String userString = gS.toJson(user);
                            intent.putExtra("CLASS", classString);
                            intent.putExtra("STATUS", "1");
                            intent.putExtra("USER", userString);  // akış isimlerini değiştirmek için
                            startActivity(intent);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(StudentScreen.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_student_register, null);
                final EditText classIdTxt = (EditText) mView.findViewById(R.id.txtClassNameStudent);

                mBuilder.setPositiveButton("Kayıt Ol", new DialogInterface.OnClickListener() {
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
                        final String[] teacherOfClass = {""};
                        if(!classIdTxt.getText().toString().isEmpty())
                        {
                          // kullanicin student kismina dersi eklenir, sinifin ogrenci kismina yeni ogrenci eklenir
                            // öğretmen ile sınıfın kodunu eşleştir boylelikle sınıfın ismini kolayca bulursun
                            System.out.println("classIdTxt.getText().toString() : " + classIdTxt.getText().toString());
                            DatabaseReference dbRef = db.getReference("Map");
                            dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    System.out.println("onDataChange : 151");
                                    for (DataSnapshot data: dataSnapshot.getChildren()) {
                                        System.out.println("asd : " + data.getValue(MapClassAndTeacher.class).getClassID());
                                        if(data.getValue(MapClassAndTeacher.class).getClassID().equals(classIdTxt.getText().toString()))
                                        {
                                            teacherOfClass[0] = data.getValue(MapClassAndTeacher.class).getTeacherMail();
                                            System.out.println(" teacherOfClass[0] : " +  teacherOfClass[0]);
                                        }
                                    }
                                    if(!teacherOfClass[0].isEmpty()){
                                        System.out.println("*** teacherOfClass[0] : " +  teacherOfClass[0]);
                                        System.out.println("GİRDİ");

                                        // sinifi bulma
                                        final ClassBean[] cBean = {null};
                                        DatabaseReference dbRef = db.getReference("Class/"+teacherOfClass[0]);
                                        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                for (DataSnapshot data: dataSnapshot.getChildren()) {
                                                    if(data.getValue(ClassBean.class).getClassId().equals(classIdTxt.getText().toString()))
                                                        cBean[0] = data.getValue(ClassBean.class);

                                                }

                                                List<ClassBean> lClass = new ArrayList<ClassBean>();
                                                lClass = user.getStudentClasses();
                                                lClass.add(cBean[0]);
                                                System.out.println("burda : " + cBean[0].getClassId() + " " + cBean[0].getTeacher() + " " + cBean[0].getClassBranch()) ;
                                                user.setStudentClasses(lClass);
                                                User tempUser = user;

                                                // silme
                                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                                                Query applesQuery = ref.child("Users").orderByChild("email").equalTo(user.getEmail());
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

                                                // ekle

                                                DatabaseReference dbRef = db.getReference("Users");
                                                String key = dbRef.push().getKey();
                                                DatabaseReference dbRef2 = db.getReference("Users/" + key);
                                                dbRef2.setValue(tempUser);

                                                Toast.makeText(StudentScreen.this, "Sınıf Eklendi!", Toast.LENGTH_SHORT).show();

                                            }
                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });


                                    }
                                    else
                                    {
                                        Toast.makeText(StudentScreen.this, "Girilen Sınıf Kodu Bulunamadı!", Toast.LENGTH_SHORT).show();
                                    }

                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                        else{
                            Toast.makeText(StudentScreen.this, "Lütfen Boş Alan Bırakmayın!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
