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
    
    public static boolean debugCardsActivity(){
        /*是否调试debugCardsActivity*/
        //adb shell setprop log.tag.stamp20_cards_start V : make this return true
        //adb shell setprop log.tag.stamp20_cards_start S : make this return false
        final String STAMP20_CARDS_START = "stamp20_cards_start";
        return Constant.isPropertyEnabled(STAMP20_CARDS_START);
    }
    
    public static boolean debugCardsTemplateChooseActivity(){
        /*是否调试debugCardsActivity*/
        //adb shell setprop log.tag.stamp20_template_start V : make this return true
        //adb shell setprop log.tag.stamp20_template_start S : make this return false
        final String STAMP20_CARDS_TEMPLATE_CHOOSE_START = "stamp20_template_start";
        return Constant.isPropertyEnabled(STAMP20_CARDS_TEMPLATE_CHOOSE_START);
    }
    
    public static boolean debugMainActivity(){
        /*是否调试debugCardsActivity*/
        //adb shell setprop log.tag.stamp20_main_start V : make this return true
        //adb shell setprop log.tag.stamp20_main_start S : make this return false
        final String STAMP20_MAIN_START = "stamp20_main_start";
        return Constant.isPropertyEnabled(STAMP20_MAIN_START);
    }
    
    public static boolean debugXixiaLog(){
        /*是否调试Xixia的Log*/
        //adb shell setprop log.tag.stamp20_xixia_log V : make this return true
        //adb shell setprop log.tag.stamp20_xixia_log S : make this return false
        final String STAMP20_XIXIA_LOG = "stamp20_xixia_log";
        return Constant.isPropertyEnabled(STAMP20_XIXIA_LOG);
    }
}
