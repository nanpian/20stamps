package com.stamp20.app.activities;

import lenovo.jni.ImageUtils;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import com.stamp20.app.R;

public class BlurTestActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blur_test);
        ImageView img = (ImageView) findViewById(R.id.img_blur);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.background_home_pets);
        //调用JNI接口对bitmap高斯模糊
        bitmap = ImageUtils.fastBlur(bitmap, 50);
        img.setImageBitmap(bitmap);
    }
}
