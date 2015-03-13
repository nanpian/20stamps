package com.stamp20.app.adapter;

import java.util.ArrayList;
import java.util.List;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.stamp20.app.R;
import com.stamp20.app.makeramen.RoundedImageView;

public class ChooseBackColorAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater;
	private Bitmap cardBackShape;
    private static int sCardsBackList[] = {R.drawable.card_back_white, 
        R.drawable.card_back_green,R.drawable.card_back_grey,R.drawable.card_back_lite_blue,R.drawable.card_back_lite_green,
        R.drawable.card_back_lite_grey, R.drawable.card_back_lite_orange, R.drawable.card_back_lite_red, R.drawable.card_back_orange,
        R.drawable.card_back_red}; 

	public ChooseBackColorAdapter(Context c) {
		mContext = c;
		mInflater = LayoutInflater.from(mContext);
		colorArray.add(new ColorArray(Color.WHITE));
		colorArray.add(new ColorArray(Color.parseColor("#008B00")));
		colorArray.add(new ColorArray(Color.parseColor("#EEE9BF")));
		colorArray.add(new ColorArray(Color.parseColor("#66CDAA")));
		colorArray.add(new ColorArray(Color.parseColor("#9ACD32")));
		colorArray.add(new ColorArray(Color.parseColor("#E0EEEE")));
		colorArray.add(new ColorArray(Color.parseColor("#EEE685")));
		colorArray.add(new ColorArray(Color.parseColor("#FFE4C4")));
		colorArray.add(new ColorArray(Color.parseColor("#EE9A00")));
		colorArray.add(new ColorArray(Color.parseColor("#DC143C")));
		this.cardBackShape = BitmapFactory.decodeResource(
				mContext.getResources(), R.drawable.activity_card_back_shape);
	}

	private List<ColorArray> colorArray = new ArrayList<ColorArray>();
	private int selectItem;
	public void setSelectItem (int selectId) {
		this.selectItem = selectId;
		notifyDataSetChanged();
	}

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
	
	public int getColor(int position) {
		// TODO Auto-generated method stub
		return position < colorArray.size() ? colorArray.get(position).color
				: null;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		convertView = mInflater.inflate(R.layout.gallery_choose_color_layout, null);
		// FontManager.changeFonts(mContext,
		// (LinearLayout)convertView.findViewById(R.id.root));
		RoundedImageView imageView = null;
		TextView textView = null;
		imageView = (RoundedImageView) convertView.findViewById(R.id.image_item);
		textView = (TextView) convertView.findViewById(R.id.text_item);
/*		Bitmap image = maskWithColor(cardBackShape,
				colorArray.get(position).color);*/
		//imageView.setImageBitmap(image);
		//imageView.setBackgroundColor(colorArray.get(position).color);
		imageView.setImageResource(sCardsBackList[position]);
		//image.recycle();
		if (selectItem == position) {
			// 处理点击放大效果，注意，这里还要加入边框效果，需要UI
			imageView.setBorderColor(Color.parseColor("#f1c40f"));
			ObjectAnimator xAnimator = ObjectAnimator.ofFloat(imageView, "scaleX", 1.1f);
			ObjectAnimator yAnimator = ObjectAnimator.ofFloat(imageView, "scaleY", 1.1f);
			xAnimator.setDuration(500);
			yAnimator.setDuration(500);
			AnimatorSet animatorSet = new AnimatorSet();
			animatorSet.play(xAnimator).with(yAnimator);
			animatorSet.start();
			//textView.setTextColor(Color.parseColor("#f1c40f"));
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
	
	public Bitmap maskWithTransparent(Bitmap cardBackBitmapSource ) {
		return cardBackBitmapSource;
	}
}
