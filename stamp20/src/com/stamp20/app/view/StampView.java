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

import com.stamp20.app.R;
import com.stamp20.app.util.BitmapCache;
import com.stamp20.app.util.Log;

/**
 * 自定义的ImageView控制，可对图片进行多点触控缩放和拖动
 * 
 * @author guolin
 */
public class StampView extends View {

    /**
     * 初始化状态常量
     */
    public static final int STATUS_INIT = 1;

    /**
     * 图片放大状态常量
     */
    public static final int STATUS_ZOOM_OUT = 2;

    /**
     * 图片缩小状态常量
     */
    public static final int STATUS_ZOOM_IN = 3;

    /**
     * 图片拖动状态常量
     */
    public static final int STATUS_MOVE = 4;

    /**
     * 松开手指状态常量
     */
    public static final int STATUS_NONE = 5;

    /**
     * 用于对图片进行移动和缩放变换的矩阵
     */
    private Matrix matrix = new Matrix();

    /**
     * 用于对邮票框移动以及缩放的变换矩阵
     */
    private Matrix stampbackgroundMatrix = new Matrix();

    /**
     * 用于邮票框右上角旋转按钮移动的变换矩阵
     */
    private Matrix btnRotateMatrix = new Matrix();

    /**
     * 待展示的Bitmap对象
     */
    private Bitmap sourceBitmap;

    /**
     * 邮票框Bitmap对象
     */
    private Bitmap bmpStampBackground;

    /**
     * 制作邮票时右上角的旋转按钮
     */
    private Bitmap bmpBtnReversal = null;

    /**
     * 记录当前操作的状态，可选值为STATUS_INIT、STATUS_ZOOM_OUT、STATUS_ZOOM_IN和STATUS_MOVE
     */
    private int currentStatus;

    /**
     * ZoomImageView控件的宽度
     */
    private int width;

    /**
     * ZoomImageView控件的高度
     */
    private int height;

    /**
     * 邮票框宽度
     */
    private int stampBackgroundWidth;

    /**
     * 邮票框高度
     */
    private int stampBackgroundHeight;

    /**
     * 记录两指同时放在屏幕上时，中心点的横坐标值
     */
    private float centerPointX;

    /**
     * 记录两指同时放在屏幕上时，中心点的纵坐标值
     */
    private float centerPointY;

    /**
     * 记录当前图片的宽度，图片被缩放时，这个值会一起变动
     */
    private float currentBitmapWidth;

    /**
     * 记录当前图片的高度，图片被缩放时，这个值会一起变动
     */
    private float currentBitmapHeight;

    /**
     * 记录上次手指移动时的横坐标
     */
    private float lastXMove = -1;

    /**
     * 记录上次手指移动时的纵坐标
     */
    private float lastYMove = -1;

    /**
     * 记录手指在横坐标方向上的移动距离
     */
    private float movedDistanceX;

    /**
     * 记录手指在纵坐标方向上的移动距离
     */
    private float movedDistanceY;

    /**
     * 记录图片在矩阵上的横向偏移值
     */
    private float totalTranslateX;

    /**
     * 记录图片在矩阵上的纵向偏移值
     */
    private float totalTranslateY;

    /**
     * 记录图片在矩阵上的总缩放比例
     */
    private float totalRatio;

    /**
     * 记录手指移动的距离所造成的缩放比例
     */
    private float scaledRatio;

    /**
     * 记录图片初始化时的缩放比例
     */
    private float initRatio;

    /**
     * 记录上次两指之间的距离
     */
    private double lastFingerDis;

    /**
     * 邮票框中间用于呈现图案的矩阵
     */
    private Rect mStampCenterRect = new Rect();

    /**
     * 移动图片时，在邮票背景框后面画白色，边框比背景框小的差值
     */
    private static final int DELTA_LEN = 15;

    /**
     * 画白色背景所用的画笔
     */
    Paint viewBackgroundPaint = new Paint();

