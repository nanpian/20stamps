package com.stamp20.app.view;

import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.provider.MediaStore;

import com.stamp20.app.util.Log;

public class ImageUtil {

    private static final String TAG = "Stamp20";

    public ImageUtil() {
        
    }

    public static String getLocalPathFromUri(ContentResolver resolver, Uri uri) {
        Cursor cursor = resolver.query(uri, new String[] { MediaStore.Images.Media.DATA }, null, null, null);
        if (cursor == null) {
            return null;
        }
        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(index);
    }

    /**
     * Returns the bounds of the bitmap stored at a given Url.
     */
    public static Rect loadBitmapBounds(Context context, Uri uri) {
        BitmapFactory.Options o = new BitmapFactory.Options();
        loadBitmap(context, uri, o);
        return new Rect(0, 0, o.outWidth, o.outHeight);
    }

    /**
     * Loads a bitmap that has been downsampled using sampleSize from a given
     * url.
     */
    public static Bitmap loadDownsampledBitmap(Context context, Uri uri, int sampleSize) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        options.inSampleSize = sampleSize;
        return loadBitmap(context, uri, options);
    }

    /**
     * Returns the bitmap from the given uri loaded using the given options.
     * Returns null on failure.
     */
    public static Bitmap loadBitmap(Context context, Uri uri, BitmapFactory.Options o) {
        if (uri == null || context == null) {
            throw new IllegalArgumentException("bad argument to loadBitmap");
        }
        InputStream is = null;
        try {
            is = context.getContentResolver().openInputStream(uri);
            return BitmapFactory.decodeStream(is, null, o);
        } catch (FileNotFoundException e) {
            Log.d(TAG, "FileNotFoundException for " + uri, e);
        } finally {
            closeSilently(is);
        }
        return null;
    }

    /**
     * Loads a bitmap at a given URI that is downsampled so that both sides are
     * smaller than maxSideLength. The Bitmap's original dimensions are stored
     * in the rect originalBounds.
     *
     * @param uri
     *            URI of image to open.
     * @param context
     *            context whose ContentResolver to use.
     * @param maxSideLength
     *            max side length of returned bitmap.
     * @param originalBounds
     *            If not null, set to the actual bounds of the stored bitmap.
     * @param useMin
     *            use min or max side of the original image
     * @return downsampled bitmap or null if this operation failed.
     */
    public static Bitmap loadConstrainedBitmap(Uri uri, Context context, int maxSideLength, Rect originalBounds, boolean useMin) {
        if (maxSideLength <= 0 || uri == null || context == null) {
            throw new IllegalArgumentException("bad argument to getScaledBitmap");
        }
        // Get width and height of stored bitmap
        Rect storedBounds = loadBitmapBounds(context, uri);
        if (originalBounds != null) {
            originalBounds.set(storedBounds);
        }
        int w = storedBounds.width();
        int h = storedBounds.height();

        // If bitmap cannot be decoded, return null
        if (w <= 0 || h <= 0) {
            return null;
        }

        // Find best downsampling size
        int imageSide = 0;
        if (useMin) {
            imageSide = Math.min(w, h);
        } else {
            imageSide = Math.max(w, h);
        }
        int sampleSize = 1;
        while (imageSide > maxSideLength) {
            imageSide >>>= 1;
            sampleSize <<= 1;
        }

        // Make sure sample size is reasonable
        if (sampleSize <= 0 || 0 >= (Math.min(w, h) / sampleSize)) {
            return null;
        }
        return loadDownsampledBitmap(context, uri, sampleSize);
    }

    private static void closeSilently(Closeable c) {
        if (c == null)
            return;
        try {
            c.close();
        } catch (IOException t) {
            Log.e(TAG, "close fail ", t);
        }
    }
    
    //计算合适的图片大小  
    public static int computeSampleSize(BitmapFactory.Options options, int minSideLength,  
            int maxNumOfPixels) {  
        int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);  
  
        int roundedSize;  
        if (initialSize <= 8) {  
            roundedSize = 1;  
            while (roundedSize < initialSize) {  
                roundedSize <<= 1;  
            }  
        } else {  
            roundedSize = (initialSize + 7) / 8 * 8;  
        }  
  
        return roundedSize;  
    }  
  
    //计算合适的图片大小  
    private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength,  
            int maxNumOfPixels) {  
        double w = options.outWidth;  
        double h = options.outHeight;  
  
        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h  
                / maxNumOfPixels));  
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(  
                Math.floor(w / minSideLength), Math.floor(h / minSideLength));  
  
        if (upperBound < lowerBound) {  
            // return the larger one when there is no overlapping zone.  
            return lowerBound;  
        }  
  
        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {  
            return 1;  
        } else if (minSideLength == -1) {  
            return lowerBound;  
        } else {  
            return upperBound;  
        }  
    }  
}
