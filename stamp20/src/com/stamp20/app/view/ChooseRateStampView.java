package com.stamp20.app.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.paypal.android.sdk.d;
import com.stamp20.app.util.BitmapCache;
import com.stamp20.app.util.Log;

public class ChooseRateStampView extends View {

    private int width;
    private int height;
    private Bitmap stampBitmap;
    private Matrix matrix = new Matrix();
    private int totalTranslateY;

    private int defaultTranslateY = 50;

    private static int MSG_TOUCH_EVENT_ACTION_UP = 100;
    private static int MSG_INIT_VIEW = 101;

    private int mode;
    private int MODE_NONE = 0;
    private int MODE_GRAG = 1;

    public ChooseRateStampView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.d(this, "ChooseRateStampView");
        stampBitmap = BitmapCache.getCache().get();
        matrix.reset();
        totalTranslateY = 250;
        matrix.postTranslate(0, totalTranslateY);
        mode = MODE_NONE;
    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG_TOUCH_EVENT_ACTION_UP) {

                MotionEvent event = (MotionEvent) msg.obj;
                if (movedDistanceY == 0) {
                    handleStop();
                } else {
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_TOUCH_EVENT_ACTION_UP, event), 100);
                    movedDistanceY = 0;
                }
            } else if (msg.what == MSG_INIT_VIEW) {
                removeMessages(MSG_INIT_VIEW);
                matrix.reset();
                if (Math.abs(totalTranslateY - defaultTranslateY) < 5) {
                    totalTranslateY = defaultTranslateY;
                } else if (totalTranslateY < defaultTranslateY) {
                    totalTranslateY += 5;
                } else {
                    totalTranslateY -= 5;
                }
                matrix.postTranslate(0, totalTranslateY);
                Log.d(this, "MSG_INIT_VIEW" + totalTranslateY);
                postInvalidate();

            }

        }

    };

    private void initView() {
        if (totalTranslateY == defaultTranslateY) {
            return;
        } else {
            totalTranslateY--;
            matrix.reset();
            matrix.postTranslate(0, totalTranslateY);
            mHandler.sendEmptyMessageDelayed(MSG_INIT_VIEW, 10);
        }
    }

    public void setBmpStampPhoto(Bitmap stampBitmap) {
        Log.d(this, "setBmpStampPhoto");
        this.stampBitmap = stampBitmap;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        Log.d(this, "onLayout");
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            width = getWidth();
            height = getHeight();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d(this, "onDraw..." + totalTranslateY);
        canvas.save();
        canvas.drawBitmap(stampBitmap, matrix, null);
        canvas.restore();
        // initView();
        if (mode == MODE_NONE && totalTranslateY != defaultTranslateY) {
            mHandler.sendEmptyMessageDelayed(MSG_INIT_VIEW, 5);
        }
    }

    private void handleStop() {
        matrix.reset();
        matrix.postTranslate(0, totalTranslateY);
        invalidate();
    }

    float lastYMove = -1;
    float movedDistanceY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
        case MotionEvent.ACTION_DOWN:
            break;
        case MotionEvent.ACTION_MOVE:
            mode = MODE_GRAG;
            float y = event.getY();
            if (lastYMove == -1) {
                lastYMove = y;
            }
            movedDistanceY = y - lastYMove;
            Log.d(this, "movedDistanceY:" + movedDistanceY);
            // 移动手指滑动距离的1/3
            movedDistanceY = movedDistanceY / 3;
            float translateY = totalTranslateY + movedDistanceY;
            matrix.reset();
            matrix.postTranslate(0, translateY);
            invalidate();
            lastYMove = y;
            totalTranslateY = (int) translateY;
            break;
        case MotionEvent.ACTION_UP:
            Log.d(this, "up.....");
            lastYMove = -1;
            mode = MODE_NONE;
            // totalTranslateY = defaultTranslateY;
            // matrix.reset();
            // matrix.postTranslate(0, totalTranslateY);
            // //
            // mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_TOUCH_EVENT_ACTION_UP,
            // // event), 100);
            invalidate();
            break;
        }
        return true;
    }
}
