package com.stamp20.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class InstagramPhotoDetailsView extends Activity {/*
	public static final String EXTRA_IMAGE_FULL_URL = "extra_image_full_url";

	private ImageButton header_menu_btn;
	private ImageButton header_cart_btn;
	private TextView header_title;

	private Button button_use;
	private ImageView image_downloaded;
	private TextView text_details;
	private String imageURI;
	
	private ImageLoader imageLoader;
	private Activity activity = this;

	private LocalyticsSession localyticsSession;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(getResources().getBoolean(R.bool.portrait_only)){
	        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	    }
		
		setContentView(R.layout.image_search_details_view);

		imageLoader = ImageLoader.get(getApplicationContext());
		
		header_title = (TextView)findViewById(R.id.header_center_text);
		header_menu_btn = (ImageButton)findViewById(R.id.header_left_button);
		header_cart_btn = (ImageButton)findViewById(R.id.header_right_button);
		header_title.setText("Image Details");
		header_cart_btn.setVisibility(View.INVISIBLE);
		header_menu_btn.setImageResource(R.drawable.ic_header_back);
		header_menu_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				activity.setResult(RESULT_CANCELED);
				activity.finish();
			}
		});

		button_use = (Button) findViewById(R.id.image_search_details_view_button_use);
		image_downloaded = (ImageView) findViewById(R.id.image_search_details_view_image);
		text_details = (TextView) findViewById(R.id.image_search_details_view_text_link);
		
		button_use.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				doClickUse();
			}
		});
		if (getIntent()!=null&& getIntent().hasExtra(EXTRA_IMAGE_FULL_URL)){
		  imageURI = getIntent().getStringExtra(EXTRA_IMAGE_FULL_URL);
		}
		
		if (imageURI!=null){
			Bitmap cachedBitmap = imageLoader.getCachedBitmap(imageURI);
			if (cachedBitmap!=null){
				image_downloaded.setImageBitmap(cachedBitmap);
			}
			imageLoader.displayImageFromUrlLater(imageURI, this, image_downloaded,500,R.drawable.photo_downloading);
			text_details.setText("");
		}else{
			image_downloaded.setImageResource(R.drawable.error_loading);
			text_details.setText("Oops, error. Please go back.");
			button_use.setText("Back");
		}
		
		//locallytics tracking
		this.localyticsSession = new LocalyticsSession(this.getApplicationContext());
		this.localyticsSession.open();
		this.localyticsSession.tagScreen("InstagramPhotoDetailsView");
		this.localyticsSession.upload();

	}

	@Override
	public void onStart(){
		super.onStart();
		GoogleAnalytics.getInstance(getApplicationContext()).getDefaultTracker().send(MapBuilder
			  .createAppView()
			  .set(Fields.SCREEN_NAME, "InstagramPhotoDetailsView")
			  .build());
	}
	
	@Override
	public void onResume(){
	  super.onResume();
	  this.localyticsSession.open();
	}
	
	@Override
	public void onPause(){
	  this.localyticsSession.close();
	  this.localyticsSession.upload();
	  super.onPause();
	}

	private void doClickUse(){
		if (imageURI!=null){
		    GoogleAnalytics.getInstance(getApplicationContext()).getDefaultTracker().send(MapBuilder
			  .createEvent("ui_action", "button_press", "UseInstagramPhoto", null)
			  .build());
		    this.localyticsSession.tagEvent(TrackingUtil.Event_UseInstagramPhoto);
		    
			activity.setResult(RESULT_OK, new Intent().putExtra(EXTRA_IMAGE_FULL_URL, imageURI));
			activity.finish();
		}else{
			activity.setResult(RESULT_CANCELED);
			activity.finish();
		}
	}*/

}
