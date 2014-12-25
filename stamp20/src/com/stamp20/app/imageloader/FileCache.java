package com.stamp20.app.imageloader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import android.content.Context;
import android.util.Log;

public class FileCache {
    
    private File cacheDir;
    private long size;
    private final long MAX_SIZE=(long)10485760;//10MB
    private LinkedList<CacheFileInfo> fileList;
    
    public FileCache(Context context){
    	Log.w("case", "FileCache created");
        cacheDir=new File(context.getCacheDir().getAbsolutePath()+File.separator+"imagecache");
        if(!cacheDir.exists()){
            cacheDir.mkdirs();
            size = 0;
            fileList = new LinkedList<CacheFileInfo>();
        }else{
        	fileList = new LinkedList<CacheFileInfo>();
        	initiateFileList();
        }
    }
    
    public File getFileIfAvailable(String url){
        //I identify images by hashcode. Not a perfect solution, good for the demo.
        File f = new File(cacheDir, String.valueOf(url.hashCode()));
        if (f!=null && f.exists()){
        	return f;
        }else{
        	return null;
        }
    }
    
    public void saveFileFromInputStream(String url, InputStream in){
        File f = new File(cacheDir, String.valueOf(url.hashCode()));
        FileOutputStream out = null;
        try{
        	out = new FileOutputStream(f);
        	byte[] buffer = new byte[65536];//64kb
        	int length = -1;
        	while((length=in.read(buffer))!=-1){
        		out.write(buffer, 0, length);
        	}
        }catch(IOException e){
        	Log.e("cases", "FileCache:saveFileFromInputStream", e);
        }finally{
        	if (out!=null){
        		try{
        			out.close();
        		}catch(IOException e){}
        	}
        	out = null;
        }
    	if (f!=null && f.exists()){
    		fileList.add(new CacheFileInfo(f.lastModified(),f.length(),f.getName()));
    		size=size+f.length();
    	}
    	f = null;
    	checkSize();
    }
    
//    public File createFileToSave(String url){
//        return new File(cacheDir, String.valueOf(url.hashCode()));
//    }
//    
//    public void afterNewFileAdded(String url){
//        File f = new File(cacheDir, String.valueOf(url.hashCode()));
//    	if (f!=null && f.exists()){
//    		fileList.add(new CacheFileInfo(f.lastModified(),f.length(),f.getName()));
//    		size=size+f.length();
//    	}
//    	f = null;
//    	checkSize();
//    }
    
    private void initiateFileList(){
    	fileList.clear();
        File[] files=cacheDir.listFiles();
        for(File f:files){
        	CacheFileInfo cacheFileInfo = new CacheFileInfo(f.lastModified(),f.length(),f.getName());
            fileList.add(cacheFileInfo);  
            size = size + f.length();
        }
        files = null;
        Collections.sort(fileList, new Comparator<CacheFileInfo>(){
			@Override
			public int compare(CacheFileInfo lhs, CacheFileInfo rhs) {
				if (lhs.getTime() < rhs.getTime()){
					return -1;
				}else if (lhs.getTime() > rhs.getTime()){
					return 1;
				}else{
					return 0;
				}
			}
        });
    }
    
    private void checkSize(){
    	while (size>MAX_SIZE){
    		CacheFileInfo oldestCacheFileInfo = fileList.poll();
    		if (oldestCacheFileInfo == null){
    			Log.e("cases", "FileCache: checkSize() first element is null");
    			initiateFileList();
    			break;
    		}else{
    			File oldestCacheFile = getFileIfAvailable(oldestCacheFileInfo.getHashCode());
    			if (oldestCacheFile == null || !oldestCacheFile.exists()){
    				Log.e("cases", "FileCache: oldestCacheFile is null or not existing");
    				size = size - oldestCacheFileInfo._size;
    				oldestCacheFile = null;
    			}else{
    				oldestCacheFile.delete();
    				size = size - oldestCacheFileInfo._size;
    				oldestCacheFile = null;
    			}
    		}
    	}
    }
    

    private class CacheFileInfo{
    	private long _time;
    	private long _size;
    	private String _hashCode;
    	public CacheFileInfo(long time, long size, String hashCode){
    		_time = time;
    		_size = size;
    		_hashCode = hashCode;
    	}
    	public long getTime(){
    		return _time;
    	}
    	public long getSize(){
    		return _size;
    	}
    	public String getHashCode(){
    		return _hashCode;
    	}
    }
}