package com.bitirme2.onursezer.dmpsistemi;

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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
                        if(l1.get(l1.size()-1).getTxtQRcode().equals(result.getText().toString())) { //qr code check

                            Calendar cal = Calendar.getInstance();
                            String month = new Integer(cal.get(Calendar.MONTH) + 1).toString();
                            String year = new Integer(cal.get(Calendar.YEAR)).toString();
                            String dayofmonth = new Integer(cal.get(Calendar.DAY_OF_MONTH)).toString();
                            if(month.length() == 1)
                                month = "0" + month;
                            if(dayofmonth.length() == 1)
                                dayofmonth = "0" + dayofmonth;
                            String date = dayofmonth + "/" + month + "/" + year;

                            String second = new Integer(cal.get(Calendar.SECOND)).toString();
                            String minute = new Integer(cal.get(Calendar.MINUTE)).toString();
                            String hour = new Integer(cal.get(Calendar.HOUR_OF_DAY)).toString();
                            if(second.length() == 1)
                                second = "0" + second;
                            if(minute.length() == 1)
                                minute = "0" + minute;
                            if(hour.length() == 1)
                                hour = "0" + hour;
                            String time = hour + ":" + minute + ":" +second;


                            boolean flag = false;
                            String dateStop = date + " " + time;
                            String dateStart = l1.get(l1.size()-1).getDate() + " " + l1.get(l1.size()-1).getTime();
                            long minutes = diffMinute(dateStart,dateStop);
                            System.out.println("minutes : " + minutes);
                            System.out.println("Integer.parseInt(l1.get(l1.size()-1).getOpenTime()) : " + Integer.parseInt(l1.get(l1.size()-1).getOpenTime()));
                            if(minutes <= Integer.parseInt(l1.get(l1.size()-1).getOpenTime())){  // minute check
                                for (int i = 0; i < l2.size(); i++) {
                                    if (l2.get(i).getStudent().getEmail().equals(user.getEmail())) {
                                        if(l2.get(i).getTime() != null  || l2.get(i).getDate() != null){
                                            String studentTime = l2.get(i).getTime();
                                            String studentDate = l2.get(i).getDate();

                                            String dateStop1 = studentDate + " " + studentTime;
                                            String dateStart1 = l1.get(l1.size()-1).getDate() + " " + l1.get(l1.size()-1).getTime();
                                            long minutes1 = diffMinute(dateStart1,dateStop1);
                                            if(!(minutes1 <= Integer.parseInt(l1.get(l1.size()-1).getOpenTime())))
                                            {
                                                AttendanceStudent a = l2.get(i);
                                                Integer att = Integer.parseInt(a.getAttendace());
                                                ++att;
                                                a.setDate(date);
                                                a.setTime(time);
                                                a.setAttendace(att.toString());
                                                l2.set(i, a);
                                                flag = true;
                                                break;
                                            }
                                            else
                                                Toast.makeText(ScanQRCode.this, "Yoklama Kapanmadan Yeniden Kayıt Olunmaz!", Toast.LENGTH_SHORT).show();

                                        }
                                        else
                                        {
                                            AttendanceStudent a = l2.get(i);
                                            Integer att = Integer.parseInt(a.getAttendace());
                                            ++att;
                                            a.setDate(date);
                                            a.setTime(time);
                                            a.setAttendace(att.toString());
                                            l2.set(i, a);
                                            flag = true;
                                            break;
                                        }
                                    }
                                }
                                if(flag == true)
                                {
                                    snapshot.getRef().child("list2").setValue(l2);
                                    Toast.makeText(ScanQRCode.this, "Yoklamaya Katılımınız Sağlandı!", Toast.LENGTH_SHORT).show();
                                }

                            }
                            else
                                Toast.makeText(ScanQRCode.this, "Yoklamanın Süresi Doldu, Yoklamaya Katılımınız Sağlanamadı.!", Toast.LENGTH_SHORT).show();

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



/*        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Scan result");
        builder.setMessage(result.getText());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();*/

        //Resume scanning
        //mScannerView.resumeCameraPreview(this);
    }

    private long diffMinute(String dateStart, String dateStop)
    {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date d1 = null;
        Date d2 = null;

        try {
            d1 = format.parse(dateStart);
            d2 = format.parse(dateStop);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long diff = d2.getTime() - d1.getTime();
        return TimeUnit.MILLISECONDS.toMinutes(diff);
    }
}