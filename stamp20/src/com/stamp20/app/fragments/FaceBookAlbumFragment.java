package com.stamp20.app.fragments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.stamp20.app.R;
import com.stamp20.app.activities.ImageLoaderActivity;
import com.stamp20.app.activities.ShowFBImageActivity;
import com.stamp20.app.activities.ShowImageActivity;
import com.stamp20.app.facebook.FbAlbumResult;
import com.stamp20.app.facebook.FbAlbumStore;
import com.stamp20.app.imageloader.GroupAdapter;
import com.stamp20.app.imageloader.ImageBean;
import com.stamp20.app.imageloader.UserDetailsActivity;
import com.stamp20.app.util.Log;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

public class FaceBookAlbumFragment extends Fragment implements OnClickListener{
	public static final String TAG = "FacebookAlbumFragment";
	
	private final static int LOAD_OK = 1;
	
	private Context mContext;
	
	private Dialog progressDialog;
	private ParseUser currentUser;
	
	private boolean isLinked = false;
	
    private GroupAdapter adapter;
    private GridView mGroupGridView;
    private List<ImageBean> mImageList;
    
    private View mFBView;
    
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
            case LOAD_OK:
            	Log.d(TAG,"Receive Msg LOAD_OK");
            	adapter = new GroupAdapter(mContext, mImageList, mGroupGridView);
            	mGroupGridView.setAdapter(adapter);
                break;
            }
        }

    };
    
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		// Check if there is a currently logged in user
        // and it's linked to a Facebook account.        
        currentUser = ParseUser.getCurrentUser();
        if ((currentUser != null) && ParseFacebookUtils.isLinked(currentUser)) {
        	isLinked = true;        	
        }
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        Log.d(this, "onCreateView");
        
        mFBView = inflater.inflate(R.layout.tab_fb_album, container, false);
        ((Button)mFBView.findViewById(R.id.facebook_login_button)).setOnClickListener(this);
        ((Button)mFBView.findViewById(R.id.facebook_logout_button)).setOnClickListener(this);
        
        mGroupGridView = (GridView) mFBView.findViewById(R.id.main_grid);
        mGroupGridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	// TODO
            	String parseCMD = mImageList.get(position).getFbAlbum().getId() + "/photos";
            	Log.d(TAG, "position = " + position + "; parseCMD = " + parseCMD);
            	
            	Intent intent = new Intent(getActivity(), ShowFBImageActivity.class);
            	intent.putExtra("parse_cmd", parseCMD);
            	startActivity(intent);
            }
        });
        
        updateView(isLinked);
        
        return mFBView;
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity();
        if(isLinked)
        	getFacebookAlbums();
    }
    
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
			case R.id.facebook_login_button:
				progressDialog = ProgressDialog.show(getActivity(), "", "Logging in...", true);
		        
		        List<String> permissions = Arrays.asList("public_profile", "email", "user_photos");
		        
		        ParseFacebookUtils.logIn(permissions, getActivity(), new LogInCallback() {
		          @Override
		          public void done(ParseUser user, ParseException err) {
		            progressDialog.dismiss();
		            if (user == null) {
		              Log.d(TAG, "Uh oh. The user cancelled the Facebook login.");
		            } else if (user.isNew()) {
		              Log.d(TAG, "User signed up and logged in through Facebook!");
		              //showUserDetailsActivity();
		              getFacebookAlbums();
		            } else {
		              Log.d(TAG, "User logged in through Facebook!");
		              //showUserDetailsActivity();
		              updateView(true);
		              getFacebookAlbums();
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
	
    private void getFacebookAlbums() {
    	new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				FbAlbumResult albums = FbAlbumStore.getAlbums("me/albums");
				
				mImageList = new ArrayList<ImageBean>();
				int count = albums.size();
				for(int i=0;i<count;i++){
					ImageBean tmp = new ImageBean(mContext);
										
					tmp.setFolderName(albums.get(i).getTitle());
		            tmp.setImageCounts(albums.get(i).getCount());
		            tmp.setFbAlbum(albums.get(i));
		            
					mImageList.add(tmp);
				}
				
				mHandler.sendEmptyMessage(LOAD_OK);
			}
    		
    	}).start();		
	}
    
    private void updateView(boolean isLinked){
        if(isLinked){
        	mFBView.findViewById(R.id.facebook_login_button).setVisibility(View.GONE);
        	mFBView.findViewById(R.id.fb_no_login_id).setVisibility(View.GONE);
        	mGroupGridView.setVisibility(View.VISIBLE);
        	mFBView.findViewById(R.id.facebook_logout_button).setVisibility(View.VISIBLE);
        }else{
        	mFBView.findViewById(R.id.facebook_login_button).setVisibility(View.VISIBLE);
        	mFBView.findViewById(R.id.fb_no_login_id).setVisibility(View.VISIBLE);
        	mGroupGridView.setVisibility(View.GONE);
        	mFBView.findViewById(R.id.facebook_logout_button).setVisibility(View.GONE);
        }	
    }
    
    private void logout() {
    	// Log the user out
    	ParseUser.logOut();
    	startImageLoaderActivity();
    }

    private void startImageLoaderActivity() {
    	Intent intent = new Intent(mContext, ImageLoaderActivity.class);
    	intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    	intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    	startActivity(intent);
    }
}
