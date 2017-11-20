package com.bitirme2.onursezer.dmpsistemi;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by OnurSezer on 20.11.2017.
 */

public class Tab1 extends Fragment {


    private static final String HOTEL = "hotel";

    private TextView hotel_nombre;
    private String name;

    public static Tab1 newInstance(String s) {
        Tab1 result = new Tab1();
        Bundle bundle = new Bundle();
        bundle.putString(HOTEL, s);
        result.setArguments(bundle);
        return result;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        name = bundle.getString(HOTEL);
    }


    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);
        hotel_nombre = (TextView) getView().findViewById(R.id.textView4);
        hotel_nombre.setText(name);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab1, container, false);
    }
}
