/**
 * 
 */
package com.stamp20.app.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ImageView;

/** 
 * @author 作者 E-mail: 
 * @version 创建时间：2014-12-16 上午12:06:30 
 * 类说明 
 */
/**
 * @author lenovo
 *
 */
public class FrameImageView extends ImageView {

    private int borderwidth;
    private int co;

    public FrameImageView(Context context) {
        super(context);
    }

    public FrameImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FrameImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 画边框
        Rect rec = canvas.getClipBounds();
        rec.bottom--;
        rec.right--;
        Paint paint = new Paint();
        // 设置边框颜色
        paint.setColor(co);
        paint.setStyle(Paint.Style.STROKE);
        // 设置边框宽度
        paint.setStrokeWidth(borderwidth);
        canvas.drawRect(rec, paint);
    }

    // 设置边框宽度
    public void setBorderWidth(int width) {

        borderwidth = width;
    }

    // 设置颜色
    public void setColour(int color) {
        co = color;
    }

}