    /**
     * 画邮票图片所用的画笔
     */
    Paint stampPhotoPaint = new Paint();

    /**
     * 画邮票框所用画笔
     */
    Paint stampBackgroundPaint = new Paint();

    /**
     * 最终生成的邮票图案
     */
    private Bitmap mStamp;

    /**
     * 用于保存最终生成的邮票cache
     */
    private BitmapCache mCache = BitmapCache.getCache();

    /**
     * 用于判定当前是否需要重绘
     */
    private static boolean needDraw = false;

    /**
     * 旋转按钮的坐标
     */
    private StampPoints btnRotatePoints = new StampPoints();

    /**
     * 判断旋转按钮是否被触发
     */
    private boolean isBtnReversalClicked = false;

    /**
     * 当前邮票是横屏还是竖屏界面
     */
    private boolean isHorizontal = true;

    public StampView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        currentStatus = STATUS_INIT;
        // 设置画笔颜色
        viewBackgroundPaint.setColor(Color.WHITE);
        // 设置邮票图片透明度
        stampPhotoPaint.setAlpha(StampViewConstants.PAINT_NO_TRANSPRANT);
        // 设置背景图片透明度
        stampBackgroundPaint.setAlpha(StampViewConstants.PAINT_NO_TRANSPRANT);
        // 设置邮票框资源
        setBmpStampBackground(R.drawable.background_stamp_h_transparent_pierced);
        // 设置旋转按钮资源
        setBmpBtnReversal(R.drawable.icon_rotation_left);
    }

    /**
     * 对图片进行初始化操作，包括让图片居中，以及当图片大于屏幕宽高时对图片进行压缩。
     * 
     * @param canvas
     */
    private void initBitmap(Canvas canvas) {

        if (isHorizontal) {
            setBmpStampBackground(R.drawable.background_stamp_h_transparent_pierced);
            setBmpBtnReversal(R.drawable.icon_rotation_left);
        } else {
            setBmpStampBackground(R.drawable.background_stamp_v_transparent_pierced);
            setBmpBtnReversal(R.drawable.icon_rotation_right);
        }
        if (sourceBitmap != null) {
            matrix.reset();
            int bitmapWidth = sourceBitmap.getWidth();
            int bitmapHeight = sourceBitmap.getHeight();
            // 计算邮票宽与图片宽比率，背景高与图片高比率，取较大值作为比率，使得缩放宽度与邮票框宽度一致
            float ratio = Math.max(stampBackgroundWidth / (bitmapWidth * 1.0f),
                    stampBackgroundHeight / (bitmapHeight * 1.0f));
            matrix.postScale(ratio, ratio);
            float translateX = (width - (bitmapWidth * ratio)) / 2f;
            float translateY = (height - (bitmapHeight * ratio)) / 2f;
            matrix.postTranslate(translateX, translateY);
            totalTranslateX = translateX;
            totalTranslateY = translateY;
            totalRatio = initRatio = ratio;

            currentBitmapWidth = bitmapWidth * initRatio;
            currentBitmapHeight = bitmapHeight * initRatio;

        }

        int left = (width - bmpStampBackground.getWidth()) / 2;
        int top = (height - bmpStampBackground.getHeight()) / 2;
        int right = width - left;
        int bottom = height - top;
        // // 初始化邮票框
        stampbackgroundMatrix.reset();
        float stampBackgroundTranslateX = (width - bmpStampBackground
                .getWidth()) / 2f;
        float stampBackgroundTranslateY = (height - bmpStampBackground
                .getHeight()) / 2f;
        stampbackgroundMatrix.postTranslate(stampBackgroundTranslateX,
                stampBackgroundTranslateY);

        mStampCenterRect.set(left + DELTA_LEN, top + DELTA_LEN, right
                - DELTA_LEN, bottom - DELTA_LEN);
        // 设置右上角旋转按钮图片，默认为水平方向
        btnRotateMatrix.reset();
        btnRotatePoints.setX(right - getBmpBtnReversal().getWidth() / 2);
        btnRotatePoints.setY(top - getBmpBtnReversal().getHeight() / 2);
        btnRotateMatrix.postTranslate(right - getBmpBtnReversal().getWidth()
                / 2, top - getBmpBtnReversal().getHeight() / 2);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
            int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.d(this, "onLayout...");
        if (changed) {
            // 分别获取到ZoomImageView的宽度和高度
            width = getWidth();
            height = getHeight();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
        case MotionEvent.ACTION_DOWN:
            Log.d(this, "ACTION_DOWN");
            onTouchDown(event);
        case MotionEvent.ACTION_POINTER_DOWN:
            Log.d(this, "ACTION_POINTER_DOWN");
            if (event.getPointerCount() == 2) {
                // 当有两个手指按在屏幕上时，计算两指之间的距离
                lastFingerDis = distanceBetweenFingers(event);
            }
            lastXMove = -1;
            lastYMove = -1;
            break;
        case MotionEvent.ACTION_MOVE:
            Log.d(this, "onTouch,move");
            if (!isBtnReversalClicked) {
                needDraw = true;
                if (event.getPointerCount() == 1) {
                    // 只有单指按在屏幕上移动时，为拖动状态
                    float xMove = event.getX();
                    float yMove = event.getY();
                    if (lastXMove == -1 && lastYMove == -1) {
                        lastXMove = xMove;
                        lastYMove = yMove;
                    }
                    currentStatus = STATUS_MOVE;
                    movedDistanceX = xMove - lastXMove;
                    Log.d(this, "movedDistanceX:" + movedDistanceX);
                    movedDistanceY = yMove - lastYMove;
                    invalidate();
                    lastXMove = xMove;
                    lastYMove = yMove;
                } else if (event.getPointerCount() == 2) {
                    // 有两个手指按在屏幕上移动时，为缩放状态
                    centerPointBetweenFingers(event);
                    double fingerDis = distanceBetweenFingers(event);
                    if (fingerDis - lastFingerDis > 15) {
                        currentStatus = STATUS_ZOOM_OUT;
                    } else if (lastFingerDis - fingerDis > 15) {
                        currentStatus = STATUS_ZOOM_IN;
                    }
                    // 进行缩放倍数检查，最大只允许将图片放大4倍，最小可以缩小到初始化比例的1/2
                    if ((currentStatus == STATUS_ZOOM_OUT && totalRatio < 4 * initRatio)
                            || (currentStatus == STATUS_ZOOM_IN && totalRatio > initRatio / 2)) {
                        scaledRatio = (float) (fingerDis / lastFingerDis);
                        totalRatio = totalRatio * scaledRatio;
                        if (totalRatio > 4 * initRatio) {
                            totalRatio = 4 * initRatio;
                        } else if (totalRatio < initRatio / 2) {
                            totalRatio = initRatio / 2;
                        }
                        invalidate();
                        lastFingerDis = fingerDis;
                    }
                }
            }
            break;
        case MotionEvent.ACTION_POINTER_UP:
            if (event.getPointerCount() == 2) {
                // 手指离开屏幕时将临时值还原
                lastXMove = -1;
                lastYMove = -1;
            }
            stampBackgroundPaint
                    .setAlpha(StampViewConstants.PAINT_NO_TRANSPRANT);
            currentStatus = STATUS_NONE;
            invalidate();
            break;
        case MotionEvent.ACTION_UP:
            // 手指离开屏幕时将临时值还原
            lastXMove = -1;
            lastYMove = -1;
            stampBackgroundPaint
                    .setAlpha(StampViewConstants.PAINT_NO_TRANSPRANT);
            currentStatus = STATUS_NONE;
            invalidate();
            break;
        default:
            break;
        }
        return true;
    }

    private void onTouchDown(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        if (x >= btnRotatePoints.getX() - 5
                && x <= btnRotatePoints.getX() + getBmpBtnReversal().getWidth()
                        + 5
                && y >= btnRotatePoints.getY() - 5
                && y <= btnRotatePoints.getY()
                        + getBmpBtnReversal().getHeight() + 5) {
            Log.d(this, "rotate....");
            isBtnReversalClicked = true;
            setHorizontal(!isHorizontal);
            currentStatus = STATUS_INIT;
            invalidate();
        } else {
            isBtnReversalClicked = false;
        }

    }

    /**
     * 根据currentStatus的值来决定对图片进行什么样的绘制操作。
     */
    @Override
    protected void onDraw(Canvas canvas) {
        // super.onDraw(canvas);
        Log.d(this, "onDraw()....");
        canvas.save();
        canvas.drawRect(mStampCenterRect, viewBackgroundPaint);
        switch (currentStatus) {
        case STATUS_INIT:
            initBitmap(canvas);
            canvas.drawBitmap(sourceBitmap, matrix, stampPhotoPaint);
            canvas.drawColor(Color
                    .parseColor(StampViewConstants.COLOR_BACKGROUND_GRAY));
            canvas.drawRect(mStampCenterRect, viewBackgroundPaint);
            break;
        case STATUS_NONE:
            canvas.drawColor(Color
                    .parseColor(StampViewConstants.COLOR_BACKGROUND_GRAY));
            canvas.drawRect(mStampCenterRect, viewBackgroundPaint);
            break;
        case STATUS_ZOOM_OUT:
        case STATUS_ZOOM_IN:
            stampPhotoPaint.setAlpha(StampViewConstants.PAINT_TRANSPRANT);
            stampBackgroundPaint.setAlpha(StampViewConstants.PAINT_TRANSPRANT);
            if (needDraw) {
                zoom(canvas);
            }
            break;
        case STATUS_MOVE:
            if (needDraw) {
                Log.d(this, "onDraw,move");
                stampPhotoPaint.setAlpha(StampViewConstants.PAINT_TRANSPRANT);
                stampBackgroundPaint
                        .setAlpha(StampViewConstants.PAINT_TRANSPRANT);
                move(canvas);
            }
            break;
        }
        // 画出邮票中间部分，让图片在中间区域不透明
        canvas.save();
        canvas.clipRect(mStampCenterRect);
        stampPhotoPaint.setAlpha(StampViewConstants.PAINT_NO_TRANSPRANT);
        canvas.drawBitmap(sourceBitmap, matrix, stampPhotoPaint);
        canvas.restore();
        // 画出邮票框
        canvas.drawBitmap(getStampBackground(), stampbackgroundMatrix,
                stampBackgroundPaint);
        if (currentStatus == STATUS_INIT || currentStatus == STATUS_NONE) {
            generateStamp();
        }
        // 画出邮票右上角旋转按钮
        canvas.drawBitmap(getBmpBtnReversal(), btnRotateMatrix, null);
        canvas.restore();
    }

    public void generateStamp() {
        buildDrawingCache();
        mStamp = getDrawingCache();
        mCache.put(mStamp);
        destroyDrawingCache();
    }

    /**
     * 对图片进行缩放处理。
     * 
     * @param canvas
     */
    private void zoom(Canvas canvas) {
        canvas.save();
        needDraw = false;
        matrix.reset();
        // 将图片按总缩放比例进行缩放
        matrix.postScale(totalRatio, totalRatio);
        float scaledWidth = sourceBitmap.getWidth() * totalRatio;
        float scaledHeight = sourceBitmap.getHeight() * totalRatio;
        float translateX = 0f;
        float translateY = 0f;
        translateX = totalTranslateX * scaledRatio + centerPointX
                * (1 - scaledRatio);
        translateY = totalTranslateY * scaledRatio + centerPointY
                * (1 - scaledRatio);
        // 缩放后对图片进行偏移，以保证缩放后中心点位置不变
        matrix.postTranslate(translateX, translateY);
        totalTranslateX = translateX;
        totalTranslateY = translateY;
        currentBitmapWidth = scaledWidth;
        currentBitmapHeight = scaledHeight;
        canvas.drawBitmap(sourceBitmap, matrix, stampPhotoPaint);
        canvas.restore();
    }

    /**
     * 对图片进行平移处理
     * 
     * @param canvas
     */
    private void move(Canvas canvas) {
        Log.d(this, "move...");
        needDraw = false;
        canvas.save();
        matrix.reset();
        // 根据手指移动的距离计算出总偏移值
        float translateX = totalTranslateX + movedDistanceX;
        Log.d(this, "move,totalX:" + totalTranslateX + ", movedX:"
                + movedDistanceX + ", X:" + translateX);
        float translateY = totalTranslateY + movedDistanceY;
        // 先按照已有的缩放比例对图片进行缩放
        matrix.postScale(totalRatio, totalRatio);
        // 再根据移动距离进行偏移
        matrix.postTranslate(translateX, translateY);
        totalTranslateX = translateX;
        totalTranslateY = translateY;
        canvas.drawBitmap(sourceBitmap, matrix, stampPhotoPaint);
        canvas.restore();
    }

    /**
     * 将待展示的图片设置进来。
     * 
     * @param bitmap
     *            待展示的Bitmap对象
     */
    public void setImageBitmap(Bitmap bitmap) {
        sourceBitmap = bitmap;
        invalidate();
    }

    /**
     * 设置制作邮票所使用的图片
     * 
     * @param bitmap
     */
    public void setBmpStampPhoto(Bitmap bitmap) {
        sourceBitmap = bitmap;
        invalidate();
    }

    public void setBmpStampBackground(Bitmap bitmap) {
        this.bmpStampBackground = bitmap;
        stampBackgroundWidth = bitmap.getWidth();
        stampBackgroundHeight = bitmap.getHeight();
        invalidate();
    }

    /**
     * 设置邮票框bitmap
     * 
     * @param resId
     */
    public void setBmpStampBackground(int resId) {
        Resources res = getResources();
        this.bmpStampBackground = BitmapFactory.decodeResource(res, resId);
        stampBackgroundWidth = bmpStampBackground.getWidth();
        stampBackgroundHeight = bmpStampBackground.getHeight();
        // invalidate();
    }

    private Bitmap getStampBackground() {
        return this.bmpStampBackground;
    }

    public Bitmap getBmpStampPhoto() {
        return this.sourceBitmap;
    }

    private Bitmap getBmpBtnReversal() {
        return bmpBtnReversal;
    }

    private void setBmpBtnReversal(Bitmap bmpBtnReversal) {
        this.bmpBtnReversal = bmpBtnReversal;
    }

    private void setBmpBtnReversal(int resId) {
        Resources res = getResources();
        this.bmpBtnReversal = BitmapFactory.decodeResource(res, resId);
    }

    public boolean isHorizontal() {
        return isHorizontal;
    }

    public void setHorizontal(boolean isHorizontal) {
        this.isHorizontal = isHorizontal;
    }

    /**
     * 计算两个手指之间的距离。
     * 
     * @param event
     * @return 两个手指之间的距离
     */
    private double distanceBetweenFingers(MotionEvent event) {
        float disX = Math.abs(event.getX(0) - event.getX(1));
        float disY = Math.abs(event.getY(0) - event.getY(1));
        return Math.sqrt(disX * disX + disY * disY);
    }

    /**
     * 计算两个手指之间中心点的坐标。
     * 
     * @param event
     */
    private void centerPointBetweenFingers(MotionEvent event) {
        float xPoint0 = event.getX(0);
        float yPoint0 = event.getY(0);
        float xPoint1 = event.getX(1);
        float yPoint1 = event.getY(1);
        centerPointX = (xPoint0 + xPoint1) / 2;
        centerPointY = (yPoint0 + yPoint1) / 2;
    }
}
