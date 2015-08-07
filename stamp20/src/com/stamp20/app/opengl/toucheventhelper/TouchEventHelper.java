package com.stamp20.app.opengl.toucheventhelper;

import android.util.Log;
import android.view.MotionEvent;

/**
 * 只用来处理GLSurfaceView的onTouch事件的帮忙类 只输出总的位移和总的缩放
 * 
 * @author zhaoxin5
 *
 */
public class TouchEventHelper {

    public interface InvalidateCallback {
        public void invalidate(float ratio, float translateX, float translateY);
    }
    /**
     * 初始化状态常量
     */
    public static final int STATUS_INIT = 1;
    /**
     * 图片拖动状态常量
     */
    public static final int STATUS_MOVE = 4;
    /**
     * 图片缩小状态常量
     */
    public static final int STATUS_ZOOM_IN = 3;
    /**
     * 图片放大状态常量
     */
    public static final int STATUS_ZOOM_OUT = 2;
    protected int bitmapHeight;

    protected int bitmapWidth;

    /**
     * ZoomImageView控件的高度
     */
    protected int canvasHeight;

    /**
     * ZoomImageView控件的宽度
     */
    protected int canvasWidth;

    /**
     * 记录两指同时放在屏幕上时，中心点的横坐标值
     */
    protected float centerPointX;

    /**
     * 记录两指同时放在屏幕上时，中心点的纵坐标值
     */
    protected float centerPointY;

    /**
     * 记录当前图片的高度，图片被缩放时，这个值会一起变动
     */
    protected float currentBitmapHeight;

    /**
     * 记录当前图片的宽度，图片被缩放时，这个值会一起变动
     */
    protected float currentBitmapWidth;

    /**
     * 记录当前操作的状态，可选值为STATUS_INIT、STATUS_ZOOM_OUT、STATUS_ZOOM_IN和STATUS_MOVE
     */
    protected int currentStatus;

    final boolean DEBUG = true;

    final boolean DEBUG_ACTION_POINTER_DOWN = false;

    final boolean DEBUG_DOUBLE_MOVE = false;

    final boolean DEBUG_MAIN = true;

    final boolean DEBUG_SINGLE_MOVE = false;

    /**
     * 记录图片初始化时的缩放比例
     */
    protected float initRatio;

    /**
     * 记录上次两指之间的距离
     */
    private double lastFingerDis;

    /**
     * 记录上次手指移动时的横坐标
     */
    protected float lastXMove = -1;

    /**
     * 记录上次手指移动时的纵坐标
     */
    protected float lastYMove = -1;

    /**
     * totalRatio 最大缩放倍数是 maxRatioMultiple * initRatio
     */
    protected float maxRatioMultiple;

    protected float minRatioMultiple;

    InvalidateCallback mInvalidateCallback = null;
    /**
     * 当前处于移动中或者是缩放中
     */
    private boolean mIsMovingOrZooming = false;

    /**
     * 记录手指在横坐标方向上的移动距离
     */
    protected float movedDistanceX;

    /**
     * 记录手指在纵坐标方向上的移动距离
     */
    protected float movedDistanceY;

    /**
     * 记录手指移动的距离所造成的缩放比例
     */
    protected float scaledRatio;

    final String TAG = "TouchEventHelper";

    /**
     * 记录图片在矩阵上的总缩放比例
     */
    protected float totalRatio;

    /**
     * 记录图片在矩阵上的横向偏移值
     */
    protected float totalTranslateX;

    /**
     * 记录图片在矩阵上的纵向偏移值
     */
    protected float totalTranslateY;

