package com.stamp20.app.activities;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
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
import android.widget.RelativeLayout;
import android.widget.ImageView.ScaleType;

import com.stamp20.app.BaseTitleActivity;
import com.stamp20.app.R;
import com.stamp20.app.util.Constant;
import com.stamp20.app.util.FontManager;
import com.stamp20.app.util.PhotoFromWhoRecorder;
import com.stamp20.app.view.ImageUtil;

public class HomeActivity extends BaseTitleActivity implements View.OnClickListener{

    private ArrayList<Integer> mDrawableIDs = new ArrayList<Integer>();
    /*private int[] mIds = new int[] { R.drawable.background_home_baby_shower, R.drawable.background_home_birthday, R.drawable.background_home_kids,  
            R.drawable.background_home_pets, R.drawable.background_home_save_the_date, R.drawable.background_home_wedding };*/  
    private Drawable[] mDrawables;  
    private ImageView mBackgroundImageView;  
    private int mCurrentPicNum = 0;
    private Button mButtonGreen;
    private Button mButtonRed;
    
    private final static int ANIMATION_DURATION = 1000;
    private final static int CHANGE_PICTURE_DURATION = 2000;
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
                        mDrawables[mCurrentPicNum % mDrawableIDs.size()],//实现从0 1 2 3 4 5 0 1 2.。。这样的不停转变  
                        mDrawables[(mCurrentPicNum + 1) % mDrawableIDs.size()] });  
                mCurrentPicNum++;  
                mBackgroundImageView.setImageDrawable(transitionDrawable);
                /*mBackgroundImageView.setScaleType(ScaleType.CENTER_CROP);*/
                transitionDrawable.startTransition(ANIMATION_DURATION);  
            }
        }
    };
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreateNoTitle(); 
        setContentView(R.layout.activity_home);
        FontManager.changeFonts((RelativeLayout)findViewById(R.id.root), this);
        mButtonGreen = (Button) this.findViewById(R.id.green_button);
        mButtonGreen.setOnClickListener(this);
        mButtonRed = (Button) this.findViewById(R.id.red_button);
        mButtonRed.setOnClickListener(this);
        mBackgroundImageView = (ImageView) this.findViewById(R.id.background);
        mBackgroundImageView.setScaleType(ScaleType.FIT_XY);
        mBackgroundImageView.setImageDrawable(getResources().getDrawable(R.drawable.background_home_baby_shower));
        initBackgroundArrays();
        
        /*获得合适的drawable资源*/  
        BitmapFactory.Options opts = new BitmapFactory.Options();  
        opts.inJustDecodeBounds = true;  
        BitmapFactory.decodeResource(getResources(), R.drawable.background_home_baby_shower, opts);  
        opts.inSampleSize = ImageUtil.computeSampleSize(opts, -1, 1280 * 960);  
        opts.inJustDecodeBounds = false;  
        mDrawables=new Drawable[mDrawableIDs.size()];  
        try {  
            for (int i = 0; i < mDrawableIDs.size(); i++) {// for循环，加载5个drawable资源  
                Bitmap bmp = BitmapFactory.decodeResource(getResources(), mDrawableIDs.get(i), opts);  
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
                            
                        }, CHANGE_PICTURE_DURATION - ANIMATION_DURATION, CHANGE_PICTURE_DURATION);
    }

    private void initBackgroundArrays(){
        TypedArray typedArray = getResources().obtainTypedArray(R.array.home_background);
        if( null != typedArray ){
            if(Constant.debugXixiaLog()){
                Log.i("xixia", "typedArray:"+typedArray.length());
            }
            for(int i=0; i<typedArray.length(); i++){
                mDrawableIDs.add(typedArray.getResourceId(i, 0));
            }
        }
    }
    
    @Override
    public void onClick(View v) {
        if(v.getId() == mButtonGreen.getId()){
            startActivity(new Intent(HomeActivity.this,
                    ImageLoaderActivity.class));
            PhotoFromWhoRecorder.recordFromWhich(getApplicationContext(), "stamp");
        }else if(v.getId() == mButtonRed.getId()){
            startActivity(new Intent(HomeActivity.this,
                    CardsTemplateChooseActivity.class));
        	//startActivity(new Intent(HomeActivity.this,
             //       OpenGL10ZoomMoveDemo.class));
            PhotoFromWhoRecorder.recordFromWhich(getApplicationContext(), "card");
        }
    }
}
