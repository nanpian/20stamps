package com.stamp20.app.imageloader;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.stamp20.app.R;
import com.stamp20.app.util.Log;

public class ImageLoaderActivity extends FragmentActivity implements OnClickListener, OnPageChangeListener {

    private ImageView headerPrevious = null;
    private TextView headerTitle = null;

    private ViewPager mViewPager;
    private ImageButton mFeaturedAlbumButton;
    private ImageButton mPhotoAlbumButton;;
    private ImageButton mFaceBookAlbumButton;
    private ImageButton mInstagramAlbumButton;
    private ImageView mImageSelectedIndicator;
    private List<Fragment> mDatas;
    private FragmentPagerAdapter mAdapter;

    private int mScreen1_4;
    private int mCurrentPageIndex = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        setContentView(R.layout.activity_load_image_main);
        headerPrevious = (ImageView) findViewById(R.id.header_previous);
        headerTitle = (TextView) findViewById(R.id.header_title);
        headerTitle.setText(R.string.select_a_picture);
        initSelectedImage();
        initView();
    }

    private void initSelectedImage() {
        mImageSelectedIndicator = (ImageView) findViewById(R.id.image_source_selected);
        Display display = getWindow().getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        Log.d(this, "mScreen" + outMetrics.widthPixels);

        mScreen1_4 = outMetrics.widthPixels / 4;
        Log.d(this, "mScreen1_4" + mScreen1_4);
        LayoutParams lp = (LayoutParams) mImageSelectedIndicator.getLayoutParams();
        lp.width = mScreen1_4;
        lp.leftMargin = mCurrentPageIndex * mScreen1_4;
        mImageSelectedIndicator.setLayoutParams(lp);
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

        mDatas = new ArrayList<Fragment>();

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
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPx) {
        Log.d(this, "currenindex:" + mCurrentPageIndex + ", position:" + position);
        LayoutParams lp = (LayoutParams) mImageSelectedIndicator.getLayoutParams();
        if (mCurrentPageIndex == 0 && position == 0) {// 0->1
            lp.leftMargin = (int) (mCurrentPageIndex * mScreen1_4 + positionOffset * mScreen1_4);
        } else if (mCurrentPageIndex == 1 && position == 0) {// 1->0
            lp.leftMargin = (int) (mCurrentPageIndex * mScreen1_4 + (positionOffset - 1) * mScreen1_4);
        } else if (mCurrentPageIndex == 1 && position == 1) {// 1->2
            lp.leftMargin = (int) (mCurrentPageIndex * mScreen1_4 + positionOffset * mScreen1_4);
        } else if (mCurrentPageIndex == 2 && position == 1) {// 2->1
            lp.leftMargin = (int) (mCurrentPageIndex * mScreen1_4 + (positionOffset - 1) * mScreen1_4);
        } else if (mCurrentPageIndex == 2 && position == 2) {// 2->3
            lp.leftMargin = (int) (mCurrentPageIndex * mScreen1_4 + positionOffset * mScreen1_4);
        } else if (mCurrentPageIndex == 3 && position == 2) {// 3->2
            lp.leftMargin = (int) (mCurrentPageIndex * mScreen1_4 + (positionOffset - 1) * mScreen1_4);
        }

        mImageSelectedIndicator.setLayoutParams(lp);
    }

    @Override
    public void onPageSelected(int position) {
        mCurrentPageIndex = position;
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
