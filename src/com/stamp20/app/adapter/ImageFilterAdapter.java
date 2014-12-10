package com.stamp20.app.adapter;

import java.util.ArrayList;
import java.util.List;

import com.stamp20.app.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import com.stamp20.app.R;
import com.stamp20.app.filter.BlackWhiteFilter;
import com.stamp20.app.filter.GrayFilter;
import com.stamp20.app.filter.IImageFilter;
import com.stamp20.app.filter.NormaFilter;
import com.stamp20.app.filter.SunShineFilter;
import com.stamp20.app.filter.YellowFilter;

public class ImageFilterAdapter extends BaseAdapter {
	private class FilterInfo {
		public int filterID;
		public IImageFilter filter;

		public FilterInfo(int filterID, IImageFilter filter) {
			this.filterID = filterID;
			this.filter = filter;
		}
	}

	private Context mContext;
	private List<FilterInfo> filterArray = new ArrayList<FilterInfo>();

	/**
	 * @param c 保存Context
	 * @author zhudewei
	 * @todo  定义八种滤镜，分别是正常、灰度、光照、泛黄
	 */
	public ImageFilterAdapter(Context c) {
		mContext = c;
		
		filterArray.add(new FilterInfo(R.drawable.video_filter1, new NormaFilter()));
		filterArray.add(new FilterInfo(R.drawable.video_filter2, new GrayFilter()));
		filterArray.add(new FilterInfo(R.drawable.video_filter3, new BlackWhiteFilter()));
		filterArray.add(new FilterInfo(R.drawable.video_filter4, new YellowFilter()));
		filterArray.add(new FilterInfo(R.drawable.video_filter4, new SunShineFilter()));
		
	}

	public int getCount() {
		return filterArray.size();
	}

	public Object getItem(int position) {
		return position < filterArray.size() ? filterArray.get(position).filter
				: null;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		Bitmap bmImg = BitmapFactory
				.decodeResource(mContext.getResources(),
						filterArray.get(position).filterID);
		int width = 100;// bmImg.getWidth();
		int height = 100;// bmImg.getHeight();
		bmImg.recycle();
		ImageView imageview = new ImageView(mContext);
		imageview.setImageResource(filterArray.get(position).filterID);
		imageview.setLayoutParams(new Gallery.LayoutParams(width, height));
		imageview.setScaleType(ImageView.ScaleType.FIT_CENTER);
		return imageview;
	}
};
