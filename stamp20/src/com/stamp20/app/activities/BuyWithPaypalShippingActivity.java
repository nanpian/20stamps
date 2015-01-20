package com.stamp20.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.stamp20.app.R;
import com.stamp20.app.util.FontManager;

public class BuyWithPaypalShippingActivity extends Activity implements View.OnClickListener{

    private ImageView headerPrevious;
    private TextView headerTitle;
    private TextView tailTitle;
    private TextView headerButton;
    private ViewGroup header;
    private ViewGroup tail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_with_paypay_shipping);
        FontManager.changeFonts((LinearLayout)findViewById(R.id.root), this);
        header = (ViewGroup) findViewById(R.id.header);
        header.setBackgroundResource(R.color.tabbar_blue_background);
        headerPrevious = (ImageView) findViewById(R.id.header_previous);
        headerPrevious.setOnClickListener(this);
        headerTitle = (TextView) findViewById(R.id.header_title);
        headerTitle.setText(R.string.shipping_title_1);
        headerButton = (TextView) findViewById(R.id.header_right);
        headerButton.setOnClickListener(this);
        headerButton.setVisibility(View.VISIBLE);
        tail = (ViewGroup) findViewById(R.id.tail);
        tail.setBackgroundResource(R.color.tabbar_blue_background);
        tail.setOnClickListener(this);
        tailTitle = (TextView) findViewById(R.id.tail_text);
        tailTitle.setText(R.string.next_review);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.header_previous:
            finish();
            break;
        case R.id.header_right:
        case R.id.tail:
            Intent reviewIntent = new Intent(this, BuyWithPaypalReviewActivity.class);
            startActivity(reviewIntent);
            break;
        }
    }
    
    private void pay(){
        
    }

}
