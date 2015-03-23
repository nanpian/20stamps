package com.stamp20.app.activities;

import lenovo.jni.ImageUtils;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint.FontMetricsInt;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.PopupWindow.OnDismissListener;

import com.stamp20.app.R;
import com.stamp20.app.anim.AnimationUtil;
import com.stamp20.app.data.Design;
import com.stamp20.app.util.BitmapCache;
import com.stamp20.app.util.Constant;
import com.stamp20.app.util.FontManager;
import com.stamp20.app.util.Log;
import com.stamp20.app.view.ChooseRatePopupWindow;
import com.stamp20.app.view.ChooseRateStampView;

public class ChooseRateActivity extends Activity implements View.OnClickListener, ChooseRatePopupWindow.OnRateSelecedListener{

    private static final String titleName = "Choose a Rate";
	Bitmap stampBitmap;
    ChooseRateStampView chooseRateStampView;
    /*Button btnPostCard;
    Button btnLetter;
    Button btnMore;*/
    View mPostCard;
    View mLetter;
    View mMore;
    TextView mTextSelectRate;
	private ImageView headerPrevious;
	private TextView headerTitle;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_rate);
        chooseRateStampView = (ChooseRateStampView) findViewById(R.id.view_stamp);
        FontManager.changeFonts((RelativeLayout)findViewById(R.id.root), this);
		headerPrevious = (ImageView) findViewById(R.id.header_previous);
		headerPrevious.setOnClickListener(this);
		headerTitle = (TextView) findViewById(R.id.header_title);
		headerTitle.setText(titleName);
        /*btnPostCard = (Button) findViewById(R.id.btn_post_card);
        btnLetter = (Button) findViewById(R.id.btn_letter);
        btnMore = (Button) findViewById(R.id.btn_more);*/
        stampBitmap = BitmapCache.getCache().get();
        if( stampBitmap !=null ) {
            Log.i("bitmapcache", "onDrawFrame, stampbitmap get is " + stampBitmap.getWidth());
        } else {
        	Log.i("bitmapchache", "onDrawFrame, stampbitmap is " + stampBitmap);
        }
        chooseRateStampView.setImageBitmap(stampBitmap);
        findViewById(R.id.tail).setOnClickListener(this);
        findViewById(R.id.tail_icon).setVisibility(View.GONE);
        ((TextView)findViewById(R.id.tail_text)).setText(R.string.next_review);;
        mStampViewIsHorizontal = getIntent().getBooleanExtra(Constant.STAMP_IS_HORIZONTAL, true);
        chooseRateStampView.startBuilRateBitmapTask(mStampViewIsHorizontal);
        
        popupWindowInit();
        
        mPostCard = this.findViewById(R.id.btn_post_card_bg);//.setOnClickListener(this);
        mLetter = this.findViewById(R.id.btn_letter_bg);//.setOnClickListener(this);
        mMore = this.findViewById(R.id.btn_more_bg);//.setOnClickListener(this);
        
        buttonInit(mPostCard, mLetter, mMore);
        mTextSelectRate = (TextView) this.findViewById(R.id.text_select_rate);
    }
    
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.btn_more_bg:
            Log.d(this, "onclick tail...");
            showPopupWindow();
            break;
        case R.id.tail:
            drawRate2BitmapCache();
            Intent intent = new Intent(this, ReviewActivity.class);
            startActivity(intent);
            break;
        case R.id.header_previous:
        	finish();
        	break;
        case R.id.btn_post_card_bg:
        	mTextSelectRate.setText("Post Card");
            chooseRateStampView.setRateBitmapId(0, mStampViewIsHorizontal);
        	break;
        case R.id.btn_letter_bg:
        	mTextSelectRate.setText("1st Class Letter 1oz");
            chooseRateStampView.setRateBitmapId(1, mStampViewIsHorizontal);
        	break;
        default:
            break;
        }
    }
    
    

    /*和底部弹出的PopupWindow相关 START*/
    
    private void drawRate2BitmapCache(){
        if(chooseRateStampView.getRateBitmap() == null){
            return;
        }
        Canvas c = new Canvas(BitmapCache.getCache().get());
        c.drawBitmap(chooseRateStampView.getRateBitmap(), 
        		chooseRateStampView.getRateXMove(), chooseRateStampView.getRateYMove(), null);
        c.setBitmap(null);
    }
    
    private void buttonInit(View... btns){
        for(int i=0; i<btns.length; i++){
            final View v = btns[i];
            v.setOnClickListener(this);
            /*btn.setTextColor(this.getResources().getColorStateList(R.color.sel_cards_choose_button));*/
        }
    }
    private boolean mStampViewIsHorizontal = false;
    private ImageView mBlurImageView;
    private LinearLayout mChooseRateRoot;
    private float mStartAlpha = 0.001f;
    private float mEndAlpha = 1.0f;
    private long mDuration = 500;
    private ChooseRatePopupWindow mCRP;
    private void popupWindowInit(){
        mChooseRateRoot = (LinearLayout) this.findViewById(R.id.choose_rate_root);
        mBlurImageView = (ImageView) this.findViewById(R.id.blur_background);
        if(mCRP == null){
            mCRP = new ChooseRatePopupWindow(getApplicationContext(), ChooseRateActivity.this.findViewById(R.id.root), mStampViewIsHorizontal);
            mCRP.setOnRateSelecedListener(this);
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
                mBlurImageView.setVisibility(View.GONE);
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
    
    @Override
    public void onRateSelecedListener(int id, boolean isH) {
    	String[] rateArray = this.getResources().getStringArray(R.array.stamp_rate_title);
    	mTextSelectRate.setText(rateArray[id]);
        chooseRateStampView.setRateBitmapId(id, isH);
        Design.getInstance().setPrice(Integer.valueOf(rateArray[id]));
        mCRP.dismiss();
    }
    /*和底部弹出的PopupWindow相关 END*/
}
