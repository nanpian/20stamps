package com.stamp20.app.imageloader;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.Stack;
import java.util.WeakHashMap;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.ImageView;

import com.stamp20.app.*;
import com.stamp20.app.Setting;
import com.stamp20.app.util.BitmapUtils;

public class ImageLoader2 {
	private final static boolean DEBUGLOG = true && Setting.ALL_LOG;
	
   // final int stub_id=R.drawable.loading_placeholder_gray;
    private final int IO_BUFFER_SIZE = 131072/16; //128kb
    private final int COPY_BUFFER_SIZE = 131072;//128kb
    private final String MEDIASTORE_PREFIX="com.dedeniu.imageloader.mediastore.prefix";
    private final String URI_PREFIX="com.dedeniu.imageloader.uri.prefix";
    private final String URI_SIZE_PREFIX="com.dedeniu.imageloader.uri.size.prefix";
    private static ImageLoader2 instance;
    private LruCache<String, Bitmap> memorySmallCache;
    private LruCache<String, Bitmap> memoryLargeCache;
    private FileCache fileCache;
    private PhotosLoader photoLoaderThread=new PhotosLoader();
    private PhotosQueue photosQueue=new PhotosQueue();
    private Bitmap notFoundBitmap;
    private ContentResolver mContentResolver;
    private Resources mResource;
    
    private Map<ImageView, String> imageViews=Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
    
    public static ImageLoader2 get(Context context){
    	if (instance == null){
    		instance = new ImageLoader2(context.getApplicationContext());
    	}
    	return instance;
    }
    
    private ImageLoader2(Context context){
    	mContentResolver = context.getContentResolver();
    	mResource = context.getResources();
        //Make the background thead low priority. This way it will not affect the UI performance
        photoLoaderThread.setPriority(Thread.NORM_PRIORITY-1);
        
        // Get memory class of this device, exceeding this amount will throw an
        // OutOfMemory exception.
        int memClass = ((ActivityManager) context.getSystemService(
                Context.ACTIVITY_SERVICE)).getMemoryClass();
        int memClassLarge = memClass;
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB){
          memClassLarge = ((ActivityManager) context.getSystemService(
              Context.ACTIVITY_SERVICE)).getLargeMemoryClass();
        }

