package com.stamp20.app.activities;


import com.parse.ParseUser;
import com.stamp20.app.*;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class InstagramAuthView extends Activity {

  private static final String REDIRECT_URI = "http://my-redirect-uri.com";
  public static final String CLIENTID = "045d2613563c455eb9882369bc661523";

  
  private WebView mWebview;
  private WebChromeClient mChromeClient;
  private ProgressDialog progressDialog;

  private boolean _visible=false;
  private static boolean DEBUGLOG = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		String url = "https://instagram.com/oauth/authorize/?client_id="+CLIENTID+
					"&redirect_uri="+REDIRECT_URI+"&response_type=token";
		
		mWebview = new WebView(this);
		mChromeClient = new WebChromeClient() {
			public boolean onConsoleMessage(android.webkit.ConsoleMessage consoleMessage) {
				if (DEBUGLOG) Log.i("case", "InstagramAuthView-console:"+consoleMessage);
				return false;
			};
			public void onProgressChanged(WebView view, int newProgress) {
				if (DEBUGLOG) Log.d("case", "InstagramAuthView-onProgressChanged:"+newProgress);
			};
		};
		setContentView(mWebview);
		
		mWebview.setWebChromeClient(mChromeClient);

		mWebview.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if(!_visible) return false;

				if (url.startsWith(REDIRECT_URI)) {
					String code = extractCodeFromURL(url);
					Log.i("case", "InstagramAuthView-code="+code);
					onTokenRetrieved(code);
					return true;
				}
				return false;
			}
			@Override
			public void onPageFinished(WebView view, String url)
			{
				if(!_visible) return;
				
				if(progressDialog != null && progressDialog.isShowing())
				{
					progressDialog.dismiss();
					progressDialog = null;
				}
			}
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				if(!_visible) return;

				if (progressDialog != null && progressDialog.isShowing()) {
					progressDialog.dismiss();
					progressDialog = null;
				}
				progressDialog = ProgressDialog.show(InstagramAuthView.this, "", "Loading...", true);
				progressDialog.setCanceledOnTouchOutside(false);
				progressDialog.setCancelable(true);
				progressDialog.setOnCancelListener(new OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog) {
						dialog.cancel();
						InstagramAuthView.this.finish();
					}
				});
				super.onPageStarted(view, url, favicon);
			}
		});

		mWebview.loadUrl(url);
	}

	private String extractCodeFromURL(String url) {
		return url.substring(url.indexOf("access_token=")+13);
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	protected void onPause() {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
			progressDialog = null;
		}
		_visible = false;
		finish();
		super.onPause();
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		_visible = true;
	}
	
	private void onTokenRetrieved(String token){
	  if (DEBUGLOG) Log.i("case", "InstagramAUthView-token retrieved");
	  ParseUser.getCurrentUser().put(Setting.KEY_INSTAGRAM_TOKEN, token);
	  ParseUser.getCurrentUser().saveEventually();
	  setResult(Activity.RESULT_OK);
	  this.finish();
	}

}
