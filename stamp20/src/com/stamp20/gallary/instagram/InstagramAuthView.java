package com.stamp20.gallary.instagram;

import org.jinstagram.auth.InstagramAuthService;
import org.jinstagram.auth.model.Token;
import org.jinstagram.auth.model.Verifier;
import org.jinstagram.auth.oauth.InstagramService;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.stamp20.app.util.FontManager;

public class InstagramAuthView extends Activity {

    private class GetTokenTask extends AsyncTask<String, Void, Token> {

        InstagramService mS;

        public GetTokenTask(InstagramService s) {
            mS = s;
        }

        @Override
        protected Token doInBackground(String... params) {
            Verifier verifier = new Verifier(params[0]);
            Token accessToken = mS.getAccessToken(null, verifier);
            return accessToken;
        }

        @Override
        protected void onPostExecute(Token result) {
            onTokenRetrieved(result.getToken());
        }

    }
    public static final String CLIENTID = "ea0e8b0e8d5d4db1b2a5d56449fe3cc4";
    private static final String CLIENTSECRET = "af2bbd2d48304b0bb0fee2b3cf3fa35a";

    private static boolean DEBUGLOG = true;
    private static final String REDIRECT_URI = "http://yourcallback.com/";
    private boolean _visible = false;

    private WebChromeClient mChromeClient;
    private WebView mWebview;

    private ProgressDialog progressDialog;

    private String extractCodeFromURL(String url) {
        return url.substring(url.indexOf("code") + 5);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final InstagramService service = new InstagramAuthService().apiKey(CLIENTID).apiSecret(CLIENTSECRET)
                .callback(REDIRECT_URI).build();

        String url = service.getAuthorizationUrl(null);

        mWebview = new WebView(this);
        mChromeClient = new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(android.webkit.ConsoleMessage consoleMessage) {
                if (DEBUGLOG)
                    Log.i("case", "InstagramAuthView-console:" + consoleMessage);
                return false;
            };

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (DEBUGLOG)
                    Log.d("case", "InstagramAuthView-onProgressChanged:" + newProgress);
            };
        };
        setContentView(mWebview);
        FontManager.changeFonts(mWebview, this);

        mWebview.setWebChromeClient(mChromeClient);

        mWebview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                if (!_visible)
                    return;

                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                    progressDialog = null;
                }
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if (!_visible)
                    return;

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

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (!_visible)
                    return false;
                if (url.startsWith(REDIRECT_URI)) {
                    String code = extractCodeFromURL(url);
                    Log.i("case", "InstagramAuthView-code=" + code);
                    new GetTokenTask(service).execute(code);
                    return true;
                }
                return false;
            }
        });

        mWebview.loadUrl(url);
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
    protected void onResume() {
        super.onResume();
        _visible = true;
    }

    private void onTokenRetrieved(String token) {
        if (DEBUGLOG)
            Log.i("case", "InstagramAUthView-token retrieved");
        Log.i("token", "token panduan0 " + token);
        InstagramTokenKeeper.writeAccessToken(this, token);
        // ParseUser.getCurrentUser().put(Setting.KEY_INSTAGRAM_TOKEN, token);
        // ParseUser.getCurrentUser().saveEventually();
        setResult(Activity.RESULT_OK);
        this.finish();
    }

}
