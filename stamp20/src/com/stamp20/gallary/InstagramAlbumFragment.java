package com.stamp20.gallary;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.jinstagram.Instagram;
import org.jinstagram.auth.model.Token;
import org.jinstagram.entity.common.Pagination;
import org.jinstagram.entity.users.basicinfo.UserInfo;
import org.jinstagram.entity.users.basicinfo.UserInfoData;
import org.jinstagram.entity.users.feed.MediaFeed;
import org.jinstagram.entity.users.feed.MediaFeedData;
import org.jinstagram.exceptions.InstagramException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.stamp20.app.R;
import com.stamp20.app.activities.MainEffect;
import com.stamp20.app.util.Image;
import com.stamp20.app.util.Log;
import com.stamp20.app.view.MyImageView;
import com.stamp20.gallary.ChildAdapter.ViewHolder;
import com.stamp20.gallary.instagram.InstagramAuthView;
import com.stamp20.gallary.instagram.InstagramTokenKeeper;
import com.stamp20.gallary.instagram.InstagramUtils;

public class InstagramAlbumFragment extends GallaryFragment implements OnClickListener {
    // Dialog for request user to setup an account
    public class AccountRequestDialog extends DialogFragment {
        public AccountRequestDialog() {
            // empty constructor required
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Account Required");
            builder.setMessage("Please register a stampdesign account before we can connect to your Instagram.");
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setPositiveButton("Register", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Intent loginIntent = new Intent(getActivity(),
                    // LoginView.class);
                    // startActivityForResult(loginIntent,REQUEST_CODE_SELECT_INSTAGRAM_REGISTER);
                    dialog.dismiss();
                }
            });
            return builder.create();
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Intent> {

        @Override
        protected Intent doInBackground(String... urls) {
            Bitmap tmp = Image.LoadImage(urls[0]);

            if (tmp == null)
                return null;

            File file = new File(mContext.getFilesDir(), "tmp.jpg");
            if (file.exists())
                file.delete();
            try {
                FileOutputStream out = new FileOutputStream(file);
                tmp.compress(CompressFormat.JPEG, 90, out);
                out.flush();
                out.close();
                Log.d(TAG, "Image tmp file is saved");
            } catch (IOException e) {
                e.printStackTrace();
            }

            Uri uri = Uri.fromFile(file);
            Intent intent = new Intent(mContext, MainEffect.class);
            intent.putExtra("imageUri", uri);

            return intent;
        }

        @Override
        protected void onPostExecute(Intent intent) {
            progressDialog.dismiss();
            if (intent == null)
                return;
            startActivity(intent);
        }

    }
    /*
     * private void doSelectInstagramClick(){ if (getActivity()==null){ return;
     * //so it won't throw NullPointerException after user leaves app } if
     * (Setting.isUserInstagramLinked(getActivity())){ Log.i("token",
     * "token panduan3, enter instagramphoto view"); Intent instagramIntent =
     * new Intent(getActivity(),InstagramPhotosView.class);
     * startActivityForResult(instagramIntent,REQUEST_CODE_SELECT_INSTAGRAM );
     * }else{ if(Setting.isUserLogin(getActivity())){ InstagramAuthRequestDialog
     * dialogFrag = new InstagramAuthRequestDialog();
     * dialogFrag.show(getFragmentManager(), "instagram_auth_request_dialog");
     * }else{ AccountRequestDialog dialogFrag = new AccountRequestDialog();
     * dialogFrag.show(getFragmentManager(),
     * "instagram_register_account_dialog"); } } }
     */
    // Dialog for auth with instagram
    public class InstagramAuthRequestDialog extends DialogFragment {
        public InstagramAuthRequestDialog() {
            // empty constructor required
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Connect Instagram");
            builder.setMessage("Connect your Instagram account to choose photos from your Instagram.");
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setPositiveButton("Connect Now", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    doInstagramAuth();
                    dialog.dismiss();
                }
            });
            return builder.create();
        }
    }
    private class InstagramImageGridAdapter extends BaseAdapter {
        private RotateAnimation anim;
        private LayoutInflater mInflater;

        public InstagramImageGridAdapter() {
            mInflater = LayoutInflater.from(mContext);
            anim = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            anim.setInterpolator(new LinearInterpolator());
            anim.setRepeatCount(Animation.INFINITE);
            anim.setDuration(1000);
        }

        @Override
        public int getCount() {
            if (mediaFeedDataList.isEmpty() && !isLoading) {
                return 0;
            } else {
                return mediaFeedDataList.size() + 1;
            }
        }

        @Override
        public Object getItem(int position) {
            if (position == mediaFeedDataList.size()) {
                return null;
            } else {
                return mediaFeedDataList.get(position);
            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            return position < mediaFeedDataList.size() ? 0 : 1; // 0 for image,
                                                                // 1 for loading
                                                                // block
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.grid_child_item, null);
                // FontManager.changeFonts(mContext,
                // (FrameLayout)convertView.findViewById(R.id.framelayout));
                viewHolder = new ViewHolder();
                viewHolder.mImageView = (MyImageView) convertView.findViewById(R.id.child_image);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            if (getItemViewType(position) == 0) {
                // imageLoader.displayImageFromUrlLater(mediaFeedDataList.get(position).getImages().getThumbnail().getImageUrl(),
                // activity, imageView,150);
                Picasso.with(mContext).load(mediaFeedDataList.get(position).getImages().getThumbnail().getImageUrl())
                        .resize(200, 200).centerCrop().placeholder(R.drawable.friends_sends_pictures_no)
                        .into(viewHolder.mImageView);
            } else {
                ImageView imageView = viewHolder.mImageView;
                imageView.setAnimation(null);
                if (isLoading) {
                    imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    imageView.setImageResource(R.drawable.ic_loading_circle);
                    imageView.startAnimation(anim);
                } else {
                    imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    imageView.setImageResource(R.drawable.icon_load_more);
                    imageView.setAnimation(null);
                }
            }
            return convertView;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }
    }
    private class LoadRecentMediaTask extends AsyncTask<Void, Void, MediaFeed> {
        @Override
        protected MediaFeed doInBackground(Void... params) {
            try {
                if (pagination == null) {
                    return instagramClient.getRecentMediaFeed(instagramUserId);
                } else if (pagination.hasNextPage()) {
                    return instagramClient.getRecentMediaNextPage(pagination);
                } else {
                    Log.e("case", "InstagramPhotosView-doInBackground no next page?!");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(MediaFeed result) {
            isLoading = false;
            if (result == null) {
                Toast.makeText(mContext, "Oops, error while loading...Please check internet connection",
                        Toast.LENGTH_SHORT).show();
            } else {
                for (MediaFeedData mdf : result.getData()) {
                    if (mdf.getType().equals(MediaFeedData.TYPE_IMAGE) && mdf.getImages() != null) {
                        mediaFeedDataList.add(mdf);
                    }
                }
                pagination = result.getPagination();
                mHandler.sendEmptyMessage(LOAD_OK);
            }

        }

        @Override
        protected void onPreExecute() {
            isLoading = true;
            // imageAdapter.notifyDataSetChanged();
            instagramClient.setAccessToken(new Token(InstagramTokenKeeper.readAccessToken(mContext), null));
        }
    }

    private class TokenValidationTask extends AsyncTask<Void, Void, UserInfoData> {

        @Override
        protected UserInfoData doInBackground(Void... params) {
            UserInfo currentUserInfo = null;
            try {
                instagramClient.getAccessToken().getToken();
                currentUserInfo = instagramClient.getCurrentUserInfo();
            } catch (InstagramException e) {
                e.printStackTrace();
            }
            if (currentUserInfo != null && currentUserInfo.getData() != null) {
                return currentUserInfo.getData();
            }
            return null;
        }

        @Override
        protected void onPostExecute(UserInfoData result) {
            if (result == null) {
                Toast.makeText(mContext, "Instagram Authorization required", Toast.LENGTH_LONG);
                // InstagramTokenKeeper.clear(mContext);
                updateView(false);
            } else {
                instagramUserId = result.getId();
                loadMore();
            }
        }

        @Override
        protected void onPreExecute() {
            // instagramClient.setAccessToken(new
            // Token(InstagramTokenKeeper.readAccessToken(mContext),null));
        }

    }
    private final static int LOAD_OK = 1;

    private static final int REQUEST_CODE_BASE = 50;
    public static int REQUEST_CODE_SELECT_INSTAGRAM_AUTH = REQUEST_CODE_BASE + 1;
    private static String TAG = "InstagramAlbum";

    private InstagramImageGridAdapter imageAdapter;
    private Instagram instagramClient;
    private String instagramUserId;
    private boolean isLoading = false;
    private LoadRecentMediaTask loadRecentMediaTask;
    private Context mContext;
    private ArrayList<MediaFeedData> mediaFeedDataList;
    private GridView mGroupGridView;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
            case LOAD_OK:
                Log.d(TAG, "Receive Msg LOAD_OK");
                imageAdapter = new InstagramImageGridAdapter();
                mGroupGridView.setAdapter(imageAdapter);
                break;
            }
        }

    };

    private View mInstagramView;
    private Button mLogin = null;

    private Button mLogout = null;

    private View mNoLoginIcon;

    private Pagination pagination = null;

    private Dialog progressDialog;

    private int REQUEST_CODE_SELECT_INSTAGRAM = REQUEST_CODE_BASE + 2;

    private TokenValidationTask tokenValidationTask;

    private void doInstagramAuth() {
        Intent i = new Intent(getActivity(), InstagramAuthView.class);
        getActivity().startActivityForResult(i, REQUEST_CODE_SELECT_INSTAGRAM_AUTH);
    }

    private void getInsAlbums() {
        // TODO Auto-generated method stub
        instagramClient.setAccessToken(new Token(InstagramTokenKeeper.readAccessToken(mContext), null));

    }

    private void loadMore() {
        if (loadRecentMediaTask != null && loadRecentMediaTask.getStatus().equals(AsyncTask.Status.RUNNING)) {
            return;
        }
        if (isLoading) {
            return;
        }
        if (pagination != null && !pagination.hasNextPage()) {
            Toast.makeText(mContext, "You have reached the end.", Toast.LENGTH_LONG).show();
            return;
        }
        loadRecentMediaTask = new LoadRecentMediaTask();
        loadRecentMediaTask.execute();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.d(TAG, "onActivityCreated");
        super.onActivityCreated(savedInstanceState);

        // if(InstagramUtils.isUserInstagramLinked(mContext))
        // getInsAlbums();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent dataIntent) {
        if (requestCode == REQUEST_CODE_SELECT_INSTAGRAM_AUTH) {
            if (resultCode == Activity.RESULT_OK) {
                // doSelectInstagramClick();
                updateView(true);
                // getInsAlbums();
                instagramClient.setAccessToken(new Token(InstagramTokenKeeper.readAccessToken(mContext), null));
                if (instagramUserId == null) {
                    tokenValidationTask = new TokenValidationTask();
                    tokenValidationTask.execute();
                } else
                    loadMore();
            } else {
                Toast.makeText(mContext, "Instagram authorization canceled", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, dataIntent);
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
        case R.id.instagram_login_button:
            // doSelectInstagramClick();
            InstagramAuthRequestDialog dialogFrag = new InstagramAuthRequestDialog();
            dialogFrag.show(getFragmentManager(), "instagram_auth_request_dialog");
            break;
        case R.id.instagram_logout_button:
            InstagramTokenKeeper.clear(mContext);
            instagramUserId = null;
            updateView(false);
            break;
        default:
            break;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(this, "onCreate");
        super.onCreate(savedInstanceState);

        mediaFeedDataList = new ArrayList<MediaFeedData>();
        instagramClient = new Instagram(InstagramAuthView.CLIENTID);

        mContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(this, "onCreateView");

        mInstagramView = inflater.inflate(R.layout.tab_instagram_album, container, false);
        mLogin = (Button) mInstagramView.findViewById(R.id.instagram_login_button);
        mLogin.setOnClickListener(this);

        mLogout = (Button) mInstagramView.findViewById(R.id.instagram_logout_button);
        mLogout.setOnClickListener(this);

        mNoLoginIcon = mInstagramView.findViewById(R.id.ins_no_login_id);

        mGroupGridView = (GridView) mInstagramView.findViewById(R.id.main_grid);
        mGroupGridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == mediaFeedDataList.size() && !isLoading) {
                    loadMore();
                } else {
                    MediaFeedData mdf = (MediaFeedData) parent.getAdapter().getItem(position);
                    String url = null;
                    try {
                        url = mdf.getImages().getStandardResolution().getImageUrl();
                    } catch (Exception e) {
                    }
                    if (url != null) {
                        Log.d(TAG, "photo position: " + position + " url: " + url);
                        progressDialog = ProgressDialog.show(mContext, "", "Download...", true);
                        new DownloadImageTask().execute(url);
                    } else {
                        Toast.makeText(mContext, "Error loading this photo", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        return mInstagramView;
    }

    @Override
    public void onFront() {
        super.onFront();
    }

    @Override
    public void onPause() {
        if (tokenValidationTask != null && !tokenValidationTask.getStatus().equals(AsyncTask.Status.FINISHED)) {
            tokenValidationTask.cancel(false);
        }

        if (loadRecentMediaTask != null && !loadRecentMediaTask.getStatus().equals(AsyncTask.Status.FINISHED)) {
            loadRecentMediaTask.cancel(false);
        }
        isLoading = false;
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateView(InstagramUtils.isUserInstagramLinked(mContext));
        if (instagramUserId == null) {
            instagramClient.setAccessToken(new Token(InstagramTokenKeeper.readAccessToken(mContext), null));
            tokenValidationTask = new TokenValidationTask();
            tokenValidationTask.execute();
        } else
            loadMore();
    }

    private void updateView(boolean b) {
        // TODO Auto-generated method stub
        if (b) {
            // already login
            mLogin.setVisibility(View.GONE);
            mNoLoginIcon.setVisibility(View.GONE);
            mGroupGridView.setVisibility(View.VISIBLE);
            mLogout.setVisibility(View.VISIBLE);
        } else {
            mLogin.setVisibility(View.VISIBLE);
            mNoLoginIcon.setVisibility(View.VISIBLE);
            mGroupGridView.setVisibility(View.GONE);
            mLogout.setVisibility(View.GONE);
        }
    }

}
