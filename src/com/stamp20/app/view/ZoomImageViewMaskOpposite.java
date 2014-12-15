package com.stamp20.app.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;

public class ZoomImageViewMaskOpposite extends ZoomImageView{

    public ZoomImageViewMaskOpposite(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 对图片进行mask处理
     * 
     * 为什么要重写这个方法？
     * 假设原始图片A如下：    mask图片B如下:   则操作后得到的A图如下：
     * ------------        ------------      -----
     * |          |        | |   | 透  |      |   |
     * |          |        | |   | 明  |      |   |
     * |          |        | -----    |      -----
     * ------------        ------------
     *      
     * 但是上面的mask完以后，会有一个【bug，就是A图原来去掉的地方的颜色无法更改。！！！！！】
     * 因此下面重写的这个方法的原理是和mask图片做反向mask，得到如下的一个颜色
     * 是我们指定的一个bitmap，然后再将此bitmap绘制到原来我们的A图上，达到反向mask的效果
     * ---   ------      
     * | |   |    |      
     * | |   |    |    
     * | -----    |     
     * ------------       
     * 
     * @param canvas
     */
    @Override
    protected void mask(Canvas canvas) {
        if(mMaskBitmapId == -1 || mMaskColorId == -1) {
            return;
        }
        if(mMaskBitmap == null){
            mMaskBitmap = BitmapFactory.decodeResource(getResources(), mMaskBitmapId);
            mMaskBitmap = ImageUtil.zoomBitmap(mMaskBitmap, getWidth(), getHeight());    
            Bitmap dstB = Bitmap.createBitmap(getWidth(), getHeight(), Config.ARGB_8888);
            Canvas tempC = new Canvas(dstB);
            /*将此bitmap的颜色设为纯色*/
            tempC.drawColor(getResources().getColor(mMaskColorId));
            mMaskBitmap = ImageUtil.maskingImage(dstB, mMaskBitmap);
        }
        canvas.drawBitmap(mMaskBitmap, 0, 0, null);
    }
}
