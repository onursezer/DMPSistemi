package com.bitirme2.onursezer.dmpsistemi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

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
 * Created by OnurSezer on 24.12.2017.
 */

public class HWtab2 extends Fragment {

    private Context fCon;
    private FirebaseDatabase db;
    private String hwGson;
    private Homework homeworkObj;
    private ListView mListView;

    public static HWtab2 newInstance(String homework) {
        HWtab2 result = new HWtab2();
        Bundle bundle = new Bundle();
        bundle.putString("hw", homework);
        result.setArguments(bundle);
        return result;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        hwGson = bundle.getString("hw");
        Gson gS = new Gson();
        homeworkObj = gS.fromJson(hwGson, Homework.class);
        fCon = getContext();
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);


        db = FirebaseDatabase.getInstance();
        mListView  = (ListView) getView().findViewById(R.id.listViewStudentsHW);

        DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference();
        Query applesQuery2 = ref2.child("Map4").orderByChild("hwID").equalTo(homeworkObj.getHwId());
        applesQuery2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot tasksSnapshot) {

                final List<String> list = new ArrayList<String>();
                final List<String> list2 = new ArrayList<String>();
                final List<HomeworkInfo> list3 = new ArrayList<HomeworkInfo>();
                final List<String> list4 = new ArrayList<String>();

                for (DataSnapshot snapshot: tasksSnapshot.getChildren()) {
                    List<HomeworkInfo> l = snapshot.getValue(MapHomeworkAndStudent.class).getList();
                    if(l != null)
                    {
                        for (HomeworkInfo data : l) {
                            list2.add( data.getStudentInfo().getEmail() );
                            list.add( data.getStudentInfo().getName() + " " + data.getStudentInfo().getSurname() );
                            list3.add(data);
                            list4.add(data.getScore());
                        }
                        String[] names = new String[list.size()];
                        String[] date = new String[list.size()];
                        String[] scores = new String[list.size()];
                        Integer[] icons = new Integer[list.size()];
                        for (int i = 0; i < list.size(); i++) {
                            names[i] = list.get(i);
                        }
                        for (int i = 0; i < list2.size(); i++) {
                            date[i] = list2.get(i);
                        }
                        for (int i = 0; i < list.size(); i++) {
                            icons[i] = R.mipmap.icon_hw;
                        }
                        for (int i = 0; i < list4.size(); i++) {
                            scores[i] = list4.get(i);
                        }

                        MyAdapter2 myAdapter2 = new MyAdapter2(fCon, names, date, icons, scores);
                        mListView.setAdapter(myAdapter2);


                        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                System.out.println("position : " + position);

                                Gson gS = new Gson();
                                String hwInfo = gS.toJson(list3.get(position));

                                Intent intent = new Intent(fCon, HWGiveScore.class);
                                intent.putExtra("HWINFO", hwInfo);
                                intent.putExtra("HW", hwGson);
                                startActivity(intent);

                            }
                        });
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
        return inflater.inflate(R.layout.hw_tab2, container, false);
    }

}
