package com.stamp20.gallary;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.stamp20.app.R;
import com.stamp20.app.util.FontManager;
import com.stamp20.app.view.MyImageView;

public class AlbumAdapter extends BaseAdapter {
    public static class ViewHolder {
        public MyImageView mImageView;
        public TextView mTextViewCounts;
        public TextView mTextViewTitle;
    }
    private List<Album> mAlbums;
    private Context mContext;

    private LayoutInflater mInflater;

    public AlbumAdapter(Context context, List<Album> albums) {
        this.mContext = context;
        this.mAlbums = albums;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (null == mAlbums)
            return 0;

        return mAlbums.size();
    }

    @Override
    public Object getItem(int position) {
        if (null == mAlbums)
            return null;

        return mAlbums.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        Album album = mAlbums.get(position);

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.home_list_group_item, null);
            FontManager.changeFonts(mContext, (RelativeLayout) convertView.findViewById(R.id.root));
            viewHolder.mImageView = (MyImageView) convertView.findViewById(R.id.list_group_image);
            viewHolder.mTextViewTitle = (TextView) convertView.findViewById(R.id.list_group_title);
            viewHolder.mTextViewCounts = (TextView) convertView.findViewById(R.id.list_group_count);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.mTextViewTitle.setText(album.getAlbumName());
        viewHolder.mTextViewCounts.setText(" (" + Integer.toString(album.getContentCount()) + ")");

        String uri = album.getCoverUri();
        // "file://" or "http://"
        Log.i("wangpeng14", "album cover uri: " + uri);
        Picasso.with(mContext).load(uri).resize(200, 200).centerCrop()
                .placeholder(R.drawable.friends_sends_pictures_no).into(viewHolder.mImageView);

        return convertView;
    }

}
