package com.stamp20.gallary;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.stamp20.app.R;
import com.stamp20.gallary.FeaturedAlbumFragment;
import com.stamp20.gallary.PhotoAlbumFragment;
import com.stamp20.app.activities.HomeActivity;
import com.stamp20.app.util.FontManager;
import com.stamp20.app.util.Log;

public class GallaryActivity extends FragmentActivity implements
        OnClickListener, OnPageChangeListener {

    public static long OUT_OF_TIME = 10000;

    private ImageView headerPrevious = null;
    private TextView headerTitle = null;

    private ViewPager mViewPager;
    private ImageButton mFeaturedAlbumButton;
    private ImageButton mPhotoAlbumButton;;
    private ImageButton mFaceBookAlbumButton;
    private ImageButton mInstagramAlbumButton;
    private View mImageSelectedIndicator;
    private List<GallaryFragment> mDatas;
    private FragmentPagerAdapter mAdapter;

    private int mScreen1_4;
    private int margin_left;
    private int mCurrentPageIndex = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // getActionBar().hide();
        setContentView(R.layout.activity_load_image_main);
        FontManager.changeFonts((LinearLayout) findViewById(R.id.root), this);
        headerPrevious = (ImageView) findViewById(R.id.header_previous);
        headerPrevious.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (mViewPager != null && mDatas != null) {
                    if (!mDatas.get(mCurrentPageIndex).onBackClick()) {
                        startActivity(new Intent(GallaryActivity.this,
                                HomeActivity.class));
                        finish();
                    }
                } else {
                    startActivity(new Intent(GallaryActivity.this,
                            HomeActivity.class));
                    finish();
                }
            }

        });
        headerTitle = (TextView) findViewById(R.id.header_title);
        headerTitle.setText(R.string.select_a_picture);
        initSelectedImage();
        initView();

        // Check if there is a currently logged in user
        // and it's linked to a Facebook account.
        ParseUser currentUser = ParseUser.getCurrentUser();
        if ((currentUser != null) && ParseFacebookUtils.isLinked(currentUser)) {
            // Go to the user info activity
            // showUserDetailsActivity();
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) { // 按下的如果是BACK，同时没有重复
            startActivity(new Intent(GallaryActivity.this, HomeActivity.class));
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(this, "finish authentication" + requestCode);
        if (requestCode == InstagramAlbumFragment.REQUEST_CODE_SELECT_INSTAGRAM_AUTH) {
            mDatas.get(3).onActivityResult(requestCode, resultCode, data);
        } else
            ParseFacebookUtils.finishAuthentication(requestCode, resultCode,
                    data);
    }

    // 将indicator宽度设置为和icon_featured_album.png相等
    private void initSelectedImage() {

        Bitmap icon = BitmapFactory.decodeResource(getResources(),
                R.drawable.icon_featured_album);
        int icon_width = icon.getWidth();

        mImageSelectedIndicator = (View) findViewById(R.id.image_source_selected);
        Display display = getWindow().getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        Log.d(this, "mScreen" + outMetrics.widthPixels);

        mScreen1_4 = outMetrics.widthPixels / 4;
        Log.d(this, "mScreen1_4" + mScreen1_4);

        // 计算距离左边缘间隔
        margin_left = (outMetrics.widthPixels - icon_width * 4) / 8;

        // 动态设置indicator的宽高,宽度和icon宽度一致
        LayoutParams lp = (LayoutParams) mImageSelectedIndicator
                .getLayoutParams();
        lp.width = icon_width;
        lp.leftMargin = mCurrentPageIndex * mScreen1_4 + margin_left;
        mImageSelectedIndicator.setLayoutParams(lp);

        // recycle bitmap memory
        icon.recycle();
        icon = null;
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.id_viewpager);
        mFeaturedAlbumButton = (ImageButton) findViewById(R.id.button_featured_album);
        mPhotoAlbumButton = (ImageButton) findViewById(R.id.button_photo_album);
        mFaceBookAlbumButton = (ImageButton) findViewById(R.id.button_fb_album);
        mInstagramAlbumButton = (ImageButton) findViewById(R.id.button_instagram_album);

        mFeaturedAlbumButton.setOnClickListener(this);
        mPhotoAlbumButton.setOnClickListener(this);
        mFaceBookAlbumButton.setOnClickListener(this);
        mInstagramAlbumButton.setOnClickListener(this);

        FeaturedAlbumFragment mFeaturedAlbumFragment = new FeaturedAlbumFragment();
        PhotoAlbumFragment mPhotoAlbumFragment = new PhotoAlbumFragment();
        FaceBookAlbumFragment mFaceBookAlbumFragment = new FaceBookAlbumFragment();
        InstagramAlbumFragment mInstagramAlbumFragment = new InstagramAlbumFragment();

        mDatas = new ArrayList<GallaryFragment>();

        mDatas.add(mFeaturedAlbumFragment);
        mDatas.add(mPhotoAlbumFragment);
        mDatas.add(mFaceBookAlbumFragment);
        mDatas.add(mInstagramAlbumFragment);

        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return mDatas.size();
            }

            @Override
            public Fragment getItem(int arg0) {
                return mDatas.get(arg0);
            }
        };
        mViewPager.setAdapter(mAdapter);

        mViewPager.setOnPageChangeListener(this);
        // 设置默认打开页面为本地图库page
        mViewPager.setCurrentItem(1);
    }

    @Override
    public void onPageScrollStateChanged(int position) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageScrolled(int position, float positionOffset,
            int positionOffsetPx) {
        // Log.d(this, "currenindex:" + mCurrentPageIndex + ", position:" +
        // position);
        LayoutParams lp = (LayoutParams) mImageSelectedIndicator
                .getLayoutParams();
        if (mCurrentPageIndex == 0 && position == 0) {// 0->1
            lp.leftMargin = (int) (mCurrentPageIndex * mScreen1_4
                    + positionOffset + margin_left * mScreen1_4);
        } else if (mCurrentPageIndex == 1 && position == 0) {// 1->0
            lp.leftMargin = (int) (mCurrentPageIndex * mScreen1_4 + (positionOffset - 1)
                    * mScreen1_4)
                    + margin_left;
        } else if (mCurrentPageIndex == 1 && position == 1) {// 1->2
            lp.leftMargin = (int) (mCurrentPageIndex * mScreen1_4 + positionOffset
                    * mScreen1_4)
                    + margin_left;
        } else if (mCurrentPageIndex == 2 && position == 1) {// 2->1
            lp.leftMargin = (int) (mCurrentPageIndex * mScreen1_4 + (positionOffset - 1)
                    * mScreen1_4)
                    + margin_left;
        } else if (mCurrentPageIndex == 2 && position == 2) {// 2->3
            lp.leftMargin = (int) (mCurrentPageIndex * mScreen1_4 + positionOffset
                    * mScreen1_4)
                    + margin_left;
        } else if (mCurrentPageIndex == 3 && position == 2) {// 3->2
            lp.leftMargin = (int) (mCurrentPageIndex * mScreen1_4 + (positionOffset - 1)
                    * mScreen1_4)
                    + margin_left;
        }

        mImageSelectedIndicator.setLayoutParams(lp);
    }

    @Override
    public void onPageSelected(int position) {
        mCurrentPageIndex = position;
        setFragmentStatus(position);
    }

    private void setFragmentStatus(int position) {
        int c = mDatas.size();
        for (int i = 0; i < c; i++) {
            if (i == position)
                mDatas.get(i).onFront();
            else
                mDatas.get(i).onBackground();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.button_featured_album:
            mViewPager.setCurrentItem(0);
            break;
        case R.id.button_photo_album:
            mViewPager.setCurrentItem(1);
            break;
        case R.id.button_fb_album:
            mViewPager.setCurrentItem(2);
            break;
        case R.id.button_instagram_album:
            mViewPager.setCurrentItem(3);
            break;

        default:
            break;
        }
    }

}
