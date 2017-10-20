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

import org.w3c.dom.Text;

import java.util.Iterator;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener{


    private EditText yapilacak;
    private Button ekle, kullaniciKayit, gorBtn;
    private TextView gor;
    FirebaseDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        baslangic();
    }

    private void baslangic(){
        db = FirebaseDatabase.getInstance();
        yapilacak = (EditText) findViewById(R.id.txtYapilacak);
        ekle = (Button) findViewById(R.id.btnEkle);
        gorBtn = (Button) findViewById(R.id.btnGor);
        gor = (TextView) findViewById(R.id.txtView);

        ekle.setOnClickListener(this);
        gorBtn.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.btnEkle:
                String todo = yapilacak.getText().toString().trim();
                yapilacak(todo);
                break;
            case R.id.btnGor:
                yapilacaklariGoster();
        }
    }

    private void yapilacak (String todo){

        DatabaseReference dbRef = db.getReference("yapilacaklar");
        String key = dbRef.push().getKey();
        DatabaseReference dbRef2 = db.getReference("yapilacaklar/"+key);
        dbRef2.setValue(todo);

    }

    private void yapilacaklariGoster(){

        DatabaseReference dbRef = db.getReference("yapilacaklar");
        // dbRef.addValueEventListener() uygulama açıldıktan sonra herhangi bi değişikilk anında yansıyacak
        //dbRef.addListenerForSingleValueEvent(); uygulama açılınca bi kere çekilecek tekrar bağlantı sağklanmayacak
        //dbRef.addChildEventListener();  childları dinler
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                gor.setText("");
                Iterable<DataSnapshot> keys = dataSnapshot.getChildren();
                for (DataSnapshot key: keys) {
                    gor.append(key.getValue().toString() + "\n");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
