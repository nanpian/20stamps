package com.stamp20.app.cards;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.stamp20.app.BaseTitleActivity;
import com.stamp20.app.R;
import com.stamp20.app.view.ZoomImageView;

public class CardsActivity extends Activity{

    /**
     * 自定义的ImageView控制，可对图片进行多点触控缩放和拖动
     */
    private ZoomImageView zoomImageView;
    
    /**
     * 待展示的图片
     */
    private Bitmap bitmap;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards_main);
        zoomImageView = (ZoomImageView) findViewById(R.id.zoom_image_view);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.background_home_wedding);
        zoomImageView.setImageBitmap(bitmap);
    }
    
    
    
}
