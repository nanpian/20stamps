package com.stamp20.app.adapter;

import java.util.ArrayList;
import java.util.List;

import com.stamp20.app.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Bitmap.Config;
import android.media.effect.Effect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ChooseBackColorAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater;
	private Bitmap cardBackShape;

	public ChooseBackColorAdapter(Context c) {
		mContext = c;
		mInflater = LayoutInflater.from(mContext);
		colorArray.add(new ColorArray(Color.WHITE));
		colorArray.add(new ColorArray(Color.BLUE));
		colorArray.add(new ColorArray(Color.GRAY));
		colorArray.add(new ColorArray(Color.GREEN));
		colorArray.add(new ColorArray(Color.YELLOW));
		colorArray.add(new ColorArray(Color.DKGRAY));
		colorArray.add(new ColorArray(Color.LTGRAY));
		this.cardBackShape = BitmapFactory.decodeResource(
				mContext.getResources(), R.drawable.activity_card_back_shape);
	}

	private List<ColorArray> colorArray = new ArrayList<ColorArray>();
	private int selectItem;

	private class ColorArray {
		int color; // 颜色

		public ColorArray(int colorxx) {
			this.color = colorxx;
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return colorArray.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position < colorArray.size() ? colorArray.get(position).color
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
		convertView = mInflater.inflate(R.layout.gallery_choose_color_layout, null);
		// FontManager.changeFonts(mContext,
		// (LinearLayout)convertView.findViewById(R.id.root));
		ImageView imageView = null;
		TextView textView = null;
		imageView = (ImageView) convertView.findViewById(R.id.image_item);
		textView = (TextView) convertView.findViewById(R.id.text_item);
		Bitmap image = maskWithColor(cardBackShape,
				colorArray.get(position).color);
		imageView.setImageBitmap(image);
		//image.recycle();
		if (selectItem == position) {
			// 处理点击放大效果，注意，这里还要加入边框效果，需要UI
			// Animation animation = AnimationUtils.loadAnimation(mContext,
			// R.anim.gallery_click_scale);
			// imageView.startAnimation(animation);
			// imageView.setLayoutParams(new
			// LinearLayout.LayoutParams(200,200));
			textView.setTextColor(Color.parseColor("#f1c40f"));
			// imageView.setBackgroundResource(R.drawable.bg_filter_item_selected);
		} else {
			// imageView.setLayoutParams(new LinearLayout.LayoutParams(width,
			// height));
			textView.setTextColor(Color.parseColor("#ffffff"));
			// imageView.setBackgroundResource(R.drawable.bg_filter_item_selected_no);
		}
		// imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
		// textView.setText(colorArray.get(position).color);
		return convertView;
	}

	public Bitmap maskWithColor(Bitmap cardBackBitmapSource, int maskcolor) {
		int mBitmapHeight = cardBackBitmapSource.getHeight();
		int mBitmapWidth = cardBackBitmapSource.getWidth();
		int mArrayColorLengh = mBitmapWidth * mBitmapHeight;
		int mArrayColor[] = new int[mArrayColorLengh];
		int count = 0;
		for (int i = 0; i < mBitmapHeight; i++) {
			for (int j = 0; j < mBitmapWidth; j++) {
				// 获得Bitmap 图片中每一个点的color颜色值
				int color = cardBackBitmapSource.getPixel(j, i);
				// 将颜色值存在一个数组中 方便后面修改
				// 如果你想做的更细致的话 可以把颜色值的R G B 拿到做响应的处理 笔者在这里就不做更多解释
				int r = Color.red(color);
				int g = Color.green(color);
				int b = Color.blue(color);
				int alpha = Color.alpha(color);
				// 如果透明保持，不透明变为白色
				if (alpha < 120) {
					mArrayColor[count] = color;
				} else {
					mArrayColor[count] = maskcolor;

				}
				count++;
			}
		}
		Bitmap newBmp = Bitmap.createBitmap(mBitmapWidth, mBitmapHeight,
				Config.ARGB_4444);
		newBmp.setPixels(mArrayColor, 0, mBitmapWidth, 0, 0, mBitmapWidth,
				mBitmapHeight);
		return newBmp;
	}
}
