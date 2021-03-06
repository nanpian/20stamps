package com.stamp20.app.activities;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

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
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.stamp20.app.BaseTitleActivity;
import com.stamp20.app.R;
import com.stamp20.app.Stamp20Application;
import com.stamp20.app.data.Cart;
import com.stamp20.app.util.FontManager;
import com.stamp20.app.util.PhotoFromWhoRecorder;
import com.stamp20.app.view.ImageUtil;
import com.stamp20.app.view.RoundNumber;
import com.stamp20.gallary.GallaryActivity;

/**
 * 
 * 首页动画变化，这里需要关注内存泄露的问题
 * 
 * @author zhudewei
 *
 */
public class HomeActivity extends BaseTitleActivity implements View.OnClickListener {

    private ArrayList<Integer> mDrawableIDs = new ArrayList<Integer>();
    /*
     * private int[] mIds = new int[] { R.drawable.background_home_baby_shower,
     * R.drawable.background_home_birthday, R.drawable.background_home_kids,
     * R.drawable.background_home_pets,
     * R.drawable.background_home_save_the_date,
     * R.drawable.background_home_wedding };
     */
    private long firstTime = 0;
    private BitmapDrawable[] mDrawables;
    private ImageView mBackgroundImageView;
    private int mCurrentPicNum = 0;
    private ViewGroup mLinearLayoutHasLocalDesign;
    private ViewGroup mLinearLayoutNoLocalDesign;
    private Button btnPostage, btnPostageStamp;
    private Button btnCards, btnGettingCards;
    private Button btnViewCart;
    private ImageButton btnCoupons, btnAbout;
    private RoundNumber mLocalDesignNumber;
    private LinearLayout btn_viewcart_layout;

    private final static int ANIMATION_DURATION = 1000;
    private final static int CHANGE_PICTURE_DURATION = 2000;
    private Boolean isPaused = false;

    private final int SWITCH_CURRENT_PICTURE = 1;

