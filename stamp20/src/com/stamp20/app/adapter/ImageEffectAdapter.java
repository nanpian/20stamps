package com.stamp20.app.adapter;

import java.util.ArrayList;
import java.util.List;

import jp.co.cyberagent.android.gpuimage.GPUImageColorInvertFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageContrastFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageGammaFilter;
import jp.co.cyberagent.android.gpuimage.GPUImagePixelationFilter;

import com.stamp20.app.R;
import com.stamp20.app.activities.MainEffect;
import com.stamp20.app.filter.BlackWhiteFilter;
import com.stamp20.app.filter.BlueFilter;
import com.stamp20.app.filter.GrayFilter;
import com.stamp20.app.filter.IImageFilter;
import com.stamp20.app.filter.LomoFilter;
import com.stamp20.app.filter.NormaFilter;
import com.stamp20.app.filter.YellowFilter;
import com.stamp20.app.util.FontManager;

import android.content.Context;
import android.graphics.Color;
import android.media.effect.Effect;
import android.media.effect.EffectContext;
import android.media.effect.EffectFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ImageEffectAdapter extends BaseAdapter{
	
	private Context mContext;
	private LayoutInflater mInflater;
	public Effect mEffect;
	private EffectContext mEffectContext;

	/**
	 * @param c 保存Context
	 * @author zhudewei
	 * @todo  定义多种滤镜，采用android mediaeffect实现
	 */
	public ImageEffectAdapter(Context c) {
		mContext = c;
		mInflater = LayoutInflater.from(mContext);
		
	}
	
	public ImageEffectAdapter(Context c,
			EffectContext meffectcontect) {
		// TODO Auto-generated constructor stub
		mContext = c;
		mInflater = LayoutInflater.from(mContext);
		mEffectContext = meffectcontect;
		filterArray.add(new FilterInfo(0,"normal",createEffect(0)));
		filterArray.add(new FilterInfo(1,"autofix",createEffect(1)));
		filterArray.add(new FilterInfo(2,"bw",createEffect(2)));
		filterArray.add(new FilterInfo(3,"brightness",createEffect(3)));
		filterArray.add(new FilterInfo(4,"contrast",createEffect(4)));
		filterArray.add(new FilterInfo(5,"crossprocess",createEffect(5)));
		filterArray.add(new FilterInfo(6,"documentary",createEffect(6)));
		filterArray.add(new FilterInfo(7,"duotone",createEffect(7)));
		filterArray.add(new FilterInfo(8,"filllight",createEffect(8)));
		filterArray.add(new FilterInfo(9,"fisheye",createEffect(9)));
		filterArray.add(new FilterInfo(10,"flipvert",createEffect(10)));
		filterArray.add(new FilterInfo(11,"fliphor",createEffect(11)));
		filterArray.add(new FilterInfo(12,"grain",createEffect(12)));
		filterArray.add(new FilterInfo(13,"grayscale",createEffect(13)));
		filterArray.add(new FilterInfo(14,"lomoish",createEffect(14)));
		filterArray.add(new FilterInfo(15,"negative",createEffect(15)));
		filterArray.add(new FilterInfo(16,"posterize",createEffect(16)));
		filterArray.add(new FilterInfo(17,"rotate",createEffect(17)));
		filterArray.add(new FilterInfo(18,"saturate",createEffect(18)));
		filterArray.add(new FilterInfo(19,"sepia",createEffect(19)));
		filterArray.add(new FilterInfo(20,"sharpen",createEffect(20)));
		filterArray.add(new FilterInfo(21,"temperature",createEffect(21)));
		filterArray.add(new FilterInfo(22,"tint",createEffect(22)));
		filterArray.add(new FilterInfo(23,"vignette",createEffect(23)));

	}
	
	public Effect createEffect(int currentfilterID, EffectContext mEffectContext2) {
		// TODO Auto-generated method stub
		mEffectContext = mEffectContext2;
		return mEffect = createEffect(currentfilterID);
	}

	public Effect createEffect(int mCurrentEffect) {
		
		EffectFactory effectFactory = null;
		if (mEffectContext == null ) {
			try {
			mEffectContext = EffectContext.createWithCurrentGlContext();
			effectFactory = mEffectContext.getFactory();
			if (mEffect != null) {
				mEffect.release();
			}
			} catch (Exception e ) {
				Log.e("EffectException","create effect exception");
				e.printStackTrace();
			}
		}
		// Initialize the correct effect based on the selected menu/action item
		if (effectFactory == null ) {
			return null;
		}
		switch (mCurrentEffect) {

		case 0:
			break;

		case 1:
			mEffect = effectFactory.createEffect(EffectFactory.EFFECT_AUTOFIX);
			mEffect.setParameter("scale", 0.5f);
			break;

		case 2:
			mEffect = effectFactory
					.createEffect(EffectFactory.EFFECT_BLACKWHITE);
			mEffect.setParameter("black", .1f);
			mEffect.setParameter("white", .7f);
			break;

		case 3:
			mEffect = effectFactory
					.createEffect(EffectFactory.EFFECT_BRIGHTNESS);
			mEffect.setParameter("brightness", 2.0f);
			break;

		case 4:
			mEffect = effectFactory.createEffect(EffectFactory.EFFECT_CONTRAST);
			mEffect.setParameter("contrast", 1.4f);
			break;

		case 5:
			mEffect = effectFactory
					.createEffect(EffectFactory.EFFECT_CROSSPROCESS);
			break;

		case 6:
			mEffect = effectFactory
					.createEffect(EffectFactory.EFFECT_DOCUMENTARY);
			break;

		case 7:
			mEffect = effectFactory.createEffect(EffectFactory.EFFECT_DUOTONE);
			mEffect.setParameter("first_color", Color.YELLOW);
			mEffect.setParameter("second_color", Color.DKGRAY);
			break;

		case 8:
			mEffect = effectFactory
					.createEffect(EffectFactory.EFFECT_FILLLIGHT);
			mEffect.setParameter("strength", .8f);
			break;

		case 9:
			mEffect = effectFactory.createEffect(EffectFactory.EFFECT_FISHEYE);
			mEffect.setParameter("scale", .5f);
			break;

		case 10:
			mEffect = effectFactory.createEffect(EffectFactory.EFFECT_FLIP);
			mEffect.setParameter("vertical", true);
			break;

		case 11:
			mEffect = effectFactory.createEffect(EffectFactory.EFFECT_FLIP);
			mEffect.setParameter("horizontal", true);
			break;

		case 12:
			mEffect = effectFactory.createEffect(EffectFactory.EFFECT_GRAIN);
			mEffect.setParameter("strength", 1.0f);
			break;

		case 13:
			mEffect = effectFactory
					.createEffect(EffectFactory.EFFECT_GRAYSCALE);
			break;

		case 14:
			mEffect = effectFactory.createEffect(EffectFactory.EFFECT_LOMOISH);
			break;

		case 15:
			mEffect = effectFactory.createEffect(EffectFactory.EFFECT_NEGATIVE);
			break;

		case 16:
			mEffect = effectFactory
					.createEffect(EffectFactory.EFFECT_POSTERIZE);
			break;

		case 17:
			mEffect = effectFactory.createEffect(EffectFactory.EFFECT_ROTATE);
			mEffect.setParameter("angle", 180);
			break;

		case 18:
			mEffect = effectFactory.createEffect(EffectFactory.EFFECT_SATURATE);
			mEffect.setParameter("scale", .5f);
			break;

		case 19:
			mEffect = effectFactory.createEffect(EffectFactory.EFFECT_SEPIA);
			break;

		case 20:
			mEffect = effectFactory.createEffect(EffectFactory.EFFECT_SHARPEN);
			break;

		case 21:
			mEffect = effectFactory
					.createEffect(EffectFactory.EFFECT_TEMPERATURE);
			mEffect.setParameter("scale", .9f);
			break;

		case 22:
			mEffect = effectFactory.createEffect(EffectFactory.EFFECT_TINT);
			mEffect.setParameter("tint", Color.MAGENTA);
			break;

		case 23:
			mEffect = effectFactory.createEffect(EffectFactory.EFFECT_VIGNETTE);
			mEffect.setParameter("scale", .5f);
			break;

		default:
			break;
		}
		return mEffect;
	}

	private class FilterInfo {
		public int filterID;
		public String filterName;
		public Effect mEffect ;
		public int selectItem;

		public FilterInfo(int filterid, String filtername, Effect meffect) {
			this.filterID = filterid;
			this.filterName = filtername;
			this.mEffect = meffect;
		}
		
	}
	private List<FilterInfo> filterArray = new ArrayList<FilterInfo>();
	private int selectItem;

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return filterArray.size();
	}
	
	public String getFilterName(int position) {
		// TODO Auto-generated method stub
		return position < filterArray.size() ? filterArray.get(position).filterName
				: null;
	}
	
	public int getFilterID(int position) {
		return position < filterArray.size() ? filterArray.get(position).filterID
				: 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position < filterArray.size() ? filterArray.get(position).mEffect
				: null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		//int width = 180;
		//int height = 180;
		convertView = mInflater.inflate(R.layout.gallery_item_layout, null);
		FontManager.changeFonts(mContext, (LinearLayout)convertView.findViewById(R.id.root));
		ImageView imageView = null;
	    TextView textView = null;
        imageView = (ImageView)convertView.findViewById(R.id.image_item);
        textView = (TextView)convertView.findViewById(R.id.text_item);
	    imageView.setImageResource(R.drawable.filter_sample_normal);
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

}
