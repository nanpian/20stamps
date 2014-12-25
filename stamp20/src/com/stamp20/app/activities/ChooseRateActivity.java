package com.stamp20.app.activities;

import lenovo.jni.ImageUtils;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow.OnDismissListener;

import com.stamp20.app.R;
import com.stamp20.app.guide.AnimationUtil;
import com.stamp20.app.util.BitmapCache;
import com.stamp20.app.util.Log;
import com.stamp20.app.view.ChooseRatePopupWindow;
import com.stamp20.app.view.ChooseRateStampView;

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
        
        btnPostCard.setOnClickListener(this);
        btnLetter.setOnClickListener(this);
        btnMore.setOnClickListener(this);
        findViewById(R.id.tail).setOnClickListener(this);
        
        popupWindowInit();
    }
    
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.btn_more:
            Log.d(this, "onclick tail...");
            showPopupWindow();
            break;
        case R.id.tail:
            Intent intent = new Intent(this, ReviewActivity.class);
            startActivity(intent);
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
    private ChooseRatePopupWindow mCRP;
    private void popupWindowInit(){
        mChooseRateRoot = (LinearLayout) this.findViewById(R.id.choose_rate_root);
        mBlurImageView = (ImageView) this.findViewById(R.id.blur_background);
        if(mCRP == null){
            mCRP = new ChooseRatePopupWindow(getApplicationContext(), ChooseRateActivity.this.findViewById(R.id.root));
            mCRP.setOnDismissListener(new OnDismissListener() {
                @Override
                public void onDismiss() {
                    hidePopupWindow();
                }
            });
        }
    }
    
    private void hidePopupWindow(){
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
    
    private void showPopupWindow(){
        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected void onPreExecute() {
                mCRP.show();
            }

            /*
             * 第一个参数是doInBackground的输入参数
             * 第二个参数是用于输出中间计算进度的参数
             * 第三个参数是说明doInBackground的返回参数和onPostExecute的输入参数
             * */
            @Override
            protected Bitmap doInBackground(Void... unused) {
                mChooseRateRoot.buildDrawingCache();
                Bitmap source = mChooseRateRoot.getDrawingCache();
                return ImageUtils.fastBlur(source, 100);
            }
            
            @Override
            protected void onPostExecute(Bitmap result) {
                mBlurImageView.setVisibility(View.VISIBLE);
                mBlurImageView.setImageBitmap(result);
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
        }.execute();
        
    }
    /*和底部弹出的PopupWindow相关 END*/
}