    private Timer timer;
    private TimerTask timerTask;

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (isPaused)
                return;
            if (msg.what == SWITCH_CURRENT_PICTURE) {
                TransitionDrawable transitionDrawable = null;
                transitionDrawable = new TransitionDrawable(new Drawable[] {
                        mDrawables[mCurrentPicNum % mDrawableIDs.size()],
                        mDrawables[(mCurrentPicNum + 1) % mDrawableIDs.size()] });
                mCurrentPicNum++;
                mBackgroundImageView.setImageDrawable(transitionDrawable);
                /* mBackgroundImageView.setScaleType(ScaleType.CENTER_CROP); */
                transitionDrawable.startTransition(ANIMATION_DURATION);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreateNoTitle();
        Stamp20Application.getInstance().addActivity(this);
        setContentView(R.layout.activity_home);
        FontManager.changeFonts((RelativeLayout) findViewById(R.id.root), this);
        btnCoupons = (ImageButton) findViewById(R.id.btn_coupons);
        btnAbout = (ImageButton) findViewById(R.id.btn_about);
        mLinearLayoutHasLocalDesign = (ViewGroup) findViewById(R.id.layout_has_local_design);
        mLinearLayoutNoLocalDesign = (ViewGroup) findViewById(R.id.layout_has_no_local_design);
        btnPostage = (Button) findViewById(R.id.btn_postage);
        btnPostageStamp = (Button) findViewById(R.id.btn_postage_stamp);
        btnCards = (Button) findViewById(R.id.btn_cards);
        btnGettingCards = (Button) findViewById(R.id.btn_getting_cards);
        // btnViewCart = (Button) findViewById(R.id.btn_view_cart);
        mLocalDesignNumber = (RoundNumber) findViewById(R.id.local_design_number);
        btn_viewcart_layout = (LinearLayout) findViewById(R.id.btn_viewcart_layout);

        btn_viewcart_layout.setOnClickListener(this);
        btn_viewcart_layout.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btn_viewcart_layout.setBackgroundResource(R.drawable.dra_home_view_cart_button_pressed_true);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btn_viewcart_layout.setBackgroundResource(R.drawable.dra_home_view_cart_button_pressed_false);
                }
                return false;
            }
        });
        FontManager.changeFonts((LinearLayout) findViewById(R.id.btn_viewcart_layout), this);

        btnCoupons.setOnClickListener(this);
        btnAbout.setOnClickListener(this);
        btnPostage.setOnClickListener(this);
        btnPostageStamp.setOnClickListener(this);
        btnCards.setOnClickListener(this);
        btnGettingCards.setOnClickListener(this);

        mBackgroundImageView = (ImageView) this.findViewById(R.id.background);
        mBackgroundImageView.setScaleType(ScaleType.FIT_XY);
        mBackgroundImageView.setImageDrawable(getResources().getDrawable(R.drawable.background_home_baby_shower));
        initBackgroundArrays();

    }

    /**
     * 图片切换开始
     */
    private void startTimerTask() {
        if (timerTask == null) {
            timer = new Timer();
            timerTask = new TimerTask() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    mHandler.sendEmptyMessage(SWITCH_CURRENT_PICTURE);
                }
            };
            timer.schedule(timerTask, CHANGE_PICTURE_DURATION - ANIMATION_DURATION, CHANGE_PICTURE_DURATION);
        }

    }

    /**
     * 图片切换结束
     */
    private void stopTimerTask() {
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    /**
     * 加载图片
     */
    @SuppressWarnings("deprecation")
    private void loadImageMem() {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), R.drawable.background_home_baby_shower, opts);
        opts.inSampleSize = ImageUtil.computeSampleSize(opts, -1, 1280 * 960);
        opts.inJustDecodeBounds = false;
        mDrawables = new BitmapDrawable[mDrawableIDs.size()];
        try {
            for (int i = 0; i < mDrawableIDs.size(); i++) {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), mDrawableIDs.get(i), opts);
                mDrawables[i] = new BitmapDrawable(bitmap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateLayout() {
        Cart c = Cart.getInstance();
        if (c.isEmpty()) {
            mLinearLayoutHasLocalDesign.setVisibility(View.GONE);
            mLinearLayoutNoLocalDesign.setVisibility(View.VISIBLE);
        } else {
            mLinearLayoutNoLocalDesign.setVisibility(View.GONE);
            mLinearLayoutHasLocalDesign.setVisibility(View.VISIBLE);
            mLocalDesignNumber.setText(String.valueOf(c.getCount()));
        }
    }

    private void initBackgroundArrays() {
        TypedArray typedArray = getResources().obtainTypedArray(R.array.home_background);
        if (null != typedArray) {
            for (int i = 0; i < typedArray.length(); i++) {
                mDrawableIDs.add(typedArray.getResourceId(i, 0));
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.btn_coupons:
            coupons();
            finish();
            break;
        case R.id.btn_about:
            showAboutInfo();
            finish();
            break;
        case R.id.btn_cards:
        case R.id.btn_getting_cards:
            startActivity(new Intent(HomeActivity.this, CardsTemplateChooseActivity.class));
            PhotoFromWhoRecorder.recordFromWhich(getApplicationContext(), "card");
            finish();
            break;

        case R.id.btn_postage:
        case R.id.btn_postage_stamp:
            startActivity(new Intent(HomeActivity.this, GallaryActivity.class));
            PhotoFromWhoRecorder.recordFromWhich(getApplicationContext(), "stamp");
            finish();
            break;
        case R.id.btn_viewcart_layout:
            Intent intent = new Intent(HomeActivity.this, ShopCartItemsActivity.class);
            intent.putExtra("from", "home");
            startActivity(intent);
            finish();
            break;
        }
    }

    private void coupons() {
        Intent couponsIntent = new Intent(this, CouponsActivity.class);
        startActivity(couponsIntent);
        overridePendingTransition(R.anim.bottom_in, R.anim.bottom_out);
    }

    private void showAboutInfo() {
        Intent aboutIntent = new Intent(this, AboutActivity.class);
        // Intent aboutIntent = new Intent(this, AboutDetailsActivity.class);
        startActivity(aboutIntent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        isPaused = true;
        mHandler.removeMessages(SWITCH_CURRENT_PICTURE);
        stopTimerTask();
        // recycleMem();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        loadImageMem();
        startTimerTask();
        isPaused = false;
        updateLayout();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        // 回收内存
        recycleMem();
    }

    private void recycleMem() {
        if (mDrawables != null) {
            for (int i = 0; i < mDrawables.length; i++) {
                BitmapDrawable drawableForRecyle = mDrawables[i];
                Bitmap bitmapForRecycle = drawableForRecyle.getBitmap();
                if (bitmapForRecycle != null) {
                    bitmapForRecycle.recycle();
                    bitmapForRecycle = null;
                }
                drawableForRecyle.setCallback(null);
                drawableForRecyle = null;
            }
            mDrawables = null;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 2000) {
                // 如果两次按键时间间隔大于2秒，则不退出
                Toast.makeText(this, "Press once more time to exit app!", Toast.LENGTH_SHORT).show();
                firstTime = secondTime;// 更新firstTime
                return true;
            } else {
                // 两次按键小于2秒时，退出应用
                Stamp20Application.getInstance().exit();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
