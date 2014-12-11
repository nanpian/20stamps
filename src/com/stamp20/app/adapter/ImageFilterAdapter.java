package com.stamp20.app.adapter;

import java.util.ArrayList;
import java.util.List;

import com.stamp20.app.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

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
		public String filterName;
		public IImageFilter filter;
		public int selectItem;

		public FilterInfo(int filterID, IImageFilter filter) {
			this.filterID = filterID;
			this.filter = filter;
		}
		
		public FilterInfo(int filterID, IImageFilter filter, String filtername) {
			this.filterID = filterID;
			this.filter = filter;
			this.filterName = filtername;
		}
	}

	private Context mContext;
	private List<FilterInfo> filterArray = new ArrayList<FilterInfo>();
	private List<String> filterName = new ArrayList<String>();
	private LayoutInflater mInflater;
	private int selectItem = -1;

	/**
	 * @param c 保存Context
	 * @author zhudewei
	 * @todo  定义八种滤镜，分别是正常、灰度、光照、泛黄
	 */
	public ImageFilterAdapter(Context c) {
		mContext = c;
		mInflater = LayoutInflater.from(mContext);
		filterArray.add(new FilterInfo(R.drawable.video_filter1, new NormaFilter(),"Normal"));
		filterArray.add(new FilterInfo(R.drawable.video_filter2, new GrayFilter(),"Gray"));
		filterArray.add(new FilterInfo(R.drawable.video_filter3, new BlackWhiteFilter(),"Black"));
		filterArray.add(new FilterInfo(R.drawable.video_filter4, new YellowFilter(),"Yellow"));
		filterArray.add(new FilterInfo(R.drawable.video_filter4, new SunShineFilter(),"SunShine"));

		
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

		int width = 180;
		int height = 180;
		convertView = mInflater.inflate(R.layout.gallery_item_layout, null);
		ImageView imageView = null;
	    TextView textView = null;
        imageView = (ImageView)convertView.findViewById(R.id.image_item);
        textView = (TextView)convertView.findViewById(R.id.text_item);
	    imageView.setImageResource(filterArray.get(position).filterID);
		 if(selectItem==position){
			 //处理点击放大效果，注意，这里还要加入边框效果，需要UI
		    Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.gallery_click_scale);
		    imageView.startAnimation(animation);
		    imageView.setLayoutParams(new LinearLayout.LayoutParams(200,200));
		  } else {
		    imageView.setLayoutParams(new LinearLayout.LayoutParams(width, height));
		  }
		imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        textView.setText(filterArray.get(position).filterName);
		return convertView;
	}

	/**
	*  日期 2014-12-12
	*  作者 zhudewei
	*  说明 得到点击的图片的id
	*  返回 void
	 * @param position
	 */
	public void setSelectItem(int selectItemid) {      
			  selectItem = selectItemid;
			  notifyDataSetChanged();  
	}
};
