package com.stamp20.app.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import lenovo.jni.ImageUtils;
import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

import com.stamp20.app.R;
import com.stamp20.app.anim.AnimationUtil;
import com.stamp20.app.util.BitmapCache;
import com.stamp20.app.util.Constant;
import com.stamp20.app.util.Log;

/**
 * 只准上下移动
 */
public class ChooseRateStampView extends ZoomImageView {
    /**
     * 图片拖动状态常量
     */
    public static final int STATUS_ANIM_DOWN_OR_UP = 8001;
    private static final float sDampFactor = 0.25f;
    private static float yEdgeRate = 0.7f;
    private static float yStartUpAnimEdgeRate = 1.1f;
    private static float sMin =  -Float.MAX_VALUE + 1;
    private float mAnimDistance = sMin;
    private float mAnimStart = sMin;
    private ValueAnimator mDropDownAnimation = null;// mDropDownAnimation
    private ValueAnimator mFirstUpAnimation = null;// mDropDownAnimation
    
    private int mRateBitmapId = -1;
    private Bitmap mRateBitmap = null;
    public Bitmap getRateBitmap(){
        return mRateBitmap;
    }
    
    /*1025 650*/
    /* 790 500*/
    public void setRateBitmapId(final int position, final boolean isH){
        if(mRateBitmapId != position){
        	final int sizeOfRateBitmap = isH ? mHorizontalRateBitmap.size() : mVerticalRateBitmap.size();
        	ValueAnimator va = ValueAnimator.ofInt(0, sizeOfRateBitmap);
            va.setDuration(500);
            va.addUpdateListener(new AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                	int id = (Integer) animation.getAnimatedValue();
                	if(id != sizeOfRateBitmap){
                    	mRateBitmap = isH ? mHorizontalRateBitmap.get(id) : mVerticalRateBitmap.get(id);
                	}
                    invalidate();
                }
            });
            va.addListener(new AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {}

                @Override
                public void onAnimationRepeat(Animator animation) {}

                @Override
                public void onAnimationEnd(Animator animation) {
                    mRateBitmap = isH ? mHorizontalRateBitmap.get(position) : mVerticalRateBitmap.get(position);
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    mRateBitmap = isH ? mHorizontalRateBitmap.get(position) : mVerticalRateBitmap.get(position);
                }
            });
            va.start();
        }
        invalidate();
    }

    public ChooseRateStampView(Context context, AttributeSet attrs) {
        super(context, attrs);
        /*setupDropDownAnim();
        setupFirstUpAnim();*/
		
		setupDropDownAnim();
    }

    @Override
    protected void onTouchEventDoubleFingerMove(MotionEvent event) {
        // 空实现，不响应双指移动的事件
    }

    @Override
    protected void onTouchEventSingleFingerMove(MotionEvent event) {
        // 屏蔽X方向的移动处理
        // 只有单指按在屏幕上移动时，为拖动状态
        /* float xMove = event.getX(); */
        float yMove = event.getY();
        if (/* lastXMove == -1 && */lastYMove == -1) {
            /* lastXMove = xMove; */
            lastYMove = yMove;
        }
        currentStatus = STATUS_MOVE;
        /* movedDistanceX = xMove - lastXMove; */
        movedDistanceY = (yMove - lastYMove) * sDampFactor;
        // 进行边界检查，不允许将图片拖出边界
        /*
         * if (totalTranslateX + movedDistanceX > 0) { movedDistanceX = 0; }
         * else if (width - (totalTranslateX + movedDistanceX) >
         * currentBitmapWidth) { movedDistanceX = 0; }
         */
        if (totalTranslateY + movedDistanceY > 0 + height * yEdgeRate) {
            movedDistanceY = 0;
        } else if (height - (totalTranslateY + movedDistanceY) > currentBitmapHeight
                + height * yEdgeRate) {
            movedDistanceY = 0;
        }
        // 调用onDraw()方法绘制图片
        invalidate();
        /* lastXMove = xMove; */
        lastYMove = yMove;
    }

    @Override
    protected void onTouchEventSingleFingerUp(MotionEvent event) {
        super.onTouchEventSingleFingerUp(event);
        startDropDownAnim();
    }

    @Override
    protected void onTouchEventCancel(MotionEvent event) {
        // TODO Auto-generated method stub
        super.onTouchEventCancel(event);
        startDropDownAnim();
    }

    @Override
    protected void onFinishInit() {
        // TODO Auto-generated method stub
        super.onFinishInit();
        if(mFirstUpAnimation == null){
            mFirstUpAnimation = getTranslationValueAnimator(totalTranslateY, height * yEdgeRate, 800);
        }
        new Handler().postDelayed(new Runnable() { 
            @Override
            public void run() {
                // TODO Auto-generated method stub
                mFirstUpAnimation.start(); 
            }
        }, 200);
    }

    @Override
    protected void initBitmap(Canvas canvas) {
        /* 重写该方法，直接让bitmap居中显示 */
        if (sourceBitmap != null) {
            matrix.reset();
            int bitmapWidth = sourceBitmap.getWidth();
            int bitmapHeight = sourceBitmap.getHeight();
            // 当图片的宽高都小于屏幕宽高时，直接让图片居中显示
            float translateX = (width - sourceBitmap.getWidth()) / 2f;
            /* 初始让Y偏移到最底下 */
            float translateY = height * yStartUpAnimEdgeRate;// (height -
                                                  // sourceBitmap.getHeight()) /
                                                  // 2f;
            matrix.postTranslate(translateX, translateY);
            totalTranslateX = translateX;
            totalTranslateY = translateY;
            totalRatio = initRatio = 1f;
            currentBitmapWidth = bitmapWidth;
            currentBitmapHeight = bitmapHeight;
            canvas.drawBitmap(sourceBitmap, matrix, null);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (currentStatus == STATUS_ANIM_DOWN_OR_UP) {
            // 如果当前是执行DropDown的动画的话，则进入这个分支
            matrix.reset();
            // 直接根据最新计算的totalTranslateY进行位移，实现DropDown动画
            float translateX = totalTranslateX /* + movedDistanceX */;
            float translateY = totalTranslateY /* + movedDistanceY */;
            // 先按照已有的缩放比例对图片进行缩放
            matrix.postScale(totalRatio, totalRatio);
            // 再根据移动距离进行偏移
            matrix.postTranslate(translateX, translateY);
            totalTranslateX = translateX;
            totalTranslateY = translateY;
            canvas.drawBitmap(sourceBitmap, matrix, null);
        } else {
            super.onDraw(canvas);
        }
        drawRate(canvas);
    }

    private void drawRate(Canvas canvas){
        if(mRateBitmap == null) {
            return;
        }
        Matrix m = new Matrix();
        // 直接根据最新计算的totalTranslateY进行位移，实现DropDown动画
        float translateX = totalTranslateX /* + movedDistanceX */;
        float translateY = totalTranslateY /* + movedDistanceY */;
        // 先按照已有的缩放比例对图片进行缩放
        m.postScale(totalRatio, totalRatio);
        // 再根据移动距离进行偏移
        m.postTranslate(translateX + rateXMove * totalRatio, translateY + rateYMove * totalRatio);
        totalTranslateX = translateX;
        totalTranslateY = translateY;
        canvas.save();
        canvas.drawBitmap(mRateBitmap, m, null);
        canvas.restore();
    }
    
    @Override
    protected void onPreTouchEvent() {
        super.onPreTouchEvent();
        // 在OnTouchEvent时间前取消可能存在的DropDown动画
        stopDropDownAnim();
    }

    private ValueAnimator getTranslationValueAnimator(final float start, final float end, long duration) {
        ValueAnimator va = ValueAnimator.ofFloat(0f, 1f);
        va.setDuration(duration);
        va.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if(mAnimDistance == sMin){
                    mAnimDistance = end - start;
                    mAnimStart = start;
                }
                totalTranslateY = mAnimStart + mAnimDistance/*((height * yEdgeRate) - totalTranslateY)*/
                        * (Float) animation.getAnimatedValue();
                invalidate();
            }
        });
        va.addListener(new AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                currentStatus = STATUS_ANIM_DOWN_OR_UP;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                totalTranslateY = end;
                currentStatus = -1; 
                invalidate();
                mAnimDistance = sMin;
                mAnimStart = sMin;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                totalTranslateY = end;
                currentStatus = -1;
                invalidate();
                mAnimDistance = sMin;
                mAnimStart = sMin;
            }
        });
        return va;
    }
    
    private void setupDropDownAnim() {
        mDropDownAnimation = ValueAnimator.ofFloat(0f, 1f);
        mDropDownAnimation.setDuration(1500);
        mDropDownAnimation.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                totalTranslateY = totalTranslateY + ((height * yEdgeRate) - totalTranslateY)
                        * (Float) animation.getAnimatedValue();
                //android.util.Log.i("xixia", "(height * yEdgeRate):"+(height * yEdgeRate)+",totalTranslateY:"+totalTranslateY);
                if(totalTranslateY > (height * yEdgeRate)){
                    totalTranslateY = (height * yEdgeRate);
                }
                invalidate();
            }
        });
        mDropDownAnimation.addListener(new AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                currentStatus = STATUS_ANIM_DOWN_OR_UP;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                totalTranslateY = (float) (height * yEdgeRate);
                currentStatus = -1; 
                invalidate();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                totalTranslateY = (float) (height * yEdgeRate);
                currentStatus = -1;
                invalidate();
            }
        });
    }
    
    private void startDropDownAnim() {
        if(mDropDownAnimation == null){
            mDropDownAnimation = getTranslationValueAnimator(totalTranslateY, height * yEdgeRate, 500);
        }
        mDropDownAnimation.start();
    }

    private void stopDropDownAnim() {
        if(null == mDropDownAnimation){
            return;
        }
        if (mDropDownAnimation.isRunning()) {
            mDropDownAnimation.cancel();
        }
    }
    
    private List<Bitmap> mHorizontalRateBitmap;
    private List<Bitmap> mVerticalRateBitmap;
    public void startBuilRateBitmapTask(final boolean isStampViewIsHorizontal){
    	if(null == BitmapCache.getCache().get()){
        	android.util.Log.i("xixia", "BitmapCache.getCache().get() is null, return");
    		return;
    	}
    	final Resources res = this.getContext().getResources();
    	new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
            }

            /*
             * 第一个参数是doInBackground的输入参数
             * 第二个参数是用于输出中间计算进度的参数
             * 第三个参数是说明doInBackground的返回参数和onPostExecute的输入参数
             * */
            @Override
            protected Void doInBackground(Void... unused) {
            	TypedArray typedArray = res.obtainTypedArray(R.array.stamp_rate_horizontal);
                if( null != typedArray && isStampViewIsHorizontal){
                	mHorizontalRateBitmap = new ArrayList<Bitmap>();
                    Constant.LogXixia("pop", "Horizontal typedArray:"+typedArray.length());
                    for(int i=0; i<typedArray.length(); i++){
                    	int id = typedArray.getResourceId(i, 0);
                    	mHorizontalRateBitmap.add(buildRateBitmap(id));
                    }
                }
                
                typedArray = res.obtainTypedArray(R.array.stamp_rate_vertical);
                if( null != typedArray && !isStampViewIsHorizontal){
                	mVerticalRateBitmap = new ArrayList<Bitmap>();
                    Constant.LogXixia("pop", "Vertical typedArray:"+typedArray.length());
                    for(int i=0; i<typedArray.length(); i++){
                    	int id = typedArray.getResourceId(i, 0);
                    	mVerticalRateBitmap.add(buildRateBitmap(id));
                    }
                }
				return null;
            }
            
            @Override
            protected void onPostExecute(Void result) {
            }
        }.execute();
    }
    
    private int rateXMove = -1;
    private int rateYMove = -1;
    public int getRateXMove(){
    	return rateXMove;
    }
    public int getRateYMove(){
    	return rateYMove;
    }
    private Bitmap buildRateBitmap(int id){
    	final float scale = 790f / 1025f;
    	Bitmap rt = BitmapFactory.decodeResource(getResources(), id);
        Matrix matrix = new Matrix(); 
        matrix.postScale(scale, scale);
        rt = Bitmap.createBitmap(rt, 0, 0, 
								 rt.getWidth(), rt.getHeight(), 
                                 matrix,true);
        
        //scaleRateCanvas.drawBitmap(rt, 0.5f * (scaleRateBitmap.getWidth() - rt.getWidth()), 0.5f * (scaleRateBitmap.getHeight() - rt.getHeight()), null);
        //rt.recycle();
        if(rateXMove == -1 && rateYMove == -1){
        	rateXMove = (int) (0.5f * (BitmapCache.getCache().get().getWidth() - rt.getWidth()));
        	rateYMove = (int) (0.5f * (BitmapCache.getCache().get().getHeight() - rt.getHeight()));
        }
        return rt;
    }
    
}
