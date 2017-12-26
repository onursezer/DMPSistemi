package com.bitirme2.onursezer.dmpsistemi;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

/**
 * Created by OnurSezer on 24.12.2017.
 */

public class HWtab2 extends Fragment {

    Context fCon;
    FirebaseDatabase db;
    String hwGson;
    Homework homeworkObj;

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
        TextView text = (TextView) getView().findViewById(R.id.textView9);
        text.setText(homeworkObj.getNameOfHW());

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.hw_tab2, container, false);
    }

}
