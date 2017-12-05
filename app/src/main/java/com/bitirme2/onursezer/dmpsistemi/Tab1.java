package com.bitirme2.onursezer.dmpsistemi;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

/**
 * Created by OnurSezer on 20.11.2017.
 */

public class Tab1 extends Fragment {

    FirebaseDatabase db;
    ListView mListView;
    User teacher;
    Context fCon;

    private static int SIGN_IN_REQUEST_CODE = 1;
    private FirebaseListAdapter<ChatMessage> adapter;
    RelativeLayout activity_main;

    //Add Emojicon
    EmojiconEditText emojiconEditText;
    ImageView emojiButton,submitButton;
    EmojIconActions emojIconActions;

    private String nameUser;
    private String classID;

    public static Tab1 newInstance(String name, String id) {
        Tab1 result = new Tab1();
        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        bundle.putString("id", id);
        result.setArguments(bundle);
        return result;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        nameUser = bundle.getString("name");
        classID =  bundle.getString("id");
        fCon = getContext();
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);

        //Add Emoji
        emojiButton = (ImageView)getView().findViewById(R.id.emoji_button);
        submitButton = (ImageView)getView().findViewById(R.id.submit_button);
        emojiconEditText = (EmojiconEditText)getView().findViewById(R.id.emojicon_edit_text);
        emojIconActions = new EmojIconActions(getActivity().getApplicationContext(),activity_main, emojiconEditText, emojiButton);
//        emojIconActions.ShowEmojIcon();

        db = FirebaseDatabase.getInstance();
        mListView  = (ListView) getView().findViewById(R.id.list_of_message);


        DatabaseReference dbRef = db.getReference("Sohpet/"+classID);
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<String> list = new ArrayList<String>();
                final List<String> list2 = new ArrayList<String>();
                final List<String> list3 = new ArrayList<String>();
                for (DataSnapshot data: dataSnapshot.getChildren()) {
                    list.add(data.getValue(ChatMessage.class).getMessageUser());
                    list2.add(data.getValue(ChatMessage.class).getMessageTime());
                    list3.add(data.getValue(ChatMessage.class).getMessageText());
                }
                String[] messageUser = new String[list.size()];
                String[] messageTime = new String[list.size()];
                String[] messageText = new String[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    messageUser[i] = list.get(i);
                }
                for (int i = 0; i < list2.size(); i++) {
                    messageTime[i] = list2.get(i);
                }
                for (int i = 0; i < list.size(); i++) {
                    messageText[i] = list3.get(i);
                }
                MyMessageAdapter myAdapter = new MyMessageAdapter(fCon, messageUser, messageTime, messageText);
                mListView.setAdapter(myAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!emojiconEditText.getText().toString().trim().equals("") && emojiconEditText.getText().toString() != null){
                    FirebaseDatabase.getInstance().getReference("Sohpet/"+classID).push().setValue(new ChatMessage(emojiconEditText.getText().toString().trim(),
                            nameUser));
                    emojiconEditText.setText("");
                    emojiconEditText.requestFocus();
                }
            }
        });
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab1, container, false);
    }

}
