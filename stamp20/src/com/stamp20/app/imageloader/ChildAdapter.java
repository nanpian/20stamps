package com.stamp20.app.imageloader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;
import com.stamp20.app.R;
import com.stamp20.app.facebook.FbPhotoResult;
import com.stamp20.app.imageloader.MyImageView.OnMeasureListener;
import com.stamp20.app.imageloader.ImageLoader.NativeImageCallBack;
import com.stamp20.app.util.FontManager;
import com.stamp20.app.view.ImageUtil;

public class ChildAdapter extends BaseAdapter {
    private Point mPoint = new Point(0, 0);// 用来封装ImageView的宽和高的对象
	private int mWidth = 0;
	private int mHeight = 0;
    /**
     * 用来存储图片的选中情况
     */
    private HashMap<Integer, Boolean> mSelectMap = new HashMap<Integer, Boolean>();
    private GridView mGridView;
    private List<Uri> list;
    private FbPhotoResult mFbPhotos;
    protected LayoutInflater mInflater;
    private Context mContext;

    public ChildAdapter(Context context, List<Uri> list, GridView mGridView) {
        this.list = list;
        this.mFbPhotos = null;
        
        this.mGridView = mGridView;
        mInflater = LayoutInflater.from(context);
        mContext = context;
    }
    
    public ChildAdapter(Context context, FbPhotoResult fbPhotos, GridView mGridView){
    	this.list = null;
    	this.mFbPhotos = fbPhotos;
    	
    	this.mGridView = mGridView;
        mInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public int getCount() {
    	if(list != null && mFbPhotos == null)
    		return list.size();
    	else
    		return mFbPhotos.size();
    }

    @Override
    public Object getItem(int position) {
    	if(list != null && mFbPhotos == null)
    		return list.get(position);
    	else
    		return mFbPhotos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.grid_child_item, null);
//            FontManager.changeFonts(mContext, (FrameLayout)convertView.findViewById(R.id.framelayout));
            viewHolder = new ViewHolder();
            viewHolder.mImageView = (MyImageView) convertView.findViewById(R.id.child_image);
            // 用来监听ImageView的宽和高
            viewHolder.mImageView.setOnMeasureListener(new OnMeasureListener() {
                @Override
                public void onMeasureSize(int width, int height) {
                   mPoint.set(width, height);
                }
            });

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        
        Bitmap bitmap = null;
        		
        if(list != null && mFbPhotos == null){
        	Uri uri = list.get(position);
        	Picasso.with(mContext).load(uri)
        	   	.resize(200, 200).centerCrop()
    			.placeholder(R.drawable.friends_sends_pictures_no)
    			.into(viewHolder.mImageView);
        	/*
        	String path = ImageUtil.getLocalPathFromUri(mContext.getContentResolver(), uri);
        	viewHolder.mImageView.setTag(path);
        	// 利用NativeImageLoader类加载本地图片
        	bitmap = ImageLoader.getInstance().loadNativeImage(path, mPoint, new NativeImageCallBack() {

        		@Override
        		public void onImageLoader(Bitmap bitmap, String path) {
        			ImageView mImageView = (ImageView) mGridView.findViewWithTag(path);
        			if (bitmap != null && mImageView != null) {
        				mImageView.setImageBitmap(bitmap);
        			}
        			}
        	});
        	*/
        } else if (list == null && mFbPhotos != null){
        	// Facebook
        	String url = mFbPhotos.get(position).getSourceImageUrl();
        	Log.i("wangpeng14","childadapter net url: " + url);
        	Picasso.with(mContext).load(url)
        		.resize(200, 200).centerCrop()
				.placeholder(R.drawable.friends_sends_pictures_no)
				.into(viewHolder.mImageView);
        	/*
        	// 给ImageView设置路径Tag,这是异步加载图片的小技巧
            viewHolder.mImageView.setTag(url);

            // 利用ImageLoader类加载网络图片
            bitmap = ImageLoader.getInstance().loadNetImage(url, mPoint, new NativeImageCallBack() {

                @Override
                public void onImageLoader(Bitmap bitmap, String path) {
                    ImageView mImageView = (ImageView) mGridView.findViewWithTag(path);
                    if (bitmap != null && mImageView != null) {
                        mImageView.setImageBitmap(bitmap);
                    }
                }
            });
            */
        }
 /*       
        if (bitmap != null) {
            viewHolder.mImageView.setImageBitmap(bitmap);
        } else {
            viewHolder.mImageView.setImageResource(R.drawable.friends_sends_pictures_no);
        }
*/
        return convertView;
    }

    /**
     * 给CheckBox加点击动画，利用开源库nineoldandroids设置动画
     * 
     * @param view
     */
    private void addAnimation(View view) {
        float[] vaules = new float[] { 0.5f, 0.6f, 0.7f, 0.8f, 0.9f, 1.0f, 1.1f, 1.2f, 1.3f, 1.25f, 1.2f, 1.15f, 1.1f, 1.0f };
        AnimatorSet set = new AnimatorSet();
        set.playTogether(ObjectAnimator.ofFloat(view, "scaleX", vaules), ObjectAnimator.ofFloat(view, "scaleY", vaules));
        set.setDuration(150);
        set.start();
    }

    /**
     * 获取选中的Item的position
     * 
     * @return
     */
    public List<Integer> getSelectItems() {
        List<Integer> list = new ArrayList<Integer>();
        for (Iterator<Map.Entry<Integer, Boolean>> it = mSelectMap.entrySet().iterator(); it.hasNext();) {
            Map.Entry<Integer, Boolean> entry = it.next();
            if (entry.getValue()) {
                list.add(entry.getKey());
            }
        }

        return list;
    }

    public static class ViewHolder {
        public MyImageView mImageView;
    }

}
