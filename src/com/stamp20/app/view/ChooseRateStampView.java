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

import com.stamp20.app.util.BitmapCache;
import com.stamp20.app.util.Log;

public class ChooseRateStampView extends View {

    private int width;
    private int height;
    private Bitmap stampBitmap;
    private Matrix matrix = new Matrix();

    private static int MSG_TOUCH_EVENT_ACTION_UP = 100;

    public ChooseRateStampView(Context context, AttributeSet attrs) {
        super(context, attrs);
        stampBitmap = BitmapCache.getCache().get();
        matrix.reset();
        matrix.postTranslate(0, 0);
    }

    public void setBmpStampPhoto(Bitmap stampBitmap) {
        this.stampBitmap = stampBitmap;
        invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.d(this, "onLayout...");
        if (changed) {
            width = getWidth();
            height = getHeight();
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        canvas.drawBitmap(stampBitmap, matrix, null);
        canvas.restore();
    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG_TOUCH_EVENT_ACTION_UP) {

                MotionEvent event = (MotionEvent) msg.obj;
                if(movedDistanceY == 0){
                    handleStop();
                } else {
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_TOUCH_EVENT_ACTION_UP, event), 100);
                    movedDistanceY = 0;
                }
            }

        }

    };
    
    private void handleStop(){
        matrix.reset();
        matrix.postTranslate(0, 0);
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
            float y = event.getY();
            if (lastYMove == -1) {
                lastYMove = y;
            }
            movedDistanceY = y - lastYMove;
            Log.d(this, "movedDistanceY:" + movedDistanceY);
            //移动手指滑动距离的1/3
            movedDistanceY = movedDistanceY/4;
            matrix.postTranslate(0, movedDistanceY);
            invalidate();
            lastYMove = y;
            break;
        case MotionEvent.ACTION_UP:
            Log.d(this, "up.....");
            lastYMove = -1;
            mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_TOUCH_EVENT_ACTION_UP, event), 100);
            break;
        }
        return true;
    }
}
