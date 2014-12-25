package com.stamp20.app;

import android.app.Application;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseFacebookUtils;

public class Stamp20Application extends Application {
	static final String TAG = "Stamp20Application";

	  @Override
	  public void onCreate() {
	    super.onCreate();
	    Log.e(TAG,"stamp 20 application start");
	    
	    Parse.initialize(this, 
	        "lrvQWDyUVWC2iNG7ZvK1PfSAKSgIQ0MWtVu6jIhf",
	        "0V96uKLLzg2DYcCT7ndlI0HULdOh297tIP8RUk4d"
	    );
	    
	    ParseFacebookUtils.initialize("713170092123951");
	  }
}