    public TouchEventHelper() {
        totalRatio = 1;
        initRatio = 1;
        totalTranslateX = 0;
        totalTranslateY = 0;
        maxRatioMultiple = 4.0f;
    }
    public TouchEventHelper(float max, float min, int cw, int ch, int bw, int bh, InvalidateCallback c) {
        this();
        totalRatio = initRatio = 1.0f;
        maxRatioMultiple = max;
        minRatioMultiple = min;

        canvasWidth = cw;
        canvasHeight = ch;
        bitmapWidth = bw;
        bitmapHeight = bh;
        mInvalidateCallback = c;

        initParameters();
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

    private void DEBUG_LOG(boolean show, String msg) {
        if (DEBUG && show) {
            Log.i(TAG, msg);
        }
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

    protected void initParameters() {
        if (bitmapWidth > canvasWidth || bitmapHeight > canvasHeight) {
            if (bitmapWidth - canvasWidth > bitmapHeight - canvasHeight) {
                // 当图片宽度大于屏幕宽度时，将图片等比例压缩，使它可以完全显示出来
                totalRatio = canvasWidth / (bitmapWidth * 1.0f);
                totalTranslateY = (canvasHeight - (bitmapHeight * totalRatio)) / 2f;
                // 在纵坐标方向上进行偏移，以保证图片居中显示
            } else {
                // 当图片高度大于屏幕高度时，将图片等比例压缩，使它可以完全显示出来
                totalRatio = canvasHeight / (bitmapHeight * 1.0f);
                totalTranslateX = (canvasWidth - (bitmapWidth * totalRatio)) / 2f;
                // 在横坐标方向上进行偏移，以保证图片居中显示
                currentBitmapWidth = bitmapWidth * totalRatio;
                currentBitmapHeight = bitmapHeight * totalRatio;
            }
        } else {
            // 当图片的宽高都小于屏幕宽高时，直接让图片居中显示
            totalTranslateX = (canvasWidth - bitmapWidth) / 2f;
            totalTranslateY = (canvasHeight - bitmapHeight) / 2f;
            currentBitmapWidth = bitmapWidth;
            currentBitmapHeight = bitmapHeight;
        }
        initRatio = totalRatio;
        invalidate();
    }

    private void invalidate() {

        DEBUG_LOG(DEBUG_MAIN, "totalRatio:" + totalRatio + ",movedDistanceX:" + movedDistanceX + ",movedDistanceY:"
                + movedDistanceY + ",centerPointX:" + centerPointX + ",centerPointY:" + centerPointY);

        if (mInvalidateCallback != null) {
            mInvalidateCallback.invalidate(totalRatio, totalTranslateX, totalTranslateY);
        }
    }

    private void move() {
        // 根据手指移动的距离计算出总偏移值
        float translateX = totalTranslateX + movedDistanceX;
        float translateY = totalTranslateY + movedDistanceY;
        totalTranslateX = translateX;
        totalTranslateY = translateY;

        invalidate();
    }

    protected void onPreTouchEvent() {
        // 供子类在onTouch事件前进行某些处理
    }

    public boolean onTouchEvent(MotionEvent event) {
        onPreTouchEvent();
        switch (event.getActionMasked()) {
        case MotionEvent.ACTION_POINTER_DOWN:
            DEBUG_LOG(DEBUG_ACTION_POINTER_DOWN, "ACTION_POINTER_DOWN");
            if (event.getPointerCount() == 2) {
                // 当有两个手指按在屏幕上时，计算两指之间的距离
                lastFingerDis = distanceBetweenFingers(event);
                DEBUG_LOG(DEBUG_ACTION_POINTER_DOWN, "lastFingerDis:" + lastFingerDis);
            }
            break;
        case MotionEvent.ACTION_MOVE:
            DEBUG_LOG(false, "ACTION_MOVE");
            mIsMovingOrZooming = true;
            if (event.getPointerCount() == 1) {
                onTouchEventSingleFingerMove(event);
            } else if (event.getPointerCount() == 2) {
                onTouchEventDoubleFingerMove(event);
            }
            break;
        case MotionEvent.ACTION_POINTER_UP:
            DEBUG_LOG(false, "ACTION_POINTER_UP");
            onTouchEventDoubleFingerUp(event);
            break;
        case MotionEvent.ACTION_UP:
            DEBUG_LOG(false, "ACTION_UP");
            onTouchEventSingleFingerUp(event);
            break;
        case MotionEvent.ACTION_CANCEL:
            DEBUG_LOG(false, "ACTION_CANCEL");
            onTouchEventCancel(event);
            break;
        default:
            break;
        }
        return true;
    }

    protected void onTouchEventCancel(MotionEvent event) {
        mIsMovingOrZooming = false;
        invalidate();
    }

    protected void onTouchEventDoubleFingerMove(MotionEvent event) {
        DEBUG_LOG(DEBUG_DOUBLE_MOVE, "onTouchEventDoubleFingerMove");

        // 有两个手指按在屏幕上移动时，为缩放状态
        centerPointBetweenFingers(event);

        DEBUG_LOG(DEBUG_DOUBLE_MOVE, "centerPointX:" + centerPointX + ",centerPointY:" + centerPointY);

        double fingerDis = distanceBetweenFingers(event);
        if (fingerDis > lastFingerDis) {
            currentStatus = STATUS_ZOOM_OUT;
        } else {
            currentStatus = STATUS_ZOOM_IN;
        }

        DEBUG_LOG(DEBUG_DOUBLE_MOVE, "fingerDis:" + fingerDis + ",lastFingerDis:" + lastFingerDis);

        // 进行缩放倍数检查，最大只允许将图片放大maxRatioMultiple倍，最小可以缩小到初始化比例
        if ((currentStatus == STATUS_ZOOM_OUT && totalRatio < maxRatioMultiple * initRatio)
                || (currentStatus == STATUS_ZOOM_IN && totalRatio > initRatio * minRatioMultiple)) {
            scaledRatio = (float) (fingerDis / lastFingerDis);
            totalRatio = totalRatio * scaledRatio;
            if (totalRatio > maxRatioMultiple * initRatio) {
                totalRatio = maxRatioMultiple * initRatio;
            } else if (totalRatio < initRatio * minRatioMultiple) {
                totalRatio = initRatio * minRatioMultiple;
            }
            // 调用onDraw()方法绘制图片
            zoom();
            lastFingerDis = fingerDis;
        }
    }

    protected void onTouchEventDoubleFingerUp(MotionEvent event) {
        if (event.getPointerCount() == 2) {
            // 手指离开屏幕时将临时值还原
            lastXMove = -1;
            lastYMove = -1;
        }
        mIsMovingOrZooming = false;
        invalidate();
    }

    protected void onTouchEventSingleFingerMove(MotionEvent event) {
        // 只有单指按在屏幕上移动时，为拖动状态
        float xMove = event.getX();
        float yMove = event.getY();
        if (lastXMove == -1 && lastYMove == -1) {
            lastXMove = xMove;
            lastYMove = yMove;
        }
        currentStatus = STATUS_MOVE;
        movedDistanceX = xMove - lastXMove;
        movedDistanceY = yMove - lastYMove;
        // 进行边界检查，不允许将图片拖出边界
        if (totalTranslateX + movedDistanceX > canvasWidth * 0.5f) {
            movedDistanceX = 0;
        } else if (canvasWidth - (totalTranslateX + movedDistanceX) > currentBitmapWidth + canvasWidth * 0.5f) {
            movedDistanceX = 0;
        }
        if (totalTranslateY + movedDistanceY > canvasHeight * 0.5f) {
            movedDistanceY = 0;
        } else if (canvasHeight - (totalTranslateY + movedDistanceY) > currentBitmapHeight + canvasHeight * 0.5f) {
            movedDistanceY = 0;
        }
        // 调用onDraw()方法绘制图片
        move();
        lastXMove = xMove;
        lastYMove = yMove;
    }

    protected void onTouchEventSingleFingerUp(MotionEvent event) {
        // 手指离开屏幕时将临时值还原
        lastXMove = -1;
        lastYMove = -1;
        mIsMovingOrZooming = false;
        invalidate();
    }

    public void setInvalidateCallback(InvalidateCallback c) {
        mInvalidateCallback = c;
    }

    protected void zoom() {

        float scaledWidth = bitmapWidth * totalRatio;
        float scaledHeight = bitmapHeight * totalRatio;
        float translateX = 0f;
        float translateY = 0f;

        translateX = totalTranslateX * scaledRatio + centerPointX * (1 - scaledRatio);
        if (translateX > 0 + canvasWidth * 0.5f) {
            // 移动超过一半了
            translateX = 0 + canvasWidth * 0.5f;
        } else if (canvasWidth * 0.5f - translateX > scaledWidth) {
            translateX = canvasWidth * 0.5f - scaledWidth;
        }

        translateY = totalTranslateY * scaledRatio + centerPointY * (1 - scaledRatio);
        if (translateY > 0 + canvasHeight * 0.5f) {
            translateY = 0 + canvasHeight * 0.5f;
        } else if (canvasHeight * 0.5f - translateY > scaledHeight) {
            translateY = canvasHeight * 0.5f - scaledHeight;
        }

        totalTranslateX = translateX;
        totalTranslateY = translateY;
        currentBitmapWidth = scaledWidth;
        currentBitmapHeight = scaledHeight;

        invalidate();
    }
}
