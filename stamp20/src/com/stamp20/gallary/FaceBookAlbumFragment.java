package com.stamp20.gallary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.stamp20.app.R;
import com.stamp20.app.util.Log;
import com.stamp20.gallary.facebook.FbAlbum;
import com.stamp20.gallary.facebook.FbAlbumResult;
import com.stamp20.gallary.facebook.FbAlbumStore;
import com.stamp20.gallary.facebook.FbPhoto;
import com.stamp20.gallary.facebook.FbPhotoResult;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

public class FaceBookAlbumFragment extends GallaryFragment implements OnClickListener, GallaryLoader {
    public static final String TAG = "FacebookAlbumFragment";

    private Context mContext;

    private Dialog progressDialog;
    private ParseUser currentUser;

    private boolean isLinked = false;

    private ListView mAlbumsView;
    private List<Album> mAlbums;
    private GridView mPhotosView;
    private List<Photo> mPhotos;
    private Button mFbLogin;
    private Button mFbLogout;
    private View mFBView;
    private View mFbNoLogin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        // Check if there is a currently logged in user
        // and it's linked to a FaceBook account.
        currentUser = ParseUser.getCurrentUser();
        if ((currentUser != null) && ParseFacebookUtils.isLinked(currentUser)) {
            isLinked = true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(this, "onCreateView");

        mFBView = inflater.inflate(R.layout.tab_fb_album, container, false);
        mFbNoLogin = mFBView.findViewById(R.id.fb_no_login_id);
        mFbLogin = ((Button) mFBView.findViewById(R.id.facebook_login_button));
        mFbLogin.setOnClickListener(this);
        mFbLogout = ((Button) mFBView.findViewById(R.id.facebook_logout_button));
        mFbLogout.setOnClickListener(this);

        mAlbumsView = (ListView) mFBView.findViewById(R.id.main_list);
        mAlbumsView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String parseCMD = mAlbums.get(position).getContent() + "/photos";
                Log.d(TAG, "position = " + position + "; parseCMD = " + parseCMD);

                new FbPhotosLoad().execute(parseCMD);

            }
        });

        mPhotosView = (GridView) mFBView.findViewById(R.id.main_child_grid);
        mPhotosView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = mPhotos.get(position).getUri();
                Log.d("wangpeng", "facebook position: " + position + " url: " + url);
                GallaryUtil.goToEffectAfterDownLoad(mContext, url);
            }
        });

        return mFBView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    return onBackClick();
                }

                return false;
            }
        });
    };

    @Override
    public void onResume() {
        Log.i("wangpeng", "Fb fragment onResume");
        super.onResume();
        if (isFront())
            fresh();
    }

    @Override
    public void onFront() {
        super.onFront();
        fresh();
    }

    private void fresh() {
        updateView(isLinked);
        if (isLinked)
            new FbAlbumsLoad().execute();
    }

    @Override
    public boolean onBackClick() {
        if (mPhotosView.getVisibility() == View.VISIBLE) {
            mPhotosView.setVisibility(View.GONE);
            mAlbumsView.setVisibility(View.VISIBLE);
            return true;
        }
        return super.onBackClick();
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.facebook_login_button:
            progressDialog = GallaryProgressDialog.show(mContext, true, GallaryActivity.OUT_OF_TIME, false, null);

            List<String> permissions = Arrays.asList("public_profile", "email", "user_photos");
            ParseFacebookUtils.logIn(permissions, getActivity(), new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException err) {
                    progressDialog.dismiss();
                    if (user == null) {
                        Log.d(TAG, "Uh oh. The user cancelled the Facebook login.");
                    } else {
                        Log.d(TAG, "is new user? " + user.isNew());
                        isLinked = true;
                        fresh();
                    }
                }
            });
            break;
        case R.id.facebook_logout_button:
            logout();
            break;
        default:
            break;
        }
    }

    private void updateView(boolean isLinked) {
        if (isLinked) {
            mFbLogin.setVisibility(View.GONE);
            mFbNoLogin.setVisibility(View.GONE);
            mAlbumsView.setVisibility(View.VISIBLE);
            mFBView.findViewById(R.id.facebook_logout_button).setVisibility(View.VISIBLE);

            // just for now
            mPhotosView.setVisibility(View.GONE);
        } else {
            mFbLogin.setVisibility(View.VISIBLE);
            mFbNoLogin.setVisibility(View.VISIBLE);
            mAlbumsView.setVisibility(View.GONE);
            mPhotosView.setVisibility(View.GONE);
            mFBView.findViewById(R.id.facebook_logout_button).setVisibility(View.GONE);
        }
    }

    private void logout() {
        // Log the user out
        ParseUser.logOut();
        isLinked = false;
        startImageLoaderActivity();
    }

    private void startImageLoaderActivity() {
        Intent intent = new Intent(mContext, GallaryActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private class FbAlbumsLoad extends AsyncTask<Void, Void, List<Album>> {
        Dialog progress;

        @Override
        protected void onPreExecute() {
            progress = GallaryProgressDialog.show(mContext, true, GallaryActivity.OUT_OF_TIME, false, new GallaryProgressDialog.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onTimeCancel(DialogInterface dialog) {
                    // TODO Auto-generated method stub
                    Log.e("wangpeng", "get Facebook albums out time, need add response");
                }
            });
        }

        @Override
        protected List<Album> doInBackground(Void... arg0) {

            FbAlbumResult albums = FbAlbumStore.getAlbums("me/albums");
            List<Album> as = new ArrayList<Album>(10);
            int count = albums.size();
            String name = null, uri = null, id = null;
            int photoCount = 0;
            FbAlbum album = null;
            for (int i = 0; i < count; i++) {
                album = albums.get(i);
                name = album.getTitle();
                uri = album.getCoverSourceUrl();
                photoCount = album.getCount();
                id = album.getId();
                Log.i("wangpeng", "FBAlbum name=" + name + ", count=" + photoCount + ", content=" + id + ", uri=" + uri);
                as.add(new Album(name, uri, photoCount, id));
            }

            return as;
        }

        @Override
        protected void onPostExecute(List<Album> as) {
            progress.dismiss();
            mAlbumsView.setAdapter(new AlbumAdapter(mContext, mAlbums = as));
        }

    }

    private class FbPhotosLoad extends AsyncTask<String, Void, List<Photo>> {
        Dialog progress;

        @Override
        protected void onPreExecute() {
            progress = GallaryProgressDialog.show(mContext, true, GallaryActivity.OUT_OF_TIME, false, new GallaryProgressDialog.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onTimeCancel(DialogInterface dialog) {
                    // TODO Auto-generated method stub
                    Log.e("wangpeng", "get Facebook photos out time, need add response");
                }
            });
        }

        @Override
        protected List<Photo> doInBackground(String... arg0) {

            FbPhotoResult photos = FbAlbumStore.getPhotos(arg0[0]);
            List<Photo> ps = new ArrayList<Photo>(200);
            int count = photos.size();
            String name = null, uri = null, thumb = null;
            FbPhoto p = null;
            for (int i = 0; i < count; i++) {
                p = photos.get(i);
                name = p.getId();
                uri = p.getSourceImageUrl();
                thumb = p.getThumbnailUrl();
                Log.i("wangpeng", "FBPhoto name=" + name + ", source=" + uri + ", thumbnail=" + thumb);
                ps.add(new Photo(name, uri, thumb, Photo.PHOTO_NET_TYPE));
            }

            return ps;
        }

        @Override
        protected void onPostExecute(List<Photo> ps) {
            progress.dismiss();
            mAlbumsView.setVisibility(View.GONE);
            mPhotosView.setVisibility(View.VISIBLE);
            mPhotosView.setAdapter(new PhotoAdapter(mContext, mPhotos = ps));
        }

    }

    @Override
    public List<Album> getAlbums() {
        return mAlbums;
    }

    @Override
    public List<Photo> getPhotos(Album a) {
        return mPhotos;
    }
}
