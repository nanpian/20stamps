package com.stamp20.app.view;

import android.R.integer;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
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
    private StampPoints mStampBackgroundPoints = null;
    private StampPoints mStampPoints = null;

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

        mStampBackgroundPoints = new StampPoints();
        mStampPoints = new StampPoints();

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

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d(this, "onMeasure()...");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d(this, "width:" + getWidth() + ", height:" + getHeight());
        int bmpStampBackgroundWidth = bmpStampBackground.getWidth();
        int bmpStampBackgroundHeight = bmpStampBackground.getHeight();
        mStampBackgroundPoints.setX(getWidth() / 2 - bmpStampBackgroundWidth / 2);
        mStampBackgroundPoints.setY(getHeight() / 2 - bmpStampBackgroundHeight / 2);
        mStampPoints.setX(mStampBackgroundPoints.getX() + 20);
        mStampPoints.setY(mStampBackgroundPoints.getY() + 20);

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
//                zoomMatrix.postScale(1.0f, 1.0f);
                Bitmap zoomBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), zoomMatrix, true);
                int left = (int) mStampPoints.getX();
                int top = (int) mStampPoints.getY();
                int right = (int) (mStampPoints.getX() + zoomBitmap.getWidth());
                int bottom = (int) (mStampPoints.getY() + zoomBitmap.getHeight());
//                Log.d(this, left + ", " + top + ", " + right + ", " + bottom);
                mStampRect.set(left, top, right, bottom);
                if (mode == NONE) {
                    // 此处为剪切画布，移动图案松手后，只在邮票框内显示图案
                    canvas.save();
                    canvas.clipRect(mStampCenterRect);
                    canvas.drawBitmap(zoomBitmap, mStampPoints.getX(), mStampPoints.getY(), mStampPaint);
                    canvas.restore();
                    // canvas.drawBitmap(newBitmap, null, mStampCenterRect,
                    // mStampPaint);
                } else if (mode == DRAG) {
                    canvas.drawBitmap(zoomBitmap, mStampPoints.getX(), mStampPoints.getY(), mStampPaint);
                } else if (mode == ZOOM) {
                    
                    canvas.drawBitmap(zoomBitmap, mStampPoints.getX(), mStampPoints.getY(), mStampPaint);
                }
            }

            //画出最上层的邮票框
            canvas.drawBitmap(getBmpStampBackground(), mStampBackgroundPoints.getX(), mStampBackgroundPoints.getY(), mStampBackgroundPaint);
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
            onTouchMove(event);
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
        mStampBackgroundPaint.setAlpha(StampViewConstants.PAINT_NO_TRANSPRANT);
        mStampPaint.setAlpha(StampViewConstants.PAINT_NO_TRANSPRANT);
        setBmpStampBackground(R.drawable.background_stamp_h_transparent_pierced);
        setStampViewCanvasColor(Color.parseColor(StampViewConstants.COLOR_BACKGROUND_GRAY));
        lastDistance = -1;
        draw(getBmpStamp());
    }

    private float x = 0;// current x coordinate of position
    private float y = 0;// current y coordinate of position
    float deltaX = 0;
    float deltaY = 0;
    float currentDistance;
    float lastDistance = -1;

    private void onTouchMove(MotionEvent event) {
        // 移动图片
        if (mode == DRAG) {
            Log.d(this, "DRAG...");
            mStampBackgroundPaint.setAlpha(StampViewConstants.PAINT_TRANSPRANT);
            mStampPaint.setAlpha(StampViewConstants.PAINT_NO_TRANSPRANT);
            deltaX = event.getX() - x;
            deltaY = event.getY() - y;
            x = event.getX();
            y = event.getY();
            mStampPoints.setX(mStampPoints.getX() + deltaX);
            mStampPoints.setY(mStampPoints.getY() + deltaY);
            draw(getBmpStamp());
            // 缩放图片
        } else if (mode == ZOOM && event.getPointerCount() == 2) {
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
                    draw(getBmpStamp());
                } else if (lastDistance - currentDistance > 15) {
                    lastDistance = currentDistance;
                    Log.d(this, "缩小");
                    sx = 0.99f;
                    sy = 0.99f;
                    zoomMatrix.postScale(sx, sy);
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
        mStampBackgroundPaint.setAlpha(StampViewConstants.PAINT_TRANSPRANT);
        mStampPaint.setAlpha(StampViewConstants.PAINT_NO_TRANSPRANT);
        setBmpStampBackground(R.drawable.background_stamp_h_transparent_pierced);
        // setStampViewCanvasColor(Color.parseColor(StampViewConstants.COLOR_BACKGROUND_BLACK));
    }

}
