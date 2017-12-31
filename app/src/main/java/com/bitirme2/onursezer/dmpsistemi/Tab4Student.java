package com.bitirme2.onursezer.dmpsistemi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by OnurSezer on 20.11.2017.
 */

public class Tab4Student extends Fragment {
    private Context fCon;
    String classID,userBean;

    public static Tab4Student newInstance( String id, String userBean) {
        Tab4Student result = new Tab4Student();
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

        Button btn = (Button) getView().findViewById(R.id.btnScanQRCode);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(fCon, ScanQRCode.class);
                intent.putExtra("CLASSID",  classID);
                intent.putExtra("USER",  userBean);
                startActivity(intent);

            }
        });

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab4_student, container, false);
    }
}
