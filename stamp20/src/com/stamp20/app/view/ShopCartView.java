package com.stamp20.app.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.stamp20.app.R;
import com.stamp20.app.util.Log;

public class ShopCartView extends View {

	private static final String TAG = "ShopCartView";
	
	private Bitmap bmpStampBackground;
	private Bitmap bmpStampSource;
	private int stampBackgroundWidth;
	private int stampBackgroundHeight;
	private int shopViewWidth;
	private int shopViewHeight;
	private int sourceWidth;
	private int sourceHeight;
	private Matrix matrix = new Matrix();
	private Matrix backgroundMatrix = new Matrix();
	private Paint backgroundPaint = new Paint();
	private Rect stampShowRect = new Rect();
	
	public ShopCartView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView();
	}

	public ShopCartView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initView();
	}

	public ShopCartView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		initView();
	}
	
	public void initView(){
		TypedArray typeArray = getResources().obtainTypedArray(R.array.shop_cart_background);
		int random = (int)(Math.random()*2);
		setBackgroundResource(typeArray.getResourceId(random, 0));
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		shopViewWidth = MeasureSpec.getSize(widthMeasureSpec);
		shopViewHeight = MeasureSpec.getSize(heightMeasureSpec);
		
		initMatrix();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		
//		if(rectBackground != null){
//		    canvas.drawRect(rectBackground, backgroundPaint);	
//		}
		//显示时可以根据框子和原始图的比例进行下缩放
		canvas.save();
		if(bmpStampSource != null && matrix != null){
		//	canvas.clipRect(stampShowRect);
		    canvas.drawBitmap(bmpStampSource, matrix, backgroundPaint);
		}
		canvas.restore();
		if(bmpStampBackground != null && backgroundMatrix != null){
			canvas.drawBitmap(bmpStampBackground, backgroundMatrix, backgroundPaint);
		}
	}

	public void setBackgroundResource(int resId){
		bmpStampBackground = BitmapFactory.decodeResource(getResources(), resId);
		stampBackgroundWidth = bmpStampBackground.getWidth();
		stampBackgroundHeight = bmpStampBackground.getHeight();
	}
	
	public void initMatrix(){
        backgroundMatrix.reset();
        float xRate = (shopViewWidth/(float)stampBackgroundWidth);
        float yRate = (shopViewHeight/(float)stampBackgroundHeight);
        float newBackgroundWidth = (stampBackgroundWidth*xRate);
        float newBackgroundHeight = (stampBackgroundHeight*yRate);
	    float stampBackgroundTranslateX = (shopViewWidth - newBackgroundWidth) / 2f;
	    float stampBackgroundTranslateY = (shopViewHeight - newBackgroundHeight) / 2f;
	    Log.i(TAG, "sourceWidth is : " + sourceWidth );
	    Log.i(TAG, "sourceHeight is : " + sourceHeight);
	    Log.i(TAG, "stampBackgroundHeight is : " + stampBackgroundHeight);
	    Log.i(TAG, "stampBackgroundWidth is : " + stampBackgroundWidth);
	    backgroundPaint.setColor(Color.RED);
	    backgroundPaint.setAntiAlias(true);
	    //缩放和移动
	    backgroundMatrix.postScale(xRate, yRate);
	    backgroundMatrix.postTranslate(stampBackgroundTranslateX, stampBackgroundTranslateY);
	    matrix.reset();
	    xRate = (newBackgroundWidth / (sourceWidth * 1.0f));
	    yRate = (newBackgroundHeight / (sourceHeight * 1.0f));
	    matrix.postScale(xRate, yRate);
	    float newSourceWidth = sourceWidth*xRate;
	    float newSourceHeight = sourceHeight*yRate;
	    float sourceTranslateX = (newBackgroundWidth - newSourceWidth) / 2f;
	    float sourceTranslateY = (newBackgroundHeight - newSourceHeight) / 2f;
	    matrix.postTranslate(sourceTranslateX, sourceTranslateY);
	}
	public Bitmap getmBmpStampBackground() {
		return bmpStampBackground;
	}

	public void setmBmpStampBackground(Bitmap bmpStampBackground) {
		this.bmpStampBackground = bmpStampBackground;
	}

	public Bitmap getmBpStampSource() {
		return bmpStampSource;
	}

	public void setmBpStampSource(Bitmap bmpStampSource) {
		this.bmpStampSource = bmpStampSource;
		this.sourceWidth = bmpStampSource.getWidth();
		this.sourceHeight = bmpStampSource.getHeight();
		invalidate();
	}


}
