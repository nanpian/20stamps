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
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.stamp20.app.R;
import com.stamp20.app.util.Log;
import com.stamp20.app.util.BitmapCache;

public class StampView extends View implements OnTouchListener {

    /*
     * 邮票框
     */
    private Bitmap bmpStampBackground = null;
    
    /*
     * 制作邮票所用的图片
     */
    private Bitmap bmpStampPhoto = null;
    
    /*
     * 制作邮票时右上角的旋转按钮
     */
    private Bitmap bmpBtnReversal = null;
    
    /*
     * 最终生成的邮票图案，不包含右上角的旋转按钮
     */
    private Bitmap bmpStamp = null;

    /*
     * 邮票框的点坐标
     */
    private StampPoints mStampBackgroundPoints = null;
    
    /*
     * 邮票所用图片点坐标
     */
    private StampPoints mStampPoints = null;
    
    /*
     * 旋转按钮点坐标
     */
    private StampPoints mBtnReversalPoints = null;
    
    /*
     * 当前邮票是横屏还是竖屏界面
     */
    private boolean isHorizontal = true;

    /*
     * 画邮票框所用的画笔
     */
    private Paint mStampBackgroundPaint = null;
    
    /*
     * 画邮票所用图片的画笔
     */
    private Paint mStampPaint = null;
    
    /*
     * 画整个画面的背景的画笔
     */
    private Paint mViewBackgroundPaint = null;

    // private Matrix mStampBackgroundMatrix;
    // private Matrix mStampMatrix;
    /*
     * 缩放图片使用的矩阵
     */
    private Matrix zoomMatrix;
    float sx;
    float sy;

    // private Rect mStampRect;
    /*
     * 邮票框背景矩阵
     */
    private Rect mStampBackgroundRect;
    /*
     * 邮票框中间用于呈现图案的矩阵
     */
    private Rect mStampCenterRect;

    /*
     * 背景色
     */
    private int stampViewCanvasColor = Color.parseColor(StampViewConstants.COLOR_BACKGROUND_GRAY);

    /*
     * 手势:
     * NONE:手指没有操作
     * DRAG：移动
     * ZOOM：缩放
     * mode：当前所处的模式NONE/DRAG/ZOOM
     */
    private int NONE = 0;
    private int DRAG = 1;
    private int ZOOM = 2;
    private int mode = NONE;
    
    /*
     * 旋转按钮是否被触发
     */
    private boolean isBtnReversalClicked = false;

    private static final int MSG_ROTATE_STAMP_VIEW = 1100;
    private static final int DELTA_LEN = 15;

    /*
     * 用于保存最终生成的邮票cache
     */
    private BitmapCache mCache = null;

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
        Resources res = getResources();
        bmpStampBackground = BitmapFactory.decodeResource(res, R.drawable.background_stamp_h_transparent_pierced);
        bmpBtnReversal = BitmapFactory.decodeResource(res, R.drawable.icon_rotation_left);

        mStampBackgroundPoints = new StampPoints();
        mStampPoints = new StampPoints();
        mBtnReversalPoints = new StampPoints();

        mStampBackgroundPaint = new Paint();
        mStampPaint = new Paint();
        mViewBackgroundPaint = new Paint();
        mViewBackgroundPaint.setColor(Color.WHITE);

        // mStampBackgroundMatrix = new Matrix();
        // mStampMatrix = new Matrix();

        // mStampRect = new Rect();
        mStampBackgroundRect = new Rect();
        mStampCenterRect = new Rect();

        zoomMatrix = new Matrix();
        sx = 1.0f;
        sy = 1.0f;// 默认设置缩放为1
        zoomMatrix.postScale(sx, sy);

        mCache = BitmapCache.getCache();

