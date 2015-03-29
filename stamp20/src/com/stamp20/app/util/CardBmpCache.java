package com.stamp20.app.util;

import com.stamp20.app.R;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class CardBmpCache {
    private  Bitmap cardBmpFront;
    private  Bitmap cardBmpBack;
    private Bitmap cardBmpEnve;
	public static CardBmpCache cache;

    private CardBmpCache() {
    }

    public static CardBmpCache getCacheInstance() {
    	if (cache==null) {
    		cache = new CardBmpCache();
    	} 
        return cache;
    }

    public void putFront(Bitmap src) {
        if (src != null && (cardBmpFront == null || !cardBmpFront.sameAs(src))) {
        	cardBmpFront = Bitmap.createBitmap(src);
        }
    }
    
    public void putBack(Bitmap src) {
        if (src != null && (cardBmpBack == null || !cardBmpBack.sameAs(src))) {
        	cardBmpBack = Bitmap.createBitmap(src);
        }
    }
    
    public void putEnve(Bitmap src) {
        if (src != null && (cardBmpEnve == null || !cardBmpEnve.sameAs(src))) {
        	cardBmpEnve = Bitmap.createBitmap(src);
        }
    }
    
    public Bitmap getEnve() {
    	return cardBmpEnve;    	
    }

    public Bitmap getFront() {
        return cardBmpFront;
    }
    
    public Bitmap getBack() {
    	return cardBmpBack;
    }
}
