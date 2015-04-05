package com.stamp20.app.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.stamp20.app.R;
import com.stamp20.app.anim.Rotate3dAnimation;
import com.stamp20.app.util.FontManager;
import com.stamp20.app.util.Log;

public class CouponsActivity extends Activity implements OnClickListener {

    private ImageView headClose;
    private Button btnGetCouponCode;
    private Button btnShareOnFacebook;
    private Button btnShareOnTwitter;
    private TextView btnCancel;

    private ViewGroup getCouponsViewGroup;
    private ViewGroup shareCouponsViewGroup;

    private boolean isInGetCoupons = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupons);
        FontManager.changeFonts((LinearLayout) findViewById(R.id.root), this);
        initView();
    }

    private void initView() {
        headClose = (ImageView) findViewById(R.id.header_previous);
        btnGetCouponCode = (Button) findViewById(R.id.btn_get_coupons);
        btnShareOnFacebook = (Button) findViewById(R.id.btn_share_on_facebook);
        btnShareOnTwitter = (Button) findViewById(R.id.btn_share_on_twitter);
        btnCancel = (TextView) findViewById(R.id.cancel_share);
        getCouponsViewGroup = (LinearLayout) findViewById(R.id.get_coupons);
        shareCouponsViewGroup = (LinearLayout) findViewById(R.id.share_coupons);

        headClose.setOnClickListener(this);
        btnGetCouponCode.setOnClickListener(this);
        btnShareOnFacebook.setOnClickListener(this);
        btnShareOnTwitter.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.bottom_in, R.anim.bottom_out);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.header_previous:
            finish();
            break;
        case R.id.btn_get_coupons:
            getCoupons();
            break;
        case R.id.btn_share_on_facebook:
            shareOnFacebook();
            break;
        case R.id.btn_share_on_twitter:
            shareOnTwitter();
            break;
        case R.id.cancel_share:
            cancel_share();
            break;
        }
    }

    private void cancel_share() {
        applyRotation(shareCouponsViewGroup, 0, 90, false);
       // applyRotation(shareCouponsViewGroup, -90, 0, false);

    }

    private void shareOnTwitter() {

    }

    private void shareOnFacebook() {

    }

    private void getCoupons() {
        applyRotation(getCouponsViewGroup, 0, 90, true);
        //applyRotation(shareCouponsViewGroup, -90, 0, true);

    }

    private void applyRotation(final ViewGroup v, float start, float end, final boolean isFront) {
        final float centerX = getCouponsViewGroup.getVisibility() == View.VISIBLE ? getCouponsViewGroup.getWidth() / 2.0f : shareCouponsViewGroup
                .getWidth() / 2.0f;
        final float centerY = getCouponsViewGroup.getVisibility() == View.VISIBLE ? getCouponsViewGroup.getHeight() / 2.0f : shareCouponsViewGroup
                .getHeight() / 2.0f;
        Rotate3dAnimation rotation = new Rotate3dAnimation(start, end, centerX, centerY, 300.0f, true);
        rotation.setDuration(500);
        rotation.setInterpolator(new AccelerateInterpolator());
        rotation.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationEnd(Animation arg0) { // 动画结束
                v.post(new Runnable() {
                    @Override
                    public void run() {
                    	if (isFront) {
                    		getCouponsViewGroup.setVisibility(View.GONE);
                    		shareCouponsViewGroup.setVisibility(View.VISIBLE);
                    	} else {
                    		shareCouponsViewGroup.setVisibility(View.GONE);
                    		getCouponsViewGroup.setVisibility(View.VISIBLE);
                    	}
                        btnCancel.setVisibility(shareCouponsViewGroup.getVisibility());

                        Rotate3dAnimation rotatiomAnimation = new Rotate3dAnimation(-90, 0, centerX, centerY, 300.0f, false);
                        rotatiomAnimation.setDuration(500);
                        rotatiomAnimation.setInterpolator(new DecelerateInterpolator());
                        if (isFront) {
                        	shareCouponsViewGroup.startAnimation(rotatiomAnimation);
                        } else {
                        	getCouponsViewGroup.startAnimation(rotatiomAnimation);
                        }
                    }
                });

            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
                Log.d(this, "repeat...");
            }

            @Override
            public void onAnimationStart(Animation arg0) {
                Log.d(this, "start...");
            }
        });
        v.startAnimation(rotation);

    }
}