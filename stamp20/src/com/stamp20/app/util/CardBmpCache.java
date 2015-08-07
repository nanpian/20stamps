package com.stamp20.app.util;

import android.graphics.Bitmap;

public class CardBmpCache {
    public static CardBmpCache cache;
    public static CardBmpCache getCacheInstance() {
        if (cache == null) {
            cache = new CardBmpCache();
        }
        return cache;
    }
    private Bitmap cardBmpBack;
    private Bitmap cardBmpEnve;

    private Bitmap cardBmpFront;

    private CardBmpCache() {
    }

    public Bitmap getBack() {
        return cardBmpBack;
    }

    public Bitmap getEnve() {
        return cardBmpEnve;
    }

    public Bitmap getFront() {
        return cardBmpFront;
    }

    public void putBack(Bitmap src) {
        if (src != null && (cardBmpBack == null || !cardBmpBack.sameAs(src))) {
            if (cardBmpBack != null) {
                cardBmpBack.recycle();
                cardBmpBack = null;
            }
            cardBmpBack = Bitmap.createBitmap(src);
        }
    }

    public void putEnve(Bitmap src) {
        if (src != null && (cardBmpEnve == null || !cardBmpEnve.sameAs(src))) {
            if (cardBmpEnve != null) {
                cardBmpEnve.recycle();
                cardBmpEnve = null;
            }
            cardBmpEnve = Bitmap.createBitmap(src);
        }
    }

    public void putFront(Bitmap src) {
        if (src != null && (cardBmpFront == null || !cardBmpFront.sameAs(src))) {
            if (cardBmpFront != null) {
                cardBmpFront.recycle();
                cardBmpFront = null;
            }
            cardBmpFront = Bitmap.createBitmap(src);
        }
    }
}
