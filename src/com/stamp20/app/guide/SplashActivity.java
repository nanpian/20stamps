package com.stamp20.app.guide;

import com.stamp20.app.R;
import com.stamp20.app.activities.MainActivity;
import com.stamp20.app.util.Constant;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashActivity extends Activity {

    private final static String TAG = "SplashActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        final ImageView imageView = (ImageView) this
                .findViewById(R.id.fullscreen_content);
        final Animation animation = AnimationUtils.loadAnimation(this,
                R.anim.splash_activity_scale);
        animation.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationRepeat(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                /*下面这句用于控制动画结束以后，保持默认状态*/
                animation.setFillAfter(true);
                boolean firstStart = SplashActivity.this.getSharedPreferences(
                        Constant.SHAREDPREFERENCES_GUIDE, Context.MODE_PRIVATE)
                        .getBoolean(
                                Constant.SHAREDPREFERENCES_GUIDE_FIRSTSTART,
                                true);
                if (firstStart) {
                    startActivity(new Intent(SplashActivity.this,
                            WelcomeActivity.class));
                } else {
                    startActivity(new Intent(SplashActivity.this,
                            MainActivity.class));
                }
                finish();
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                imageView.startAnimation(animation);
            }
        }, 500);
    }
}