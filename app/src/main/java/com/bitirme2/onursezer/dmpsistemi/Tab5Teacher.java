package com.bitirme2.onursezer.dmpsistemi;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OnurSezer on 28.12.2017.
 */

public class Tab5Teacher extends Fragment {
    private Context fCon;
    String classID;

    public static Tab5Teacher newInstance(String id) {
        Tab5Teacher result = new Tab5Teacher();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        result.setArguments(bundle);
        return result;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        classID =  bundle.getString("id");
        fCon = getContext();
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);
        final ListView mListView = (ListView) getView().findViewById(R.id.listViewAttendanceStudents);
        final TextView textCountAttendance = (TextView) getView().findViewById(R.id.textCountAttendance);

        DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference();
        Query applesQuery2 = ref2.child("Map5").orderByChild("classId").equalTo(classID);
        applesQuery2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot tasksSnapshot) {

                final List<String> list = new ArrayList<String>();
                final List<String> list2 = new ArrayList<String>();
                final List<String> list3 = new ArrayList<String>();

                for (DataSnapshot snapshot: tasksSnapshot.getChildren()) {

                    List<Attendance> l1 = snapshot.getValue(MapAttendanceAndClass.class).getList();
                    if(l1 != null)
                        textCountAttendance.setText(Integer.toString(l1.size()));

                    List<AttendanceStudent> l = snapshot.getValue(MapAttendanceAndClass.class).getList2();
                    if(l != null)
                    {
                        for (AttendanceStudent data : l) {
                            list2.add( data.getStudent().getEmail() );
                            list.add( data.getStudent().getName() + " " + data.getStudent().getSurname() );
                            list3.add(data.getAttendace());
                        }
                        String[] names = new String[list.size()];
                        String[]  mail = new String[list.size()];
                        String[] scores = new String[list.size()];
                        Integer[] icons = new Integer[list.size()];
                        for (int i = 0; i < list.size(); i++) {
                            names[i] = list.get(i);
                        }
                        for (int i = 0; i < list2.size(); i++) {
                            mail[i] = list2.get(i);
                        }
                        for (int i = 0; i < list.size(); i++) {
                            icons[i] = R.mipmap.icon_attedance;
                        }
                        for (int i = 0; i < list3.size(); i++) {
                            scores[i] = list3.get(i);
                        }

                        MyAdapter2 myAdapter2 = new MyAdapter2(fCon, names, mail, icons, scores);
                        mListView.setAdapter(myAdapter2);
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
        return inflater.inflate(R.layout.tab5_teacher, container, false);
    }
}