        int memoryForThisApp= memClass;
        if (memClassLarge>memClass){
          memoryForThisApp = Math.round((memClass+Math.min(memClassLarge,memClass*2))/2);
        }
        
        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = Math.round(1024 * 1024 * memoryForThisApp / 10);
        //Crashlytics.log(Log.WARN,"case", "app mem="+memClass+";largeMem="+memClassLarge+" two cache total "+memoryForThisApp/5+"MB");
        memorySmallCache = new LruCache<String, Bitmap>(cacheSize){
        	@Override protected int sizeOf(String key, Bitmap value){
        		return value.getHeight()*value.getRowBytes();
        	}
        };
        memoryLargeCache = new LruCache<String, Bitmap>(cacheSize){
        	@Override protected int sizeOf(String key, Bitmap value){
        		return value.getHeight()*value.getRowBytes();
        	}
        };
        fileCache=new FileCache(context);
       // notFoundBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.error_loading);
         
    }
    
    /**
     * Async load image for an imageView. If loadingImageResource is not zero, show it during loading
     * @param url
     * @param activity
     * @param imageView
     * @param maxSize
     * @param loadingImageResource
     */
    public void displayImageFromUrlLater(String weburl, Activity activity, ImageView imageView, int maxSize, int loadingImageResource)
    {
        imageViews.put(imageView, weburl);
        Bitmap bitmap=getCachedBitmap(weburl);
        
        if(bitmap!=null){
            imageView.setImageBitmap(bitmap);
        }else{
        	if(loadingImageResource!=0){
        		imageView.setImageResource(loadingImageResource);
        	}
        	WeakReference<ImageView> imageViewRef = new WeakReference<ImageView>(imageView);
            queuePhoto(weburl, activity, imageViewRef, maxSize);
        }    
    }
    /**
     * Async load image for an imageView. Show default loading image during loading.
     * @param url
     * @param activity
     * @param imageView
     * @param maxSize
     */
    public void displayImageFromUrlLater(String weburl, Activity activity, ImageView imageView, int maxSize)
    {
    //	displayImageFromUrlLater(weburl,activity,imageView,maxSize,stub_id);
    }
    
    /**
     * Async load image for an imageView. Show the image from pendingUrl during loading if the image from pendingUrl is already available.Otherwise, show default loading image.
     * @param url
     * @param activity
     * @param imageView
     * @param maxSize
     * @param pendingUrl
     */
    public void displayImageFromUrlLater(String weburl, Activity activity, ImageView imageView, int maxSize, String pendingUrl)
    {
        imageViews.put(imageView, weburl);
        Bitmap bitmap=getCachedBitmap(weburl);
        
        if(bitmap!=null){
            imageView.setImageBitmap(bitmap);
        }else{
        	Bitmap pendingBitmap = getCachedBitmap(pendingUrl);
        	
        	if(pendingBitmap!=null){
        		imageView.setImageBitmap(pendingBitmap);
        	}else{
        		//imageView.setImageResource(stub_id);
        	}        	
        	WeakReference<ImageView> imageViewRef = new WeakReference<ImageView>(imageView);
            queuePhoto(weburl, activity, imageViewRef, maxSize);
        }    
    }
    
    public void displayImageFromUrlLaterWithTemplate(String weburl, Activity activity, ImageView imageView, int maxSize, int loadingImageResource,
     		int frameResId, int shadowResId, int frameMaskMaskResId,int red, int green, int blue, String deviceId, int width, int height,
     		float[][] imageTransformationPointPairArray)
    {
    	String targetUrl = deviceId+weburl+width+height;
    	imageViews.put(imageView, targetUrl);
    	Bitmap bitmap = getCachedBitmap(targetUrl);
    	if(bitmap!=null){
    		imageView.setImageBitmap(bitmap);
    	}else{
    		imageView.setImageResource(loadingImageResource);
        	WeakReference<ImageView> imageViewRef = new WeakReference<ImageView>(imageView);
            queuePhoto(targetUrl, activity, imageViewRef, maxSize,
            		frameResId, shadowResId, frameMaskMaskResId,red,green,blue,
        			width,height,weburl,imageTransformationPointPairArray);
    	}
    }
    
    
    
    /**
     * Async load image from MediaStore with provided id for an imageView. If loadingImageResource is not zero, show it during loading
     * @param id
     * @param activity
     * @param imageView
     * @param loadingImageResource
     */
    public void displayImageThumbFromMediaStoreLater(int id, Activity activity, ImageView imageView, int loadingImageResource){
    	String url = MEDIASTORE_PREFIX+id;
        imageViews.put(imageView, url);
        Bitmap bitmap=getCachedBitmap(url);
        if(bitmap!=null){
            imageView.setImageBitmap(bitmap);
        }else{
        	if(loadingImageResource!=0){
        		imageView.setImageResource(loadingImageResource);
        	}
        	WeakReference<ImageView> imageViewRef = new WeakReference<ImageView>(imageView);
            queuePhoto(url, activity, imageViewRef, 0);//0 is maxSize, but it is ignored in the mediaStore thumb case
        }    
    }
    
    /**
     * Async load image for an imageView. Show default loading image during loading.
     * @param id
     * @param activity
     * @param imageView
     */
    public void displayImageThumbFromMediaStoreLater(int id, Activity activity, ImageView imageView){
    	//displayImageThumbFromMediaStoreLater(id,activity,imageView,stub_id);
    }
    
    /**
     * Async load image for an imageView. If loadingImageResource is not zero, show it during loading
     * @param uriString
     * @param activity
     * @param imageView
     * @param maxSize
     * @param loadingImageResource
     */
    public void displayImageFromUriLater(String uriString, Activity activity , ImageView imageView, int maxSize, int loadingImageResource){
    	String url = URI_PREFIX+uriString+URI_SIZE_PREFIX+maxSize;
    	imageViews.put(imageView, url);
        Bitmap bitmap=getCachedBitmap(url);
        if(bitmap!=null){
            imageView.setImageBitmap(bitmap);
        }else{
        	if(loadingImageResource!=0){
        		imageView.setImageResource(loadingImageResource);
        	}
        	WeakReference<ImageView> imageViewRef = new WeakReference<ImageView>(imageView);
            queuePhoto(url, activity, imageViewRef, 0);
        }    
    }
    
    public void displayImageFromUriLater(String uriString, Activity activity, ImageView imageView, int maxSize){
    	//displayImageFromUriLater(uriString, activity, imageView, maxSize, stub_id);
    }
    
    public void displayImageFromUriLaterWithTemplate(String uriString, Activity activity , ImageView imageView, int maxSize, int loadingImageResource,
     		int frameResId,int shadowResId, int frameMaskMaskResId,int red, int green, int blue, String deviceId, int width, int height,
     		float[][] imageTransformationPointPairArray){
    	String uriUrl = URI_PREFIX+uriString+URI_SIZE_PREFIX+maxSize;
    	String targetUrl = deviceId+uriUrl+width+height;
    	imageViews.put(imageView, targetUrl);
    	Bitmap bitmap = getCachedBitmap(targetUrl);
    	if(bitmap!=null){
    		imageView.setImageBitmap(bitmap);
    	}else{
    		imageView.setImageResource(loadingImageResource);
        	WeakReference<ImageView> imageViewRef = new WeakReference<ImageView>(imageView);
            queuePhoto(targetUrl, activity, imageViewRef, 0,
            		frameResId, shadowResId, frameMaskMaskResId,red,green,blue,
        			width,height,uriUrl,imageTransformationPointPairArray);
    	}
    
    }
    
    public Bitmap getCachedBitmap(String url){
    	if (memorySmallCache.get(url)!=null){
    		return memorySmallCache.get(url);
    	}
    	return memoryLargeCache.get(url);
    }
    
    //TODO transform and save a square of targetSize to filepath
    public void saveOrigonalImage(String url, String filepath, Matrix matrix, int targetSize, int maxFileSizeInKb){
    	
    }
    
    
    private void queuePhoto(String url, Activity activity, WeakReference<ImageView> imageViewRef, int maxSize){
    	queuePhoto(url,activity,imageViewRef,maxSize,
    			Integer.MIN_VALUE, Integer.MIN_VALUE,Integer.MIN_VALUE,Integer.MIN_VALUE,Integer.MIN_VALUE,Integer.MIN_VALUE,
    			Integer.MIN_VALUE,Integer.MIN_VALUE,"",null);
    }
    private void queuePhoto(String url, Activity activity, WeakReference<ImageView> imageViewRef, int maxSize,
    		int frameResId, int shadowResId, int frameMaskMaskResId, int red, int green, int blue,
    		int width, int height, String sourceUrl,float[][] imageTransformationPointPairArray)
    {
        //This ImageView may be used for other images before. So there may be some old tasks in the queue. We need to discard them. 
        boolean shouldStart = false;
    	synchronized(photosQueue.photosToLoad){
    		if (photosQueue.isClean(url,imageViewRef)){
    	        PhotoToLoad p;
    	        if(frameResId!=Integer.MIN_VALUE){
    	        	p=new PhotoToLoad(url, imageViewRef, maxSize,
    	        			frameResId,shadowResId,frameMaskMaskResId,red,green,blue,
    	        			width,height,sourceUrl,imageTransformationPointPairArray);
    	        }else{
    	        	p=new PhotoToLoad(url, imageViewRef, maxSize);
    	        }
	            photosQueue.photosToLoad.push(p);
	            photosQueue.photosToLoad.notifyAll();
	            shouldStart = true;
    		}
    	}
        if(shouldStart && photoLoaderThread.getState()==Thread.State.NEW)
            photoLoaderThread.start();

//    	if (photosQueue.isClean(url,imageViewRef)){
//	        PhotoToLoad p=new PhotoToLoad(url, imageViewRef, maxSize);
//	        synchronized(photosQueue.photosToLoad){
//	            photosQueue.photosToLoad.push(p);
//	            photosQueue.photosToLoad.notifyAll();
//	        }
//	        
//	        //start thread if it's not started yet
//	        if(photoLoaderThread.getState()==Thread.State.NEW)
//	            photoLoaderThread.start();
//        }
    }
    
    private Bitmap getBitmap(String url, int maxSize) 
    {
    	if(DEBUGLOG) Log.i("cases", "ImageLoader getBitmap for "+url +" with maxSize="+maxSize);
    	//if mediastore, use system function
    	if(url.startsWith(MEDIASTORE_PREFIX)){
    		int imageId = -1;
    		try{
    			imageId = Integer.parseInt(url.substring(MEDIASTORE_PREFIX.length()));
    		}catch(Exception e){
    			Log.e("cases", "ImageLoader parse error in getBitmap for "+url,e);
    		}
	        Bitmap bitmap = null;
    		if(imageId>0){
    			bitmap = MediaStore.Images.Thumbnails.getThumbnail(mContentResolver,
    			        imageId, MediaStore.Images.Thumbnails.MICRO_KIND, null);
    		}
    		return bitmap;
    	}else if(url.startsWith(URI_PREFIX) && url.contains(URI_SIZE_PREFIX)){
    		int sizeValue = -1;
    		String uriString = url.substring(URI_PREFIX.length(), url.indexOf(URI_SIZE_PREFIX));
    		try{
    			sizeValue = Integer.parseInt(url.substring(url.indexOf(URI_SIZE_PREFIX)+URI_SIZE_PREFIX.length()));
    		}catch(Exception e){
    			Log.e("cases", "ImageLoader parse error in getBitmap for "+url,e);
    		}
    		Bitmap bitmap = null;
    		if(sizeValue>0){
    			if(uriString.startsWith("file")||uriString.startsWith("content")){
    	            bitmap = BitmapUtils.decodeUri(Uri.parse(uriString),mContentResolver,sizeValue);
        		}else{
    	            bitmap=BitmapUtils.decodeUri(Uri.fromFile(new File(uriString)),mContentResolver,sizeValue);
        		}
    		}
    		return bitmap;
    	}/*else if (url.equals(Idea.SPACEHOLDER_ID)){
    		return BitmapFactory.decodeResource(mResource,R.drawable.loading_placeholder_gray);
    	}*/
    	
        File f=fileCache.getFileIfAvailable(url);
        
        //from SD cache
        if (f!=null){
        	Bitmap b = decodeFile(f,maxSize);
        	if(b!=null){
        		return b;
        	}
        }
        
        //from web
        Bitmap bitmap = null;
        HttpURLConnection conn = null;
        BufferedInputStream in = null;
        try {
            final URL imageUrl = new URL(url);
            conn = (HttpURLConnection)imageUrl.openConnection();
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);
            in = new BufferedInputStream(conn.getInputStream(),IO_BUFFER_SIZE);
            fileCache.saveFileFromInputStream(url, in);
            bitmap = decodeFile(fileCache.getFileIfAvailable(url),maxSize);
        } catch (Exception ex){
           ex.printStackTrace();
           bitmap = null;
        } finally{
        	if (conn!=null ){
        		conn.disconnect();
        	}
        	try{
        		if (in != null){in.close();}
        	}catch(IOException e){}
        }
        return bitmap;
    }

    private Bitmap getBitmapWithFrame(int frameResId, int shadowResId, int frameMaskMaskResId,
    		int red, int green, int blue, int width, int height, String sourceUrl,int maxSize,
    		float[][] imageTransformationPointPairArray){
    	Bitmap sourceBitmap = getCachedBitmap(sourceUrl);
    	if(sourceBitmap==null){
    		sourceBitmap = getBitmap(sourceUrl,maxSize);
    	}
    	if(sourceBitmap == null){
    		sourceBitmap = notFoundBitmap;//set to error image
    		//Crashlytics.log(Log.ERROR, "case", "ImageLoader getBitmapWithFrame Error. sourceBitmap is null for "+sourceUrl);
    		//Crashlytics.logException(new Exception("sourceBitmap Null Exception",new Throwable("ImageLoader getBitmapWithFrame Error. sourceBitmap is null for "+sourceUrl)));
    	}
    	//prepare resultBitmap 
    	Bitmap resultBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
    	resultBitmap.eraseColor(Color.argb(255, red, green, blue));
    	Canvas resultCanvas = new Canvas(resultBitmap);
    	//draw it the same way as in touchImageView
    	Matrix matrix=new Matrix();
    	Matrix frameMatrix=new Matrix();
    	int frameIntrinsicWidth, frameIntrinsicHeight;
    	
    	Bitmap frameBitmap;
    	Bitmap templateMaskResultBitmap;
    	Bitmap shadowBitmap;
    	
    	//prepare frameBitmap  	
    	Drawable frameDrawable = mResource.getDrawable(frameResId);
    	frameBitmap = Bitmap.createBitmap(frameDrawable.getIntrinsicWidth(), frameDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
    	frameDrawable.setBounds(0, 0, frameDrawable.getIntrinsicWidth(), frameDrawable.getIntrinsicHeight());
    	frameDrawable.draw(new Canvas(frameBitmap));
    	
        frameIntrinsicWidth = frameDrawable.getIntrinsicWidth();
        frameIntrinsicHeight = frameDrawable.getIntrinsicHeight();
        frameDrawable = null;
        
        	
        if(frameMaskMaskResId!=0 && frameMaskMaskResId!=Integer.MIN_VALUE){
        	//create the templateMaskResultBitmap from the frameMaskMaskRes image and a color
        	Bitmap frameMaskBitmap = BitmapFactory.decodeResource(mResource, frameMaskMaskResId);
        	//make the destBitmap same size as frameBitmap
        	templateMaskResultBitmap = Bitmap.createBitmap(frameBitmap.getWidth(), frameBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        	templateMaskResultBitmap.eraseColor(Color.rgb(red, green, blue));
    		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1){
    			templateMaskResultBitmap.setHasAlpha(true);
    		}
    		Canvas canvas = new Canvas(templateMaskResultBitmap);
    		Paint paint = new Paint();
    		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
    		canvas.drawBitmap(frameMaskBitmap, 0,0, paint);
    		frameMaskBitmap.recycle();
    		frameMaskBitmap = null;
    		if (DEBUGLOG) Log.i("cases", "TouchImageView-setFrameDrawableResource templateMaskResultBitmap created");
        }else{
        	templateMaskResultBitmap = null;
        }
        
        if (shadowResId != 0 && frameMaskMaskResId != Integer.MIN_VALUE){
        	shadowBitmap = BitmapFactory.decodeResource(mResource, shadowResId);
        }else{
        	shadowBitmap = null;
        }
        
        //prepare frameMatrix from configureFrameBounds() TouchImageView
        int dwidth = frameIntrinsicWidth;
        int dheight = frameIntrinsicHeight;

        int vwidth = width;
        int vheight = height;
        float scale;
        float dx;
        float dy;
        
        if (dwidth <= vwidth && dheight <= vheight) {
            scale = Math.max((float) vwidth / (float) dwidth,
                    (float) vheight / (float) dheight);  // scale up so at least one side is full
        } else {
            scale = Math.min((float) vwidth / (float) dwidth,
                    (float) vheight / (float) dheight);  //scale down so at least one side is in
        }
        
        dx = (int) ((vwidth - dwidth * scale) * 0.5f + 0.5f);
        dy = (int) ((vheight - dheight * scale) * 0.5f + 0.5f);

        frameMatrix.setScale(scale, scale);
        frameMatrix.postTranslate(dx, dy);
        
        //prepare image matrix from resetScaleAndMatrix() TouchImageView
        BitmapDrawable sourceBitmapDrawable = new BitmapDrawable(mResource, sourceBitmap);
        int bmWidth = sourceBitmapDrawable.getIntrinsicWidth();
        int bmHeight = sourceBitmapDrawable.getIntrinsicHeight();
        float scaleX = (float) width / (float) bmWidth;
        float scaleY = (float) height / (float) bmHeight;
        scale = Math.max(scaleX, scaleY);//fill the view
        matrix.setScale(scale, scale);
        float redundantYSpace = (float) height - (scale * (float) bmHeight);
        float redundantXSpace = (float) width - (scale * (float) bmWidth);
        redundantYSpace /= (float) 2;
        redundantXSpace /= (float) 2;

        matrix.postTranslate(redundantXSpace, redundantYSpace);

    	//transform sourceBitmap to another angle if needed
    	if (imageTransformationPointPairArray!=null && imageTransformationPointPairArray.length==2){
    	  Matrix imageTransformMatrix = new Matrix();
		  boolean ptop = imageTransformMatrix.setPolyToPoly(imageTransformationPointPairArray[0],0,imageTransformationPointPairArray[1],0, 4);
		  matrix.postConcat(imageTransformMatrix);
    	}
        
        //drawToCache method from TouchImageView
    	if (sourceBitmap == null){
    		//Crashlytics.log(Log.ERROR, "case", "ImageLoader getBitmapWithFrame Error before framedCanvas.drawBitmap. sourceBitmap is null for "+sourceUrl);
    		//Crashlytics.logException(new Exception("sourceBitmap Null Exception",new Throwable("ImageLoader getBitmapWithFrame Error before framedCanvas.drawBitmap. sourceBitmap is null for "+sourceUrl)));
    	}else{
    		resultCanvas.drawBitmap(sourceBitmap, matrix, null);
    	}
        
        if (templateMaskResultBitmap!=null){
        	resultCanvas.drawBitmap(templateMaskResultBitmap,new Matrix(),null);
        }
        
        if (shadowBitmap != null){
        	Paint shadowPaint = new Paint();
        	shadowPaint.setXfermode(new PorterDuffXfermode(Mode.MULTIPLY));
        	shadowPaint.setShader(new BitmapShader(shadowBitmap,TileMode.CLAMP,TileMode.CLAMP));
        	resultCanvas.drawRect(0, 0, width, height, shadowPaint);
        }

        resultCanvas.drawBitmap(frameBitmap, frameMatrix, null);
        //end draw
    	return resultBitmap;
    }
    
    
    //decodes image and scales it to reduce memory consumption
    private Bitmap decodeFile(File f, int maxSize){
        try {
            //decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f),null,o);
            
            //Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE=maxSize;
            int width_tmp=o.outWidth, height_tmp=o.outHeight;
            int scale=1;
            while(true){
                if(width_tmp/2<REQUIRED_SIZE || height_tmp/2<REQUIRED_SIZE)
                    break;
                width_tmp/=2;
                height_tmp/=2;
                scale*=2;
            }
            
            //not scale
//            return BitmapFactory.decodeStream(new FileInputStream(f), null, null);

            //decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {}
        return null;
    }
    
    /** return the native size of an image from url input. This method should be called when knowing image is recently downloaded
     * @param url
     * @return
     * return null is the specified image is not in cache.
     */
    public Rect getImageNativeSizeFromFile(String url){
    	File f = fileCache.getFileIfAvailable(url);
    	Rect result = null;
    	if (f!=null){
            try {
                //decode image size
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(new FileInputStream(f),null,options);
                result = new Rect(0,0,options.outWidth,options.outHeight);
            } catch (FileNotFoundException e) {
            	Log.i("case", "ImageLoader getImageNativeSizeFromFile "+f.getAbsolutePath(), e);
            }
    	}
        return result;
    }
    
    //Task for the queue
    private class PhotoToLoad
    {
        public String url;
        public WeakReference<ImageView> imageViewRef;
        public int maxSize;
        public int frameResId=Integer.MIN_VALUE;
        public int shadowResId = Integer.MIN_VALUE;
        public int frameMaskMaskResId = Integer.MIN_VALUE;
        public float[][] imageTransformationPointPairArray = null;
        public int red = Integer.MIN_VALUE;
        public int green = Integer.MIN_VALUE;
        public int blue = Integer.MIN_VALUE;
        public int width = Integer.MIN_VALUE;
        public int height = Integer.MIN_VALUE;
        public String sourceUrl ="";
        
        public PhotoToLoad(String urlIn, WeakReference<ImageView> imageRefIn, int maxSizeIn){
            url=urlIn; 
            imageViewRef=imageRefIn;
            maxSize = maxSizeIn;
        }
        public PhotoToLoad(String urlIn, WeakReference<ImageView> imageRefIn, int maxSizeIn,
        		int frameResId, int shadowResId, int frameMaskMaskResId, int red, int green, int blue,
        		int width, int height, String sourceUrl, float[][]imageTransformationPointPairArray){
        	this(urlIn,imageRefIn,maxSizeIn);
        	this.frameResId = frameResId;
        	this.shadowResId = shadowResId;
        	this.frameMaskMaskResId = frameMaskMaskResId;
        	this.red = red;
        	this.green = green;
        	this.blue = blue;
        	this.width = width;
        	this.height = height;
        	this.sourceUrl = sourceUrl;
        	this.imageTransformationPointPairArray = imageTransformationPointPairArray;
        }        
    }
    
    
    public void stopThread()
    {
        photoLoaderThread.interrupt();
    }
    
    //stores list of photos to download
    class PhotosQueue
    {
        private Stack<PhotoToLoad> photosToLoad=new Stack<PhotoToLoad>();
        
        //removes all photoToLoad with empty imageViewReference
        //remove all photoToLoad with same imageVireReference but different url
        //return true if no same photoToLoad exist
        public boolean isClean(String url, WeakReference<ImageView> imageRef)
        {
        	boolean isExisting = false;
            for(int j=0 ;j<photosToLoad.size();){
                if(photosToLoad.get(j).imageViewRef==null ||photosToLoad.get(j).imageViewRef.get()==null){
                    photosToLoad.remove(j);
                }else if(photosToLoad.get(j).imageViewRef.get()==imageRef.get()){
                	if (!photosToLoad.get(j).url.equals(url)){
                		photosToLoad.remove(j);
                	}else{
                		isExisting = true; //find the same task,move to next one
                		++j;
                	}
                }else{
                    ++j;
                }
            }
            return !isExisting;
        }
    }
    
    class PhotosLoader extends Thread {
        public void run() {
            try {
                while(true)
                {
                    //thread waits until there are any images to load in the queue
                    if(photosQueue.photosToLoad.size()==0)
                        synchronized(photosQueue.photosToLoad){
                            photosQueue.photosToLoad.wait();
                        }
                    if(photosQueue.photosToLoad.size()!=0)
                    {
                        PhotoToLoad photoToLoad;
                        synchronized(photosQueue.photosToLoad){
                            photoToLoad=photosQueue.photosToLoad.pop();
                        }
                        Bitmap bmp=null;
                        if(photoToLoad.frameResId==Integer.MIN_VALUE){
                        	bmp=getBitmap(photoToLoad.url,photoToLoad.maxSize);
                        }else{
                        	bmp = getBitmapWithFrame(photoToLoad.frameResId, photoToLoad.shadowResId, photoToLoad.frameMaskMaskResId,
                        			photoToLoad.red,photoToLoad.green,photoToLoad.blue,
                        			photoToLoad.width,photoToLoad.height,photoToLoad.sourceUrl,photoToLoad.maxSize,
                        			photoToLoad.imageTransformationPointPairArray);
                        }
                        if (bmp==null){
                        	bmp = notFoundBitmap;
                        }
                        if (bmp.getRowBytes()*bmp.getHeight() >204800){//200 kb
                            memoryLargeCache.put(photoToLoad.url, bmp);
                        }else{
                            memorySmallCache.put(photoToLoad.url, bmp);
                        }
                        if (photoToLoad.imageViewRef.get()!=null){
                            String targetUrl=imageViews.get(photoToLoad.imageViewRef.get());
                            if(targetUrl!=null && targetUrl.equals(photoToLoad.url)){
                                BitmapDisplayer bd=new BitmapDisplayer(bmp, photoToLoad.imageViewRef);
                                Activity a=(Activity)photoToLoad.imageViewRef.get().getContext();
                                a.runOnUiThread(bd);
                            }
                        }
                    }
                    if(Thread.interrupted())
                        break;
                }
            } catch (InterruptedException e) {
                //allow thread to exit
            }
        }
    }
    
    
    //Used to display bitmap in the UI thread
    class BitmapDisplayer implements Runnable
    {
        Bitmap bitmap;
        WeakReference<ImageView> imageViewRef;
        public BitmapDisplayer(Bitmap b, WeakReference<ImageView> iRef){
        	bitmap=b;
        	imageViewRef=iRef;
        }
        public void run()
        {
            if(bitmap!=null && imageViewRef.get()!=null){
                imageViewRef.get().setImageBitmap(bitmap);
            }else if (imageViewRef.get()!=null){
               // imageViewRef.get().setImageResource(stub_id);
            }
        }
    }

    public void clearCache() {
        memorySmallCache.evictAll();
        memoryLargeCache.evictAll();
    }

    public File getFileFromCacheIfExisting(String url){
    	return fileCache.getFileIfAvailable(url);
    }
}