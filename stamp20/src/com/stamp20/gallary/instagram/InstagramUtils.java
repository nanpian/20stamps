/**
 * 
 */
/**
 * @author Peng
 *
 */
package com.stamp20.gallary.instagram;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.jinstagram.Instagram;
import org.jinstagram.auth.oauth.InstagramService;

import android.content.Context;

import com.stamp20.app.util.StringUtils;

public final class InstagramUtils {

    private static final String CONFIG_PROPERTIES = "/config.properties";

    private static Instagram instagram = null;

    private static InstagramService service = null;

    public static Properties getConfigProperties() {
        InputStream input = null;
        final Properties prop = new Properties();
        try {
            input = InstagramUtils.class.getResourceAsStream(CONFIG_PROPERTIES);
            prop.load(input);

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return prop;

    }
    public static InstagramService getInsService() {
        return service;
    }

    public static Instagram getInstagram() {
        return instagram;
    }

    public static boolean isUserInstagramLinked(Context mContext) {
        String token = InstagramTokenKeeper.readAccessToken(mContext);
        if (token != null && !StringUtils.isEmptyString(token)) {
            return true;
        }
        return false;
    }

    public static void setInsService(InstagramService s) {
        service = s;
    }

    public static void setInstagram(Instagram i) {
        instagram = i;
    }
}
