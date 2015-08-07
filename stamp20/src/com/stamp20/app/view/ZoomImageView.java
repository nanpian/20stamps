package com.stamp20.app.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.stamp20.app.R;

/**
 * 自定义的ImageView控制，可对图片进行多点触控缩放和拖动
 * 
 * @author guolin
 */
public class ZoomImageView extends View {

    public interface OnMoveOrZoomListener {
        public void onMoveOrZoomListener(boolean flag, float ratio, float currentRatio);
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

    /**
     * ZoomImageView控件的高度
     */
    protected int height;

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

    OnMoveOrZoomListener listener = null;

    /**
     * 用于对图片进行移动和缩放变换的矩阵
     */
    protected Matrix matrix = new Matrix();

    private boolean mIsMovingOrZooming = false;

    protected Bitmap mMaskBitmap = null;

    protected int mMaskBitmapId = -1;

    protected int mMaskColorId = -1;

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

    /**
     * 待展示的Bitmap对象
     */
    protected Bitmap sourceBitmap;

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

    /**
     * ZoomImageView控件的宽度
     */
    protected int width;

    /**
     * ZoomImageView构造函数，将当前操作状态设为STATUS_INIT。
     * 
     * @param context
     * @param attrs
     */
    public ZoomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        currentStatus = STATUS_INIT;

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ZoomImageView);
        mMaskBitmapId = a.getResourceId(R.styleable.ZoomImageView_maskDrawable, -1);
        mMaskColorId = a.getResourceId(R.styleable.ZoomImageView_maskColor, -1);
        a.recycle();
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
     * 对图片进行初始化操作，包括让图片居中，以及当图片大于屏幕宽高时对图片进行压缩。
     * 
     * @param canvas
     */
    protected void initBitmap(Canvas canvas) {
        if (sourceBitmap != null) {
            matrix.reset();
            int bitmapWidth = sourceBitmap.getWidth();
            int bitmapHeight = sourceBitmap.getHeight();
            if (bitmapWidth > width || bitmapHeight > height) {
                if (bitmapWidth - width > bitmapHeight - height) {
                    // 当图片宽度大于屏幕宽度时，将图片等比例压缩，使它可以完全显示出来
                    float ratio = width / (bitmapWidth * 1.0f);
                    matrix.postScale(ratio, ratio);
                    float translateY = (height - (bitmapHeight * ratio)) / 2f;
                    // 在纵坐标方向上进行偏移，以保证图片居中显示
                    matrix.postTranslate(0, translateY);
                    totalTranslateY = translateY;
                    totalRatio = initRatio = ratio;
                } else {
                    // 当图片高度大于屏幕高度时，将图片等比例压缩，使它可以完全显示出来
                    float ratio = height / (bitmapHeight * 1.0f);
                    matrix.postScale(ratio, ratio);
                    float translateX = (width - (bitmapWidth * ratio)) / 2f;
                    // 在横坐标方向上进行偏移，以保证图片居中显示
                    matrix.postTranslate(translateX, 0);
                    totalTranslateX = translateX;
                    totalRatio = initRatio = ratio;
                }
                currentBitmapWidth = bitmapWidth * initRatio;
                currentBitmapHeight = bitmapHeight * initRatio;
            } else {
                // 当图片的宽高都小于屏幕宽高时，直接让图片居中显示
                float translateX = (width - sourceBitmap.getWidth()) / 2f;
                float translateY = (height - sourceBitmap.getHeight()) / 2f;
                matrix.postTranslate(translateX, translateY);
                totalTranslateX = translateX;
                totalTranslateY = translateY;
                totalRatio = initRatio = 1f;
                currentBitmapWidth = bitmapWidth;
                currentBitmapHeight = bitmapHeight;
            }
            canvas.drawBitmap(sourceBitmap, matrix, null);
        }
    }

    /**
     * 对图片进行mask处理
     * 
     * @param canvas
     */
    protected void mask(Canvas canvas) {
        if (mMaskBitmapId == -1) {
            return;
        }
        if (mMaskBitmap == null) {
            mMaskBitmap = BitmapFactory.decodeResource(getResources(), mMaskBitmapId);
            mMaskBitmap = ImageUtil.zoomBitmap(mMaskBitmap, getWidth(), getHeight());
        }
        Paint paint = new Paint();
        // 仅仅绘制DST图片中，不和SRC图片相交的部分(完全不绘制SRC)
        paint.setXfermode(new PorterDuffXfermode(android.graphics.PorterDuff.Mode.DST_IN));
        // 这幅图片是SRC图片
        canvas.drawBitmap(mMaskBitmap, 0, 0, paint);
        paint.setXfermode(null);
    }

    /**
     * 对图片进行平移处理
     * 
     * @param canvas
     */
    private void move(Canvas canvas) {
        matrix.reset();
        // 根据手指移动的距离计算出总偏移值
        float translateX = totalTranslateX + movedDistanceX;
        float translateY = totalTranslateY + movedDistanceY;
        // 先按照已有的缩放比例对图片进行缩放
        matrix.postScale(totalRatio, totalRatio);
        // 再根据移动距离进行偏移
        matrix.postTranslate(translateX, translateY);
        totalTranslateX = translateX;
        totalTranslateY = translateY;
        canvas.drawBitmap(sourceBitmap, matrix, null);
    }

