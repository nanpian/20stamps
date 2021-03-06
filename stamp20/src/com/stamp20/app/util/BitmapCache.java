package com.stamp20.app.util;

import android.graphics.Bitmap;

public class BitmapCache {

    private static Bitmap date;

    private BitmapCache() {
        date = null;
    }

    private static BitmapCache cache = new BitmapCache();

    public static BitmapCache getCache() {
        return cache;
    }

    public void put(Bitmap src) {
        if (src != null && (date == null || !date.sameAs(src))) {
            if (date != null) {
                date.recycle();
                date = null;
            }
            date = Bitmap.createBitmap(src);
        }
    }

    public Bitmap get() {
        return date;
    }
}
