package com.bitirme2.onursezer.dmpsistemi;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.zxing.Result;

import java.util.ArrayList;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanQRCode extends AppCompatActivity  implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;
    private String classId;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qrcode);
        classId = getIntent().getStringExtra("CLASSID");
        Gson gS = new Gson();
        String userBean = getIntent().getStringExtra("USER");
        user = gS.fromJson(userBean, User.class);
        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(final Result result) {
        //Do anything with result here :D
        Log.w("handleResult", result.getText());


        DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference();
        Query applesQuery2 = ref2.child("Map5").orderByChild("classId").equalTo(classId);
        applesQuery2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot tasksSnapshot) {
                for (DataSnapshot snapshot: tasksSnapshot.getChildren()) {
                    List<Attendance> l1 = snapshot.getValue(MapAttendanceAndClass.class).getList();
                    List<AttendanceStudent> l2 = snapshot.getValue(MapAttendanceAndClass.class).getList2();
                    if(l1 != null){
                        if(l1.get(l1.size()-1).getTxtQRcode().equals(result.getText().toString())) {
                            if (l2 == null) {
                                l2 = new ArrayList<AttendanceStudent>();
                            }
                            int flag = 0;
                            for (int i = 0; i < l2.size(); i++) {
                                if(l2.get(i).getStudent().getEmail().equals(user.getEmail()))
                                {
                                    AttendanceStudent a = l2.get(i);
                                    Integer att = Integer.parseInt(a.getAttendace());
                                    ++att;
                                    a.setAttendace( att.toString() );
                                    l2.set(i,a);
                                    flag = 1;
                                    break;
                                }
                            }
                            if(flag == 0){
                                l2.add(new AttendanceStudent(new StudentInfo(user.getName(), user.getSurname(), user.getEmail()), "0"));
                            }
                            snapshot.getRef().child("list2").setValue(l2);
                            Toast.makeText(ScanQRCode.this, "Yoklamaya Katılımınız Sağlandı!", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(ScanQRCode.this, "QR Kod Hatalı, Yoklamaya Katılımınız Sağlanamadı!", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        ref2.onDisconnect();



        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Scan result");
        builder.setMessage(result.getText());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        //Resume scanning
        //mScannerView.resumeCameraPreview(this);
    }
}