/**
 * 
 */ 
package com.stamp20.app.filter; 

import com.spore.meitu.jni.ImageUtilEngine;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

/** 
 * @author 作者 E-mail: 
 * @version 创建时间：2014-12-10 下午11:30:34 
 * 类说明 
 */

public class BlackWhiteFilter implements IImageFilter {

	@Override
	public Bitmap process(Bitmap bitmap) {
		// TODO filter for black and white photo
		ImageUtilEngine imageEngine = new ImageUtilEngine();
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] buf = new int[width * height];
        bitmap.getPixels(buf, 0, width, 1, 1, width - 1, height - 1);
        int[] result = imageEngine.toHeibai(buf, width, height);
        bitmap = Bitmap.createBitmap(result, width, height, Config.ARGB_8888);
        return bitmap;
	}

}
 