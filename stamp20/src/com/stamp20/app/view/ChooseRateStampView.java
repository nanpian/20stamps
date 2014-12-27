package com.stamp20.app.view;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.CycleInterpolator;

import com.paypal.android.sdk.d;
import com.stamp20.app.util.BitmapCache;
import com.stamp20.app.util.Log;

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
    protected void onTouchEventSigleFingerUp(MotionEvent event) {
        super.onTouchEventSigleFingerUp(event);
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

    /*
     * private int width; private int height; private Bitmap stampBitmap;
     * private Matrix matrix = new Matrix(); private int totalTranslateY;
     * 
     * private int defaultTranslateY = 50;
     * 
     * private static int MSG_TOUCH_EVENT_ACTION_UP = 100; private static int
     * MSG_INIT_VIEW = 101;
     * 
     * private int mode; private int MODE_NONE = 0; private int MODE_GRAG = 1;
     * 
     * public ChooseRateStampView(Context context, AttributeSet attrs) {
     * super(context, attrs); Log.d(this, "ChooseRateStampView"); stampBitmap =
     * BitmapCache.getCache().get(); matrix.reset(); totalTranslateY = 250;
     * matrix.postTranslate(0, totalTranslateY); mode = MODE_NONE; }
     * 
     * private Handler mHandler = new Handler() {
     * 
     * @Override public void handleMessage(Message msg) {
     * super.handleMessage(msg); if (msg.what == MSG_TOUCH_EVENT_ACTION_UP) {
     * 
     * MotionEvent event = (MotionEvent) msg.obj; if (movedDistanceY == 0) {
     * handleStop(); } else {
     * mHandler.sendMessageDelayed(mHandler.obtainMessage(
     * MSG_TOUCH_EVENT_ACTION_UP, event), 100); movedDistanceY = 0; } } else if
     * (msg.what == MSG_INIT_VIEW) { removeMessages(MSG_INIT_VIEW);
     * matrix.reset(); if (Math.abs(totalTranslateY - defaultTranslateY) < 5) {
     * totalTranslateY = defaultTranslateY; } else if (totalTranslateY <
     * defaultTranslateY) { totalTranslateY += 5; } else { totalTranslateY -= 5;
     * } matrix.postTranslate(0, totalTranslateY); Log.d(this, "MSG_INIT_VIEW" +
     * totalTranslateY); postInvalidate();
     * 
     * }
     * 
     * }
     * 
     * };
     * 
     * private void initView() { if (totalTranslateY == defaultTranslateY) {
     * return; } else { totalTranslateY--; matrix.reset();
     * matrix.postTranslate(0, totalTranslateY);
     * mHandler.sendEmptyMessageDelayed(MSG_INIT_VIEW, 10); } }
     * 
     * public void setBmpStampPhoto(Bitmap stampBitmap) { Log.d(this,
     * "setBmpStampPhoto"); this.stampBitmap = stampBitmap; }
     * 
     * @Override protected void onLayout(boolean changed, int left, int top, int
     * right, int bottom) { Log.d(this, "onLayout"); super.onLayout(changed,
     * left, top, right, bottom); if (changed) { width = getWidth(); height =
     * getHeight(); } }
     * 
     * @Override protected void onDraw(Canvas canvas) { Log.d(this, "onDraw..."
     * + totalTranslateY); canvas.save(); canvas.drawBitmap(stampBitmap, matrix,
     * null); canvas.restore(); // initView(); if (mode == MODE_NONE &&
     * totalTranslateY != defaultTranslateY) {
     * mHandler.sendEmptyMessageDelayed(MSG_INIT_VIEW, 5); } }
     * 
     * private void handleStop() { matrix.reset(); matrix.postTranslate(0,
     * totalTranslateY); invalidate(); }
     * 
     * float lastYMove = -1; float movedDistanceY;
     * 
     * @Override public boolean onTouchEvent(MotionEvent event) { switch
     * (event.getActionMasked()) { case MotionEvent.ACTION_DOWN: break; case
     * MotionEvent.ACTION_MOVE: mode = MODE_GRAG; float y = event.getY(); if
     * (lastYMove == -1) { lastYMove = y; } movedDistanceY = y - lastYMove;
     * Log.d(this, "movedDistanceY:" + movedDistanceY); // 移动手指滑动距离的1/3
     * movedDistanceY = movedDistanceY / 3; float translateY = totalTranslateY +
     * movedDistanceY; matrix.reset(); matrix.postTranslate(0, translateY);
     * invalidate(); lastYMove = y; totalTranslateY = (int) translateY; break;
     * case MotionEvent.ACTION_UP: Log.d(this, "up....."); lastYMove = -1; mode
     * = MODE_NONE; // totalTranslateY = defaultTranslateY; // matrix.reset();
     * // matrix.postTranslate(0, totalTranslateY); // // //
     * mHandler.sendMessageDelayed
     * (mHandler.obtainMessage(MSG_TOUCH_EVENT_ACTION_UP, // // event), 100);
     * invalidate(); break; } return true; }
     */
}
