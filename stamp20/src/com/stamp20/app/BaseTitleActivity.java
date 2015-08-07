package com.stamp20.app;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BaseTitleActivity extends Activity {

    private LinearLayout contentLayout;
    private Button mButtonLeft;
    private Button mButtonRight;
    private TextView mTitleTx;

    private void initContentView() {
        ViewGroup content = (ViewGroup) findViewById(android.R.id.content);
        content.removeAllViews();
        contentLayout = new LinearLayout(this);
        contentLayout.setOrientation(LinearLayout.VERTICAL);
        content.addView(contentLayout);
        LayoutInflater.from(this).inflate(R.layout.common_title_bar, contentLayout, true);
    }

    public void initTitleBar() {
        mTitleTx = (TextView) findViewById(R.id.titlebar_title);
        mButtonLeft = (Button) findViewById(R.id.titlebar_left);
        mButtonLeft.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("dewei", "left button cllicked");
                finish();
            }
        });
        mButtonRight = (Button) findViewById(R.id.titlebar_right);
        mButtonLeft.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                // finish();
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContentView();
        initTitleBar();
    }

    public void onCreateNoTitle() {
        super.onCreate(null);
        ViewGroup content = (ViewGroup) findViewById(android.R.id.content);
        content.removeAllViews();
        contentLayout = new LinearLayout(this);
        contentLayout.setOrientation(LinearLayout.VERTICAL);
        content.addView(contentLayout);
    }

    @Override
    public void setContentView(int layoutResID) {
        LayoutInflater.from(this).inflate(layoutResID, contentLayout, true);
    }

    @Override
    public void setContentView(View customContentView) {
        contentLayout.addView(customContentView);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitleTx.setText(title);
    }

}
