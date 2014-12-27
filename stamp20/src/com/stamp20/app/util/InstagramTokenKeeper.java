
package com.stamp20.app.util; 

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.stamp20.app.Setting;
/** 
 * @author 作者 E-mail: 
 * @version 创建时间：2014-12-28 上午12:30:02 
 * 类说明 
 */
public class InstagramTokenKeeper {

    private static final String PREFERENCES_NAME = 	Setting.KEY_INSTAGRAM_TOKEN;
    private static final String KEY_TOKEN           = "token";
    
    /**
     * 保存 Token 对象到 SharedPreferences。
     * 
     * @param context 应用程序上下文环境
     * @param token   Token 对象
     */
    public static void writeAccessToken(Context context, String token) {
        if (null == context || null == token) {
            return;
        }
        
        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
        Editor editor = pref.edit();
        editor.putString(KEY_TOKEN, token);
        editor.commit();
    }
    
    /**
     * 从 SharedPreferences 读取 Token 信息。
     * 
     * @param context 应用程序上下文环境
     * 
     * @return 返回 Token 对象
     */
    public static String readAccessToken(Context context) {
        if (null == context) {
            return null;
        }
        
        String token = new String();
        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
        token = pref.getString(KEY_TOKEN, "");
        return token;
    }
    
    /**
     * 清空 SharedPreferences 中 Token信息。
     * 
     * @param context 应用程序上下文环境
     */
    public static void clear(Context context) {
        if (null == context) {
            return;
        }
        
        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
        Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }
}
 