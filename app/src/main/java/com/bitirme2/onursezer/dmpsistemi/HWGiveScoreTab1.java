package com.bitirme2.onursezer.dmpsistemi;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OnurSezer on 27.12.2017.
 */

public class HWGiveScoreTab1 extends Fragment {

    Context fCon;
    FirebaseDatabase db;
    HomeworkInfo homeworkObj;


    private DatabaseReference mDatabaseRef;
    private List<ImageUpload> imgList;
    private ListView lv;
    private ImageListAdapter adapter;

    public static HWGiveScoreTab1 newInstance(String homeworkInfo) {
        HWGiveScoreTab1 result = new HWGiveScoreTab1();
        Bundle bundle = new Bundle();
        bundle.putString("hw", homeworkInfo);
        result.setArguments(bundle);
        return result;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        String hwGson;
        hwGson = bundle.getString("hw");
        Gson gS = new Gson();
        homeworkObj = gS.fromJson(hwGson, HomeworkInfo.class);
        fCon = getContext();
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);

        imgList = new ArrayList<>();
        lv = (ListView) getView().findViewById(R.id.listViewStudentHomework);
        //Show progress dialog during list image loading

        Gson gS = new Gson();
        String path = homeworkObj.getLink();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(path);

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //Fetch image data from firebase database
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //ImageUpload class require default constructor
                    ImageUpload img = snapshot.getValue(ImageUpload.class);
                    imgList.add(img);
                }

                //Init adapter
                adapter = new ImageListAdapter((Activity) fCon, R.layout.image_item, imgList);
                //Set adapter for listview
                lv.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.hw_score_tab1, container, false);
    }
}
