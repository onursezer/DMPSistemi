package com.bitirme2.onursezer.dmpsistemi;

import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.bitirme2.onursezer.dmpsistemi.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

public class DashBoard extends AppCompatActivity implements View.OnClickListener {

    private TextView txtWelcome;
    private EditText input_new_password;
    private Button btnChangePass,btnLogout;
    private LinearLayout activity_dashboard;
    private Button btnDasboardTeacher, btnDasboardStudent;
    private FirebaseAuth auth;
    private  User user = new User();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        //View
        txtWelcome = (TextView)findViewById(R.id.dashboard_welcome);
        activity_dashboard = (LinearLayout)findViewById(R.id.activity_dash_board);
        btnDasboardTeacher = (Button)findViewById(R.id.btnDasboardTeacher);
        btnDasboardStudent = (Button)findViewById(R.id.btnDasboardStudent);

        btnDasboardTeacher.setOnClickListener(this);
        btnDasboardStudent.setOnClickListener(this);

        //Init Firebase
        auth = FirebaseAuth.getInstance();

        //Session check
        if(auth.getCurrentUser() != null) {
            final String[] name = {" "};
            FirebaseDatabase db = FirebaseDatabase.getInstance();
            DatabaseReference dbRef = db.getReference("Users");
            dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot data: dataSnapshot.getChildren()) {
                        if(data.getValue(User.class).getEmail().equals(auth.getCurrentUser().getEmail()))
                        {
                            name[0] = data.getValue(User.class).getName().toString();
                            user = new User(data.getValue(User.class).getName().toString(),
                                    data.getValue(User.class).getSurname().toString(),
                                    data.getValue(User.class).getEmail().toString());
                            txtWelcome.setText(name[0]);
                            break;
                        }
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) { }
            });

        }


    }

    @Override
    public void onClick(View view) {
        Gson gS = new Gson();
        String target = gS.toJson(user);
        Intent intent ;
        switch (view.getId()){
            case R.id.btnDasboardTeacher:

                intent = new Intent(getBaseContext(), TeacherScreen.class);
                intent.putExtra("USER",  target);
                startActivity(intent);
                break;
            case R.id.btnDasboardStudent:
                intent = new Intent(getBaseContext(), StudentScreen.class);
                intent.putExtra("USER",  target);
                startActivity(intent);
                break;
        }

    }

    private void logoutUser() {
        auth.signOut();
        if(auth.getCurrentUser() == null)
        {
            startActivity(new Intent(DashBoard.this,MainActivity.class));
            finish();
        }
    }

    private void changePassword(String newPassword) {
        FirebaseUser user = auth.getCurrentUser();
        user.updatePassword(newPassword).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Snackbar snackBar = Snackbar.make(activity_dashboard,"Password changed",Snackbar.LENGTH_SHORT);
                    snackBar.show();
                }
            }
        });
    }
}
