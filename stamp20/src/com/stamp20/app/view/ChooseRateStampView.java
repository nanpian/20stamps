package com.stamp20.app.view;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.stamp20.app.util.BitmapCache;

/**
 * 只准上下移动
 */
public class ChooseRateStampView extends ZoomImageView {
    /**
     * 图片拖动状态常量
     */
    public static final int STATUS_ANIM_DOWN = 8001;
    private static final float sDampFactor = 0.25f;
    private static float yEdgeRate = 0.2f;
    private ValueAnimator mDropDownAnimation;// mDropDownAnimation
    
    private int mRateBitmapId = -1;
    private Bitmap mRateBitmap = null;
    public Bitmap getRateBitmap(){
        return mRateBitmap;
    }
    
    /*1025 650*/
    /* 790 500*/
    public void setRateBitmapId(int id){
        if(mRateBitmapId != id){
            /*
             * 这是一个经验数据，因为StampView中使用的是这个图片（A）"background_stamp_h_transparent_pierced"
             * 德伟给的资源包中的数字替换资源的图片是（B）"background_stamp_h_rate_112"这样的图片
             * 而（B）对应的图片是这个（C）"Stamp_H_Final_Transparent@3x.png"图片
             * 
             * 仔细观察（A）和（C）
             * 可以发现两个图片中间显示邮票框的内容的长宽大概是(1025,,650)和(790,500);
             * 
             * 因此处理这个数字图片我是模拟StampView中处理邮票框的过程进行处理：
             * 
             * 1,首先将（B）图按照（A）和（C）之间的比例进行缩放。
             * 2,创建一个和StampView中（A）图进行绘图操作一样大小的空白位图,将（B）图居中绘制在其中
             * 3,得到的最终图片就是一个和StampView中的（A）图进行过相同处理的我们需要的图片
             * 
             * */
            float scale = 790f / 1025f;
            mRateBitmapId = id;
            mRateBitmap = BitmapFactory.decodeResource(getResources(), mRateBitmapId);
            Matrix matrix = new Matrix(); 
            matrix.postScale(scale, scale);
            mRateBitmap = Bitmap.createBitmap(mRateBitmap, 0, 0, 
                                            mRateBitmap.getWidth(), mRateBitmap.getHeight(), 
                                            matrix,true);
            
            Bitmap tmpCanvasBitmap = Bitmap.createBitmap(BitmapCache.getCache().get().getWidth(), 
                    BitmapCache.getCache().get().getHeight(), Bitmap.Config.ARGB_8888);
            Canvas tmpCanvas = new Canvas(tmpCanvasBitmap);
            tmpCanvas.drawBitmap(mRateBitmap, 0.5f * (tmpCanvasBitmap.getWidth() - mRateBitmap.getWidth()), 0.5f * (tmpCanvasBitmap.getHeight() - mRateBitmap.getHeight()), null);
            
            mRateBitmap = tmpCanvasBitmap;
        }
        invalidate();
    }

    public ChooseRateStampView(Context context, AttributeSet attrs) {
        super(context, attrs);
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
    protected void initBitmap(Canvas canvas) {
        /* 重写该方法，直接让bitmap居中显示 */
        if (sourceBitmap != null) {
            matrix.reset();
            int bitmapWidth = sourceBitmap.getWidth();
            int bitmapHeight = sourceBitmap.getHeight();
            // 当图片的宽高都小于屏幕宽高时，直接让图片居中显示
            float translateX = (width - sourceBitmap.getWidth()) / 2f;
            /* 初始让Y偏移到最底下 */
            float translateY = height * yEdgeRate;// (height -
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
        if (currentStatus == STATUS_ANIM_DOWN) {
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
        canvas.drawBitmap(mRateBitmap, matrix, null);
    }
    
    @Override
    protected void onPreTouchEvent() {
        super.onPreTouchEvent();
        // 在OnTouchEvent时间前取消可能存在的DropDown动画
        stopDropDownAnim();
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
                currentStatus = STATUS_ANIM_DOWN;
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
        mDropDownAnimation.start();
    }

    private void stopDropDownAnim() {
        if (mDropDownAnimation.isRunning()) {
            mDropDownAnimation.cancel();
        }
    }
}
