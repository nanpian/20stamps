package com.stamp20.app.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;

import com.stamp20.app.R;
import com.stamp20.app.util.Log;

public class StampView extends SurfaceView implements Callback, OnTouchListener {

    private Bitmap bmpStampBackground = null;
    private Bitmap bmpStamp = null;
    private Bitmap bmpBtnReversal = null;

    private StampPoints mStampBackgroundPoints = null;
    private StampPoints mStampPoints = null;
    private StampPoints mBtnReversalPoints = null;
    private boolean isHorizontal = true;

    private Paint mStampBackgroundPaint = null;
    private Paint mStampPaint = null;

    private Matrix mStampBackgroundMatrix;
    private Matrix mStampMatrix;
    private Matrix zoomMatrix;
    float sx;
    float sy;

    private Rect mStampRect;
    private Rect mStampBackgroundRect;
    private Rect mStampCenterRect;

    private int stampViewCanvasColor = Color.parseColor(StampViewConstants.COLOR_BACKGROUND_GRAY);

    private int NONE = 0;
    private int DRAG = 1;
    private int ZOOM = 2;
    private int mode = NONE;
    private boolean isBtnReversalClicked = false;

    private static final int MSG_ROTATE_STAMP_VIEW = 1100;

    public StampView(Context context) {
        super(context);
        init();
    }

    public StampView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public StampView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        getHolder().addCallback(this);
        Resources res = getResources();
        bmpStampBackground = BitmapFactory.decodeResource(res, R.drawable.background_stamp_h_transparent_pierced);
        bmpBtnReversal = BitmapFactory.decodeResource(res, R.drawable.icon_rotation_left);

        mStampBackgroundPoints = new StampPoints();
        mStampPoints = new StampPoints();
        mBtnReversalPoints = new StampPoints();

        mStampBackgroundPaint = new Paint();
        mStampPaint = new Paint();

        mStampBackgroundMatrix = new Matrix();
        mStampMatrix = new Matrix();

        mStampRect = new Rect();
        mStampBackgroundRect = new Rect();
        mStampCenterRect = new Rect();

        zoomMatrix = new Matrix();
        sx = 1.0f;
        sy = 1.0f;// 默认设置缩放为1
        zoomMatrix.postScale(sx, sy);

