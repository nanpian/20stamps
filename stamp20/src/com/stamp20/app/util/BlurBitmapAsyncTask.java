package com.stamp20.app.util;

import android.graphics.Bitmap;
import android.os.AsyncTask;

public class BlurBitmapAsyncTask extends AsyncTask<Bitmap, Integer, Bitmap>{
    @Override
    protected Bitmap doInBackground(Bitmap... params) {
        Bitmap blurBitmap = null;
        try{
            blurBitmap = lenovo.jni.ImageUtils.fastBlur(params[0], 100);
        }catch(Exception e){
            Log.i(this, "error happened!");
            return null;
        }
        return blurBitmap;
    }
}
