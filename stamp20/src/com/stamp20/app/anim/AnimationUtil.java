package com.stamp20.app.anim;

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
    
    public static Animation getTranslateAnimation(int fromXType, float fromXValue, 
            int toXType, float toXValue, 
            int fromYType, float fromYValue, 
            int toYType, float toYValue, 
            boolean fillAfter, long durationMillis, AnimationListener listener){
        TranslateAnimation animation = new TranslateAnimation(fromXType, fromXValue, toXType, toXValue, 
                fromYType, fromYValue, toYType, toYValue);
        animation.setDuration(durationMillis);
        animation.setFillAfter(fillAfter);
        if(listener != null){
            animation.setAnimationListener(listener);
        }
        return animation;
    }
}
