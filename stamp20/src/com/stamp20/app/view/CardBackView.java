package com.stamp20.app.view;

import com.stamp20.app.R;
import com.stamp20.app.util.Log;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewDebug.ExportedProperty;
import android.view.animation.Animation;

public class CardBackView extends View {

	private static final String Tag = "CardBackView";
	// 背景颜色
	public int cardBackColor;
	// 背景画笔
	Paint backgroundPaint = new Paint();
	// 整个view的宽度和高度
	private int cardBackWidth, cardBackHeight, logoWidth, logoHeight;
	// 这是背景形状图片
	private Bitmap cardBackShape, cardBackBitmap, cardBackLineBitmap,
			cardBackBottomLogo;
	// 第一次初始化
	private boolean mInitialized = true;
	private boolean isHasLine = false;
	public onMeasuredListener listener = null;

	public void setOnMeasuredListener(onMeasuredListener l) {
		this.listener = l;
	}

	public CardBackView(Context context) {
		super(context);
		initView();
	}

	public CardBackView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}
	
	public void setHasLine(boolean b) {
		isHasLine = b;
	}

	private void initView() {
		Log.i(Tag, "init View ");
		this.cardBackBitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.activity_card_back_shape);
		this.cardBackShape = BitmapFactory.decodeResource(getResources(),
				R.drawable.activity_card_back_shape);
		if (listener != null) {
			// listener.onMeasuredListener(cardBackShape.getWidth(),
			// cardBackShape.getHeight());
		}
		this.cardBackBottomLogo = BitmapFactory.decodeResource(getResources(),
				R.drawable.activity_card_back_bottom_logo);
		this.cardBackLineBitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.lines_back_small);
		logoWidth = cardBackBottomLogo.getWidth();
		logoHeight = cardBackBottomLogo.getHeight();
		if (cardBackBitmap != null) {

		}
		this.cardBackColor = Color.WHITE;

		this.cardBackBitmap = createWhiteBitmap(cardBackBitmap);
		
		// test color mask function
		//this.cardBackBitmap = maskWithColor(cardBackBitmap, Color.GREEN);
	}

	private Bitmap createWhiteBitmap(Bitmap cardBackBitmapSource) {
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
				//如果透明保持，不透明变为白色
                if(alpha<120) {
                	mArrayColor[count] = color;
                } else {
                	mArrayColor[count] = Color.WHITE;
                }
				count++;
			}
		}
		Bitmap newBmp = Bitmap.createBitmap(mBitmapWidth, mBitmapHeight, Config.ARGB_4444);  
	    newBmp.setPixels(mArrayColor, 0, mBitmapWidth, 0, 0, mBitmapWidth, mBitmapHeight); 
	    
	    
	    cardBackBitmap = newBmp;
		return newBmp;
	}
	
	public Bitmap maskWithColor(Bitmap cardBackBitmapSource, int maskcolor) {
		int mBitmapHeight = cardBackBitmapSource.getHeight();
		int mBitmapWidth = cardBackBitmapSource.getWidth();
		int mArrayColorLengh = mBitmapWidth * mBitmapHeight;
		int mArrayColor[] = new int[mArrayColorLengh];
		int count = 0;
		for (int i = 0; i < mBitmapHeight ; i++) {
			for (int j = 0; j < mBitmapWidth; j++) {
				// 获得Bitmap 图片中每一个点的color颜色值
				int color = cardBackBitmapSource.getPixel(j, i);
				// 将颜色值存在一个数组中 方便后面修改
				// 如果你想做的更细致的话 可以把颜色值的R G B 拿到做响应的处理 笔者在这里就不做更多解释
				int r = Color.red(color);
				int g = Color.green(color);
				int b = Color.blue(color);
				int alpha = Color.alpha(color);
				if (i > mBitmapHeight - logoHeight) {
					//如果透明保持，不透明变为白色
	                if(alpha<120) {
	                	mArrayColor[count] = color;
	                } else {
	                	mArrayColor[count] = Color.WHITE;
	                }
				} else {
					//如果透明保持，不透明变为白色
	                if(alpha<120) {
	                	mArrayColor[count] = color;
	                } else {
	                	mArrayColor[count] = maskcolor;
	                }
				}
				count++;
			}
		}
		Bitmap newBmp = Bitmap.createBitmap(mBitmapWidth, mBitmapHeight, Config.ARGB_4444);  
	    newBmp.setPixels(mArrayColor, 0, mBitmapWidth, 0, 0, mBitmapWidth, mBitmapHeight);  
	    mInitialized = true;
		return newBmp;
	}

	public void setCardBackLine(boolean istrue, Bitmap cardBackLineBitmap) {

	}

	public void setCardBackColor(int cardcolor) {
		this.cardBackColor = cardcolor;
		maskWithColor(cardBackBitmap, cardcolor);
	}

	public void setCardBackBitmap(Bitmap cardBitmap) {
		this.cardBackBitmap = cardBitmap;
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		Log.d(this, "onLayout...");
		if (changed) {
			// 分别获取到当前view的宽度和高度
			// 分别获取到ZoomImageView的宽度和高度
			cardBackWidth = getWidth();
			cardBackHeight = getHeight();
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (mInitialized) {
			Paint paintShape = new Paint();
			// 构建ShapeDrawable对象并定义形状为椭圆
			paintShape.setColor(cardBackColor);
			int left = (cardBackWidth - logoWidth) / 2;
			int top = (cardBackHeight - cardBackBitmap.getHeight()) / 2
					+ cardBackBitmap.getHeight() - logoHeight;
			// draw the shape background
			canvas.drawBitmap(cardBackBitmap,
					(cardBackWidth - cardBackBitmap.getWidth()) / 2,
					(cardBackHeight - cardBackBitmap.getHeight()) / 2,
					paintShape);
			// canvas.drawBitmap(cardBackShape, null, paintShape);
			// draw the bottom 20stamp logo ,first we get the location the logo
			// should be
			canvas.drawBitmap(cardBackBottomLogo, left, top, null);
			
			if (isHasLine) {
				canvas.drawBitmap(cardBackLineBitmap, (cardBackWidth - cardBackLineBitmap.getWidth()) / 2, 20, null);
			}
			
			mInitialized = false;
		} else {

		}
	}

	public interface onMeasuredListener {
		public void onMeasuredListener(int width, int height);
	}

}
