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
 * Created by OnurSezer on 28.12.2017.
 */

public class Tab5Student extends Fragment {
    private Context fCon;
    String classID,userBean;
    User user;

    public static Tab5Student newInstance(String id, String userBean) {
        Tab5Student result = new Tab5Student();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putString("userBean", userBean);
        result.setArguments(bundle);
        return result;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        classID =  bundle.getString("id");
        userBean =  bundle.getString("userBean");
        fCon = getContext();
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);
        Gson gS = new Gson();
        user = gS.fromJson(userBean, User.class);

        final TextView textCountAttendanceSt = (TextView) getView().findViewById(R.id.textCountAttendanceSt);
        final TextView textStudentCountAttendance = (TextView) getView().findViewById(R.id.textStudentCountAttendance);

        DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference();
        Query applesQuery2 = ref2.child("Map5").orderByChild("classId").equalTo(classID);
        applesQuery2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot tasksSnapshot) {

                for (DataSnapshot snapshot: tasksSnapshot.getChildren()) {

                    List<Attendance> l1 = snapshot.getValue(MapAttendanceAndClass.class).getList();
                    if(l1 != null)
                        textCountAttendanceSt.setText(Integer.toString(l1.size()));

                    List<AttendanceStudent> l = snapshot.getValue(MapAttendanceAndClass.class).getList2();
                    if(l != null)
                    {
                        for (int i = 0; i < l.size(); i++) {
                            if(l.get(i).getStudent().getEmail().equals(user.getEmail())){
                                textStudentCountAttendance.setText(l.get(i).getAttendace());
                            }
                        }
                    }
                }
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
        return inflater.inflate(R.layout.tab5_student, container, false);
    }
}