        setOnTouchListener(this);

    }

    /*
     * 获取邮票背景框
     * return：bitmap
     */
    public Bitmap getBmpStampBackground() {
        return bmpStampBackground;
    }

    /*
     * 设置邮票背景框
     * @para:bmpStampBackground, 邮票框bitmap
     */
    public void setBmpStampBackground(Bitmap bmpStampBackground) {
        this.bmpStampBackground = bmpStampBackground;
    }

    public void setBmpStampBackground(int resId) {
        this.bmpStampBackground = null;
        Resources res = getResources();
        this.bmpStampBackground = BitmapFactory.decodeResource(res, resId);
    }

    public Bitmap getBmpStampPhoto() {
        return bmpStampPhoto;
    }
    
	public void setBmpStampPhoto(Bitmap bitmap, boolean b) {
		// TODO 使用滤镜的时候不缩放
	       this.bmpStampPhoto = bmpStamp;
	        zoomMatrix.postScale(1.0f, 1.0f);
	}

    public void setBmpStampPhoto(Bitmap bmpStamp) {
        this.bmpStampPhoto = bmpStamp;
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

    public Bitmap getBmpStamp() {
        return bmpStamp;
    }

    public void setBmpStamp(Bitmap bmpStamp) {
        this.bmpStamp = bmpStamp;
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
        // mStampRect.set(left, top, right, bottom);
        mStampCenterRect.set(left + DELTA_LEN, top + DELTA_LEN, right - DELTA_LEN, bottom - DELTA_LEN);

    }

    @Override
    public void onDraw(Canvas canvas) {
        if (canvas != null) {
            canvas.drawColor(getStampViewCanvasColor());
            Bitmap bmpStamp = getBmpStampPhoto();
            if (bmpStamp != null) {
                // 画出邮票后的白色背景，此处应替换为与邮票背景一样的白色图案
                canvas.drawRect(mStampCenterRect, mViewBackgroundPaint);
                bmpStamp = Bitmap.createBitmap(bmpStamp, 0, 0, bmpStamp.getWidth(), bmpStamp.getHeight(), zoomMatrix, true);
                if (mode == DRAG || mode == ZOOM) {
                    canvas.drawBitmap(bmpStamp, mStampPoints.getX(), mStampPoints.getY(), mStampPaint);
                }
                // 画出邮票中间部分，让图片在中间区域不透明
                canvas.save();
                canvas.clipRect(mStampCenterRect);
                mStampPaint.setAlpha(StampViewConstants.PAINT_NO_TRANSPRANT);
                canvas.drawBitmap(bmpStamp, mStampPoints.getX(), mStampPoints.getY(), mStampPaint);
                canvas.restore();
            }

            // 画出最上层的邮票框
            canvas.drawBitmap(getBmpStampBackground(), mStampBackgroundPoints.getX(), mStampBackgroundPoints.getY(), mStampBackgroundPaint);
            // 松开手指，此时产生一张图片，用于review等后续操作，该图案不包含右上角的旋转按钮
            if (bmpStamp != null && mode == NONE) {
                generateStamp();
            }
            // 画出右上角旋转按钮
            canvas.drawBitmap(getBmpBtnReversal(), mBtnReversalPoints.getX(), mBtnReversalPoints.getY(), mViewBackgroundPaint);
            canvas.restore();
        }
    }

    public void generateStamp() {
        buildDrawingCache();
        mCache.put(getDrawingCache());
        destroyDrawingCache();
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
            // draw(getBmpStamp());
            invalidate();
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
        // mStampRect.set(left, top, right, bottom);
        mStampCenterRect.set(left + DELTA_LEN, top + DELTA_LEN, right - DELTA_LEN, bottom - DELTA_LEN);
        invalidate();
        // draw(getBmpStamp());
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
            mStampBackgroundPaint.setAlpha(StampViewConstants.PAINT_TRANSPRANT);
            mStampPaint.setAlpha(StampViewConstants.PAINT_TRANSPRANT);
            deltaX = event.getX() - x;
            deltaY = event.getY() - y;
            x = event.getX();
            y = event.getY();
            mStampPoints.setX(mStampPoints.getX() + deltaX);
            mStampPoints.setY(mStampPoints.getY() + deltaY);
            // draw(getBmpStamp());
            invalidate();
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
                    // draw(getBmpStamp());
                    invalidate();
                } else if (lastDistance - currentDistance > 15) {
                    lastDistance = currentDistance;
                    Log.d(this, "缩小");
                    sx = 0.99f;
                    sy = 0.99f;
                    zoomMatrix.postScale(sx, sy);
                    mStampPoints.setX(mStampPoints.getX() / 0.99f);
                    mStampPoints.setY(mStampPoints.getY() / 0.99f);
                    // draw(getBmpStamp());
                    invalidate();
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
