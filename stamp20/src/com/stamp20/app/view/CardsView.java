package com.stamp20.app.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class CardsView extends ZoomImageView {

    public CardsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 对图片进行mask处理
     * 
     * 为什么要重写这个方法？ 假设原始图片A如下： mask图片B如下: 则操作后得到的A图如下： ------------ ------------
     * ----- | | | | | 透 | | | | | | | | 明 | | | | | | ----- | -----
     * ------------ ------------
     * 
     * 但是上面的mask完以后，会有一个【bug，就是A图原来去掉的地方的颜色无法更改。！！！！！】
     * 因此下面重写的这个方法的原理是和mask图片做反向mask，得到如下的一个颜色
     * 是我们指定的一个bitmap，然后再将此bitmap绘制到原来我们的A图上，达到反向mask的效果 --- ------ | | | | | |
     * | | | ----- | ------------
     * 
     * @param canvas
     */
    @Override
    protected void mask(Canvas canvas) {
        if (mMaskBitmapId == -1 || mMaskColorId == -1) {
            return;
        }
        if (mMaskBitmap == null) {
            mMaskBitmap = BitmapFactory.decodeResource(getResources(),
                    mMaskBitmapId);
            mMaskBitmap = ImageUtil.zoomBitmap(mMaskBitmap, getWidth(),
                    getHeight());
            Bitmap dstB = Bitmap.createBitmap(getWidth(), getHeight(),
                    Config.ARGB_8888);
            Canvas tempC = new Canvas(dstB);
            // 将此bitmap的颜色设为纯色
            tempC.drawColor(getResources().getColor(mMaskColorId));
            mMaskBitmap = ImageUtil.maskingImage(dstB, mMaskBitmap);
        }
        canvas.drawBitmap(mMaskBitmap, 0, 0, null);
    }

    /**
     * 对图片进行缩放处理。
     * 
     * 
     * 
     * 坐标系从左上角开始
     * 
     * -------> | | | * | | | |---- ↓
     * 
     * @param canvas
     */
    @Override
    protected void zoom(Canvas canvas) {
        matrix.reset();
        // 将图片按总缩放比例进行缩放
        matrix.postScale(totalRatio, totalRatio);
        float scaledWidth = sourceBitmap.getWidth() * totalRatio;
        float scaledHeight = sourceBitmap.getHeight() * totalRatio;
        float translateX = 0f;
        float translateY = 0f;

        translateX = totalTranslateX * scaledRatio + centerPointX
                * (1 - scaledRatio);
        if (translateX > 0 + width * 0.5f) {
            // 移动超过一半了
            translateX = 0 + width * 0.5f;
        } else if (width * 0.5f - translateX > scaledWidth) {
            translateX = width * 0.5f - scaledWidth;
        }

        translateY = totalTranslateY * scaledRatio + centerPointY
                * (1 - scaledRatio);
        if (translateY > 0 + height * 0.5f) {
            translateY = 0 + height * 0.5f;
        } else if (height * 0.5f - translateY > scaledHeight) {
            translateY = height * 0.5f - scaledHeight;
        }

        // 缩放后对图片进行偏移，以保证缩放后中心点位置不变
        matrix.postTranslate(translateX, translateY);
        totalTranslateX = translateX;
        totalTranslateY = translateY;
        currentBitmapWidth = scaledWidth;
        currentBitmapHeight = scaledHeight;
        canvas.drawBitmap(sourceBitmap, matrix, null);
    }

    @Override
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
        if (totalTranslateX + movedDistanceX > width * 0.5f) {
            movedDistanceX = 0;
        } else if (width - (totalTranslateX + movedDistanceX) > currentBitmapWidth
                + width * 0.5f) {
            movedDistanceX = 0;
        }
        if (totalTranslateY + movedDistanceY > height * 0.5f) {
            movedDistanceY = 0;
        } else if (height - (totalTranslateY + movedDistanceY) > currentBitmapHeight
                + height * 0.5f) {
            movedDistanceY = 0;
        }
        // 调用onDraw()方法绘制图片
        invalidate();
        lastXMove = xMove;
        lastYMove = yMove;
    }
}
