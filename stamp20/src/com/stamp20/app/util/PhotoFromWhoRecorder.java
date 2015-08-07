package com.stamp20.app.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.stamp20.app.Setting;

/** 这个类的作用主要是记录选择图片是从邮票定制这边还是贺卡定制那边 */
public class PhotoFromWhoRecorder {
    private static final String PREFERENCES_NAME = Setting.KEY_FROM_WHO;
    private static final String KEY_TOKEN = "select_from_which";

    public static void recordFromWhich(Context context, String token) {
        if (null == context || null == token) {
            return;
        }

        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME,
                Context.MODE_APPEND);
        Editor editor = pref.edit();
        editor.putString(KEY_TOKEN, token);
        editor.commit();
    }

    public static String readFromWhich(Context context) {
        if (null == context) {
            return null;
        }

        String token = new String();
        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME,
                Context.MODE_APPEND);
        token = pref.getString(KEY_TOKEN, "");
        return token;
    }

    public static void clear(Context context) {
        if (null == context) {
            return;
        }

        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME,
                Context.MODE_APPEND);
        Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }
}
