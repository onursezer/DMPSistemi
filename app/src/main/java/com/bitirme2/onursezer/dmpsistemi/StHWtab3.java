package com.bitirme2.onursezer.dmpsistemi;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.List;

/**
 * Created by OnurSezer on 26.12.2017.
 */

public class StHWtab3 extends Fragment {

    Context fCon;
    FirebaseDatabase db;
    Homework homeworkObj;
    User userObj;



    public static StHWtab3 newInstance(String homework, String user) {
        StHWtab3 result = new StHWtab3();
        Bundle bundle = new Bundle();
        bundle.putString("hw", homework);
        bundle.putString("user", user);
        result.setArguments(bundle);
        return result;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        String hwGson, userGson;
        hwGson = bundle.getString("hw");
        userGson = bundle.getString("user");
        Gson gS = new Gson();
        homeworkObj = gS.fromJson(hwGson, Homework.class);
        userObj = gS.fromJson(userGson, User.class);
        fCon = getContext();
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);

        final TextView text = (TextView) getView().findViewById(R.id.textHWScore) ;
        final String[] str = {""};
        DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference();
        Query applesQuery2 = ref2.child("Map4").orderByChild("hwID").equalTo(homeworkObj.getHwId());
        applesQuery2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot tasksSnapshot) {
                int st = -1;
                for (DataSnapshot snapshot: tasksSnapshot.getChildren()) {
                    List<HomeworkInfo> l = snapshot.getValue(MapHomeworkAndStudent.class).getList();
                    if(l != null) {
                        for (int i = 0; i < l.size(); i++) {
                            if (l.get(i).getStudentInfo().getEmail().equals(userObj.getEmail())) {
                                if(l.get(i).getScore() != null || l.get(i).getScore().equals("")){
                                    str[0] = l.get(i).getScore();
                                    st = 1;
                                }
                                break;
                            }
                        }
                    }
                }
                if(st == 1)
                    text.setText("Ödevinize girilen not : " +str[0] );
                else
                    text.setText("Ödevinize not girilmedi.");
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        ref2.onDisconnect();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.st_hw_tab3, container, false);
    }

}