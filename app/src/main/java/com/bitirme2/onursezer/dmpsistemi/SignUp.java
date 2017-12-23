package com.bitirme2.onursezer.dmpsistemi;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    private Button btnSignup;
    private TextView btnLogin,btnForgotPass;
    private EditText inputEmail,inputPass, inputName, inputSurname;
    private LinearLayout activity_sign_up;
    private FirebaseAuth auth;
    private FirebaseDatabase db;
    private Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //View
        btnSignup = (Button)findViewById(R.id.btnSignupRegister);
        btnLogin = (TextView)findViewById(R.id.btnSignupLogin);
        btnForgotPass = (TextView)findViewById(R.id.btnSignupForgotPass);
        inputEmail = (EditText)findViewById(R.id.txtSignupEmail);
        inputPass = (EditText)findViewById(R.id.txtSignupPassword);
        inputName = (EditText)findViewById(R.id.txtSignupName);
        inputSurname = (EditText)findViewById(R.id.txtSignupSurname);
        activity_sign_up = (LinearLayout) findViewById(R.id.activity_sign_up);

        btnSignup.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnForgotPass.setOnClickListener(this);

        //Init Firebase
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
    }

    @Override
    public void onClick(View view) {
            if(view.getId() == R.id.btnSignupLogin){
                startActivity(new Intent(SignUp.this,MainActivity.class));
                finish();
            }
        else if(view.getId() == R.id.btnSignupForgotPass){
                startActivity(new Intent(SignUp.this,ForgotPassword.class));
                finish();
            }
            else if(view.getId() == R.id.btnSignupRegister){
              signUpUser(inputEmail.getText().toString(),inputPass.getText().toString());
            }
    }

    private void signUpUser(String email, String password) {
        if(auth == null) {
            System.out.println("ONUR : GIRMEDI");
        }
        else {
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                snackbar = Snackbar.make(activity_sign_up, "Error: " + task.getException(), Snackbar.LENGTH_SHORT);
                                snackbar.show();
                            } else {

                                // Kullanıcı Ekle
                                DatabaseReference dbRef = db.getReference("Users");
                                String key = dbRef.push().getKey();
                                DatabaseReference dbRef2 = db.getReference("Users/" + key);
                                dbRef2.setValue(new User(inputName.getText().toString(), inputSurname.getText().toString(),
                                        inputEmail.getText().toString()));


                                snackbar = Snackbar.make(activity_sign_up, "Kayıt Başarılı! ", Snackbar.LENGTH_SHORT);
                                snackbar.show();
                            }
                        }
                    });
        }
    }
}
