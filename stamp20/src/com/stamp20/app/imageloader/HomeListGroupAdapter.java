package com.stamp20.app.imageloader;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.stamp20.app.R;
import com.stamp20.app.facebook.FbAlbum;
import com.stamp20.app.imageloader.ImageLoader.NativeImageCallBack;
import com.stamp20.app.imageloader.MyImageView.OnMeasureListener;
import com.stamp20.app.util.FontManager;

public class HomeListGroupAdapter extends BaseAdapter {
    private List<ImageBean> list;
    private Point mPoint = new Point(0, 0);// 用来封装ImageView的宽和高的对象
    private ListView mListView;
    protected LayoutInflater mInflater;
    private Context mContext;

    @Override
    public int getCount() {
       if(list == null){
    	   return 0;
       }else{
    	   return list.size();
       }
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public HomeListGroupAdapter(Context context, List<ImageBean> list, ListView mListView) {
        this.list = list;
        this.mListView = mListView;
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        ImageBean mImageBean = list.get(position);
        
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.home_list_group_item, null);
            FontManager.changeFonts(mContext, (RelativeLayout)convertView.findViewById(R.id.root));
            viewHolder.mImageView = (MyImageView) convertView.findViewById(R.id.list_group_image);
            viewHolder.mTextViewTitle = (TextView) convertView.findViewById(R.id.list_group_title);
            viewHolder.mTextViewCounts = (TextView) convertView.findViewById(R.id.list_group_count);

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
            viewHolder.mImageView.setImageResource(R.drawable.friends_sends_pictures_no);
        }

        viewHolder.mTextViewTitle.setText(mImageBean.getFolderName());
        viewHolder.mTextViewCounts.setText(" (" + Integer.toString(mImageBean.getImageCounts()) + ")");
        
        String path = mImageBean.getTopImagePath();
        FbAlbum album = mImageBean.getFbAlbum();
        
        Bitmap bitmap = null;
        
        if(path == null && album != null){
        	//Facebook 
        	String url = album.getCoverSourceUrl();
        	
        	// 给ImageView设置路径Tag,这是异步加载图片的小技巧
            viewHolder.mImageView.setTag(url);

            // 利用ImageLoader类加载网络图片
            bitmap = ImageLoader.getInstance().loadNetImage(url, mPoint, new NativeImageCallBack() {

                @Override
                public void onImageLoader(Bitmap bitmap, String path) {
                    ImageView mImageView = (ImageView) mListView.findViewWithTag(path);
                    if (bitmap != null && mImageView != null) {
                        mImageView.setImageBitmap(bitmap);
                    }
                }
            });
        	
        	
        }else{
        	//本地目录
        	// 给ImageView设置路径Tag,这是异步加载图片的小技巧
            viewHolder.mImageView.setTag(path);

            // 利用NativeImageLoader类加载本地图片
            bitmap = ImageLoader.getInstance().loadNativeImage(path, mPoint, new NativeImageCallBack() {

                @Override
                public void onImageLoader(Bitmap bitmap, String path) {
                    ImageView mImageView = (ImageView) mListView.findViewWithTag(path);
                    if (bitmap != null && mImageView != null) {
                        mImageView.setImageBitmap(bitmap);
                    }
                }
            });	
        }
        

        if (bitmap != null) {
            viewHolder.mImageView.setImageBitmap(bitmap);
        } else {
            viewHolder.mImageView.setImageResource(R.drawable.friends_sends_pictures_no);
        }

        return convertView;
    }

    public static class ViewHolder {
        public MyImageView mImageView;
        public TextView mTextViewTitle;
        public TextView mTextViewCounts;
    }

}
