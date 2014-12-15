/**
 * 
 */
package com.stamp20.app.view;

import com.stamp20.app.R;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * @author 作者 zhudewei
 * @version 创建时间：2014-12-13 下午2:35:33 类说明
 */
public class StampFrameView extends View {
	private Paint mPaint;
	private static final String Tag = "StampFrameView";
	private Bitmap bmpStampBackground = null;
	private Paint mStampBackgroundPaint;
	private StampPoints mStampBackgroundPoints;

	public StampFrameView(Context context) {
		super(context);
		Log.i(Tag, "init ");
		init();
	}

	public StampFrameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		Log.i(Tag, "init 2");
		init();
	}

	public StampFrameView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		Log.i(Tag, "init 3");
		init();
	}

	private void init() {
		Log.i(Tag, "init ");
		mStampBackgroundPoints = new StampPoints();
		bmpStampBackground = BitmapFactory.decodeResource(getResources(), R.drawable.background_stamp_h_transparent_pierced);
		bmpStampBackground = ImageUtil.bigBitMap(bmpStampBackground);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		Log.i(Tag, "the sf view layout");
		super.onLayout(changed, left, top, right, bottom);
	}

	public Bitmap getBmpStampBackground() {
		return bmpStampBackground;
	}

	public void setBmpStampBackground(Bitmap stampBackground) {
		bmpStampBackground = stampBackground;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		Log.i(Tag, "the sf view onmeasure");
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
		int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
		Log.i(Tag, "The width mode is " + widthMode + " The height mode is " + heightMode);
		Log.i(Tag, "The size of with is " + sizeWidth + " The size of height is " + sizeHeight);
		int bmpStampBackgroundWidth = bmpStampBackground.getWidth();
		int bmpStampBackgroundHeight = bmpStampBackground.getHeight();
		Log.d(Tag, "bmpStampBackgroundWidth:" + bmpStampBackgroundWidth + "bmpStampBackgroundHeight:" + bmpStampBackgroundHeight);
		mStampBackgroundPoints.setX(sizeWidth / 2 - bmpStampBackgroundWidth / 2);
		mStampBackgroundPoints.setY(sizeHeight / 2 - bmpStampBackgroundHeight / 2);
		Log.d(Tag, "getwidth width is " + getWidth() / 2 + " getheight height is " + getHeight() / 2);
		Log.d(Tag, "bmp set x " + (sizeWidth / 2 - bmpStampBackgroundWidth / 2));
		Log.d(Tag, "bmp set y " + (sizeHeight / 2 - bmpStampBackgroundHeight / 2));
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Log.i(Tag, "the sf view ondraw");
		super.onDraw(canvas);
		Log.i(Tag,"the sf view ondraw,location is "+mStampBackgroundPoints.getX() +" y is " +mStampBackgroundPoints.getY());
		canvas.drawBitmap(getBmpStampBackground(), mStampBackgroundPoints.getX(), mStampBackgroundPoints.getY(), mStampBackgroundPaint);
	}

}
