package com.stamp20.app.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.stamp20.app.R;
import com.stamp20.app.Stamp20Application;
import com.stamp20.app.util.Constant;
import com.stamp20.app.util.FontManager;
import com.stamp20.app.util.Log;

public class GuideActivity extends Activity implements OnClickListener, OnPageChangeListener {

    private final static String TAG = "WelcomeActivity";

    private ViewPager mViewPager;
    private WelcomeViewPagerAdapter mWelcomeViewPagerAdapter;
    private List<View> mViews;
    /* 欢迎页面最后一页的点击按钮，点击以后进入正常的Acitivty界面 */
    private Button mButton;

    // 引导图片资源
    private static final int[] mPics = { R.drawable.guide1, R.drawable.guide2, R.drawable.guide3, R.drawable.guide4 };

    private int mCurrentViewPagerState = 0;
    private int mCurrentPage = 0;
    private float mCurrentPageMove = 0;

    // 底部页面指示器图片
    private ImageView[] mDots;

    // 记录当前选中位置
    private int currentIndex;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Stamp20Application.getInstance().addActivity(this);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);// 去掉信息栏
        // this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_guide);
        FontManager.changeFonts((RelativeLayout) findViewById(R.id.root), this);
        mButton = (Button) findViewById(R.id.button);
        mViews = new ArrayList<View>();

        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        // 初始化引导图片列
        for (int i = 0; i < mPics.length; i++) {
            ImageView iv = new ImageView(this);
            iv.setLayoutParams(mParams);
            iv.setImageResource(mPics[i]);
            mViews.add(iv);
        }
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        // 初始化Adapter
        mWelcomeViewPagerAdapter = new WelcomeViewPagerAdapter(mViews);
        mViewPager.setAdapter(mWelcomeViewPagerAdapter);
        // 绑定回调
        mViewPager.setOnPageChangeListener(this);
        // 初始化底部的页面指示器小点点
        initDots();
        mButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent();
                intent.setClass(GuideActivity.this, HomeActivity.class);
                GuideActivity.this.startActivity(intent);
                com.stamp20.app.util.Log.i(TAG, "First start WelcomeActivity, set the flag of SharedPreferences_Guide_FirstStart to true");
                GuideActivity.this.getSharedPreferences(Constant.SHAREDPREFERENCES_GUIDE, Context.MODE_PRIVATE).edit()
                        .putBoolean(Constant.SHAREDPREFERENCES_GUIDE_FIRSTSTART, false).commit();
                finish();
            }
        });
        /*
         * runOnUiThread(new Runnable() {
         * 
         * @Override public void run() { if(mCurrentPage == 0 &&
         * mCurrentViewPagerState == 0){ ImageView iv = (ImageView)
         * mViews.get(0).findViewById(R.id.pic3);
         * iv.startAnimation(AnimationUtil
         * .getTranslateAnimation(0,300,0,0,null));
         * 
         * ImageView iv2 = (ImageView) mViews.get(0).findViewById(R.id.pic2);
         * iv2.startAnimation(AnimationUtil.getTranslateAnimation(0, 0, 0,
         * -250,null)); } } });
         */

    }

    private void initDots() {
        /*
         * LinearLayout ll = (LinearLayout) findViewById(R.id.ll);
         * 
         * mDots = new ImageView[mPics.length];
         * 
         * // 循环取得小点图片 for (int i = 0; i < mPics.length; i++) { mDots[i] =
         * (ImageView) ll.getChildAt(i); mDots[i].setEnabled(true);// 都设为不选中状态
         * mDots[i].setOnClickListener(this); mDots[i].setTag(i);//
         * 设置位置tag，方便取出与当前位置对应 }
         * 
         * currentIndex = 0; mDots[currentIndex].setEnabled(false);//
         * 设置为白色，表示当前页面
         */}

    /**
     * 设置当前的引导页
     */
    private void setCurView(int position) {
        if (position < 0 || position >= mPics.length) {
            return;
        }
        mViewPager.setCurrentItem(position);
    }

    /**
     * 这只当前引导小点的状态
     */
    private void setCurDot(int positon) {
        if (positon < 0 || positon > mPics.length - 1 || currentIndex == positon) {
            return;
        }
        mDots[positon].setEnabled(false);
        mDots[currentIndex].setEnabled(true);
        currentIndex = positon;
    }

    // 当滑动状态改变时调用
    /**
     * arg0: 1,表示正在滑动 2,表示滑动完毕 3,表示什么都没做
     */
    @Override
    public void onPageScrollStateChanged(int arg0) {
        mCurrentViewPagerState = arg0;
    }

    // 当当前页面被滑动时调用
    /**
     * arg0:标识当前页面 0,第一页 1,第二页 2,第三页 3,第四页 arg1:当前页面偏移的百分比 从0.0开始变化到1.0
     * arg2:当前页面偏移的像素位置
     */
    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        Log.i("xixia", "arg0:" + arg0 + ",arg1:" + arg1 + ",arg2:" + arg2);
        mCurrentPage = arg0;
        mCurrentPageMove = arg1;
    }

    // 当新的页面被选中时调用
    @Override
    public void onPageSelected(int arg0) {
        // 设置底部小点选中状态
        /* setCurDot(arg0); */
        if (arg0 == 3) {
            mButton.setVisibility(View.VISIBLE);

        } else {
            mButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        int position = (Integer) v.getTag();
        setCurView(position);
        /* setCurDot(position); */
    }

    class WelcomeViewPagerAdapter extends PagerAdapter {
        // 界面列表
        private List<View> views;

        public WelcomeViewPagerAdapter(List<View> views) {
            this.views = views;
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView(views.get(arg1));
        }

        @Override
        public void finishUpdate(View arg0) {
        }

        // 获得当前界面
        @Override
        public int getCount() {
            if (views != null) {
                return views.size();
            }
            return 0;
        }

        @Override
        public Object instantiateItem(View arg0, int arg1) {
            ((ViewPager) arg0).addView(views.get(arg1), 0);
            return views.get(arg1);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return (arg0 == arg1);
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
        }

        @Override
        public Parcelable saveState() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void startUpdate(View arg0) {
        }
    }
}