    /**
     * 根据currentStatus的值来决定对图片进行什么样的绘制操作。
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        switch (currentStatus) {
        case STATUS_ZOOM_OUT:
        case STATUS_ZOOM_IN:
            zoom(canvas);
            break;
        case STATUS_MOVE:
            move(canvas);
            break;
        case STATUS_INIT:
            initBitmap(canvas);
            onFinishInit();
        default:
            canvas.drawBitmap(sourceBitmap, matrix, null);
            break;
        }
        if (mIsMovingOrZooming) {
            if (listener != null) {
                listener.onMoveOrZoomListener(true, initRatio, totalRatio);
            }
        } else {
            if (listener != null) {
                listener.onMoveOrZoomListener(false, initRatio, totalRatio);
            }
            mask(canvas);
        }
    }

    /* 渲染完初始图片的回调函数 */
    protected void onFinishInit() {

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            // 分别获取到ZoomImageView的宽度和高度
            width = getWidth();
            height = getHeight();
        }
    }

    protected void onPreTouchEvent() {
        // 供子类在onTouch事件前进行某些处理
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        onPreTouchEvent();
        switch (event.getActionMasked()) {
        case MotionEvent.ACTION_POINTER_DOWN:
            if (event.getPointerCount() == 2) {
                // 当有两个手指按在屏幕上时，计算两指之间的距离
                lastFingerDis = distanceBetweenFingers(event);
            }
            break;
        case MotionEvent.ACTION_MOVE:
            mIsMovingOrZooming = true;
            if (event.getPointerCount() == 1) {
                onTouchEventSingleFingerMove(event);
            } else if (event.getPointerCount() == 2) {
                onTouchEventDoubleFingerMove(event);
            }
            break;
        case MotionEvent.ACTION_POINTER_UP:
            onTouchEventDoubleFingerUp(event);
            break;
        case MotionEvent.ACTION_UP:
            onTouchEventSingleFingerUp(event);
            break;
        case MotionEvent.ACTION_CANCEL:
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
        // 有两个手指按在屏幕上移动时，为缩放状态
        centerPointBetweenFingers(event);
        double fingerDis = distanceBetweenFingers(event);
        if (fingerDis > lastFingerDis) {
            currentStatus = STATUS_ZOOM_OUT;
        } else {
            currentStatus = STATUS_ZOOM_IN;
        }
        // 进行缩放倍数检查，最大只允许将图片放大4倍，最小可以缩小到初始化比例
        if ((currentStatus == STATUS_ZOOM_OUT && totalRatio < 4 * initRatio)
                || (currentStatus == STATUS_ZOOM_IN && totalRatio > initRatio)) {
            scaledRatio = (float) (fingerDis / lastFingerDis);
            totalRatio = totalRatio * scaledRatio;
            if (totalRatio > 4 * initRatio) {
                totalRatio = 4 * initRatio;
            } else if (totalRatio < initRatio) {
                totalRatio = initRatio;
            }
            // 调用onDraw()方法绘制图片
            invalidate();
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
        if (totalTranslateX + movedDistanceX > 0) {
            movedDistanceX = 0;
        } else if (width - (totalTranslateX + movedDistanceX) > currentBitmapWidth) {
            movedDistanceX = 0;
        }
        if (totalTranslateY + movedDistanceY > 0) {
            movedDistanceY = 0;
        } else if (height - (totalTranslateY + movedDistanceY) > currentBitmapHeight) {
            movedDistanceY = 0;
        }
        // 调用onDraw()方法绘制图片
        invalidate();
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

    public void setOnMoveOrZoomListener(OnMoveOrZoomListener l) {
        listener = l;
    }

    /**
     * 对图片进行缩放处理。
     * 
     * @param canvas
     */
    protected void zoom(Canvas canvas) {
        matrix.reset();
        // 将图片按总缩放比例进行缩放
        matrix.postScale(totalRatio, totalRatio);
        float scaledWidth = sourceBitmap.getWidth() * totalRatio;
        float scaledHeight = sourceBitmap.getHeight() * totalRatio;
        float translateX = 0f;
        float translateY = 0f;
        // 如果当前图片宽度小于屏幕宽度，则按屏幕中心的横坐标进行水平缩放。否则按两指的中心点的横坐标进行水平缩放
        if (currentBitmapWidth < width) {
            translateX = (width - scaledWidth) / 2f;
        } else {
            translateX = totalTranslateX * scaledRatio + centerPointX * (1 - scaledRatio);
            // 进行边界检查，保证图片缩放后在水平方向上不会偏移出屏幕
            if (translateX > 0) {
                translateX = 0;
            } else if (width - translateX > scaledWidth) {
                translateX = width - scaledWidth;
            }
        }
        // 如果当前图片高度小于屏幕高度，则按屏幕中心的纵坐标进行垂直缩放。否则按两指的中心点的纵坐标进行垂直缩放
        if (currentBitmapHeight < height) {
            translateY = (height - scaledHeight) / 2f;
        } else {
            translateY = totalTranslateY * scaledRatio + centerPointY * (1 - scaledRatio);
            // 进行边界检查，保证图片缩放后在垂直方向上不会偏移出屏幕
            if (translateY > 0) {
                translateY = 0;
            } else if (height - translateY > scaledHeight) {
                translateY = height - scaledHeight;
            }
        }
        // 缩放后对图片进行偏移，以保证缩放后中心点位置不变
        matrix.postTranslate(translateX, translateY);
        totalTranslateX = translateX;
        totalTranslateY = translateY;
        currentBitmapWidth = scaledWidth;
        currentBitmapHeight = scaledHeight;
        canvas.drawBitmap(sourceBitmap, matrix, null);
    }
}
