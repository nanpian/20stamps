package com.stamp20.app.activities;

import java.util.ArrayList;
import java.util.List;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.stamp20.app.BaseTitleActivity;
import com.stamp20.app.R;
import com.stamp20.app.R.anim;
import com.stamp20.app.data.Design;
import com.stamp20.app.util.Constant;
import com.stamp20.app.util.FontManager;
import com.stamp20.app.util.Log;
import com.stamp20.app.util.PhotoFromWhoRecorder;
import com.stamp20.app.view.ImageUtil;
import com.stamp20.app.view.RoundNumber;
import com.stamp20.gallary.GallaryActivity;

public class HomeActivity extends BaseTitleActivity implements View.OnClickListener {

    private ArrayList<Integer> mDrawableIDs = new ArrayList<Integer>();
    /*
     * private int[] mIds = new int[] { R.drawable.background_home_baby_shower,
     * R.drawable.background_home_birthday, R.drawable.background_home_kids,
     * R.drawable.background_home_pets,
     * R.drawable.background_home_save_the_date,
     * R.drawable.background_home_wedding };
     */
    private Drawable[] mDrawables;
    private ImageView mBackgroundImageView;
    private int mCurrentPicNum = 0;
    private ViewGroup mLinearLayoutHasLocalDesign;
    private ViewGroup mLinearLayoutNoLocalDesign;
    private Button btnPostage, btnPostageStamp;
    private Button btnCards, btnGettingCards;
    private Button btnViewCart;
    private ImageButton btnCoupons, btnAbout;
    private RoundNumber mLocalDesignNumber;

    private final static int ANIMATION_DURATION = 1000;
    private final static int CHANGE_PICTURE_DURATION = 2000;
    private final static float MAX_ALPHA = 1.0f;
    private final static float MIN_ALPHA_0 = 0.0f;
    private final static float MIN_ALPHA_1 = 0.1f;

    private final static int SWITCH_CURRENT_PICTURE = 1;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == SWITCH_CURRENT_PICTURE) {
                TransitionDrawable transitionDrawable = null;
                transitionDrawable = new TransitionDrawable(new Drawable[] { mDrawables[mCurrentPicNum % mDrawableIDs.size()],
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
        btnViewCart = (Button) findViewById(R.id.btn_view_cart);
        mLocalDesignNumber = (RoundNumber) findViewById(R.id.local_design_number);

        btnCoupons.setOnClickListener(this);
        btnAbout.setOnClickListener(this);
        btnPostage.setOnClickListener(this);
        btnPostageStamp.setOnClickListener(this);
        btnCards.setOnClickListener(this);
        btnGettingCards.setOnClickListener(this);
        btnViewCart.setOnClickListener(this);

        mBackgroundImageView = (ImageView) this.findViewById(R.id.background);
        mBackgroundImageView.setScaleType(ScaleType.FIT_XY);
        mBackgroundImageView.setImageDrawable(getResources().getDrawable(R.drawable.background_home_baby_shower));
        initBackgroundArrays();

        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), R.drawable.background_home_baby_shower, opts);
        opts.inSampleSize = ImageUtil.computeSampleSize(opts, -1, 1280 * 960);
        opts.inJustDecodeBounds = false;
        mDrawables = new Drawable[mDrawableIDs.size()];
        try {
            for (int i = 0; i < mDrawableIDs.size(); i++) {
                Bitmap bmp = BitmapFactory.decodeResource(getResources(), mDrawableIDs.get(i), opts);
                mDrawables[i] = new BitmapDrawable(bmp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(SWITCH_CURRENT_PICTURE);
            }

        }, CHANGE_PICTURE_DURATION - ANIMATION_DURATION, CHANGE_PICTURE_DURATION);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        getLocalDesign();
    }

    private void getLocalDesign() {
        ParseQuery<Design> query = ParseQuery.getQuery("Design");
        query.fromLocalDatastore();
        query.findInBackground(new FindCallback<Design>() {
            public void done(List<Design> designs, ParseException e) {
                if (e == null) {
                    Log.d(this, "getMyLocalDesign: success..." + designs.size());
                    getLocalDesignSuccess(designs);
                } else {
                    Log.d(this, "getMyLocalDesign: failed...");
                }
            }
        });
    }

    private void getLocalDesignSuccess(List<Design> designs) {
        if (designs != null && designs.size() >= 1) {
            mLocalDesignNumber.setText(String.valueOf(designs.size()));
            mLinearLayoutNoLocalDesign.setVisibility(View.GONE);
            mLinearLayoutHasLocalDesign.setVisibility(View.VISIBLE);

        } else {
            mLinearLayoutHasLocalDesign.setVisibility(View.GONE);
            mLinearLayoutNoLocalDesign.setVisibility(View.VISIBLE);
        }
    }

    private void initBackgroundArrays() {
        TypedArray typedArray = getResources().obtainTypedArray(R.array.home_background);
        if (null != typedArray) {
            if (Constant.debugXixiaLog()) {
                Log.i("xixia", "typedArray:" + typedArray.length());
            }
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
            break;
        case R.id.btn_about:
            showAboutInfo();
            break;
        case R.id.btn_cards:
        case R.id.btn_getting_cards:
            startActivity(new Intent(HomeActivity.this, CardsTemplateChooseActivity.class));
            PhotoFromWhoRecorder.recordFromWhich(getApplicationContext(), "card");
            break;

        case R.id.btn_postage:
        case R.id.btn_postage_stamp:
            startActivity(new Intent(HomeActivity.this, GallaryActivity.class));
            PhotoFromWhoRecorder.recordFromWhich(getApplicationContext(), "stamp");
            break;
        case R.id.btn_view_cart:
            startActivity(new Intent(HomeActivity.this, ShopCartItemsActivity.class));
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
//        Intent aboutIntent = new Intent(this, AboutDetailsActivity.class);
        startActivity(aboutIntent);
    }
}
