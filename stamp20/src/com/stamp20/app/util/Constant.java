package com.stamp20.app.util;

import android.util.Log;

public class Constant {
	
	private static final boolean DEBUG = false;
    /* 用来监测是否是初次启动的 SharedPreferences_Guide */
    public static final String SHAREDPREFERENCES_GUIDE = "SharedPreferences_Guide";
    public static final String SHAREDPREFERENCES_GUIDE_FIRSTSTART = "SharedPreferences_Guide_FirstStart";
    // MainActivity向ChooseRateActivity传递的Extra，判断当前Stamp的方向
    public static final String STAMP_IS_HORIZONTAL = "stamp_is_horizontal";
    public static final String PAY_STYLE = "pay_with_paypal_or_checkout";
    
    public enum Pay_method{Standard, Priority, Oneday}
    
    //adb shell setprop log.tag.propertyName V : open this property
    //adb shell setprop log.tag.propertyName S : close this property
    public static boolean isPropertyEnabled(String propertyName) {
        return DEBUG && Log.isLoggable(propertyName, Log.VERBOSE);
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
    
    public static void LogXixia(String... str){
        if(debugXixiaLog() && str.length >= 1){
            String tag = "";
            String msg = "";
            if(str.length == 1){
                tag = "xixia";
                msg = str[0];
            } else {
                tag = "xixia-" + str[0];
                msg = str[1];
            }
            android.util.Log.i(tag, msg);
        }
    }
}
