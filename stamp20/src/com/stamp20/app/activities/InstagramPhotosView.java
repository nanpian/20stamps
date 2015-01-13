package com.stamp20.app.activities;

import java.util.ArrayList;

import org.jinstagram.Instagram;
import org.jinstagram.auth.model.Token;
import org.jinstagram.entity.common.Pagination;
import org.jinstagram.entity.users.basicinfo.UserInfo;
import org.jinstagram.entity.users.basicinfo.UserInfoData;
import org.jinstagram.entity.users.feed.MediaFeed;
import org.jinstagram.entity.users.feed.MediaFeedData;
import org.jinstagram.exceptions.InstagramException;

import com.parse.ParseUser;
import com.stamp20.app.R;
import com.stamp20.app.Setting;
import com.stamp20.app.imageloader.ImageLoader;
import com.stamp20.app.imageloader.ImageLoader2;
import com.stamp20.app.util.FontManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class InstagramPhotosView extends Activity {

	private final int REQUEST_CODE_IMAGE_DETAILS = 1;
	
	private ImageButton header_menu_btn;
	private ImageButton header_cart_btn;
	private TextView header_title;

	private TextView guideText;
	private GridView imageGrid;
	private InstagramImageGridAdapter imageAdapter;
	
	private ArrayList<MediaFeedData> mediaFeedDataList;
	private Pagination pagination=null;
	private ImageLoader2 imageLoader;
	
	private Instagram instagramClient;
	private long instagramUserId=-1;
	
	private Activity activity = this;
	private boolean isLoading=false;
	private TokenValidationTask tokenValidationTask;
	private LoadRecentMediaTask loadRecentMediaTask;
	
	//private LocalyticsSession localyticsSession;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//if(getResources().getBoolean(R.bool.portrait_only)){
	        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	    //}
		setContentView(R.layout.instagram_photos_view);
		FontManager.changeFonts((RelativeLayout)findViewById(R.id.root), this);
		
		mediaFeedDataList = new ArrayList<MediaFeedData>();
		
		imageLoader = ImageLoader2.get(this.getApplicationContext());

		//header_title = (TextView)findViewById(R.id.header_center_text);
		//header_menu_btn = (ImageButton)findViewById(R.id.header_left_button);
		//header_cart_btn = (ImageButton)findViewById(R.id.header_right_button);
		//header_title.setText("Instagram Photos");
		//header_cart_btn.setVisibility(View.INVISIBLE);
		//header_menu_btn.setImageResource(R.drawable.ic_header_back);
	/*	header_menu_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				activity.setResult(RESULT_CANCELED);
				activity.finish();
			}
		});*/

		guideText = (TextView) findViewById(R.id.instagram_photos_view_guide_text);
		imageGrid = (GridView) findViewById(R.id.instagram_photos_view_gridview);
		
		imageAdapter = new InstagramImageGridAdapter();
		imageGrid.setAdapter(imageAdapter);
		imageGrid.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
				if (position == mediaFeedDataList.size() && !isLoading){
					loadMore();
				}else{
				    MediaFeedData mdf = (MediaFeedData) parent.getAdapter().getItem(position);
				    String url = null;
				    try{
				      url = mdf.getImages().getStandardResolution().getImageUrl();
				    }catch(Exception e){
				    	if (mdf.getImages()!=null){
							  //Crashlytics.log(Log.ERROR, "case", "InstagramPhotosView-mdf.getImages() is "+mdf.getImages().toString());				    		
				    	}else{
							  //Crashlytics.log(Log.ERROR, "case", "InstagramPhotosView-mdf.getImages() is null!?");				    		
				    	}
				     // Crashlytics.logException(e);
				    }
				    if (url!=null){
						Intent i = new Intent(activity,InstagramPhotoDetailsView.class);
						//i.putExtra(InstagramPhotoDetailsView.EXTRA_IMAGE_FULL_URL, url);
						//startActivityForResult(i,REQUEST_CODE_IMAGE_DETAILS);				      
				    }else{
				      Toast.makeText(activity, "Error loading this photo", Toast.LENGTH_LONG).show();
				    }
				}
			}
		});
		
		instagramClient = new Instagram(InstagramAuthView.CLIENTID);
		instagramClient.setAccessToken(new Token(ParseUser.getCurrentUser().getString(Setting.KEY_INSTAGRAM_TOKEN),null));
		pagination = null;
		
		//locallytics tracking
		//this.localyticsSession = new LocalyticsSession(this.getApplicationContext());
		//this.localyticsSession.open();
		//this.localyticsSession.tagScreen("InstagramPhotosView");
		//this.localyticsSession.upload();

	}
	
	@Override
	public void onStart(){
	  super.onStart();
	 
	}
	
	@Override
	public void onResume(){
	  super.onResume();
	 // this.localyticsSession.open();
	  if (instagramUserId==-1){
		tokenValidationTask = new TokenValidationTask();
		tokenValidationTask.execute();
	  }else{
		loadMore();
	  }
	}
	
	@Override
	public void onPause(){

	  if (tokenValidationTask!=null && !tokenValidationTask.getStatus().equals(AsyncTask.Status.FINISHED)){
		tokenValidationTask.cancel(false);
	  }
	  if (loadRecentMediaTask!=null && !loadRecentMediaTask.getStatus().equals(AsyncTask.Status.FINISHED)){
		loadRecentMediaTask.cancel(false);
	  }
	  isLoading = false;
	  super.onPause();
	}

	private void loadMore(){
	  if (loadRecentMediaTask!=null && loadRecentMediaTask.getStatus().equals(AsyncTask.Status.RUNNING)){
		return;
	  }
	  if (isLoading){
		return;
	  }
	  if (pagination!=null && !pagination.hasNextPage()){
		Toast.makeText(activity, "You have reached the end.", Toast.LENGTH_LONG).show();
		return;
	  }
	  loadRecentMediaTask = new LoadRecentMediaTask();
	  loadRecentMediaTask.execute();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		if (requestCode == REQUEST_CODE_IMAGE_DETAILS){
/*			if (resultCode == RESULT_OK && data!=null 
					&& data.hasExtra(InstagramPhotoDetailsView.EXTRA_IMAGE_FULL_URL)){
				activity.setResult(RESULT_OK, data);
				activity.finish();
			}*/
		}else{
			super.onActivityResult(requestCode, resultCode, data);
		}
	}
	
	
	private class InstagramImageGridAdapter extends BaseAdapter{
        private LayoutInflater inflater;
        private RotateAnimation anim;
        
        public InstagramImageGridAdapter() {
            super();
            inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            anim = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF,0.5f, Animation.RELATIVE_TO_SELF,0.5f);
            anim.setInterpolator(new LinearInterpolator());
            anim.setRepeatCount(Animation.INFINITE);
            anim.setDuration(1000);
        }

		@Override
		public int getCount() {
			if(mediaFeedDataList.isEmpty() && !isLoading){
				return 0;
			}else{
				return mediaFeedDataList.size()+1;
			}
		}

		@Override
		public Object getItem(int position) {
			if(position==mediaFeedDataList.size()){
				return null;
			}else{
				return mediaFeedDataList.get(position);
		    }
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public int getViewTypeCount(){
			return 2;
		}
		
		@Override
		public int getItemViewType(int position){
			return position<mediaFeedDataList.size()?0:1; //0 for image, 1 for loading block
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (getItemViewType(position)==0){
	            ImageView imageView = null;
	            if (convertView == null) { 
	                //imageView = (ImageView)inflater.inflate(R.layout.image_search_view_grid_item, parent, false);
	            } else {
	                imageView = (ImageView) convertView;
	            }
	            imageLoader.displayImageFromUrlLater(mediaFeedDataList.get(position).getImages().getThumbnail().getImageUrl(), activity, imageView,150);            	
	            return imageView;
			}else{
	            ImageView imageView = null;
	            if (convertView == null) { 
	               // imageView = (ImageView)inflater.inflate(R.layout.image_search_view_grid_item, parent, false);
	            } else {
	                imageView = (ImageView) convertView;
	            }
	            imageView.setAnimation(null);
            	if(isLoading){
            		imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
	            	imageView.setImageResource(R.drawable.ic_loading_circle);
            		imageView.startAnimation(anim);
            	}else{
            		imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            		imageView.setImageResource(R.drawable.icon_load_more);
            		imageView.setAnimation(null);
            	}
	            return imageView;
			}
		}
	}
	
	
	private class TokenValidationTask extends AsyncTask<Void, Void, UserInfoData>{

	  @Override
	  protected void onPostExecute(UserInfoData result){
		if (result == null){
		  Toast.makeText(activity, "Instagram Authorization required", Toast.LENGTH_LONG);
		  ParseUser.getCurrentUser().remove(Setting.KEY_INSTAGRAM_TOKEN);
		  ParseUser.getCurrentUser().saveEventually();
		  activity.setResult(Activity.RESULT_FIRST_USER);//request ChooseImageFrag to redo authorizaiton
		  activity.finish();
		}else{
		  instagramUserId = result.getId();
		  guideText.setText(result.getUsername()+" recent images");
		  ParseUser.getCurrentUser().saveEventually();//token verifyied, save it!
		  loadMore();
		}
	  }

	  @Override
	  protected UserInfoData doInBackground(Void... params) {
		UserInfo currentUserInfo = null;
		try{
			 currentUserInfo = instagramClient.getCurrentUserInfo();
		}catch(InstagramException e){
			// Crashlytics.log(Log.ERROR, "case", e.getMessage());
		}
		if (currentUserInfo!=null && currentUserInfo.getData()!=null){
			return currentUserInfo.getData();
		}
		return null;
	  }
	
	}
	  
	private class LoadRecentMediaTask extends AsyncTask<Void, Void, MediaFeed>{
		@Override 
		protected void onPreExecute(){
			isLoading = true;
			imageAdapter.notifyDataSetChanged();
		}
		@Override
		protected MediaFeed doInBackground(Void... params) {
		  try{
			if (pagination == null){
			  return instagramClient.getRecentMediaFeed(instagramUserId);
			}else if (pagination.hasNextPage()){
			  return instagramClient.getRecentMediaNextPage(pagination);
			}else{
			  //Crashlytics.log(Log.ERROR, "case", "InstagramPhotosView-doInBackground no next page?!");
			}
		  }catch(Exception e){
			//Crashlytics.logException(e);
		  }
		  return null;
		}
		@Override
		protected void onPostExecute(MediaFeed result){
			isLoading = false;
			if (result==null){
				Toast.makeText(activity, "Oops, error while loading...Please check internet connection", Toast.LENGTH_SHORT).show();
			}else{
				for(MediaFeedData mdf:result.getData()){
				  if (mdf.getType().equals(MediaFeedData.TYPE_IMAGE) && mdf.getImages()!=null){
					mediaFeedDataList.add(mdf);
				  }
				}
				pagination = result.getPagination();
			}
			imageAdapter.notifyDataSetChanged();
		}
	}

}