        setOnTouchListener(this);
    }

    public Bitmap getBmpStampBackground() {
        return bmpStampBackground;
    }

    public void setBmpStampBackground(Bitmap bmpStampBackground) {
        this.bmpStampBackground = bmpStampBackground;
    }

    public void setBmpStampBackground(int resId) {
        this.bmpStampBackground = null;
        Resources res = getResources();
        this.bmpStampBackground = BitmapFactory.decodeResource(res, resId);
    }

    public Bitmap getBmpStamp() {
        return bmpStamp;
    }

    public void setBmpStamp(Bitmap bmpStamp) {
        this.bmpStamp = bmpStamp;
        sx = (float) getBmpStampBackground().getWidth() / bmpStamp.getWidth();
        sy = (float) getBmpStampBackground().getHeight() / bmpStamp.getHeight();
        zoomMatrix.postScale(sx, sy);
    }

    public int getStampViewCanvasColor() {
        return stampViewCanvasColor;
    }

    public void setStampViewCanvasColor(int stampViewCanvasColor) {
        this.stampViewCanvasColor = stampViewCanvasColor;
    }

    public boolean isHorizontal() {
        return isHorizontal;
    }

    public void setHorizontal(boolean isHorizontal) {
        this.isHorizontal = isHorizontal;
    }

    public Bitmap getBmpBtnReversal() {
        return bmpBtnReversal;
    }

    public void setBmpBtnReversal(Bitmap bmpBtnReversal) {
        this.bmpBtnReversal = bmpBtnReversal;
    }

    public void setBmpBtnReversal(int resId) {
        Resources res = getResources();
        this.bmpBtnReversal = BitmapFactory.decodeResource(res, resId);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d(this, "onMeasure()...");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d(this, "width:" + getWidth() + ", height:" + getHeight());
        int bmpStampBackgroundWidth = bmpStampBackground.getWidth();
        int bmpStampBackgroundHeight = bmpStampBackground.getHeight();
        Log.d(this, "bmpStampBackgroundWidth:" + bmpStampBackgroundWidth + "bmpStampBackgroundHeight:" + bmpStampBackgroundHeight);
        mStampBackgroundPoints.setX(getWidth() / 2 - bmpStampBackgroundWidth / 2);
        mStampBackgroundPoints.setY(getHeight() / 2 - bmpStampBackgroundHeight / 2);
        mStampPoints.setX(mStampBackgroundPoints.getX() + 20);
        mStampPoints.setY(mStampBackgroundPoints.getY() + 20);
        mBtnReversalPoints.setX(mStampBackgroundPoints.getX() + bmpStampBackgroundWidth - bmpBtnReversal.getWidth() / 2);
        mBtnReversalPoints.setY(mStampBackgroundPoints.getY() - bmpBtnReversal.getHeight() / 2);

        int left = getWidth() / 2 - bmpStampBackgroundWidth / 2;
        int top = getHeight() / 2 - bmpStampBackgroundHeight / 2;
        int right = getWidth() - left;
        int bottom = getHeight() - top;

        mStampBackgroundRect.set(left, top, right, bottom);
        mStampRect.set(left, top, right, bottom);
        mStampCenterRect.set(160, 300, 500, 550);

    }

    public void draw(Bitmap bitmap) {
        Canvas canvas = getHolder().lockCanvas();
        if (canvas != null) {
            canvas.save();
            canvas.drawColor(getStampViewCanvasColor());

            if (bitmap != null) {
                // 画出邮票后的白色背景，此处应替换为与邮票背景一样的白色图案
                Paint p = new Paint();
                p.setColor(Color.WHITE);
                canvas.drawRect(mStampCenterRect, p);

                // Matrix matrix = new Matrix();
                // float sx = (float) getBmpStampBackground().getWidth()/
                // bitmap.getWidth();
                // float sy = (float) getBmpStampBackground().getHeight()/
                // bitmap.getHeight();
                // matrix.postScale(Math.min(sx, sy), Math.min(sx, sy));
                int w = bitmap.getWidth();
                int h = bitmap.getHeight();
                Bitmap zoomBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), zoomMatrix, false);
                int left = (int) mStampPoints.getX();
                int top = (int) mStampPoints.getY();
                int right = (int) (mStampPoints.getX() + zoomBitmap.getWidth());
                int bottom = (int) (mStampPoints.getY() + zoomBitmap.getHeight());
                mStampRect.set(left, top, right, bottom);
                if (mode == DRAG) {
                    canvas.drawBitmap(zoomBitmap, mStampPoints.getX(), mStampPoints.getY(), mStampPaint);
                } else if (mode == ZOOM) {

                    canvas.drawBitmap(zoomBitmap, mStampPoints.getX(), mStampPoints.getY(), mStampPaint);
                }
                // 画出邮票中间部分，让图片在中间区域不透明
                canvas.save();
                canvas.clipRect(mStampCenterRect);
                mStampPaint.setAlpha(StampViewConstants.PAINT_NO_TRANSPRANT);
                canvas.drawBitmap(zoomBitmap, mStampPoints.getX(), mStampPoints.getY(), mStampPaint);
                canvas.restore();
            }

            // 画出最上层的邮票框
            canvas.drawBitmap(getBmpStampBackground(), mStampBackgroundPoints.getX(), mStampBackgroundPoints.getY(), mStampBackgroundPaint);
            // 画出右上角旋转按钮
            canvas.drawBitmap(getBmpBtnReversal(), mBtnReversalPoints.getX(), mBtnReversalPoints.getY(), mStampBackgroundPaint);
            canvas.restore();
            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(this, "surfaceCreated()...");
        draw(getBmpStamp());
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d(this, "surfaceChanged()...");
        draw(getBmpStamp());
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(this, "surfaceDestroyed()...");
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
        // 处理单点事件
        case MotionEvent.ACTION_DOWN:
            mode = DRAG;
            onTouchDown(event);
            break;
        // 处理多点事件
        case MotionEvent.ACTION_POINTER_DOWN:
            lastDistance = -1;
            mode = ZOOM;
            break;
        case MotionEvent.ACTION_MOVE:
            if (!isBtnReversalClicked) {
                onTouchMove(event);
            }
            break;

        case MotionEvent.ACTION_UP:
        case MotionEvent.ACTION_POINTER_UP:
            mode = NONE;
            onTouchUp();
            break;

        default:
            break;
        }
        return true;
    }

    private void onTouchUp() {
        Log.d(this, "onTouchUp");
        if (isBtnReversalClicked) {
            // 旋转画面
            setHorizontal(!isHorizontal);
            rotateStampView();
        } else {
            mStampBackgroundPaint.setAlpha(StampViewConstants.PAINT_NO_TRANSPRANT);
            mStampPaint.setAlpha(StampViewConstants.PAINT_NO_TRANSPRANT);
            if (isHorizontal()) {
                setBmpStampBackground(R.drawable.background_stamp_h_transparent_pierced);
            } else {
                setBmpStampBackground(R.drawable.background_stamp_v_transparent_pierced);
            }
            setStampViewCanvasColor(Color.parseColor(StampViewConstants.COLOR_BACKGROUND_GRAY));
            lastDistance = -1;
            draw(getBmpStamp());
        }
    }

    private void rotateStampView() {
        Log.d(this, "rotateStampView");
        if (isHorizontal()) {
            setBmpStampBackground(R.drawable.background_stamp_h_transparent_pierced);
            setBmpBtnReversal(R.drawable.icon_rotation_left);
        } else {
            setBmpStampBackground(R.drawable.background_stamp_v_transparent_pierced);
            setBmpBtnReversal(R.drawable.icon_rotation_right);
        }
        drawRotateView();
        // mHandler.sendEmptyMessageDelayed(MSG_ROTATE_STAMP_VIEW, 3000);

    }

    private void drawRotateView() {
        Log.d(this, "width:" + getWidth() + ", height:" + getHeight());
        int bmpStampBackgroundWidth = bmpStampBackground.getWidth();
        int bmpStampBackgroundHeight = bmpStampBackground.getHeight();
        Log.d(this, "bmpStampBackgroundWidth:" + bmpStampBackgroundWidth + "bmpStampBackgroundHeight:" + bmpStampBackgroundHeight);
        mStampBackgroundPoints.setX(getWidth() / 2 - bmpStampBackgroundWidth / 2);
        mStampBackgroundPoints.setY(getHeight() / 2 - bmpStampBackgroundHeight / 2);
        if (isHorizontal) {
            mStampPoints.setX(mStampBackgroundPoints.getX() + 20);
            mStampPoints.setY(mStampBackgroundPoints.getY() + 40);
        } else {
            mStampPoints.setX(mStampBackgroundPoints.getX());
            mStampPoints.setY(mStampBackgroundPoints.getY() + 80);
        }
        mBtnReversalPoints.setX(mStampBackgroundPoints.getX() + bmpStampBackgroundWidth - bmpBtnReversal.getWidth() / 2);
        mBtnReversalPoints.setY(mStampBackgroundPoints.getY() - bmpBtnReversal.getHeight() / 2);

        int left = getWidth() / 2 - bmpStampBackgroundWidth / 2;
        int top = getHeight() / 2 - bmpStampBackgroundHeight / 2;
        int right = getWidth() - left;
        int bottom = getHeight() - top;

        mStampBackgroundRect.set(left, top, right, bottom);
        mStampRect.set(left, top, right, bottom);
        if (isHorizontal) {
            mStampCenterRect.set(160, 300, 500, 550);
        } else {
            mStampCenterRect.set(220, 310, 480, 600);
        }
        draw(getBmpStamp());
    }

    // Handler mHandler = new Handler(){
    //
    // @Override
    // public void handleMessage(Message msg) {
    // switch (msg.what) {
    // case MSG_ROTATE_STAMP_VIEW:
    // Log.d(this, "drawRotateView...");
    // drawRotateView();
    // break;
    //
    // default:
    // break;
    // }
    // }
    //
    // };

    private float x = 0;// current x coordinate of position
    private float y = 0;// current y coordinate of position
    float deltaX = 0;
    float deltaY = 0;
    float currentDistance;
    float lastDistance = -1;

    private void onTouchMove(MotionEvent event) {
        // 移动图片
        if (mode == DRAG) {
            mStampBackgroundPaint.setAlpha(StampViewConstants.PAINT_TRANSPRANT);
            mStampPaint.setAlpha(StampViewConstants.PAINT_TRANSPRANT);
            deltaX = event.getX() - x;
            deltaY = event.getY() - y;
            x = event.getX();
            y = event.getY();
            mStampPoints.setX(mStampPoints.getX() + deltaX);
            mStampPoints.setY(mStampPoints.getY() + deltaY);
            draw(getBmpStamp());
            // 缩放图片
        } else if (mode == ZOOM && event.getPointerCount() == 2) {
            mStampPaint.setAlpha(StampViewConstants.PAINT_TRANSPRANT);
            float offsetX = event.getX(0) - event.getX(1);
            float offsetY = event.getY(0) - event.getY(1);
            currentDistance = getDistance(offsetX, offsetY);
            Log.d(this, "current:" + currentDistance + ", last:" + lastDistance);

            if (lastDistance < 0) {
                lastDistance = currentDistance;
            } else {
                if (currentDistance - lastDistance > 15) {
                    Log.d(this, "放大");
                    lastDistance = currentDistance;
                    sx = 1.01f;
                    sy = 1.01f;
                    zoomMatrix.postScale(sx, sy);
                    mStampPoints.setX(mStampPoints.getX() / 1.01f);
                    mStampPoints.setY(mStampPoints.getY() / 1.01f);
                    draw(getBmpStamp());
                } else if (lastDistance - currentDistance > 15) {
                    lastDistance = currentDistance;
                    Log.d(this, "缩小");
                    sx = 0.99f;
                    sy = 0.99f;
                    zoomMatrix.postScale(sx, sy);
                    mStampPoints.setX(mStampPoints.getX() / 0.99f);
                    mStampPoints.setY(mStampPoints.getY() / 0.99f);
                    draw(getBmpStamp());
                }
            }

        }
    }

    private float getDistance(float offsetX, float offsetY) {
        return (float) Math.sqrt(offsetX * offsetX + offsetY * offsetY);
    }

    private void onTouchDown(MotionEvent event) {
        Log.d(this, "onTouchDown");
        x = event.getX();
        y = event.getY();
        Log.d(this, "x:" + x + ", y:" + y);
        Log.d(this, "x, y:" + mBtnReversalPoints.getX() + ", " + mBtnReversalPoints.getY());
        if (x >= mBtnReversalPoints.getX() - 5 && x <= mBtnReversalPoints.getX() + getBmpBtnReversal().getWidth() + 5
                && y >= mBtnReversalPoints.getY() - 5 && y <= mBtnReversalPoints.getY() + getBmpBtnReversal().getHeight() + 5) {
            // 点击了旋转按钮
            Log.d(this, "点击了旋转按钮");
            isBtnReversalClicked = true;
        } else {
            isBtnReversalClicked = false;
            mStampBackgroundPaint.setAlpha(StampViewConstants.PAINT_TRANSPRANT);
            mStampPaint.setAlpha(StampViewConstants.PAINT_NO_TRANSPRANT);
            if (isHorizontal()) {
                setBmpStampBackground(R.drawable.background_stamp_h_transparent_pierced);
            } else {
                setBmpStampBackground(R.drawable.background_stamp_v_transparent_pierced);
            }
            // setStampViewCanvasColor(Color.parseColor(StampViewConstants.COLOR_BACKGROUND_BLACK));
        }
    }

}
