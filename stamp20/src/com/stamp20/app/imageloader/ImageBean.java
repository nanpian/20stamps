package com.stamp20.app.imageloader;

import com.stamp20.app.facebook.FbAlbum;
import com.stamp20.app.view.ImageUtil;

import android.content.Context;
import android.net.Uri;

/**
 * GridView的每个item的数据对象
 * 
 * @author len
 *
 */
public class ImageBean {
    /**
     * 文件夹的第一张图片路径
     */
    private String topImagePath;
    
    /**
     * facebook album
     */
    private FbAlbum album;
    
    /**
     * 文件夹名
     */
    private String folderName;
    /**
     * 文件夹中的图片数
     */
    private int imageCounts;
    private Context mContext;

    public ImageBean(Context context) {
        this.mContext = context;
    }
    
    public FbAlbum getFbAlbum(){
    	return this.album;
    }
    
    public void setFbAlbum(FbAlbum album){
    	this.album = album;
    	this.topImagePath = null;
    }

    public String getTopImagePath() {
        return topImagePath;
    }

    public void setTopImagePath(String topImagePath) {
        this.topImagePath = topImagePath;
        this.album = null;
    }

    public void setTopImagePath(Uri uri) {
        this.topImagePath = ImageUtil.getLocalPathFromUri(mContext.getContentResolver(), uri);
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public int getImageCounts() {
        return imageCounts;
    }

    public void setImageCounts(int imageCounts) {
        this.imageCounts = imageCounts;
    }

}
