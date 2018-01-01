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
import android.widget.Toast;

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

public class Tab4Teacher extends Fragment {
    private Context fCon;
    String classID;

    public static Tab4Teacher newInstance( String id) {
        Tab4Teacher result = new Tab4Teacher();
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

        final EditText editTextQRCodeStr = (EditText) getView().findViewById(R.id.editTextQRCodeStr);
        final EditText editTextAttendanceTime = (EditText) getView().findViewById(R.id.editTextAttendanceTime);
        Button btnStartAttendance = (Button) getView().findViewById(R.id.btnStartAttendance);

        btnStartAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference();
                Query applesQuery2 = ref2.child("Map5").orderByChild("classId").equalTo(classID);
                applesQuery2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot tasksSnapshot) {
                        for (DataSnapshot snapshot: tasksSnapshot.getChildren()) {
                            List<Attendance> l = snapshot.getValue(MapAttendanceAndClass.class).getList();
                            if(l == null)
                            {
                                l = new ArrayList<Attendance>();
                            }
                            l.add(new Attendance(editTextQRCodeStr.getText().toString(), editTextAttendanceTime.getText().toString()));
                            snapshot.getRef().child("list").setValue(l);
                        }
                        Toast.makeText(fCon, "Yoklama Başlatıldı!", Toast.LENGTH_SHORT).show();
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
        return inflater.inflate(R.layout.tab4_teacher, container, false);
    }
}
