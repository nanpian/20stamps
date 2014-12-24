package com.stamp20.app.view;

import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.stamp20.app.R;
import com.stamp20.app.activities.BlurTestActivity;
import com.stamp20.app.guide.AnimationUtil;
import com.stamp20.app.util.BitmapCache;
import com.stamp20.app.util.BlurBitmapAsyncTask;
import com.stamp20.app.util.Log;

public class ChooseRateActivity extends Activity implements View.OnClickListener{

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
        findViewById(R.id.tail).setOnClickListener(this);
        
        popupWindowInit();
    }
    
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.tail:
            Log.d(this, "onclick tail...");
            /*Intent intent = new Intent(this, BlurTestActivity.class);
            startActivity(intent);*/
            showPopupWindow();
            break;
        default:
            break;
        }
    }
    
    

    /*和底部弹出的PopupWindow相关 START*/
    private ImageView mBlurImageView;
    private LinearLayout mChooseRateRoot;
    private float mStartAlpha = 0.001f;
    private float mEndAlpha = 1.0f;
    private long mDuration = 1000;
    private void popupWindowInit(){
        mChooseRateRoot = (LinearLayout) this.findViewById(R.id.choose_rate_root);
        mBlurImageView = (ImageView) this.findViewById(R.id.blur_background);
        mBlurImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mBlurImageView.startAnimation(AnimationUtil.getAlphaAnimation(mEndAlpha, mStartAlpha, false, mDuration, 
                        new AnimationListener() {
                    
                    @Override
                    public void onAnimationStart(Animation animation) {}
                    
                    @Override
                    public void onAnimationRepeat(Animation animation) {}
                    
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        mBlurImageView.setClickable(false);
                        mBlurImageView.setVisibility(View.INVISIBLE);
                    }
                }));
            }
        });
    }
    
    private void showPopupWindow(){
        mChooseRateRoot.buildDrawingCache();
        Bitmap source = mChooseRateRoot.getDrawingCache();
        
        mBlurImageView.setVisibility(View.VISIBLE);
        try {
            mBlurImageView.setImageBitmap(new BlurBitmapAsyncTask().execute(source).get());
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.i(this, "blur bitmap error InterruptedException");
            return;
        } catch (ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.i(this, "blur bitmap error ExecutionException");
            return;
        }
        mBlurImageView.startAnimation(AnimationUtil.getAlphaAnimation(mStartAlpha, mEndAlpha, false, mDuration, 
                new AnimationListener() {
            
            @Override
            public void onAnimationStart(Animation animation) {}
            
            @Override
            public void onAnimationRepeat(Animation animation) {}
            
            @Override
            public void onAnimationEnd(Animation animation) {
                mBlurImageView.setClickable(true);
            }
        }));
    }
    /*和底部弹出的PopupWindow相关 END*/
}
