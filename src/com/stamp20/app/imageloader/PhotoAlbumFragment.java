package com.stamp20.app.imageloader;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.stamp20.app.R;
import com.stamp20.app.util.Log;

public class PhotoAlbumFragment extends Fragment{
    private HashMap<String, List<Uri>> mGroupMap = new HashMap<String, List<Uri>>();
    private List<ImageBean> list = new ArrayList<ImageBean>();
    private final static int SCAN_OK = 1;
    // private ProgressDialog mProgressDialog;
    private GroupAdapter adapter;
    private GridView mGroupGridView;

    private ImageView headerPrevious = null;
    private TextView headerTitle = null;
    private Context mContext;

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
            case SCAN_OK:
                // 关闭进度条
                // mProgressDialog.dismiss();

                adapter = new GroupAdapter(mContext, list = subGroupOfImage(mGroupMap), mGroupGridView);
                mGroupGridView.setAdapter(adapter);
                break;
            }
        }

    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        Log.d(this, "onCreateView");
        final View parent = inflater.inflate(R.layout.tab_photo_album, container, false);
        mGroupGridView = (GridView) parent.findViewById(R.id.main_grid);
        mGroupGridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                List<Uri> childList = mGroupMap.get(list.get(position).getFolderName());
                Intent mIntent = new Intent(mContext, ShowImageActivity.class);
                mIntent.putParcelableArrayListExtra("data", (ArrayList<Uri>) childList);
                startActivity(mIntent);
            }
        });
        
        return parent;
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity();
        getImages();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
    
    /**
     * 利用ContentProvider扫描手机中的图片，此方法在运行在子线程中
     */
    private void getImages() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(mContext, "暂无外部存储", Toast.LENGTH_SHORT).show();
            return;
        }

        // 显示进度条
        // mProgressDialog = ProgressDialog.show(this, null, "正在加载...");

        new Thread(new Runnable() {

            @Override
            public void run() {
                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver = mContext.getContentResolver();

                // 只查询jpeg和png的图片
                Cursor mCursor = mContentResolver
                        .query(mImageUri, null, MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=?",
                                new String[] { "image/jpeg", "image/png" }, MediaStore.Images.Media.DATE_MODIFIED);

                while (mCursor.moveToNext()) {
                    // 获取图片的路径
                    String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    int index = mCursor.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                    index = mCursor.getInt(index);
                    Log.d(this, "index:" + index);
                    Uri uri = Uri.parse("content://media/external/images/media/" + index);

                    // 获取该图片的父路径名
                    String parentName = new File(path).getParentFile().getName();

                    // 根据父路径名将图片放入到mGruopMap中
                    if (!mGroupMap.containsKey(parentName)) {
                        List<Uri> childList = new ArrayList<Uri>();
                        childList.add(uri);
                        mGroupMap.put(parentName, childList);
                    } else {
                        mGroupMap.get(parentName).add(uri);
                    }
                }

                mCursor.close();

                // 通知Handler扫描图片完成
                mHandler.sendEmptyMessage(SCAN_OK);

            }
        }).start();

    }

    /**
     * 组装分组界面GridView的数据源，因为我们扫描手机的时候将图片信息放在HashMap中 所以需要遍历HashMap将数据组装成List
     * 
     * @param mGruopMap
     * @return
     */
    private List<ImageBean> subGroupOfImage(HashMap<String, List<Uri>> mGruopMap) {
        if (mGruopMap.size() == 0) {
            return null;
        }
        List<ImageBean> list = new ArrayList<ImageBean>();

        Iterator<Map.Entry<String, List<Uri>>> it = mGruopMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, List<Uri>> entry = it.next();
            ImageBean mImageBean = new ImageBean(mContext);
            String key = entry.getKey();
            List<Uri> value = entry.getValue();

            mImageBean.setFolderName(key);
            mImageBean.setImageCounts(value.size());
            mImageBean.setTopImagePath(value.get(0));// 获取该组的第一张图片

            list.add(mImageBean);
        }

        return list;

    }

}
