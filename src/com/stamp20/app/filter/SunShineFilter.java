/**
 * 
 */ 
package com.stamp20.app.filter; 

import com.spore.meitu.jni.ImageUtilEngine;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

/** 
 * @author 作者 E-mail: 
 * @version 创建时间：2014-12-10 下午10:40:57 
 * 类说明 
 */

public class SunShineFilter implements IImageFilter {

	@Override
	public Bitmap process(Bitmap bitmap) {
		// TODO 处理日照效果
		ImageUtilEngine imageProcessEng = new ImageUtilEngine();
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] buf = new int[width * height];
        Math.pow(2, 1);
        bitmap.getPixels(buf, 0, width, 1, 1, width - 1, height - 1);
        int[] result = imageProcessEng.toSunshine(buf, width, height, 100, 100, 20, 150);
        return bitmap = Bitmap.createBitmap(result, width, height, Config.ARGB_8888);
	}

}
 