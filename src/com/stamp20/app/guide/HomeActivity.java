package com.stamp20.app.guide;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.stamp20.app.BaseTitleActivity;
import com.stamp20.app.R;
import com.stamp20.app.activities.MainActivity;
import com.stamp20.app.view.ImageUtil;

public class HomeActivity extends BaseTitleActivity implements View.OnClickListener{

    private int[] mIds = new int[] { R.drawable.background_home_baby_shower, R.drawable.background_home_birthday, R.drawable.background_home_kids,  
            R.drawable.background_home_pets, R.drawable.background_home_save_the_date, R.drawable.background_home_wedding };  
    private Drawable[] mDrawables;  
    private ImageView mBackgroundImageView;  
    private int mCurrentPicNum = 0;
    private Button mButtonGreen;
    private Button mButtonRed;
    
    private final static int ANIMATION_DURATION = 1500;
    private final static int CHANGE_PICTURE_DURATION = 2500;
    private final static float MAX_ALPHA = 1.0f;
    private final static float MIN_ALPHA_0 = 0.0f;
    private final static float MIN_ALPHA_1 = 0.1f;
    
    private final static int SWITCH_CURRENT_PICTURE = 1; 
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == SWITCH_CURRENT_PICTURE){
                TransitionDrawable transitionDrawable=null;  
                transitionDrawable= new TransitionDrawable(new Drawable[] {  
                        mDrawables[mCurrentPicNum%mIds.length],//实现从0 1 2 3 4 5 0 1 2.。。这样的不停转变  
                        mDrawables[(mCurrentPicNum+1)%mIds.length] });  
                mCurrentPicNum++;  
                mBackgroundImageView.setImageDrawable(transitionDrawable);  
                transitionDrawable.startTransition(ANIMATION_DURATION);  
            }
        }
    };
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getActionBar().hide();
        mButtonGreen = (Button) this.findViewById(R.id.green_button);
        mButtonGreen.setOnClickListener(this);
        mButtonRed = (Button) this.findViewById(R.id.red_button);
        mButtonRed.setOnClickListener(this);
        mBackgroundImageView = (ImageView) this.findViewById(R.id.background);
        /*获得合适的drawable资源*/  
        BitmapFactory.Options opts = new BitmapFactory.Options();  
        opts.inJustDecodeBounds = true;  
        BitmapFactory.decodeResource(getResources(), R.drawable.background_home_baby_shower, opts);  
        opts.inSampleSize = ImageUtil.computeSampleSize(opts, -1, 1280 * 960);  
        opts.inJustDecodeBounds = false;  
        mDrawables=new Drawable[mIds.length];  
        try {  
            for (int i = 0; i < mIds.length; i++) {// for循环，加载5个drawable资源  
                Bitmap bmp = BitmapFactory.decodeResource(getResources(), mIds[i], opts);  
                mDrawables[i] = new BitmapDrawable(bmp);  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        
        new Timer().schedule(new TimerTask(){
                            @Override
                            public void run() {
                                mHandler.sendEmptyMessage(SWITCH_CURRENT_PICTURE);
                            }
                            
                        }, 1000, CHANGE_PICTURE_DURATION);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == mButtonGreen.getId() || v.getId() == mButtonRed.getId()){
            startActivity(new Intent(HomeActivity.this,
                    MainActivity.class));
        }
    }
}
