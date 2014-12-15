package com.stamp20.app.cards;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.stamp20.app.BaseTitleActivity;
import com.stamp20.app.R;
import com.stamp20.app.util.Log;
import com.stamp20.app.view.ImageUtil;
import com.stamp20.app.view.ImageViewText;
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
    
    private ImageViewText mChoosePhoto;
    private ImageViewText mChooseTemplate;
    
    private static final int SELECT_PIC = 1;
    private static final int MSG_SELECT_PICTURE = 1000;
    
    private ImageViewText.OnClickListenerImageViewText onClickListenerImageViewText = new ImageViewText.OnClickListenerImageViewText() {
        @Override
        public void onClick(View arg0, int id) {
            com.stamp20.app.util.Log.d(this, "click");
            if(id == mChoosePhoto.getId()){
                com.stamp20.app.util.Log.d(this, "mChoosePhoto.click");
                selectPicture();
            }else if (id == mChooseTemplate.getId()){
                com.stamp20.app.util.Log.d(this, "mChooseTemplate.click");
            }
            
        }
    };
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards_main);
        zoomImageView = (ZoomImageView) findViewById(R.id.zoom_image_view);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.background_home_wedding);
        zoomImageView.setImageBitmap(bitmap);
        
        mChoosePhoto = (ImageViewText) this.findViewById(R.id.choose_photo);
        mChoosePhoto.setOnClickListenerImageViewText(onClickListenerImageViewText);
        mChooseTemplate = (ImageViewText) this.findViewById(R.id.choose_template);
        mChooseTemplate.setOnClickListenerImageViewText(onClickListenerImageViewText);
    }


    private void selectPicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, SELECT_PIC);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECT_PIC && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            mHandler.sendMessage(mHandler.obtainMessage(MSG_SELECT_PICTURE, uri));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case MSG_SELECT_PICTURE:
                Log.d(this, "handleMessage--MSG_SELECT_PICTURE");
                Uri uri = (Uri) msg.obj;
                bitmap = ImageUtil.loadDownsampledBitmap(CardsActivity.this, uri, 2);
                zoomImageView.setImageBitmap(bitmap);
                break;

            default:
                break;
            }
        }
        
    };
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 记得将Bitmap对象回收掉
        if (bitmap != null) {
            bitmap.recycle();
        }
    }
}
