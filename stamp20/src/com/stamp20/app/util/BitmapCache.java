package com.stamp20.app.util;

import android.graphics.Bitmap;

public class BitmapCache {

    private static BitmapCache cache = new BitmapCache();

    public static BitmapCache getCache() {
        return cache;
    }

    private Bitmap date;

    private BitmapCache() {
        date = null;
    }

    public Bitmap get() {
        return date;
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
}
