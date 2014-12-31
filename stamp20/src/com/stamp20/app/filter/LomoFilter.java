/**
 * 
 */ 
package com.stamp20.app.filter; 

import com.stamp20.app.filter.BrightContrastFilter;
import com.stamp20.app.filter.GradientMapFilter;
import com.stamp20.app.filter.IImageFilter;
import com.stamp20.app.util.Image;
import com.stamp20.app.filter.ImageBlender;
import com.stamp20.app.filter.NoiseFilter;
import com.stamp20.app.filter.VignetteFilter;
import com.stamp20.app.filter.ImageBlender.BlendMode;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Bitmap.Config;

/** 
 * @author 作者 E-mail: 
 * @version 创建时间：2014-12-23 下午5:10:01 
 * 类说明 
 */

public class LomoFilter implements IImageFilter{
	@Override
	public Bitmap process(Bitmap bitmap) {
		// TODO 照片泛蓝效果
	        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
	                bitmap.getHeight(), Config.RGB_565);

	        Canvas canvas = new Canvas(output);

	        Paint paint = new Paint();        
	        ColorMatrix cm = new ColorMatrix();
	        float[] array = {1,0,0,0,0,
	                0,1,0,0,50,
	                0,0,1,0,0,
	                0,0,0,1,0};
	        cm.set(array);
	        paint.setColorFilter(new ColorMatrixColorFilter(cm));

	        canvas.drawBitmap(bitmap, 0, 0, paint);
	        return output;
	}
}

 