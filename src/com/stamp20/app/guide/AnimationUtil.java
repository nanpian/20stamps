package com.stamp20.app.guide;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;

public class AnimationUtil {
    
    public static Animation getAlphaAnimation(float fromAlpha, float toAlpha, boolean fillAfter,
            long durationMillis, AnimationListener listener){
        AlphaAnimation animation = new AlphaAnimation(fromAlpha, toAlpha);
        animation.setDuration(durationMillis);
        animation.setFillAfter(fillAfter);
        if(listener != null){
            animation.setAnimationListener(listener);
        }
        return animation;
    }
}
