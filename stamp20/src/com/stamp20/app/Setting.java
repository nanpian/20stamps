package com.stamp20.app;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.util.Log;


import com.parse.ParseAnonymousUtils;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.stamp20.app.util.InstagramTokenKeeper;
import com.stamp20.app.util.StringUtils;

public class Setting {
			
	public static boolean ALL_LOG = false;
	public static boolean UPLOAD_UPON_ADD_TO_CART = true; //change this to true before release
	public static boolean PRODUCT_PRICE_INFO_ALWAYS_PROD = true; //use true to text product price info on debug build
	
	public static long SINGLE_IDEA_OBJECT_CACHE_AGE = 3*24*3600000;//3days
	public static long IDEA_QUERY_CACHE_AGE = 24*3600000;//1 day
	public static boolean ENABLE_CACHE = true;// disabled for testing
	public static long ONE_DAY_MILLI_SECONDS = 24*3600000;
	public static long THREE_DAY_MILLI_SECONDS = 72*3600000;
	public static long SEVEN_DAY_MILLI_SECONDS = 168*3600000;
	
	private static boolean ENABLE_TUTORIAL = true;//ready now
	private static boolean ENABLE_TUTORIAL_FOREVER = false;//true for testing
		
	public static boolean STRIPE_TEST = false;
	public static final String STRIPE_TEST_PUBLISH_KEY="pk_test_nJnK5UAMhwPtfAdzoj8prd5Z";
	public static final String STRIPE_LIVE_PUBLISH_KEY="pk_test_nJnK5UAMhwPtfAdzoj8prd5Z";
	
	public static final boolean LOCALYTICS_UPLOAD_AFTER_TAG_FRAG=false;
	
	public static final boolean SAVE_AS_JPG = true;
	public static final int JPG_QUALITY = 80;
	
	public static final String URL_SHIP_POLICY="http://0a2c7bf653b503b70df7-35fb4f44afd6f30a3ab87d88746a229f.r71.cf2.rackcdn.com/info_ship_return_policy.html";
	
	public static final String KEY_INSTAGRAM_TOKEN = "instagramToken";
	
	public static final String KEY_FROM_WHO = "selectphotofromwho";
	
	public static final String GCM_SENDER_ID = "497293283334";//project number from the API console
	
	public static final String USER_PRODUCTS_DEVICES_KEY = "devices";
	
	public static final boolean UseMyOwnGCM = false;//false to use parse's GCM support
	
	public static final String MAT_Advertiser_ID="16782";
	public static final String MAT_KEY="bc0dab35245126b0c50ffac73e528720";
	
	public synchronized static boolean isUserLogin(Context context){
		//ParseUser currentUser = ParseUser.getCurrentUser();
		//return currentUser!=null && !ParseAnonymousUtils.isLinked(currentUser);
		return true;
	}
		
	public synchronized static boolean isUserFacebookLinked(Context context){
		if (isUserLogin(context)){
			return ParseFacebookUtils.isLinked(ParseUser.getCurrentUser());
		}else{
			return false;
		}
	}
		
	
	public long sinceLastUpdate(Context context){
		SharedPreferences _s = PreferenceManager.getDefaultSharedPreferences(context);
		return System.currentTimeMillis()-_s.getLong("updateTime", 0);
	}
	
	public void setUpdateTime(Context context){
		SharedPreferences _s = PreferenceManager.getDefaultSharedPreferences(context);
		Editor e = _s.edit();
		e.putLong("updateTime", System.currentTimeMillis());
		e.commit();
	}
	
	
	public boolean isInOrderProcessing(Context context){
		SharedPreferences _s = PreferenceManager.getDefaultSharedPreferences(context);
		return _s.getBoolean("orderprocessing", false);
	}
	
	public void setInOrderProcessing(Context context,boolean isProcessing){
		SharedPreferences _s = PreferenceManager.getDefaultSharedPreferences(context);
		Editor e = _s.edit();
		e.putBoolean("orderprocessing", isProcessing);
		e.commit();
	}
	
	public static boolean isShowTutorial(Context context){
	  if(ENABLE_TUTORIAL){
		SharedPreferences _s = PreferenceManager.getDefaultSharedPreferences(context);
		boolean result = _s.getBoolean("showTutorial", true);
	    if (!ENABLE_TUTORIAL_FOREVER){
    		Editor e = _s.edit();
    		e.putBoolean("showTutorial", false);
    		e.commit();
	    }else{
	      result = true;
	    }
		return result;
	  }else{
		return false;
	  }
	}
	
