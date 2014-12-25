/**
 * 
 */ 
package com.stamp20.app.util; 

import android.content.Context;
import android.graphics.Typeface;

/** 
 * @author 作者 E-mail: 
 * @version 创建时间：2014-12-25 下午4:41:22 
 * 类说明 
 */

public class StampUtil {
	
    private static Typeface mTypeface = null;

    private static Typeface mHelveticaTypeface = null;
	
    public static void initHelveticaTypeface(Context context) {
        if (null == mHelveticaTypeface && context != null) mHelveticaTypeface = Typeface.createFromAsset(context.getAssets(),
                "fonts/OpenSans-Bold.ttf");
    }

    public static Typeface getHelveticaTypeface() {
        return mHelveticaTypeface;
    }
}
 