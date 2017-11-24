package com.bitirme2.onursezer.dmpsistemi;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by OnurSezer on 24.11.2017.
 */

public class MyMessageAdapter extends ArrayAdapter<String> {


    String[] messageUser;
    String[] messageTime;
    String [] messageText;
    Context mContext;

    public MyMessageAdapter(Context context, String[] messageUser,String[] messageTime, String[] messageText) {
        super(context, R.layout.listview_item);
        this.messageUser = messageUser;
        this.messageTime = messageTime;
        this.messageText = messageText;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return messageUser.length;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        MyMessageAdapter.ViewHolder mViewHolder = new MyMessageAdapter.ViewHolder();
        if(convertView == null) {
            LayoutInflater mInf = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInf.inflate(R.layout.list_item, parent, false);
            mViewHolder.messageUser = (TextView) convertView.findViewById(R.id.message_user);
            mViewHolder.messageTime = (TextView) convertView.findViewById(R.id.message_time);
            mViewHolder.messageText = (TextView) convertView.findViewById(R.id.message_text);
            convertView.setTag(mViewHolder);
        }
        else{
            mViewHolder = (MyMessageAdapter.ViewHolder) convertView.getTag();
        }
        mViewHolder.messageUser.setText(messageUser[position]);
        mViewHolder.messageTime.setText(messageTime[position]);
        mViewHolder.messageText.setText(messageText[position]);
        return convertView;
    }

    static class  ViewHolder{
        TextView messageUser;
        TextView messageTime;
        TextView messageText;
    }



}
