package com.stamp20.app.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.stamp20.app.R;
import com.stamp20.app.util.FontManager;
import com.stamp20.app.util.Log;

public class AboutDetailsActivity extends Activity implements OnClickListener {

    private ImageView headerBack;
    private TextView headerTitle;
    WebView mWebView;
    String title;
    String url;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.header_previous:
            finish();
            break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_details);
        FontManager.changeFonts((LinearLayout) findViewById(R.id.root), this);
        mWebView = (WebView) findViewById(R.id.webview_details);
        headerTitle = (TextView) findViewById(R.id.header_title);
        headerBack = (ImageView) findViewById(R.id.header_previous);
        headerBack.setOnClickListener(this);
        url = getIntent().getStringExtra("url");
        title = getIntent().getStringExtra("title");
        headerTitle.setText(title);
        mWebView.loadUrl(url);
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                Log.d(this, "newProgress: " + newProgress);
                showProgressDialog(newProgress);
                super.onProgressChanged(view, newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }
        });
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
    }

    private void showProgressDialog(int progress) {
    }
}
