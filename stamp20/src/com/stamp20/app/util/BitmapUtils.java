package com.stamp20.app.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;

public class BitmapUtils {
	
	public static boolean saveUriInFile(Uri selectedImage,ContentResolver cr, String path){
		boolean result = false;
		InputStream inputStream = null;
		File f = new File(path);
		FileOutputStream outStream = null;
		
		try{
	        inputStream = cr.openInputStream(selectedImage);
	        outStream = new FileOutputStream(f);
	        byte[] buffer = new byte[65536];//64kb
	        int length = -1;
	        while((length=inputStream.read(buffer))!=-1){
	        	outStream.write(buffer, 0, length);
	        }
	        result = true;
		}catch(FileNotFoundException e){
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(inputStream!=null){
				try {
					inputStream.close();
				} catch (IOException e) {
				}
				inputStream = null;
			}
			if(outStream!=null){
				try {
					outStream.close();
				} catch (IOException e) {
				}
				outStream = null;
			}
		}
		return result;
	}
	
	public static Bitmap decodeUri(Uri selectedImage,ContentResolver cr,final int requiredSize){
//		Log.d("cases", "BitmapUtils decoce "+selectedImage.toString());
		Bitmap result = null;
		InputStream inputStream = null;
		try{
	        // Decode image size
	        BitmapFactory.Options options = new BitmapFactory.Options();
	        options.inJustDecodeBounds = true;
	        inputStream = cr.openInputStream(selectedImage);
	        BitmapFactory.decodeStream(inputStream, null, options);
	        try{
	        	inputStream.close();
	        	inputStream = null;
	        }catch(IOException e){
	        	
	        }
	        // The new size we want to scale to
	        final int REQUIRED_SIZE = requiredSize;
	
	        // Find the correct scale value. 
	        int scale = calculateInSampleSize(options, REQUIRED_SIZE,REQUIRED_SIZE);
	        // Decode with inSampleSize
	        options.inSampleSize = scale;
	        options.inJustDecodeBounds = false;
	        inputStream = cr.openInputStream(selectedImage);
	        result = BitmapFactory.decodeStream(inputStream, null, options);
	        try{
	        	inputStream.close();
	        	inputStream = null;
	        }catch(IOException e){
	        	
	        }
		}catch(FileNotFoundException e){
			Log.e("cases", "error decode uri", e);
			inputStream = null;
		}
		return result;
    }	
	
	/**
	 * Return the instrinic image size from an Uri
	 * @param selectedImage
	 * @param cr
	 * @return
	 */
	public static Rect getImageNativeSizeFromUri(Uri selectedImage,ContentResolver cr){
		Rect result = null;
		InputStream inputStream = null;
		try{
	        // Decode image size
	        BitmapFactory.Options options = new BitmapFactory.Options();
	        options.inJustDecodeBounds = true;
	        inputStream = cr.openInputStream(selectedImage);
	        BitmapFactory.decodeStream(inputStream, null, options);
	        try{
	        	inputStream.close();
	        	inputStream = null;
	        }catch(IOException e){
	        	
	        }
		    final int height = options.outHeight;
		    final int width = options.outWidth;
		    result = new Rect(0,0,width,height);
		}catch(FileNotFoundException e){
			Log.e("cases", "error getImageSizeFromUri uri "+selectedImage.toString(), e);
			inputStream = null;
		}
		return result;
	}
	
	
	private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight){
	    // Raw height and width of image
	    final int height = options.outHeight;
	    final int width = options.outWidth;
	    int inSampleSize = 1;

	    if (height > reqHeight || width > reqWidth) {
	        if (width > height) {
	            inSampleSize = Math.round((float)height / (float)reqHeight);
	        } else {
	            inSampleSize = Math.round((float)width / (float)reqWidth);
	        }
	    }
	    return inSampleSize;
	}
	
	public static Bitmap drawableToBitmap (Drawable drawable) {
	    if (drawable instanceof BitmapDrawable) {
	        return ((BitmapDrawable)drawable).getBitmap();
	    }

	    Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Config.ARGB_8888);
	    Canvas canvas = new Canvas(bitmap); 
	    drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
	    drawable.draw(canvas);

	    return bitmap;
	}
	
	public static float[] getScalarArray(float[] inArray, float scalar){
		float[] result = new float[inArray.length];
		for(int i=0;i<inArray.length;i++){
			result[i]=scalar*inArray[i];
		}
		return result;
	}
	
	public static float getScreenDensity(Activity activity){
		Display display = activity.getWindowManager().getDefaultDisplay();
	    DisplayMetrics metrics = new DisplayMetrics();
	    display.getMetrics(metrics);
	    return metrics.density;
	}
}
