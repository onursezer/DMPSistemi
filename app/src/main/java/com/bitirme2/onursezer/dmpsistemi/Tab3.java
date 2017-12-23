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
 * Created by OnurSezer on 20.11.2017.
 */

public class Tab3 extends Fragment {
    private Context fCon;
    private String classID, teacherName, teacherMail;
    FirebaseDatabase db;
    ListView mListView;
    TextView txtTeacherName, txtTeacherMail, txtClassID;

    public static Tab3 newInstance( String id, String teacherName, String teacherMail ) {
        Tab3 result = new Tab3();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putString("teacherName", teacherName);
        bundle.putString("teacherMail", teacherMail);
        result.setArguments(bundle);
        return result;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        classID =  bundle.getString("id");
        teacherName =  bundle.getString("teacherName");
        teacherMail =  bundle.getString("teacherMail");
        fCon = getContext();
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);
        db = FirebaseDatabase.getInstance();
        mListView  = (ListView) getView().findViewById(R.id.list_of_student);
        txtClassID = (TextView) getView().findViewById(R.id.txtInfoClassId);
        txtTeacherMail = (TextView) getView().findViewById(R.id.txtInfoTeacherMail);
        txtTeacherName = (TextView) getView().findViewById(R.id.txtInfoTeacherName);

        txtClassID.setText(classID);
        txtTeacherName.setText(teacherName);
        txtTeacherMail.setText(teacherMail);

        DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference();
        Query applesQuery2 = ref2.child("Map2").orderByChild("classId").equalTo(classID);
        applesQuery2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot tasksSnapshot) {

                final List<String> list = new ArrayList<String>();
                final List<String> list2 = new ArrayList<String>();

                for (DataSnapshot snapshot: tasksSnapshot.getChildren()) {
                    List<StudentInfo> l = snapshot.getValue(MapClassAndStudents.class).getList();
                    if(l != null)
                    {
                        for (StudentInfo data : l) {
                            list.add( data.getName() + " " + data.getSurname());
                            list2.add( data.getEmail() );
                        }

                        String[] names = new String[list.size()];
                        String[] mails = new String[list.size()];
                        Integer[] icons = new Integer[list.size()];
                        for (int i = 0; i < list.size(); i++) {
                            names[i] = list.get(i);
                        }
                        for (int i = 0; i < list2.size(); i++) {
                            mails[i] = list2.get(i);
                        }
                        for (int i = 0; i < list.size(); i++) {
                            icons[i] = R.mipmap.class_person_icon;
                        }
                        MyAdapter myAdapter = new MyAdapter(fCon, names,mails, icons);
                        mListView.setAdapter(myAdapter);
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
        return inflater.inflate(R.layout.tab3, container, false);
    }
}
