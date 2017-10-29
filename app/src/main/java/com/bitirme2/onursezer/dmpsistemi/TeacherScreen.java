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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

public class TeacherScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Sınıflar");
        toolbar.setBackgroundColor(Color.parseColor("#00897B"));

        Gson gS = new Gson();
        String target = getIntent().getStringExtra("USER");
        User user = gS.fromJson(target, User.class);

        System.out.println("deneme : " + user);

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
/*                Button btnClassCreate = (Button) mView.findViewById(R.id.btnCreateClass);

                btnClassCreate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                            if(!className.getText().toString().isEmpty() && !classBranchName.getText().toString().isEmpty())
                            {
                                Toast.makeText(TeacherScreen.this, "Sınıf Oluşturuldu", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(TeacherScreen.this, "Lütfen Boş Alan Bırakmayın!", Toast.LENGTH_SHORT).show();
                            }
                    }
                }

                );*/
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

                            // sınıf oluştur


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

}
