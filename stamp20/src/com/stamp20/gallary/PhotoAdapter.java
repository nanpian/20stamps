package com.stamp20.gallary;

import java.util.List;

import com.squareup.picasso.Picasso;
import com.stamp20.app.R;
import com.stamp20.app.view.MyImageView;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class PhotoAdapter extends BaseAdapter {
    private List<Photo> mPhotos;
    private Context mContext;
    private LayoutInflater mInflater;

    public PhotoAdapter(Context context, List<Photo> photos) {
        this.mPhotos = photos;
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (null == mPhotos)
            return 0;
        return mPhotos.size();
    }

    @Override
    public Object getItem(int position) {
        if (null == mPhotos)
            return null;
        return mPhotos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        Photo photo = mPhotos.get(position);

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.grid_child_item, null);
            viewHolder.mImageView = (MyImageView) convertView.findViewById(R.id.child_image);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String uri = photo.getUri();
        // "file://" or "http://"
        Picasso.with(mContext).load(uri).resize(200, 200).centerCrop().placeholder(R.drawable.friends_sends_pictures_no).into(viewHolder.mImageView);

        return convertView;
    }

    public static class ViewHolder {
        public MyImageView mImageView;
    }

}