	public static boolean isTutorialAlreadyShowed(Context context){
		SharedPreferences _s = PreferenceManager.getDefaultSharedPreferences(context);
		boolean result = _s.getBoolean("showTutorial", true);
		return !result;
	}
	
	
	public static boolean isShowGestureHelp(Context context){
	  if(ENABLE_TUTORIAL){
		SharedPreferences _s = PreferenceManager.getDefaultSharedPreferences(context);
		boolean result = _s.getBoolean("isShowGestureHelp", true);
	    if (!ENABLE_TUTORIAL_FOREVER){
    		Editor e = _s.edit();
    		e.putBoolean("isShowGestureHelp", false);
    		e.commit();
	    }else{
	      result = Math.random() > 0.3; //50% show, otherwise it stucks at this tutorial screen
	    }
		return result;
	  }else{
		return false;
	  }
	}

	public static boolean isDebuggable(Context mContext){
	  try {
		return ( 0 != ( mContext.getPackageManager().getApplicationInfo("com.pic2press.android.app", 0).flags & ApplicationInfo.FLAG_DEBUGGABLE ) );
	  } catch (NameNotFoundException e) {
		e.printStackTrace();
	  }
	  return false;
	}

	public static int getCurrentAppVersion(Context mContext){
	  try{
		  PackageInfo pInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
		  return pInfo.versionCode;
	  }catch(Exception e){
		  //Crashlytics.logException(e);
	  }
	  return 0;
	}
	
	public static int getMinAppVersion(Context mContext){
		SharedPreferences _s = PreferenceManager.getDefaultSharedPreferences(mContext);
		return _s.getInt("MinAppVersion", getCurrentAppVersion(mContext));
	}
	
	public static void setMinAppVersion(Context mContext, int latestVersion){
		SharedPreferences _s = PreferenceManager.getDefaultSharedPreferences(mContext);
		Editor e = _s.edit();
		e.putInt("MinAppVersion", latestVersion);
		e.commit();
	}

	public static boolean isUserInstagramLinked(Context mContext) {
		/*ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser!=null && !ParseAnonymousUtils.isLinked(currentUser)){
		  Log.d("case", "Setting-CurrentUserHasInstagramKey is "+currentUser.has(KEY_INSTAGRAM_TOKEN));
		  return currentUser.has(KEY_INSTAGRAM_TOKEN) && !StringUtils.isEmptyString(currentUser.get(KEY_INSTAGRAM_TOKEN).toString());
		}*/
		Log.i("token","token panduan 1");
		String token = InstagramTokenKeeper.readAccessToken(mContext);
		if(token!=null && !StringUtils.isEmptyString(token)) {
			Log.i("token", "token panduan2");
			return true;
		}
		return false;
	}
	
	
	/*
	 *Product Price JSON 
	 * -updatedTime: long
	 * -device-id: jsonObject
	 *
	 *Each Device JSON
	 *-listPrice: int cents
	 *-salePrice: int cents
	 *-saleStandardShip: int cents
	 *-promotion: String (html format)
	 * */
	
	//store product price info in sharedPreferences. Add update time
	public static void saveProductPriceJsonString(Context mContext, String productPriceInfoInJsonString){
		JSONObject result =new JSONObject();;
		try {
			result = new JSONObject(productPriceInfoInJsonString);
			result.put("updatedTime", System.currentTimeMillis());
			SharedPreferences _s = PreferenceManager.getDefaultSharedPreferences(mContext);
			Editor e = _s.edit();
			e.putString("ProductPriceJsonString", result.toString());
			e.commit();
		} catch (JSONException e) {

		}
	}
	
	//retrieve product info in a json object format if the info has not expired ( 1 day)
	public static JSONObject getProductPriceJsonObject(Context mContext){
		JSONObject result =new JSONObject();;
		SharedPreferences _s = PreferenceManager.getDefaultSharedPreferences(mContext);
		String productPriceJsonString = _s.getString("ProductPriceJsonString", "");
		if (productPriceJsonString!=null && !StringUtils.isBlank(productPriceJsonString)){
			try {
				result = new JSONObject(productPriceJsonString);
				if (result.optLong("updatedTime", 0) < (System.currentTimeMillis()-ONE_DAY_MILLI_SECONDS)){
					result = new JSONObject();
				}
			} catch (JSONException e) {

			}
		}else{
			//Crashlytics.log(Log.ERROR, "case", "Setting getProductPriceJsonObject string is empty ");
		}
		return result;
	}
	
