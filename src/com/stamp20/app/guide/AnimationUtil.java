package com.stamp20.app.guide;

import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;

public class AnimationUtil {

    private static final int GUIDE_PAGED_VIEW_1 = 1200;
    //初始化  
    public static Animation getTranslateAnimation(float fromXDelta, float toXDelta, 
                                                    float fromYDelta, float toYDelta,
                                                    AnimationListener listener){
        TranslateAnimation animation = new TranslateAnimation(fromXDelta, toXDelta, fromYDelta, toYDelta); 
        animation.setDuration(GUIDE_PAGED_VIEW_1);
        animation.setFillAfter(true);
        if(listener != null){
            animation.setAnimationListener(listener);
        }
        return animation;
    }  
}
