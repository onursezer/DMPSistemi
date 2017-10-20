package com.bitirme2.onursezer.dmpsistemi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class second extends AppCompatActivity implements View.OnClickListener{

    private EditText name, surname, age;
    private Button addUser, showUSers;
    private TextView list;
    FirebaseDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        initial();
    }

    private  void initial(){

        db = FirebaseDatabase.getInstance();
        name = (EditText) findViewById(R.id.txtName);
        surname = (EditText) findViewById(R.id.txtSurname);
        age = (EditText) findViewById(R.id.txtAge);
        addUser = (Button) findViewById(R.id.btnAddUser);
        showUSers = (Button) findViewById(R.id.btnShowUsers);
        list = (TextView) findViewById(R.id.txtShowUsers);

        addUser.setOnClickListener(this);
        showUSers.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.btnAddUser:
                String n = name.getText().toString().trim();
                String s = surname.getText().toString().trim();
                String a = age.getText().toString().trim();
                addUSerToDB(new Kullanici(n, s, a));
                break;
            case R.id.btnShowUsers:
                getUsers();
                break;

        }

    }

    private void addUSerToDB(Kullanici kullanici){

        DatabaseReference dbRef = db.getReference("Kullanicilar");
        String key = dbRef.push().getKey();
        DatabaseReference dbRef2 = db.getReference("Kullanicilar/" + key);
        dbRef2.setValue(kullanici);

    }

    private void getUsers(){

        list.setText("");
        DatabaseReference dbRef = db.getReference("Kullanicilar");
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot data: dataSnapshot.getChildren()) {
                    list.append(data.getValue(Kullanici.class).getName() + "\n");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