	public static int getMultipleShippingFee(int singleFee, int count){
		float savePercent = 0.5f; //assume package is half of product
		if (singleFee > 500){
			savePercent = 0.25f; // for heavy product, package is only one quarter
		}
		return (int) Math.round(singleFee*(count-savePercent*(count-1)));
	}
	
	public static boolean supportAnimation(){
		// 4.1 and above
		return Build.VERSION.SDK_INT >= 16;// Build.VERSION_CODES.JELLY_BEAN
		
		/*windows animation is not possible due to support FragmentAcitivty doesn't support new 
		 startActivityForResult(intent, int, option) 
		 should change this when dropping 2.3
		*/
	}
	
	
	/**
	 * Gets the current registration ID for application on GCM service.
	 * <p>
	 * If result is empty, the app needs to register.
	 *
	 * @return registration ID, or empty string if there is no existing
	 *         registration ID.
	 */
//	public static String getGCMRegistrationId(Context mContext){
//		SharedPreferences _s = PreferenceManager.getDefaultSharedPreferences(mContext);
//	    String registrationId = _s.getString("GCMRegistrationId", "");
//	    if (registrationId.isEmpty()) {
//			Crashlytics.log(Log.INFO, "case", "Setting GCMRegistrationId string is empty ");
//	        return "";
//	    }
//	    // Check if app was updated; if so, it must clear the registration ID
//	    // since the existing regID is not guaranteed to work with the new
//	    // app version.
//	    int registeredVersion = _s.getInt("RegisteredAppVersion", Integer.MIN_VALUE);
//	    int currentVersion = getAppVersion(mContext);
//	    if (registeredVersion != currentVersion) {
//			Crashlytics.log(Log.DEBUG, "case", "Setting GCMRegistrationId string is empty ");
//	        return "";
//	    }
//	    return registrationId;
//	}
	/**
	 * Stores the registration ID and app versionCode in the application's
	 * {@code SharedPreferences}.
	 *
	 * @param context application's context.
	 * @param regId registration ID
	 */
//	public static void storeRegistrationId(Context mContext, String regId) {
//	    final SharedPreferences _s = PreferenceManager.getDefaultSharedPreferences(mContext);
//	    int appVersion = getAppVersion(mContext);
//	    Log.d("case", "Saving regId on app version " + appVersion);
//	    SharedPreferences.Editor editor = _s.edit();
//	    editor.putString("GCMRegistrationId", regId);
//	    editor.putInt("RegisteredAppVersion", appVersion);
//	    editor.commit();
//	}


	public static void syncCurrentAppVersion(Context mContext){
	    final SharedPreferences _s = PreferenceManager.getDefaultSharedPreferences(mContext);
	    int appVersion = getCurrentAppVersion(mContext);
	    SharedPreferences.Editor editor = _s.edit();
	    editor.putInt("CurrentAppVersion", appVersion);
	    editor.commit();
	}
	
	public static final int APP_VERSION_UNKNOWN = -1;
	public static int getRememberedAppVersion(Context mContext){
	    final SharedPreferences _s = PreferenceManager.getDefaultSharedPreferences(mContext);
	    return _s.getInt("CurrentAppVersion", APP_VERSION_UNKNOWN);
	}
	
	
	public static void saveCouponCode(Context mContext, String code){
	    final SharedPreferences _s = PreferenceManager.getDefaultSharedPreferences(mContext);
	    Editor e = _s.edit();
	    e.putString("savedCouponCode", code);
	    e.putLong("savedCouponDate", System.currentTimeMillis());
	    e.commit();
	}
	//return the saved coupon code that is saved after earliestDate (in milli seconds)
	public static String getSavedCouponCodeIfAvailable(Context mContext, long earliestDate){
	    final SharedPreferences _s = PreferenceManager.getDefaultSharedPreferences(mContext);
	    String code = _s.getString("savedCouponCode", null);
	    long time = _s.getLong("savedCouponDate", 0);
	    if (code != null && time >= earliestDate){
	    	return code;
	    }
	    return null;
	}
}
