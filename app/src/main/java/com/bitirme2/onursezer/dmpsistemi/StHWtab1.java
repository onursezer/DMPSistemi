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
 * Created by OnurSezer on 26.12.2017.
 */

public class StHWtab1 extends Fragment {

    Context fCon;
    FirebaseDatabase db;
    String hwGson;
    Homework homeworkObj;


    private DatabaseReference mDatabaseRef;
    private List<ImageUpload> imgList;
    private ListView lv;
    private ImageListAdapter adapter;

    public static StHWtab1 newInstance(String homework) {
        StHWtab1 result = new StHWtab1();
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

        imgList = new ArrayList<>();
        lv = (ListView) getView().findViewById(R.id.listViewImageST);
        //Show progress dialog during list image loading

        Gson gS = new Gson();
        String path = homeworkObj.getHwId();

        System.out.println("** PATH : " + path);
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
        return inflater.inflate(R.layout.st_hw_tab1, container, false);
    }

}