package com.bitirme2.onursezer.dmpsistemi;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
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

/**
 * Created by OnurSezer on 27.12.2017.
 */

public class HWGiveScoreTab2  extends Fragment {

    private Context fCon;
    private FirebaseDatabase db;
    private Homework homeworkObj;
    private HomeworkInfo homeworkInfoObj;

    private DatabaseReference mDatabaseRef;
    private List<ImageUpload> imgList;
    private ListView lv;
    private ImageListAdapter adapter;

    public static HWGiveScoreTab2 newInstance(String homeworkInfo, String homework) {
        HWGiveScoreTab2 result = new HWGiveScoreTab2();
        Bundle bundle = new Bundle();
        bundle.putString("hwinfo", homeworkInfo);
        bundle.putString("hw", homework);
        result.setArguments(bundle);
        return result;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        String hwInfoGson, hwGson;
        hwInfoGson = bundle.getString("hwinfo");
        hwGson = bundle.getString("hw");
        Gson gS = new Gson();
        homeworkInfoObj = gS.fromJson(hwInfoGson, HomeworkInfo.class);
        homeworkObj = gS.fromJson(hwGson, Homework.class);
        fCon = getContext();
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);

        TextView textNameOfHomework = (TextView) getView().findViewById(R.id.textNameOfHomework);
        TextView txthwDeliveryDate = (TextView) getView().findViewById(R.id.txthwDeliveryDate);
        TextView txthwDeliveryTime = (TextView) getView().findViewById(R.id.txthwDeliveryTime);
        TextView txtStudentName = (TextView) getView().findViewById(R.id.txtStudentName);
        TextView txtStudentSurname = (TextView) getView().findViewById(R.id.txtStudentSurname);
        TextView txtStudentMail = (TextView) getView().findViewById(R.id.txtStudentMail);
        TextView txtStudentDeliveryDate = (TextView) getView().findViewById(R.id.txtStudentDeliveryDate);
        TextView txtStudentDeliveryTime = (TextView) getView().findViewById(R.id.txtStudentDeliveryTime);
        final EditText editText21 = (EditText) getView().findViewById(R.id.editText21);
        Button btnGiveScoreHomework = (Button) getView().findViewById(R.id.btnGiveScoreHomework);


        textNameOfHomework.setText(homeworkObj.getNameOfHW());
        txthwDeliveryDate.setText(homeworkObj.getDeliveryDate());
        txthwDeliveryTime.setText(homeworkObj.getDeliveryTime());
        txtStudentName.setText(homeworkInfoObj.getStudentInfo().getName());
        txtStudentSurname.setText(homeworkInfoObj.getStudentInfo().getSurname());
        txtStudentMail.setText(homeworkInfoObj.getStudentInfo().getEmail());
        txtStudentDeliveryDate.setText(homeworkInfoObj.getDeliveryDate());
        txtStudentDeliveryTime.setText(homeworkInfoObj.getDelivetyTime());

        btnGiveScoreHomework.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String score = editText21.getText().toString();

                DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference();
                Query applesQuery2 = ref2.child("Map4").orderByChild("hwID").equalTo(homeworkObj.getHwId());
                applesQuery2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot tasksSnapshot) {
                        for (DataSnapshot snapshot: tasksSnapshot.getChildren()) {
                            List<HomeworkInfo> l = snapshot.getValue(MapHomeworkAndStudent.class).getList();
                            if(l == null)
                            {
                                l = new ArrayList<HomeworkInfo>();
                            }
                            for (int i = 0; i < l.size(); i++) {
                                if(l.get(i).getStudentInfo().getEmail().equals(homeworkInfoObj.getStudentInfo().getEmail())){
                                    HomeworkInfo info = l.get(i);
                                    info.setScore(score);
                                    l.set(i,info);
                                }
                            }
                            snapshot.getRef().child("list").setValue(l);
                        }
                        Toast.makeText(fCon, "Puan Verildi!", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                ref2.onDisconnect();
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.hw_score_tab2, container, false);
    }
}
