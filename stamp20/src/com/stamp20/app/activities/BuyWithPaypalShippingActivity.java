package com.stamp20.app.activities;

import com.paypal.android.sdk.payments.PayPalAuthorization;
import com.stamp20.app.R;
import com.stamp20.app.util.FontManager;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BuyWithPaypalShippingActivity extends Activity implements View.OnClickListener{

    private ImageView headerPrevious;
    private TextView headerTitle;
    private Button headerButton;
    private ViewGroup header;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_with_paypay_shipping);
        header = (ViewGroup) findViewById(R.id.header);
        header.setBackgroundResource(R.color.tabbar_blue_background);
        FontManager.changeFonts((LinearLayout)findViewById(R.id.root), this);
        headerPrevious = (ImageView) findViewById(R.id.header_previous);
        headerPrevious.setOnClickListener(this);
        headerTitle = (TextView) findViewById(R.id.header_title);
        headerTitle.setText(R.string.shipping_title_1);
        headerButton = (Button) findViewById(R.id.header_button);
        headerButton.setOnClickListener(this);
        headerButton.setVisibility(View.VISIBLE);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.header_previous:
            finish();
            break;
        case R.id.header_button:
            pay();
            break;
        }
    }
    
    private void pay(){
        
    }

}
