package com.bitirme2.onursezer.dmpsistemi;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by OnurSezer on 31.10.2017.
 */

public class MyAdapter extends ArrayAdapter<String>{

    String[] names;
    String[] names2;
    Integer [] flags;
    Context mContext;

    public MyAdapter(Context context, String[] className,String[] branchName, Integer[] countryFlags) {
        super(context, R.layout.listview_item);
        this.names = className;
        this.names2 = branchName;
        this.flags = countryFlags;
        this.mContext = context;
        System.out.println("MyAdapter");
    }

    @Override
    public int getCount() {
        return names.length;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder mViewHolder = new ViewHolder();
        if(convertView == null) {
            LayoutInflater mInf = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInf.inflate(R.layout.listview_item, parent, false);
            mViewHolder.mFlag = (ImageView) convertView.findViewById(R.id.imageView);
            mViewHolder.mName = (TextView) convertView.findViewById(R.id.textListItem);
            mViewHolder.mName2 = (TextView) convertView.findViewById(R.id.textListItem2);
            convertView.setTag(mViewHolder);
        }
        else{
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        mViewHolder.mFlag.setImageResource(flags[position]);
        mViewHolder.mName.setText(names[position]);
        mViewHolder.mName2.setText(names2[position]);
        return convertView;
    }

    static class  ViewHolder{
        ImageView mFlag;
        TextView mName;
        TextView mName2;
    }
}
