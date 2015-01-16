package com.stamp20.app.adapter;

import java.util.ArrayList;
import java.util.List;

import jp.co.cyberagent.android.gpuimage.GPUImageColorInvertFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageContrastFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageGammaFilter;
import jp.co.cyberagent.android.gpuimage.GPUImagePixelationFilter;

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
import com.stamp20.app.filter.BlueFilter;
import com.stamp20.app.filter.GrayFilter;
import com.stamp20.app.filter.IImageFilter;
import com.stamp20.app.filter.LomoFilter;
import com.stamp20.app.filter.NormaFilter;
import com.stamp20.app.filter.SunShineFilter;
import com.stamp20.app.filter.YellowFilter;
import com.stamp20.app.util.FontManager;

public class ImageFilterAdapter extends BaseAdapter {
	private class FilterInfo {
		public int filterID;
		public String filterName;
		public IImageFilter filter;
		public GPUImageFilter gpufilter;
		public String type;
		public int selectItem;

		public FilterInfo(int filterID, IImageFilter filter) {
			this.filterID = filterID;
			this.filter = filter;
		}
		
		public FilterInfo(int filterID, IImageFilter filter, String filtername) {
			this.filterID = filterID;
			this.filter = filter;
			this.filterName = filtername;
			this.type = "common";
		}
		
		public FilterInfo(int filterID, GPUImageFilter gpufilter, String filtername) {
			this.filterID = filterID;
			this.gpufilter = gpufilter;
			this.filterName = filtername;
			this.type = "gpu";
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
		filterArray.add(new FilterInfo(R.drawable.filter_sample_normal, new NormaFilter(),"normal"));
		filterArray.add(new FilterInfo(R.drawable.filter_sample_gray, new GrayFilter(),"gray"));
		filterArray.add(new FilterInfo(R.drawable.filter_sample_black, new BlackWhiteFilter(),"black"));
		filterArray.add(new FilterInfo(R.drawable.filter_sample_yellow, new YellowFilter(),"yellow"));
		filterArray.add(new FilterInfo(R.drawable.filter_sample_sunshine, new BlueFilter(),"Blue"));
		filterArray.add(new FilterInfo(R.drawable.filter_sample_sunshine, new LomoFilter(),"Lomo"));
		filterArray.add(new FilterInfo(R.drawable.filter_sample_sunshine, new GPUImageContrastFilter(),"contrast"));
		filterArray.add(new FilterInfo(R.drawable.filter_sample_sunshine, new GPUImageGammaFilter(),"gama"));
		filterArray.add(new FilterInfo(R.drawable.filter_sample_sunshine, new GPUImageColorInvertFilter(),"invert"));
		filterArray.add(new FilterInfo(R.drawable.filter_sample_sunshine, new GPUImagePixelationFilter(),"pixel"));
		
	}

	public int getCount() {
		return filterArray.size();
	}

	public Object getItem(int position) {
		if(filterArray.get(position).type.equals("common")) {
		return position < filterArray.size() ? filterArray.get(position).filter
				: null;
		} else if (filterArray.get(position).type.equals("gpu")) {
			return position < filterArray.size() ? filterArray.get(position).gpufilter
					: null;
		}
		return null;
	}
	
	public String getType(int position) {
		return position < filterArray.size() ? filterArray.get(position).type
				: null;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		int width = 180;
		int height = 180;
		convertView = mInflater.inflate(R.layout.gallery_item_layout, null);
		FontManager.changeFonts(mContext, (LinearLayout)convertView.findViewById(R.id.root));
		ImageView imageView = null;
	    TextView textView = null;
        imageView = (ImageView)convertView.findViewById(R.id.image_item);
        textView = (TextView)convertView.findViewById(R.id.text_item);
	    imageView.setImageResource(filterArray.get(position).filterID);
	    
		 if(selectItem==position){
			 //处理点击放大效果，注意，这里还要加入边框效果，需要UI
		    //Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.gallery_click_scale);
		    //imageView.startAnimation(animation);
		    //imageView.setLayoutParams(new LinearLayout.LayoutParams(200,200));
		    textView.setTextColor(Color.parseColor("#f1c40f"));
		    imageView.setBackgroundResource(R.drawable.bg_filter_item_selected);
		  } else {
		    //imageView.setLayoutParams(new LinearLayout.LayoutParams(width, height));
		    textView.setTextColor(Color.parseColor("#ffffff"));
		    imageView.setBackgroundResource(R.drawable.bg_filter_item_selected_no);
		  }
		 //imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
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