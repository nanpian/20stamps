package com.stamp20.gallary;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.ImageColumns;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;

import com.stamp20.app.R;
import com.stamp20.app.data.Design;
import com.stamp20.app.util.Log;

public class PhotoAlbumFragment extends GallaryFragment implements
        OnItemClickListener, GallaryLoader {

    private Context mContext;
    private ContentResolver mContentResolver;
    private boolean isGridChildView = false;
    private ListView mAlbumsView;
    private List<Album> mAlbums;
    // private AlbumAdapter mAlbumsAdapter;
    private GridView mPhotosView;
    private List<Photo> mPhotos;
    // private PhotoAdapter mPhotosAdapter;
    private View parentView;

    private Album mCurrentAlbum;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        mContentResolver = mContext.getContentResolver();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        Log.d(this, "onCreateView");
        final View parent = inflater.inflate(R.layout.tab_photo_album,
                container, false);
        parentView = parent;
        mAlbumsView = (ListView) parent.findViewById(R.id.main_list);
        mPhotosView = (GridView) parent.findViewById(R.id.main_child_grid);
        if (isGridChildView) {
            mPhotosView.setVisibility(View.VISIBLE);
            mAlbumsView.setVisibility(View.GONE);
        } else {
            mAlbumsView.setVisibility(View.VISIBLE);
            mPhotosView.setVisibility(View.GONE);
            mAlbumsView.setOnItemClickListener(this);
        }

        return parent;
    }

    public void updateLayout(int position) {
        if (isGridChildView) {
            if (mPhotosView == null) {
                mPhotosView = (GridView) parentView
                        .findViewById(R.id.main_child_grid);
            }
            mPhotosView.setVisibility(View.VISIBLE);
            mAlbumsView.setVisibility(View.GONE);
            mCurrentAlbum = mAlbums.get(position);
            mPhotosView.setAdapter(new PhotoAdapter(mContext,
                    mPhotos = getPhotos(mCurrentAlbum)));
            mPhotosView.setOnItemClickListener(this);
        } else {
            if (mAlbumsView == null) {
                mAlbumsView = (ListView) parentView
                        .findViewById(R.id.main_list);
            }
            mPhotosView.setVisibility(View.GONE);
            mAlbumsView.setVisibility(View.VISIBLE);
            mAlbumsView.setAdapter(new AlbumAdapter(mContext,
                    mAlbums = getAlbums()));
            mAlbumsView.setOnItemClickListener(this);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // getImages();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP
                        && keyCode == KeyEvent.KEYCODE_BACK) {
                    return onBackClick();
                }

                return false;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!isGridChildView) {
            mAlbumsView.setAdapter(new AlbumAdapter(mContext,
                    mAlbums = getAlbums()));
        } else {
            mPhotosView.setAdapter(new PhotoAdapter(mContext,
                    mPhotos = getPhotos(mCurrentAlbum)));
        }

    }

    @Override
    public boolean onBackClick() {
        if (isGridChildView) {
            isGridChildView = false;
            updateLayout(0);
            return true;
        }
        return super.onBackClick();
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        isGridChildView = false;
    }

    public boolean getIsGridView() {
        return isGridChildView;
    }

    public void setIsGridView(boolean isGridView) {
        this.isGridChildView = isGridView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
        Design.clearInstance();
        if (!isGridChildView) {
            isGridChildView = true;
            updateLayout(position);
        } else {
            String sUri = mPhotos.get(position).getUri();
            Log.i("wangpeng", "photo open: " + sUri);
            GallaryUtil.goToEffectActivity(mContext, sUri);
        }
    }

    @Override
    public List<Album> getAlbums() {
        Log.i("wangpeng", "getAlbums");

        Cursor cursor = mContentResolver.query(
                Images.Media.EXTERNAL_CONTENT_URI, new String[] { "DISTINCT "
                        + ImageColumns.BUCKET_DISPLAY_NAME }, null, null,
                ImageColumns.BUCKET_DISPLAY_NAME + " ASC");
        if (cursor == null)
            return null;

        List<Album> albums = new ArrayList<Album>(10);
        while (cursor.moveToNext()) {
            String name = cursor.getString(0);
            String coverUri = this.getBucketCoverPhoto(name);
            int count = this.getBucketPhotoCount(name);
            // Log.i("wangpeng", "name= " + name + ", count= " + count +
            // ", uri= " + coverUri);
            albums.add(new Album(name, coverUri, count, name));
        }
        cursor.close();
        return albums;
    }

    private String getBucketCoverPhoto(String bucket) {
        Uri baseUri = Images.Media.EXTERNAL_CONTENT_URI;
        Uri query = baseUri.buildUpon().appendQueryParameter("limit", "1")
                .build();
        Cursor c = mContentResolver.query(query,
                new String[] { ImageColumns._ID },
                ImageColumns.BUCKET_DISPLAY_NAME + "='" + bucket + "'", null,
                ImageColumns.DATE_TAKEN + " DESC");
        if (c != null && c.moveToFirst()) {
            String uri = "content://media/external/images/media/"
                    + c.getLong(0);
            c.close();
            return uri;
        }
        return null;
    }

    private int getBucketPhotoCount(String bucket) {
        Cursor c = mContentResolver.query(Images.Media.EXTERNAL_CONTENT_URI,
                null, ImageColumns.BUCKET_DISPLAY_NAME + "='" + bucket + "'",
                null, null);
        if (c != null) {
            int count = c.getCount();
            c.close();
            return count;
        }
        return 0;
    }

    @Override
    public List<Photo> getPhotos(Album a) {
        Log.i("wangpeng", "getPhotos");

        Cursor c = mContentResolver.query(Images.Media.EXTERNAL_CONTENT_URI,
                new String[] { ImageColumns._ID, ImageColumns.DISPLAY_NAME },
                ImageColumns.BUCKET_DISPLAY_NAME + "='" + a.getContent() + "'",
                null, ImageColumns.DATE_TAKEN + " DESC");
        if (c == null)
            return null;

        List<Photo> photos = new ArrayList<Photo>(200);
        while (c.moveToNext()) {
            photos.add(new Photo(c.getString(1),
                    "content://media/external/images/media/" + c.getLong(0),
                    Photo.PHOTO_LOC_TYPE));
        }
        c.close();
        return photos;
    }

}
