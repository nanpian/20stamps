package com.stamp20.app.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.stamp20.app.R;
import com.stamp20.app.util.FontManager;

public class AboutAdapter extends BaseAdapter {
    private List<String> aboutArray = new ArrayList<String>();
    private int[] aboutstring = { R.string.about_1, R.string.about_2, R.string.about_3, R.string.about_4,
            R.string.about_5, R.string.about_6, R.string.about_7, R.string.about_8, R.string.about_9, R.string.about_10 };
    private Context mContext;
    private LayoutInflater mInflater;

    public AboutAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        for (int i = 0; i < 10; i++) {
            aboutArray.add(mContext.getResources().getString(aboutstring[i]));
        }
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return aboutArray.size();
    }

    @Override
    public Object getItem(int postion) {
        // TODO Auto-generated method stub
        return getCount() > 0 ? aboutArray.get(postion) : null;
    }

    @Override
    public long getItemId(int postion) {
        // TODO Auto-generated method stub
        return postion;
    }

    @Override
    public View getView(int postion, View view, ViewGroup parent) {
        // TODO Auto-generated method stub
        view = mInflater.inflate(R.layout.about_item, null);
        TextView mTextView = (TextView) view.findViewById(R.id.about_text);
        FontManager.changeFonts(mContext, mTextView);
        mTextView.setText(aboutArray.get(postion));
        return view;
    }

}