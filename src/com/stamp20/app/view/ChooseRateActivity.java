package com.stamp20.app.view;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;

import com.stamp20.app.R;
import com.stamp20.app.util.BitmapCache;

public class ChooseRateActivity extends Activity{

    Bitmap stampBitmap;
    ChooseRateStampView chooseRateStampView;
    Button btnPostCard;
    Button btnLetter;
    Button btnMore;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_rate);
        chooseRateStampView = (ChooseRateStampView) findViewById(R.id.view_stamp);
        btnPostCard = (Button) findViewById(R.id.btn_post_card);
        btnLetter = (Button) findViewById(R.id.btn_letter);
        btnMore = (Button) findViewById(R.id.btn_more);
        stampBitmap = BitmapCache.getCache().get();
        chooseRateStampView.setBmpStampPhoto(stampBitmap);
    }
    
    

}
