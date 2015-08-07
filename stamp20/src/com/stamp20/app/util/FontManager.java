/**
 * 
 */
package com.stamp20.app.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.stamp20.app.R;

/**
 * @author 作者 E-mail:
 * @version 创建时间：2014-12-25 下午4:41:22 类说明
 */

public class FontManager {

    private static Typeface mTypefaceBold = null;
    private static Typeface mTypefaceNormal = null;

    public static void changeFonts(Context mContext, View view) {

        mTypefaceNormal = Typeface.createFromAsset(mContext.getAssets(), "fonts/OpenSans.ttf");

        if (view instanceof TextView) {
            ((TextView) view).setTypeface(mTypefaceNormal);
        }
    }

    public static void changeFonts(Context mContext, ViewGroup root) {

        mTypefaceNormal = Typeface.createFromAsset(mContext.getAssets(), "fonts/OpenSans.ttf");

        for (int i = 0; i < root.getChildCount(); i++) {
            View v = root.getChildAt(i);
            if (v instanceof TextView) {
                ((TextView) v).setTypeface(mTypefaceNormal);
            }
        }
    }

    public static void changeFonts(ViewGroup root, Activity activity) {

        mTypefaceBold = Typeface.createFromAsset(activity.getAssets(), "fonts/OpenSans-Semibold.ttf");
        mTypefaceNormal = Typeface.createFromAsset(activity.getAssets(), "fonts/OpenSans.ttf");

        for (int i = 0; i < root.getChildCount(); i++) {
            View v = root.getChildAt(i);
            if (v instanceof TextView) {
                if (v.getId() == R.id.header_title || v.getId() == R.id.tail_text
                        || (v.getTag() != null && v.getTag().equals("forBoldText"))) {
                    ((TextView) v).setTypeface(mTypefaceBold);
                } else {
                    ((TextView) v).setTypeface(mTypefaceNormal);
                }
            } else if (v instanceof Button) {
                if (v.getTag() != null && v.getTag().equals("forBoldText")) {
                    ((Button) v).setTypeface(mTypefaceBold);
                } else {
                    ((Button) v).setTypeface(mTypefaceNormal);
                }
            } else if (v instanceof EditText) {
                if (v.getTag() != null && v.getTag().equals("forBoldText")) {
                    ((EditText) v).setTypeface(mTypefaceBold);
                } else {
                    ((EditText) v).setTypeface(mTypefaceNormal);
                }
            } else if (v instanceof ViewGroup) {
                changeFonts((ViewGroup) v, activity);
            }
        }
    }

    public static void changeFontsBlod(Context mContext, ViewGroup root) {

        mTypefaceNormal = Typeface.createFromAsset(mContext.getAssets(), "fonts/OpenSans-Semibold.ttf");

        for (int i = 0; i < root.getChildCount(); i++) {
            View v = root.getChildAt(i);
            if (v instanceof TextView) {
                ((TextView) v).setTypeface(mTypefaceNormal);
            }
        }
    }
}
