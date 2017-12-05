package com.bitirme2.onursezer.dmpsistemi;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * Created by OnurSezer on 20.11.2017.
 */

public class Tab2 extends Fragment {

    private String classID;
    private TextView txtID;
    public static Tab2 newInstance( String id) {
        Tab2 result = new Tab2();
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
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);
        txtID = (TextView)getView().findViewById(R.id.textView11);
        txtID.setText(classID);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab2, container, false);
    }
}
