package com.stamp20.app;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.stamp20.app.data.Design;
import com.stamp20.gallary.features.FeaturePhoto;

public class Stamp20Application extends Application {

    static final String TAG = "Stamp20Application";

    protected static Stamp20Application instance;
    private List<Activity> mList = new LinkedList<Activity>();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "stamp 20 application start");
        // ParseObject.registerSubclass(UserProfileParse.class);
        ParseObject.registerSubclass(Design.class);
        ParseObject.registerSubclass(FeaturePhoto.class);
        Parse.initialize(this, "lrvQWDyUVWC2iNG7ZvK1PfSAKSgIQ0MWtVu6jIhf", "0V96uKLLzg2DYcCT7ndlI0HULdOh297tIP8RUk4d");
        Parse.enableLocalDatastore(this);

        ParseFacebookUtils.initialize("713170092123951");

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        instance = this;
    }

    public synchronized static Stamp20Application getInstance() {
        return instance;
    }

    // add Activity.
    public void addActivity(Activity activity) {
        mList.add(activity);
    }

    // remove all activities and exit.
    public void exit() {
        try {
            for (Activity activity : mList) {
                if (activity != null)
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }

    // when low memory, kill the app
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }
}
