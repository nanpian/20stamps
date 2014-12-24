/**
 * 
 */ 
package com.stamp20.app.filter; 

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;


/** 
 * @author 作者 E-mail: 
 * @version 创建时间：2014-12-10 下午10:40:37 
 * 类说明 
 */

public class GrayFilter implements IImageFilter {


	@Override
	public Bitmap process(Bitmap bmpOriginal) {
		// 灰度
			int width, height;
			height = bmpOriginal.getHeight();
			width = bmpOriginal.getWidth();

			Bitmap bmpGrayscale = Bitmap.createBitmap(width, height,
					Bitmap.Config.RGB_565);
			Canvas c = new Canvas(bmpGrayscale);
			Paint paint = new Paint();
			ColorMatrix cm = new ColorMatrix();
			cm.setSaturation(0);
			ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
			paint.setColorFilter(f);
			c.drawBitmap(bmpOriginal, 0, 0, paint);
			return bmpGrayscale;
	}

}
 