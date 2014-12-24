package com.stamp20.app.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.stamp20.app.R;
import com.stamp20.app.util.BitmapCache;

public class ReviewActivity extends Activity implements View.OnClickListener {

    private BitmapCache mCache = null;

    private ImageView mReviewer = null;

    private ImageView headerPrevious = null;
    private TextView headerTitle = null;

    private TextView tailText = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getActionBar().hide();

        setContentView(R.layout.review_activity);

        headerPrevious = (ImageView) findViewById(R.id.header_previous);
        headerTitle = (TextView) findViewById(R.id.header_title);
        tailText = (TextView) findViewById(R.id.tail_text);

        headerPrevious.setOnClickListener(this);
        findViewById(R.id.tail).setOnClickListener(this);

        headerTitle.setText(R.string.review_title);

        mCache = BitmapCache.getCache();
        mReviewer = (ImageView) findViewById(R.id.reviewer);
        mReviewer.setImageBitmap(mCache.get());

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
        case R.id.header_previous:
            finish();
            break;
        case R.id.tail:
            break;
        default:
            break;
        }

    }
}
