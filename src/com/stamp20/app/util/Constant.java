package com.stamp20.app.util;

import android.util.Log;

public class Constant {

    /* 用来监测是否是初次启动的 SharedPreferences_Guide */
    public static final String SHAREDPREFERENCES_GUIDE = "SharedPreferences_Guide";
    public static final String SHAREDPREFERENCES_GUIDE_FIRSTSTART = "SharedPreferences_Guide_FirstStart";
    
    //adb shell setprop log.tag.propertyName V : open this property
    //adb shell setprop log.tag.propertyName S : close this property
    public static boolean isPropertyEnabled(String propertyName) {
        return Log.isLoggable(propertyName, Log.VERBOSE);
    }
    
    public static boolean debugGuideActivity(){
        /*是否调试GuideActivity*/
        //adb shell setprop log.tag.stamp20_first_start V : make this return true
        //adb shell setprop log.tag.stamp20_first_start S : make this return false
        final String STAMP20_FIRST_START = "stamp20_first_start";
        return Constant.isPropertyEnabled(STAMP20_FIRST_START);
    }
}
